package com.espmail.util;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.espmail.reader.Reader;
import java.io.*;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Vector;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;


import com.espmail.log.Logger;
import com.espmail.launcher.Launcher;
import com.espmail.utils.contexto.Contexto;

/**
 * ESPMail
 * User: Luis
 */
public class LauncherXMLParser extends DefaultHandler {
   
   StringBuffer sb = new StringBuffer();
   
   Object activeObject;
   String TYPE;
   String METHOD;
   String name;

   Map readers = new LinkedHashMap();
   Map launchers = new LinkedHashMap();
   

   int maxCampaigns;
   int launcherInstances;
   

   String etiquetas = "reader,launcher";
   String contenedores = "readers,launchers, lanzador";
   String launcherReference;

   Logger log= new Logger(true, LauncherXMLParser.class);
/**
 *
 * @param uri
 * @param localName
 * @param qName
 * @param attributes
 * @throws SAXException
 */
   public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

      try {
         if( etiquetas.indexOf(localName) >= 0 ){
            TYPE = attributes.getValue("class");

            if(localName.equals("reader") )
               activeObject = getObject(TYPE);
            else
               activeObject = getObject(TYPE, attributes.getValue("reader"),attributes.getValue("methodName"));


         }else if(contenedores.indexOf(localName) < 0){
            METHOD = "set"+attributes.getValue("methodName");
            name = attributes.getValue("name");
         }else if("lanzador".equals(localName)){
            if(attributes.getValue("maxCampaigns") != null)
               maxCampaigns=Integer.parseInt(attributes.getValue("maxCampaigns"));
         }
      } catch (ParserException e) {
         throw new SAXException(e);
      }
   }

/**
 *
 * @param uri
 * @param localName
 * @param qName
 * @throws SAXException
 */
   public void endElement(String uri, String localName, String qName) throws SAXException {

      if(localName.trim().equals("launcher_instances")){
         launcherInstances = Integer.parseInt(sb.toString().trim());
         sb=new StringBuffer();
      }else if(etiquetas.indexOf(localName)<0 &&      
           contenedores.indexOf(localName)<0){
         try {
            setValues();
         } catch (ParserException e) {
            throw new SAXException(e);
         }
      }else if(localName.equals("reader")){
         readers.put(TYPE,activeObject);
         launcherReference = TYPE;
      }else if(localName.equals("launcher")){
         ArrayList lanzadores = null;

         for(lanzadores = new ArrayList(launcherInstances);
               lanzadores.size() < launcherInstances;
               add(lanzadores,(Launcher)((Launcher)activeObject).getInstance()));
         
         try {
            launchers.put(Class.forName(launcherReference), lanzadores);
         } catch (ClassNotFoundException e) {
            throw new SAXException(e);
         }
       }else if(localName.equals("context")){
         try {
            ((Reader)readers.get(
                  activeObject.getClass().getMethod("getReader",null).invoke(null,null))).setContext((Contexto)activeObject);
         } catch (NoSuchMethodException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
         } catch (InvocationTargetException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
         } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
         }
      }
   }

   public void characters(char ch[], int start, int length) throws SAXException {
      this.sb.append(ch, start, length);
   }

   public Object getObject(String clazz) throws ParserException {
      return getObject(clazz,null,null);

   }
/**
 *
 * @param clazz
 * @param reference
 * @param methodName
 * @return
 * @throws ParserException
 */
   public Object getObject(String clazz, String reference, String methodName) throws ParserException{
      if(clazz == null)
         throw new NullPointerException("The attribute class cannot be null");
      Object retorno;

      try {         
         retorno = Class.forName(clazz).newInstance();
      } catch (InstantiationException e) {
         throw new ParserException(e);         
      } catch (IllegalAccessException e) {
         throw new ParserException(e);
      } catch (ClassNotFoundException e) {
         throw new ParserException(e);
      }
      
      if(reference!=null && methodName !=null)
         try {

            Method metodo = retorno.getClass().getMethod( methodName, new Class[]{Class.class} );

            int i=0;
            Class [] parametros = metodo.getParameterTypes();            
            metodo.invoke( retorno, new Object[]{ Class.forName(reference) } );
         } catch (IllegalAccessException e) {
            e.printStackTrace();
         } catch (InvocationTargetException e) {
            e.printStackTrace();
         } catch (NoSuchMethodException e) {
            e.printStackTrace();
         } catch (ClassNotFoundException e) {
            e.printStackTrace();
         }
      
      return retorno;
   }
/**
 *
 * @throws ParserException
 */
   public void setValues() throws ParserException {
      Method[] metodos = activeObject.getClass().getMethods();

      Method metodo = null;
      boolean encontrado = false;

      for(int i = 0; i< metodos.length;i++){

         if((metodo = metodos[i]).getName().equals(METHOD))
            for(int j=0 ; j < metodo.getParameterTypes().length
                 && !(encontrado = String.class.equals(metodo.getParameterTypes()[j])) ;j++);
         if(encontrado)
            break;
      }
      Vector parametros = new Vector();
      parametros.add(sb.toString().trim());
      
      if(name != null)
         parametros.add(name);
      
      try {
         metodo.invoke(activeObject, parametros.toArray());
         sb=new StringBuffer();
      } catch (IllegalAccessException e) {
         throw new ParserException(e);
      } catch (InvocationTargetException e) {
         throw new ParserException(e);
      }
   }
/**
 * 
 * @param argv
 */
   public static void main(String [] argv){
      
      LauncherXMLParser handler = new LauncherXMLParser();
      //InputStream is = LauncherXMLParser.class.getResourceAsStream("."+ File.separatorChar+ "configuration.xml");
      InputStream is = LauncherXMLParser.class.getResourceAsStream("."+ File.separator+ "configuration.xml");
      try {

         XMLReader reader = XMLReaderFactory.createXMLReader();
			reader.setContentHandler(handler);
         reader.parse(new InputSource(is));

      } catch (SAXException e) {

         e.printStackTrace();
         throw new RuntimeException(e);
      } catch (IOException e) {
         System.out.println(e.getMessage());
         e.printStackTrace();
         throw new RuntimeException(e);
      }


   }

   public Map getReaders() {
      return readers;
   }

   public void setReaders(Map readers) {
      this.readers = readers;
   }

   public Map getLaunchers() {
      return launchers;
   }

   public void setLaunchers(Map launchers) {
      this.launchers = launchers;
   }

   public int getMaxCampaigns() {
      return maxCampaigns;
   }

   public void setMaxCampaigns(int maxCampaigns) {
      this.maxCampaigns = maxCampaigns;
   }

   public void add(ArrayList lanzadores, Launcher lanzador){
      lanzadores.add(lanzador);
      lanzador.setId(lanzadores.size());
   }

   
}
