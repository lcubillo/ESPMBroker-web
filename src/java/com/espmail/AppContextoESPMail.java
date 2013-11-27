package com.espmail;

import com.espmail.utils.contexto.Contexto;
import com.espmail.reader.Reader;
import com.espmail.util.Etiquetas;
import java.io.IOException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.dbcp.BasicDataSourceFactory;

/**
 * ESPMail
 * User: Luis


 */
public class AppContextoESPMail extends Contexto {

   public static final String MAIL_SERVER = "mail.server";
	public static final String MAIL_DEBUG = "mail.debug";

   private String reader;

   DataSource DS; 
   
   protected AppContextoESPMail(){
      super();
      System.out.println("Object Initializing");
      init(getProperties());
      System.out.println("INitialized");
   }
   public Connection getConexion() throws SQLException {    
      return DS.getConnection();
   }

   public void init (Properties prop){
      try {
         DS = BasicDataSourceFactory.createDataSource(prop);
      } catch (Exception e) {
         e.printStackTrace();  
      }
   }
   public void setReader(String className){
      this.reader=className;
   }

   public String getReader() {
      return reader;
   }
   
   private Properties getProperties(){
      
      Properties prop = new Properties();
      try {
         prop.load(this.getClass().getResourceAsStream("/com/espmail/util/db.properties"));
      } catch (IOException ex) {
         System.err.println("Don't access to db.properties: "+ex.getMessage());
         ex.printStackTrace();
      }
      return prop;
   }
}
