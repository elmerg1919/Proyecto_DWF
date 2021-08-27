<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page session="true"%>
<%
	HttpSession sesion = request.getSession();
	if (sesion.getAttribute("empEmpresa") == null) {
		response.sendRedirect("../loginEmpresa.jsp");
	} else {
		String emp = sesion.getAttribute("empEmpresa").toString();
		if (!emp.equals("EMP")) {
			response.sendRedirect("../loginEmpresa.jsp");
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
	<jsp:include page="menuEmpresa.jsp" />
	<article class="col-xs-1 col-sm-2 col-md-3"></article>
	<div class="container col-xs-10 col-sm-8 col-md-6">
		<div class="row">
			<c:if test="${not empty listaErrores}">
				<div class="alert alert-danger">
					<ul>
						<c:forEach var="errores" items="${requestScope.listaErrores}">
							<li>${errores}</li>
						</c:forEach>
					</ul>
				</div>
			</c:if>
			<form action="${contextPath}/empresa.do?op=cambiarp&id=<%=sesion.getAttribute("codigoEmpresa")%>&pa=<%=sesion.getAttribute("passwordActual")%>" method="POST">
				<input type="hidden" name="op" value="">
				<div class="form-group col-lg-12">
					<h2>
						<strong>Cambiar contraseña</strong>
					</h2>
				</div>
				<div class="form-group col-lg-12">
					<label for="apasswordId">Contraseña actual:</label>
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fas fa-key"></i>
						</div>
						<input type="password" class="form-control" id="apasswordId" name="actualpassword">
					</div>
				</div>
				<div class="form-group col-lg-12">
					<label for="passwordId">Nueva contraseña:</label>
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fas fa-key"></i>
						</div>
						<input type="password" class="form-control" id="passwordId" name="newpassword">
					</div>
				</div>

				<div class="form-group col-lg-12">
					<button type="submit" class="btn btn-warning" name="Guardar">
						<i class="fas fa-cog"></i> Actualizar
					</button>
					<a class="btn btn-danger"
						href="${contextPath}/empresa/indexEmpresa.jsp"><i
						class="fas fa-times"></i> Cancelar</a>
				</div>

			</form>

		</div>
	</div>
	<article class="col-xs-1 col-sm-2 col-md-3"></article>
<script>

$(document).ready(function() {
    $('.js-example-basic-single').select2();
});

</script>
</body>
</html>