package com.espmail.util;

import com.espmail.lanzador.Lanzador;
import com.espmail.launcher.Launcher;
import com.espmail.launcher.LaunchException;
import com.espmail.launcher.mail.MailLauncher;
import com.espmail.log.Logger;
import com.espmail.message.mail.Email;
import com.espmail.message.ESPMailMessage;
import com.espmail.reader.Reader;
import com.espmail.reader.ReaderState;
import com.espmail.reader.mail.MailReader;
import com.espmail.utils.contexto.Contexto;


import javax.mail.Address;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.AddressException;
import javax.swing.text.AbstractDocument.LeafElement;



import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * ESPMail
 * User: Luis
 */
public class ESPMailBrokerTranslator implements Translator{
      
	Logger log;
	  	  
   String separador;

   Properties prop;
   
   public Properties getProp() {
	return prop;
}
public void setProp(Properties prop) {
	this.prop = prop;
}

String directorioGeneracion;

   String CONTROL, END_OF_SECTION,END_OF_FILE,CONTROL_CABECERAS;
   
   private final static int NUMERO_MAILS= 20;
   private final static int OK = 0;
   private final static int ERROR_WRITE_FILE = -1;
   public final static String IGUAL = "%;";
   public final static String CANALMAIL = "20130";
   public final static String PRISACOM = "1618819";

  
   public ESPMailBrokerTranslator(){
	 		prop = getProperties();        
	   }
   public ESPMailBrokerTranslator(Properties proper){
		 prop = proper;
		 CONTROL = prop.getProperty("CONTROL");
		 CONTROL_CABECERAS = prop.getProperty("CONTROL_CABECERAS");
		 END_OF_FILE = prop.getProperty("END_OF_FILE");
		 END_OF_SECTION = prop.getProperty("END_OF_SECTION");
  }
  
   public static ESPMailBrokerTranslator getInstance(){
      return new ESPMailBrokerTranslator();
   }
   public static ESPMailBrokerTranslator getInstance(Properties prop){
	   		
	      return new ESPMailBrokerTranslator(prop);
	   }

   /**
    *
    * @param bf
    * @param valores
    * @return
    * @throws TranslatorException
    */
   public ESPMailMessage translate(StringBuffer bf, String [] valores)throws TranslatorException{
	  //this.prop= getProperties();
	  
	   log = new Logger();
	   log.info("ENTRO EN TRANSLATE PARA ESPMail");
 	  log.info("                       ************************************");
 	  log.info(CONTROL+" "+END_OF_SECTION+" "+END_OF_FILE+" "+CONTROL_CABECERAS);
 	  log.info("                       ************************************");
 	  
 	  

      Email email = new Email();
      String mensaje = bf.toString();
          
      try{
    	  log.info(directorioGeneracion);
    	  log.info(CONTROL);  
      }catch (Exception ex){
    	  log.info("log error");
    	  ex.printStackTrace();
      }
      
      int inicio = 0;
      int tamControl = 0;
      try{
       tamControl = CONTROL.length();
       inicio = mensaje.indexOf(CONTROL);
      }catch (Exception ex){
    	  log.info("ERROR con el control");
    	  ex.printStackTrace();
    	  
      }
      int fin = inicio + tamControl;
      InternetAddress from = null;

      try {
         from = new InternetAddress(mensaje.substring(inicio+tamControl , mensaje.indexOf(CONTROL, inicio+tamControl)));
      } catch (AddressException e) {
    	   log.info("ERROR AL OBTENER EL FROM");
         e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
      }
      email.setFrom(from);
      
      

      inicio = mensaje.indexOf(CONTROL,inicio+tamControl);
      fin = mensaje.indexOf(CONTROL, inicio+tamControl);
      fin = (fin < 0) ? mensaje.length(): fin;      
      if(mensaje.substring(inicio).startsWith(CONTROL+"CLASE")){
         inicio = mensaje.indexOf(CONTROL,fin);
         fin = mensaje.indexOf(CONTROL, inicio+tamControl);
         fin = (fin < 0) ? mensaje.length(): fin;
      }
      
      email.setCabeceras(getHeaders(mensaje.substring(inicio+tamControl,fin)));
      

      String headersAux = (String) email.getCabeceras().get(3);      
      String pedCodAux []= headersAux.split("\\.");     
      email.setPedCod(pedCodAux[1].toString());
      //---
    
      inicio = mensaje.indexOf(CONTROL, inicio+tamControl);
      fin = mensaje.indexOf(CONTROL, inicio+tamControl);
      fin = (fin < 0) ? mensaje.length(): fin;
      if(mensaje.substring(inicio+tamControl,fin).length() < 2){
         inicio = mensaje.indexOf(CONTROL, inicio+tamControl);
         fin = mensaje.indexOf(CONTROL, inicio+tamControl);
         fin = (fin < 0) ? mensaje.length(): fin;
      }
      email.setTo(getDestinatarios(mensaje.substring(inicio+tamControl, fin)));

      inicio = mensaje.indexOf(CONTROL, inicio+tamControl);
      fin = mensaje.indexOf(CONTROL, inicio+tamControl);
      fin = (fin < 0) ? mensaje.length(): fin;
      email.setSubject(mensaje.substring(inicio+tamControl, fin));

      inicio = mensaje.indexOf(CONTROL, inicio+tamControl);
      fin = mensaje.indexOf(CONTROL, inicio+tamControl);
      fin = (fin < 0) ? mensaje.length(): fin;
      email.setMessageHTML(mensaje.substring(inicio, fin));

      inicio = mensaje.indexOf(CONTROL, inicio+tamControl);
      fin = mensaje.indexOf(CONTROL, inicio+tamControl);
      fin = (fin < 0) ? mensaje.length(): fin;
      email.setMessageTXT(new StringBuffer(mensaje.substring(inicio, fin)));
      
      
      
      //Letra
      inicio = mensaje.indexOf(CONTROL,inicio+tamControl);
      fin = mensaje.indexOf(CONTROL, inicio+tamControl);
      fin = (fin < 0) ? mensaje.length(): fin;
      email.setLetra(mensaje.substring(inicio,fin));
      
      //Url
      inicio = mensaje.indexOf(CONTROL,inicio+tamControl);
      fin = mensaje.indexOf(CONTROL, inicio+tamControl);
      fin = (fin < 0) ? mensaje.length(): fin;
      email.setUrl(mensaje.substring(inicio,fin));
                        
      return email;
   }

