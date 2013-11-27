<%@ page contentType="text/html;charset=iso-8859-1" language="java"%>

<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://utils.espmail.com" prefix="ut"%>

<div class="tabPanel">
	<ul class="tabs">
		<ut:tabItem key="envios.tabs.datos" url="/app/envios/datos.do?idEnvio=${param.idEnvio}"/>
		<ut:tabItem key="envios.tabs.preview" url="/app/envios/preview.do?idEnvio=${param.idEnvio}"/>
		<ut:tabItem key="envios.tabs.asociarListas" url="/app/envios/asociarListas.do?idEnvio=${param.idEnvio}"/>
		<ut:tabItem key="envios.tabs.test" url="/app/envios/test.do?idEnvio=${param.idEnvio}"/>
		<ut:tabItem key="envios.tabs.lanzar" url="/app/envios/lanzar.do?idEnvio=${param.idEnvio}"/>
	</ul>
	<div class="tabPanelContent">
		<tiles:insert attribute="contenidoTab"/>
	</div>
</div>
