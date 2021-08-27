<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page session="true"%>
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
	<div class="row">
		<div class="container">
			<h2><strong>Lista de empresas </strong><a class="btn btn-success" href="${contextPath}/administrador.do?op=nuevae"><i
										class="fas fa-plus"></i> Nueva empresa</a></h2>
			<p class="help-block">
				<strong>TOTAL DE EMPRESAS: </strong>${requestScope.totalEmpresas}</p>
		</div>
		<div class="row">
			<div class="container">

				<table class="table table-striped table-bordered table-hover"
					id="tabla" style="width: 100%">
					<thead>
						<tr>
							<th>Código</th>
							<th>Empresa</th>
							<th>Teléfono</th>
							<th>Correo</th>
							<th>Rubro</th>
							<th>Visualizar</th>
							<th>Modificar</th>
							<th>Eliminar</th>
						</tr>
					</thead>
					<tbody>

						<c:forEach items="${requestScope.listaEmpresas}" var="empresas">
							<tr>
								<!-- propiedades de la clase de los beans Usuario.java -->
								<td>${empresas.codigo}</td>
								<td>${empresas.nombre}</td>
								<td>${empresas.telefono}</td>
								<td>${empresas.correo}</td>
								<td>${empresas.stringCodigoRubro}</td>
								<td><a style="width: 100%" class="btn btn-primary"
									href="${contextPath}/administrador.do?op=obtenere&id=${empresas.codigo}"><i
										class="fas fa-search"></i></a></td>
								<td><a style="width: 100%" class="btn btn-warning"
									href="${contextPath}/administrador.do?op=obtenerpe&id=${empresas.codigo}"><i
										class="fas fa-cog"></i></a></td>
								<td><a style="width: 100%" class="btn btn-danger"
									href="javascript:eliminar('${empresas.codigo}')"><i
										class="fas fa-trash-alt"></i></a></td>

							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>

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
			alertify.confirm("¿Realmente desea eliminar esta empresa?",
					function(e) {
						if (e) {
							location.href = "administrador.do?op=eliminare&id="
									+ id;
						}
					});
		}
	</script>
</body>
</html>