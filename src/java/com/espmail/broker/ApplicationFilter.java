package com.espmail.broker;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;

import com.espmail.broker.actions.listas.MapeoAction;
import com.espmail.utils.contexto.Contexto;

public class ApplicationFilter implements Filter {
	
	private static final Log LOG = LogFactory.getLog(ApplicationFilter.class);
	
	public void destroy() {
		// Nada
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
      System.out.println();
		HttpServletRequest req = (HttpServletRequest) request;
		try{
			
		}catch(Exception ex){
			ex.printStackTrace();
		}

		request.setAttribute("path", req.getServletPath());
		Contexto.getInstance().setIdioma((Locale) req.getSession()
				.getAttribute(Globals.LOCALE_KEY));
		chain.doFilter(request, response);
		
	}

	public void init(FilterConfig config) throws ServletException {
		// Nada
	}

}
