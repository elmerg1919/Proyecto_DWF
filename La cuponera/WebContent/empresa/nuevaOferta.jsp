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
			<form action="${contextPath}/empresa.do?op=insertaro&id=<%=sesion.getAttribute("codigoEmpresa")%>" method="POST">
				<input type="hidden" name="op" value="insertaro">
				<div class="form-group col-lg-12">
					<h2>
						<strong>Nueva oferta</strong>
					</h2>
				</div>
				
				<div class="form-group col-lg-12">
					<label for="tituloId">Título:</label>
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fas fa-heading"></i>
						</div>
						<input type="text" class="form-control" id="tituloId"
							name="titulo">
					</div>
				</div>
				
				<div class="form-group col-lg-12">
					<label for="descripcionId">Descripción:</label>
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fas fa-align-justify"></i>
						</div>
						<textarea class="form-control" id="descripcionId"
							name="descripcion"></textarea>
					</div>
				</div>
				
				<div class="form-group col-lg-12">
					<label for="detallesId">Detalles:</label>
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fas fa-list"></i>
						</div>
						<textarea class="form-control" id="detallesId"
							name="detalles"></textarea>
					</div>
				</div>
				
				<div class="form-group col-md-6  col-lg-6">
					<label for="fechaInicioId">Fecha de inicio:</label>
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fas fa-calendar-plus"></i>
						</div>
						<input type="date" class="form-control" id="fechaInicioId"
							name="fechaInicio">
					</div>
				</div>
				
				<div class="form-group col-md-6  col-lg-6">
					<label for="fechaFinalId">Fecha final:</label>
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fas fa-calendar-times"></i>
						</div>
						<input type="date" class="form-control" id="fechaFinalId"
							name="fechaFinal">
					</div>
				</div>
				
				<div class="form-group col-md-4  col-lg-3">
					<label for="precioId">Precio:</label>
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fas fa-dollar-sign"></i>
						</div>
						<input type="number" class="form-control" id="precioId"
							name="precio" min="0" step="0.01">
					</div>
				</div>
				
				<div class="form-group col-md-4  col-lg-3">
					<label for="cuponesId">Cupones disponibles:</label>
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fas fa-ticket-alt"></i>
						</div>
						<input type="number" class="form-control" id="cuponesId"
							name="cupones" min="0" step="1">
					</div>
				</div>
				
				<div class="form-group col-md-4  col-lg-6">
					<label for="fechaVencimientoId">Vencimiento de cupón:</label>
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fas fa-calendar-minus"></i>
						</div>
						<input type="date" class="form-control" id="fechaVencimientoId"
							name="fechaVencimiento">
					</div>
				</div>
				
				<div class="form-group col-lg-12">
					<button type="submit" class="btn btn-success" name="Guardar">
						<i class="fas fa-plus"></i> Registrar
					</button>
					<a class="btn btn-danger"
						href="${contextPath}/empresa.do?op=listaro&id=<%=sesion.getAttribute("codigoEmpresa")%>"><i
						class="fas fa-times"></i> Cancelar</a>
				</div>

			</form>

		</div>
	</div>
	<article class="col-xs-1 col-sm-2 col-md-3"></article>

</body>
</html>