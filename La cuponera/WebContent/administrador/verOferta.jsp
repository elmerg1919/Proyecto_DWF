<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setLocale value="en_US" />
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
		<article class="col-xs-1 col-sm-2 col-md-3"></article>
		<div class="container col-xs-10 col-sm-8 col-md-6">
			<div class="row">
				<h2>
					<strong>Datos de la oferta</strong>
				</h2>
			</div>
			<br>
			<div class="row">
				<p>
					<strong>Empresa: </strong>${oferta.stringCodigoEmpresa}
					(${oferta.codigoEmpresa})
				</p>
				<p>
					<strong>Título: </strong>${oferta.titulo}</p>
				<p>
					<strong>Descripción: </strong>${oferta.descripcion}</p>
				<p>
					<strong>Precio: </strong>
					<fmt:formatNumber value="${oferta.precio}" type="currency" />
				</p>
				<p>
					<strong>Detalles: </strong>${oferta.detalles}</p>

				<c:if test="${oferta.estado.equals('Pendiente')}">
					<p>
						<strong>Estado: </strong><strong class="label label-warning">${oferta.estado}</strong>
					</p>
				</c:if>
				<c:if test="${oferta.estado.equals('Aprobada')}">
					<p>
						<strong>Estado: </strong><strong class="label label-success">${oferta.estado}</strong>
					</p>
				</c:if>
				<c:if test="${oferta.estado.equals('Vencida')}">
					<p>
						<strong>Estado: </strong><strong class="label label-default">${oferta.estado}</strong>
					</p>
				</c:if>
				<c:if test="${oferta.estado.equals('Descartada')}">
					<p>
						<strong>Estado: </strong><strong class="label label-danger">${oferta.estado}</strong>
					</p>
				</c:if>
				<p>
					<strong>Fecha de inicio: </strong>${oferta.fechaInicio}</p>
				<p>
					<strong>Fecha límite de la oferta: </strong>${oferta.fechaFinal}</p>
				<p>
					<strong>Fecha de vencimiento de cupones: </strong>${oferta.fechaFinalCupon}</p>
				<p>
					<strong>Cupones iniciales: </strong>${oferta.cuponesIniciales}</p>
				<p>
					<strong>Cupones disponibles: </strong>${oferta.cuponesDisponibles}</p>
				<p>
					<strong>Cupones vendidos: </strong>${oferta.cuponesVendidos}</p>



			</div>
			<c:if test="${oferta.estado.equals('Aprobada')}">
				<div class="row">


					<ul class="list-group col-md-6">
						<li class="list-group-item"><strong>INGRESOS TOTALES</strong></li>
						<li class="list-group-item "><fmt:formatNumber value="${oferta.ingresosTotales}" type="currency" /></li>
					</ul>

					<ul class="list-group col-md-6">
						<li class="list-group-item"><strong>CARGO POR EL
								SERVICIO</strong></li>
						<li class="list-group-item"><fmt:formatNumber value="${oferta.cargoPorServicio}" type="currency" /></li>
					</ul>


				</div>
			</c:if>
			
			<c:if test="${oferta.estado.equals('Vencida')}">
				<div class="row">


					<ul class="list-group col-md-6">
						<li class="list-group-item"><strong>INGRESOS TOTALES</strong></li>
						<li class="list-group-item "><fmt:formatNumber value="${oferta.ingresosTotales}" type="currency" /></li>
					</ul>

					<ul class="list-group col-md-6">
						<li class="list-group-item"><strong>CARGO POR EL
								SERVICIO</strong></li>
						<li class="list-group-item"><fmt:formatNumber value="${oferta.cargoPorServicio}" type="currency" /></li>
					</ul>


				</div>
			</c:if>




			<div class="row">
				<c:if test="${oferta.estado.equals('Pendiente')}">
					<a class="btn btn-success col-md-2"
						href="${contextPath}/administrador.do?op=aceptar&id=${oferta.codigo}"><i
						class="fas fa-check"></i></a>
					<a class="btn btn-danger col-md-2" style="margin-left: 10px"
						href="${contextPath}/administrador/justificacion.jsp?id=${oferta.codigo}"><i
						class="fas fa-times"></i></a>
						<a class="btn btn-primary col-md-2" style="margin-left: 10px"
						href="${contextPath}/administrador.do?op=listaro"><i
						class="fas fa-reply"></i></a>
				</c:if>
				<c:if test="${oferta.estado.equals('Aprobada')}">
					<a class="btn btn-primary col-md-2"
						href="${contextPath}/administrador.do?op=listaro"><i
						class="fas fa-reply"></i></a>
				</c:if>
				<c:if test="${oferta.estado.equals('Vencida')}">
					<a class="btn btn-primary col-md-2"
						href="${contextPath}/administrador.do?op=listaro"><i
						class="fas fa-reply"></i></a>
				</c:if>
				<c:if test="${oferta.estado.equals('Descartada')}">
				<p class="alert alert-warning">
					<strong>Justificación de descarte: </strong>${oferta.justificacion}</p>
					<a class="btn btn-primary col-md-2"
						href="${contextPath}/administrador.do?op=listaro"><i
						class="fas fa-reply"></i></a>

				</c:if>

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