<%@ page contentType="text/html;charset=iso-8859-1" language="java"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://utils.espmail.com" prefix="ut"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<script language="JavaScript" type="text/JavaScript">
<!--
var seleccion = [0, 0, 0, 0, 0, 0, 0];

function show(nombre, mostrar) {
	var el = document.getElementById(nombre);
	el.style.display = mostrar? '' : 'none';
	el.disabled = !mostrar;
}

function cambia(componente) {
	var index = componente.selectedIndex - 1;
	var id = componente.id;

	if (index != -1 && index != 7 && seleccion[index] != 0) {
		alert("<fmt:message key="listas.mapeo.error.yaSeleccionado"/>");
		componente.selectedIndex = 0;
		index = 0;
	}
	
	for (var i = 0; i < 7; i++) {
		var value = seleccion[i];

		if (value == id) {
			seleccion[i] = 0;
			
			if (i > 3) {
				show("T" + (i - 3) + id, false);
			}
		}
	}

	if (index > -1 && index != 7) {
		seleccion[index] = id;
		
		if (index > 3) {
			show("T" + (index - 3) + id, true);
		}
	}
}

function valida() {
	if (seleccion[0] == -1) {
		alert("<fmt:message key="listas.mapeo.error.faltaEmail"/>");
		return false;
	}

	for (var i = 1; i <= ${fn:length(listas_datosForm.datos[0].valores)}; i++) {
		var el = document.getElementById(i);

		if (el.selectedIndex == 0) {
			alert("<fmt:message key="listas.mapeo.error.seleccion"/>");
			el.focus();
			return false;
		}
	}

	for (var i = 4; i < 7; i++) {
		var value = seleccion[i];

		if (value != -1) {
			var el = document.getElementById("T" + (i - 3)+""+ value);

			if (el.value == "") {
				alert("<fmt:message key="listas.mapeo.error.darNombre"/>");
				el.focus();
				return false;
			}
		}
	}
	
	return true;
}

//-->
</script>
<html:errors />
<html:form action="/app/listas/mapeo.do" method="post" enctype="multipart/form-data">
    <html:hidden property="nombreLista" />
    <html:hidden property="idLista" />
    <html:hidden property="nombreArchivo" />
    <html:hidden property="paso" value="2" />

	<div class="scrollable">
	    <table class="listado">
	    	<thead>
		    	<tr>
					<c:forEach var="contador" begin="1" end="${fn:length(listas_datosForm.datos[0].valores)}">
			            <th <c:if test="${contador == 1}">class="first-child"</c:if><c:if test="${contador == fn:length(listas_datosForm.datos[0].valores)}">class="last-child"</c:if>>
					        <html:select property="columnas" styleId="${contador}" onchange="javascript:cambia(this);">
					        	<html:option value=""><fmt:message key="combo.seleccionar"/></html:option>
					        	<c:forEach var="item" begin="0" end="6">
					        		<html:option value="${item}"><fmt:message key="listas.mapeo.campos.${item}"/></html:option>
					        	</c:forEach>
					        	<html:option value="-1"><fmt:message key="listas.mapeo.campos.ignorar"/></html:option>
					        </html:select><br>
					        <input type="text" name="t1" id="T1${contador}" style="display:none" disabled="disabled" maxlength="12" onkeyup="this.value = this.value.toUpperCase()"/>
					        <input type="text" name="t2" id="T2${contador}" style="display:none" disabled="disabled" maxlength="12" onkeyup="this.value = this.value.toUpperCase()" />
					        <input type="text" name="t3" id="T3${contador}" style="display:none" disabled="disabled" maxlength="12" onkeyup="this.value = this.value.toUpperCase()" />
			            </th>
		            </c:forEach>
		        </tr>
		    </thead>
		    <tbody>
		    <c:forEach items="${listas_datosForm.datos}" var="fila">
			    <tr>
			        <c:forEach items="${fila.valores}" var="columna" >
			        	<td><c:out value="${columna}" /></td>
			        </c:forEach>
				</tr>
		    </c:forEach>
		    </tbody>
	    </table>
	</div>
	<div class="form_actions">
		<html:submit property="summit" onclick="return valida();" styleClass="button">
			<fmt:message key="listas.mapeo.submit" />
		</html:submit>
	</div>
</html:form>