package com.espmail.reader;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
 * ESPMail
 * User: Luis
 */
public class ReaderState {

   static final Map INSTANCES = new LinkedHashMap();

   public static final ReaderState LISTO =  new ReaderState("L");

   public static final ReaderState ERROR =  new ReaderState("F");

   public static final ReaderState ENVIANDO =  new ReaderState("S");

   public final String codigo;



   private ReaderState(String codigo){
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

   public static ReaderState get(String codigo){
      return (ReaderState) INSTANCES.get(codigo);
   }

}
