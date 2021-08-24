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
				<input type="hidden" name="op" value="modificare">
				<div class="form-group col-lg-12">
					<h2>
						<strong>Modificar empresa</strong>
					</h2>
				</div>
				<div class="form-group col-lg-12">
					<label for="codigoId">Código:</label>
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fas fa-key"></i>
						</div>
						<input type="text" class="form-control" id="codigoId" readonly
							value="${empresa.codigo}" name="codigo">
					</div>
				</div>

				<div class="form-group col-lg-12">
					<label for="nombreId">Nombre:</label>
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fas fa-file-signature"></i>
						</div>
						<input type="text" class="form-control" id="nombreId"
							name="nombre" value="${empresa.nombre}">
					</div>
				</div>

				<div class="form-group col-md-6  col-lg-6">
					<label for="contactoId">Contacto:</label>
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fas fa-user-shield"></i>
						</div>
						<input type="text" class="form-control" id="contactoId"
							name="contacto" value="${empresa.contacto}">
					</div>
				</div>

				<div class="form-group col-md-6  col-lg-6">
					<label for="telefonoId">Teléfono:</label>
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fas fa-phone"></i>
						</div>
						<input type="number" class="form-control" id="telefonoId"
							name="telefono" min="0" step="1" value="${empresa.telefono}">
					</div>
				</div>

				<div class="form-group col-lg-12">
					<label for="direccionId">Dirección:</label>
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fas fa-map-marker-alt"></i>
						</div>
						<input type="text" class="form-control" id="direccionId"
							name="direccion" value="${empresa.direccion}">
					</div>
				</div>

				<div class="form-group col-md-6  col-lg-6">
					<label for="selectRubroId">Rubro:</label>
					<div class="input-group">
						<div class="input-group-addon">
							<i class="far fa-window-maximize"></i>
						</div>
						<c:set value="${empresa.stringCodigoRubro}" var="stringrubro" />
						<select class="form-control js-example-basic-single"
							id="selectRubroId" name="rubro">
							<%-- 							<option value="${empresa.codigoRubro}" selected>${empresa.stringCodigoRubro}</option> --%>
							<c:forEach var="rubro" items="${requestScope.listaRubros}">

								<c:set var="nombreRubro" value="${rubro.nombre}" />


								<c:if test="${stringrubro == nombreRubro}">
									<option selected value="${empresa.codigoRubro }">${empresa.stringCodigoRubro }
										(Original)</option>
								</c:if>

								<c:if test="${stringrubro != nombreRubro}">
									<option value="${rubro.codigo}">${rubro.nombre}</option>
								</c:if>

							</c:forEach>
						</select>
					</div>
				</div>

				<div class="form-group col-md-6  col-lg-6">
					<label for="comisionId">Comisión:</label>
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fas fa-percentage"></i>
						</div>
						<input type="number" class="form-control" id="comisionId"
							name="comision" min="0" step="1" value="${empresa.comision}">
					</div>
				</div>

				<div class="form-group col-lg-12">
					<label for="correoId">Correo:</label>
					<div class="input-group">
						<div class="input-group-addon">
							<i class="fas fa-at"></i>
						</div>
						<input type="email" class="form-control" id="correoId"
							name="correo" value="${empresa.correo}">
					</div>
				</div>

				<div class="form-group col-lg-12">
					<button type="submit" class="btn btn-warning" name="Editar">
						<i class="fas fa-edit"></i> Modificar
					</button>
					<a class="btn btn-danger"
						href="${contextPath}/administrador.do?op=listare"><i
						class="fas fa-times"></i> Cancelar</a>
				</div>

			</form>

		</div>
	</div>
	<article class="col-xs-1 col-sm-2 col-md-3"></article>
	<script>
		$(document).ready(function() {
			$('.js-example-basic-single').select2();
		});
	</script>
</body>
</html>