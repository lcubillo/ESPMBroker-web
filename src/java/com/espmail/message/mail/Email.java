package com.espmail.message.mail;

import com.espmail.message.ESPMailMessage;
import com.espmail.util.Etiquetas;
import com.espmail.util.mail.EMailState;
import com.espmail.launcher.LaunchException;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.AddressException;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Arrays;
import java.net.InetAddress;
import java.lang.reflect.Method;

/**
 * ESPMail
 * User: Luis
 */
public class Email extends ESPMailMessage implements Comparable {

   
   private Address from ;
   private Address [] to;
   private Address [] replyTo;

   private long idEmail=-1l;
   private int idLista=-1;
   private int idEnvio=-1;

   private String subject;

   private StringBuffer messageHTML;
   private StringBuffer messageTXT;

   private EMailState estado;
	
   private Vector cabeceras;
   
   private String pedCod;
   
   private String letra; //Hara referencia a la letra (normalmente S) del final de cada correo
   private String url; //Hara referencia a la URL de vuelta
   

   //
   
   public Email(){
      estado = EMailState.get(Etiquetas.PENDIENTE);

   }

   public StringBuffer getMessageTXT() {
	   return messageTXT;
   }

   public void setMessageTXT(StringBuffer messageTXT) {
	   this.messageTXT = messageTXT;
   }

   public String getLetra() {
		return letra;
	}

	public void setLetra(String letra) {
		this.letra = letra;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
   public String getPedCod() {
	   return pedCod;
   }
   
   public void setPedCod(String pedCod) {
	   this.pedCod = pedCod;
   }

   public void setCabeceras(Vector cabeceras) {
	  this.cabeceras = cabeceras;      
   }
   
   public Vector getCabeceras(){
	   return cabeceras;
   }
  

   public void setEstado(String estado){
      this.estado = EMailState.get(estado);
   }

   public Address getFrom() {
      return from;
   }

   public void setFrom(Address from) {
      this.from = from;
   }


   public Address [] getTo() {
      return to;
   }

   public void setTo(InternetAddress [] to) {
      this.to = to;
   }
      
   public StringBuffer getMessageHTML() {
      return messageHTML;
   }

   public void setMessageHTML(String messageHTML) {
      this.messageHTML = new StringBuffer(messageHTML);
   }

   public String getSubject() {
      return subject;
   }

   public void setSubject(String subject) {
      this.subject = subject;
   }

   public Address[] getReplyTo() {
      return replyTo;
   }

   public void setReplyTo(InternetAddress [] replyTo) {
      this.replyTo = replyTo;
   }

   public void setReplyTo(String replyTo) throws LaunchException {
      try {
         setReplyTo(InternetAddress.parse(replyTo));
      } catch (AddressException e) {
         throw new LaunchException(e);
      }
   }

   public long getIdEmail() {
      return idEmail;
   }

   public void setIdEmail(long idEmail) {
      this.idEmail = idEmail;
   }

   public int getIdLista() {
      return idLista;
   }

   public void setIdLista(int idLista) {
      this.idLista = idLista;
   }

   public int getIdEnvio() {
      return idEnvio;
   }

   public void setIdEnvio(int idEnvio) {
      this.idEnvio = idEnvio;
   }
   
   public int compareTo(Object o) {
	    Email otro = (Email) o;	
	    return pedCod.compareTo(otro.getPedCod());
	 }
   

}
