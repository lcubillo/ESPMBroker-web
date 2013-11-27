package com.espmail.broker.modelo;

import java.sql.Timestamp;

import com.espmail.utils.TextUtils;

public class Lista {
	
	private int idLista;
	private String idCliente;
	private String nombre;
	private int numeroRegistros;
	private EstadoLista estado;
	private String etiqueta1;
	private String etiqueta2;
	private String etiqueta3;
	private Timestamp creacion;
	private Timestamp baja;
	
	public Lista(Number idLista, String idCliente, String nombre, 
			Number numeroRegistros, String estado, String etiqueta1, String etiqueta2,
			String etiqueta3, String creacion, String baja) {
		this.idLista = idLista.intValue();
		this.idCliente = idCliente;
		this.nombre = nombre;
		this.numeroRegistros = numeroRegistros == null? 0 : numeroRegistros.intValue();
		this.estado = EstadoLista.get(estado);
		this.etiqueta1 = etiqueta1;
		this.etiqueta2 = etiqueta2;
		this.etiqueta3 = etiqueta3;
		this.creacion = creacion != null? TextUtils.asTimestamp(creacion) : null;
		this.baja = baja != null? TextUtils.asTimestamp(baja) : null;;
	}

    public Lista() {
    }

	public EstadoLista getEstado() {
		return estado;
	}
	public void setEstado(EstadoLista estado) {
		this.estado = estado;
	}
	public String getEtiqueta1() {
		return etiqueta1;
	}
	public void setEtiqueta1(String etiqueta1) {
		this.etiqueta1 = etiqueta1;
	}
	public String getEtiqueta2() {
		return etiqueta2;
	}
	public void setEtiqueta2(String etiqueta2) {
		this.etiqueta2 = etiqueta2;
	}
	public String getEtiqueta3() {
		return etiqueta3;
	}
	public void setEtiqueta3(String etiqueta3) {
		this.etiqueta3 = etiqueta3;
	}
	public Timestamp getBaja() {
		return baja;
	}
	public void setBaja(Timestamp baja) {
		this.baja = baja;
	}
	public Timestamp getCreacion() {
		return creacion;
	}
	public void setCreacion(Timestamp creacion) {
		this.creacion = creacion;
	}
	public String getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(String idCliente) {
		this.idCliente = idCliente;
	}
	public int getIdLista() {
		return idLista;
	}
	public void setIdLista(int idLista) {
		this.idLista = idLista;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getNumeroRegistros() {
		return numeroRegistros;
	}
	public void setNumeroRegistros(int numeroRegistros) {
		this.numeroRegistros = numeroRegistros;
	}
	
	
}
