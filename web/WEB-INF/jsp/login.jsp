<%@ page contentType="text/html;charset=iso-8859-1" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://utils.espmail.com" prefix="ut"%>


<c:url var="action" value="/j_security_check" />
<form method="post" action="${action}">
    
	<c:if test="${param.error == 'true'}">
		<ul class="errors"><li><fmt:message key="login.error"/></li></ul>
	</c:if>
	<div class="sub_cont">
		<ut:field key="login.user">
			<input type="text"  name= "j_username"></input>
		</ut:field>
		<ut:field key="login.password">
			<input type="password"  name= "j_password"></input>
		</ut:field>
	</div>
	<div class="sub_cont_end"></div>

	<div class="to-center">
		<fmt:message var="entrar" key="login.entrar"/>
		<input type="submit" value="${entrar}" class="button"></input>
	</div>
</form>		

