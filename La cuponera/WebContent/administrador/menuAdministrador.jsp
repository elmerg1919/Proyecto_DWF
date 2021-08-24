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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<div class="container">
	<nav class="navbar navbar-default navbar-fixed-top">
		<div class="container">
			<div class="navbar-header logo">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#navbar-1">
					<span class="sr-only">Menú</span> <span class="icon-bar"></span> <span
						class="icon-bar"></span> <span class="icon-bar"></span>
				</button>
				<a href="${contextPath}/administrador/indexAdministrador.jsp" class="navbar-brand" style="font-size: 24px;"><i
					class="fas fa-ghost"></i> <em
					style="font-size: 22px; font-weight: bold;">LA CUPONERA</em></a>
			</div>
			<div class="collapse navbar-collapse" id="navbar-1">
				<ul style="width: 80%;" class="nav navbar-nav">
					<li><a class="dropdown-toggle" data-toggle="dropdown" href="">Empresas
							<span class="caret"></span>
					</a>
						<ul class="dropdown-menu">
							<li><a href="${contextPath}/administrador.do?op=listare">Administrar
									empresas</a></li>
							<li><a href="${contextPath}/administrador.do?op=listaro">Administrar
									ofertas</a></li>
						</ul></li>
					<li><a class="dropdown-toggle" data-toggle="dropdown" href="">
							Rubros <span class="caret"></span>
					</a>
						<ul class="dropdown-menu">
							<li><a href="${contextPath}/administrador.do?op=listarr">Administrar
									rubros</a></li>
						</ul></li>
					<li><a class="dropdown-toggle" data-toggle="dropdown" href="">
							Clientes <span class="caret"></span>
					</a>
						<ul class="dropdown-menu">
							<li><a href="${contextPath}/administrador.do?op=listaru">Administrar
									clientes</a></li>
						</ul></li>
					<li style="float: right;"><a class="dropdown-toggle"
						data-toggle="dropdown" href=""><%=sesion.getAttribute("nombreUsuario")%>
							<span class="caret"></span> </a>
						<ul class="dropdown-menu">
							<li><a href="${contextPath}/administrador/cambiarPassword.jsp">Cambiar
									contraseña</a></li>
							<li><a
								href="${contextPath}/loginAdministrador.jsp?logout=true">Cerrar
									sesión</a></li>
						</ul></li>
				</ul>
			</div>
		</div>
	</nav>
</div>