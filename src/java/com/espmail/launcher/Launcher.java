package com.espmail.launcher;

import com.espmail.util.mail.LauncherState;
import com.espmail.util.Etiquetas;

import java.util.*;
import java.lang.reflect.Method;

import com.espmail.reader.Reader;
import com.espmail.message.ESPMailMessage;
import com.espmail.log.Logger;


/**
 * ESPMail
 * User: Luis
 */
public abstract class Launcher extends Logger implements Runnable, Cloneable{

   private long serialVersion=System.currentTimeMillis();

   private LauncherState estado=LauncherState.get("L");

   private List bads;
   
   private long retardo;
   private Method backToQueeMethod;

   private int reintentos;
   private Class lector;

   private int id;

   public Launcher(){
      super(true);
      this.id=1;
   }

   public Launcher(Class clazz, long retardo, Method backToQueue){
      super(true);
      this.id=1;
      this.lector=clazz;
      this.retardo=retardo;
      this.backToQueeMethod=backToQueue;
   }


   public String getEstado(){     
         return estado.toString();
   }   

   public void setEstado(String codigo ){
      synchronized(estado){
         estado = LauncherState.get(codigo);
      }
   }   

   public void setReader(Class lector){
      this.lector=lector;
   }

   public Class getReader(){
      return lector;
   }

   public void insertBad(ESPMailMessage message){
      if(bads==null)
         bads=new ArrayList(0);
      message.incReintento();
      bads.add(message);
   }

   public Object clone(){
//      Launcher obj = null;
//
//      try {
//         obj = ( Launcher )this.getClass().newInstance();
//      } catch (InstantiationException e) {
//         e.printStackTrace();
//      } catch (IllegalAccessException e) {
//         e.printStackTrace();
//      }
//
//
//      try {
//         obj.lector = Class.forName(this.lector.getName());
//      } catch (ClassNotFoundException e) {
//         e.printStackTrace();
//      }
//      obj.bads = null;
//      obj.backToQueeMethod=null;
//      obj.setId((this.getId()));
//      id++;
//      return obj;

      Launcher obj=null;
      try {
         obj = ( Launcher )this.getClass().newInstance();
      } catch (InstantiationException e) {
         e.printStackTrace();
      } catch (IllegalAccessException e) {
         e.printStackTrace();  
      }

      try {
         obj.lector = Class.forName(this.lector.getName());
         obj.id= this.id++;
      } catch (ClassNotFoundException e) {
         e.printStackTrace();  
      }

      return obj;
   }

   public abstract void send(ESPMailMessage mensaje) throws LaunchException;

   public long getRetardo() {
      return retardo;
   }

   public void setRetardo(long retardo) {
      this.retardo = retardo;
   }

   public void setRetardo(String retardo){
      setRetardo(Long.parseLong(retardo));
   }

   public List getBads() {
      return bads;
   }

   public void setBads(List bads) {
      this.bads = bads;
   }

   public void setReintentos(String reintentos){
      setReintentos(Integer.parseInt(reintentos));
   }

   public int getReintentos() {
      return reintentos;
   }

   public void setReintentos(int reintentos) {
      this.reintentos = reintentos;
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {      
      this.serialVersion+=id;
   }

   public long getSerialVersion() {
      return this.serialVersion;
   }

   public abstract Object getInstance();
}
