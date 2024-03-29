package com.espmail.broker.modelo;

import java.sql.Date;

import com.espmail.utils.dao.DaoException;

public interface EnvioDao {
	
	Envio[] find(String idCliente) throws DaoException;

	Envio findById(String idCliente, int idEnvio) throws DaoException;
	
	/**
	 * No se debe usar en la aplicaci�n de clientes
	 * 
	 * @param idCliente
	 * @param idEnvio
	 * @return
	 * @throws DaoException
	 */
	Envio findByIdPrivado(int idEnvio) throws DaoException;
	
	void insert(Envio envio) throws DaoException;
	
	/**
	 * Actualiza un envio, si est� en estado pendiente.
	 * 
	 * @param envio
	 * @throws DaoException
	 */
	void update(Envio envio) throws DaoException;
	
	/**
	 * Comprueba si existe la etiqueta para un cliente y es distinta a la del envio.
	 * 
	 * @param idCliente
	 * @param idEnvio
	 * @param etiqueta
	 * @return
	 * @throws DaoException
	 */
	boolean existeEtiqueta(String idCliente, int idEnvio, String etiqueta) throws DaoException;

	/**
	 * Devuelve los ids de los envios listos para enviar
	 * 
	 * @return
	 * @throws DaoException
	 */
	Integer[] findListos() throws DaoException;
	
	/**
	 * 
	 * @return
	 * @throws DaoException
	 */
	Integer[] findListosEjecucion(String maquina) throws DaoException;
	
	/**
	 * 
	 * @param idEnvio
	 * @return
	 * @throws DaoException
	 */
	String findMaquinaEnvio(int idEnvio) throws DaoException;
	
	
	
	/**
	 * Obtiene un envio y bloquea la tupla
	 * 
	 * @param idEnvio
	 * @return
	 * @throws DaoException
	 */
	Envio findByIdForUpdate(int idEnvio) throws DaoException;
	
    /**
     * devuelve el siguiente valor en la secuencia que se
     * utilizara como id_envio en la creacion
     * @return  el id_envio
     */
    int getId() throws DaoException;
    
	/**
	 * Borra todas las listas asociadas a un envio, si est� en estado pendiente.
	 * 
	 * @param idCliente
	 * @param idEnvio
	 * @throws DaoException
	 */
	void deleteListas(String idCliente, int idEnvio) throws DaoException;
	
	/**
	 * Asocia una lista a un envio, si est� en estado pendiente.
	 * 
	 * @param idCliente
	 * @param idEnvio
	 * @param idLista
	 * @throws DaoException
	 */
	void insertLista(String idCliente, int idEnvio, int idLista) throws DaoException;
	
	/**
	 * Devuelve las listas asociadas a un envio.
	 * 
	 * @param idCliente
	 * @param idEnvio
	 * @return
	 * @throws DaoException
	 */
	Integer[] findListas(String idCliente, int idEnvio) throws DaoException;
	
	/**
	 * Cambia el estado de un envio
	 * 
	 * @param idCliente
	 * @param idEnvio
	 * @param estado
	 */
	void cambiaEstado(int idEnvio, EstadoEnvio estado) throws DaoException;
	
	/**
	 * Cambia el estado de un envio y la maquina q lo trata
	 * @param idEnvio
	 * @param estado
	 * @param maquina
	 * @throws DaoException
	 */
	void cambiaEstadoMaquina(int idEnvio,EstadoEnvio estado,String maquina) throws DaoException;
	
	
	/**
	 * Inserta la fecha en la cual se quiere que se ejecute el envio
	 * 
	 * @param idEnvio
	 * @param idCliente
	 * @param fechaEnvio
	 */
	void insertFechaEnvio(String idCliente, int idEnvio,java.sql.Timestamp fechaEnvio) throws DaoException;
	
	/**
	 * Prepara un envio, pone la fecha de envio y genera las tuplas de la tabla
	 * enviados.
	 * 
	 * @param idCliente
	 * @param idEnvio
	 */
	
	void prepararEnvio(String idCliente, int idEnvio) throws DaoException;

	/**
	 * Devuelve los codigos de los envios que se encuentren enviando m�s de 24horas
	 * 
	 * @return
	 * @throws DaoException
	 */
	Integer[] findEnviosParados() throws DaoException;
}
