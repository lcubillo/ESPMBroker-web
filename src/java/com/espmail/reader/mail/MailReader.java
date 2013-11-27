package com.espmail.reader.mail;

import com.espmail.lanzador.Lanzador;

import com.espmail.launcher.LaunchException;
import com.espmail.launcher.mail.MailLauncher;
import com.espmail.log.Logger;
import com.espmail.message.mail.Email;
import com.espmail.reader.Reader;
import com.espmail.reader.ReaderState;
import com.espmail.util.*;
import com.espmail.utils.dao.FactoriaDao;
import com.espmail.utils.dao.DaoException;
import com.espmail.modelo.mail.CampaignSelectDao;

import javax.management.Notification;



import java.io.*;

import java.util.*;

/**
 * ESPMail
 * User: Luis
 */
public class MailReader extends Reader {

   /**
    * Extraerá emails de los ficheros hasta alcanzar el max_emails
    */
   public final static String CANALMAIL = "20130";
   public final static String PRISACOM = "1618819";
   Logger log;
   String CONTROL, END_OF_SECTION, END_OF_FILE, CONTROL_CABECERAS;
   String replyTo;
   ReaderState ESTADO;
   String directorioGeneracion;
   ArrayList ficheros;
   int enviados = 0;
   int sequence_notification = 0;
   ESPMailBrokerTranslator cmt;
   File campanha;
   boolean stop = false;

   public MailReader() {
      super();
      ESTADO = ReaderState.get(ReaderState.LISTO.toString());
   }

   public MailReader(Properties prop) {
      super(prop);
      ESTADO = ReaderState.get(ReaderState.LISTO.toString());
   }

   public void init() {

      Properties prop = new Properties();
      prop.setProperty("CONTROL", this.CONTROL);
      prop.setProperty("CONTROL_CABECERAS", this.CONTROL_CABECERAS);
      prop.setProperty("END_OF_SECTION", this.END_OF_SECTION);
      prop.setProperty("END_OF_FILE", this.END_OF_FILE);



      //cmt = new ESPMailBrokerTranslator(null);

      cmt = ESPMailBrokerTranslator.getInstance(prop);


      replyTo = getReplyTo();

   }

   /**
    * Devolverá null si se alcanzó el max_emails
    * @deprecated
    * @param id
    * @return
    */
   public synchronized List getMessages(String id) {

      if (enviados >= getMax_emails()) {
         return null;
      }

      return null;
   }

   public synchronized void badMessages(File fichero, List bads) throws BadsMailsWriteException {
      StringBuffer sb = new StringBuffer();
      for (; bads.size() > 0; sb.append(cmt.returnBads((Email) bads.remove(0)).toString()));
      try {
         fichero.createNewFile();
      } catch (IOException e) {
         fichero.delete();
         try {
            fichero.createNewFile();
            FileWriter writer = new FileWriter(fichero);
            writer.write(sb.toString());
            writer.flush();
            writer.close();
         } catch (IOException e1) {
            throw new BadsMailsWriteException(sb.toString());
         }
      }
   }

