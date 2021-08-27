<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setLocale value="en_US" />
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
	HttpSession sesion = request.getSession();
	if (sesion.getAttribute("estadoCliente") == null) {
		response.sendRedirect("../LACUPONERA2019/loginCliente.jsp");
	} else {
		int estado = (Integer) sesion.getAttribute("estadoCliente");
		if (estado != 1) {
			response.sendRedirect("../LACUPONERA2019/loginCliente.jsp");
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
	<jsp:include page="menuCliente.jsp" />
	<div class="container">
		<c:if
			test="${oferta.estado.equals('Aprobada') || oferta.estado.equals('Vencida')}">
			<div class="container col-lg-6">
				<div class="row">
					<h2>
						<strong>Datos de la oferta</strong>
					</h2>
				</div>
				<br>
				<div class="row">
					<p>
						<strong>Empresa: </strong>${oferta.stringCodigoEmpresa}
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
						<strong>Cupones disponibles: </strong>${oferta.cuponesDisponibles}</p>



				</div>

				<div class="row">

					<c:if test="${oferta.estado.equals('Aprobada')}">
						<a class="btn btn-primary col-md-2"
							href="${contextPath}/cliente.do?op=listaro"><i
							class="fas fa-reply"></i></a>
					</c:if>
				</div>



			</div>
		</c:if>

		<c:if
			test="${oferta.estado.equals('Descartada') || oferta.estado.equals('Pendiente')}">

			<div class="row">

				<div style="float: none; margin: 0 auto;"
					class="col-md-6 col-centered text-center">
					<h1>
						<strong>ERROR 404</strong> <br>
					</h1>
					<hr>
					<h3>
						<strong>La página solicitada no se ha encontrado</strong>
					</h3>
					<h4>Seleccione una opcion del menú superior</h4>

				</div>
			</div>

		</c:if>

		<c:if test="${oferta.estado.equals('Aprobada')}">



			<article class="col-lg-6">
				<c:if test="${not empty listaErrores}">
					<div class="alert alert-danger">
						<ul>
							<c:forEach var="errores" items="${requestScope.listaErrores}">
								<li>${errores}</li>
							</c:forEach>
						</ul>
					</div>
				</c:if>
				<form
					action="${contextPath}/cliente.do?op=insertarc&dui=<%=sesion.getAttribute("duiCliente")%>&of=${oferta.codigo}&emp=${oferta.codigoEmpresa}&fe=${oferta.fechaFinalCupon}&email=<%=sesion.getAttribute("correoCliente")%>"
					method="POST">

					<div class="form-group col-lg-12">
						<h2>
							<strong>Comprar en línea </strong>
						</h2>
					</div>


					<div class="form-group col-lg-12">
						<label for="tarjetaId">Número de tarjeta:</label>
						<div class="input-group">
							<div class="input-group-addon">
								<i class="fas fa-hashtag"></i>
							</div>
							<input type="number" min="0" step="1" class="form-control"
								id="tarjeta" name="tarjeta">
						</div>
					</div>

					<div class="form-group col-lg-12">
						<label for="titularId">Titular:</label>
						<div class="input-group">
							<div class="input-group-addon">
								<i class="fas fa-user"></i>
							</div>
							<input type="text" class="form-control" id="titularId"
								name="titular">
						</div>
					</div>

					<div class="form-group col-md-4 col-lg-4">
						<label for="venceId">Mes:</label>
						<div class="input-group">
							<div class="input-group-addon">
								<strong>MM</strong>
							</div>
							<input type="number" class="form-control" id="venceId" name="mes"
								min="1" max="12" step="1">

						</div>

					</div>

					<div class="form-group col-md-4  col-lg-4">
						<label for="yearId">Año:</label>
						<div class="input-group">
							<div class="input-group-addon">
								<strong>YY</strong>
							</div>

							<input type="number" class="form-control" id="yearId" name="year"
								min="1" step="1">
						</div>

					</div>


					<div class="form-group col-md-4  col-lg-4">
						<label for="seguridadId">CVV:</label>
						<div class="input-group">
							<div class="input-group-addon">
								<i class="fas fa-lock"></i>
							</div>
							<input type="number" class="form-control" id="seguridadId"
								name="cvv" min="1" step="1">
						</div>
					</div>


					<div class="form-group col-md-6  col-lg-6">
						<label for="totalId">Total a pagar:</label>
						<div class="input-group">
							<div class="input-group-addon">
								<i class="fas fa-dollar-sign"></i>
							</div>
							<input type="text" readonly
								value="<fmt:formatNumber value="${oferta.precio}" type="currency" />"
								class="form-control" id="totalId">
						</div>
					</div>


					<div class="form-group col-md-12  col-lg-12">
						<div class="input-group">
							<button type="submit" class=" btn btn-success" name="Guardar">
								<i class="fas fa-shopping-cart"></i> Comprar
							</button>
						</div>
					</div>
				</form>


			</article>
		</c:if>
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