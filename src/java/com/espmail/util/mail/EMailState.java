package com.espmail.util.mail;

import java.util.Collection;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * ESPMail
 * User: Luis
 */
public class EMailState {

   static final Map INSTANCES = new LinkedHashMap();

   public static final EMailState ENVIADO= new EMailState("E");

   public static final EMailState PENDIENTE =  new EMailState("P");

   public static final EMailState LISTO =  new EMailState("L");

   public static final EMailState ERROR =  new EMailState("F");

   public static final EMailState ENVIANDO =  new EMailState("S");

   public final String codigo;

   private EMailState(String codigo){
      this.codigo = codigo;      
      INSTANCES.put(codigo, this);
   }

   public String getCodigo(){
      return codigo;
   }

   public String toString(){
      return codigo;
   }

   public static Collection getValues(){
      return INSTANCES.values();
   }

   public static EMailState get(String codigo){
      return (EMailState) INSTANCES.get(codigo);
   }
}
