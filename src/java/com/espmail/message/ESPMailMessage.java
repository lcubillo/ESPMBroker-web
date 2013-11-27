package com.espmail.message;

import com.espmail.util.Translator;


/**
 * ESPMail
 * User: Luis
 */
public class ESPMailMessage {


   private Class traductorTYPE;
   
   private int numReintento=1;
   private StringBuffer messageTXT;

   public ESPMailMessage(){
      
   }

   public void incReintento(){
      setNumReintento(getNumReintento()+1);
   }

   public int getNumReintento() {
      return numReintento;
   }

   public void setNumReintento(int numReintento) {
      this.numReintento = numReintento;
   }
   
   public StringBuffer getMessageTXT() {
      return messageTXT;
   }

   public void setMessageTXT(StringBuffer message) {
      this.messageTXT = message;
   }

   public Class getTranslator() {
      return traductorTYPE;
   }

   public void setTranslator(Class traductor) {
      this.traductorTYPE = traductor;
   }
}
