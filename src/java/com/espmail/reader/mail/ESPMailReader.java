package com.espmail.reader.mail;

import com.espmail.broker.modelo.*;
import com.espmail.broker.generacion.Link;

import com.espmail.reader.Reader;
import com.espmail.reader.ReaderMBean;
import com.espmail.reader.ReaderState;
import com.espmail.utils.dao.FactoriaDao;
import com.espmail.utils.dao.DaoException;
import com.espmail.utils.dao.Transaccion;
import com.espmail.utils.contexto.Contexto;
import com.espmail.util.ESPMailTranslator;
import com.espmail.util.TranslatorException;
import com.espmail.util.Translator;
import com.espmail.util.mail.LauncherState;
import com.espmail.util.mail.EMailState;
import com.espmail.launcher.LaunchException;
import com.espmail.launcher.Launcher;
import com.espmail.launcher.mail.MailLauncher;
import com.espmail.message.mail.Email;
import com.espmail.lanzador.Lanzador;
import com.espmail.log.Logger;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.AddressException;
import javax.management.*;
import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.io.UnsupportedEncodingException;

/**
 * ESPMail
 * User: Luis
 */
public class ESPMailReader extends Reader implements NotificationBroadcaster {

	protected final static String QUERY = "select usuarios.id_email, usuarios.id_lista, email, nombre, apellidos,"
			+ " tratamiento, etiqueta_t1, t1, etiqueta_t2, t2,etiqueta_t3, t3"
			+ " from usuarios, envios_listas, listas, enviados"
			+ " where usuarios.id_lista = envios_listas.id_lista"
			+ " and enviados.id_lista = usuarios.id_lista"
			+ " and enviados.id_email = usuarios.id_email"
			+ " and enviados.id_envio = envios_listas.id_envio"
			+ " and enviados.estado = 'P'"
			+ " and listas.id_lista = usuarios.id_lista and envios_listas.id_envio = ";
	protected final static String QUERY_FINAL = " order by id_email asc";

	private ESPMailTranslator html;
	private ESPMailTranslator asunto;
	private ESPMailTranslator texto;

	private int sequenceNumber = 1;

	private long idEmailInicio = -1l;

	private Envio envio;

	private List badEmails;

	private InternetAddress[] replyTo;
	private InternetAddress remitente;

	private int enviados = 0;

	private ReaderState estado;
	private NotificationBroadcasterSupport broadcaster;
	private Logger log = new Logger(true, ESPMailReader.class);

	/**
         *
         */
	public ESPMailReader() {
		super();
		this.estado = ReaderState.LISTO;
		this.broadcaster = new NotificationBroadcasterSupport();
	}

	/**
	 * 
	 * @param envio
	 * @throws LaunchException
	 */
	public ESPMailReader(Envio envio) throws LaunchException {
		super();
		this.estado = ReaderState.LISTO;
		this.broadcaster = new NotificationBroadcasterSupport();
		this.envio = envio;
	}

