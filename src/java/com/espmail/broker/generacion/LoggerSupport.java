package com.espmail.broker.generacion;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.espmail.utils.TextUtils;

public class LoggerSupport {

	private final Log log;
	private final boolean mbean;
	
	public LoggerSupport(boolean mbean) {
		this.mbean = mbean;
		this.log = LogFactory.getLog(this.getClass());
	}
	
	public void debug(String message) {
		if (!this.mbean) {
			this.log.debug(message);
		} else {
			write("DEBUG", message);
		}
	}

	public void error(Throwable t) {
		if (!this.mbean) {
			this.log.error(t.getMessage());
		} else {
			write("ERROR", t.getMessage());
		}
		
		t.printStackTrace();
	}
	
	private void write(String type, String message) {
		StringBuffer st = new StringBuffer(TextUtils.asString(new Timestamp(new Date().getTime())));
		st.append(" ").append(type);
		st.append(" --> ").append(message);

		System.out.println(st.toString());
		
		//this.sb.insert(0, st.toString());*/
	}
}
