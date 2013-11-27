<%@ page contentType="text/html;charset=iso-8859-1" language="java"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://utils.espmail.com" prefix="ut"%>


<c:choose>
	<c:when test="${envios_lanzarForm.error}">
		<div class="error">
			<div>
				<fmt:message key="envios.lanzar.error"/>
			</div>
			<c:url var="url" value="/app/envios/asociarListas.do?idEnvio=${idEnvio}"/>
			<a href="${url}"><fmt:message key="envios.lanzar.error.link"/></a>
		</div>
	</c:when>
	<c:otherwise>
		<html:form action="/app/envios/lanzar.do">
			<html:hidden property="idEnvio"/>
			<ut:field key="envios.lanzar.lanzamiento" required="true">
				<html:select property="dia">
					<c:forEach var="item" items="${ut:dias()}">
						<html:option value="${item}">${item}</html:option>
					</c:forEach>
				</html:select>
				/
				<html:select property="mes">
					<c:forEach var="item" items="${ut:meses()}">
						<html:option value="${item}"><fmt:message key="mes.${item}"/></html:option>
					</c:forEach>
				</html:select>
				/
				<html:select property="anho">
					<html:option value="${ut:anho()}">${ut:anho()}</html:option>
					<html:option value="${ut:anho() + 1}">${ut:anho() + 1}</html:option>
				</html:select>
				&nbsp;&nbsp;
				<html:select property="horas">
					<c:forEach var="item" begin="0" end="24">
						<html:option value="${item}">${item}</html:option>
					</c:forEach>
				</html:select>
				:
				<html:select property="minutos">
					<html:option value="0">00</html:option>
					<html:option value="15">15</html:option>
					<html:option value="30">30</html:option>
					<html:option value="45">45</html:option>
				</html:select>
			</ut:field>
			<div class="form_actions">
				<html:submit property="summit" styleClass="button">
					<fmt:message key="envios.lanzar.submit" />
				</html:submit>
			</div>
		</html:form>
	</c:otherwise>
</c:choose>
