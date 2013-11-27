package com.espmail.broker.actions.listas;

import com.espmail.broker.forms.listas.DatosForm;

import com.espmail.broker.modelo.EstadoLista;
import com.espmail.broker.modelo.Lista;
import com.espmail.broker.modelo.ListaDao;

import com.espmail.broker.util.Etiquetas;

import com.espmail.utils.TextUtils;
import com.espmail.utils.contexto.Contexto;

import com.espmail.utils.dao.DaoException;
import com.espmail.utils.dao.FactoriaDao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.SQLException;

import java.util.StringTokenizer;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.regex.*;
/**
 * User: Luis Cubillo
 * Date: 07-sep-2009
 * Time: 17:02:30
 */
public class MapeoAction extends Action{

	private static final Log LOG = LogFactory.getLog(MapeoAction.class);
	
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
    		HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
    	//--------------------------------------
    	if( isTokenValid(req,true) ){
            LogFactory.getLog(this.getClass()).info("el token es valido ");
        }
    	else{//habria que hacer la redireccion a la pagina de lista
               LogFactory.getLog(this.getClass()).info("redireccionamos a return null ");
               //return null;
              return( new ActionForward("/app/listas/listado.do",true));

        }
        //-------------------------
    	
    	
    	
    	ActionErrors listaErrores=null;
    	StringBuffer errores = new StringBuffer();
        DatosForm form = (DatosForm) actionForm;
        String idCliente = req.getRemoteUser();
        int[] columnas = form.getColumnas();
        int idLista = form.getIdLista();
        String nombreArchivo = form.getNombreArchivo();
        
        try {
        	
	        ListaDao dao = (ListaDao) FactoriaDao.getInstance().getDao(ListaDao.class);
	     
	        Lista lista = null;
	        boolean nuevo = idLista == 0;
	        
	        LOG.info("Inteto entrar en nuevo");
	        if (nuevo) {	        	
	        	 LOG.info("Intento obtener el id");
	        	idLista = dao.getId();
	        	
	            lista = new Lista();
	            lista.setIdLista(idLista);
	            lista.setIdCliente(idCliente);
	            lista.setNombre(form.getNombreLista());
	            lista.setEstado(EstadoLista.PENDIENTE);
	            lista.setCreacion(new Timestamp(new Date().getTime()));
	        } else {
	        	
	            lista = dao.findById(idCliente, idLista);
	
	            if (lista.getEstado() == EstadoLista.PENDIENTE) {
	    	        ActionMessages messages = new ActionMessages();
	    	        messages.add(ActionMessages.GLOBAL_MESSAGE,
	    	        		new ActionMessage("listas.mapeo.error.pendiente"));
	    	        saveErrors(req, messages);
	
	            	return mapping.findForward("error");
	            }
	        }
	        
	        if(!TextUtils.isEmpty(form.getT1())) {
	            lista.setEtiqueta1(form.getT1());
	        }
	
	        if(!TextUtils.isEmpty(form.getT2())) {
	            lista.setEtiqueta2(form.getT2());
	        }
	
	        if(!TextUtils.isEmpty(form.getT3())) {
	            lista.setEtiqueta3(form.getT3());
	        }
	        
	        if (nuevo) {
	        	
	            dao.insert(lista);
	            
	            form.setIdLista(idLista);
	        } else {
	        	
	            dao.update(lista);
	            
	        }
	
	        StringBuffer query = new StringBuffer("INSERT INTO USUARIOS (FECHA_CREACION, ID_LISTA ");
	        StringBuffer values = new StringBuffer(") VALUES(SYSDATE, ").append(form.getIdLista());

	       
	        for(int i = 0; i < columnas.length; i++) {
	        	int index = columnas[i];
	        	
	        	if (index != -1) {
	        		query.append(",").append(Etiquetas.parametros_totales[index]);
	        		
	                if (columnas[i] == Etiquetas.EMAIL){
	                    query.append(", ID_EMAIL");
	                    values.append(",?");
	                }
	                
	                values.append(",?");
	        	}
	        }
	
	        values.append(")");
	        query.append(values);
	
	        LOG.debug(query.toString());
	
	        int fallos = 0;
	        int exitos = 0;
	        Connection con = null;
	        PreparedStatement ps = null;
	        
	        try {
	        	
	        	con = Contexto.getInstance().getConexion();
	        	
		        ps = con.prepareStatement(query.toString());
		        //Deberíamos comprobar si este archivo sigue existiendo , si no es así mandarnos a nueva.do
		        
		        BufferedReader br = new BufferedReader(new FileReader(nombreArchivo));
		        
		        String linea = br.readLine();
		        
		        while(linea != null){
		            StringTokenizer token = new StringTokenizer(linea, ";,");
		            int psIndex = 1;
		            
		            try{
		                for(int i = 0; i< columnas.length; i++){
		                	int index = columnas[i];
		    	        	
		    	        	if (index != -1) {
			                    if (Etiquetas.EMAIL == index){
			                        String email = token.nextToken().toLowerCase();	
			                        
		                        	ps.setObject(psIndex , validaEmail(email.trim()));
		                        	psIndex++;
			                        ps.setObject(psIndex, getIdEmail(email.trim()));		                        
			                        
			                    } else {
			                        ps.setObject(psIndex, token.nextToken());
			                    }

			                    psIndex++;
		    	        	} else {
		    	        		token.nextToken();
		    	        	}
		                }
		               
		                ps.execute();
		               
		                exitos++;		             
		            } catch (NoSuchElementException nse) {
		            	
		            	errores.append("Fallo en los elementos en la linea "+token +"<br>");
		                fallos++;
		            } catch (SQLException de){		            	
		            	LOG.error("Error de insercion para "+linea +": "+ de.getMessage());
		            	errores.append("Error de insercion para "+linea +": "+ de.getMessage()+"<br>");
		                fallos++;
		            }catch(Exception e){ //TODO Crear la excepcion.			                        	
                    	fallos++;
                    	LOG.error("Para el email "+linea+" error-> "+e.getMessage());
                    	errores.append("Para el email "+linea+" error-> "+e.getMessage()+"<br>");                    	
                    }
		
		            linea = br.readLine();
		        }
	        } catch (SQLException e) {
	        	throw new ServletException(e);
	        } finally {
	        	if (ps != null) {
	        		try { ps.close(); } catch (SQLException e) { }
	        	}
	        	
	        	if (con != null) {
	        		try { con.close(); } catch (SQLException e) { }
	        	}
	        	
	        }
	      
	        new File(nombreArchivo).delete();
	      
	        
	        if (fallos > 0) {
		        if(listaErrores== null)
		        	listaErrores=new ActionErrors();
		        listaErrores.add(ActionMessages.GLOBAL_MESSAGE,
		        		new ActionMessage("listas.mapeo.exito", "" + fallos +"<br>"+errores.toString()));//TODO utilizar una etiqueta generica para los fallos mas el mensaje 
		        saveErrors(req, listaErrores);
	        }
	      
	        dao.actualizaRegistros(idCliente, idLista, exitos);
	      
	        lista.setEstado(EstadoLista.LISTO);
	        dao.update(lista);
        } catch (DaoException e) {
        	throw new ServletException(e);
        }

