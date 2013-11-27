package com.espmail.broker.actions.listas;

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

import com.espmail.broker.modelo.Lista;
import com.espmail.broker.modelo.ListaDao;

import com.espmail.utils.TextUtils;

import com.espmail.utils.dao.DaoException;
import com.espmail.utils.dao.FactoriaDao;

public class ListadoAction extends LookupDispatchAction {

	private final Map map;

	/**
	 * Añade los datos en los mapas para los distintos idiomas las operaciones.
	 * 
	 */
	public ListadoAction() {
		this.map = new HashMap();
		this.map.put("listas.listado.borrar", "borrar");
	}

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		
			String idCliente=req.getRemoteUser();

			try {
				ListaDao dao = (ListaDao) FactoriaDao.getInstance().getDao(ListaDao.class);
				req.setAttribute("listas", dao.findAll(idCliente));
        	} catch (DaoException e) {
        		throw new ServletException(e);
        	}
			
			return mapping.getInputForward();
	}
	
	public ActionForward borrar(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        String idCliente = req.getRemoteUser();
        String stLista = req.getParameter("idLista");
        boolean encontrado = false;
        
        if (!TextUtils.isEmpty(stLista)) {
        	int idLista = Integer.parseInt(stLista);
        	
        	try {
	        	ListaDao dao = (ListaDao) FactoriaDao.getInstance().getDao(ListaDao.class);
	            Lista lista = dao.findById(idCliente, idLista);
	            
	            if (lista != null) {
	            	encontrado = true;
	            	dao.eliminar(idCliente, idLista);
	            }
        	} catch (DaoException e) {
        		throw new ServletException(e);
        	}
        }

        if (!encontrado) {
        	res.sendError(HttpServletResponse.SC_NOT_FOUND);
        	return null;
        }

        return mapping.findForward("exito");
	}

	protected Map getKeyMethodMap() {
		return this.map;
	}
}