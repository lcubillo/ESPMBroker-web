package com.espmail.broker;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.beanutils.ConvertUtils;

import com.espmail.broker.modelo.EstadoEnviado;
import com.espmail.broker.modelo.EstadoEnvio;
import com.espmail.broker.modelo.EstadoLista;
import com.espmail.modelo.maestras.Idioma;
import com.espmail.modelo.maestras.Pais;



import com.espmail.utils.contexto.Contexto;

import com.espmail.utils.set.SetConverter;

public class ApplicationListener implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent evt) {
		// Nada
	}

	public void contextInitialized(ServletContextEvent evt) {
		Contexto.TYPE = AppContexto.class;
		ServletContext ctx = evt.getServletContext();

		Contexto.setPropiedad(AppContexto.MAIL_SERVER,
				ctx.getInitParameter(AppContexto.MAIL_SERVER));
		Contexto.setPropiedad(AppContexto.MAIL_DEBUG,
				ctx.getInitParameter(AppContexto.MAIL_DEBUG));

		SetConverter converter = new SetConverter();
		ConvertUtils.register(converter, Pais.class);
		ConvertUtils.register(converter, Idioma.class);
		ConvertUtils.register(converter, EstadoEnviado.class);
		ConvertUtils.register(converter, EstadoEnvio.class);
		ConvertUtils.register(converter, EstadoLista.class);
	}
}