   public void run() {


      init();

      List emails = new ArrayList();

      //********************************
      //TODO Obtener la campaña a tratar
      //********************************
      try {
         log = new Logger();
         log.info("Intento obtener la campaña");
         this.getCampanha(false);
      } catch (MailReaderException e1) {
         e1.printStackTrace();
      }

      BufferedReader bf = null;
      ESTADO = ReaderState.get(ReaderState.ENVIANDO.toString());

      while (enviados <= getMax_emails() && !stop) {
         log.info("EMAILS MAXIMOS: " + String.valueOf(getMax_emails()));
         log.info("NUMERO DE FICHEROS A ENVIAR: " + ficheros.size());



         File realFile = nextFile();

         try {
            bf = new BufferedReader(new FileReader(realFile));
         } catch (FileNotFoundException e) {
            e.printStackTrace();
         }

         StringBuffer sb = new StringBuffer();
         String linea = null;
         try {
            linea = bf.readLine();
            sb.append(linea);
         } catch (IOException e) {
            Notification ntfctn = new Notification(Lanzador.ERROR_READING, this, ++this.sequence_notification, enviados);
            sendNotification(ntfctn);
         }

         //Extraemos los emails del fichero
         while (linea != null && !(CONTROL + END_OF_FILE).equalsIgnoreCase(linea)) {

            if ((CONTROL + END_OF_SECTION).equalsIgnoreCase(linea)) {
               try {
                  log.info("INTENTO TRADUCIR EL MENSAJE");
                  emails.add(cmt.translate(sb, null));
               } catch (TranslatorException e) {
                  log.info("ERROR TRANSLATOREXCEPTION AL TRADUCIR EL MENSAJE");
                  e.printStackTrace();
               } catch (Exception ex) {
                  log.info("ERROR GENERAL AL TRADUCIR EL MENSAJE");
                  ex.printStackTrace();
               }
               sb = new StringBuffer();
               enviados++;
            }

            try {

               linea = bf.readLine();
               sb.append(linea);
            } catch (IOException e) {
               e.printStackTrace();
               Notification ntfctn = new Notification(Lanzador.ERROR_READING, this, ++this.sequence_notification, enviados);
               sendNotification(ntfctn);
            }
         }

         try {
            //	Una vez leido el fichero obtenemos el launcher y le pasamos los emails
            if (emails.size() > 0) { //si tenemos algo que enviar, TODO modificado por Gonzalo 07/11/08
               log.info("INTENTAMOS OBTENER EL LAUNCHER");
               MailLauncher lanzador = (MailLauncher) getLauncher(this.getClass());
               lanzador.envia(emails);
               new Thread(lanzador).start();
            }
         } catch (LaunchException e) {
            Notification ntfctn = new Notification(Lanzador.NOT_LAUNCHER, this, ++this.sequence_notification, enviados);
            sendNotification(ntfctn);
            e.printStackTrace();
         }

      }

      Lock bloqueo = new Lock();

      campanha.setLastModified(System.currentTimeMillis());
      bloqueo.unLock(getPedido(campanha), getEmpresa());

      Notification ntfctn = null;
      if (!stop) {
         ntfctn = new Notification(Lanzador.MAX_REACHED, this, ++this.sequence_notification, enviados);
      } else {
         ntfctn = new Notification(Lanzador.STOP_FORCED, this, ++this.sequence_notification, enviados);
      }
      sendNotification(ntfctn);


   }

   public void stopRead() {
      stop = !stop;
   }

   public File nextFile() throws NullPointerException {
      if (ficheros.size() > 0) {
         log.info(ficheros.toString());
         File fileAux = new File(ficheros.get(0).toString());
         ficheros.remove(0);
         return fileAux;
      } else {
         throw new NullPointerException("End of list of files");
      }

   }

   public void setCampanha(File campanha) {
      this.campanha = campanha;
   }

   public String getReplyTo() {
      StringBuffer buffer = new StringBuffer();

      String replyTo = "";
      try {
         File ficheroReplyTo = new File(campanha.getPath() + File.separator + "replyto.env");
         int longitud = (int) ficheroReplyTo.length();


         new FileReader(new File(campanha.getPath() + File.pathSeparator + "replyto.env")).read(buffer.toString().toCharArray(), 0, longitud);
         replyTo = buffer.toString();
      } catch (IOException e) {
      } catch (Exception ex) {
         ex.printStackTrace();
      }

      return replyTo;
   }

