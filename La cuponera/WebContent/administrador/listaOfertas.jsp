<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value = "en_US"/>
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
	<div class="row alert alert-info">
		<div class="container">
			<h2>
				<strong>Ofertas pendientes</strong>
			</h2>
			<p class="help-block">
				<strong>TOTAL DE OFERTAS PENDIENTES: </strong>${requestScope.totalOfertasPendientes}</p>
		</div>
		<div class="row">
			<div class="container">

				<table class="table table-striped table-bordered table-hover"
					id="tabla" style="width: 100%">
					<thead>
						<tr>
							<th>Empresa</th>
							<th>Título</th>
							<th>Precio</th>
							<th>Fecha inicio</th>
							<th>Fecha final</th>
							<th>Estado</th>
							<th>Administrar</th>
						</tr>
					</thead>
					<tbody>

						<c:forEach items="${requestScope.listaOfertasPendientes}"
							var="ofertasPendientes">
							<tr>
								<!-- propiedades de la clase de los beans Usuario.java -->
								<td>${ofertasPendientes.stringCodigoEmpresa}</td>
								<td>${ofertasPendientes.titulo}</td>
								<td><fmt:formatNumber value = "${ofertasPendientes.precio}" type = "currency"/></td>
								<td>${ofertasPendientes.fechaInicio}</td>
								<td>${ofertasPendientes.fechaFinal}</td>
								<td><p class="label label-warning">${ofertasPendientes.estado}</p></td>
								<td><a style="width: 100%" class="btn btn-default"
									href="${contextPath}/administrador.do?op=obtenero&id=${ofertasPendientes.codigo}"><i
										class="fas fa-cog"></i></a></td>

							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>

		</div>
	</div>
	
	
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
							<th>Cupones vendidos</th>
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
								<td>${ofertas.cuponesVendidos}</td>
								<c:if test="${ofertas.estado.equals('Pendiente')}">
									<td><p class="label label-warning">${ofertas.estado}</p></td>
								</c:if>
								<c:if test="${ofertas.estado.equals('Vencida')}">
									<td><p class="label label-default">${ofertas.estado}</p></td>
								</c:if>
								<c:if test="${ofertas.estado.equals('Aprobada')}">
									<td><p class="label label-success">${ofertas.estado}</p></td>
								</c:if>
								<c:if test="${ofertas.estado.equals('Descartada')}">
									<td><p class="label label-danger">${ofertas.estado}</p></td>
								</c:if>
								<td><a style="width: 100%" class="btn btn-primary"
									href="${contextPath}/administrador.do?op=obtenero&id=${ofertas.codigo}"><i
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