<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>La Cuponera - UDB</title>
<%@ include file='cabecera.jsp'%>
</head>
<body>
	<%@ include file='menuPrincipal.jsp'%>

	<div class="container">

		<div class="row">

			<article class="col-xs-1 col-sm-2 col-md-3"></article>

			<article class="col-xs-10 col-sm-8 col-md-6">
				<c:if test="${not empty listaErrores}">
					<div class="alert alert-danger">
						<ul>
							<c:forEach var="errores" items="${requestScope.listaErrores}">
								<li>${errores}</li>
							</c:forEach>
						</ul>
					</div>
				</c:if>
				<form action="${contextPath}/cliente.do" method="POST">
					<input type="hidden" name="op" value="registrarse">
					<div class="form-group col-lg-12">
						<h2>
							<strong>Registrarse</strong>
						</h2>
					</div>



					<div class="form-group col-md-6  col-lg-6">
						<label for="nombresId">Nombres:</label>
						<div class="input-group">
							<div class="input-group-addon">
								<i class="fas fa-file-signature"></i>
							</div>
							<input type="text" class="form-control" id="nombresId"
								name="nombres">
						</div>
					</div>

					<div class="form-group col-md-6  col-lg-6">
						<label for="apellidosId">Apellidos:</label>
						<div class="input-group">
							<div class="input-group-addon">
								<i class="fas fa-file-signature"></i>
							</div>
							<input type="text" class="form-control" id="apellidosId"
								name="apellidos">
						</div>
					</div>

					<div class="form-group col-lg-12">
						<label for="direccionId">Dirección:</label>
						<div class="input-group">
							<div class="input-group-addon">
								<i class="fas fa-map-marker-alt"></i>
							</div>
							<input type="text" class="form-control" id="direccionId"
								name="direccion">
						</div>
					</div>


					<div class="form-group col-md-6  col-lg-6">
						<label for="duiId">DUI:</label>
						<div class="input-group">
							<div class="input-group-addon">
								<i class="fas fa-id-card"></i>
							</div>
							<input type="text" class="form-control" id="duiId" name="dui">
						</div>
					</div>

					<div class="form-group col-md-6  col-lg-6">
						<label for="telefonoId">Teléfono:</label>
						<div class="input-group">
							<div class="input-group-addon">
								<i class="fas fa-phone"></i>
							</div>
							<input type="number" class="form-control" id="telefonoId"
								name="telefono" min="0" step="1">
						</div>
					</div>

					<div class="form-group col-lg-12">
						<label for="correoId">Correo electrónico:</label>
						<div class="input-group">
							<div class="input-group-addon">
								<i class="fas fa-at"></i>
							</div>
							<input type="email" class="form-control" id="correoId"
								name="correo">
						</div>
					</div>

					<div class="form-group col-lg-12">
						<label for="contrasenaId">Contraseña:</label>
						<div class="input-group">
							<div class="input-group-addon">
								<i class="fas fa-key"></i>
							</div>
							<input type="password" class="form-control" id="contrasenaId"
								name="password">
						</div>
					</div>

					<div class="form-group col-lg-12">
					<br/>
						<div class="input-group alert alert-info">
							
							<p class="">
								<strong><i class="fas fa-info-circle"></i> Importante:
								</strong>Luego de registrarte, revisa tu bandeja de entrada del correo
								ingresado para activar tu cuenta e iniciar sesión sin problema
							</p>
						</div>
					</div>



					<div class="form-group col-lg-12">
						<div class="input-group">
							<button style="margin-right: 10px" type="submit"
								class="btn btn-success" name="Guardar">
								<i class="fas fa-plus"></i> Registrarse
							</button>
							¿Ya tienes una cuenta? <a href="loginCliente.jsp">Inicia
								sesión aquí</a>
						</div>
					</div>
				</form>


			</article>
			<article class="col-xs-1 col-sm-2 col-md-3"></article>

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
	</script>
</body>
</html>