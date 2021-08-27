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
<%@ include file='../cabecera.jsp'%>
</head>
<body>
	<jsp:include page="menuAdministrador.jsp" />
	<div class="container">

		<div class="container col-md-4">
			<div class="row">
				<h2>
					<strong>Datos del cliente</strong>
				</h2>
			</div>
			<br>
			<div class="row">
				<p>
					<strong>Nombre completo: </strong>${usuario.nombres}
					${usuario.apellidos}
				</p>
				<p>
					<strong>DUI: </strong>${usuario.dui}</p>
				<p>
					<strong>Número de teléfono: </strong>${usuario.telefono}</p>
				<p>
					<strong>Correo electrónico: </strong>${usuario.correo}</p>
				<p>
					<strong>Dirección: </strong>${usuario.direccion}</p>

				<strong>
				<c:if test="${usuario.stringEstado.equals('Activo')}">
				<p>Estado: <span class="label label-success">${usuario.stringEstado}</span></p>
					</c:if> <c:if test="${usuario.stringEstado.equals('Inactivo')}">
				<p>Estado: <span class="label label-danger">${usuario.stringEstado}</span></p>
					</c:if>
				</strong>


			</div>
			<div class="row">
				<p>
					<a class="btn btn-primary col-md-2"
						href="${contextPath}/administrador.do?op=listaru"><i
						class="fas fa-reply"></i></a></p>

			</div>

		</div>
		<div class="container col-md-8">
			<div class="row">
				<h2>
					<strong>Cupones de ${usuario.nombres}</strong>
				</h2>
			</div>
			<div class="row">
				<table class="table table-striped table-bordered table-hover"
					id="tabla" style="width: 100%">
					<thead>
						<tr>
							<th>Código</th>
							<th>Oferta</th>
							<th>Estado</th>
						</tr>
					</thead>
					<tbody>

						<c:forEach items="${requestScope.listaCupones}" var="cupon">
							<tr>
								<!-- propiedades de la clase de los beans Cupon.java -->
								<td>${cupon.codigo}</td>
								<td>${cupon.tituloOferta}</td>
								<c:if test="${cupon.estado.equals('Canjeado')}">
									<td><p class="label label-primary">${cupon.estado}</p></td>
								</c:if>
								<c:if test="${cupon.estado.equals('Disponible')}">
									<td><p class="label label-success">${cupon.estado}</p></td>
								</c:if>
								<c:if test="${cupon.estado.equals('Vencido')}">
									<td><p class="label label-default">${cupon.estado}</p></td>
								</c:if>
								
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
			alertify.confirm("¿Realmente desea eliminar este usuario?",
					function(e) {
						if (e) {
							location.href = "administrador.do?op=listarc";
						}
					});
		}
	</script>
</body>
</html>