<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
<%@ include file='/cabecera.jsp'%>
</head>
<body>
	<jsp:include page="menuAdministrador.jsp" />
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
			<form action="${contextPath}/administrador.do" method="POST">
				<input type="hidden" name="op" value="insertarr">
				<div class="form-group col-lg-12">
					<h2>
						<strong>Nuevo rubro</strong>
					</h2>
				</div>
				<div class="form-group col-lg-12">
					<label for="nombreId">Nombre:</label>
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fas fa-user-edit"></i>
						</div>
						<input type="text" class="form-control" id="nombreId"
							name="nombre">
					</div>
				</div>
				<div class="form-group col-lg-12">
					<label for="descripcionId">Descripción:</label>
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fas fa-user-edit"></i>
						</div>
						<textarea class="form-control" id="descripcionId"
							name="descripcion"></textarea>
					</div>
				</div>
				<div class="form-group col-lg-12">
					<button type="submit" class="btn btn-success" name="Guardar">
						<i class="fas fa-plus"></i> Registrar
					</button>
					<a class="btn btn-danger"
						href="${contextPath}/administrador.do?op=listarr"><i
						class="fas fa-times"></i> Cancelar</a>
				</div>

			</form>

		</div>
	</div>
	<article class="col-xs-1 col-sm-2 col-md-3"></article>

</body>
</html>