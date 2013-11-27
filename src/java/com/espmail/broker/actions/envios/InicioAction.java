package com.espmail.broker.actions.envios;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.espmail.broker.modelo.Envio;
import com.espmail.broker.modelo.EnvioDao;
import com.espmail.broker.modelo.EstadisticaDao;
import com.espmail.utils.dao.FactoriaDao;



public class InicioAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		String idCliente = req.getRemoteUser().trim();
		EnvioDao dao = ( EnvioDao) FactoriaDao.getInstance().getDao(EnvioDao.class);
		Envio[] envios =dao.find(idCliente);
		req.setAttribute("envios", envios);
		
		EstadisticaDao dao2 = (EstadisticaDao) FactoriaDao.getInstance().getDao(EstadisticaDao.class);
		List estadistica = dao2.dameResumen(idCliente);	
		
		req.setAttribute("estadistica", estadistica);
		
		return mapping.getInputForward();
	}
}