<%@ page contentType="text/html;charset=iso-8859-1" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://utils.espmail.com" prefix="ut"%>

<script language="JavaScript" type="text/JavaScript">
function ir(){	
	  var indice = document.getElementById("enviosEnviados").selectedIndex	  	   
	id =  document.getElementById("enviosEnviados").options[indice].value;	
	url="/app/estadisticas/dia.do?idEnvio="+id;	
	location.href=url;	 
}
</script>	
<ut:field key="envios.estadistica.etiqueta">
	<select name="enviosEnviados" id="enviosEnviados" onchange="ir();">
		<option value="">Seleccione</option>
		<c:forEach var="item" items="${enviosEnviados}">
			<c:url var="urlEstadisticaDia" value="/app/estadisticas/dia.do">
				<c:param name="idEnvio" value="${item.idEnvio}" />
			</c:url>
			<c:choose>
				<c:when test="${item.idEnvio == resumenEnvio.idEnvio}">
					<option value="${item.idEnvio}" 
						 selected="selected">${item.etiqueta}</option>
				</c:when>
				<c:otherwise>
					<option value="${item.idEnvio}">${item.etiqueta}</option>
				</c:otherwise>
			</c:choose>
		</c:forEach>
	</select>
</ut:field>
<c:if test="${not empty resumenEnvio}">
	<ut:field key="estadisticas.dia.fechaEnvio">
		${resumenEnvio.fechaEnvio}
	</ut:field>
	<ut:field key="estadisticas.dia.mailsEnviados">
		${resumenEnvio.emailsEnviados}
	</ut:field>
	<c:url var="urlCsvEstadisticaDia" value="/app/informe.csv?operacion=estadisticaDia">
		<c:param name="idEnvio" value="${resumenEnvio.idEnvio}" />
	</c:url>
	<div class="form_actions">
		<a class="csvlink" href="${urlCsvEstadisticaDia}">Descargar Fichero</a>
	</div>
</c:if>


<c:if test="${not empty estadisticaDia}">
	<table class="listado">
	<c:if test="${not empty totales}">
	<thead>
			<c:forEach var="item" items="${totales}">
				<tr bgcolor="#FFFFFF">
					<td class="totales" align="center" ><fmt:message key="estadisticas.dia.totales"/></td>		
					<td class="totales"><fmt:message key="estadisticas.dia.lecturas"/>: <span class="totalesNum"><fmt:formatNumber type="number" value="${item.lecturas}"/></span></td>
					<td class="totales"><fmt:message key="estadisticas.dia.clicks"/>: <span class="totalesNum"><fmt:formatNumber type="number" value="${item.Clicks}"/></span></td>
					<td class="totales"><fmt:message key="estadisticas.dia.numBajas"/>: <span class="totalesNum"><fmt:formatNumber type="number" value="${item.Bajas}"/></span></td>					
 				</tr>
			</c:forEach>
	
</c:if>
		
			<tr>
				<th width="200" class="first-child"><fmt:message key="estadisticas.dia.fecha"/></th>				
				<th width="200"><fmt:message key="estadisticas.dia.lecturas"/></th>
				<th width="200"><fmt:message key="estadisticas.dia.clicks"/></th>
				<th width="200"><fmt:message key="estadisticas.dia.numBajas"/></th>												
			</tr>
		</thead>
		<tbody>
			<c:forEach var="item" items="${estadisticaDia}">
				<tr>
					<td align="center">${item.fecha}</td>		
					<td class="number"><fmt:formatNumber type="number" value="${item.lecturas}"/></td>
					<td class="number"><fmt:formatNumber type="number" value="${item.clicks}"/></td>
					<td class="number"><fmt:formatNumber type="number" value="${item.bajas}"/></td>					
 				</tr>
			</c:forEach>
		</tbody>
	</table>
	<ut:chart type="lc" title="Estadistica por click" data="${estadisticaDia}" columnLabel="fecha" columnValue="lecturas,clicks,bajas,rebotes"/>	
</c:if>
