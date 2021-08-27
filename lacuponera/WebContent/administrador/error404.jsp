<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page session="true"%>


<%
	HttpSession sesion = request.getSession();
	if (sesion.getAttribute("tipo") == null) {
		response.sendRedirect("../loginAdministrador.jsp");
	} else {
		String tipo = sesion.getAttribute("tipo").toString();
		if (!tipo.equals("Administrador")) {
			response.sendRedirect("../loginAdministrador.jsp");
		}
	}
%>


<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>La Cuponera - UDB</title>
<%@ include file='/cabecera.jsp'%>
</head>
<body>
	<jsp:include page="menuAdministrador.jsp" />
	<div class="row">

		<div style="float: none; margin: 0 auto;"
			class="col-md-6 col-centered text-center">
			<h1>
				<strong>ERROR 404</strong>
				<br>
			</h1>
			<hr>
			<h3>
				<strong>La página solicitada no se ha encontrado</strong>
			</h3>
			<h4>Seleccione una opcion del menú superior</h4>

		</div>
	</div>

</body>
</html>