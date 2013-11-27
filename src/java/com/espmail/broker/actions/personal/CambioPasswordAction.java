package com.espmail.broker.actions.personal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.espmail.broker.forms.personal.CambioPasswordForm;
import com.espmail.broker.modelo.ClienteDao;
import com.espmail.utils.dao.FactoriaDao;


public class CambioPasswordAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) throws Exception {
	
		
		
		CambioPasswordForm formulario = (CambioPasswordForm) form;
		if (formulario.getOldPassword()!=null){
			String idCliente=req.getRemoteUser();
			ClienteDao dao = (ClienteDao) FactoriaDao.getInstance().getDao(ClienteDao.class);
			
			dao.cambiarPassword(idCliente, formulario.getOldPassword(),formulario.getNewPassword1());
	
			return mapping.findForward("exito");
		}
		return mapping.getInputForward();
	}

}