package com.espmail.broker;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.*;
import javax.servlet.http.*;

import com.espmail.broker.modelo.EstadisticaDao;
import com.espmail.utils.dao.DaoException;
import com.espmail.utils.dao.FactoriaDao;

public class CsvServlet extends HttpServlet
 {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest req,HttpServletResponse res)throws ServletException, IOException
	{
		res.setContentType("application/csv");
		OutputStream salida=res.getOutputStream();
		String operacion = req.getParameter("operacion");
		String idCliente = req.getRemoteUser();
		String idEnvioSt = req.getParameter("idEnvio");
		Integer idEnvio = idEnvioSt==null?null:new Integer(idEnvioSt);
		
		String idListaSt = req.getParameter("idLista");
		Integer idLista = idListaSt==null?null:new Integer(idListaSt);
		if(operacion==null || idCliente == null){
			throw new ServletException("Parametros vacios.");			
		}
		List datos=null;
		try {
			EstadisticaDao dao = (EstadisticaDao) FactoriaDao.getInstance().getDao(EstadisticaDao.class);
			if(operacion.equals("bajasEnvio")){
				System.out.println("Estoy en las bajas de envio.");
				if(idEnvio==null)
					throw new ServletException("IdEnvio nulo");
				datos=dao.dameBajasEnvio(idEnvio.intValue());
			}else if(operacion.equals("bajasLista")){
				if(idLista==null)
					throw new ServletException("IdLista nulo");
				datos=dao.dameBajasListas(idLista.intValue());
			}else{	
				if(idEnvio==null)
					throw new ServletException("IdEnvio nulo");
				if(operacion.equals("estadisticaDia")){
					datos=dao.dameResumenDia(idCliente, idEnvio.intValue());
				}else if(operacion.equals("estadisticaClick")){
					datos= dao.dameClicks(idCliente, idEnvio.intValue());
				}else if (operacion.equals("emailsFallidos")){
					if(idEnvio==null)
						throw new ServletException("IdEnvio nulo");
					datos=dao.dameEnvioEmailsFallidos(idCliente,idEnvio.intValue());
				}else{
					throw new ServletException("Operacion no válida.");
				}
			}
			if(datos!=null){
				Iterator itr= ((Map)datos.get(0)).keySet().iterator();
				while (itr.hasNext()){
					salida.write(itr.next().toString().toUpperCase().getBytes());
					if(itr.hasNext())
						salida.write(";".getBytes());
				}
				salida.write("\n".getBytes());
				for(int i=0;i<datos.size();i++){
					Map resumenEnvio = (Map) datos.get(i);		
					Collection datosSacados=resumenEnvio.values();
					itr=datosSacados.iterator();
					while(itr.hasNext()){
						Object objeto=itr.next();
						if(objeto!=null)
						{
							String dato=objeto.toString();
						//		salida.write(itr.next().toString().getBytes());
								salida.write(dato.getBytes());
								
						}
						if(itr.hasNext())
							salida.write(";".getBytes());
					}
					salida.write("\n".getBytes());
				}
			}
			salida.flush();
		}catch (DaoException e) {
			throw new ServletException(e.getCause());
		}
	}
  }