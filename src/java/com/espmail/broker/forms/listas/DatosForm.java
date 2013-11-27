package com.espmail.broker.forms.listas;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import org.apache.struts.upload.FormFile;

import org.apache.struts.validator.ValidatorForm;

import com.espmail.broker.modelo.ListaDao;
import com.espmail.utils.TextUtils;
import com.espmail.utils.dao.DaoException;
import com.espmail.utils.dao.FactoriaDao;

public class DatosForm extends ValidatorForm {

	private static final long serialVersionUID = 147223338611154771L;

	private String nombreLista;
	private String formato="csv";
	private int[] columnas;
	private String T1,T2,T3;
	private DatosForm.Fila [] datos;
    private String nombreArchivo;
    private FormFile fichero;
    private int idLista;
    private int paso;

	public int getPaso() {
		return this.paso;
	}

	public void setPaso(int paso) {
		this.paso = paso;
	}

	public int[] getColumnas() {
		return columnas;
	}

	public void setColumnas(int[] columnas) {
		this.columnas = columnas;
	}

	public Fila[] getDatos() {
		return datos;
	}

	public void setDatos(Fila[] datos) {
		this.datos = datos;
	}

	public String getFormato() {
		return formato;
	}

	public void setFormato(String formato) {
		this.formato = formato;
	}

	public String getNombreLista() {
		return nombreLista;
	}

	public void setNombreLista(String nombreLista) {
		this.nombreLista = nombreLista;
	}

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public int getIdLista() {
		return idLista;
	}

	public void setIdLista(int idLista) {
		this.idLista = idLista;
	}

	public String getT1() {
        return T1;
    }

    public void setT1(String t1) {
        T1 = TextUtils.isEmpty(t1)? null : t1.toUpperCase();;
    }

    public String getT2() {
        return T2;
    }

    public void setT2(String t2) {
        T2 = TextUtils.isEmpty(t2)? null : t2.toUpperCase();
    }

    public String getT3() {
        return T3;
    }

    public void setT3(String t3) {
        T3 = TextUtils.isEmpty(t3)? null : t3.toUpperCase();
    }

    public FormFile getFichero() {
        return fichero;
    }

    public void setFichero(FormFile fichero) {
        this.fichero = fichero;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest req) {
    	if (this.paso != 1 || TextUtils.isEmpty(req.getParameter("summit"))) {
    		return null;
    	}
    	
    	ActionErrors errors = super.validate(mapping, req);
    	
    	if (errors == null || errors.isEmpty()) {
    		try {
				ListaDao dao = ((ListaDao) FactoriaDao.getInstance().getDao(ListaDao.class));

				if (dao.existeNombre(req.getRemoteUser(), this.idLista, this.nombreLista)) {
					if (errors == null) {
						errors = new ActionErrors();
					}
					
					errors.add(ActionErrors.GLOBAL_MESSAGE,
							new ActionMessage("listas.datos.nombre.error"));
				}
			} catch (DaoException e) {
				throw new RuntimeException(e);
			}
    	}

    	return errors;
	}

	public static class Fila implements java.io.Serializable {
		private static final long serialVersionUID = 5528106556291061495L;
		
		private String[] valores;

        public Fila(String[] valores) {
            this.valores = valores;
        }

        public Fila() {
        }

        public String[] getValores() {
            return valores;
        }

        public void setValores(String[] valores) {
            this.valores = valores;
        }
    }
}
