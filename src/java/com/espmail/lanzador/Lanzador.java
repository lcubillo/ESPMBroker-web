package com.espmail.lanzador;

import com.espmail.AppContextoESPMail;
import com.espmail.launcher.LaunchException;
import com.espmail.launcher.Launcher;
import com.espmail.log.Logger;

import com.espmail.reader.Reader;
import com.espmail.reader.ReaderState;
import com.espmail.util.Etiquetas;
import com.espmail.util.LauncherXMLParser;
import com.espmail.util.TranslatorException;
import com.espmail.util.mail.LauncherState;
import com.espmail.utils.contexto.Contexto;
import com.sun.jdmk.comm.HtmlAdaptorServer;
import java.net.MalformedURLException;
import java.util.logging.Level;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.management.*;
import javax.management.timer.Timer;
import java.io.*;
import java.net.InetAddress;
import java.util.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;



/**
 * ESPMail
 * User: Luis 
 */
public class Lanzador extends Logger implements LanzadorMBean, NotificationListener {

   public boolean fin=false;

   public static final String OBJECT_NAME="com.espmail.lanzador:type=Lanzador";
   //private static final String ENVIO_NAME = "com.espmail.generacion:type=CMNetReader,idEnvio=";   
   private static final String ENVIO_NAME = "com.espmail.generacion:type=MailReader,idEnvio=";
	private static final String TIMER_NAME = "Services:type=Timer";
	private static final String OBJECT_NAME_HTML = "SimpleAgent:name=htmladapter,port=";	
	private static final String HTML_PORT = "http.port";
	private static final String SLEEP_TIME = "lanzador.sleepTime";
	private static final String MAX_ENVIOS = "lanzador.maxEnvios";
	private static final String SCAN_ENVIOS = "scanEnvios";
	private static final String SCAN_BADS = "scanBads";
	private static final long TIME_PROCESS_BADS = 1000 * 60 * 2; // Cada 2 min hacemos procesamiento de los malos
   private static final long TIME_SCAN_PARADOS = 1000 * 60 * 60 * 2; // Cada 2 h.
   private static final long TIME_SCAN_ENVIOS=1000 * 60 * 5; //Cada 5 min
   public static final String ERROR_READING="ReadException";
   public static final String MESSAGING_ERROR="SendingError";
   public static final String ERROR_WRITING="WriteException";
   public static final String ERROR_LISTING="LsException";
   public static final String NOT_LAUNCHER="InvocationException";
   public static final String MAX_REACHED="FinTanda";
   public static final String STOP_FORCED="FinForzado";



   Properties prop = null;

   int[] exception_count;
   int maxCampaign=1;
   int MAX_ERRORS;
   boolean maxReached=false;
   int lanzados=0;
   private long lastUpdate;
   private long sleeptime;
   private String configuration; 
   private long tiempoMedioEnvio=0l;

   

   private Timer timer;

   private MBeanServer server;

   Map readers = new LinkedHashMap();
   Map launchers = new LinkedHashMap();
   Map bads = new LinkedHashMap();
   
