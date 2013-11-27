package com.espmail.reader.mail;

/**
 * ESPMail
 * User: Luis 
 */
public class MailReaderException extends Exception{

   public MailReaderException() {
      super();    //To change body of overridden methods use File | Settings | File Templates.
   }

   public MailReaderException(String message) {
      super(message);    //To change body of overridden methods use File | Settings | File Templates.
   }

   public MailReaderException(String message, Throwable cause) {
      super(message, cause);    //To change body of overridden methods use File | Settings | File Templates.
   }

   public MailReaderException(Throwable cause) {
      super(cause);    //To change body of overridden methods use File | Settings | File Templates.
   }
}
