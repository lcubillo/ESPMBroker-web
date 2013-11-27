package com.espmail.broker.forms.listas;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import org.apache.struts.validator.ValidatorForm;

public class BusquedaEmailForm extends ValidatorForm {
	
	private static final long serialVersionUID = 1L;

	private String email;
	private List usuarios;
	private String[] seleccionados;
	
	public List getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List usuarios) {
		this.usuarios = usuarios;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String[] getSeleccionados() {
		return this.seleccionados;
	}

	public void setSeleccionados(String[] seleccionados) {
		this.seleccionados = seleccionados;
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest req) {
		if (req.getParameter("summit") == null) {
			return null;
		}

		return super.validate(mapping, req);
	}

}
