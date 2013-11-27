package com.espmail.broker.actions.envios;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.apache.struts.actions.LookupDispatchAction;

import com.espmail.broker.forms.envios.AsociarListasForm;
import com.espmail.broker.modelo.EnvioDao;
import com.espmail.broker.modelo.Lista;
import com.espmail.broker.modelo.ListaDao;
import com.espmail.utils.dao.DaoException;
import com.espmail.utils.dao.FactoriaDao;
import com.espmail.utils.dao.Transaccion;

public class AsociarListasAction extends  LookupDispatchAction{
	
	private final Map map;

	/**
	 * Añade los datos en los mapas para los distintos idiomas las operaciones.
	 * 
	 */
	public AsociarListasAction() {
		this.map = new HashMap();
		this.map.put("envios.asociarListas.submit", "guardar");
	}

	/**
	 * Rellena los datos del webmaster
	 */
	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {
		AsociarListasForm formulario = (AsociarListasForm) form;
		int idEnvio = formulario.getIdEnvio();
		String idCliente = req.getRemoteUser();

		try {
			formulario.init(req);
			EnvioDao dao = (EnvioDao) FactoriaDao.getInstance().getDao(EnvioDao.class);
			formulario.setListasSeleccionadas(dao.findListas(idCliente, idEnvio));
		} catch (DaoException e) {
			throw new ServletException(e);
		}
		String inc = (String)req.getParameter("inc")==null?"":(String)req.getParameter("inc");
		if (inc.equals("S"))
			req.setAttribute("incompatibles","S");
		return mapping.getInputForward();
	}


	public ActionForward guardar(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {
		AsociarListasForm formulario = (AsociarListasForm) form;
		int idEnvio = formulario.getIdEnvio();
		String idCliente = req.getRemoteUser();
		Integer[] listas = formulario.getListasSeleccionadas();
		
		//TODO:Comprobar que sean compatibles si tienen parametros
		if (listasCompatibles(idCliente,listas)){
		Transaccion transaccion = null;
		
		try {
			EnvioDao dao = (EnvioDao) FactoriaDao.getInstance().getDao(EnvioDao.class);
			transaccion = new Transaccion();
			dao.deleteListas(idCliente, idEnvio);
			
			for (int i = 0; i < listas.length; i++) {
				dao.insertLista(idCliente, idEnvio, listas[i].intValue());
			}
			
			transaccion.commit();
			
		} catch (DaoException e) {
			try {
				transaccion.rollback();
			} catch (DaoException e1) {
				// Nada
			}
			
			throw new ServletException(e);
		} finally {
			if (transaccion != null) {
				transaccion.close();
			}			
		}
				
		if (esPersonalizada(idCliente,listas))
			return new ActionForward("/app/envios/datos.do?idEnvio=" + idEnvio, true);
		else
			return new ActionForward("/app/envios/test.do?idEnvio=" + idEnvio, true);
		}else{
			
			//req.setAttribute("incompatibles","S");
			return new ActionForward("/app/envios/asociarListas.do?inc=S&idEnvio=" + idEnvio, true);
		}
	}

	private boolean esPersonalizada (String idCliente,Integer [] listas){
		ArrayList datos =new ArrayList();
		Lista lista = null;		
		ListaDao ldao = null;
		
		
		try {
			ldao = (ListaDao) FactoriaDao.getInstance().getDao(ListaDao.class);
		} catch (DaoException e) {			
			e.printStackTrace();
		}		
		for (int i=0;i<listas.length;i++){			
			try{
			datos.add(ldao.findById(idCliente, listas[i].intValue()));
			
			}catch(DaoException e){
				e.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		Object ob = datos.get(0);
		lista =(Lista) ob;
		

		if ((lista.getEtiqueta1()==null || "".equals(lista.getEtiqueta1())) &&( lista.getEtiqueta2()==null || "".equals(lista.getEtiqueta2()))
				&&( lista.getEtiqueta3()==null || "".equals(lista.getEtiqueta3()) ))						
			return false;
		else
			return true;
		
	}
	private boolean listasCompatibles(String idCliente,Integer [] listas){				
		ArrayList  datos =  new ArrayList();
		ListaDao ldao = null;
		boolean sigue = true;
		if (listas.length==1) return true;
		try {
			ldao = (ListaDao) FactoriaDao.getInstance().getDao(ListaDao.class);
		} catch (DaoException e) {			
			e.printStackTrace();
		}		
		for (int i=0;i<listas.length;i++){			
			try{
				int idLista =  listas[i].intValue();
			datos.add(ldao.findById(idCliente,idLista));			
			}catch(DaoException e){
				e.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		if(datos.size()>1){
			Lista lista = null;
			Lista lista2 = null;
			boolean t1 = true;
			boolean t2 = true;
			boolean t3 = true;
			for (int j= 0;j<datos.size();j++){
				 t1 = true;
				 t2 = true;
				 t3 = true;
				Object ob = datos.get(j);
				if (j==0){
					lista =(Lista) ob;
					
				}
				lista2 = (Lista) ob;				
					
				if ((lista.getEtiqueta1()!=null && lista2.getEtiqueta1()!=null) && lista.getEtiqueta1().equals(lista2.getEtiqueta1())){
					t1 = true;
				}else if (lista.getEtiqueta1()== null && lista2.getEtiqueta1()== null) t1 = true;
					else t1 = false;
				
				if ((lista.getEtiqueta2()!=null && lista2.getEtiqueta2()!=null)&& lista.getEtiqueta2().equals(lista2.getEtiqueta2())){
					t2 = true;
				}else if (lista.getEtiqueta2()==null && lista2.getEtiqueta2()==null) t2 = true;
					else t2 = false;
				
				if ((lista.getEtiqueta3()!=null && lista2.getEtiqueta3()!=null)&&lista.getEtiqueta3().equals(lista2.getEtiqueta3())){
					t3 =  true;
				}else if (lista.getEtiqueta3()==null && lista2.getEtiqueta3()==null) t3 = true;
				else t3 = false;
				
								
				
				if (t1 && t2 && t3) sigue = true;
				else sigue = false;
							
				if (sigue==false) j = datos.size();			
			}		
		}
		return sigue;
	}
	
	protected Map getKeyMethodMap() {
		return this.map;
	}
}
