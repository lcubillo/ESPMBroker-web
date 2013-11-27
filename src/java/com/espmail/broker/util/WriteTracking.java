package com.espmail.broker.util;

import java.io.File;
import java.io.FileOutputStream;


/**
 * User: Luis Cubillo Date: 13-sep-2009 Time: 19:47:20
 */
public class WriteTracking {

	private static FileOutputStream fTraking;

	private static WriteTracking instancia;

	private WriteTracking() {

	}

	public static WriteTracking getInstance() {
		if (instancia == null) {
			instancia = new WriteTracking();
		}

		return instancia;
	}

	public synchronized boolean write(String tracking) {
		try {
			fTraking = new FileOutputStream(
					"/opt/asp/tracking/fTracking.sql", true);
			fTraking.write(tracking.getBytes());
			fTraking.flush();
            fTraking.close();
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	public static void main(String args[]) {

		WriteTracking wt = WriteTracking.getInstance();	
		wt.write("Esto es lo que escribo en el fichero para realizar una prueba");
		File file = new File("/opt/asp/tracking/fTracking.sql");
		file.delete();
		wt.write("Esto es la segunda linea, tras borrar");

	}

}
