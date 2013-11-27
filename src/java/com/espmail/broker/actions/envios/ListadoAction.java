package com.espmail.broker.actions.envios;

import java.io.IOException;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.espmail.broker.forms.envios.ListadoForm;

import com.espmail.broker.modelo.EnvioDao;

import com.espmail.utils.dao.DaoException;
import com.espmail.utils.dao.FactoriaDao;

public class ListadoAction extends Action {

	/**
	 * Rellena los datos del webmaster
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		ListadoForm formulario = (ListadoForm) form;

		try {
			EnvioDao dao = (EnvioDao) FactoriaDao.getInstance().getDao(
					EnvioDao.class);
			formulario.setEnvios(dao.find(req.getRemoteUser()));

		} catch (DaoException e) {
			throw new ServletException(e);
		}

		return mapping.getInputForward();
	}
}
