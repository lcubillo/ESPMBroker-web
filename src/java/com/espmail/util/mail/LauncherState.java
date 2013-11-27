package com.espmail.util.mail;


import java.util.Collection;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * ESPMail
 * User: Luis
 */
public class LauncherState {

   static final Map INSTANCES = new LinkedHashMap();

   public static final LauncherState LISTO =  new LauncherState("L");

   public static final LauncherState ERROR =  new LauncherState("F");

   public static final LauncherState ENVIANDO =  new LauncherState("S");

   public final String codigo;



   private LauncherState(String codigo){
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

   public static LauncherState get(String codigo){
      return (LauncherState) INSTANCES.get(codigo);
   }
   
}
