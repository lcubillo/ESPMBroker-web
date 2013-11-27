package com.espmail.broker.generacion;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.espmail.broker.AppContexto;

import com.espmail.broker.modelo.Cliente;
import com.espmail.broker.modelo.ClienteDao;
import com.espmail.broker.modelo.Envio;
import com.espmail.broker.modelo.Lista;
import com.espmail.broker.modelo.ListaDao;

import com.espmail.utils.contexto.Contexto;

import com.espmail.utils.dao.DaoException;
import com.espmail.utils.dao.FactoriaDao;

public abstract class GeneradorEnvio extends LoggerSupport implements Runnable {

	private static final String HEAD_HTML = "<div><img src=\"{LINK_LECTURA}\""
		+ " height=\"1\" width=\"1\">Si no puede ver correctamente el mensaje,"
		+ " haz clic <a href=\"{LINK_MEMCAMP}\">aqu&iacute;</a></div>";
	
	private static final String HEAD_TEXTO = "Si no puede ver correctamente el mensaje,"
		+ " copie este enlace {LINK_MEMCAMP} en el navegador\n";

	private final int idEnvio;
   private final String idCliente;
   
   private final InternetAddress remitente;
	private final InternetAddress[] replyTo;
	private final Plantilla plantillaAsunto;
	private final Plantilla plantillaHtml;
	private final Plantilla plantillaTexto;
	private final List etiquetas;
	private final Properties sessionProps;
	private final boolean mailDebug;
	private boolean stop = false;
	protected long tiempoSession = 0;
	protected long tiempoComposicion = 0;
	protected long tiempoSend = 0;
	
	public GeneradorEnvio(boolean mbean, Envio envio) throws GeneradorException {
		super(mbean);
      this.idCliente = envio.getIdCliente();
      this.idEnvio = envio.getIdEnvio();
		this.etiquetas = new ArrayList();
		this.etiquetas.add("LINK_MEMCAMP");
		this.etiquetas.add("LINK_BAJA");
		this.etiquetas.add("LINK_LECTURA");
		this.etiquetas.add("EMAIL");
		this.etiquetas.add("NOMBRE");
		this.etiquetas.add("APELLIDOS");
		this.etiquetas.add("TRATAMIENTO");
		
		this.sessionProps = new Properties();
		this.sessionProps.setProperty("mail.smtp.host",
				Contexto.getPropiedad(AppContexto.MAIL_SERVER));
		this.mailDebug = Boolean.getBoolean(Contexto
				.getPropiedad(AppContexto.MAIL_DEBUG));

		try {
			debug("Iniciando GeneradorEnvio " + idEnvio);
			FactoriaDao factoria = FactoriaDao.getInstance();
			ClienteDao clienteDao = (ClienteDao) factoria.getDao(ClienteDao.class);
			Cliente cliente = clienteDao.findById(envio.getIdCliente());
			ListaDao listaDao = (ListaDao) factoria.getDao(ListaDao.class);
			Lista[] listas = listaDao.findByEnvio(envio.getIdCliente(), this.idEnvio);
			
			if (listas != null) {
				for (int i = 0; i < listas.length; i++) {
					Lista lista = listas[i];
					addEtiqueta(lista.getEtiqueta1());
					addEtiqueta(lista.getEtiqueta2());
					addEtiqueta(lista.getEtiqueta3());
				}
			}

			this.remitente = new InternetAddress(envio.getRemitente() + "<" + cliente.getRemitente() + ">");
			this.replyTo = InternetAddress.parse(envio.getReplyTo());
			this.plantillaAsunto = getPlantilla(envio.getAsunto());
			
			String html = envio.getHtml();
			String htmlMin = html.toLowerCase();
			int indexBody = htmlMin.indexOf('>', htmlMin.indexOf("<body")) + 1;
			int indexEndBody = htmlMin.indexOf("</body>", indexBody);
			
			if (indexBody == -1 || indexEndBody == -1) {
				throw new GeneradorException("El mail no tiene las tags de body");
			}

			StringBuffer sb = new StringBuffer(html.substring(0, indexBody));
			sb.append(HEAD_HTML);
			sb.append(html.substring(indexBody, indexEndBody));
			sb.append(cliente.getPieHtml());
			sb.append(html.substring(indexEndBody));
			this.plantillaHtml = getPlantilla(sb.toString());
			
			String texto = HEAD_TEXTO + envio.getTexto() + cliente.getPieTexto();
			this.plantillaTexto = getPlantilla(texto);
		} catch (DaoException e) {
			throw new GeneradorException(e);
		} catch (PlantillaException e) {
			throw new GeneradorException(e);
		} catch (AddressException e) {
			throw new GeneradorException(e);
		}
	}

	public final int getIdEnvio() {
		return this.idEnvio;
	}

	public final void stop() {
		this.stop = true;
	}

