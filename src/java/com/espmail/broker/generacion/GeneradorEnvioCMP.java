package com.espmail.broker.generacion;

import java.net.ConnectException;
import java.util.Date;

import javax.mail.MessagingException;

import javax.management.ListenerNotFoundException;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcaster;
import javax.management.NotificationBroadcasterSupport;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;

import com.espmail.broker.modelo.*;

import com.espmail.utils.dao.DaoException;
import com.espmail.utils.dao.FactoriaDao;
import com.espmail.utils.dao.Transaccion;
import com.espmail.utils.TextUtils;

public class GeneradorEnvioCMP extends GeneradorEnvio 
		implements GeneradorEnvioCMPMBean, NotificationBroadcaster  {

	protected final static String QUERY
		= "select usuarios.id_email, usuarios.id_lista, email, nombre, apellidos,"
		+ " tratamiento, etiqueta_t1, t1, etiqueta_t2, t2,etiqueta_t3, t3"
		+ " from usuarios, envios_listas, listas, enviados"
		+ " where usuarios.id_lista = envios_listas.id_lista"
		+ " and enviados.id_lista = usuarios.id_lista"
		+ " and enviados.id_email = usuarios.id_email"
		+ " and enviados.id_envio = envios_listas.id_envio"
		+ " and enviados.estado = 'P'"
		+ " and listas.id_lista = usuarios.id_lista and envios_listas.id_envio = ";
	
	private static final String NOTIFICACION
		= "com.espmail.broker.generacion.GeneradorEnvioCMP.finaliza";

	private final NotificationBroadcasterSupport broadcaster;
	private final Transaccion transaccion;
	private final String query;
	private final Date inicio;
	private Date fin;
	private int enviados = 0;
	private int intentos = 0;
	private int fallos = 0;
	private long notificationSequence = 0;
	private long timeMail = 0;
	private long timeUpdate = 0;
	private final UsuarioDao dao;
	private final int idEnvio;
   

   public GeneradorEnvioCMP(Envio envio) throws GeneradorException {
		super(true, envio);
		this.query = QUERY + envio.getIdEnvio();
		this.broadcaster = new NotificationBroadcasterSupport();
		this.inicio = new Date();
		this.idEnvio = envio.getIdEnvio();
		try {
			this.dao = (UsuarioDao) FactoriaDao.getInstance().getDao(UsuarioDao.class);
			this.transaccion = new Transaccion();
		} catch (DaoException e) {
			throw new GeneradorException(e);
		}
	}

	protected void envia(long idEmail, int idLista, String email,
			String asunto, String html, String texto)
			throws GeneradorException {
		envia(idEmail, idLista, email, asunto, html, texto, 0);
	}

	protected final void envia(long idEmail, int idLista, String email,
			String asunto, String html, String texto, int intento)
			throws GeneradorException {
		try {
			if (intento == 0){
				this.enviados++;
			}
			
			try {
				long init = System.currentTimeMillis();
				sendMail(idEmail, idLista, email, asunto, html, texto);
				long next = System.currentTimeMillis();
				this.timeMail += next - init;
				this.dao.cambiaEstadoEnviado(idEmail, this.idEnvio,
						EstadoEnviado.ENVIADO);
				this.timeUpdate += System.currentTimeMillis() - next;
			} catch (MessagingException e) {
				Exception next = e.getNextException();

				error(e);
				
				if (next != null && next instanceof ConnectException) {
					// Si falla la conexión con el correo dormimos el proceso
					// 2 segundos, a la tercer intento paramos el envio.

					try {
						Thread.sleep(2000);
					} catch (InterruptedException e1) {
						// Nada
					}
					
					if (intento < 2) {
						this.intentos++;
						envia(idEmail, idLista, email, asunto, html, texto, intento + 1);
					} else {
						throw new ConexionException(this.idEnvio);
					}
				} else {
					this.dao.cambiaEstadoEnviado(idEmail, this.idEnvio,
							EstadoEnviado.FALLO);
					this.fallos++;
					error(e);
				}
			}
			
			if (this.enviados % 100 == 0) {
				this.transaccion.commit();
			}
		} catch (DaoException e) {
			throw new GeneradorException(e);
		}
	}

	public long getTiempoPorMail() {
		return ((this.fin == null? System.currentTimeMillis() : this.fin.getTime())
				- this.inicio.getTime()) / (this.enviados == 0? 1 : this.enviados);
	}
	
	public long getTiempoMail() {
		return this.timeMail;
	}
	
	public long getTiempoUpdate() {
		return this.timeUpdate;
	}
	
	public Date getInicio() {
		return this.inicio;
	}
	
	public Date getFin() {
		return this.fin;
	}
	
	public int getEnviados() {
		return this.enviados;
	}
	
	public int getFallos() {
		return this.fallos;
	}

	public void addNotificationListener(NotificationListener listener,
			NotificationFilter filter, Object handback) {
		this.broadcaster.addNotificationListener(listener, filter, handback);
	}
	
	public float getMediaIntentos(){
		return ((float)intentos)/((float)enviados);
	}

	public float getMediaTiempoSesion(){
		return (float)((float)this.tiempoSession/(float)enviados);
	}
	
	public float getMediaTiempoComposicion(){
		return (float)((float)this.tiempoComposicion/(float)enviados);
	}	
	
	public float getMediaTiempoSend(){
		return (float)((float)this.tiempoSend/(float)enviados);
	}	
	
	public void removeNotificationListener(NotificationListener listener)
			throws ListenerNotFoundException {
		this.broadcaster.removeNotificationListener(listener);
	}

	public MBeanNotificationInfo[] getNotificationInfo() {
		return new MBeanNotificationInfo[] { new MBeanNotificationInfo(
				new String[] { NOTIFICACION },
				Notification.class.getName(), "Finaliza"
		) };
	}

	protected String getQuery() {
		return this.query;
	}

   protected void finaliza(boolean exito) throws GeneradorException {
		this.fin = new Date();

		try {
			EnvioDao dao = (EnvioDao) FactoriaDao.getInstance().getDao(EnvioDao.class);
			dao.cambiaEstado(getIdEnvio(), exito? EstadoEnvio.ENVIADO: EstadoEnvio.ERROR);
			this.transaccion.commit();
			this.transaccion.close();
		} catch (DaoException e) {
			throw new GeneradorException(e);
		}
		
		this.broadcaster.sendNotification(new Notification(NOTIFICACION,
				this, ++this.notificationSequence,
				"GeneradorEnvio " + getIdEnvio() + " finalizado."
		));
	}
}
