package com.espmail.util;

import com.espmail.message.ESPMailMessage;
import com.espmail.message.mail.Email;
import com.espmail.reader.mail.MailReaderException;
import com.espmail.broker.generacion.Link;
import com.espmail.broker.modelo.UsuarioDao;
import com.espmail.broker.modelo.EstadoEnviado;
import com.espmail.log.Logger;
import com.espmail.utils.dao.FactoriaDao;
import com.espmail.utils.dao.DaoException;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * ESPMail
 * User: Luis 
 */
public class ESPMailTranslator implements Translator{

   private static final String HEAD_HTML = "<div><img src=\"{LINK_LECTURA}\""
		+ " height=\"1\" width=\"1\">Si no puede ver correctamente el mensaje,"
		+ " haz clic <a href=\"{LINK_MEMCAMP}\">aqu&iacute;</a></div>";

	private static final String HEAD_TEXTO = "Si no puede ver correctamente el mensaje,"
		+ " copie este enlace {LINK_MEMCAMP} en el navegador\n";

   private final List etiquetas;
   private final List elementos;
   private String[] valores;

   private final boolean html;
   private boolean cabeceras=true;

   private String pie;

   private String email;
   private int idLista;
   private long idEmail;

   private boolean initialized=false;

   private int idEnvio;
   private Logger log = new Logger(true, ESPMailTranslator.class);
   
   public ESPMailTranslator(){
      this(true);
   }
/**
 *
 * @param html
 */
   public ESPMailTranslator(boolean html){
      super();

      this.html=html;
      
      this.elementos=new ArrayList();
      this.etiquetas = new ArrayList();
      this.etiquetas.add("LINK_MEMCAMP");
		this.etiquetas.add("LINK_BAJA");
		this.etiquetas.add("LINK_LECTURA");
		this.etiquetas.add("EMAIL");
		this.etiquetas.add("NOMBRE");
		this.etiquetas.add("APELLIDOS");
		this.etiquetas.add("TRATAMIENTO");      
   }
/**
 *
 * @param bf
 * @param valores
 * @return
 * @throws TranslatorException
 */
   public ESPMailMessage translate(StringBuffer bf, String [] valores ) throws TranslatorException {
      
      setValores(valores);
      getElementos(bf.toString());

      Email email = new Email();

      StringBuffer mensaje = new StringBuffer();

      for(int i=0;i< this.elementos.size();i++){
         
         Object elemento = this.elementos.get(i);

         if(elemento instanceof Integer){
            String valor = valores[(( Integer )elemento ).intValue()];
            mensaje.append(valor!=null?valor:"");
         }else if(elemento instanceof Link){
            mensaje.append((( Link )elemento).genera(idEmail,idLista));
         }else{
            mensaje.append(elemento);
         }
      }
      
      email.setMessageHTML(mensaje.toString());

      return email;
   }
/**
 *
 * @param malos
 * @throws TranslatorException
 */
   public void badsProcess(List malos) throws TranslatorException {

      try {
         UsuarioDao emails = ( UsuarioDao )FactoriaDao.getInstance().getDao(UsuarioDao.class);
         for(int i = 0; i<malos.size();i++){

            Email email = ( Email )malos.get(i);
            log.debug("Procesando email malo "+email.getIdEmail());
            emails.cambiaEstadoEnviado(email.getIdEmail(), email.getIdEnvio(), EstadoEnviado.PENDIENTE);
         }
      } catch (DaoException e) {
         throw new TranslatorException(e);
      }
   }
  /**
   *
   * @param messages
   * @return
   */
   public StringBuffer returnBads(ESPMailMessage messages) {
      return null; 
   }

   public String getPie() {
      return pie;
   }

   public void setPie(String pie) {
      this.pie = pie;
   }

   public void addEtiqueta(String etiqueta){
      if(etiqueta != null)
         this.etiquetas.add(etiqueta);
   }

   public String[] getValores() {
      return valores;
   }

