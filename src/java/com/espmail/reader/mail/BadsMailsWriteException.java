package com.espmail.reader.mail;

import com.espmail.log.Logger;

/**
 * ESPMail
 * User: Luis
 */
public class BadsMailsWriteException extends Exception{
   Logger log;

   public BadsMailsWriteException(String message) {
      log = new Logger();
      log.info("Emails malos:\n"+message);
   }


}
