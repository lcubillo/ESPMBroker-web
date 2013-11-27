package com.espmail.broker.generacion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.sql.SQLException;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;

import javax.management.timer.Timer;

import com.espmail.broker.AppContexto;
import com.espmail.broker.modelo.Envio;
import com.espmail.broker.modelo.EnvioDao;
import com.espmail.broker.modelo.EstadoEnvio;

import com.espmail.utils.contexto.Contexto;

import com.espmail.utils.dao.DaoException;
import com.espmail.utils.dao.FactoriaDao;
import com.espmail.utils.dao.Transaccion;

import com.sun.jdmk.comm.HtmlAdaptorServer;


public class Monitor extends LoggerSupport implements MonitorMBean, NotificationListener {

	private static final String OBJECT_NAME = "com.espmail.generacion:type=Monitor";
	private static final String ENVIO_NAME = "com.espmail.generacion:type=GeneradorEnvioCMP,idEnvio=";
	private static final String TIMER_NAME = "Services:type=Timer";
	private static final String OBJECT_NAME_HTML = "SimpleAgent:name=htmladapter,port=";
	private static final String CONFIG_FILE = "/opt/asp/monitor.properties";
	private static final String HTML_PORT = "http.port";
	private static final String SLEEP_TIME = "monitor.sleepTime";
	private static final String MAX_ENVIOS = "monitor.maxEnvios";
	private static final String SCAN_ENVIOS = "scanEnvios";
	private static final String SCAN_PARADOS = "scanParados";
	private static final long TIME_SCAN_PARADOS = 1000 * 60 * 60 * 2; // Cada 2 horas
	
	public static Monitor INSTANCE = null;

	private final MBeanServer server;
	private final Timer timer;
	private final Properties props;
	
	private long lastUpdate; // Momento en que se actualizó el estado
	private long sleepTime;
	private int maxEnvios;
	private int enviosActivos = 0;

	public Monitor() throws GeneradorException {
		super(true);
		INSTANCE = this;
		Contexto.TYPE = AppContexto.class;
		this.props = new Properties();
		
 		this.server = MBeanServerFactory.createMBeanServer();
		registra(OBJECT_NAME, this, null);

		this.timer = new Timer();  
		registra(TIMER_NAME, this.timer, this);
		this.timer.start();

		this.timer.addNotification("Timer", SCAN_PARADOS, new String(),
				new Date(System.currentTimeMillis() + 10000), TIME_SCAN_PARADOS);

		loadEstado(true);
		
		//Incializamos la conexión a BD
		try {
			((AppContexto) Contexto.getInstance()).init(this.props);
		} catch (SQLException e) {
			throw new GeneradorException(e);
		}
		
		String port = this.props.getProperty(HTML_PORT);
		HtmlAdaptorServer adapter = new HtmlAdaptorServer();
		adapter.setPort(Integer.parseInt(port));
		registra(OBJECT_NAME_HTML + port, adapter, null);
		adapter.start();
	}

	public int getMaxEnvios() {
		return this.maxEnvios;
	}
	
	public void setMaxEnvios(int maxEnvios) {
		this.maxEnvios = maxEnvios;
	}
	
	public long getSleepTime() {
		return this.sleepTime;
	}

	public void setSleepTime(long time) {
		if (this.sleepTime != 0) {
			this.timer.removeAllNotifications();
		}

		this.sleepTime = time;
		this.timer.addNotification("Timer", SCAN_ENVIOS, new String(),
				new Date(System.currentTimeMillis() + 1000), time);
	}

	public int getEnviosActivos() {
		return this.enviosActivos;
	}

	public String getMailServer() {
		return Contexto.getPropiedad(AppContexto.MAIL_SERVER);
	}

	public void setMailServer(String mailServer) {
		Contexto.setPropiedad(AppContexto.MAIL_SERVER, mailServer);
	}

	public boolean isMailDebug() {
		return Boolean.getBoolean(Contexto.getPropiedad(AppContexto.MAIL_DEBUG));
	}

	public void setMailDebug(boolean mailDebug) {
		Contexto.setPropiedad(AppContexto.MAIL_DEBUG, Boolean.toString(mailDebug));
	}

	public void scanEnvios() throws GeneradorException {
		debug("ScanEnvios");
		loadEstado(false);

		if (this.enviosActivos >= this.maxEnvios) {
			return;
		}

		try {
			EnvioDao dao = (EnvioDao) FactoriaDao.getInstance().getDao(EnvioDao.class);
			Integer[] idsEnvio = dao.findListos();
			
			for (int i = 0; this.enviosActivos < this.maxEnvios
					&& idsEnvio != null && i < idsEnvio.length; i++) {
				Transaccion transaction = new Transaccion();
	
				try {
					int idEnvio = idsEnvio[i].intValue();
					debug("Buscando datos de " + idEnvio);
					Envio envio = dao.findByIdForUpdate(idEnvio);
					dao.cambiaEstado(idEnvio, EstadoEnvio.ENVIANDO);
					transaction.commit();
					GeneradorEnvioCMP generador = new GeneradorEnvioCMP(envio);
					registra(ENVIO_NAME + idEnvio, generador, this);
					this.enviosActivos++;
					Thread thread = new Thread(generador);
					thread.start();
				} catch (DaoException e) {
					debug(e.getMessage());
					transaction.rollback();
				}
			
				transaction.close();
			}
		} catch (DaoException e) {
			debug("Error " + e.getMessage());
			throw new GeneradorException(e);
		} catch (Throwable t ) {
			t.printStackTrace();
		}
	}
	
