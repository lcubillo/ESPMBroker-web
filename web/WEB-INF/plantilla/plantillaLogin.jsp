<%@ page contentType="text/html;charset=iso-8859-1" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

	<head>
		<title><fmt:message key="login.titulo"/> :: asp Mail</title>
		<c:url var="urlEstilo" value="/resources/style.css"/>
		<link type="text/css" rel="stylesheet" href="${urlEstilo}"></link>
      <c:url var="favicon" value="/resources/favicon.ico"/>
      <link rel="shortcut icon" href="${favicon}" />
      <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	</head>
	<body class="login">
		<div id="wrap">
			<div id="header">
				<h1><a href="<c:url value="/"/>">asp Mail</a></h1>
			</div>
			<div id="content" class="clearfix">
				<div id="main_content">
					<div id="cont">
						<h2><fmt:message key="login.titulo"/></h2>
						<tiles:insert attribute="cuerpo"/>
					</div>
				</div>
			</div>
			<div id="footer">
				<p>Copyright © 2008 aspmail</p>
			</div>
		</div>
	</body>
</html>