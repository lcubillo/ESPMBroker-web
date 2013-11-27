<%@ page contentType="text/html;charset=iso-8859-1" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://utils.espmail.com" prefix="ut"%>

<html:errors/>

<html:form method="post" action="/app/personal/cambiopassword.do">
	<ut:field key="personal.cambioPassword.oldPassword" required="true">
		<html:password  property="oldPassword"/>
	</ut:field>
	<ut:field key="personal.cambioPassword.newPassword1" required="true">
		<html:password property="newPassword1"/>
	</ut:field>
	<ut:field key="personal.cambioPassword.newPassword2" required="true">
		<html:password property= "newPassword2"/>
	</ut:field>	
	<div class="form_actions">
		<html:submit property="summit" styleClass="button">
			<fmt:message key="personal.cambioPassword.cambiar" />
		</html:submit>
	</div>
</html:form>