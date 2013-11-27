package com.espmail.broker.actions.listas;

import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.apache.struts.actions.LookupDispatchAction;

import com.espmail.broker.forms.listas.BusquedaEmailForm;

import com.espmail.broker.modelo.UsuarioDao;

import com.espmail.utils.RequestUtils;

import com.espmail.utils.dao.DaoException;
import com.espmail.utils.dao.FactoriaDao;

public class BusquedaEmailAction extends LookupDispatchAction {
	
	private final Map map;

	/**
	 * Añade los datos en los mapas para los distintos idiomas las operaciones.
	 * 
	 */
	public BusquedaEmailAction() {
		this.map = new HashMap();
		this.map.put("listas.busqueda.eliminar", "eliminar");
	}
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		BusquedaEmailForm formulario = (BusquedaEmailForm) form;
        String idCliente = req.getRemoteUser();
        String email = formulario.getEmail();

        if (email != null) {
        	try {
	        	UsuarioDao dao = (UsuarioDao) FactoriaDao.getInstance().getDao(UsuarioDao.class);
	        	List usuarios = dao.findByEmail(idCliente, email);
	
	        	if(usuarios != null) {
	        		formulario.setUsuarios(usuarios);
	        	}
        	} catch (DaoException e) {
        		throw new ServletException(e);
        	}
        }

        return mapping.getInputForward();
	}
	
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		BusquedaEmailForm formulario = (BusquedaEmailForm) form;
		String idCliente = req.getRemoteUser();
		String[] seleccionados = formulario.getSeleccionados();
		String ip = RequestUtils.getIp(req);

		try {
			UsuarioDao dao = (UsuarioDao) FactoriaDao.getInstance().getDao(UsuarioDao.class);
	
			for (int i = 0; i < seleccionados.length; i++) {
				String[] listaEmail = seleccionados[i].split("_");
	
				dao.eliminar(idCliente, Integer.parseInt(listaEmail[0]),
						Long.parseLong(listaEmail[1]), ip);
			}
    	} catch (DaoException e) {
    		throw new ServletException(e);
    	}

        return mapping.findForward("exito");
	}

	protected Map getKeyMethodMap() {
		return this.map;
	}
}
