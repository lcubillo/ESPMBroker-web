package com.espmail.broker.modelo;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.espmail.utils.set.Set;

public class EstadoLista implements Set, Serializable {

	private static final long serialVersionUID = -7891356035009383622L;

	private final static Map INSTANCES = new LinkedHashMap();

	public final static EstadoLista PENDIENTE = new EstadoLista("P");

	public final static EstadoLista LISTO = new EstadoLista("L");

	private final String codigo;

	private EstadoLista(String codigo) {
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
	public static EstadoLista get(String codigo) {
		return (EstadoLista) INSTANCES.get(codigo);
	}
}
