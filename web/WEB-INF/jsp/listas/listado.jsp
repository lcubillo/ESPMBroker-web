<%@ page contentType="text/html;charset=iso-8859-1" language="java"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://utils.espmail.com" prefix="ut"%>

<script type="text/javascript" language="JavaScript">
   function borrar(url){
      if(confirm('¿Esta seguro de querer borrar la lista?'))
         document.location = url;
   }

</script>

<table class="listado">
	<thead>
		<tr>
			<th class="first-child"><fmt:message key="listas.listado.nombre" /></th>
			<th><fmt:message key="listas.listado.registros" /></th>
			<th><fmt:message key="listas.listado.creacion" /></th>
			<th colspan="2"></th>
			<th class="last-child"></th>
		</tr>
	</thead>
	<tbody>
	<c:forEach items="${listas}" var="lista">
		<tr>
			<c:url var="anhadir" value="/app/listas/datos.do" >
				<c:param name="idLista" value="${lista.idLista}"/>
			</c:url>
            <c:url var="borrar" value="/app/listas/listado.do">
            	<c:param name="summit"><fmt:message key="listas.listado.borrar"/></c:param>
                <c:param name="idLista" value="${lista.idLista}" />
            </c:url>
			<td class="first-child strong">${lista.nombre}</td>
			<td class="number"><fmt:formatNumber type="number" value="${lista.numeroRegistros}"/></td>
			<td><fmt:formatDate type="both" dateStyle="short" value="${lista.creacion}"/></td>
			<td><a href="${anhadir}" class="button"><fmt:message key="listas.listado.anhadir"/></a></td>
               <td><a href="javascript:borrar('${borrar}')" class="button"><fmt:message key="listas.listado.borrar"/></a></td>
               <td>
               	<c:url var="bajas" value="/app/informe.csv?operacion=bajasLista" >
					<c:param name="idLista" value="${lista.idLista}"/>
				</c:url>
				<a class="csvlink" href="${bajas}">Descargar Fichero</a>
			</td>
		</tr>
	</c:forEach>
	</tbody>
</table>

