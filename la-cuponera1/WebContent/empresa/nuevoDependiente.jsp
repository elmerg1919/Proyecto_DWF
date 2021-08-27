<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page session="true"%>

<%
	HttpSession sesion = request.getSession();
	if (sesion.getAttribute("empEmpresa") == null) {
		response.sendRedirect("../LACUPONERA2019/loginEmpresa.jsp");
	} else {
		String emp = sesion.getAttribute("empEmpresa").toString();
		if (!emp.equals("EMP")) {
			response.sendRedirect("../LACUPONERA2019/loginEmpresa.jsp");
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
			<form action="${contextPath}/empresa.do?op=insertard&id=<%=sesion.getAttribute("codigoEmpresa")%>" method="POST">
				<input type="hidden" name="op" value="insertard">
				<div class="form-group col-lg-12">
					<h2>
						<strong>Nuevo dependiente de sucursal</strong>
					</h2>
				</div>
				
				<div class="form-group col-lg-12">
					<label for="nombresId">Nombres:</label>
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fas fa-file-signature"></i>
						</div>
						<input type="text" class="form-control" id="nombresId"
							name="nombres">
					</div>
				</div>
				
				<div class="form-group col-lg-12">
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
					<label for="correoId">Correo:</label>
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fas fa-at"></i>
						</div>
						<input type="email" class="form-control" id="correoId"
							name="correo">
					</div>
				</div>
				
				<div class="form-group col-lg-12">
					<button type="submit" class="btn btn-success" name="Guardar">
						<i class="fas fa-plus"></i> Registrar
					</button>
					<a class="btn btn-danger"
						href="${contextPath}/empresa.do?op=listard&id=<%=sesion.getAttribute("codigoEmpresa")%>"><i
						class="fas fa-times"></i> Cancelar</a>
				</div>

			</form>

		</div>
	</div>
	<article class="col-xs-1 col-sm-2 col-md-3"></article>

</body>
</html>