	public void handleNotification(Notification notificacion, Object handback) {
		try {
			if (SCAN_ENVIOS.equals(notificacion.getMessage())) {
				scanEnvios();
			} else if (SCAN_PARADOS.equals(notificacion.getMessage())) {
				EnvioDao dao = (EnvioDao) FactoriaDao.getInstance().getDao(EnvioDao.class);
				Integer[] parados = dao.findEnviosParados();
				//TODO
				
				if (parados != null && parados.length > 0) {
					StringBuffer sb = new StringBuffer("Los siguiente envios están paralizados: ");

					for (int i = 0; i < parados.length; i++) {
						sb.append(parados[i]).append(' ');
					}

					Properties props = new Properties();
					props.setProperty("mail.smtp.host",
							Contexto.getPropiedad(AppContexto.MAIL_SERVER));
					Session session = Session.getInstance(props, null);
					MimeMessage msg = new MimeMessage(session);
					msg.setFrom(new InternetAddress("sistemas@corp.espmail.com"));
					msg.setSubject("Envios paralizados en espmail", "iso-8859-1");
					msg.setSentDate(new Date());
					msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse("sistemas@corp.espmail.com"));
					msg.setText(sb.toString(), "iso-8859-1");
					Transport.send(msg);
				}
			} else {
				this.enviosActivos--;
			}
		} catch (GeneradorException e) {
			error(e);
		} catch (MessagingException e) {
			error(e);
		} catch (DaoException e) {
			error(e);
		}
	}

	public void saveEstado() throws IOException {
		debug("Guardando estado");
		File file = new File(CONFIG_FILE);
		this.props.setProperty(SLEEP_TIME, Long.toString(this.sleepTime));
		this.props.setProperty(MAX_ENVIOS, Integer.toString(this.maxEnvios));
		this.props.setProperty(AppContexto.MAIL_SERVER, getMailServer());
		this.props.setProperty(AppContexto.MAIL_DEBUG, Boolean.toString(isMailDebug()));
		this.props.store(new FileOutputStream(file), null);
	}

	/**
	 * Carga el estado del properties si este ha cambiado, si es required se
	 * carga siempre.
	 * 
	 * @param required
	 * @throws GeneradorException 
	 */
	private void loadEstado(boolean required) throws GeneradorException {
		File file = new File(CONFIG_FILE);

		if (!file.exists()) {
			throw new GeneradorException("No se encontro el archivo de configuración " 
					+ file.getAbsolutePath());
		}
		
		if (required || this.lastUpdate < file.lastModified()) {
			try {
				debug("Cargando estado");
				this.props.load(new FileInputStream(file));
				this.lastUpdate = System.currentTimeMillis();
				setSleepTime(Long.parseLong(this.props.getProperty(SLEEP_TIME)));
				setMaxEnvios(Integer.parseInt(this.props.getProperty(MAX_ENVIOS)));
				setMailServer(this.props.getProperty(AppContexto.MAIL_SERVER));
				setMailDebug(Boolean.getBoolean(this.props.getProperty(AppContexto.MAIL_DEBUG)));
			} catch (IOException e) {
				throw new GeneradorException(e);
			}
		}
	}

	private void registra(String name, Object mbean, NotificationListener listener) throws GeneradorException {
		try {
			ObjectName oname = new ObjectName(name);

			if (this.server.isRegistered(oname)) {
				this.server.unregisterMBean(oname);
			}

			this.server.registerMBean(mbean, oname);
			
			if (listener != null) {
				this.server.addNotificationListener(oname, listener, null, oname);
			}
		} catch (InstanceAlreadyExistsException e) {
			throw new GeneradorException(e);
		} catch (MBeanRegistrationException e) {
			throw new GeneradorException(e);
		} catch (NotCompliantMBeanException e) {
			throw new GeneradorException(e);
		} catch (MalformedObjectNameException e) {
			throw new GeneradorException(e);
		} catch (InstanceNotFoundException e) {
			throw new GeneradorException(e);
		}
	}

	public static void main(String[] args) throws Exception {
		System.setProperty("org.xml.sax.driver", "org.apache.xerces.parsers.SAXParser");
		new Monitor();
	}
}