   static Lanzador INSTANCE;


/**
 *
 * @throws LaunchException
 */
   private Lanzador() throws LaunchException{
      super(true);

      sleeptime = TIME_SCAN_ENVIOS;

      //Contexto.TYPE = com.espmail.AppContextoCMNet.class;
      Contexto.TYPE = AppContextoESPMail.class;

      INSTANCE = init(true);

      this.server = MBeanServerFactory.createMBeanServer();

      registra(OBJECT_NAME, this, null);


      this.timer = new javax.management.timer.Timer();
      registra(TIMER_NAME, this.timer, this);
      this.timer.start();

      this.timer.addNotification(SCAN_ENVIOS, SCAN_ENVIOS, new String(),
              new Date(System.currentTimeMillis() + 10000), TIME_SCAN_ENVIOS);

      this.timer.addNotification(SCAN_BADS, SCAN_BADS, new String(),
              new Date(System.currentTimeMillis() + 60000), TIME_PROCESS_BADS);

      loadEstado(true);
      this.lastUpdate = System.currentTimeMillis();

      //Incializamos la conexi�n a BD

      //((com.espmail.AppContextoCMNet) Contexto.getInstance()).init(this.prop);
      ((AppContextoESPMail) Contexto.getInstance()).init(this.prop);

      JMXServiceURL url;
      //Arrancamos un conector para permitir el acceso al servidor por rmi

      try {
         InetAddress direccion = InetAddress.getLocalHost();
         url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + direccion.getHostAddress() + ":9999/server");
         JMXConnectorServer jmxcs = JMXConnectorServerFactory.newJMXConnectorServer(url, null, this.server);
         jmxcs.start();
      } catch (IOException ex) {
         java.util.logging.Logger.getLogger(Lanzador.class.getName()).log(Level.SEVERE, null, ex);
      }
      String port = this.prop.getProperty(HTML_PORT);
      HtmlAdaptorServer adapter = new HtmlAdaptorServer();
      adapter.setPort(Integer.parseInt(port));
      registra(OBJECT_NAME_HTML + port, adapter, null);
      adapter.start();
   }

   /**
    *
    * @param addHook
    * @return
    */
   private Lanzador init(boolean addHook){

      try {
         loadEstado(true);
      } catch (LaunchException e) {
         e.printStackTrace();  
      }      

      LauncherXMLParser handler;

      try {

         debug("Parsing...");

         handler = new LauncherXMLParser();
         InputStream is =getInputStream();
         
         XMLReader reader = XMLReaderFactory.createXMLReader();
			reader.setContentHandler(handler);
         reader.parse(new InputSource(is));

      } catch (SAXException e) {
         System.out.println(e.getMessage());
         e.printStackTrace();
         throw new RuntimeException(e);
      } catch (IOException e) {
         System.out.println(e.getMessage());
         e.printStackTrace();
         throw new RuntimeException(e);
      }

      launchers=handler.getLaunchers();
      readers = handler.getReaders();
      maxCampaign=handler.getMaxCampaigns();


      if(addHook)
         Runtime.getRuntime().addShutdownHook(new Thread(){ public void run(){stopLanzador();}});
      
      return this;
   }

   /**
    *
    * @return
    * @throws LaunchException
    */
   public static Lanzador getInstance() throws LaunchException {
      if (INSTANCE == null){
         INSTANCE = new Lanzador();
      }
      return INSTANCE;
   }

   /**
    *
    */
   public void run(){

      Iterator it = readers.values().iterator();

      try {
         debug("Lanzados "+lanzados);
         while(it.hasNext() &&
              lanzados <  maxCampaign ){

            Reader lector =(Reader) it.next();
            
            if(lector.getState().equals(ReaderState.LISTO)){

               Thread reader = new Thread(lector);

               reader.start();
               registra(ENVIO_NAME+(lanzados++), lector, this );
            }
         }
         debug("Lanzados "+lanzados);
      } catch (LaunchException e) {
         error("Error lanzando las campa�as nos dormimos => "+e.getMessage());
         e.printStackTrace();
      }

   }

   /**
    *
    * @param clazz
    * @return
    */
   public Launcher getLauncher(Class clazz){

      boolean obtenido = false;
      Launcher lanzador = null;

      while(!obtenido ){
         synchronized (launchers){
            if(((ArrayList)launchers.get(clazz)).size() > 0 ){
               lanzador = (Launcher)((ArrayList)launchers.get(clazz)).remove(0);
               obtenido=true;
            }
         }
         if(!obtenido && !isMaxReached() )
            try {
               Thread.sleep(200);
            } catch (InterruptedException e) {}
      }
      return lanzador;
   }

   /**
    *
    * @param lanzador
    */

   public void returnToQueue(Launcher lanzador) {
      List malos = lanzador.getBads();
      lanzador.setBads(new ArrayList());
      
      synchronized(launchers){
//         lanzador.getBads();
         ((ArrayList)launchers.get(lanzador.getReader())).add(lanzador);
      }
      if(malos != null && malos.size()>0)
            addBad(lanzador.getReader(),malos);
         
   }

   /**
    *
    * @param notification
    * @param handback
    */
   public void handleNotification(Notification notification, Object handback) {      
      debug("Handlenotification "+notification.getType()+" seq "+notification.getSequenceNumber());
      
      
      if(MAX_REACHED.equals(notification.getType())){
         lanzados--;
         debug(" HandleNotification->Lanzados="+lanzados);
      }
      
      if(ERROR_READING.equals(notification.getType())){
         //ERROR de disco
      }
      
      if(ERROR_WRITING.equals(notification.getType())){

      }

      if(ERROR_LISTING.equals(notification.getType())){


      }
      if(SCAN_BADS.equals(notification.getType())){
         try {
            badsProcess();
         } catch (LaunchException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
         }
      }
      if(SCAN_ENVIOS.equals(notification.getType())){
         run();
      }
   }

   /**
    *
    */
   public void stopLanzador(){
      //TODO Release resources and quit

      info("Quitting...");

      Reader lector = null;
      Iterator it = readers.values().iterator(); 
      while(it.hasNext()){
         lector = (Reader)it.next();

//         if( lector.getState().equals(ReaderState.ENVIANDO))
         lector.stopReader();

         try {
            while(!lector.getState().equals(ReaderState.LISTO))
               Thread.sleep(300);
         } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
         }
      }
      try {
         badsProcess();
         
      } catch (LaunchException e) {}
   }

   public String getShortClassName( String className ){
      return className.substring(className.lastIndexOf("."));
   }

   public static void main(String[] args){
      System.setProperty("org.xml.sax.driver", "org.apache.xerces.parsers.SAXParser");
      try {
         Lanzador.getInstance().run();
      } catch (LaunchException e) {}
   }


   public boolean isMaxReached(){
      return lanzados < maxCampaign;
   }
