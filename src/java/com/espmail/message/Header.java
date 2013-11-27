package com.espmail.message;

/**
 * ESPMail
 * User: Luis
 */
public interface Header {
      
   public String toString();
   public String getHeaderName();
   public String getValue();
   public void setHeaderName(String name);
   public void setHeaderValue(String value);
}
