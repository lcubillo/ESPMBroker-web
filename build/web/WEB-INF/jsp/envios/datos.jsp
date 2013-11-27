<%@ page contentType="text/html;charset=iso-8859-1" language="java"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic-el" prefix="logic"%>
<%@ taglib uri="http://utils.espmail.com" prefix="ut"%>

<script language="JavaScript" type="text/JavaScript">
	function selectTag(){
	var tex = document.forms[0].lista.value;
	if (document.getElementById("panelHtml").style.display == "block"){
		instag(tex,'html');
	}
	else{
		instag(tex,'texto');
	}
	
	return;
	}
	function instag(tag,panel){
	
		if (panel == 'html')
			var input = document.forms[0].html;
		else
			var input = document.forms[0].texto;
		//para mac
		if(typeof document.selection != 'undefined' && document.selection) {
			var str = document.selection.createRange().text;
			input.focus();
			var sel = document.selection.createRange();
			sel.text = "{" + tag + "}";
			return;
		}else if(typeof input.selectionStart != 'undefined'){
			var start = input.selectionStart;
			var end = input.selectionEnd;
			var insText = input.value.substring(start, end);
			input.value = input.value.substr(0, start) + '{'+tag+'}' + insText + input.value.substr(end);
			return;
		}else{
	  		input.value+=' {'+tag+'}';
  			return;
		}
	}
</script>
<html:form action="${path}">
	<html:errors/>
	
	<html:hidden property="idEnvio"/>

		<ut:field key="envios.datos.etiqueta" required="true">
			<html:text property="etiqueta" maxlength="20" size="50"/>
		</ut:field> 
		<ut:field key="envios.datos.asunto" required="true">
			<html:text property="asunto" maxlength="60" size="50"/>
		</ut:field> 			
		<ut:field key="envios.datos.remitente" required="true">
			<html:text property="remitente" maxlength="60" size="50"/>
		</ut:field> 						
		<ut:field key="envios.datos.replyTo" required="true">
			<html:text property="replyTo" maxlength="60" size="50"/>
		</ut:field> 						
		<ut:field key="envios.datos.descripcion">
			<html:text property="descripcion" maxlength="80" size="50"/>
		</ut:field>
	
			
	<ut:field key="envios.datos.personalizar">

 <c:if test="${empty listas}">
         	       
         
          <select name="lista">
         	<option value="NOMBRE">NOMBRE</option>
         	<option value="APELLIDOS">APELLIDOS</option>
         	<option value="TRATAMIENTO">TRATAMIENTO</option> 
			<option value="EMAIL">EMAIL</option>   
         	<option value="" disabled="true">Etiqueta 1</option>
         	<option value="" disabled="true">Etiqueta 2</option>
         	<option value="" disabled="true">Etiqueta 3</option>
          </select>
        
		</c:if>
		
		<c:if test="${!empty listas}">
 		<select name="lista">
         	<option value="NOMBRE">NOMBRE</option>
         	<option value="APELLIDOS">APELLIDOS</option>
         	<option value="TRATAMIENTO">TRATAMIENTO</option> 
			<option value="EMAIL">EMAIL</option>   
         	<c:if test="${!empty listas.etiqueta1}"><option value="${listas.etiqueta1}" >${listas.etiqueta1}</option></c:if>
         	<c:if test="${!empty listas.etiqueta2}"><option value="${listas.etiqueta2}" >${listas.etiqueta2}</option></c:if>
         	<c:if test="${!empty listas.etiqueta3}"><option value="${listas.etiqueta3}" >${listas.etiqueta3}</option></c:if>
          </select>
        
        </c:if>  
			<a href ="#ancla" onclick="selectTag();" class="button">Añadir</a>
          </ut:field>       
		
	<A NAME="ancla">
	
	<div class="tabPanel">
		<ul class="tabs">
			<li><a href="javascript:show('Html');" id="tabHtml"><fmt:message key="envios.datos.html"/></a></li>
			<li><a href="javascript:show('Texto');" id="tabTexto"><fmt:message key="envios.datos.texto"/></a></li>
		</ul>
		<div id="panelHtml" class="tabPanelContent">
			<ut:field key="envios.datos.html" required="true">
				<html:textarea property="html" rows="25" cols="70"/>
			</ut:field> 
		</div>
		<div id="panelTexto" class="tabPanelContent" style="display:none">
			<ut:field key="envios.datos.texto" required="true">
				<html:textarea property="texto" rows="25"  cols="70"/>
			</ut:field> 
		</div>
	</div>
	<div class="form_actions">
		<html:submit property="summit" styleClass="button">
			<fmt:message key="envios.datos.submit" />
		</html:submit>
		<html:reset styleClass="button"><fmt:message key="envios.datos.reset" /></html:reset>
	</div>
</html:form>
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

