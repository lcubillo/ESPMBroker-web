package com.espmail.broker.generacion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Plantilla {
	private static final char[] LINK = "href = \"".toCharArray();

	private final int idEnvio;
	private final List elementos;

	public Plantilla(int idEnvio, String plantilla, List parametros)
			throws PlantillaException {
		this.idEnvio = idEnvio;
		this.elementos = new ArrayList();
		char[] letras = plantilla.toCharArray();
		int length = letras.length;
		char modo = 'T'; // T->Texto P->Parametro L->Link
		int ini = 0;
		int linkIni = 0;
		int linkIndex = 0;

		for (int i = 0; i < length; i++) {
			char ch = letras[i];

			switch (modo) {
			case 'P':
				boolean parametro = false;
				if (ch == '{') {
					throw new PlantillaException(
							"Encontrada un caracter { dentro de un parametro.");
				}

				if (ch == '}') {
					String param = plantilla.substring(ini + 1, i);
					Iterator it = parametros.iterator();

					for (int j = 0; it.hasNext(); j++) {
						String item = (String) it.next();

						if (item.equals(param)) {
							this.elementos.add(new Integer(j));
							parametro = true;
							break;
						}
					}
					if (!parametro){
						//Si no es un parametro se inserta como texto (por ej, un css o javascript)
						this.elementos.add(plantilla.substring(ini , i+1));																	
					}
					ini = i+1;					
					modo = 'T';
				}

				break;
			case 'L':
				if (ch == '{') {
					throw new PlantillaException(
							"No se permiten parametros en los enlaces.");
				}

				if (ch == '"') {
					this.elementos.add(new Link(this.idEnvio, 'C', linkIndex++,
							plantilla.substring(ini, i)));
					ini = i;
					modo = 'T';
				}

				break;
			default:
				char lch = LINK[linkIni];

				if (lch == ch
						|| (linkIni > 0 && ch == ' ' && LINK[linkIni - 1] == ' ')
						|| (lch == ' ' && LINK[linkIni + 1] == ch)) {
					if (lch == ch) {
						linkIni++;
					} else if (lch == ' ') {
						linkIni += 2;
					}

					if (ch == '"') {
						linkIni = 0;

						if (letras[i + 1] != '{') {
							this.elementos.add(plantilla.substring(ini, i + 1));
							modo = 'L';
							ini = i + 1;
						}
					}
				} else {
					linkIni = 0;

					if (ch == '{') {
						this.elementos.add(plantilla.substring(ini, i));
						ini = i;
						modo = 'P';
					}
				}
			}
		}

		if (ini < length) {
			this.elementos.add(plantilla.substring(ini));
		}
	}

	public String genera(long idEmail, int idLista, String[] valores) {
		StringBuffer sb = new StringBuffer();
		Iterator it = this.elementos.iterator();

		while (it.hasNext()) {
			Object next = it.next();

			if (next instanceof Integer) {
				String value = valores[((Integer) next).intValue()];

				if (value != null) {
					sb.append(value);
				}
			} else if (next instanceof Link) {
				sb.append(((Link) next).genera(idEmail, idLista));
			} else {
				sb.append(next);
			}
		}

		return sb.toString();
	}

	public static void main(String[] args) throws Exception {
		List parametros = new ArrayList();
		parametros.add("NOMBRE");
		parametros.add("MAIL");

		Plantilla plantilla = new Plantilla(
				1,
				"<html><head><title>Documento sin t&iacute;tulo</title><meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\"></head><body><a href=\"http://www.google.com\">click</a></body></html>",
				parametros);
		System.out.println(plantilla.genera(12345, 0, new String[] { "Pepe",
				"pepe@hotmail.com" }));
	}
}
