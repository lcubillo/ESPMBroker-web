package com.espmail.message;

import com.espmail.message.mail.ListUnsubscribe;

import java.util.Map;
import java.util.LinkedHashMap;

/**
 * ESPMail
 * User: Luis
 */
public class HeaderSet {

   public static Map INSTANCES = new LinkedHashMap();

   public HeaderSet(){
      Header UNSUBSCRIBE = new ListUnsubscribe("List-Unsubscribe");
      INSTANCES.put("List-Unsubscribe",UNSUBSCRIBE);
   }

}
