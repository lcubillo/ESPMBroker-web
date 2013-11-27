<%@ page contentType="text/html;charset=iso-8859-1" language="java"%>
<%@ page import="org.apache.struts.util.TokenProcessor"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic-el" prefix="logic"%>
<%@ taglib uri="http://utils.espmail.com" prefix="ut"%>
<%
	TokenProcessor token = TokenProcessor.getInstance();
	token.saveToken(request);
%>

<html:errors />
<html:form action="${path}" method="post" enctype="multipart/form-data">
    <html:hidden property="idLista" />
    <html:hidden property="paso" value="1" /> 

	<c:choose>
		<c:when test="${listas_datosForm.idLista == 0}">
			<ut:field key="listas.datos.nombreLista" required="true">
				<html:text property="nombreLista" />
			</ut:field>
		</c:when>
		<c:otherwise>
			<html:hidden property="nombreLista" /> 
			<ut:field key="listas.datos.nombreLista" value="${listas_datosForm.nombreLista}"/>
		</c:otherwise>
	</c:choose>
	<ut:field key="listas.datos.fichero" required="true">
		<html:file property="fichero" size="50" />
	</ut:field> 
	<div class="form_actions">
		<html:submit property="summit" styleClass="button">
			<fmt:message key="listas.datos.submit" />
		</html:submit>
		<html:reset styleClass="button">
			<fmt:message key="listas.datos.cancelar" />
		</html:reset>
	</div>
</html:form>
