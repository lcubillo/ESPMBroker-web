<%@ page contentType="text/html;charset=iso-8859-1" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://utils.espmail.com" prefix="ut"%>

<%
String com=(String)request.getAttribute("incompatibles")==null?"":(String)request.getAttribute("incompatibles");
	%>

<html:form action="/app/envios/asociarListas.do">
	<html:errors/>
	<html:hidden property="idEnvio"/>
	<%if (com=="S"){%>
			<table class="listado">
			<tr>			
				<td><font color="red"><fmt:message key="envios.asociarListas.errorNoCompatibles" /></font></td>			
			</tr>
		</table>
	<%}%>
	<table class="listado">
		<thead>
			<tr>
				<th></th>
				<th><fmt:message key="envios.asociarListas.nombre" /></th>
				<th><fmt:message key="envios.asociarListas.numRegistros" /></th>
				<th><fmt:message key="envios.asociarListas.fechaCreacion" /></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="item" items="${envios_asociarListasForm.listas}">
				<tr>
					<td>
						<html:multibox property="listasSeleccionadas" value="${item.idLista}"/>
					</td>
					<td>${item.nombre}</td>
					<td>${item.numeroRegistros}</td>
					<td>
						<fmt:formatDate type="both" value="${item.creacion}"/>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<div class="form_actions">
		<html:submit property="summit" styleClass="button">
			<fmt:message key="envios.asociarListas.submit" />
		</html:submit>
		<html:reset styleClass="button"><fmt:message key="envios.asociarListas.reset" /></html:reset>
	</div>
</html:form>

