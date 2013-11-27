package com.espmail.broker.modelo;

import java.sql.Timestamp;

import com.espmail.utils.TextUtils;

public class Usuario {
	
	private Long idEmail;

	private Integer idLista;

	private String email;

	private String nombre;

	private String apellidos;

	private String tratamiento;

	private Timestamp creacion;

	private Timestamp ultimaActividad;

	private String valor1;

	private String valor2;

	private String valor3;

	public Usuario(Number idEmail, Number idLista, String email, String nombre,
			String apellidos, String tratamiento, String creacion,
			String ultimaActividad, String valor1, String valor2, String valor3) {
		this.idEmail = new Long(idEmail.intValue());
		this.idLista = new Integer(idLista.intValue());
		this.email = email;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.tratamiento = tratamiento;
		this.creacion = TextUtils.asTimestamp(creacion);
		this.ultimaActividad = TextUtils.asTimestamp(ultimaActividad);
		this.valor1 = valor1;
		this.valor2 = valor2;
		this.valor3 = valor3;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Timestamp getCreacion() {
		return this.creacion;
	}

	public void setCreacion(Timestamp creacion) {
		this.creacion = creacion;
	}

	public Timestamp getUltimaActividad() {
		return this.ultimaActividad;
	}

	public void setUltimaActividad(Timestamp ultimaActividad) {
		this.ultimaActividad = ultimaActividad;
	}

	public Long getIdEmail() {
		return this.idEmail;
	}

	public void setIdEmail(Long idEmail) {
		this.idEmail = idEmail;
	}

	public Integer getIdLista() {
		return idLista;
	}

	public void setIdLista(Integer idLista) {
		this.idLista = idLista;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTratamiento() {
		return tratamiento;
	}

	public void setTratamiento(String tratamiento) {
		this.tratamiento = tratamiento;
	}

	public String getValor1() {
		return valor1;
	}

	public void setValor1(String valor1) {
		this.valor1 = valor1;
	}

	public String getValor2() {
		return valor2;
	}

	public void setValor2(String valor2) {
		this.valor2 = valor2;
	}

	public String getValor3() {
		return valor3;
	}

	public void setValor3(String valor3) {
		this.valor3 = valor3;
	}

}
