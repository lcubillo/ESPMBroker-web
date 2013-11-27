package com.espmail.broker;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.InitialContext;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.logging.LogFactory;

import com.espmail.utils.contexto.Contexto;

/**
 * Define el contexto de la aplicación
 * @author Luis Cubillo
 *
 */
public class AppContexto extends Contexto {

	public static final String MAIL_SERVER = "mail.server";
	public static final String MAIL_DEBUG = "mail.debug";

	/**
	 * Obtiene la conexion con la base de datos.
	 */
	private static DataSource DS = null;

	public Connection getConexion() throws SQLException {
		if (DS == null) {
			LogFactory.getLog(this.getClass()).info("AppContexto: Nueva Conexion");	
			init(null);
		}
		LogFactory.getLog(this.getClass()).info("Conexion "+DS.getConnection().getMetaData().getUserName());
		
		return DS.getConnection();
	}

	/**
	 * Crea la conexion con la base de datos.
	 * @throws SQLException Excepcion en caso de error.
	 */
	public synchronized void init(Properties props) throws SQLException {
		if (DS == null) {
			try {
				if (props == null) {					
                DS = (DataSource) new InitialContext()
							.lookup("java:comp/env/jdbc/pool");
				} else {					
					DS = BasicDataSourceFactory.createDataSource(props);
				}
			} catch (Exception e) {
				throw new SQLException(e.getMessage());
			}
		}
	}
}