   public StringBuffer returnBads(ESPMailMessage email){
      return new StringBuffer();
   }

   
   /**
    * Recibe un String con la cabecera con caracteres de control y devuelve un Vector de Strings con los par�metros separados.
    * @param linea
    * @return Vector con los Parametros de la cabecera
    */
   
   private Vector getHeaders(String  linea) {
	   String auxValue="";
	   Vector  header =  new Vector();
	  try{
		  //Separo
		  String separatedLine []= linea.split(CONTROL_CABECERAS);
		  String listUnsubscribe []= separatedLine[0].split(IGUAL);
		  String replyTo[] = separatedLine[1].split(IGUAL);	
		  String xClase[] = separatedLine[2].split(IGUAL);
		  String returnPath []= separatedLine[3].split(IGUAL);
		  //	Relleno
		  try{
			  header.add(listUnsubscribe[1].toString());
			  header.add(replyTo[1].toString());
			  header.add(xClase[1].toString());
			  header.add(returnPath[1].toString());
		  }catch(Exception ex){
			  header = null;
			  log.error("Error composing the new header."); 
			  ex.printStackTrace();
		 }
	  }catch (Exception ex){
		  header = null;
		  log.error("Error obtaining the header."); 
		  ex.printStackTrace();
	  }
	 
      return header;
   }

   private Properties getProperties(){
	      if(prop != null)	      
	    	  return prop;
	      
	      prop = new Properties();
	      try {
	         prop.load(ESPMailBrokerTranslator.class.getClassLoader().getResourceAsStream("configuracion.properties"));
	      } catch (IOException e) {
	         e.printStackTrace();
	         return null;
	      }
	      return prop;
	   }   
/**
 *
 * @param direcciones
 * @return
 */
   private InternetAddress[] getDestinatarios(String direcciones){
      
      try {
         return InternetAddress.parse(direcciones);
      } catch (AddressException e) {
         return null;
      }
   }

   // GONZALO
   //Recibe la lista de malos correspondiente a  CanalMail/Prisacom
   //Tendra que devolver otra lista de malos, vacia o por tratar
   
