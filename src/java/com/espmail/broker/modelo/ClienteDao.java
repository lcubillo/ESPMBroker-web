package com.espmail.broker.modelo;

import java.util.List;

import com.espmail.utils.dao.DaoException;

public interface ClienteDao {

	Cliente findById(String id) throws DaoException;
	
	
	/**
	 * Cambia la contraseña de un usuario
	 * 
	 * @param idCliente
	 * @param oldPassword
	 * @param newPassword
	 * @throws DaoException
	 */
	void cambiarPassword (String idCliente, String oldPassword, String newPassword) throws DaoException;
	

	/**
	 * 
	 * @param idCliente
	 * @throws DaoException
	 */
	List getPassword (String idCliente) throws DaoException;
}
