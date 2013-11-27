package com.espmail.broker.modelo;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.espmail.utils.set.Set;

public class EstadoEnvio implements Set, Serializable {

	private static final long serialVersionUID = -3379225358506413685L;

	private final static Map INSTANCES = new LinkedHashMap();

	public final static EstadoEnvio PENDIENTE = new EstadoEnvio("P");

	public final static EstadoEnvio LISTO = new EstadoEnvio("L");
	
	public final static EstadoEnvio ENVIANDO = new EstadoEnvio("N");

	public final static EstadoEnvio ENVIADO = new EstadoEnvio("E");
	
	public final static EstadoEnvio ERROR = new EstadoEnvio("F");
	
	

	private final String codigo;

	private EstadoEnvio(String codigo) {
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
	public static EstadoEnvio get(String codigo) {
		return (EstadoEnvio) INSTANCES.get(codigo);
	}
}
