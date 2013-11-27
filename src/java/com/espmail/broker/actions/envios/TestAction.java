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
import org.apache.commons.logging.LogFactory;

import com.espmail.broker.forms.envios.TestForm;

import com.espmail.broker.generacion.GeneradorEnvioTest;
import com.espmail.broker.generacion.GeneradorException;

import com.espmail.broker.modelo.Envio;
import com.espmail.broker.modelo.EnvioDao;

import com.espmail.utils.dao.DaoException;
import com.espmail.utils.dao.FactoriaDao;

/**
 * 
 * @author lcubillo
 */
public class TestAction extends LookupDispatchAction {

	private final Map map;

	/**
	 * Añade los datos en los mapas para los distintos idiomas las operaciones.
	 * 
	 */
	public TestAction() {
		this.map = new HashMap();
		this.map.put("envios.test.lanza", "lanza");
	}

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		return mapping.getInputForward();
	}

	public ActionForward lanza(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {
		TestForm formulario = (TestForm) form;
		int idEnvio = formulario.getIdEnvio();

		try {
			Envio envio = ((EnvioDao) FactoriaDao.getInstance()
					.getDao(EnvioDao.class)).findById(req.getRemoteUser(), idEnvio);
			
			if (envio == null) {
				res.sendError(HttpServletResponse.SC_NOT_FOUND);
				return null;
			}
         LogFactory.getLog(this.getClass()).info("Instanciamos GeneradorEnvioTest");
			(new GeneradorEnvioTest(envio, formulario.getEmails())).run();
         LogFactory.getLog(this.getClass()).info("Test Enviado");
      } catch (DaoException e) {
			throw new ServletException(e);
		} catch (GeneradorException e) {
			throw new ServletException(e);
		}
		
		return mapping.findForward("exito");
	}

	protected Map getKeyMethodMap() {
		return this.map;
	}

}
