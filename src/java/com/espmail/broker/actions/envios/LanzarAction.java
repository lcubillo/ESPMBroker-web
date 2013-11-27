package com.espmail.broker.actions.envios;

import java.io.IOException;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.sql.Date;
import java.sql.Timestamp;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.apache.struts.actions.LookupDispatchAction;

import sun.util.logging.resources.logging;

import com.espmail.broker.forms.envios.LanzarForm;

import com.espmail.broker.modelo.EnvioDao;

import com.espmail.utils.TextUtils;

import com.espmail.utils.dao.DaoException;
import com.espmail.utils.dao.FactoriaDao;


public class LanzarAction extends LookupDispatchAction {

	private final Map map;

	/**
	 * Añade los datos en los mapas para los distintos idiomas las operaciones.
	 * 
	 */
	public LanzarAction() {
		this.map = new HashMap();
		this.map.put("envios.lanzar.submit", "lanzar");
	}

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {
		LanzarForm formulario = (LanzarForm) form;
		String stEnvio = req.getParameter("idEnvio");
		
		if (TextUtils.isEmpty(stEnvio)) {
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}

		int idEnvio = Integer.parseInt(stEnvio);

		try {
			EnvioDao dao = (EnvioDao) FactoriaDao.getInstance()
				.getDao(EnvioDao.class);
			Integer[] listas = dao.findListas(req.getRemoteUser(), idEnvio);
			
			if (listas == null || listas.length == 0) {
				formulario.setError(true);
			} else {
				Calendar calendar = Calendar.getInstance();
				formulario.setIdEnvio(idEnvio);
				formulario.setAnho(calendar.get(Calendar.YEAR));
				formulario.setMes(calendar.get(Calendar.MONTH)+1);
				formulario.setDia(calendar.get(Calendar.DAY_OF_MONTH));
				formulario.setHoras(calendar.get(Calendar.HOUR_OF_DAY));											
				formulario.setMinutos(0);
			}
		} catch (DaoException e) {
			throw new ServletException(e);
		}
		
		return mapping.getInputForward();
	}

	public ActionForward lanzar(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {
		LanzarForm formulario = (LanzarForm) form;
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, formulario.getAnho());
		cal.set(Calendar.MONTH, formulario.getMes()-1);
		cal.set(Calendar.DAY_OF_MONTH, formulario.getDia());
		cal.set(Calendar.HOUR_OF_DAY, formulario.getHoras());
		cal.set(Calendar.MINUTE, formulario.getMinutos());
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		int idEnvio = formulario.getIdEnvio();
		
		try {
			EnvioDao dao = (EnvioDao) FactoriaDao.getInstance()
				.getDao(EnvioDao.class);			
		
			 java.text.DateFormat df = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			 String fechaEnvio = df.format(new Date(cal.getTimeInMillis()));
			Timestamp diaEnvio = TextUtils.asTimestamp(fechaEnvio);
			
			dao.insertFechaEnvio(req.getRemoteUser(), idEnvio, diaEnvio);			
			dao.prepararEnvio(req.getRemoteUser(), idEnvio);
		} catch (DaoException e) {
			throw new ServletException(e);
		}
		
		return new ActionForward("/app/envios/datos.do?idEnvio=" + idEnvio, true);
	}

	protected Map getKeyMethodMap() {
		return this.map;
	}
}
