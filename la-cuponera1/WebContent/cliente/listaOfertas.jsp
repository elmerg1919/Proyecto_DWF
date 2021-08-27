<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value = "en_US"/>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page session="true"%>
<%
	HttpSession sesion = request.getSession();
	if (sesion.getAttribute("estadoCliente") == null) {
		response.sendRedirect("../LACUPONERA2019/loginCliente.jsp");
	} else {
		int estado = (Integer)sesion.getAttribute("estadoCliente");
		if (estado != 1) {
			response.sendRedirect("../LACUPONERA2019/loginCliente.jsp");
		}
	}
%>
<!DOCTYPE html>
<html>
<head>
<title>La Cuponera - UDB</title>
<%@ include file='/cabecera.jsp'%>
</head>
<body>
	<jsp:include page="menuCliente.jsp" />
	
	
	
	<div class="row">
		<div class="container">
			<h2>
				<strong>Lista de ofertas</strong>
			</h2>
			<p class="help-block">
				<strong>TOTAL DE OFERTAS: </strong>${requestScope.totalOfertas}</p>
		</div>
		<div class="row">
			<div class="container">

				<table class="table table-striped table-bordered table-hover"
					id="tabla2" style="width: 100%">
					<thead>
						<tr>
							<th>Empresa</th>
							<th>Título</th>
							<th>Precio</th>
							<th>Estado</th>
							<th>Visualizar</th>
						</tr>
					</thead>
					<tbody>

						<c:forEach items="${requestScope.listaOfertas}"
							var="ofertas">
							<tr>
								<!-- propiedades de la clase de los beans Usuario.java -->
								<td>${ofertas.stringCodigoEmpresa}</td>
								<td>${ofertas.titulo}</td>
								<td><fmt:formatNumber value = "${ofertas.precio}" type = "currency"/></td>
								<td><p class="label label-success">Disponible</p></td>
								<td><a style="width: 100%" class="btn btn-primary"
									href="${contextPath}/cliente.do?op=obtenero&id=${ofertas.codigo}"><i
										class="fas fa-search"></i></a></td>

							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>

		</div>
	</div>
	
	
	<script>
	$(document).ready(function() {
		$('#tabla').DataTable();
	});
	$(document).ready(function() {
		$('#tabla2').DataTable();
	});
		<c:if test="${not empty exito}">
		alertify.success('${exito}');
		<c:set var="exito" value="" scope="session" />
		</c:if>
		<c:if test="${not empty fracaso}">
		alertify.error('${fracaso}');
		<c:set var="fracaso" value="" scope="session" />
		</c:if>
		function eliminar(id) {
			alertify.confirm("¿Realmente desea eliminar este usuario?",
					function(e) {
						if (e) {
							location.href = "administrador.do?op=eliminaru&id="
									+ id;
						}
					});
		}
	</script>
</body>
</html>