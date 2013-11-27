package com.espmail.broker.forms.altas;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.espmail.utils.dao.DaoException;

public class MonografiasForm  extends ActionForm {

	
	public String p1;//Email a dar de alta
	public String p2;//Pais --->ETIQUETA_T1-->T1
	public String p3;//nombre
	public String p4;//apellidos
	public String p5;//tratamiento
	public String p6;//T2
	public String p7;//T3
	
	public String getP1() {
		return p1;
	}

	public void setP1(String p1) {
		this.p1 = p1;
	}

	public String getP2() {
		return p2;
	}

	public void setP2(String p2) {
		this.p2 = p2;
	}

	public String getP3() {
		return p3;
	}

	public void setP3(String p3) {
		this.p3 = p3;
	}

	public String getP4() {
		return p4;
	}

	public void setP4(String p4) {
		this.p4 = p4;
	}

	public String getP5() {
		return p5;
	}

	public void setP5(String p5) {
		this.p5 = p5;
	}

	public String getP6() {
		return p6;
	}

	public void setP6(String p6) {
		this.p6 = p6;
	}

	public String getP7() {
		return p7;
	}

	public void setP7(String p7) {
		this.p7 = p7;
	}

	

	public ActionErrors validate(ActionMapping mapping,	HttpServletRequest req) {
		return null;
	}
}
