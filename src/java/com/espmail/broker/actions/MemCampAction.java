package com.espmail.broker.actions;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.espmail.broker.generacion.GeneradorEnvioMemCamp;
import com.espmail.broker.modelo.Envio;
import com.espmail.broker.modelo.EnvioDao;
import com.espmail.utils.Base64Coder;
import com.espmail.utils.TextUtils;
import com.espmail.utils.dao.DaoException;
import com.espmail.utils.dao.FactoriaDao;

public class MemCampAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
    		HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
 
    	String param = req.getParameter("a");
    	
		if (TextUtils.isEmpty(param)) {
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}

		String[] args = new String(Base64Coder.decode(param)).split("·");
		Envio envio = null;

		try {
			EnvioDao dao = (EnvioDao) FactoriaDao.getInstance()
				.getDao(EnvioDao.class);
			envio = dao.findByIdPrivado(Integer.parseInt(args[0]));
		} catch (DaoException e) {
			throw new ServletException(e);
		}

		GeneradorEnvioMemCamp gen = new GeneradorEnvioMemCamp(envio, 
				Long.parseLong(args[1]), Integer.parseInt(args[2]));
		gen.run();
		String html = gen.getHtml();
		
		if (html != null) {
			OutputStream os = res.getOutputStream();
			os.write(html.getBytes());
			os.close();
		}
		
		return null;
    }
}
