package com.espmail.broker.actions.altas;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.text.AbstractDocument.LeafElement;

import oracle.sql.NUMBER;

import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.espmail.broker.forms.altas.MonografiasForm;
import com.espmail.broker.forms.envios.AsociarListasForm;
import com.espmail.broker.modelo.Envio;
import com.espmail.broker.modelo.EnvioDao;
import com.espmail.broker.modelo.EstadisticaDao;
import com.espmail.broker.modelo.ListaDao;
import com.espmail.broker.modelo.Usuario;
import com.espmail.broker.modelo.UsuarioDao;
import com.espmail.utils.Base64Coder;
import com.espmail.utils.RequestUtils;
import com.espmail.utils.TextUtils;
import com.espmail.utils.dao.DaoException;
import com.espmail.utils.dao.FactoriaDao;
import com.espmail.utils.dao.Transaccion;

/**
 * Action especial para el cliente "monografias" con el fin de sincronizar altas
 * entre su sistema y espmail.net. Se recibira en la URL del action, los
 * parametros correspondientes al alta, y el action buscara donde tiene q
 * insertar los datos y los insertara en caso de que todo este OK
 * 
 * PASOS: 1. OBTENGO LOS DATOS DEL FORMULARIO (de la request tambien se podrian
 * sacar) 2. OBTENGO LA LISTA EN LA QUE IRAN LOS DATOS 3. CREO UN OBJETO USUARIO
 * CON LOS DATOS 4. INSERTO EL USUARIO EN BASE DE DATOS EN SU LISTA
 * CORRESPONDIENTE
 * 
 * @author gonzalo
 * @date 04/03/2009
 */
public class MonografiasAction extends Action {

	// Las listas por cada pais deben estar creadas previamente y añadirlas al
	// array listasMonografias en funcion del pais
	private static String[] paisesMonografias = { "ar", "bo", "br", "cl", "co", "cr", "cu", "do", "ec", "es", "gt", "hn", "mx", "ni", "pa", "pe", "pr", "py", "sv", "us", "uy", "ve", "zz" };
	private static String[] listasMonografias = { "5182", "5184", "5198", "5185", "5181", "5190", "5197", "5187", "5183", "5186", "5188", "5194", "5178", "5192", "5193", "5179", "5189", "5196", "5191", "5199", "5195", "5180", "5177" };

	// Declaro las posibles respuestas para legibilidad del codigo
	private final static int OK = 0;
	private final static int ERROR_IDENT = -1;
	private final static int ERROR_PAIS = -2;
	private final static int ERROR_INSERT = -3;
	private final static int ERROR_DAO = -4;
	private final static int ERROR_URL = -5;
	private final static int ERROR_USUARIO = -6;
	private final static int ERROR_EMAIL = -7;
	private final static int ERROR_LISTA_PAIS = -8;
	private final static int ERROR_PERMISO_IP = -9;
	private final static int ERROR_EMAIL_REPETIDO = -10;

