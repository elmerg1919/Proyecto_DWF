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
				<form action="${contextPath}/cliente.do" method="POST">
					<input type="hidden" name="op" value="validarl">
					<div class="form-group col-lg-12">
						<h2>
							<strong><i class="fas fa-users"></i> Iniciar sesi�n</strong>
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
						<small class="help-block">�Olvidaste tu contrase�a? <a href="passCliente.jsp">Ingresa aqu�</a></small>
					</div>

					<div class="form-group col-lg-12">
						<div class="input-group">
							<button  style="margin-right: 10px" type="submit" class="btn btn-success" name="Guardar">
								<i class="fas fa-sign-in-alt"></i> Ingresar
							</button>  �No tienes una cuenta? <a href="registroClientes.jsp">Reg�strate aqu�</a>
						</div>
					</div>
				</form>

				<%
					HttpSession sesion = request.getSession();
					if (request.getAttribute("estadoCliente") != null) {
						int estado = 0;
						estado = (Integer) request.getAttribute("estadoCliente");
						if (estado == 1) {
							session.setAttribute("nombreCliente", request.getAttribute("nombreCliente"));
							session.setAttribute("duiCliente", request.getAttribute("duiCliente"));
							session.setAttribute("correoCliente", request.getAttribute("correoCliente"));
							session.setAttribute("passwordActual", request.getAttribute("passwordCliente"));
							session.setAttribute("estadoCliente", estado);
							response.sendRedirect("cliente/indexCliente.jsp");
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