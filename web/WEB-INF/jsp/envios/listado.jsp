<%@ page contentType="text/html;charset=iso-8859-1" language="java"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://utils.espmail.com" prefix="ut"%>

<html:form action="/app/envios/listado.do">

	<table class="listado">
		<thead>
			<tr>
				<th class="first-child"><fmt:message key="envios.listado.etiqueta"/></th>
				<th><fmt:message key="envios.listado.descripcion"/></th>
				<th><fmt:message key="envios.listado.estado"/></th>
				<th ><fmt:message key="envios.listado.fechaEnvio"/></th>
				<th ><fmt:message key="envios.listado.descargas"/></th>
				<th class="last-child"></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="item" items="${envios_listadoForm.envios}">
				<tr>
					<td>${item.etiqueta}</td>
					<td>${item.descripcion}</td>
					<td><span class="estado_${item.estado}" title="<fmt:message key="envios.estado.${item.estado}" />">&nbsp;</span></td>
					<td><fmt:formatDate type="both" dateStyle="short" value="${item.fechaEnvio}"/></td>
					<td>
						<c:url var="bajas" value="/app/informe.csv?operacion=bajasEnvio" >
							<c:param name="idEnvio" value="${item.idEnvio}"/>
						</c:url>
						<a class="csvlink" href="${bajas}">Bajas</a>
					</td>
					<td>
						<c:url var="editDatosEnvio" value="/app/envios/datos.do">
							<c:param name="idEnvio" value="${item.idEnvio}"/>
						</c:url>
						<a href="${editDatosEnvio}" class="button">Editar</a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

</html:form>
