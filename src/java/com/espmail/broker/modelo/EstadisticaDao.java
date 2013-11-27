package com.espmail.broker.modelo;

import java.util.List;

import com.espmail.utils.dao.DaoException;

public interface EstadisticaDao {
	
	/**
	 * Devuelve todos los datos para la estadística.
	 * @return List con los datos para la estadística
	 */
	List dameResumen(String idCliente) throws DaoException;
	
	/**
	 * 
	 * @param idCliente
	 * @param idEnvio
	 * @return
	 * @throws DaoException
	 */
	List dameTotalesResumenDia (String idCliente,int idEnvio) throws DaoException;
	
	/**
	 * Devuelve la estadística perteneciente a un envio
	 * @param idCliente
	 * @param idEnvio
	 * @return
	 * @throws DaoException
	 */
		
	List dameResumenEnvio(String idCliente,int idEnvio) throws DaoException;
	
	/**
	 * Devuelve todos los clicks de un cliente.
	 * @param id_cliente Identificador del cliente.
	 * @return
	 * @throws DaoException
	 */
	List dameClicks(String idCliente,int idEnvio) throws DaoException;
	/**
	 * Devuelve los envios de cada día de un cliente y un mismo envio
	 * @param id_cliente
	 * @return
	 * @throws DaoException
	 */
	List dameResumenDia(String idCliente,int idEnvio) throws DaoException;
	
	/**
	 * Devuelve una lista con los envios enviados
	 * @param idCliente
	 * @return Lista con los envios enviados
	 */

	List dameEnviosEnviados (String idCliente);
	
	/**
	 * Devuelve el listado de los usuarios borrados filtrados por el id_envio
	 * @param idEnvio 
	 * @return Lista con los usuarios borrados
	 */
	List dameBajasEnvio(int idEnvio);
	
	/**
	 * Devuelve el listado de los usuarios borrados filtrados por el id_lista
	 * @param idLista 
	 * @return Lista con los usuarios borrados.
	 */
	List dameBajasListas(int idLista);
	/**
	 * Devuelve información con los emails que fallaron en el envío
	 * @param idCliente
	 * @param idEnvio
	 * @return
	 */
	List dameEnvioEmailsFallidos(String idCliente,int idEnvio) throws DaoException;;
}
