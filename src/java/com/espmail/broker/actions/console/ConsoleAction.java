/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.espmail.broker.actions.console;

import com.espmail.lanzador.Lanzador;


import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXServiceURL;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Luis Miguel Cubillo
 */
public class ConsoleAction extends org.apache.struts.action.Action {
    
    /* forward name="success" path="" */
    private static final String SUCCESS = "success";
    
    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

       /**
        * El servidor será el que marque en el formulario.
        */
       ConsoleForm formulario = (ConsoleForm)form;

       JMXServiceURL url = new JMXServiceURL(

         "service:jmx:rmi:///jndi/rmi://"+formulario.getHost()+":9999/server");
      JMXConnector jmxc = JMXConnectorFactory.connect(url, null);


      ObjectName mbeanName =  new ObjectName(Lanzador.OBJECT_NAME);
      MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
      mbsc.createMBean("Lanzador", mbeanName, null, null);
      mbsc.invoke(mbeanName, formulario.getMethod(), null, null);

        return mapping.findForward(SUCCESS);
    }
}
