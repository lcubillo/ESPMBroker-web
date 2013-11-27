package com.espmail.broker.generacion;

public class ConexionException extends GeneradorException {

	private static final long serialVersionUID = 3964031937701775517L;

	public ConexionException(int idEnvio) {
		super("Problema de conexión en el envio " + idEnvio);
	}
}
