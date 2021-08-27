<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page session="true"%>


<%
	HttpSession sesion = request.getSession();
	if (sesion.getAttribute("estadoCliente") == null) {
		response.sendRedirect("../loginCliente.jsp");
	} else {
		int estado = (Integer)sesion.getAttribute("estadoCliente");
		if (estado != 1) {
			response.sendRedirect("../loginCliente.jsp");
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
	<jsp:include page="menuCliente.jsp" />
	<div class="row">

		<div style="float: none; margin: 0 auto;"
			class="col-md-6 col-centered text-center">
			<h1>
				<strong>Bienvenido<br><%=sesion.getAttribute("nombreCliente")%></strong>
				<br>
			</h1>
			<hr>
			<h3>
				<strong>Gracias por usar nuestro sistema</strong>
			</h3>
			<h4>Seleccione una opcion del menú superior</h4>

		</div>
	</div>
	<script>
		
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