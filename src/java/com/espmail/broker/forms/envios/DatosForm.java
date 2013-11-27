package com.espmail.broker.forms.envios;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import org.apache.struts.validator.ValidatorForm;

import com.espmail.broker.modelo.EnvioDao;

import com.espmail.utils.dao.DaoException;
import com.espmail.utils.dao.FactoriaDao;

public class DatosForm extends ValidatorForm {

	private static final long serialVersionUID = 8983362889105136186L;
	
	private int idEnvio;
	private String etiqueta;
	private String descripcion;
	private String html;
	private String texto;
	private String remitente;
	private String asunto;
	private String replyTo;
	
	
	/**
	 * @return the asunto
	 */
	public String getAsunto() {
		return asunto;
	}
	/**
	 * @param asunto the asunto to set
	 */
	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}
	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	/**
	 * @return the etiqueta
	 */
	public String getEtiqueta() {
		return etiqueta;
	}
	/**
	 * @param etiqueta the etiqueta to set
	 */
	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}
	/**
	 * @return the html
	 */
	public String getHtml() {
		return html;
	}
	/**
	 * @param html the html to set
	 */
	public void setHtml(String html) {
		this.html = html;
	}

	/**
	 * @return the idEnvio
	 */
	public int getIdEnvio() {
		return idEnvio;
	}
	/**
	 * @param idEnvio the idEnvio to set
	 */
	public void setIdEnvio(int idEnvio) {
		this.idEnvio = idEnvio;
	}
	/**
	 * @return the remitente
	 */
	public String getRemitente() {
		return remitente;
	}
	/**
	 * @param remitente the remitente to set
	 */
	public void setRemitente(String remitente) {
		this.remitente = remitente;
	}
	/**
	 * @return the replyTo
	 */
	public String getReplyTo() {
		return replyTo;
	}
	/**
	 * @param replyTo the replyTo to set
	 */
	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}
	/**
	 * @return the texto
	 */
	public String getTexto() {
		return texto;
	}
	/**
	 * @param texto the texto to set
	 */
	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	
		

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest req) {
		if (req.getParameter("summit") == null) {
			return null;
		}
		
		ActionErrors errors = super.validate(mapping, req);
		
		if (errors == null || errors.isEmpty()) {
			try {
				EnvioDao dao = (EnvioDao) FactoriaDao.getInstance().getDao(EnvioDao.class);
				
				if (dao.existeEtiqueta(req.getRemoteUser(), this.idEnvio, this.etiqueta)) {
					if (errors == null) {
						errors = new ActionErrors();
					}
					
					errors.add(ActionErrors.GLOBAL_MESSAGE,
							new ActionMessage("envios.datos.etiqueta.error"));
				}
			} catch (DaoException e) {
				throw new RuntimeException(e);
			}
		}
		
		return errors;
	}
}