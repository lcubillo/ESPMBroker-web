<%@ page contentType="text/html;charset=iso-8859-1" language="java"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://utils.espmail.com" prefix="ut"%>

<html:form action="/app/listas/busqueda.do" method="post" enctype="multipart/form-data">
	<html:errors/>

	<ut:field key="listas.busqueda.email" required="true">
		<html:text property="email" />
	</ut:field> 
	<div class="form_actions">
		<html:submit styleClass="button">
			<fmt:message key="listas.busqueda.buscar" />
		</html:submit>
	</div>	

	<c:choose>
		<c:when test="${busquedaEmailForm.usuarios != null}">
			<table class="listado">
				<thead>
					<tr>
						<th class="first-child"></th>
						<th><fmt:message key="listas.busqueda.lista" /></th>
						<th><fmt:message key="listas.busqueda.nombre" /></th>
						<th><fmt:message key="listas.busqueda.apellidos" /></th>
						<th><fmt:message key="listas.busqueda.email" /></th>
						<th class="last-child"><fmt:message key="listas.busqueda.fechaCreacion" /></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="item" items="${busquedaEmailForm.usuarios}">
						<tr>
							<td><html:multibox property="seleccionados" value="${item.id}"/></td>
							<td  class="first-child strong">${item.nombreLista}</td>	
							<td>${item.nombre}</td>
							<td>${item.apellidos}</td>
							<td>${item.email}</td>
							<td>${item.fechaCreacion }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<div class="form_actions">
				<html:submit property="summit" styleClass="button">
					<fmt:message key="listas.busqueda.eliminar" />
				</html:submit>
			</div>
		</c:when>
		<c:when test="${busquedaEmailForm.email != null && busquedaEmailForm.email != ''}">
		 	No se encontró ningun usuario para ese email.
		</c:when>
	</c:choose>
</html:form>