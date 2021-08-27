<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
					<strong>Datos de la empresa</strong>
				</h2>
			</div>
			<br>
			<div class="row">
				<p>
					<strong>Nombre: </strong>${empresa.nombre}
				</p>
				<p>
					<strong>Código: </strong>${empresa.codigo}</p>
				<p>
					<strong>Número de teléfono: </strong>${empresa.telefono}</p>
				<p>
					<strong>Correo electrónico: </strong>${empresa.correo}</p>
				<p>
					<strong>Contacto: </strong>${empresa.contacto}</p>
				<p>
					<strong>Dirección: </strong>${empresa.direccion}</p>
				<p>
					<strong>Rubro: </strong>${empresa.stringCodigoRubro}</p>
				<p>
					<strong>Comisión: </strong>${empresa.comision}%</p>

			</div>
			<div class="row">

				<a class="btn btn-primary col-md-2"
					href="${contextPath}/administrador.do?op=listare"><i
					class="fas fa-reply"></i></a>


			</div>
		</div>
		<div class="container col-md-8">
			<div class="row">
				<h2>
					<strong>Ofertas de ${empresa.nombre}</strong>
				</h2>

			</div>
			<div class="row">
				<table class="table table-striped table-bordered table-hover"
					id="tabla" style="width: 100%">
					<thead>
						<tr>
							<th>Título de la oferta</th>
							<th>Descripcion</th>
							<th>Cupones vendidos</th>
							<th>Precio</th>
							<th>Estado</th>
							<th>Visualizar</th>
						</tr>
					</thead>
					<tbody>

						<c:forEach items="${requestScope.listaOfertas}" var="oferta">
							<tr>
								<!-- propiedades de la clase de los beans Oferta.java -->
								<td>${oferta.titulo}</td>
								<td>${oferta.descripcion}</td>
								<td>${oferta.cuponesVendidos}</td>
								<fmt:setLocale value="en_US" />
								<td><fmt:formatNumber value="${oferta.precio}"
										type="currency" /></td>
								<c:if test="${oferta.estado.equals('Pendiente')}">
									<td><p class="label label-warning">${oferta.estado}</p></td>
								</c:if>
								<c:if test="${oferta.estado.equals('Vencida')}">
									<td><p class="label label-default">${oferta.estado}</p></td>
								</c:if>
								<c:if test="${oferta.estado.equals('Aprobada')}">
									<td><p class="label label-success">${oferta.estado}</p></td>
								</c:if>
								<c:if test="${oferta.estado.equals('Descartada')}">
									<td><p class="label label-danger">${oferta.estado}</p></td>
								</c:if>
								<td><a style="width: 100%" class="btn btn-primary"
									href="${contextPath}/administrador.do?op=obtenero&id=${oferta.codigo}"><i
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