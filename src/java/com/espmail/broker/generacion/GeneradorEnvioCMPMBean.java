package com.espmail.broker.generacion;

import java.util.Date;

public interface GeneradorEnvioCMPMBean {

	long getTiempoPorMail();
	
	long getTiempoMail();
	
	long getTiempoUpdate();
	
	Date getInicio();
	
	Date getFin();
	
	int getEnviados();
	
	int getFallos();
	
	void stop();
	
	float getMediaIntentos();
	
	float getMediaTiempoSesion();
	
	float getMediaTiempoComposicion();
	
	float getMediaTiempoSend();
}
