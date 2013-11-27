<%@ page contentType="text/html;charset=iso-8859-1" language="java"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://utils.espmail.com" prefix="ut"%>

<html:form action="/app/envios/test.do">
	<html:hidden property="idEnvio"/>
	<html:errors/>
	<ut:field key="envios.test.emails1">
		<html:text property="emails[0]"/>
	</ut:field>
	<ut:field key="envios.test.emails2">
		<html:text property="emails[1]"/>
	</ut:field>
	<ut:field key="envios.test.emails3">
		<html:text property="emails[2]"/>
	</ut:field>
	<ut:field key="envios.test.emails4">
		<html:text property="emails[3]"/>
	</ut:field>
	<ut:field key="envios.test.emails5">
		<html:text property="emails[4]"/>
	</ut:field>
	<div class="form_actions">
		<html:submit property="summit" styleClass="button"><fmt:message key="envios.test.lanza"/></html:submit>
	</div>
</html:form>

