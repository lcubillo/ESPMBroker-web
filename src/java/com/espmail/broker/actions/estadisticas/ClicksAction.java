package com.espmail.broker.actions.estadisticas;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.espmail.broker.modelo.EstadisticaDao;
import com.espmail.utils.dao.FactoriaDao;



public class ClicksAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) throws Exception {

			String idCliente = req.getRemoteUser();
			String idEnvio = req.getParameter("idEnvio");

			EstadisticaDao dao = (EstadisticaDao) FactoriaDao.getInstance().getDao(EstadisticaDao.class);

			if (idEnvio != null){
				List clicks = dao.dameClicks(idCliente, Integer.parseInt(idEnvio));
				req.setAttribute("clicks", clicks);

				List resumenEnvioList = dao.dameResumenEnvio(idCliente, Integer.parseInt(idEnvio));
				
				if (resumenEnvioList != null){
					Map resumenEnvio = (Map) resumenEnvioList.get(0);
					req.setAttribute("resumenEnvio", resumenEnvio);
				}				
			}
			
			List enviosEnviados = dao.dameEnviosEnviados(idCliente);
			req.setAttribute("enviosEnviados", enviosEnviados);			
			return mapping.getInputForward();
	}
}