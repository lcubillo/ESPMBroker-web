package com.espmail.broker.actions;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.commons.logging.LogFactory;

import sun.util.logging.resources.logging;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import com.espmail.utils.Base64Coder;
import com.espmail.utils.RequestUtils;
import com.espmail.utils.TextUtils;
import com.espmail.utils.dao.FactoriaDao;
import com.espmail.utils.dao.DaoException;
import com.espmail.broker.util.WriteTracking;
import com.espmail.broker.modelo.UsuarioDao;
import com.espmail.broker.modelo.Usuario;


import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.*;
import java.io.IOException;


/**
 * User: Luis Cubillo
 * Date: 13-sep-2009
 * Time: 18:29:08
 */
public class TrackingAction extends Action {

   public static final String etiquetas []={"ID_ENVIO","ID_EMAIL","ID_LISTA","TIPO","NUM","URL","ID_CLIENTE"};
   public static final Class tipos []={Integer.class, Long.class, Integer.class, String.class, String.class, String.class, String.class};

    public ActionForward execute(ActionMapping mapping, ActionForm form, 
    	 HttpServletRequest req, HttpServletResponse res) throws Exception {
       int idLista = 0;
       String parametro = req.getParameter("a");
       String tipo = null;

       if (parametro == null) {
           throw new Exception("Parameter not present");
       }

       String ip = RequestUtils.getIp(req);
       String queryString = new String(Base64Coder.decode(parametro));
       LogFactory.getLog(this.getClass()).debug("String: "+queryString);
        StringTokenizer token = new StringTokenizer(queryString, "·");
       HashMap parametros = new LinkedHashMap(token.countTokens());
       boolean trackingClick = false;
       int i = 0;

       for(; token.hasMoreTokens(); i++){
         parametros.put(etiquetas[i],getInstance(tipos[i], token.nextToken()));
         if("URL".equalsIgnoreCase(etiquetas[i]))
            trackingClick =!trackingClick;
       }

       parametros.put("IP",ip);

        if (trackingClick) {
           if("b".equalsIgnoreCase((String)parametros.get("TIPO"))){
              //borrarUsuario(parametros); se borrara procesando la insert en el fichero
              if(idLista == 1) //es un email de prueba
                return mapping.findForward("exito");
           }
           redirect(res, parametros);
        }

        vuelcaTracking(parametros, trackingClick);
       req.getSession().invalidate();
       if("b".equalsIgnoreCase((String)parametros.get("TIPO"))){
          return mapping.findForward("exito");
       }

       return null;
    }

    private void vuelcaTracking(HashMap parametros, boolean trackingClick)
    		throws NoSuchElementException {

      StringBuffer sb = new StringBuffer("INSERT INTO TRACKING (");

       parametros.remove("ID_CLIENTE");
       for(Iterator it = parametros.keySet().iterator(); it.hasNext();) {
         sb.append(it.next()).append(",");
      }

      sb.append("FECHA) VALUES(");

      for(Iterator it = parametros.values().iterator(); it.hasNext();) {
         Object param = it.next();
         if(param instanceof String)
            sb.append("'").append(param).append("',");         
         else
            sb.append(param).append(",");
      }

      sb.append("TO_DATE('").append(TextUtils.asString(new Timestamp(System.currentTimeMillis())));
      sb.append("','DD/MM/YYYY HH24:MI:SS'));\nCOMMIT;\n");

      WriteTracking.getInstance().write(sb.toString());
    }

   private void redirect(HttpServletResponse resp, HashMap parametros) throws IOException, DaoException {      
      String url =( String )parametros.get("URL");
      
      if(url.indexOf("{EMAIL}")>=0 ||
           url.indexOf("{NOMBRE}")>=0){
         UsuarioDao dao = (UsuarioDao)FactoriaDao.getInstance().getDao(UsuarioDao.class);
         Usuario user = dao.findByIdEnvio(((Integer)parametros.get("ID_ENVIO")).intValue(),
              ((Integer)parametros.get("ID_LISTA")).intValue(),
              ((Long)parametros.get("ID_EMAIL")).longValue());

         if(user != null){
            url = url.replaceAll("\\{EMAIL\\}", user.getEmail());
            LogFactory.getLog(this.getClass() ).info("Sustituido email"+url);
            url = url.replaceAll("\\{NOMBRE\\}", user.getNombre());
            LogFactory.getLog(this.getClass() ).info("Sustituido nombre "+url);
            //todo se pueden sustituir todos los valores presentes en el usuario...
         }
      }
      resp.sendRedirect(url);

   }
      
   private Object getInstance(Class  tipo, Object parametro){
      try {
         return tipo.getConstructor(new Class[]{String.class}).newInstance(new Object[]{parametro});

      } catch (Exception e) {
         return ( String ) parametro;
      }

   }
}
