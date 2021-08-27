<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setLocale value="en_US" />
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

	<div class="row">
		<div class="container">
			<h2>
				<strong>Lista de dependientes de sucursal </strong><a class="btn btn-success" href="${contextPath}/empresa.do?op=nuevod"><i
										class="fas fa-plus"></i> Nuevo dependiente</a>
			</h2>
			<p class="help-block">
				<strong>TOTAL DE DEPENDIENTES: </strong>${requestScope.totalDependientes}</p>
		</div>
		<div class="row">
			<div class="container">

				<table class="table table-striped table-bordered table-hover"
					id="tabla2" style="width: 100%">
					<thead>
						<tr>
							<th>Nombre</th>
							<th>Correo</th>
							<th>Modificar</th>
							<th>Eliminar</th>
						</tr>
					</thead>
					<tbody>

						<c:forEach items="${requestScope.listaDependientes}"
							var="dependiente">
							<tr>
								<td>${dependiente.nombres} ${dependiente.apellidos}</td>
								<td>${dependiente.correo}</td>
								<td><a style="width: 100%" class="btn btn-warning"
									href="${contextPath}/empresa.do?op=obtenerdpe&id=${dependiente.correo}"><i
										class="fas fa-cog"></i></a></td>
								<td><a style="width: 100%" class="btn btn-danger"
									href="javascript:eliminar('${dependiente.correo}')"><i
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
		$(document).ready(function() {
			$('#tabla2').DataTable();
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
			alertify.confirm("¿Realmente desea eliminar este dependiente?",
					function(e) {
						if (e) {
							location.href = "empresa.do?op=eliminard&id="
									+ id;
						}
					});
		}
	</script>
</body>
</html>