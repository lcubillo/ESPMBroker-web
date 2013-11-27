package com.espmail.broker.generacion;

import com.espmail.utils.Base64Coder;
import com.espmail.utils.TextUtils;

public class Link {
	private final int idEnvio;
	private final char tipo; // L->Lectura C->Click B->Baja
	private final char modo; // t->Tracking g->GeneradorMemCamp
	private final int index;
	private final String url;
   private final String idCliente;

   public Link(int idEnvio) {
		this(idEnvio, 'g', (char) 0, 0, null, "");
	}

	public Link(int idEnvio, char tipo) {
		this(idEnvio, 't', tipo, 0, null,"");
	}

	public Link(int idEnvio, char tipo, int index, String url) {
		this(idEnvio, 't', tipo, index, url,"");
	}

   public Link(int idEnvio, char tipo, int index, String url, String id_cliente) {
		this(idEnvio, 't', tipo, index, url, id_cliente);
	}

   private Link(int idEnvio, char modo, char tipo, int index, String url, String idCliente) {
		this.idEnvio = idEnvio;
		this.modo = modo;
		this.tipo = tipo;
		this.index = index;
		this.url = url;
      this.idCliente = idCliente;
   }

	public String genera(long idEmail, int idLista) {



      StringBuffer sb = new StringBuffer();
		sb.append(this.idEnvio);
		sb.append("·");
		sb.append(idEmail);
		sb.append("·");
		sb.append(idLista);
		
		if (this.modo == 't') {
			sb.append("·");
			sb.append(this.tipo);
	
			if (this.url != null) {
				sb.append("·");
				sb.append(this.index);
				sb.append("·");
            sb.append(this.url);            
         }
		}
		
		StringBuffer sb2 = new StringBuffer("http://www.espmail/");
		sb2.append(this.modo);
		sb2.append(".do?a=");
		sb2.append(Base64Coder.encodeString(sb.toString()));

		return sb2.toString(); 
	}

}
