package com.espmail.broker.modelo;


import java.sql.Timestamp;
import java.sql.SQLException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;


import com.espmail.utils.TextUtils;
import com.espmail.utils.set.SetUtils;
import oracle.sql.CLOB;

public class Envio {

	private String idCliente;
	private int idEnvio;
	private String etiqueta;
	private String descripcion;
	private EstadoEnvio estado = EstadoEnvio.PENDIENTE;
	private Timestamp fechaEnvio;
	private String html;
	private String texto;
	private String remitente;
	private String asunto;
	private String replyTo;

	public Envio() {

	}

	public Envio(String idCliente, Number idEnvio, String etiqueta,
			String descripcion, String estado, String fechaEnvio, CLOB html,
			String texto, String remitente, String asunto, String replyTo) {
		this.idCliente = idCliente;
		this.idEnvio = idEnvio.intValue();
		this.etiqueta = etiqueta;
		this.descripcion = descripcion;
		this.estado = (EstadoEnvio) SetUtils.get(EstadoEnvio.class, estado);
		this.fechaEnvio = fechaEnvio == null? null : TextUtils.asTimestamp(fechaEnvio);
		this.html = getStringFromClob(html);
		this.texto = texto;
		this.remitente = remitente;
		this.asunto = asunto;
		this.replyTo = replyTo;
	}

	/**
	 * @return the asunto
	 */
	public String getAsunto() {
		return asunto;
	}

	/**
	 * @param asunto the asunto to set
	 */
	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return the estado
	 */
	public EstadoEnvio getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(EstadoEnvio estado) {
		this.estado = estado;
	}

	/**
	 * @return the etiqueta
	 */
	public String getEtiqueta() {
		return etiqueta;
	}

	/**
	 * @param etiqueta the etiqueta to set
	 */
	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}

	/**
	 * @return the fechaEnvio
	 */
	public Timestamp getFechaEnvio() {
		return fechaEnvio;
	}

	/**
	 * @param fechaEnvio the fechaEnvio to set
	 */
	public void setFechaEnvio(Timestamp fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}

	/**
	 * @return the html
	 */

	public String getHtml() {

		return html;
	}

	/**
	 * @param html the html to set
	 */
	public void setHtml(CLOB html) {

		this.html = getStringFromClob(html);
	}

    public void setHtml(String html){
        this.html = html;
    }
	/**
	 * @return the idCliente
	 */
	public String getIdCliente() {
		return idCliente;
	}

	/**
	 * @param idCliente the idCliente to set
	 */
	public void setIdCliente(String idCliente) {
		this.idCliente = idCliente;
	}

	/**
	 * @return the idEnvio
	 */
	public int getIdEnvio() {
		return idEnvio;
	}

	/**
	 * @param idEnvio the idEnvio to set
	 */
	public void setIdEnvio(int idEnvio) {
		this.idEnvio = idEnvio;
	}

	/**
	 * @return the remitente
	 */
	public String getRemitente() {
		return remitente;
	}

	/**
	 * @param remitente the remitente to set
	 */
	public void setRemitente(String remitente) {
		this.remitente = remitente;
	}

	/**
	 * @return the texto
	 */
	public String getTexto() {
		return texto;
	}

	/**
	 * @param texto the texto to set
	 */
	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}

    public String getStringFromClob(CLOB clob){
        StringBuffer contenido = new StringBuffer();

        try {
            BufferedReader bis = new BufferedReader(new InputStreamReader(clob.getAsciiStream()));
            String linea = bis.readLine();
            while(linea!=null){
                contenido.append(linea);
                linea = bis.readLine();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contenido.toString();
    }
}
