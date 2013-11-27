package com.espmail.broker.forms.personal;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.espmail.broker.modelo.ClienteDao;

import com.espmail.utils.dao.DaoException;
import com.espmail.utils.dao.FactoriaDao;

public class CambioPasswordForm extends ValidatorForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private String oldPassword;
	private String newPassword1;
	private String newPassword2;
	
	/**
	 * @return the newPassword1
	 */
	public String getNewPassword1() {
		return newPassword1;
	}
	/**
	 * @param newPassword1 the newPassword1 to set
	 */
	public void setNewPassword1(String newPassword1) {
		this.newPassword1 = newPassword1;
	}
	/**
	 * @return the newPassword2
	 */
	public String getNewPassword2() {
		return newPassword2;
	}
	/**
	 * @param newPassword2 the newPassword2 to set
	 */
	public void setNewPassword2(String newPassword2) {
		this.newPassword2 = newPassword2;
	}
	/**
	 * @return the oldPassword
	 */
	public String getOldPassword() {
		return oldPassword;
	}
	/**
	 * @param oldPassword the oldPassword to set
	 */
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest req) {
		
		if (req.getParameter("summit") == null) {
			return null;
		}
				
		ActionErrors errors = super.validate(mapping, req);
		
		if (errors == null || errors.isEmpty()) {
			try {
				
				if (errors == null) {
					errors = new ActionErrors();
				}
				
				String idCliente=req.getRemoteUser();
				
				ClienteDao dao = (ClienteDao) FactoriaDao.getInstance().getDao(ClienteDao.class);
				
				List list = (List) dao.getPassword(idCliente);
				
				String old =  (String) ((Map)list.get(0)).get("password");

				if(!this.newPassword1.equals(this.newPassword2)){
					errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("cambioPassword.diferentNew"));					
				}			
				if(!old.equals(this.oldPassword)){
					errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("cambioPassword.wrongOldPw"));					
				}
				
			} catch (DaoException e) {
				throw new RuntimeException(e);
			}
		}
		
		return errors;
	}	
}