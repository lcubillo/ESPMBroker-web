package com.espmail.broker.modelo;

import java.util.List;

import com.espmail.utils.dao.DaoException;


public interface UsuarioDao {
	
	/**
	 * Devuelve todos los usuarios de una lista.
	 * @param idCliente
	 * @param idLista
	 * @return Usuario[]
	 */
	Usuario[] findAll(String idCliente, int idLista) throws DaoException;

	/**
	 * Devuelve el usuario que pertenece a la lista y con el identificador del email dado
	 * @param idCliente
	 * @param idLista identificador de la lista.
	 * @param idEmail identificador del email
	 * @return usuario.
	 */
	Usuario find(String idCliente, int idLista, long idEmail) throws DaoException;

	/**
	 * Devuelve los usuarios con el email dado.
	 * @param idCliente
	 * @param email
	 * @return
	 */
	List findByEmail(String idCliente, String email) throws DaoException;

   /**
    * Devuelve un usuario presente en un idEnvio
    * @param idEnvio
    * @param idLista
    * @param idEmail
    * @return
    * @throws DaoException
    */
   Usuario findByIdEnvio(int idEnvio, int idLista, long idEmail) throws DaoException;

   /**
	 * borrar un usario con la lista y el email determinado.
	 * @param idCliente
	 * @param idLista identificador de la lista.
	 * @param idEmail identificador del email.
	 */
	void eliminar(String idCliente, int idLista, long idEmail, String ip) throws DaoException;

	/**
	 * Inserta un usuario.
	 * @param usuario 
	 */
	void insertar(String idCliente, Usuario usuario) throws DaoException;
	
	/**
	 * 
	 * @param idEmail
	 * @param idLista
	 * @param email
	 * @param nombre
	 * @param apellidos
	 * @param tratamiento
	 * @param fechaAlta
	 * @param fechaAcceso
	 * @param t1
	 * @param t2
	 * @param t3
	 * @throws DaoException
	 */
	void altaUsuario(long idEmail,int idLista,String email,String nombre,String apellidos,String tratamiento,java.sql.Timestamp fechaAlta,java.sql.Timestamp fechaAcceso,String t1,String t2,String t3) throws DaoException;
	/**
	 * Cambia el estado de la tabla enviados
	 *
	 * @param idEmail
	 * @param idEnvio
    * @param estado
	 *
	 */
	void cambiaEstadoEnviado(long idEmail, int idEnvio,
			EstadoEnviado estado) throws DaoException;

   /**
    * Cambia el estado de la tabla enviados para un rango de emails
    *
    * @param idEmailInicio
    * @param idEmailFin
    * @param idEnvio
    * @param estado
    * @throws DaoException
    */
   void actualizaEnviados (long idEmailInicio, long idEmailFin, int idEnvio, String estado)throws DaoException;

   /**
    * Valida que exista un usuario en una lista
    * @param idCliente
    * @param idLista
    * @param idEmail
    * @return
    * @throws DaoException
    */
   int existeUsuarioLista(int idLista, String email) throws DaoException;
   
}
