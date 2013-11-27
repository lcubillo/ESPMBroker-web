package com.espmail.broker.forms.envios;

import org.apache.struts.action.ActionForm;

/**
 * 
 * @author lcubillofreire
 */
public class LanzarForm extends ActionForm {

	private static final long serialVersionUID = -1503293333260441680L;
	
	private int idEnvio;
	private int dia;
	private int mes;
	private int anho;
	private int horas;
	private int minutos;
	private boolean error = false;

	public int getIdEnvio() {
		return idEnvio;
	}

	public void setIdEnvio(int idEnvio) {
		this.idEnvio = idEnvio;
	}

	public int getDia() {
		return this.dia;
	}

	public void setDia(int dia) {
		this.dia = dia;
	}

	public int getMes() {
		return this.mes;
	}

	public void setMes(int mes) {
		this.mes = mes;
	}

	public int getAnho() {
		return this.anho;
	}

	public void setAnho(int anho) {
		this.anho = anho;
	}

	public int getHoras() {
		return this.horas;
	}

	public void setHoras(int horas) {
		this.horas = horas;
	}

	public int getMinutos() {
		return this.minutos;
	}

	public void setMinutos(int minutos) {
		this.minutos = minutos;
	}
	
	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

}