        return mapping.findForward("exito");
    }

    public Long getIdEmail(String email) {
        Long idEmail = null;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] md5 = md.digest(email.getBytes());
            byte[] byteEmail = new byte[5];
            System.arraycopy(md5, 0, byteEmail, 0, 5);
            idEmail = new Long(Long.parseLong(getHexString(byteEmail),16));
        } catch (NoSuchAlgorithmException e) {
        	LOG.error(e);
        }

        return idEmail;
    }

    public String getHexString(byte[] email){
        StringBuffer hexadecimal = new StringBuffer();

        for (int i = 0; i < email.length; i++) {
        	hexadecimal.append(Integer.toHexString(0xFF & email[i]));
        }

        return hexadecimal.toString().toUpperCase();
    }
    
    public String validaEmail(String email) throws Exception{
    	 
    	//Comprobamos primero si es incorrecto y si no lo es, tiene que ser correcto
    	 email = email.trim();
         Pattern p = Pattern.compile("^\\.|^\\@");
         Matcher m = p.matcher(email);
         if (m.find())
            throw new Exception("Email addresses don't start" +
                               " with dots or @ signs.");


         p = Pattern.compile("^www\\.");
         m = p.matcher(email);
         if (m.find()) {
           throw new Exception("Email addresses don't start" +
                   " with \"www.\", only web pages do.");
         }
         p = Pattern.compile("[^A-Za-z0-9\\.\\@_\\-~#]+");
         m = p.matcher(email);
        
         if(!m.find())
        	 return email;
         else
        	 throw new Exception("Email address invalid "+email+"\n");
        
    }
}
