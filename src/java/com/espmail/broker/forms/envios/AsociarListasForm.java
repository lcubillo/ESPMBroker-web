package com.espmail.broker.forms.envios;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.espmail.broker.modelo.Lista;
import com.espmail.broker.modelo.ListaDao;

import com.espmail.utils.dao.DaoException;
import com.espmail.utils.dao.FactoriaDao;

public class AsociarListasForm extends ActionForm {

	private static final long serialVersionUID = -8415256539025670133L;
	
	private int idEnvio;
	private Lista[] listas;
	private Integer[] listasSeleccionadas;

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
	 * @return the listasSeleccionadas
	 */
	public Integer[] getListasSeleccionadas() {
		return listasSeleccionadas;
	}

	/**
	 * @param listasSeleccionadas the listasSeleccionadas to set
	 */
	public void setListasSeleccionadas(Integer[] listasSeleccionadas) {
		this.listasSeleccionadas = listasSeleccionadas;
	}

	/**
	 * @return the lista
	 */
	public Lista[] getListas() {
		return listas;
	}

	public void init(HttpServletRequest req) throws DaoException {
		ListaDao dao = (ListaDao) FactoriaDao.getInstance().getDao(ListaDao.class);
		this.listas = dao.findAll(req.getRemoteUser());
	}

	public ActionErrors validate(ActionMapping mapping,	HttpServletRequest req) {
		if (req.getParameter("summit") == null) {
			return null;
		}
		
		ActionErrors errors = null;

		if (this.listasSeleccionadas == null || this.listasSeleccionadas.length == 0) {
			try {
				init(req);
			} catch (DaoException e) {
				throw new RuntimeException(e);
			}
			
			errors = new ActionErrors();
			errors.add(ActionErrors.GLOBAL_MESSAGE,
					new ActionMessage("envios.listas.error"));
		}
		
		return errors;
	}
}
