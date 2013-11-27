package com.espmail.util;

import java.net.InetAddress;
import java.util.ArrayList;

import com.espmail.log.Logger;
import com.espmail.utils.dao.FactoriaDao;
import com.espmail.utils.dao.DaoException;


/**
 * ESPMail
 * User: Luis
 */
public class Lock {
	Logger log;
        /**
         *  Bloquea un pedido en la base de datos
         * @param pedido
         * @param empresa
         * @return
         */
   public synchronized boolean lock (String pedido, String empresa){
	   log = new Logger();
	   //Se obtiene la maquina
	   String maquina = "";
		try {
			maquina = InetAddress.getLocalHost().getHostName();
			try {
				String separar[] = maquina.split(".");
				maquina = separar[0];
			} catch (Exception e) {
			}				
		} catch (Exception ex) {
			maquina = "";
			log.error("Error obtaining machine name.");
		}
		//Se comprueba que ese pedido no este ya bloqueado
		try{
			String QUERY = "select * from "+empresa+".SINC_PEDIDOS where ped_cod = "+pedido+" or maquina = '"+maquina+"'";
		ArrayList existe = (ArrayList) FactoriaDao.getInstance().executeQuery(QUERY);
		if (existe.size() >0){
			log.info(""+maquina+" is busy or code "+pedido+" is already selected");
			return false;
			}
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
		//Se bloquea si esta libre
		log.info("Trying to block the code "+pedido+" in the machine "+maquina+"");
	    
      try {
         int update = FactoriaDao.getInstance().executeUpdate("INSERT INTO "+empresa+".SINC_PEDIDOS(PED_COD, MAQUINA, HORA_INICIO, TIPO_BLOQUEO)" +
              "VALUES("+pedido+",'"+maquina+"',sysdate,'L')");         
         return update > 0;
      } catch (DaoException e) {    	  
    	  e.printStackTrace();
         return false;
      }
   }
/**
 * Desbloquea un pedido ode base de datos
 * @param pedido
 * @param empresa
 * @return
 */
   public synchronized boolean unLock(String pedido, String empresa){
      //todo actualizar ( si esta presente la fecha en prioridad_lanzamiento)
	   log = new Logger();
	   log.info("DESBLOQUEO en sinc_pedidos: pedido - "+pedido+" empresa - "+empresa);
      try {
         int delete = FactoriaDao.getInstance().executeUpdate("DELETE FROM "+empresa+".SINC_PEDIDOS WHERE PED_COD =" +pedido+"");
         return delete > 0;
      } catch (DaoException e) {
         return false;
      }
   }
   
}