	public final void run() {
		debug("Vamos a empezar a enviar " + this.idEnvio);
      debug("Query de seleccion = "+getQuery());
      Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		boolean restart = true;
		Exception exception = null;
		
		while (restart) {
			restart = false;

			try {
				con = Contexto.getInstance().getConexion();
				st = con.createStatement();
				rs = st.executeQuery(getQuery());
				List etiquetas = this.etiquetas;
				int length = etiquetas.size();
				Link linkMemCamp = new Link(this.idEnvio);
				Link linkBaja = new Link(this.idEnvio, 'B',1, getUrlBaja());
				Link linkLectura = new Link(this.idEnvio, 'L');
	
				while (!this.stop && rs.next()) {
					long idEmail = rs.getLong(1);
					int idLista = rs.getInt(2);
					String email = rs.getString(3);
					debug("Procesando " + email);

               String[] valores = new String[length];
					valores[0] = linkMemCamp.genera(idEmail, idLista);
					valores[1] = linkBaja.genera(idEmail, idLista);
					valores[2] = linkLectura.genera(idEmail, idLista);
					valores[3] = email;
					valores[4] = rs.getString(4);
					valores[5] = rs.getString(5);
					valores[6] = rs.getString(6);
					
					for (int i = 7; i < 13; i+=2) {
						String etiqueta = rs.getString(i);
						
						if (etiqueta != null) {
							for (int j = 7; j < length; j++) {
								if (etiqueta.equals(etiquetas.get(j))) {
									valores[j] = rs.getString(i + 1);
									break;
								}
							}
						}
					}
	
					envia(idEmail, idLista, email,
							this.plantillaAsunto.genera(idEmail, idLista, valores),
							this.plantillaHtml.genera(idEmail, idLista, valores),
							this.plantillaTexto.genera(idEmail, idLista, valores));
				}
			} catch (SQLException e) {
				if (e.getErrorCode() == 1555) {
					// Error provocado por tener el cursor abierto demasiado tiempo.
					restart = true;
				} else {
					exception = e;
					error(e);
				}
			} catch (GeneradorException e) {
				exception = e;
				error(e);
			} finally {
				if (rs != null) {
					try { rs.close(); } catch (SQLException e2) { } 
				}
	
				if (st != null) {
					try { st.close(); } catch (SQLException e2) { } 
				}
	
				if (con != null) {
					try { con.close(); } catch (SQLException e2) { } 
				}

				if (!restart) {
					try {
						debug("Finalizando");

						if (exception != null) {
							Properties props = new Properties();
							props.setProperty("mail.smtp.host",
									Contexto.getPropiedad(AppContexto.MAIL_SERVER));
							Session session = Session.getInstance(props, null);
							MimeMessage msg = new MimeMessage(session);
							msg.setFrom(new InternetAddress("sistemas@corp.aspmail.com"));
							msg.setSubject("Error en envio " + this.idEnvio, "iso-8859-1");
							msg.setSentDate(new Date());
							msg.setRecipients(Message.RecipientType.TO,
									InternetAddress.parse("sistemas@corp.espmail.com"));
							msg.setText(exception.toString(), "iso-8859-1");
							Transport.send(msg);
						}

						finaliza(exception == null);
					} catch (GeneradorException e) {
						error(e);
					} catch (AddressException e) {
						error(e);
					} catch (MessagingException e) {
						error(e);
					}
				}
			}
		}
	}
	
	protected final void sendMail(long idEmail, int idLista, String email,
			String asunto, String html, String texto) throws MessagingException {
		debug("Enviando correo a " + email);
		String envioEmail =  this.idEnvio + "." + idEmail + ".";
		this.sessionProps.put("mail.smtp.from", "bounces." + envioEmail 
				+ idLista + "@espmail");

		long tiempoSessionAux = System.currentTimeMillis(); 
		Session session = Session.getInstance(this.sessionProps, null);
		session.setDebug(this.mailDebug);
		this.tiempoSession += System.currentTimeMillis() - tiempoSessionAux; 
		
		long tiempoComposicionAux = System.currentTimeMillis();
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(this.remitente);
		msg.setSubject(asunto, "iso-8859-1");
		msg.setSentDate(new Date());
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
		msg.setReplyTo(this.replyTo);
		msg.setHeader("list-unsubscribe", "<mailto:list-unsubscribe." + envioEmail
				+ idLista + "@espmail>");
		//TODO A largo plazo habra que definir un x-clase por cada cliente
        msg.addHeader("X-clase","Y");

		if (html == null) {
			msg.setText(texto, "iso-8859-1");
		} else {
			MimeBodyPart mbp1 = new MimeBodyPart();
			mbp1.setContent(html, "text/html");
			Multipart mp = null;

			if (texto == null) {
				mp = new MimeMultipart();
			} else {
				mp = new MimeMultipart("alternative");
				MimeBodyPart mbp2 = new MimeBodyPart();
				mbp2.setContent(texto, "text/plain");
				mp.addBodyPart(mbp2);
			}

			mp.addBodyPart(mbp1);
			msg.setContent(mp);
		}
		this.tiempoComposicion += System.currentTimeMillis() - tiempoComposicionAux;
		
		long tiempoSendAux = System.currentTimeMillis();
		Transport.send(msg);
		this.tiempoSend += System.currentTimeMillis() - tiempoSendAux;
	}   

   protected void finaliza(boolean exito) throws GeneradorException {
		// Nada
	}

	protected abstract String getQuery();
	
	protected abstract void envia(long idEmail, int idLista, String email,
			String asunto, String html,	String texto) throws GeneradorException;
	
	private void addEtiqueta(String etiqueta) {
		if (etiqueta == null) {
			return;
		}

		List etiquetas = this.etiquetas;
		int length = etiquetas.size();
		
		for (int i = 4; i < length; i++) {
			if (etiqueta.equals(etiquetas.get(i))) {
				return;
			}
		}
		
		etiquetas.add(etiqueta);
	}

   protected String getUrlBaja(){

      try {
         ClienteDao dao = (ClienteDao) FactoriaDao.getInstance().getDao(ClienteDao.class);
         Cliente cli = dao.findById(this.idCliente);
         return (cli!=null ? cli:new Cliente()).getUrlBaja();
      } catch (DaoException e) {
         return null;
      }

   }

   private Plantilla getPlantilla(String texto) throws PlantillaException {
		return texto != null? new Plantilla(this.idEnvio, texto, this.etiquetas) : null;
	}
}
