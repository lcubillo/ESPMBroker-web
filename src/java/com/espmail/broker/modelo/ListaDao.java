package com.espmail.broker.modelo;

import com.espmail.utils.dao.DaoException;

public interface ListaDao {
	
	/**
	 * Devuelve todas las listas de un cliente.
	 * @param idCliente
	 * @return Lista[]
	 */
	Lista[] findAll(String idCliente) throws DaoException;
	
	/**
	 * Devuelve una lista a partir de su identificador.
	 * @param idCliente
	 * @param idLista Identificador de la lista.
	 * @return Lista.
	 */
	Lista findById(String idCliente, int idLista) throws DaoException;
	
	/**
	 * Devuelve las listas asociadas a un envio.
	 * @param idCliente
	 * @param idEnvio
	 * @return Lista[]
	 */
	Lista[] findByEnvio(String idCliente, int idEnvio) throws DaoException;
	
	/**
	 * inserta una lista en la base de datos.
	 * @param lista Lista con los datos a insertar.
	 */
	void insert(Lista lista) throws DaoException;

    /**
     * devuelve el siguiente valor en la secuencia que se
     * utilizara como id_lista en la creacion
     * @return  el id_lista
     */
    int getId() throws DaoException;

	/**
	 * Actualiza la lista
	 * @param lista Lista con los nuevos valores.
	 */
    void update(Lista lista) throws DaoException;

    /**
     *
     * @param idCliente
     * @param idLista identificador de lista
     * @throws DaoException
     */
    void eliminar(String idCliente, int idLista ) throws DaoException;

    /**
     * Actualiza el numero de registros de una lista sumando el valor sum.
     * 
     * @param num
     * @throws DaoException
     */
    void actualizaRegistros(String idCliente, int idLista, int num) throws DaoException;
    
    /**
     * Método para comprobar si existe un nombre de lista para un cliente
     * 
     * @param idCliente
     * @param nombre
     * @return
     * @throws DaoException
     */
    boolean existeNombre(String idCliente, int idLista, String nombre) throws DaoException;
    
    
}
