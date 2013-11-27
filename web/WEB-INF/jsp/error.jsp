<%@ page contentType="text/html;charset=iso-8859-1" language="java" isErrorPage="true" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>

<html:errors/>

<div class="error">
	<c:if test="${param.code != null}">
		<fmt:message key="error.${param.code}"/>
		
		<c:if test="${param.code == '500'}">
			<dl>
				<dt><fmt:message key="error.clase"/></dt>
				<dd><%= exception.getClass() %></dd>
				<dt><fmt:message key="error.mensaje"/></dt>
				<dd><%= exception.getMessage()%></dd>
				<% exception.printStackTrace(); %>
			</dl>
		</c:if>
	</c:if>
</div>