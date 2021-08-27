<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
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
					<a class="navbar-brand" style="font-size: 24px;"><i
						class="fas fa-shopping-bag"></i> <em
						style="font-size: 22px; font-weight: bold;">LA CUPONERA</em></a>
				</div>
				<div class="collapse navbar-collapse" id="navbar-1">
					<ul style="width: 80%;" class="nav navbar-nav">
						<li><a class="dropdown-toggle" data-toggle="dropdown" href="">Empresas
								<span class="caret"></span>
						</a>
							<ul class="dropdown-menu">
								<li><a href="loginEmpresa.jsp">Iniciar sesión</a></li>
							</ul></li>
						<li><a class="dropdown-toggle" data-toggle="dropdown" href="">Administradores<span class="caret"></span>
						</a>
							<ul class="dropdown-menu">
								<li><a href="loginAdministrador.jsp">Iniciar sesión</a></li>
							</ul></li>
						<li><a class="dropdown-toggle" data-toggle="dropdown" href="">
								Clientes <span class="caret"></span>
						</a>
							<ul class="dropdown-menu">
								<li><a href="registroClientes.jsp">Registrarse</a></li>
								<li><a href="loginCliente.jsp">Iniciar sesión</a></li>
							</ul></li>
					</ul>
				</div>
			</div>
		</nav>
	</div>