package com.espmail.broker.actions.envios;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.apache.struts.actions.LookupDispatchAction;

import com.espmail.broker.forms.envios.DatosForm;

import com.espmail.broker.modelo.Envio;
import com.espmail.broker.modelo.EnvioDao;
import com.espmail.broker.modelo.EstadoEnvio;
import com.espmail.broker.modelo.Lista;
import com.espmail.broker.modelo.ListaDao;

import com.espmail.utils.dao.DaoException;
import com.espmail.utils.dao.FactoriaDao;

public class DatosAction extends LookupDispatchAction {

	private final Map map;

	/**
	 * Añade los datos en los mapas para los distintos idiomas las operaciones.
	 * 
	 */
	public DatosAction() {
		this.map = new HashMap();
		this.map.put("envios.datos.submit", "guardar");
	}

	/**
	 * Rellena los datos del webmaster
	 */
	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {
		DatosForm formulario = (DatosForm) form;
		int idEnvio = formulario.getIdEnvio();
		
		if (idEnvio != 0) {
			Envio envio = null;
			Lista[] listas = null;
			Lista lista = null;

			try {
				EnvioDao envioDao = (EnvioDao) FactoriaDao.getInstance()
					.getDao(EnvioDao.class);
				envio = envioDao.findById(req.getRemoteUser(), idEnvio);
				ListaDao listaDao = (ListaDao) FactoriaDao.getInstance().getDao(ListaDao.class);
				listas = listaDao.findByEnvio(req.getRemoteUser(), idEnvio);
				if (listas!=null) 
					lista = listaDao.findById(req.getRemoteUser(), listas[0].getIdLista());
				
			} catch (DaoException e) {
				throw new ServletException(e);
			}

			if (envio == null) {
				res.sendError(HttpServletResponse.SC_NOT_FOUND);
				return null;
			} else if (envio.getEstado() == EstadoEnvio.PENDIENTE) {
				formulario.setAsunto(envio.getAsunto());
				formulario.setDescripcion(envio.getDescripcion());
				formulario.setEtiqueta(envio.getEtiqueta());
				formulario.setHtml(envio.getHtml());
				formulario.setRemitente(envio.getRemitente());
				formulario.setReplyTo(envio.getReplyTo());
				formulario.setTexto(envio.getTexto());
		
				//Si está aquí se supone que las listas son compatibles, asi que leere los nombres
				//de la primera lista
				if (lista!=null)
				req.setAttribute("listas", lista);
			} else {
				req.setAttribute("envio", envio);
				req.setAttribute("listas", listas);
				return mapping.findForward("resumen");
			}
		}
		
		return mapping.getInputForward();
	}

	public ActionForward guardar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		DatosForm formulario = (DatosForm) form;
		int idEnvio = formulario.getIdEnvio();
		Envio envio = new Envio();
		envio.setAsunto(formulario.getAsunto());
		envio.setDescripcion(formulario.getDescripcion());
		envio.setEtiqueta(formulario.getEtiqueta());
		if (formulario.getHtml()==null)
				envio.setHtml("");
		else
			envio.setHtml(formulario.getHtml());
		envio.setIdCliente(request.getRemoteUser());
		envio.setIdEnvio(idEnvio);
		envio.setRemitente(formulario.getRemitente());
		envio.setReplyTo(formulario.getReplyTo());
		if(formulario.getTexto()==null)
			envio.setTexto("");
		else
			envio.setTexto(formulario.getTexto());
		
		try {
			EnvioDao dao = (EnvioDao) FactoriaDao.getInstance()
				.getDao(EnvioDao.class);

			if (idEnvio == 0) {
				idEnvio = dao.getId();
				envio.setIdEnvio(idEnvio);
				dao.insert(envio);
			} else {
				dao.update(envio);
			}
		} catch (DaoException e) {
			throw new ServletException(e);
		}
		
		return new ActionForward("/app/envios/preview.do?idEnvio=" + idEnvio, true);
	}

	protected Map getKeyMethodMap() {
		return this.map;
	}

}
