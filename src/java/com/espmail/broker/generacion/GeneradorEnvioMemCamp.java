package com.espmail.broker.generacion;

import com.espmail.broker.modelo.Envio;

public class GeneradorEnvioMemCamp extends GeneradorEnvio {

	protected final static String QUERY
	= "select usuarios.id_email, usuarios.id_lista, email, nombre, apellidos,"
	+ " tratamiento, etiqueta_t1, t1, etiqueta_t2, t2,etiqueta_t3, t3"
	+ " from usuarios, listas"
	+ " where listas.id_lista = usuarios.id_lista and usuarios.id_lista = ";

	private String texto;
	private String html;
	private String asunto;
	
	private final String query;
	
	public GeneradorEnvioMemCamp(Envio envio, long idEmail, int idLista) throws GeneradorException {
		super(false, envio);
		this.query = QUERY 	+ idLista + " and usuarios.id_email = " + idEmail;
	}

	protected void envia(long idEmail, int idLista, String email,
			String asunto, String html, String texto) {
		this.asunto = asunto;
		this.html = html;
		this.texto = texto;
	}
	
	public String getAsunto() {
		return this.asunto;
	}

	public String getTexto() {
		return this.texto;
	}

	public String getHtml() {
		return this.html;
	}

	protected String getQuery() {
		return this.query;
	}

}
