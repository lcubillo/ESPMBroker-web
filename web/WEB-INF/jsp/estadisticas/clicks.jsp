<%@ page contentType="text/html;charset=iso-8859-1" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://utils.espmail.com" prefix="ut"%>
<script language="JavaScript" type="text/JavaScript">
function ir(){	
	  var indice = document.getElementById("enviosEnviados").selectedIndex	  	   
	id =  document.getElementById("enviosEnviados").options[indice].value;	
	url="/app/estadisticas/clicks.do?idEnvio="+id;	
	location.href=url;	 
}
</script>	

<ut:field key="envios.estadistica.etiqueta">
	<select name="enviosEnviados" id="enviosEnviados" onchange="ir();">
		<option value="">Seleccione</option>
		<c:forEach var="item" items="${enviosEnviados}">
			<c:url var="urlEstadisticaDia" value="/app/estadisticas/clicks.do">
				<c:param name="idEnvio" value="${item.idEnvio}"/>
			</c:url>
			<c:choose>
				<c:when test="${item.idEnvio == resumenEnvio.idEnvio}">
					<option value="${item.idEnvio}" selected="selected">${item.etiqueta}</option>
				</c:when>
				<c:otherwise>
					<option value="${item.idEnvio}" >${item.etiqueta}</option>
				</c:otherwise>
			</c:choose>
		</c:forEach>
	</select>
</ut:field>
<c:if test="${not empty resumenEnvio}">
	<ut:field key="estadisticas.clicks.fechaEnvio">
		${resumenEnvio.fechaEnvio}
	</ut:field>
	<ut:field key="estadisticas.clicks.mailsEnviados">
		${resumenEnvio.emailsEnviados}
	</ut:field>
	<c:url var="urlCsvEstadisticaClick" value="/app/informe.csv?operacion=estadisticaClick&idEnvio=">
		<c:param name="idEnvio" value="${resumenEnvio.idEnvio}"/>
	</c:url>
	<div class="form_actions">
		<a class="csvlink" href="${urlCsvEstadisticaClick}">Descargar Fichero.</a>
	</div>
</c:if>

<c:if test="${not empty clicks}">	
	
	<table class="listado">
		<thead>
			<tr>
				<th class="first-child"><fmt:message key="estadisticas.clicks.orden"/></th>									
				<th><fmt:message key="estadisticas.clicks.clicks"/></th>														
				<th class="last-child"><fmt:message key="estadisticas.clicks.url"/></th>					
			</tr>
		</thead>
		<tbody>
			<c:forEach var="item" items="${clicks}">
				<tr>
					<td class="number">${item.num}</td>										
					<td class="number">${item.clicks}</td>
					<td>${item.url}</td>						
 				</tr>
			</c:forEach>
		</tbody>
	</table>
	<ut:chart type="p3" title="Estadistica por click" data="${clicks}" columnLabel="num" columnValue="clicks"/>

</c:if>