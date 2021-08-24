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
				<form action="${contextPath}/administrador.do" method="POST">
					<input type="hidden" name="op" value="validarl">
					<div class="form-group col-lg-12">
						<h2>
							<strong><i class="fas fa-user-tie"></i> Iniciar sesión</strong>
						</h2>
					</div>

					<div class="form-group col-lg-12">
						<label for="correoId">Correo electrónico:</label>
						<div class="input-group">
							<div class="input-group-addon">
								<i class="fas fa-at"></i>
							</div>
							<input type="text" class="form-control" id="correoId"
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
						<small class="help-block">¿Olvidaste tu contraseña? <a href="passAdministrador.jsp">Ingresa aquí</a></small>
					</div>

					<div class="form-group col-lg-12">
						<div class="input-group">
							<button type="submit" class="btn btn-success" name="Guardar">
								<i class="fas fa-sign-in-alt"></i> Ingresar
							</button>
						</div>
					</div>
				</form>

				<%
					HttpSession sesion = request.getSession();
					String tipo = "";
					if (request.getAttribute("tipoUsuario") != null) {
						tipo = (String) request.getAttribute("tipoUsuario");
						if (tipo.equals("Administrador")) {
							session.setAttribute("nombreUsuario", request.getAttribute("nombreUsuario"));
							session.setAttribute("duiUsuario", request.getAttribute("duiUsuario"));
							session.setAttribute("passwordActual", request.getAttribute("passwordUser"));
							session.setAttribute("tipo", tipo);
							response.sendRedirect("administrador/indexAdministrador.jsp");
						}
					}
					if (request.getParameter("logout") != null) {
						session.invalidate();
					}
				%>

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