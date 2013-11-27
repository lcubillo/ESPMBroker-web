<%@ page contentType="text/html;charset=iso-8859-1" language="java"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic-el" prefix="logic"%>
<%@ taglib uri="http://utils.espmail.com" prefix="ut"%>



<div class="tabPanel">
	<ul class="tabs">
			<li><a href="javascript:show('Html');" id="tabHtml"><fmt:message key="envios.datos.html"/></a></li>
		<li><a href="javascript:show('Texto');" id="tabTexto"><fmt:message key="envios.datos.texto"/></a></li>
	</ul>
	<div class="tabPanelContent correo">
		<dl>
			<dt><fmt:message key="envios.preview.remitente" /></dt>
			<dd>${envio.remitente}</dd>
		</dl>
		<dl>
			<dt><fmt:message key="envios.preview.asunto" /></dt>
			<dd>${envio.asunto}</dd>
		</dl>
		<dl>
			<dt><fmt:message key="envios.preview.replyTo" /></dt>
			<dd>${envio.replyTo}</dd>
		</dl>
		<div>
			<div id="panelHtml">
				${envio.html}
			</div>
			<div id="panelTexto" style="display:none">
				<pre>${envio.texto}</pre>
			</div>
		</div>
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
