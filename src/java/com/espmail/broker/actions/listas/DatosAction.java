package com.espmail.broker.actions.listas;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.actions.LookupDispatchAction;

import org.apache.struts.upload.FormFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.espmail.broker.util.Cargador;
import com.espmail.broker.util.Etiquetas;

import com.espmail.broker.forms.listas.DatosForm;

import com.espmail.broker.modelo.ListaDao;
import com.espmail.broker.modelo.Lista;

import com.espmail.utils.dao.DaoException;
import com.espmail.utils.dao.FactoriaDao;

import java.util.Map;
import java.util.HashMap;
import java.util.Properties;
import java.util.StringTokenizer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


/**
 * User: Luis Cubillo Date: 06-sep-2009 Time: 19:50:25
 */
public class DatosAction extends LookupDispatchAction {

	private final Map ops;

	public DatosAction() {
		this.ops = new HashMap();
		this.ops.put("listas.datos.submit", "aceptar");
	}

	protected Map getKeyMethodMap() {
		return this.ops;
	}

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {

		String idLista = req.getParameter("idLista");
		String idCliente = req.getRemoteUser();

		if (idLista != null) {
			DatosForm formulario = (DatosForm) form;

			try {
				ListaDao dao = (ListaDao) FactoriaDao.getInstance().getDao(
						ListaDao.class);
				Lista lista = dao
						.findById(idCliente, Integer.parseInt(idLista));

				formulario.setIdLista(lista.getIdLista());
				formulario.setNombreLista(lista.getNombre());
				formulario.setT1(lista.getEtiqueta1());
				formulario.setT1(lista.getEtiqueta2());
				formulario.setT1(lista.getEtiqueta3());
				formulario.setColumnas(null);
			} catch (DaoException e) {
				throw new ServletException(e);
			}
		}

		return mapping.getInputForward();
	}

	public ActionForward aceptar(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {

		DatosForm form = (DatosForm) actionForm;
		FormFile fichero = form.getFichero();

		if (req.getMethod().equals("POST")) {
			try {
				// Instanciamos el cargador a traves de un property
				// que tendra la clase correspondiente a cada formato
				Cargador lista = ((Cargador) Class.forName(
						getProperties().getProperty(form.getFormato()))
						.newInstance());
				String nombreArchivo = lista.carga(fichero.getInputStream());
				// Volocamos el fichero a disco y guardamos la referencia

				form.setNombreArchivo(nombreArchivo);
				form.setDatos(getMuestra(nombreArchivo));
			} catch (InstantiationException e) {
				throw new ServletException(e);
			} catch (IllegalAccessException e) {
				throw new ServletException(e);
			} catch (ClassNotFoundException e) {
				throw new ServletException(e);
			}
		}

		return mapping.findForward("mapeo");
	}

	private Properties getProperties() throws IOException {
		Properties prop = new Properties();
		prop.load(this.getClass().getResourceAsStream(Etiquetas.PROPERTIES));

		return prop;
	}

	public DatosForm.Fila[] getMuestra(String nombreArchivo) throws IOException {
		DatosForm.Fila[] muestra = new DatosForm.Fila[2];
		BufferedReader bf = new BufferedReader(new FileReader(nombreArchivo));
		int cont = 0;
		String linea = bf.readLine();

		while (linea != null && cont < 2) {
			StringTokenizer token = new StringTokenizer(linea, ";,");
			int length = token.countTokens() >= 7 ? 7 : token.countTokens();
			String[] valores = new String[length];

			for (int i = 0; i < length && token.hasMoreTokens(); i++) {
				valores[i] = token.nextToken();
			}

			muestra[cont] = new DatosForm.Fila(valores);
			cont++;
			linea = bf.readLine();
		}

		bf.close();

		return muestra;
	}
}
