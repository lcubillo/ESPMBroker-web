package com.espmail.broker.generacion;

import java.io.IOException;

public interface MonitorMBean {

	long getSleepTime();
	
	void setSleepTime(long time);
	
	int getMaxEnvios();
	
	void setMaxEnvios(int maxEnvios);

	int getEnviosActivos();

	void scanEnvios() throws GeneradorException;
	
	void saveEstado() throws IOException;
}
