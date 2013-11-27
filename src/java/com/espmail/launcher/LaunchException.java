package com.espmail.launcher;

/**
 * ESPMail
 * User: Luis
 */
public class LaunchException extends Throwable{

   public LaunchException(Throwable cause) {
      super(cause);

   }

   public LaunchException() {
      super();    //To change body of overridden methods use File | Settings | File Templates.
   }

   public LaunchException(String message) {
      super(message);    //To change body of overridden methods use File | Settings | File Templates.
   }

   public LaunchException(String message, Throwable cause) {
      super(message, cause);    //To change body of overridden methods use File | Settings | File Templates.
   }
}
