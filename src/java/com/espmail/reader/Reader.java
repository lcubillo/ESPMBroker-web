package com.espmail.reader;

import com.espmail.launcher.Launcher;
import com.espmail.launcher.LaunchException;
import com.espmail.lanzador.Lanzador;
import com.espmail.reader.mail.MailReaderException;
import com.espmail.util.Etiquetas;
import com.espmail.util.Translator;
import com.espmail.utils.dao.Transaccion;
import com.espmail.utils.dao.DaoException;
import com.espmail.utils.contexto.Contexto;

import javax.management.NotificationBroadcasterSupport;
import java.util.*;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

/**
 * ESPMail
 * User: Luis
 */
public abstract class Reader extends NotificationBroadcasterSupport implements ReaderMBean, Runnable {

   Contexto context;

   int max_threads;
    

   Method invoker;
   
   int max_emails;

   Properties prop;

   private boolean stop=false;
   
   protected BufferedReader bf;

   int id;

   String empresa;

   public Transaccion transaccion ;

   public Reader(){
      //prop= getProperties();

   }

   public Reader(Properties prop){      
      this.prop = prop;
   }

   public abstract void  init() throws LaunchException;

   public Properties getProperties(){
      if(prop != null)
         return prop;
      
      prop = new Properties();

      try {
         prop.load(Reader.class.getClassLoader().getResourceAsStream("configuracion.properties"));
      } catch (IOException e) {
         e.printStackTrace();
         return null;
      }
      return prop;
   }   

   public int getMax_emails() {
      return max_emails;
   }  

   public void setMax_emails(int max_emails) {
      this.max_emails = max_emails;
   }

   public void run(){
      
   }

   public Launcher getLauncher(Class clazz) throws LaunchException {

      return Lanzador.getInstance().getLauncher(clazz);
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public abstract void getCampanha(boolean bbdd) throws MailReaderException ;
/**
 *
 * @param empresa (canalmail|prisacom)
 * @return
 */
   public String codifica(String empresa){
      empresa=empresa.toLowerCase();
      StringBuffer destino=new StringBuffer();
      String alfabeto = "ABCDEFGHIJKLMN�OPQRSTUVWXYZ";
      for (int i=0;i < empresa.length()&&i< Etiquetas.PEDIDO_LENGTH;i++) {
         int posicion = alfabeto.toLowerCase().indexOf(empresa.charAt(i));
         if (posicion!=-1) {
             destino.append(posicion);
         }
      }
      return destino.toString();
   }

   public String getEmpresa() {
      return empresa;
   }

   public void setEmpresa(String empresa) {
      this.empresa = empresa;
   }

   public int getMax_threads() {
      return max_threads;
   }

   public void setMax_threads(String max_threads) {
      setMax_threads(Integer.parseInt(max_threads));
   }

   public void setMax_threads(int max_threads) {
      this.max_threads = max_threads;
   }

   public  void stopReader(){
      stop=!stop;
   }

   public boolean isStop() {
      return stop;
   }

   public Contexto getContext() {
      return context;
   }

   public void setContext(Contexto context) {
      this.context = context;
   }

   public abstract String getReaderState();

   public abstract ReaderState getState();

   public abstract Translator getTranslator();
}