   /**
    * Obtiene los nombres de los ficheros de una determinada campaña
    * @param bbdd Booleano que indica si debe obtener la campaña de base de datos o no
    */
   public void getCampanha(boolean bbdd) throws MailReaderException {
      Lock bloqueo = new Lock();
      List campanhas;

      try {

         if (bbdd) {
            campanhas = ((CampaignSelectDao) FactoriaDao.getInstance().getDao(CampaignSelectDao.class)).findCampanha();
            if (campanhas == null || campanhas.size() == 0) {
               getCampanha(false); // buscamos en disco
            } else {
               File directorioCampana = null;
               for (Iterator it = campanhas.iterator();
                       it.hasNext();
                       campanha = new File(directorioGeneracion + File.pathSeparator + codifica(getEmpresa()) + it.next())) {
                  if (campanha.isDirectory() && (ficheros = new ArrayList(
                          Arrays.asList(
                          campanha.list(new FilenameFilter() {

                     public boolean accept(File fichero, String nombre) {
                        return nombre.endsWith(".dat");
                     }
                  })))).size() > 0) {
                     if (bloqueo.lock(getPedido(campanha), getEmpresa())) {
                        break;    // Ya tenemos los ficheros
                     }
                  }
               }
               getCampanha(false); // buscamos en disco
            }
         } else {
            campanhas = new ArrayList(Arrays.asList(new File(directorioGeneracion).list(new FilenameFilter() {

               public boolean accept(File directorio, String nombre) {
                  File direc = new File(directorio + File.separator + nombre);
                  return direc.isDirectory();
               }
            })));
            log.info("numero de campanias -" + campanhas.size());
            //La linea comentada daba nullPointerException
            //for(Iterator it = campanhas.iterator();it.hasNext();campanha = new File(( String )it.next())){
            for (int i = 0; i < campanhas.size(); i++) {
               File campanha = new File(directorioGeneracion + File.separator + campanhas.get(i).toString());
               if (campanha.isDirectory()) {
                  this.ficheros = new ArrayList(Arrays.asList(campanha.list(new FilenameFilter() {

                     public boolean accept(File fichero, String nombre) {
                        return nombre.endsWith(".dat");
                     }
                  })));

                  log.info("El numero de ficheros de la campaña " + campanha.toString().toUpperCase() + " es " + ficheros.size());
                  if (ficheros.size() > 0) {
                     //Recompongo las direcciones de los ficheros
                     for (int j = 0; j < ficheros.size(); j++) {
                        ficheros.set(j, campanha + File.separator + ficheros.get(j).toString());
                     }
                     //Inserto en base de datos
                     String nombre = campanha.getName();
                     String empresa = "";
                     /*if (nombre.startsWith(CANALMAIL)) empresa ="canalmail";
                     else if (nombre.startsWith(PRISACOM)) empresa="prisacom";
                     else empresa=null;                	 */
                     //if(bloqueo.lock(getPedido(campanha), getEmpresa())){
                     if (bloqueo.lock(getPedido(campanha), empresa)) {
                        log.info("pedido bloqueado");

                        i = campanhas.size();
                        break;
                     }
                  }
               }
            }
         }
      } catch (DaoException e) {
         throw new MailReaderException(e);
      } catch (NullPointerException npe) {
         throw new MailReaderException(npe);
      }


   }

   public String getPedido(File campanha) {
      String nombre = campanha.getName();
      int ultimaBarra;
      if (nombre.startsWith(CANALMAIL)) {
         return nombre.substring(CANALMAIL.length());
      } else if (nombre.startsWith(PRISACOM)) {
         return nombre.substring(PRISACOM.length());
      } else {
         return null;
      }
   }

   public String getCONTROL() {
      return CONTROL;
   }

   public void setCONTROL(String CONTROL) {
      this.CONTROL = CONTROL;
   }

   public String getEND_OF_SECTION() {
      return END_OF_SECTION;
   }

   public void setEND_OF_SECTION(String END_OF_SECTION) {
      this.END_OF_SECTION = END_OF_SECTION;
   }

   public String getEND_OF_FILE() {
      return END_OF_FILE;
   }

   public void setEND_OF_FILE(String END_OF_FILE) {
      this.END_OF_FILE = END_OF_FILE;
   }

   public String getDirectorioGeneracion() {
      return directorioGeneracion;
   }

   public void setDirectorioGeneracion(String directorioGeneracion) {
      this.directorioGeneracion = directorioGeneracion;
   }

   public String getCONTROL_CABECERAS() {
      return CONTROL_CABECERAS;
   }

   public void setCONTROL_CABECERAS(String CONTROL_CABECERAS) {
      this.CONTROL_CABECERAS = CONTROL_CABECERAS;
   }

   public void setReplyTo(String replyTo) {
      this.replyTo = replyTo;
   }

   public void stopReader() {
   }

   public ReaderState getState() {
      return this.ESTADO;
   }

   public String getReaderState() {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
   }

   public Translator getTranslator() {
      return new ESPMailBrokerTranslator();

   }
}
