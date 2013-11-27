<%@ page contentType="text/html;charset=iso-8859-1" language="java"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic-el" prefix="logic"%>
<%@ taglib uri="http://utils.espmail.com" prefix="ut"%>


	<ut:field key="envios.resumen.etiqueta" value="${envio.etiqueta}"/>
	<ut:field key="envios.resumen.asunto" value="${envio.asunto}"/>
	<ut:field key="envios.resumen.remitente" value="${envio.remitente}"/>
	<ut:field key="envios.resumen.replyTo" value="${envio.replyTo}"/>
	<ut:field key="envios.resumen.descripcion" value="${envio.descripcion}"/>
	<ut:field key="envios.resumen.estado">
		<fmt:message key="envios.estado.${envio.estado}"/>
	</ut:field>


	<table class="listado">
		<thead>
			<tr>
				<th class="first-child"><fmt:message key="envios.resumen.listas.nombre" /></th>
				<th><fmt:message key="envios.resumen.listas.numRegistros" /></th>
				<th class="last-child"><fmt:message key="envios.resumen.listas.fechaCreacion" /></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="item" items="${listas}">
				<tr>
					<td>${item.nombre}</td>
					<td>${item.numeroRegistros}</td>
					<td>
						<fmt:formatDate type="both" value="${item.creacion}"/>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="tabPanel">
		<ul class="tabs">
			<li><a href="javascript:show('Html');" id="tabHtml"><fmt:message key="envios.datos.html"/></a></li>
			<li><a href="javascript:show('Texto');" id="tabTexto"><fmt:message key="envios.datos.texto"/></a></li>
		</ul>
		<div id="panelHtml" class="tabPanelContent">
			${envio.html}
		</div>
		<div id="panelTexto" class="tabPanelContent" style="display:none">
		<pre>${envio.texto}</pre>
		</div>
	</div>

<script type="text/javascript">
	function show(name) {
		var oculto = name == "Texto"? "Html" : "Texto";
		document.getElementById("panel" + name).style.display = "block";
		document.getElementById("panel" + oculto).style.display = "none";
		document.getElementById("tab" + name).className = "selected";
		document.getElementById("tab" + oculto).className = "";
	}
	show("Html");
</script>
