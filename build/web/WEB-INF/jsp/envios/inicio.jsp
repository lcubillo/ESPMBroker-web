<%@ page contentType="text/html;charset=iso-8859-1" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://utils.espmail.com" prefix="ut"%>

<div>
	<h3><fmt:message key="envios.listado.titulo"/></h3>
	<table>
		<thead>
			<tr>
				<th class="first-child"><fmt:message key="envios.listado.etiqueta"/></th>
				<th><fmt:message key="envios.listado.descripcion"/></th>
				<th><fmt:message key="envios.listado.estado"/></th>
				<th><fmt:message key="envios.listado.fechaEnvio"/></th>
				<th class="last-child"></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="item" items="${envios}">
				<tr>
					<td>${item.etiqueta}</td>
					<td>${item.descripcion}</td>
					<td><span class="estado_${item.estado}" title="<fmt:message key="envios.estado.${item.estado}" />">&nbsp;</span></td>
					<td><fmt:formatDate type="both" dateStyle="short" value="${item.fechaEnvio}"/></td>
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
</div>
<div>
	<h3><fmt:message key="envios.estadistica.titulo"/></h3>
	<table>
		<thead>
			<tr>
				<th width="200" class="first-child"><fmt:message key="envios.estadistica.etiqueta"/></th>
				<th width="200"><fmt:message key="envios.estadistica.fechaEnvio"/></th>				
				<th width="200"><fmt:message key="envios.estadistica.mailsEnviados"/></th>
				<th width="200"><fmt:message key="envios.estadistica.lecturas"/></th>
				<th width="200"><fmt:message key="envios.estadistica.clicks"/></th>
				<th width="200"><fmt:message key="envios.estadistica.numBajas"/></th>
				<th width="200" class="last-child"><fmt:message key="envios.estadistica.numRebotes"/></th>								
			</tr>
		</thead>
		<tbody>
			<c:forEach var="item" items="${estadistica}">
				<tr>
					<td>
						<c:url var="estadisticaDia" value="/app/estadisticas/dia.do">
							<c:param name="idEnvio" value="${item.idEnvio}"/>
							<c:param name="etiqueta" value="${item.etiqueta}"/>
						</c:url>
						<a href="${estadisticaDia}">${item.etiqueta}</a>
					</td>
					<td align="center">${item.fechaEnvio}</td>					
					<td class="number"><fmt:formatNumber type="number" value="${item.emailsEnviados}"/></td>
					<td class="number"><fmt:formatNumber type="number" value="${item.lecturas}"/>%</td>
					<td class="number">
						<c:url var="estadisticaClicks" value="/app/estadisticas/clicks.do">
							<c:param name="idEnvio" value="${item.idEnvio}"/>
						</c:url>
						<a href="${estadisticaClicks}">
							<fmt:formatNumber type="number" value="${item.clicks}" maxFractionDigits="2"/>%</td>
						</a>
					</td>					
					<td class="number"><fmt:formatNumber type="number" value="${item.bajas}" maxFractionDigits="2"/>%</td>
					<td class="number"><fmt:formatNumber type="number" value="${item.rebotes}" maxFractionDigits="2"/>%</td>
 				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>