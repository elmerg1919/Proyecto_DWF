<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
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
				<form action="${contextPath}/empresa.do?op=recuperar" method="POST">
					<div class="form-group col-lg-12">
						<h2>
							<strong><i class="fas fa-users"></i> Recuperar
								contraseña</strong>
						</h2>
					</div>
					
					<div class="form-group col-lg-12">
						<div class="input-group alert alert-info">
							<p>
							<strong><i class="fas fa-info-circle"></i> Importante: </strong>Ingresa
							el correo electrónico con el que fuiste registrado. Te enviaremos un correo
							con la información de recuperación
						</p>
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
					<br>
						<div class="input-group">
							<button style="margin-right: 10px" type="submit"
								class="btn btn-success" name="Guardar">
								<i class="fas fa-share"></i> Enviar 
							</button>
							<a href="loginCliente.jsp"> Volver</a>
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
		function eliminar(id) {
			alertify.confirm("¿Realmente desea eliminar este rubro?", function(
					e) {
				if (e) {
					location.href = "administrador.do?op=eliminarr&id=" + id;
				}
			});
		}
	</script>
</body>
</html>