package com.espmail.broker.generacion;

import javax.mail.MessagingException;

import com.espmail.broker.modelo.Envio;
import com.espmail.utils.TextUtils;
import org.apache.commons.logging.LogFactory;

public class GeneradorEnvioTest extends GeneradorEnvio {

	protected final static String QUERY
		= "select id_email, usuarios.id_lista, email, nombre, apellidos,"
		+ " tratamiento, etiqueta_t1, t1, etiqueta_t2, t2,etiqueta_t3, t3"
		+ " from usuarios, listas"
		+ " where listas.id_lista = usuarios.id_lista"
		+ " and listas.id_lista = 1";

	private final String[] correos;
	
	public GeneradorEnvioTest(Envio envio, String[] correos) throws GeneradorException {
		super(false, envio);
      LogFactory.getLog(this.getClass()).info("Creando test");
      this.correos = correos;
	}

	protected void envia(long idEmail, int idLista, String email,
			String asunto, String html, String texto)
			throws GeneradorException {	
		for (int i = 0; i < this.correos.length; i++) {
			String correo = this.correos[i];
			LogFactory.getLog(this.getClass()).info("Enviando test a "+correo);
			if (!TextUtils.isEmpty(correo)) {
				try {
					sendMail(idEmail, idLista, correo, asunto, html, texto);
				} catch (MessagingException e) {
					error(e);
				}
            LogFactory.getLog(this.getClass()).info("Enviado");
         }
		}
	}

	protected String getQuery() {
		return QUERY;
	}
}
