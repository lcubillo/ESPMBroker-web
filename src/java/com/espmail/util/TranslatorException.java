package com.espmail.util;

/**
 * ESPMail
 * User: Luis
 */
public class TranslatorException extends Exception{

   public TranslatorException() {
      super();
   }

   public TranslatorException(String message) {
      super(message);
   }

   public TranslatorException(String message, Throwable cause) {
      super(message, cause);
   }

   public TranslatorException(Throwable cause) {
      super(cause);    
   }
}
