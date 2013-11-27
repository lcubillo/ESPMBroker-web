package com.espmail.broker.forms.envios;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.espmail.utils.TextUtils;

/**
 * 
 * @author lcubillo
 */
public class TestForm extends ValidatorForm {

	private static final long serialVersionUID = -4981001789536032750L;
	
	private int idEnvio;
	private String[] emails = new String[5];
	
	public int getIdEnvio() {
		return this.idEnvio;
	}

	public void setIdEnvio(int idEnvio) {
		this.idEnvio = idEnvio;
	}

	public String[] getEmails() {
		return this.emails;
	}

	public void setEmails(String[] emails) {
		this.emails = emails;
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest req) {
		if (req.getParameter("summit") == null) {
			return null;
		}
		
		ActionErrors errors = super.validate(mapping, req);
		
		if (errors == null || errors.isEmpty()) {
			boolean hayAlguno = false;
			
			for (int i = 0; i < this.emails.length; i++) {
				if (!TextUtils.isEmpty(this.emails[i])) {
					hayAlguno = true;
					break;
				}
			}
			
			if (!hayAlguno) {
				if (errors == null) {
					errors = new ActionErrors();
				}
				
				errors.add(ActionErrors.GLOBAL_MESSAGE,
						new ActionMessage("envios.test.emails.error"));
			}
		}
		
		return errors;
	}
}