/**
 *
 * @param required
 * @throws LaunchException
 */
   private void loadEstado(boolean required) throws LaunchException {
      this.prop=new Properties();

      File file = new File(Etiquetas.PROPERTIES);

		if (!file.exists()) {
			throw new LaunchException("No se encontro el archivo de configuraci�n "
					+ file.getAbsolutePath());
		}

		if (required || this.lastUpdate < file.lastModified()) {
			try {
				this.prop.load(new FileInputStream(file));
				this.lastUpdate = System.currentTimeMillis();				
			} catch (IOException e) {
				throw new LaunchException(e);
			}
		}
	}
/**
 *
 * @param name
 * @param mbean
 * @param listener
 * @throws LaunchException
 */
   private void registra(String name, Object mbean, NotificationListener listener) throws LaunchException {
		try {
			ObjectName oname = new ObjectName(name);

			if (this.server.isRegistered(oname)) {
            return;
         }

			this.server.registerMBean(mbean, oname);

			if (listener != null) {
            
            this.server.addNotificationListener(oname, listener, null, oname);
			}
		} catch (InstanceAlreadyExistsException e) {
			throw new LaunchException(e);
		} catch (MBeanRegistrationException e) {
			throw new LaunchException(e);
		} catch (NotCompliantMBeanException e) {
			throw new LaunchException(e);
		} catch (MalformedObjectNameException e) {
			throw new LaunchException(e);
		} catch (InstanceNotFoundException e) {
			throw new LaunchException(e);
		}
	}

   public long getSleepTime() {
      return 0;
   }
/**
 *
 * @param time
 */
   public void setSleepTime(long time) {
      this.sleeptime=time;
      try {
         this.timer.removeNotifications("Timer");

         this.timer.addNotification("Timer", SCAN_ENVIOS, new String(),
				new Date(System.currentTimeMillis() + 10000), sleeptime);
      } catch (InstanceNotFoundException e) {
         e.printStackTrace();  
      }
   }
/**
 *
 * @return
 * @throws FileNotFoundException
 */
   public InputStream getInputStream() throws FileNotFoundException {
      if(configuration == null){
        // return LauncherXMLParser.class.getClassLoader().getResourceAsStream("/opt/ias/lanzamiento/configuration.xml");    	  
    	return new FileInputStream("/opt/ias/lanzamiento/configuration.xml");
      }else
         return new FileInputStream(configuration);
   }

   public int getMaxEnvios() {
      return 0;  
   }

   public void init(String configuration) {
      this.configuration=configuration;
      this.init(false);
   }

   public int getEnviosActivos() {
      return 0;  
   }
/**
 *
 * @param lector
 * @param malos
 */
   public synchronized void addBad(Class lector, List malos){

      List bads = ( List )this.bads.get(lector);

      if(bads == null)
         this.bads.put(lector, malos);
      else{
         debug(bads.size()+" malos para "+lector.getName());
         for(; malos.size()>0;
             bads.add(malos.remove(0)));
         this.bads.put(lector, bads);
      }

      debug("A�adidos "+this.bads.size());

   }
/**
 * 
 * @throws LaunchException
 */
   public void badsProcess() throws LaunchException {

      debug("Bad's Proccess in progress...");
      
      LinkedHashMap malos=null;

      synchronized (bads){

         malos = (LinkedHashMap)  bads;
         bads = new LinkedHashMap();
      }

      Iterator it = malos.keySet().iterator();

      try {
         
         while(it.hasNext()){
            
            Class clave = (Class) it.next();
            debug("Clase "+clave.getName());

            Reader lector = (Reader)(clave.newInstance());
            lector.getTranslator().badsProcess(( List )malos.get(clave));
            
         }
      } catch (TranslatorException e) {
         throw new LaunchException(e);
      } catch (IllegalAccessException e) {                                                                   
         throw new LaunchException(e);
      } catch (InstantiationException e) {
         throw new LaunchException(e);
      }
   }


   public long getTiempoMedioEnvio() {
      return 0;  
   }

}
