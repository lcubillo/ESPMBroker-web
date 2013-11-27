package com.espmail.broker.actions.envios;

import java.io.IOException;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.espmail.broker.modelo.Envio;
import com.espmail.broker.modelo.EnvioDao;

import com.espmail.utils.TextUtils;
import com.espmail.utils.dao.DaoException;
import com.espmail.utils.dao.FactoriaDao;

public class PreviewAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {
		String stEnvio = req.getParameter("idEnvio");
		
		if (TextUtils.isEmpty(stEnvio)) {
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		
		int idEnvio = Integer.parseInt(stEnvio);

		try {
			EnvioDao dao = (EnvioDao) FactoriaDao.getInstance()
				.getDao(EnvioDao.class);
			Envio envio = dao.findById(req.getRemoteUser(), idEnvio);
			
			if (envio == null) {
				res.sendError(HttpServletResponse.SC_NOT_FOUND);
				return null;
			}

			req.setAttribute("envio", envio);
		} catch (DaoException e) {
			throw new ServletException(e);
		}
		
		return mapping.getInputForward();
	}

}
