package com.espmail.launcher.mail;

import com.espmail.launcher.Launcher;
import com.espmail.launcher.LaunchException;
import com.espmail.util.mail.LauncherState;
import com.espmail.util.Etiquetas;
import com.espmail.message.mail.Email;
import com.espmail.message.ESPMailMessage;
import com.espmail.lanzador.Lanzador;
import com.espmail.utils.contexto.Contexto;
import com.espmail.AppContextoESPMail;
import com.sun.mail.smtp.SMTPMessage;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import java.util.*;
import java.lang.reflect.Method;

/**
 * ESPMail
 * User: Luis
 */
public class MailLauncher extends Launcher {


   /**
    * asociar� el lanzador con el fichero que esta lanzando
    */
   
   private String id_fichero;
   private List correos;
   private Session session;
   private Properties prop;
   
   private int idEnvio=-1;
   private int idLista=-1;
   
   private Map cabeceras = new LinkedHashMap();

   private ArrayList mailSmtp;

   public MailLauncher() {
      super();
   }
/**
 *
 * @param clazz
 * @param retardo
 * @param backToQueue
 */
   public MailLauncher(Class clazz, long retardo, Method backToQueue) {
      super(clazz, retardo, backToQueue);
      correos=new ArrayList();
   }   
/**
 *
 */
   public void run() {            

      prop= new Properties();
      prop.put("mail.smtp.host",getMailSmtpHost());      

      setEstado(LauncherState.ENVIANDO.getCodigo());
      for(Iterator it = this.getCorreos().iterator(); it.hasNext(); ){
         Email email = (Email) it.next();         
         try {
            send(email);
            if( getRetardo() > 0 )
               Thread.sleep(getRetardo());
         } catch (LaunchException e) {
            error("Error sending email to: " + email.getTo()+" Cause: "+e.getMessage());
            insertBad(email);
         } catch (InterruptedException e) {
            //todo ?
         }
      }
      setEstado(Etiquetas.LISTO);      
      try {
         Lanzador.getInstance().returnToQueue(this);
      } catch (LaunchException e) {
         e.printStackTrace();  
      }
   }

   public void envia(List correos){
      this.correos=correos;
      
   }

   public void envia(Email email){
      correos = new ArrayList();
      correos.add(email);

      //todo definir los properties de la session
   }
   /**
    *
    * @param message
    * @throws LaunchException
    */
   public void send(ESPMailMessage message) throws LaunchException {


//      SMTPMessage mensaje = new SMTPMessage(session);

      Email email;

      try {
         email = (Email)message;

         this.prop.put("mail.smtp.from", "bounces." + getId(email)
           + "@canalmail.net");

         /*if(email.getNumReintento()> getReintentos())
            throw new DiscardEmailException(new LaunchException("Email reaches attempts limit"));*/

      } catch (ClassCastException e) {
         getBads().add(e);
         throw new LaunchException(e);
      }
      /************************************************************/

		long tiempoSessionAux = System.currentTimeMillis();
		Session session = Session.getInstance(this.prop, null);
		session.setDebug(Boolean.getBoolean(Contexto.getPropiedad(AppContextoESPMail.MAIL_DEBUG)));

      MimeMessage msg = null;
      try {
         msg = new MimeMessage(session);
         msg.setFrom(email.getFrom());
         msg.setSubject(email.getSubject(), "iso-8859-1");
         msg.setSentDate(new Date());
         msg.setRecipients(Message.RecipientType.TO, email.getTo());
         msg.setReplyTo(email.getReplyTo());
         msg.setHeader("list-unsubscribe", "<mailto:list-unsubscribe." + getId(email)
              + "@canalmail.net>");
         //TODO A largo plazo habra que definir un x-clase por cada cliente
         msg.addHeader("X-clase","Y");

         if (email.getMessageHTML() == null) {
            msg.setText(email.getMessageTXT().toString(), "iso-8859-1");
         } else {
            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setContent(email.getMessageHTML().toString(), "text/html");
            Multipart mp = null;

            if (email.getMessageTXT() == null) {
               mp = new MimeMultipart();
            } else {
               mp = new MimeMultipart("alternative");
               MimeBodyPart mbp2 = new MimeBodyPart();
               mbp2.setContent(email.getMessageTXT().toString(), "text/plain");
               mp.addBodyPart(mbp2);
            }

            mp.addBodyPart(mbp1);
            msg.setContent(mp);
         }
      } catch (MessagingException e) {
         throw new LaunchException(e);
      }


      try {
         Transport.send(msg);

      } catch (MessagingException e) {         
         throw new LaunchException(e);
      }

      /***********************************************************************/


   }
/**
 *
 * @return
 */
   public Object clone(){
      MailLauncher ml = new MailLauncher();
      ml.session=null;
      ml.setCorreos(null);
      ml.setMailSmtp(mailSmtp);
      ml.setReader(this.getReader());
      ml.setRetardo(this.getRetardo());      
      return ml;
   }

   public String getId_fichero() {
      return id_fichero;
   }

   public void setId_fichero( String id_fichero ) {
      this.id_fichero = id_fichero;
   }   

   public void setCorreos(List correos){
      this.correos=correos;
   }

   public String getMailSmtpHost() {
      String cadena = (""+System.currentTimeMillis());

      try {
         int valor = Integer.parseInt(cadena.substring(cadena.length()-1)) % mailSmtp.size();
         return (String) mailSmtp.get(valor);
      } catch (NumberFormatException e) {
         return ( String )mailSmtp.get(0);
      }
   }

   public void addMailSmtpHost(String mailSmtp){
      if(this.mailSmtp == null)
         this.mailSmtp = new ArrayList(1);
      this.mailSmtp.add(mailSmtp);
   }

   public void addHeader(String valor, String nombre){
      cabeceras.put(nombre,valor);
   }

   public void setMailSmtp(String mailSmtp) {
      addMailSmtpHost(mailSmtp);
   }
   public void setMailSmtp(ArrayList mailSmtp){
      this.mailSmtp = mailSmtp;
   }
   public void setHeader(String valor, String nombre){
      addHeader(valor,nombre);
   }

   public String getId(Email email){
      
      return email.getIdEnvio()+"."+email.getIdEmail()+"."+email.getIdLista();
   }

   public void debug(String msg) {
      super.debug(getSerialVersion()+" "+msg);    //To change body of overridden methods use File | Settings | File Templates.
   }

   public List getCorreos() {
      return correos;
   }
/**
 * 
 * @return
 */
   public Object getInstance() {
      MailLauncher ml = new MailLauncher();
      ml.session=null;
      ml.setCorreos(null);
      ml.setMailSmtp(mailSmtp);
      ml.setReader(this.getReader());
      ml.setRetardo(this.getRetardo());
      return ml;
   }
}