   /** Recibe una lista de Emails Malos y los vuelca a disco a su carpeta correspondiente
    * @param bads
    */
   public void badsProcess(List badOnes){		
	    log = new Logger();
	    log.info("Entro en badsProcess. Hay "+badOnes.size()+" emails a tratar.");
		String fileName = "";
		String pedCodAux = "";		
		String companyCode = "";
		int mails = 0;
		boolean cambio = false;
		
		try {					
			log.info("Ordeno la lista de emails malos.");
			Collections.sort(badOnes); //Ordeno la lista por codigo de pedido
			
			while (!badOnes.isEmpty()) {
				Email email = (Email) badOnes.get(0);
				pedCodAux = email.getPedCod();				
				companyCode = getCompany(email);
				fileName = getNewFileName(email.getPedCod(),companyCode);
				FileWriter fichero = new FileWriter(directorioGeneracion+ email.getPedCod() +File.pathSeparator + fileName);
				PrintWriter pw = new PrintWriter(fichero);

				mails = 0;				
				cambio = false;
				
				while (mails < 20 && !badOnes.isEmpty() && !cambio) {			
					Email email2 = (Email)  badOnes.get(0);
					if (!email2.getPedCod().toString().equals(pedCodAux.toString())){
						cambio = true;
					}
					else {
						pedCodAux = email2.getPedCod();				
						pw.println(newEmail(email2));
						//Ya ha sido tratado, lo elimino de la lista
						badOnes.remove(0);
						mails++;
					}
				}
				pw.close();
			}
		}catch (ClassCastException ex){
			ex.printStackTrace();
		}catch(UnsupportedOperationException ex){
			ex.printStackTrace();
		}catch (Exception ex) {
		ex.printStackTrace();
	}
              
}
   
  
   /**
    * Compone un nuevo Email para volcarlo a fichero
    * @param email
    * @return StringBuffer con el nuevo email
    */
   private  StringBuffer newEmail(Email email) {
		StringBuffer mail = new StringBuffer();
		mail = mail.append(CONTROL + email.getFrom() + "\n");
		mail = mail.append(CONTROL + "CLASE" + email.getCabeceras().get(2)+ "\n");
		mail = mail.append(CONTROL + "List-Unsubscribe" + IGUAL
				+ email.getCabeceras().get(0) + CONTROL_CABECERAS + "Reply-To" + IGUAL
				+ email.getCabeceras().get(1) + CONTROL_CABECERAS + "X-Clase" + IGUAL
				+ email.getCabeceras().get(2) + CONTROL_CABECERAS + "Return-Path"
				+ IGUAL + email.getCabeceras().get(3) + "\n");
		Address to[] = email.getTo();
		//TODO Sin comprobar
		if (to.length==1)
			mail = mail.append(CONTROL + to[0].toString() + "\n");
		else if (to.length>1){
			for (int i=0;i<to.length;i++){
				mail = mail.append(CONTROL+to[i].toString());
				if (i==to.length-1) mail = mail.append("\n");
				else mail = mail.append(";");
			}
			
		}
		mail = mail.append(CONTROL + "S" + "\n");
		mail = mail.append(CONTROL + email.getSubject() + "\n");
		mail = mail.append(CONTROL + email.getMessageHTML() + "\n");
		mail = mail.append(CONTROL + email.getMessageTXT() + "\n");
		mail = mail.append(CONTROL + "S" + "\n");
		mail = mail.append(CONTROL + "http://www.canalmailcorp.com" + "\n");
		mail = mail.append(CONTROL + "EOS" + "\n");
		return mail;
	}
   
   /**
    * Obtiene el directorio donde debe volcarse el nuevo fichero
    * @param pedCod Codigo del pedido
    * @param companyCode Codigo identificador de la empresa
    * @return Nombre del directorio donde se almacenar� el archivo
    */
	private  String getDirectory(String pedCod,String companyCode) {
		//TODO: Sin comprobar
		String nDirect = "";
		nDirect = companyCode+pedCod;
		File direc = new File(directorioGeneracion +File.pathSeparator+ nDirect);
		
		if (!direc.exists()) 
			direc.mkdir();	
		
		return nDirect;
	}

	/**
	 * Obtiene un nuevo nombre para el fichero
	 * @param pedCod C�digo del pedido
	 * @param companyCode C�digo identificador de la empresa
	 * @return String con el nombre del fichero donde guardar la lista de correos malos
	 */
	private  String getNewFileName(String pedCod,String companyCode) {
		boolean generado = false;
		String sFichero = "";
		String directorio = "";
		int i = 1;
		while (!generado) {
			//--------
			//TODO Obtener la fecha para el nombre del fichero
			sFichero = "badOnes" + pedCod + "#" + i + "#.dat";
			//------------------------------
			if (directorio.equals(""))
				directorio = getDirectory(pedCod,companyCode);
			File fichero = new File(directorioGeneracion +File.pathSeparator+ directorio + File.pathSeparator
					+ sFichero);
			if (fichero.exists())
				i++;
			else
				generado = true;
		}

		return sFichero;
	}
	/**
	 * Obtiene el codigo de la compa�ia a la cual pertenece la campa�a
	 * @param email Email que se quiere enviar, de donde se sacar� la informaci�n de la campa�a
	 * @return C�digo de la compa��a responsable de la camapa�a
	 */
	
	private String getCompany(Email email){
		String companyCode = CANALMAIL;
		try{
		String returnPath = (String)email.getCabeceras().get(3);
		if(returnPath.toLowerCase().startsWith("prisacom")) companyCode =PRISACOM;
		else companyCode = CANALMAIL;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return companyCode;
	}
}
