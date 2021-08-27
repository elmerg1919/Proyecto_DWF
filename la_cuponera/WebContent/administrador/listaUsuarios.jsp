<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page session="true"%>
<%
	HttpSession sesion = request.getSession();
	if (sesion.getAttribute("tipo") == null) {
		response.sendRedirect("../LACUPONERA2019/loginAdministrador.jsp");
	} else {
		String tipo = sesion.getAttribute("tipo").toString();
		if (!tipo.equals("Administrador")) {
			response.sendRedirect("../LACUPONERA2019/loginAdministrador.jsp");
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
	<jsp:include page="menuAdministrador.jsp" />
	<div class="row">
		<div class="container">
			<h2><strong>Lista de clientes</strong></h2>
			<p class="help-block"><strong>TOTAL DE CLIENTES: </strong>${requestScope.totalUsuarios}</p>
		</div>
		<div class="row">
			<div class="container">

				<table class="table table-striped table-bordered table-hover"
					id="tabla" style="width: 100%">
					<thead>
						<tr>
							<th>DUI</th>
							<th>Nombres</th>
							<th>Apellidos</th>
							<th>Teléfono</th>
							<th>Estado</th>
							<th>Visualizar</th>
							<th>Eliminar</th>
						</tr>
					</thead>
					<tbody>

						<c:forEach items="${requestScope.listaClientes}" var="clientes">
							<tr>
								<!-- propiedades de la clase de los beans Usuario.java -->
								<td>${clientes.dui}</td>
								<td>${clientes.nombres}</td>
								<td>${clientes.apellidos}</td>
								<td>${clientes.telefono}</td>
								<td>
								<c:if test="${clientes.stringEstado.equals('Activo')}"><p class="label label-success">${clientes.stringEstado}</p></c:if>
								<c:if test="${clientes.stringEstado.equals('Inactivo')}"><p class="label label-danger">${clientes.stringEstado}</p></c:if>
								</td>
								<td><a style="width: 100%" class="btn btn-primary" href="${contextPath}/administrador.do?op=obtener&id=${clientes.dui}"><i
										class="fas fa-search"></i></a></td>
								<td><a style="width: 100%" class="btn btn-danger" href="javascript:eliminar('${clientes.dui}')"><i
										class="fas fa-trash-alt"></i></a></td>

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
		<c:if test="${not empty exito}">
		alertify.success('${exito}');
		<c:set var="exito" value="" scope="session" />
		</c:if>
		<c:if test="${not empty fracaso}">
		alertify.error('${fracaso}');
		<c:set var="fracaso" value="" scope="session" />
		</c:if>
		function eliminar(id) {
			alertify.confirm("¿Realmente desea eliminar este usuario?", function(
					e) {
				if (e) {
					location.href = "administrador.do?op=eliminaru&id=" + id;
				}
			});
		}
	</script>
</body>
</html>