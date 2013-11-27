package com.espmail.log;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import com.espmail.utils.TextUtils;

import java.sql.Timestamp;
import java.util.Date;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * ESPMail
 * User: Luis
 */
public class Logger {
   public static final String DEBUG="DEBUG";
   public static final String INFO="INFO";
   public static final String ERROR="ERROR";

   PrintStream printer;
   Log log;
   boolean mbean;
   Class clase;
   FileOutputStream fileOs;
   File fic;
/**
 *
 * @param mbean
 * @param clazz
 */
   public Logger(boolean mbean, Class clazz){
      this.mbean = mbean;
      log = LogFactory.getLog(clazz);
      clase = clazz;      
     fileOs= null;
     fic = null;
      try {
    	  
    	   fic = new File("/opt/ias/lanzamiento/lanzamiento.log"); 
    	  
		fileOs = new FileOutputStream(fic,true);
	} catch (FileNotFoundException e) {
		printer = null;
	}
     	 this.setStream((OutputStream) fileOs);      
   }

   public Logger(boolean mbean){
      this(mbean, Logger.class);
   }   

   public Logger(){
      this(false, Logger.class);
   }
   
   public void debug(String msg){
      if(!mbean){
         log.debug(msg);
      }else{
         write(DEBUG,msg);
      }
   }

   public void info(String msg){
      if(!mbean){
         log.info(msg);
      }else{
         write(INFO, msg);
      }
   }

   public void error(String msg){
      if(!mbean){
         log.error(msg);
      }else{
         write(ERROR, msg);
      }
   }

   private void write(String loglevel, String msg){
      write(this.clase, loglevel, msg);
   }

   private void write(String msg){
      this.write(this.clase, "info", msg);
   }
/**
 * 
 * @param clazz
 * @param loglevel
 * @param msg
 */
   private void write(Class clazz, String loglevel, String msg){
      //TODO insertamos el com.espmail.log en BBDD???

      StringBuffer sb = new StringBuffer(TextUtils.asString(new Timestamp(new Date().getTime())));
      sb.append(" [").append(clazz.toString()).append("] ");
      sb.append(" : ").append(msg);

      if(printer==null){
         System.out.println(sb.toString());
      }else
         printer.println(sb.toString());
                          
   }

   public void setStream(OutputStream os){
      this.printer = new PrintStream(os);
   }
   

}
