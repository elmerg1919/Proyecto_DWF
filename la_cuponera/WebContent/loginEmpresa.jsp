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
				<form action="${contextPath}/empresa.do" method="POST">
					<input type="hidden" name="op" value="validarl">
					<div class="form-group col-lg-12">
						<h2>
							<strong><i class="fas fa-building"></i> Iniciar sesi�n</strong>
						</h2>
					</div>

					<div class="form-group col-lg-12">
						<label for="correoId">Correo electr�nico:</label>
						<div class="input-group">
							<div class="input-group-addon">
								<i class="fas fa-at"></i>
							</div>
							<input type="text" class="form-control" id="correoId"
								name="correo">
						</div>
					</div>

					<div class="form-group col-lg-12">
						<label for="contrasenaId">Contrase�a:</label>
						<div class="input-group">
							<div class="input-group-addon">
								<i class="fas fa-key"></i>
							</div>
							<input type="password" class="form-control" id="contrasenaId"
								name="password">
						</div>
						<small class="help-block">�Olvidaste tu contrase�a? <a href="passEmpresa.jsp">Ingresa aqu�</a></small>
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
					String emp = "";
					if (request.getAttribute("empEmpresa") != null) {
						emp = (String) request.getAttribute("empEmpresa");
						if (emp.equals("EMP")) {
							session.setAttribute("nombreEmpresa", request.getAttribute("nombreEmpresa"));
							session.setAttribute("codigoEmpresa", request.getAttribute("codigoEmpresa"));
							session.setAttribute("passwordActual", request.getAttribute("passwordEmpresa"));
							session.setAttribute("empEmpresa", emp);
							response.sendRedirect("empresa/indexEmpresa.jsp");
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
			alertify.confirm("�Realmente desea eliminar este rubro?", function(
					e) {
				if (e) {
					location.href = "administrador.do?op=eliminarr&id=" + id;
				}
			});
		}
	</script>
</body>
</html>