	/**
         *
         * @throws LaunchException
         */
	public void init() throws LaunchException {

		estado = ReaderState.ENVIANDO;

		try {
			getCampanha(true);
		} catch (MailReaderException e) {
			throw new LaunchException(e);
		}

		try {
			FactoriaDao factoria = FactoriaDao.getInstance();
			ClienteDao clienteDao = (ClienteDao) factoria
					.getDao(ClienteDao.class);
			Cliente cliente = clienteDao.findById(envio.getIdCliente());

			try {
				this.remitente = new InternetAddress(cliente.getRemitente(),
						envio.getRemitente(), "iso-8859-1");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			this.replyTo = InternetAddress.parse(envio.getReplyTo());
			try {
				this.asunto = getPlantilla(false);
			} catch (TranslatorException e) {
				throw new LaunchException(e);
			}

			try {
				this.html = getPlantilla(true);
			} catch (TranslatorException e) {
				throw new LaunchException(e);
			}
			this.html.setPie(cliente.getPieHtml());

			try {
				this.texto = getPlantilla(false);
			} catch (TranslatorException e) {
				throw new LaunchException(e);
			}
			this.texto.setPie(cliente.getPieTexto());

			ListaDao listaDao = (ListaDao) factoria.getDao(ListaDao.class);
			Lista[] listas = listaDao.findByEnvio(envio.getIdCliente(),
					this.envio.getIdEnvio());

			if (listas != null) {
				for (int i = 0; i < listas.length; i++) {
					Lista lista = listas[i];

					html.addEtiqueta(lista.getEtiqueta1());
					html.addEtiqueta(lista.getEtiqueta2());
					html.addEtiqueta(lista.getEtiqueta3());

					this.texto.addEtiqueta(lista.getEtiqueta1());
					this.texto.addEtiqueta(lista.getEtiqueta2());
					this.texto.addEtiqueta(lista.getEtiqueta3());

					asunto.addEtiqueta(lista.getEtiqueta1());
					asunto.addEtiqueta(lista.getEtiqueta2());
					asunto.addEtiqueta(lista.getEtiqueta3());
				}
			}
		} catch (DaoException e) {
			throw new LaunchException(e);
		} catch (AddressException e) {
			throw new LaunchException(e);
		}

	}

	/**
         *
         * @param bbdd
         * @throws MailReaderException
         */
	public void getCampanha(boolean bbdd) throws MailReaderException {
		try {
			EnvioDao dao = (EnvioDao) FactoriaDao.getInstance().getDao(
					EnvioDao.class);
			
			String maquina = "";
			try {
				maquina = InetAddress.getLocalHost().getHostName();
				try {
					String separar[] = maquina.split(".");
					maquina = separar[0];
				} catch (Exception e) {
				}				
			} catch (Exception ex) {
				log.error("Fallo al obtener el nombre de la maquina, se establece a nula");
			}
			
			
			
			Integer[] idsEnvio = dao.findListosEjecucion(maquina);

			if (idsEnvio == null){
				
				
				log.debug("There isn�t campaigns");

				Notification ntfctn = new Notification(Lanzador.MAX_REACHED, this,
						sequenceNumber++, "There isn�t campaigns");
				this.broadcaster.sendNotification(ntfctn);
				throw new MailReaderException("There isn't campaigns");
			}

			for (int i = 0; i < idsEnvio.length; i++) {
				Transaccion transaction = new Transaccion();			

				String maquinaEnvio= "";
				try{
					maquinaEnvio = dao.findMaquinaEnvio(idsEnvio[i].intValue());
				}catch (DaoException e){
					maquinaEnvio="";					
				}
				
				//Si tiene maquina
				try{
				if (!maquinaEnvio.equals("")){
					//Si es otra maquina	
					
					if (!maquinaEnvio.equals(maquina)){										
						transaction.commit();
						transaction.close();
						continue;												
					}					
				}
				}catch(Exception ex){}
				
				
				try {
					int idEnvio = idsEnvio[i].intValue();
					this.envio = dao.findByIdForUpdate(idEnvio);
					log.debug("Envio seleccionado " + envio.getIdEnvio());

					if (this.envio.getEstado() == EstadoEnvio.LISTO) {
						
						dao.cambiaEstadoMaquina(idEnvio, EstadoEnvio.ENVIANDO,
								maquina);
						transaction.commit();
					} else if (maquina.equals(maquinaEnvio) && this.envio.getEstado() == EstadoEnvio.ENVIANDO){
						dao.cambiaEstadoMaquina(idEnvio, EstadoEnvio.ENVIANDO,
								maquina);
						transaction.commit();
					}else{// no esta libre
						transaction.commit();
						transaction.close();
						continue;
					}

					// -------------------

					// dao.cambiaEstado(idEnvio, EstadoEnvio.ENVIANDO);
					// transaction.commit();

					break;
				} catch (DaoException e) {
					log.error("Dao Error, se procede al roollback");
					e.printStackTrace();
					transaction.rollback();
				} catch (Exception ex) {
					log.error("Error en el dao");
				}finally{
					transaction.close();
				}
			}
		} catch (DaoException e) {

			throw new MailReaderException(e);
		} catch (Throwable t) {
			throw new MailReaderException(t);
		}
	}

	/**
         *
         */
        public void run() {

		 try {
	         init();
	      } catch (LaunchException e) {
	         estado = ReaderState.LISTO;
	         this.broadcaster.sendNotification(new Notification(Lanzador.ERROR_LISTING,this,0,"There isn't campaigns!!"));
	         return;
	      }


		try {
			Connection con = null;

			con = Contexto.getInstance().getConexion();

			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(QUERY + envio.getIdEnvio()
					+ QUERY_FINAL);

			int contadorActualizacion = 0;
			List etiquetas = this.html.getEtiquetas();
			String[] valores = new String[etiquetas.size()];
			Link linkMemCamp = new Link(this.envio.getIdEnvio());
			Link linkBaja = new Link(this.envio.getIdEnvio(), 'B', 1,getUrlBaja());
			Link linkLectura = new Link(this.envio.getIdEnvio(), 'L');
			Email emailActivo = null;
			long tiempoEnvio = System.currentTimeMillis();

			
			int aux =0;
			while (rs.next() && !isStop()) {
			
				emailActivo = new Email();

				if (enviados % 1000 == 0) {
					log.info("Enviados:"+ idEmailInicio+" a "+rs.getLong("id_email"));
					log.info("Enviados:"+ aux+"-"+rs.getRow()+"= "+(rs.getRow()-aux));
					
					if (enviados > 0) {
						actualizaEnviados(idEmailInicio,rs.getLong("id_email"), envio.getIdEnvio(),	EMailState.ENVIADO);
					}
					aux = rs.getRow();
					idEmailInicio = rs.getLong("id_email");
				}

				/*
				 * Set email idemail idlista y traducir
				 */

				try {
					emailActivo.setFrom(this.remitente);
					emailActivo.setTo(InternetAddress.parse(rs
							.getString("email")));
					emailActivo.setReplyTo(this.replyTo);
					emailActivo.setIdEmail(rs.getLong("id_email"));
					emailActivo.setIdLista(rs.getInt("id_lista"));
					emailActivo.setIdEnvio(this.envio.getIdEnvio());

					//log.debug("Procesamiento email: " + emailActivo.getTo()[0]);

					valores[0] = linkMemCamp.genera(emailActivo.getIdEmail(),
							emailActivo.getIdLista());
					valores[1] = linkBaja.genera(emailActivo.getIdEmail(),
							emailActivo.getIdLista());
					valores[2] = linkLectura.genera(emailActivo.getIdEmail(),
							emailActivo.getIdLista());
					valores[3] = emailActivo.getTo()[0].toString();
					valores[4] = rs.getString(4);
					valores[5] = rs.getString(5);
					valores[6] = rs.getString(6);

					for (int i = 7; i < 13; i += 2) {
						String etiqueta = rs.getString(i);

						if (etiqueta != null) {
							for (int j = 7; j < valores.length; j++) {
								if (etiqueta.equals(etiquetas.get(j))) {
									valores[j] = rs.getString(i + 1);
									break;
								}
							}
						}
					}

					this.html.setValores(valores);
					this.texto.setValores(valores);
					this.asunto.setValores(valores);

					this.html.setIdEmail(emailActivo.getIdEmail());
					this.texto.setIdEmail(emailActivo.getIdEmail());
					this.asunto.setIdEmail(emailActivo.getIdEmail());

					this.html.setIdLista(emailActivo.getIdLista());
					this.texto.setIdLista(emailActivo.getIdLista());
					this.asunto.setIdLista(emailActivo.getIdLista());

					this.asunto.setCabeceras(false);

					emailActivo.setMessageHTML(((Email) this.html.translate(
							new StringBuffer(envio.getHtml()), valores))
							.getMessageHTML().toString());
					emailActivo.setMessageTXT(((Email) this.texto.translate(
							new StringBuffer(envio.getTexto()), valores))
							.getMessageTXT());
					emailActivo.setSubject(((Email) this.asunto.translate(
							new StringBuffer(envio.getAsunto()), valores))
							.getMessageHTML().toString());

				} catch (TranslatorException e) {
					e.printStackTrace();
					List malo = new ArrayList(0);
					malo.add(emailActivo);
					Lanzador.getInstance().addBad(this.getClass(), malo);
					throw new LaunchException(e);
				} catch (AddressException e) {
					e.printStackTrace();
					List malo = new ArrayList(0);
					malo.add(emailActivo);
					Lanzador.getInstance().addBad(this.getClass(), malo);
					continue;
				}

				Launcher lanzador = null;
				try {
					lanzador = getLauncher(this.getClass());

				} catch (LaunchException e) {

					Notification ntfctn = new Notification(
							Lanzador.NOT_LAUNCHER, this, sequenceNumber++,
							"Not Launcher");
					this.broadcaster.sendNotification(ntfctn);
					e.printStackTrace();
					break;

				}
				

				assert ((MailLauncher) lanzador) != null;				

				((MailLauncher) lanzador).envia(emailActivo);

				new Thread(lanzador).start();
				enviados++;
				
				log.info("RS/Enviados:"+rs.getRow()+"/"+enviados);

			}
			log.error("Al salir del while.Enviados: " + enviados);

			try {
				actualizaEnviados(idEmailInicio, emailActivo.getIdEmail(),
						envio.getIdEnvio(), EMailState.ENVIADO);
			} catch (NullPointerException e) {
				log.error("Nothing to send");
			}

			finaliza(envio.getIdEnvio());

			// estado=ReaderState.LISTO;

			log.debug("Notificamos el Fin de la Tanda");

			Notification ntfctn = new Notification(Lanzador.MAX_REACHED, this,
					sequenceNumber++, "Fin de la tanda de envio");
			this.broadcaster.sendNotification(ntfctn);

		} catch (SQLException e) {
			if (e.getErrorCode() == 1555)
				log.error("el resultset caduc�");

		} catch (LaunchException e) {
			e.printStackTrace();
		} catch (DaoException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param html
	 * @return
	 * @throws TranslatorException
	 */
	public ESPMailTranslator getPlantilla(boolean html)
			throws TranslatorException {
		ESPMailTranslator traductor = new ESPMailTranslator(html);

		return traductor;
	}

	/**
	 * 
	 * @param idEmailInicio
	 * @param idEmailFin
	 * @param idEnvio
	 * @param estado
	 * @throws DaoException
	 */
	public void actualizaEnviados(long idEmailInicio, long idEmailFin,
			int idEnvio, EMailState estado) throws DaoException {

		if (!(idEmailFin > 0))
			return;// no hay rango superior

		try {
			transaccion = new Transaccion();

			UsuarioDao usuario = (UsuarioDao) FactoriaDao.getInstance().getDao(
					UsuarioDao.class);
			usuario.actualizaEnviados(idEmailInicio, idEmailFin, idEnvio,
					estado.getCodigo());

			transaccion.commit();
		} catch (DaoException e) {
			log.error("ERror in update->rollback");
			transaccion.rollback();
		}finally{
			transaccion.close();
		}
	}

	/**
	 * 
	 * @param idEnvio
	 * @throws LaunchException
	 */
	public void finaliza(int idEnvio) throws LaunchException {
		log.debug("Finaliza Enviados = :" + enviados);
		if (!(enviados > 0)) {
			finaliza(idEnvio, EstadoEnvio.ENVIADO);
			log.debug(idEnvio + " Actualizado a Enviado");
		} else {
			finaliza(idEnvio, EstadoEnvio.LISTO);
			log.debug(idEnvio + " Actualizado a Listo");
		}
	}

	/**
	 * 
	 * @param idEnvio
	 * @param estado
	 * @throws LaunchException
         *
	 */	
	  public void finaliza(int idEnvio, EstadoEnvio estado) throws LaunchException{
	      try {
	         transaccion = new Transaccion();
	         EnvioDao dao = (EnvioDao) FactoriaDao.getInstance().getDao(EnvioDao.class);
				dao.cambiaEstadoMaquina(idEnvio, estado,"");
	         transaccion.commit();
	      }catch (DaoException e) {	         
	    	  try{
	         log.error("ERror in update->rollback");
	         transaccion.rollback();	         	            	         
	         throw new LaunchException(e);
	    	  }catch(Exception ex){}
	      }finally{
	    	  transaccion.close();
	    	  enviados = 0;
		      this.estado = ReaderState.LISTO;
		 }
	   }   


	public ReaderState getState() {
		return this.estado;
	}

	public void removeNotificationListener(NotificationListener listener)
			throws ListenerNotFoundException {
		this.broadcaster.removeNotificationListener(listener);
	}

	public void addNotificationListener(NotificationListener listener,
			NotificationFilter filter, Object handback) {
		this.broadcaster.addNotificationListener(listener, filter, handback); 
	}

	public void sendNotification(Notification notification) {
		this.broadcaster.sendNotification(notification);

	}

	public String getReaderState() {
		return "E: Error<br>S: Enviando<br>L: Listo<br>Estado:"
				+ estado.getCodigo();
	}

	public Translator getTranslator() {
		return new ESPMailTranslator();
	}
/**
 * 
 * @return
 */
   protected String getUrlBaja(){

      try {
         ClienteDao dao = (ClienteDao) FactoriaDao.getInstance().getDao(ClienteDao.class);
         Cliente cli = dao.findById(envio.getIdCliente());
         return (cli!=null ? cli:new Cliente()).getUrlBaja();
      } catch (DaoException e) {
         return null;
      }

   }
}
