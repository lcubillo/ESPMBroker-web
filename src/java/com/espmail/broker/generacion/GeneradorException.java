package com.espmail.broker.generacion;

public class GeneradorException extends RuntimeException {

	private static final long serialVersionUID = -2558830357944724163L;

	public GeneradorException(Throwable t) {
		super(t);
	}
	
	public GeneradorException(String message) {
		super(message);
	}
}
