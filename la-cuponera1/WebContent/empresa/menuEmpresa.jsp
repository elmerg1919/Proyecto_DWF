<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
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
				<a href="${contextPath}/empresa/indexEmpresa.jsp" class="navbar-brand" style="font-size: 24px;"><i
					class="fas fa-ghost"></i> <em
					style="font-size: 22px; font-weight: bold;">LA CUPONERA</em></a>
			</div>
			<div class="collapse navbar-collapse" id="navbar-1">
				<ul style="width: 80%;" class="nav navbar-nav">
					<li><a class="dropdown-toggle" data-toggle="dropdown" href="">Ofertas
							<span class="caret"></span>
					</a>
						<ul class="dropdown-menu">
							<li><a href="${contextPath}/empresa.do?op=listaro&id=<%=sesion.getAttribute("codigoEmpresa")%>">Administrar
									ofertas</a></li>
						</ul></li>
					<li><a class="dropdown-toggle" data-toggle="dropdown" href="">
							Sucursales <span class="caret"></span>
					</a>
						<ul class="dropdown-menu">
							<li><a href="${contextPath}/empresa.do?op=listard&id=<%=sesion.getAttribute("codigoEmpresa")%>">Administrar
									dependientes</a></li>
						</ul></li>
					<li style="float: right;"><a class="dropdown-toggle"
						data-toggle="dropdown" href=""><%=sesion.getAttribute("nombreEmpresa")%>
							<span class="caret"></span> </a>
						<ul class="dropdown-menu">
							<li><a href="${contextPath}/empresa/cambiarPassword.jsp">Cambiar
									contraseña</a></li>
							<li><a
								href="${contextPath}/loginEmpresa.jsp?logout=true">Cerrar
									sesión</a></li>
						</ul></li>
				</ul>
			</div>
		</div>
	</nav>
</div>