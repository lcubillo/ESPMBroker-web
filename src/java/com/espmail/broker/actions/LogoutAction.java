package com.espmail.broker.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
/**
 * 
 * @author Luis Cubillo.
 *
 */
public class LogoutAction extends Action {
/**
 * Finaliza la sesion y reenvia a www.espmail.es.
 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) throws Exception {

		req.getSession().invalidate();
		res.sendRedirect("/");
		
		return null;
	}
}
