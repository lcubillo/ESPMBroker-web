package com.espmail.broker.modelo;

import java.io.Serializable;

import java.sql.Timestamp;

import com.espmail.utils.TextUtils;

public class Cliente implements Serializable {

	private static final long serialVersionUID = -1106963502253684476L;
	
	private String id;
	private String password;
	private String nombre;
	private Timestamp creacion;
	private String pieHtml;
	private String pieTexto;
	private String remitente;
	private String urlBaja;
	public Cliente() {
		
	}
	
	public Cliente(String id, String nombre, String password, String creacion,
			String pieHtml, String pieTexto, String remitente) {

      this(id, nombre, password, creacion, pieHtml, pieTexto, null, remitente);
   }
   public Cliente (String id, String nombre, String password, String creacion,
			String pieHtml, String pieTexto, String urlBaja, String remitente){
      
      this.id = id;
		this.nombre = nombre;
		this.password = password;
		this.creacion = TextUtils.asTimestamp(creacion);
		this.pieHtml = pieHtml;
		this.pieTexto = pieTexto;
		this.remitente = remitente;
      this.urlBaja = urlBaja;
   }
	
	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public Timestamp getCreacion() {
		return this.creacion;
	}
	
	public void setCreacion(Timestamp creacion) {
		this.creacion = creacion;
	}
	
	public String getPieHtml() {
		return this.pieHtml;
	}
	
	public void setPieHtml(String pieHtml) {
		this.pieHtml = pieHtml;
	}
	
	public String getPieTexto() {
		return this.pieTexto;
	}
	
	public void setPieTexto(String pieTexto) {
		this.pieTexto = pieTexto;
	}
	
	public String getRemitente() {
		return this.remitente;
	}
	
	public void setRemitente(String remitente) {
		this.remitente = remitente;
	}

   public String getUrlBaja() {
      return urlBaja;
   }

   public void setUrlBaja(String urlBaja) {
      this.urlBaja = urlBaja;
   }
}
