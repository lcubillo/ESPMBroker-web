package com.espmail.broker.modelo;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.espmail.utils.set.Set;

public class EstadoEnviado implements Set, Serializable {

	private static final long serialVersionUID = 6890711299440665121L;

	private final static Map INSTANCES = new LinkedHashMap();

	public final static EstadoEnviado PENDIENTE = new EstadoEnviado("P");

	public final static EstadoEnviado ENVIADO = new EstadoEnviado("E");

	public final static EstadoEnviado FALLO = new EstadoEnviado("F");

	private final String codigo;

	private EstadoEnviado(String codigo) {
		this.codigo = codigo;
		INSTANCES.put(codigo, this);
	}

	public String getCodigo() {
		return this.codigo;
	}

	/**
	 * Devuelve sólo el código
	 */
	public String toString() {
		return this.codigo;
	}

	/*
	 * Devuelve todos los estados
	 */
	public static Collection values() {
		return INSTANCES.values();
	}

	/**
	 * Devuelve una instancia del estado con el código que se le pasa.
	 * 
	 * @param codigo
	 *            Código del estado a recuperar.
	 * @return Estado con el codigo dado.
	 */
	public static EstadoEnviado get(String codigo) {
		return (EstadoEnviado) INSTANCES.get(codigo);
	}
}
