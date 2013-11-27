package com.espmail.broker.forms.envios;

import org.apache.struts.action.ActionForm;

import com.espmail.broker.modelo.Envio;

public class ListadoForm extends ActionForm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4873674554928856963L;

	private Envio[] envios;

	/**
	 * @return the envios
	 */
	public Envio[] getEnvios() {
		return envios;
	}

	/**
	 * @param envios the envios to set
	 */
	public void setEnvios(Envio[] envios) {
		this.envios = envios;
	}
 
}
