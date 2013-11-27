package com.espmail.util;

import java.util.Properties;
import java.io.IOException;

/**
 * ESPMail
 * User: Luis
 */
public class Etiquetas {

   public static final String ENVIADO = "E";
   public static final String PENDIENTE = "P";
   public static final String LISTO= "L";
   public static final String ERROR= "F";
   public static final String ENVIANDO= "S";

   public static final String PROPERTIES="/opt/ias/lanzamiento/lanzador.properties";

   public static final int PEDIDO_LENGTH=4;

   public static Properties getProperties(){
      Properties prop = new Properties();
      try {
         prop.load(Etiquetas.class.getResourceAsStream(Etiquetas.PROPERTIES));
      } catch (IOException e) {
         e.printStackTrace();  
      }
      return prop; 

   }
}