   public void setValores(String[] valores) {
      this.valores = valores;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public int getIdLista() {
      return idLista;
   }

   public void setIdLista(int idLista) {
      this.idLista = idLista;
   }

   public long getIdEmail() {
      return idEmail;
   }

   public void setIdEmail(long idEmail) {
      this.idEmail = idEmail;
   }
/**
 * 
 * @param texto
 * @throws TranslatorException
 */
   public void getElementos(String texto) throws TranslatorException {

      if(initialized) return;



      if(isHtml()){
         String htmlMin = texto.toLowerCase();
         int indexBody = htmlMin.indexOf('>', htmlMin.indexOf("<body")) + 1;
         int indexEndBody = htmlMin.indexOf("</body>", indexBody);

         if (indexBody == -1 || indexEndBody == -1) {
            throw new TranslatorException("El mail no tiene las tags de body");
         }

         StringBuffer sb = new StringBuffer(texto.substring(0, indexBody));
         sb.append(HEAD_HTML);
         sb.append(texto.substring(indexBody, indexEndBody));
         sb.append(getPie());
         sb.append(texto.substring(indexEndBody));
         texto = sb.toString();
         
      }else{
         texto = (cabeceras?HEAD_TEXTO:"") + texto + (cabeceras ? getPie():"");
      }
      
      char[] LINK = "href = \"".toCharArray();
		char[] letras = texto.toCharArray();

      int length = letras.length;
		char modo = 'T'; // T->Texto P->Parametro L->Link
		int ini = 0;
		int linkIni = 0;
		int linkIndex = 0;

		for (int i = 0; i < length; i++) {
			char ch = letras[i];

			switch(modo) {
			case 'P' :
				boolean parametro = false;
				if (ch == '{') {
					throw new TranslatorException("Encontrada un caracter { dentro de un parametro.");
				}

				if (ch == '}') {
					String param = texto.substring(ini + 1, i);
					Iterator it = etiquetas.iterator();

					for (int j = 0; it.hasNext(); j++) {
						String item = (String) it.next();
                  log.debug(item);
						if (item.equals(param)) {
							this.elementos.add(new Integer(j));
							parametro = true;
							break;
						}
					}

					if (!parametro){
						//Si no es un parametro se inserta como texto (por ej, un css o javascript)
						this.elementos.add(texto.substring(ini , i+1));																	
					}
					ini = i+1;
					modo = 'T';
				}
            
            break;
			case 'L' :
				if (ch == '{') {
					throw new TranslatorException("No se permiten parametros en los enlaces.");
				}

				if (ch == '"') {
					this.elementos.add(new Link(this.idEnvio, 'C', linkIndex++,
							texto.substring(ini, i)));
					ini = i;
					modo = 'T';
				}

				break;
			default :
				char lch = LINK[linkIni];

				if (lch == ch
						|| (linkIni > 0 && ch == ' ' && LINK[linkIni - 1] == ' ')
						|| (lch == ' ' && LINK[linkIni + 1] == ch)) {
					if (lch == ch) {
						linkIni++;
					} else if (lch == ' ') {
						linkIni += 2;
					}

					if (ch == '"') {
						linkIni = 0;

						if (letras[i + 1] != '{') {
							this.elementos.add(texto.substring(ini, i + 1));
							modo = 'L';
							ini = i + 1;
						}
					}
				} else {
					linkIni = 0;

					if (ch == '{') {
						this.elementos.add(texto.substring(ini, i));
						ini = i;
						modo = 'P';
					}
				}
			}
		}

		if (ini < length) {
			this.elementos.add(texto.substring(ini));
		}
      initialized = !initialized;
   }

   public boolean cabeceras(){
      return cabeceras;
   }

   public int getIdEnvio() {
      return idEnvio;
   }

   public void setIdEnvio(int idEnvio) {
      this.idEnvio = idEnvio;
   }

   public List getEtiquetas() {
      return etiquetas;
   }

   public boolean isHtml() {
      return html;
   }

   public void setCabeceras(boolean cabeceras) {
      this.cabeceras = cabeceras;
   }
}