	HashMap ips = null;

	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) throws Exception {

		MonografiasForm formulario = (MonografiasForm) form;

		UsuarioDao dao = null;
		ListaDao dao2 = null;
		
		String email, emailaux, paisT1, nombre, apellidos, tratamiento, t2, t3, ip = "";
		String ip1, ip2 = "";
		Long idEmail;
		Integer idLista = null;
		int respuesta = OK;

		// 1. OBTENGO LOS DATOS DEL FORMULARIO Y VALIDO QUE SEAN CORRECTOS(de la
		// request tambien se podrian sacar)

		try {

			/*
			 * idCliente=formulario.getP1()==null?"":formulario.getP1(); //Miro
			 * que sea monografias //monografias CODIFICADO en BASE64 es ->
			 * Vxiq1mXVYdXWL= if (!idCliente.equals("*Vxi*q1mXVYdXWL=")){
			 * respuesta = ERROR_IDENT; tratarRespuesta(respuesta,res); return
			 * null; }
			 */

			ip = RequestUtils.getIp(req);
			if (!ipPresent(ip)) {
				respuesta = ERROR_PERMISO_IP;
				tratarRespuesta(respuesta, res);
				return null;
			}

			email = formulario.getP1() == null ? "" : formulario.getP1()
					.toLowerCase();			
			try {
				emailaux = validaEmail(email);
				if (emailaux.equals("")) {
					respuesta = ERROR_EMAIL;
					tratarRespuesta(respuesta, res);
					return null;
				} else
					email = emailaux;
			} catch (Exception ex) {
				ex.printStackTrace();
				respuesta = ERROR_EMAIL;
				tratarRespuesta(respuesta, res);
				return null;
			}

			paisT1 = formulario.getP2() == null ? "" : formulario.getP2()
					.toLowerCase();
			if (paisT1.equals("")) {
				respuesta = ERROR_PAIS;
				tratarRespuesta(respuesta, res);
				return null;
			}

			nombre = formulario.getP3() == null ? "" : formulario.getP3();
			apellidos = formulario.getP4() == null ? "" : formulario.getP4();
			tratamiento = formulario.getP5() == null ? "" : formulario.getP5();
			t2 = formulario.getP6() == null ? "" : formulario.getP6();
			t3 = formulario.getP7() == null ? "" : formulario.getP7();
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta = ERROR_URL;
			tratarRespuesta(respuesta, res);
			return null;
		}

		// 2. OBTENGO LA LISTA EN LA QUE IRAN LOS DATOS

		boolean encontrado = false;
		for (int i = 0; i < paisesMonografias.length; i++) {
			if (paisesMonografias[i].equals(paisT1)) {
				idLista = Integer.valueOf(listasMonografias[i].toString());
				encontrado = true;
			}
		}
		if (!encontrado) {
			respuesta = ERROR_LISTA_PAIS;
			tratarRespuesta(respuesta, res);
			return null;
		}

		// 3. CREO UN OBJETO USUARIO CON LOS DATOS

		try {
			idEmail = getIdEmail(email);// Obtengo el id de email
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta = ERROR_USUARIO;
			tratarRespuesta(respuesta, res);
			return null;
		}

		// 4. INSERTO EL USUARIO EN BASE DE DATOS EN SU LISTA CORRESPONDIENTE
		Transaccion trans = null;
		try {
			dao = (UsuarioDao) FactoriaDao.getInstance().getDao(
					UsuarioDao.class);
			dao2 = (ListaDao) FactoriaDao.getInstance().getDao(ListaDao.class);
			trans = new Transaccion();

			String idClienteAux = "monografias";// Base64Coder.decodeString(idCliente);
			int aux = 0;
			aux = dao.existeUsuarioLista(idLista.intValue(),email);
					

			if (aux > 0) {
				respuesta = ERROR_EMAIL_REPETIDO;
				tratarRespuesta(respuesta, res);
				return null;
			} else {
				dao.altaUsuario(idEmail.longValue(), idLista.intValue(), emailaux, nombre, apellidos, tratamiento, TextUtils.asTimestamp(obtenerFechaActual()), null, paisT1, t2, t3);
				// Actualizo el numero de usuarios de la lista
				dao2.actualizaRegistros(idClienteAux, idLista.intValue(), 1);

			}
			trans.commit();
			trans.close();
		} catch (DaoException e) {
			e.printStackTrace();
			respuesta = ERROR_DAO;
			trans.rollback();
			trans.close();
			tratarRespuesta(respuesta, res);
			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			respuesta = ERROR_INSERT;
			trans.rollback();
			trans.close();

			tratarRespuesta(respuesta, res);
			return null;

		}

		tratarRespuesta(respuesta, res);

		return null;
	}

	private void tratarRespuesta(int codigo, HttpServletResponse res) {
		String respuesta = "";

		switch (codigo) {
		case 0:
			respuesta = "OK";
			break;		
		case -2:
			respuesta = "parametro p2(código del país) incorrecto.";
			break;
		case -5:
			respuesta = "Fallo general con los parámetros de la URL.";
			break;
		case -7:
			respuesta = "Email incorrecto";
			break;
		case -8:
			respuesta = "No existe una lista asociada al país especificado";
			break;
		case -9:
			respuesta = "No tienes permiso para acceder a este recurso.";
			break;// Rango de ip
		case -10:
			respuesta = "Usuario repetido para esa lista.";
			break;
		default:
			respuesta = "Fallo en el alta del usuario.";
			break;
		}
		try {
			PrintWriter pw = res.getWriter();
			pw.write(respuesta);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean ipPresent(String ip) {

		LogFactory.getLog(this.getClass()).info("Ip peticion monografias " + ip);
		ips = getIps();

		return (ips.get(ip) != null) ? true : false;

	}

	public HashMap getIps() {
		Properties prop = new Properties();
		HashMap querys = new HashMap();
		try {
			prop.load(new FileInputStream(
					"/monografias.properties"));
			StringTokenizer strToken = new StringTokenizer(prop
					.getProperty("ips"), ",");

			while (strToken.hasMoreTokens()) {
				String token = strToken.nextToken();
				LogFactory.getLog(this.getClass()).info("Ip " + token);
				querys.put(token, token);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return querys;
	}

	public Long getIdEmail(String email) {
		email = email.trim();
		
		Long idEmail = null;

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] md5 = md.digest(email.getBytes());
			byte[] byteEmail = new byte[5];
			System.arraycopy(md5, 0, byteEmail, 0, 5);
			idEmail = new Long(Long.parseLong(getHexString(byteEmail), 16));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	
		return idEmail;
	}

	public String getHexString(byte[] email) {
		StringBuffer hexadecimal = new StringBuffer();

		for (int i = 0; i < email.length; i++) {
			hexadecimal.append(Integer.toHexString(0xFF & email[i]));
		}

		return hexadecimal.toString().toUpperCase();
	}

	public String validaEmail(String email) throws Exception {

		// Comprobamos primero si es incorrecto y si no lo es, tiene que ser
		// correcto
		email = email.trim();
		Pattern p = Pattern.compile("^\\.|^\\@");
		Matcher m = p.matcher(email);
		if (m.find())
			throw new Exception("Email addresses don't start"
					+ " with dots or @ signs.");

		p = Pattern.compile("^www\\.");
		m = p.matcher(email);
		if (m.find()) {
			throw new Exception("Email addresses don't start"
					+ " with \"www.\", only web pages do.");
		}
		p = Pattern.compile("[^A-Za-z0-9\\.\\@_\\-~#]+");
		m = p.matcher(email);

		if (!m.find())
			return email;
		else
			throw new Exception("Email address invalid " + email + "\n");

	}
	public String  obtenerFechaActual() {
		String fechaActual = "";
		try {
			Calendar c = new GregorianCalendar();

			String dia = Integer.toString(c.get(Calendar.DATE));
			String mes = Integer.toString(c.get(Calendar.MONTH) + 1);
			String annio = Integer.toString(c.get(Calendar.YEAR));
			String horas = Integer.toString(c.get(Calendar.HOUR_OF_DAY));
			String minutos = Integer.toString(c.get(Calendar.MINUTE));;
			String segundos = Integer.toString(c.get(Calendar.SECOND));;
			fechaActual = "" + dia + "/" + mes + "/" + annio + " "+horas+":"+minutos+":"+segundos+"";
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return fechaActual;
	}

}
