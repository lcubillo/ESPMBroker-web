package com.espmail.lanzador;

import com.espmail.broker.generacion.GeneradorException;

import java.io.IOException;

/**
 * ESPMail
 * User: Luis
 */
public interface LanzadorMBean {
   
   public void stopLanzador();
   
   public long getSleepTime();

	public void setSleepTime(long time);

	public int getMaxEnvios();

	public void init(String configuration);

	public int getEnviosActivos();

   public long getTiempoMedioEnvio();

}
