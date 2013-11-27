<%@ page contentType="text/html;charset=iso-8859-1" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@page import="com.espmail.utils.TextUtils"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<c:set var="paginaKey"><tiles:getAsString name="pagina" ignore="true"/></c:set>

		<%
			String path = (String) request.getAttribute("path");
			String[] paths = path.substring(1, path.indexOf(".")).split("/");
			String menu = null;
			String pagina = null;
			String subpagina = null;
			String paginaKey = (String) pageContext.getAttribute("paginaKey");
			
			if (paths.length == 3) {
				menu = paths[1];
				pagina = paths[2];
			}

			if (!TextUtils.isEmpty(paginaKey)) {
				if (paginaKey.equals("exito") || paginaKey.equals("error")) {
					subpagina = paginaKey;
				} else {
					pagina = paginaKey;
				}
			}

			pageContext.setAttribute("menu", menu);
			pageContext.setAttribute("pagina", pagina);
			pageContext.setAttribute("subpagina", subpagina);
		%>

		<title><% if (subpagina != null) { %><fmt:message>${subpagina}.titulo</fmt:message> <% } if (request.getParameter("code") == null) { %><fmt:message>${menu}.${pagina}.titulo</fmt:message> <% } %> :: asp Mail</title>
		<c:url var="urlEstilo" value="/resources/style.css"/>
		<link type="text/css" rel="stylesheet" href="${urlEstilo}"></link>
      <c:url var="favicon" value="/resources/favicon.ico"/>
      <link rel="shortcut icon" href="${favicon}" />
      <c:url var="utilsUrl" value="/resources/utils.js"/>
		<script src="${utilsUrl}" type="text/javascript"></script>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	</head>
	<body>
		<div id="wrap">
			<div id="header">
				<div class="clearfix">
					<h1><a href="<c:url value="/"/>">asp Mail</a></h1>
					<% if (request.isUserInRole("cliente")) { %>
						<div id="user_field">
							<p><fmt:message key="plantilla.user"/>: <a class="usuario" href="<c:url value="/app/personal/cambiopassword.do"/>"><%= request.getRemoteUser() %></a> | <a href="<c:url value="/app/logout.do"/>"><fmt:message key="plantilla.cerrar"/></a></p>
						</div>
					<% } %>
				</div>
				<% if (request.isUserInRole("cliente")) { %>
				<div id="nav">
					<ul class="clearfix">
						<li class="uno">
							<a href="<c:url value="/app/envios/inicio.do"/>" <%= "envios".equals(menu)? "class=\"active\"" : "" %>><fmt:message key="menu.envios"/></a>
						</li>
						<li class="dos">
							<a href="<c:url value="/app/listas/listado.do"/>" <%= "listas".equals(menu)? "class=\"active\"" : "" %>><fmt:message key="menu.listas"/></a>
						</li>
						<li class="tres">
							<a href="<c:url value="/app/estadisticas/dia.do"/>" <%= "estadisticas".equals(menu)? "class=\"active\"" : "" %>><fmt:message key="menu.estadisticas"/></a>
						</li>
					</ul>
				</div>
				<% } %>
			</div>
			<div id="content" class="clearfix">
				<div id="sidebar">
					<% if (request.isUserInRole("cliente")) { %>
					<div id="menu">
						<ul>
							<% if ("envios".equals(menu)) { %>
							<li><a href="<c:url value="/app/envios/inicio.do"/>"><fmt:message key="menu.envios.inicio"/></a></li>
							<li><a href="<c:url value="/app/envios/nuevo.do"/>"><fmt:message key="menu.envios.nuevo"/></a></li>
							<li><a href="<c:url value="/app/envios/listado.do"/>"><fmt:message key="menu.envios.listado"/></a></li>
							<% } else if ("listas".equals(menu)) { %>
							<li><a href="<c:url value="/app/listas/nueva.do"/>"><fmt:message key="menu.listas.nuevo"/></a></li>
							<li><a href="<c:url value="/app/listas/listado.do"/>"><fmt:message key="menu.listas.listado"/></a></li>
							<li><a href="<c:url value="/app/listas/busqueda.do"/>"><fmt:message key="menu.listas.busqueda"/></a></li>
							<% } else if ("estadisticas".equals(menu)) { %>
							<li><a href="<c:url value="/app/estadisticas/dia.do"/>"><fmt:message key="menu.estadisticas.dia"/></a></li>
							<li><a href="<c:url value="/app/estadisticas/clicks.do"/>"><fmt:message key="menu.estadisticas.click"/></a></li>
							<% } else if ("personal".equals(menu)) { %>
							<li><a href="<c:url value="/app/personal/cambiopassword.do"/>"><fmt:message key="menu.personal.cambioPassword"/></a></li>
							<% } %>
						</ul>
					</div>
					<% } %>
				</div>
				<div id="main_content">
					<div id="encab" class="clearfix">
						<% if (request.getParameter("code") != null) { %>
						<h2><fmt:message>${subpagina}.titulo</fmt:message></h2>
						<% } else { %>
						<h2><fmt:message>menu.${menu}</fmt:message></h2>
						<h3>
						<% if (subpagina != null) { %><span><% } %>
						| <fmt:message>${menu}.${pagina}.titulo</fmt:message>
						<% if (subpagina != null) { %></span> | <fmt:message>${subpagina}.titulo</fmt:message> <% } }%>
						</h3>
					</div>
					<tiles:insert attribute="cuerpo">
						<c:set var="contenidoTab"><tiles:getAsString name="contenidoTab" ignore="true"/></c:set>
						<tiles:put name="contenidoTab" value="${contenidoTab}"/>
					</tiles:insert>
				</div>
			</div>
			<div id="footer">
				<p>Copyright © 2008 espmail</p>
			</div>
		</div>
	</body>
</html>