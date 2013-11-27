package com.espmail.message.mail;

import com.espmail.message.Header;

/**
 * ESPMail
 * User: Luis
 */
public class ListUnsubscribe implements Header {

   public static final String headerName = "List-Unsubscribe";
   public String headerValue = "";

   public ListUnsubscribe(String headerValue){
      this.headerValue=headerValue;
   }

   public String getHeaderName() {
      return headerName;
   }

   public String getValue() {
      return headerValue;
   }

   public void setHeaderName(String name) {
      //no hacemos nada
   }

   public void setHeaderValue(String value) {
      //To change body of implemented methods use File | Settings | File Templates.
   }

   public String toString(){
      return headerName+":"+getValue();
   }
}
