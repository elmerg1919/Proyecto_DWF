/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package sv.edu.udb.www.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sv.edu.udb.www.models.ClienteModel;
import sv.edu.udb.www.beans.Oferta;
//import sv.edu.udb.www.beans.Dependiente;
//import sv.edu.udb.www.beans.Empresa;
//import sv.edu.udb.www.beans.Oferta;
import sv.edu.udb.www.beans.Cupon;
import sv.edu.udb.www.beans.Usuario;
import sv.edu.udb.www.utils.Validaciones;

/**
 *
 * @author
 */
@WebServlet(name = "ClienteController", urlPatterns = { "/cliente.do" })
public class ClienteController extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<String> listaErrores = new ArrayList<>();
	ClienteModel modelo = new ClienteModel();

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException, ParseException {
		response.setContentType("text/html;charset=UTF-8");
		try (PrintWriter out = response.getWriter()) {
			if (request.getParameter("op") == "" || request.getParameter("op") == null) {
				request.getRequestDispatcher("/cliente/indexCliente.jsp").forward(request, response);
				return;
			}
			String operacion = request.getParameter("op");

			switch (operacion) {
			case "registrarse":
				registrarCliente(request, response);
				break;
			case "cambiarp":
				modificarPassword(request, response);
				break;
			case "validarl":
				validarLogin(request, response);
				break;
			case "validarc":
				activarCuenta(request, response);
				break;
			case "listaro":
				listarOfertas(request, response);
				break;
			case "obtenero":
				obtenerOferta(request, response);
				break;
			case "insertarc":
				insertarCupon(request, response);
				break;
			case "listarc":
				obtenerCupones(request, response);
				break;
			case "recuperar":
				recuperarPassword(request, response);
				break;
				
			}
		}
	}

	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the
	// + sign on the left to edit the code.">

	/**
	 * Handles the HTTP <code>GET</code> method.
	 *
	 * @param request  servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			processRequest(request, response);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 *
	 * @param request  servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			processRequest(request, response);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Returns a short description of the servlet.
	 *
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>
	
	
	private void validarLogin(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		try {
			String correo = request.getParameter("correo");
			String password = request.getParameter("password");
			Usuario miCliente = new Usuario();
			miCliente = modelo.validarLogin(correo, password);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			String fechaActual = sdf.format(date);
			modelo.actualizarVencimientosOfertas(fechaActual);
			modelo.actualizarVencimientosCupones(fechaActual);
			if(miCliente != null) {
				String nombreCompleto = miCliente.getNombres() + " " + miCliente.getApellidos();
				request.setAttribute("estadoCliente", miCliente.getEstado());
				request.setAttribute("duiCliente", miCliente.getDui());
				request.setAttribute("passwordCliente", miCliente.getPassword());
				request.setAttribute("correoCliente", miCliente.getCorreo());
				request.setAttribute("nombreCliente", nombreCompleto);
				request.getRequestDispatcher("/loginCliente.jsp").forward(request, response);
			}else {
				request.getSession().setAttribute("fracaso", "Correo o contrase�a inv�lidos");
				response.sendRedirect(request.getContextPath() + "/loginCliente.jsp");
			}
			
		} catch (SQLException | ServletException | IOException ex) {
			Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	private void activarCuenta(HttpServletRequest request, HttpServletResponse response) {
		try {
			listaErrores.clear();
			String codigo = request.getParameter("id");
			
			if (Validaciones.isEmpty(codigo)) {
				listaErrores.add("El DUI es obligatorio para activar tu cuenta");
			}
			if (!Validaciones.esDui(codigo)) {
				listaErrores.add("El DUI tiene que ser v�lido.");
			}
			if (listaErrores.size() > 0) {
				request.setAttribute("listaErrores", listaErrores);
				request.getSession().setAttribute("fracaso", "La cuenta no ha sido activada." + " Hubo un error");
				response.sendRedirect(request.getContextPath() + "/loginCliente.jsp");
			} else {
				if (modelo.activarCuenta(codigo) > 0) {
					request.getSession().setAttribute("exito", "Activaci�n exitosa. Ya puedes iniciar sesi�n");
					response.sendRedirect(request.getContextPath() + "/loginCliente.jsp");
				} else {
					request.getSession().setAttribute("fracaso", "La cuenta no ha sido activada." + " Hubo un error");
					response.sendRedirect(request.getContextPath() + "/loginCliente.jsp");
				}
			}
		} catch (IOException | SQLException  ex) {
			Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	private void recuperarPassword(HttpServletRequest request, HttpServletResponse response) {
		try {
			listaErrores.clear();
			String correo = request.getParameter("correo");
			
			if (Validaciones.isEmpty(correo)) {
				listaErrores.add("El correo es obligatorio para realizar la recuperaci�n");
			}
			if (!Validaciones.esCorreo(correo)) {
				listaErrores.add("El correo tiene que ser v�lido.");
			}
			if (listaErrores.size() > 0) {
				request.setAttribute("listaErrores", listaErrores);
				request.getSession().setAttribute("fracaso", "El proceso de recuperaci�n no puedo terminarse. " + " Hubo un error");
				response.sendRedirect(request.getContextPath() + "/passCliente.jsp");
			} else {
				if (modelo.recuperarPassword(correo) > 0) {
					request.getSession().setAttribute("exito", "Informaci�n enviada. Revisa tu bandeja de entrada");
					response.sendRedirect(request.getContextPath() + "/loginCliente.jsp");
				} else {
					request.getSession().setAttribute("fracaso", "El proceso de recuperaci�n no puedo terminarse. " + " Hubo un error");
					response.sendRedirect(request.getContextPath() + "/passCliente.jsp");
				}
			}
		} catch (IOException | SQLException  ex) {
			Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	private void registrarCliente(HttpServletRequest request, HttpServletResponse response) {
		try {
			listaErrores.clear();
			Usuario miCliente = new Usuario();
			miCliente.setNombres(request.getParameter("nombres"));
			miCliente.setApellidos(request.getParameter("apellidos"));
			miCliente.setDireccion(request.getParameter("direccion"));
			miCliente.setDui(request.getParameter("dui"));
			if(request.getParameter("telefono") != "") {
				miCliente.setTelefono(Integer.parseInt(request.getParameter("telefono")));
			}else {
				miCliente.setTelefono(0);
			}
			miCliente.setCorreo(request.getParameter("correo"));
			miCliente.setPassword(request.getParameter("password"));
			
			if (Validaciones.isEmpty(miCliente.getNombres())) {
				listaErrores.add("Los nombres son obligatorios.");
			}
			if (Validaciones.isEmpty(miCliente.getApellidos())) {
				listaErrores.add("Los apellidos son obligatorios.");
			}
			if (Validaciones.isEmpty(miCliente.getDireccion())) {
				listaErrores.add("La direcci�n es obligatoria.");
			}
			if (!Validaciones.esDui(miCliente.getDui())) {
				listaErrores.add("El DUI es obligatorio.");
			}
			if (!Validaciones.esTelefono(miCliente.getTelefono())) {
				listaErrores.add("El tel�fono es obligatorio");
			}
			if (!Validaciones.esCorreo(miCliente.getCorreo())) {
				listaErrores.add("El correo es obligatorio");
			}
			if (Validaciones.isEmpty(miCliente.getPassword())) {
				listaErrores.add("La contrase�a es obligatoria.");
			}
			
			String url = "http://localhost:8080/lacuponera/cliente.do?op=validarc&id=" + miCliente.getDui();
			
			if (listaErrores.size() > 0) {
				request.setAttribute("listaErrores", listaErrores);
				request.setAttribute("cliente", miCliente);
				request.getRequestDispatcher("registroClientes.jsp").forward(request, response);
			} else {
				if (modelo.insertarCliente(miCliente, url) > 0) {
					request.getSession().setAttribute("exito", "Registro exitoso. Activa tu cuenta para iniciar sesion");
					response.sendRedirect(request.getContextPath() + "/loginCliente.jsp");
				} else {
					request.getSession().setAttribute("fracaso", "No ha sido registrado." + " Hubo un error");
					response.sendRedirect(request.getContextPath() + "/registroClientes.jsp");
				}
			}
		} catch (IOException | SQLException | ServletException | NumberFormatException  ex) {
			Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	private void modificarPassword(HttpServletRequest request, HttpServletResponse response) {
		try {
			listaErrores.clear();
			String codigo = request.getParameter("id");
			String contraActual = request.getParameter("pa");
			String actualPassword = request.getParameter("actualpassword");
			String newPassword = request.getParameter("newpassword");
			if (Validaciones.isEmpty(codigo)) {
				listaErrores.add("El c�digo es obligatorio");
			}
			if (Validaciones.isEmpty(contraActual)) {
				listaErrores.add("La contrase�a antigua es obligatoria");
			}
			if (Validaciones.isEmpty(actualPassword)) {
				listaErrores.add("La contrase�a actual es obligatoria");
			}
			if (Validaciones.isEmpty(newPassword)) {
				listaErrores.add("La nueva contrase�a es obligatoria");
			}
			
			if (listaErrores.size() > 0) {
				request.setAttribute("listaErrores", listaErrores);
				request.getRequestDispatcher("/cliente/cambiarPassword.jsp").forward(request, response);
			} else {
				if(contraActual.equals(actualPassword)) {
					if (modelo.modificarPassword(codigo, newPassword) > 0) {
						request.getSession().removeAttribute("passwordActual");
						request.getSession().setAttribute("passwordActual", newPassword);
						request.getSession().setAttribute("exito", "Contrase�a actualizada exitosamente");
						request.getRequestDispatcher("/cliente/indexCliente.jsp").forward(request, response);
					} 
				}else {
					request.getSession().setAttribute("fracaso", "La contrase�a no ha sido modificada. " + "Hubo un error");
					request.getRequestDispatcher("/cliente/indexCliente.jsp").forward(request, response);
				}
				
			}
		} catch (IOException | SQLException | ServletException ex) {
			Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	private void listarOfertas(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			String fechaActual = sdf.format(date);
			request.setAttribute("listaOfertas", modelo.listarOfertas(fechaActual));
			request.setAttribute("totalOfertas", modelo.totalOfertas(fechaActual));
			request.getRequestDispatcher("/cliente/listaOfertas.jsp").forward(request, response);
		} catch (SQLException | ServletException | IOException ex) {
			Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	private void obtenerOferta(HttpServletRequest request, HttpServletResponse response) {
		try {
			String codigo = request.getParameter("id");
			Oferta miOferta = modelo.obtenerOferta(codigo);
			if (miOferta != null) {
				request.setAttribute("oferta", miOferta);
				request.getRequestDispatcher("/cliente/verOferta.jsp").forward(request, response);
			} else {
				request.getRequestDispatcher("/cliente/error404.jsp").forward(request, response);
			}
		} catch (SQLException | IOException | ServletException ex) {
			Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	private void insertarCupon(HttpServletRequest request, HttpServletResponse response) throws ParseException {
		try {
			listaErrores.clear();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
			String codigoOferta = request.getParameter("of");
			String fechaFinalCupon = request.getParameter("fe");
			String dui = request.getParameter("dui");
			String codigoEmpresa = request.getParameter("emp");
			String correoUsuario = request.getParameter("email");
			String numTarjeta = request.getParameter("tarjeta");
			int numTarjetaInt;
		
			String titular = request.getParameter("titular");
			String mes = request.getParameter("mes");
			if(request.getParameter("mes") != "") {
				mes = request.getParameter("mes");
			}else {
				mes = "0";
			}
			String year = request.getParameter("year");
			if(request.getParameter("year") != "") {
				year = request.getParameter("year");
			}else {
				year = "0";
			}
			int cvv;
			if(request.getParameter("cvv") != "") {
				cvv = Integer.parseInt(request.getParameter("cvv"));
			}else {
				cvv = 0;
			}
			
			Date date1 = new Date();
			String fechaActual1 = sdf.format(date1);
			Date dateHoy1 = sdf.parse(fechaActual1);
			String fechaVencimientoTarjeta = year + "-" + mes + "-01";
			Date compararVence = sdf2.parse(fechaVencimientoTarjeta);
			if (Validaciones.isEmpty(numTarjeta)) {
				listaErrores.add("El n�mero de la tarjeta es obligatorio");
			}
			if (Validaciones.isEmpty(titular)) {
				listaErrores.add("El titular de la tarjeta es obligatorio");
			}
			if(compararVence.equals(dateHoy1)) {
				listaErrores.add("La fecha de vencimiento tiene que ser v�lida");
			}
			if(compararVence.before(dateHoy1)) {
				listaErrores.add("La fecha de vencimiento tiene que ser v�lida");
			}
			if (!Validaciones.esEnteroPositivo(cvv)) {
				listaErrores.add("El c�digo de seguridad tiene que ser v�lido");
			}
			
			
			if (listaErrores.size() > 0) {
				request.setAttribute("listaErrores", listaErrores);
				request.getRequestDispatcher("cliente.do?op=obtenero&id="+codigoOferta).forward(request, response);
			} else {
				Cupon miCupon = new Cupon();
				String codigoCupon = codigoEmpresa + modelo.obtenerCodigoCupon();
				miCupon.setCodigo(codigoCupon);
				miCupon.setCodigoOferta(Integer.valueOf(codigoOferta));
				miCupon.setDui(dui);
				miCupon.setFechaVencimiento(fechaFinalCupon);
				Date date = new Date();
				String fechaVencimiento = request.getParameter("fe");
				String fechaActual = sdf.format(date);
				Date dateHoy = sdf.parse(fechaActual);
				Date dateVencimiento = sdf.parse(fechaVencimiento);
				if(dateVencimiento.equals(dateHoy)) {
					miCupon.setEstado("Vencido");
				}else if(dateVencimiento.before(dateHoy)){
					miCupon.setEstado("Vencido");
				}else if(dateVencimiento.after(dateHoy)){
					miCupon.setEstado("Disponible");
				}
				
				if (modelo.insertarCupon(miCupon, correoUsuario) > 0 && modelo.reducirCupones(codigoOferta) > 0) {
					
					request.getSession().setAttribute("exito", "Cup�n comprado exitosamente");
					response.sendRedirect(request.getContextPath() + "/cliente.do?op=listaro");
				} else {
					request.getSession().setAttribute("fracaso", "El cup�n no se pudo comprar. " + " Hubo un error");
					response.sendRedirect(request.getContextPath() + "/cliente.do?op=listaro");
				}
			}
		} catch (IOException | SQLException | ServletException ex) {
			Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	private void obtenerCupones(HttpServletRequest request, HttpServletResponse response) {
		try {
			String codigo = request.getParameter("id");
			Usuario miUsuario = modelo.obtenerUsuario(codigo);
			List<Cupon> miCupon = modelo.listarCupones(codigo);
			if (miUsuario != null && miCupon != null) {
				request.setAttribute("usuario", miUsuario);
				request.setAttribute("listaCupones", miCupon);
				request.getRequestDispatcher("/cliente/verCupones.jsp").forward(request, response);
			} else {
				request.getRequestDispatcher("/cliente/error404.jsp").forward(request, response);
			}
		} catch (SQLException | IOException | ServletException ex) {
			Logger.getLogger(AdministradorController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	/*private void insertarOferta(HttpServletRequest request, HttpServletResponse response) throws ParseException, SQLException{
		try {
			listaErrores.clear();
			String codigo = request.getParameter("id");
			Oferta miOferta = new Oferta();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String fechaInicio = request.getParameter("fechaInicio");
			String fechaFinal = request.getParameter("fechaFinal");
			String fechaVencimiento = request.getParameter("fechaVencimiento");
			Date date = new Date();
			String fechaActual = sdf.format(date);
			if(Validaciones.isEmpty(fechaInicio) || Validaciones.isEmpty(fechaFinal) || Validaciones.isEmpty(fechaVencimiento)) {
				listaErrores.add("Todas las fechas son obligatorias.");
			}else {
				Date dateInicio = sdf.parse(fechaInicio);
				Date dateHoy = sdf.parse(fechaActual);
				Date dateFinal = sdf.parse(fechaFinal);
				Date dateVencimiento = sdf.parse(fechaVencimiento);
				if(dateVencimiento.equals(dateHoy)) {
					listaErrores.add("Los cupones no se pueden vencer hoy.");
				}else {
					miOferta.setFechaFinalCupon(request.getParameter("fechaVencimiento"));
				}			
				if(dateInicio.before(dateHoy)) {
					listaErrores.add("La fecha inicial tiene que ser mayor o igual a la fecha de hoy.");
				}else {
					miOferta.setFechaInicio(request.getParameter("fechaInicio"));
				}
				
				if(dateFinal.before(dateInicio)) {
					listaErrores.add("La fecha final tiene que ser mayor a la fecha de inicio.");
				}else {
					miOferta.setFechaFinal(request.getParameter("fechaFinal"));
				}			
				if(dateVencimiento.before(dateInicio)) {
					listaErrores.add("La fecha de vencimiento tiene que ser mayor a la fecha de inicio.");
				}else {
					miOferta.setFechaFinalCupon(request.getParameter("fechaVencimiento"));
				}
			}
			
			
			miOferta.setTitulo(request.getParameter("titulo"));
			miOferta.setDescripcion(request.getParameter("descripcion"));
			miOferta.setDetalles(request.getParameter("detalles"));
			if(request.getParameter("precio") != "") {
				miOferta.setPrecio(Double.parseDouble(request.getParameter("precio")));
			}else {
				miOferta.setPrecio(0);
			}
			if(request.getParameter("cupones") != "") {
				miOferta.setCuponesDisponibles(Integer.parseInt(request.getParameter("cupones")));
			}else {
				miOferta.setCuponesDisponibles(0);
			}
			if (Validaciones.isEmpty(miOferta.getTitulo())) {
				listaErrores.add("El t�tulo de la oferta es obligatorio.");
			}
			if (Validaciones.isEmpty(miOferta.getDescripcion())) {
				listaErrores.add("La descripci�n de la oferta es obligatoria.");
			}
			if (Validaciones.isEmpty(miOferta.getDetalles())) {
				miOferta.setDetalles("No hay detalles");
			}
			if (Validaciones.isEmpty(miOferta.getFechaInicio())) {
				listaErrores.add("La fecha inicial de la oferta es obligatoria.");
			}
			if (Validaciones.isEmpty(miOferta.getFechaFinal())) {
				listaErrores.add("La fecha final de la oferta es obligatoria.");
			}
			if (Validaciones.isEmpty(miOferta.getFechaFinalCupon())) {
				listaErrores.add("La fecha de vencimiento del cup�n es obligatoria.");
			}
			if (listaErrores.size() > 0) {
				request.setAttribute("listaErrores", listaErrores);
				request.setAttribute("oferta", miOferta);
				request.getRequestDispatcher("empresa.do?op=nuevao").forward(request, response);
			} else {
				if (modelo.insertarOferta(miOferta, codigo) > 0) {
					request.getSession().setAttribute("exito", "Oferta registrada exitosamente");
					response.sendRedirect(request.getContextPath() + "/empresa.do?op=listaro&id="+codigo);
				} else {
					request.getSession().setAttribute("fracaso", "La oferta no ha sido registrada." + " Hubo un error");
					response.sendRedirect(request.getContextPath() + "/empresa.do?op=listaro&id="+codigo);
				}
			}
		} catch (IOException | ParseException | ServletException | NumberFormatException  ex) {
			Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	private void listarOfertas(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		try {
			String codigo = request.getParameter("id");
			request.setAttribute("listaOfertasPendientes", modelo.listarOfertasPendientes(codigo));
			request.setAttribute("totalOfertasPendientes", modelo.totalOfertasPendientes(codigo));
			request.setAttribute("listaOfertas", modelo.listarOfertas(codigo));
			request.setAttribute("totalOfertas", modelo.totalOfertas(codigo));
			request.getRequestDispatcher("/empresa/listaOfertas.jsp").forward(request, response);
		} catch (SQLException | ServletException | IOException ex) {
			Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	private void listarDependientes(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		try {
			String codigo = request.getParameter("id");
			request.setAttribute("listaDependientes", modelo.listarDependientes(codigo));
			request.setAttribute("totalDependientes", modelo.totalDependientes(codigo));
			request.getRequestDispatcher("/empresa/listaDependientes.jsp").forward(request, response);
		} catch (SQLException | ServletException | IOException ex) {
			Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	private void obtenerOferta(HttpServletRequest request, HttpServletResponse response) {
		try {
			String codigo = request.getParameter("id");
			Oferta miOferta = modelo.obtenerOferta(codigo);
			if (miOferta != null) {
				request.setAttribute("oferta", miOferta);
				request.getRequestDispatcher("/empresa/verOferta.jsp").forward(request, response);
			} else {
				request.getRequestDispatcher("/empresa/error404.jsp").forward(request, response);
			}
		} catch (SQLException | IOException | ServletException ex) {
			Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	
	
	private void obtenerOfertaParaEditar(HttpServletRequest request, HttpServletResponse response) {
		try {
			String codigo = request.getParameter("id");
			Oferta miOferta = modelo.obtenerOferta(codigo);
			if (miOferta != null) {
				request.setAttribute("oferta", miOferta);
				request.getRequestDispatcher("/empresa/modificarOferta.jsp").forward(request, response);
			} else {
				request.getRequestDispatcher("/empresa/error404.jsp").forward(request, response);
			}
		} catch (SQLException | IOException | ServletException ex) {
			Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	private void obtenerDependiente(HttpServletRequest request, HttpServletResponse response) {
		try {
			String correo = request.getParameter("id");
			Dependiente miDependiente = modelo.obtenerDependiente(correo);
			if (miDependiente != null) {
				request.setAttribute("dependiente", miDependiente);
				request.getRequestDispatcher("/empresa/modificarDependiente.jsp").forward(request, response);
			} else {
				request.getRequestDispatcher("/empresa/error404.jsp").forward(request, response);
			}
		} catch (SQLException | IOException | ServletException ex) {
			Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	private void modificarOferta(HttpServletRequest request, HttpServletResponse response) throws ParseException, SQLException {
		try {
			listaErrores.clear();
			String codigoEmpresa = request.getParameter("id");
			int codigoOferta = Integer.parseInt(request.getParameter("of"));
			Oferta miOferta = new Oferta();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String fechaInicio = request.getParameter("fechaInicio");
			String fechaFinal = request.getParameter("fechaFinal");
			String fechaVencimiento = request.getParameter("fechaVencimiento");
			Date date = new Date();
			String fechaActual = sdf.format(date);
			if(Validaciones.isEmpty(fechaInicio) || Validaciones.isEmpty(fechaFinal) || Validaciones.isEmpty(fechaVencimiento)) {
				listaErrores.add("Todas las fechas son obligatorias.");
			}else {
				Date dateInicio = sdf.parse(fechaInicio);
				Date dateHoy = sdf.parse(fechaActual);
				Date dateFinal = sdf.parse(fechaFinal);
				Date dateVencimiento = sdf.parse(fechaVencimiento);
				if(dateVencimiento.equals(dateHoy)) {
					listaErrores.add("Los cupones no se pueden vencer hoy.");
				}else {
					miOferta.setFechaFinalCupon(request.getParameter("fechaVencimiento"));
				}			
				if(dateInicio.before(dateHoy)) {
					listaErrores.add("La fecha inicial tiene que ser mayor o igual a la fecha de hoy.");
				}else {
					miOferta.setFechaInicio(request.getParameter("fechaInicio"));
				}
				
				if(dateFinal.before(dateInicio)) {
					listaErrores.add("La fecha final tiene que ser mayor a la fecha de inicio.");
				}else {
					miOferta.setFechaFinal(request.getParameter("fechaFinal"));
				}			
				if(dateVencimiento.before(dateInicio)) {
					listaErrores.add("La fecha de vencimiento tiene que ser mayor a la fecha de inicio.");
				}else {
					miOferta.setFechaFinalCupon(request.getParameter("fechaVencimiento"));
				}
			}
			miOferta.setCodigo(codigoOferta);
			miOferta.setCodigoEmpresa(codigoEmpresa);
			miOferta.setTitulo(request.getParameter("titulo"));
			miOferta.setDescripcion(request.getParameter("descripcion"));
			miOferta.setDetalles(request.getParameter("detalles"));
			if(request.getParameter("precio") != "") {
				miOferta.setPrecio(Double.parseDouble(request.getParameter("precio")));
			}else {
				miOferta.setPrecio(0);
			}
			if(request.getParameter("cupones") != "") {
				miOferta.setCuponesDisponibles(Integer.parseInt(request.getParameter("cupones")));
			}else {
				miOferta.setCuponesDisponibles(0);
			}
			if (Validaciones.isEmpty(miOferta.getTitulo())) {
				listaErrores.add("El t�tulo de la oferta es obligatorio.");
			}
			if (Validaciones.isEmpty(miOferta.getDescripcion())) {
				listaErrores.add("La descripci�n de la oferta es obligatoria.");
			}
			if (Validaciones.isEmpty(miOferta.getDetalles())) {
				miOferta.setDetalles("No hay detalles");
			}
			if (Validaciones.isEmpty(miOferta.getFechaInicio())) {
				listaErrores.add("La fecha inicial de la oferta es obligatoria.");
			}
			if (Validaciones.isEmpty(miOferta.getFechaFinal())) {
				listaErrores.add("La fecha final de la oferta es obligatoria.");
			}
			if (Validaciones.isEmpty(miOferta.getFechaFinalCupon())) {
				listaErrores.add("La fecha de vencimiento del cup�n es obligatoria.");
			}
			if (listaErrores.size() > 0) {
				request.setAttribute("listaErrores", listaErrores);
				request.setAttribute("oferta", miOferta);
				request.getRequestDispatcher("empresa.do?op=obtenerpe&id=" + miOferta.getCodigo()).forward(request,
						response);
			} else {
				if (modelo.modificarOferta(miOferta, codigoOferta) > 0) {
					request.getSession().setAttribute("exito", "Oferta actualizada exitosamente");
					response.sendRedirect(request.getContextPath() + "/empresa.do?op=listaro&id="+codigoEmpresa);
				} else {
					request.getSession().setAttribute("fracaso", "La oferta no ha sido actualizada. " + "Hubo un error");
					response.sendRedirect(request.getContextPath() + "/empresa.do?op=listaro&id="+codigoEmpresa);
				}
			}
		} catch (IOException | SQLException | ServletException ex) {
			Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	private void modificarDependiente(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		try {
			listaErrores.clear();
			String codigoEmpresa = request.getParameter("id");
			Dependiente miDependiente = new Dependiente();
			miDependiente.setNombres(request.getParameter("nombres"));
			miDependiente.setApellidos(request.getParameter("apellidos"));
			miDependiente.setCorreo(request.getParameter("correo"));
			if (Validaciones.isEmpty(codigoEmpresa)) {
				listaErrores.add("El c�digo de la empresa es obligatorio.");
			}
			if (Validaciones.isEmpty(miDependiente.getNombres())) {
				listaErrores.add("Los nombres del dependiente son obligatorios.");
			}
			if (Validaciones.isEmpty(miDependiente.getApellidos())) {
				listaErrores.add("Los apellidos del dependiente son obligatorios.");
			}
			if (!Validaciones.esCorreo(miDependiente.getCorreo())) {
				listaErrores.add("El correo del dependiente es obligatorio");
			}
			
			if (listaErrores.size() > 0) {
				request.setAttribute("listaErrores", listaErrores);
				request.setAttribute("dependiente", miDependiente);
				request.getRequestDispatcher("empresa.do?op=obtenerdpe&id=" + miDependiente.getCorreo()).forward(request,
						response);
			} else {
				if (modelo.modificarDependiente(miDependiente) > 0) {
					request.getSession().setAttribute("exito", "Dependiente actualizado exitosamente");
					response.sendRedirect(request.getContextPath() + "/empresa.do?op=listard&id="+codigoEmpresa);
				} else {
					request.getSession().setAttribute("fracaso", "El dependiente no ha sido actualizado. " + "Ya hay un dependiente registrado con ese correo electr�nico");
					response.sendRedirect(request.getContextPath() + "/empresa.do?op=listard&id="+codigoEmpresa);
				}
			}
		} catch (IOException | SQLException | ServletException ex) {
			Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	/*private void listarUsuarios(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		try {
			request.setAttribute("listaClientes", modelo.listarUsuarios());
			request.setAttribute("totalUsuarios", modelo.totalUsuarios());
			request.getRequestDispatcher("/administrador/listaUsuarios.jsp").forward(request, response);
		} catch (SQLException | ServletException | IOException ex) {
			Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void listarEmpresas(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		try {
			request.setAttribute("listaEmpresas", modelo.listarEmpresas());
			request.setAttribute("totalEmpresas", modelo.totalEmpresas());
			request.getRequestDispatcher("/administrador/listaEmpresas.jsp").forward(request, response);
		} catch (SQLException | ServletException | IOException ex) {
			Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void listarRubros(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		try {
			request.setAttribute("listaRubros", modelo.listarRubros());
			request.setAttribute("totalRubros", modelo.totalRubros());
			request.getRequestDispatcher("/administrador/listaRubros.jsp").forward(request, response);
		} catch (SQLException | ServletException | IOException ex) {
			Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	private void listarOfertas(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		try {
			request.setAttribute("listaOfertasPendientes", modelo.listarOfertasPendientes());
			request.setAttribute("totalOfertasPendientes", modelo.totalOfertasPendientes());
			request.setAttribute("listaOfertas", modelo.listarOfertas());
			request.setAttribute("totalOfertas", modelo.totalOfertas());
			request.getRequestDispatcher("/administrador/listaOfertas.jsp").forward(request, response);
		} catch (SQLException | ServletException | IOException ex) {
			Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void nuevaEmpresa(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setAttribute("listaRubros", modelo.listarRubros());
			request.setAttribute("nuevoCodigo", modelo.obtenerCodigoEmpresa());
			request.getRequestDispatcher("/administrador/nuevaEmpresa.jsp").forward(request, response);
		} catch (SQLException | ServletException | IOException ex) {
			Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void insertarRubro(HttpServletRequest request, HttpServletResponse response) {
		try {
			listaErrores.clear();
			Rubro miRubro = new Rubro();
			miRubro.setNombre(request.getParameter("nombre"));
			miRubro.setDescripcion(request.getParameter("descripcion"));

			if (Validaciones.isEmpty(miRubro.getNombre())) {
				listaErrores.add("El nombre del rubro es obligatorio");
			}
			if (Validaciones.isEmpty(miRubro.getDescripcion())) {
				listaErrores.add("La descipci�n del rubro es obligatoria");
			}
			if (listaErrores.size() > 0) {
				request.setAttribute("listaErrores", listaErrores);
				request.setAttribute("rubro", miRubro);
				request.getRequestDispatcher("administrador.do?op=nuevor").forward(request, response);
			} else {
				if (modelo.insertarRubro(miRubro) > 0) {
					request.getSession().setAttribute("exito", "Rubro registrado exitosamente");
					response.sendRedirect(request.getContextPath() + "/administrador.do?op=listarr");
				} else {
					request.getSession().setAttribute("fracaso", "El rubro no ha sido registrado." + " Hubo un error");
					response.sendRedirect(request.getContextPath() + "/administrador.do?op=listarr");
				}
			}
		} catch (IOException | SQLException | ServletException ex) {
			Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	private void insertarEmpresa(HttpServletRequest request, HttpServletResponse response) {
		try {
			listaErrores.clear();
			Empresa miEmpresa = new Empresa();
			miEmpresa.setCodigo(request.getParameter("codigo"));
			miEmpresa.setNombre(request.getParameter("nombre"));
			miEmpresa.setContacto(request.getParameter("contacto"));
			miEmpresa.setDireccion(request.getParameter("direccion"));
			miEmpresa.setCorreo(request.getParameter("correo"));
			if(request.getParameter("comision") != "") {
				miEmpresa.setComision(Integer.valueOf(request.getParameter("comision")));
			}else {
				miEmpresa.setComision(0);
			}
			
			miEmpresa.setCodigoRubro(Integer.parseInt(request.getParameter("rubro")));
			if(request.getParameter("telefono") != "") {
				miEmpresa.setTelefono(Integer.parseInt(request.getParameter("telefono")));
			}else {
				miEmpresa.setTelefono(0);
			}
			
			if (Validaciones.isEmpty(miEmpresa.getCodigo())) {
				listaErrores.add("El c�digo de la empresa es obligatorio.");
			}
			if (!Validaciones.esCodigoEmpresa(miEmpresa.getCodigo())) {
				listaErrores.add("El c�digo de la empresa debe tener un formato \"EMP###\".");
			}
			if (Validaciones.isEmpty(miEmpresa.getNombre())) {
				listaErrores.add("El nombre de la empresa es obligatorio");
			}
			if (Validaciones.isEmpty(miEmpresa.getContacto())) {
				listaErrores.add("El contacto de la empresa es obligatorio");
			}
			if (!Validaciones.esTelefono(miEmpresa.getTelefono())) {
				listaErrores.add("El tel�fono de la empresa es obligatorio");
			}
			if (Validaciones.isEmpty(miEmpresa.getDireccion())) {
				listaErrores.add("La direcci�n de la empresa es obligatoria");
			}
			if (!Validaciones.esEnteroPositivo(miEmpresa.getCodigoRubro())) {
				listaErrores.add("El rubro de la empresa es obligatorio");
			}
			if (!Validaciones.esEnteroPositivo(miEmpresa.getComision())) {
				listaErrores.add("La comisi�n para la empresa es obligatoria");
			}
			if (!Validaciones.esCorreo(miEmpresa.getCorreo())) {
				listaErrores.add("El correo de la empresa es obligatorio");
			}
			
			if (listaErrores.size() > 0) {
				request.setAttribute("listaErrores", listaErrores);
				request.setAttribute("empresa", miEmpresa);
				request.getRequestDispatcher("administrador.do?op=nuevae").forward(request, response);
			} else {
				if (modelo.insertarEmpresa(miEmpresa) > 0) {
					request.getSession().setAttribute("exito", "Empresa registrada exitosamente");
					response.sendRedirect(request.getContextPath() + "/administrador.do?op=listare");
				} else {
					request.getSession().setAttribute("fracaso", "La empresa no ha sido registrada." + " Hubo un error");
					response.sendRedirect(request.getContextPath() + "/administrador.do?op=listare");
				}
			}
		} catch (IOException | SQLException | ServletException | NumberFormatException  ex) {
			Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void obtener(HttpServletRequest request, HttpServletResponse response) {
		try {
			String codigo = request.getParameter("id");
			Usuario miUsuario = modelo.obtenerUsuario(codigo);
			List<Cupon> miCupon = modelo.listarCupones(codigo);
			if (miUsuario != null && miCupon != null) {
				request.setAttribute("usuario", miUsuario);
				request.setAttribute("listaCupones", miCupon);
				request.getRequestDispatcher("/administrador/verUsuario.jsp").forward(request, response);
			} else {
				request.getRequestDispatcher("/administrador/error404.jsp").forward(request, response);
			}
		} catch (SQLException | IOException | ServletException ex) {
			Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void obtenerEmpresa(HttpServletRequest request, HttpServletResponse response) {
		try {
			String codigo = request.getParameter("id");
			Empresa miEmpresa = modelo.obtenerEmpresa(codigo);
			List<Oferta> miOferta = modelo.listarOfertas(codigo);
			if (miEmpresa != null && miOferta != null) {
				request.setAttribute("empresa", miEmpresa);
				request.setAttribute("listaOfertas", miOferta);
				request.getRequestDispatcher("/administrador/verEmpresa.jsp").forward(request, response);
			} else {
				request.getRequestDispatcher("/administrador/error404.jsp").forward(request, response);
			}
		} catch (SQLException | IOException | ServletException ex) {
			Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void obtenerRubro(HttpServletRequest request, HttpServletResponse response) {
		try {
			String codigo = request.getParameter("id");
			Rubro miRubro = modelo.obtenerRubro(codigo);
			if (miRubro != null) {
				request.setAttribute("rubro", miRubro);
				request.getRequestDispatcher("/administrador/modificarRubro.jsp").forward(request, response);
			} else {
				request.getRequestDispatcher("/administrador/error404.jsp").forward(request, response);
			}
		} catch (SQLException | IOException | ServletException ex) {
			Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void obtenerEmpresaParaEditar(HttpServletRequest request, HttpServletResponse response) {
		try {
			String codigo = request.getParameter("id");
			Empresa miEmpresa = modelo.obtenerEmpresa(codigo);
			if (miEmpresa != null) {
				request.setAttribute("empresa", miEmpresa);
				request.setAttribute("listaRubros", modelo.listarRubros());
				request.getRequestDispatcher("/administrador/modificarEmpresa.jsp").forward(request, response);
			} else {
				request.getRequestDispatcher("/administrador/error404.jsp").forward(request, response);
			}
		} catch (SQLException | IOException | ServletException ex) {
			Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	private void obtenerOferta(HttpServletRequest request, HttpServletResponse response) {
		try {
			String codigo = request.getParameter("id");
			Oferta miOferta = modelo.obtenerOferta(codigo);
			if (miOferta != null) {
				request.setAttribute("oferta", miOferta);
				request.getRequestDispatcher("/administrador/verOferta.jsp").forward(request, response);
			} else {
				request.getRequestDispatcher("/administrador/error404.jsp").forward(request, response);
			}
		} catch (SQLException | IOException | ServletException ex) {
			Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	private void modificarRubro(HttpServletRequest request, HttpServletResponse response) {
		try {
			listaErrores.clear();
			Rubro miRubro = new Rubro();
			miRubro.setCodigo(Integer.parseInt(request.getParameter("codigo")));
			miRubro.setNombre(request.getParameter("nombre"));
			miRubro.setDescripcion(request.getParameter("descripcion"));

			if (Validaciones.isEmpty(Integer.toString(miRubro.getCodigo()))) {
				listaErrores.add("El c�digo es obligatorio");
			}
			if (Validaciones.isEmpty(miRubro.getNombre())) {
				listaErrores.add("El nombre del rubro es obligatorio");
			}
			if (Validaciones.isEmpty(miRubro.getDescripcion())) {
				listaErrores.add("La descipci�n del rubro es obligatoria");
			}
			if (listaErrores.size() > 0) {
				request.setAttribute("listaErrores", listaErrores);
				request.setAttribute("rubro", miRubro);
				request.getRequestDispatcher("administrador.do?op=obtenerr&id=" + miRubro.getCodigo()).forward(request,
						response);
			} else {
				if (modelo.modificarRubro(miRubro) > 0) {
					request.getSession().setAttribute("exito", "Rubro modificado exitosamente");
					response.sendRedirect(request.getContextPath() + "/administrador.do?op=listarr");
				} else {
					request.getSession().setAttribute("fracaso", "El rubro no ha sido modificado. " + "Hubo un error");
					response.sendRedirect(request.getContextPath() + "/administrador.do?op=listarr");
				}
			}
		} catch (IOException | SQLException | ServletException ex) {
			Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	
	
	private void modificarEmpresa(HttpServletRequest request, HttpServletResponse response) {
		try {
			listaErrores.clear();
			Empresa miEmpresa = new Empresa();
			miEmpresa.setCodigo(request.getParameter("codigo"));
			miEmpresa.setNombre(request.getParameter("nombre"));
			miEmpresa.setContacto(request.getParameter("contacto"));
			miEmpresa.setDireccion(request.getParameter("direccion"));
			miEmpresa.setCorreo(request.getParameter("correo"));
			if(request.getParameter("comision") != "") {
				miEmpresa.setComision(Integer.valueOf(request.getParameter("comision")));
			}else {
				miEmpresa.setComision(0);
			}
			
			miEmpresa.setCodigoRubro(Integer.parseInt(request.getParameter("rubro")));
			if(request.getParameter("telefono") != "") {
				miEmpresa.setTelefono(Integer.parseInt(request.getParameter("telefono")));
			}else {
				miEmpresa.setTelefono(0);
			}
			
			if (Validaciones.isEmpty(miEmpresa.getCodigo())) {
				listaErrores.add("El c�digo de la empresa es obligatorio.");
			}
			if (!Validaciones.esCodigoEmpresa(miEmpresa.getCodigo())) {
				listaErrores.add("El c�digo de la empresa debe tener un formato \"EMP###\".");
			}
			if (Validaciones.isEmpty(miEmpresa.getNombre())) {
				listaErrores.add("El nombre de la empresa es obligatorio");
			}
			if (Validaciones.isEmpty(miEmpresa.getContacto())) {
				listaErrores.add("El contacto de la empresa es obligatorio");
			}
			if (!Validaciones.esTelefono(miEmpresa.getTelefono())) {
				listaErrores.add("El tel�fono de la empresa es obligatorio");
			}
			if (Validaciones.isEmpty(miEmpresa.getDireccion())) {
				listaErrores.add("La direcci�n de la empresa es obligatoria");
			}
			if (!Validaciones.esEnteroPositivo(miEmpresa.getCodigoRubro())) {
				listaErrores.add("El rubro de la empresa es obligatorio");
			}
			if (!Validaciones.esEnteroPositivo(miEmpresa.getComision())) {
				listaErrores.add("La comisi�n para la empresa es obligatoria");
			}
			if (!Validaciones.esCorreo(miEmpresa.getCorreo())) {
				listaErrores.add("El correo de la empresa es obligatorio");
			}
			if (listaErrores.size() > 0) {
				request.setAttribute("listaErrores", listaErrores);
				request.setAttribute("empresa", miEmpresa);
				request.getRequestDispatcher("administrador.do?op=obtenerpe&id=" + miEmpresa.getCodigo()).forward(request,
						response);
			} else {
				if (modelo.modificarEmpresa(miEmpresa) > 0) {
					request.getSession().setAttribute("exito", "Empresa modificada exitosamente");
					response.sendRedirect(request.getContextPath() + "/administrador.do?op=listare");
				} else {
					request.getSession().setAttribute("fracaso", "La empresa no ha sido modificada. " + "Hubo un error");
					response.sendRedirect(request.getContextPath() + "/administrador.do?op=listare");
				}
			}
		} catch (IOException | SQLException | ServletException ex) {
			Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void eliminarRubro(HttpServletRequest request, HttpServletResponse response) {
		try {
			String codigo = request.getParameter("id");
			if (modelo.eliminarRubro(codigo) > 0) {
				request.setAttribute("exito", "Rubro eliminado exitosamente");

			} else {
				request.setAttribute("fracaso", "No se puede eliminar este rubro");
			}
			request.getRequestDispatcher("/administrador.do?op=listarr").forward(request, response);
		} catch (SQLException | ServletException | IOException ex) {
			Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response) {
		try {
			String codigo = request.getParameter("id");
			if (modelo.eliminarUsuario(codigo) > 0) {
				request.setAttribute("exito", "Cliente eliminado exitosamente");

			} else {
				request.setAttribute("fracaso", "No se puede eliminar este cliente");
			}
			request.getRequestDispatcher("/administrador.do?op=listaru").forward(request, response);
		} catch (SQLException | ServletException | IOException ex) {
			Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	private void eliminarEmpresa(HttpServletRequest request, HttpServletResponse response) {
		try {
			String codigo = request.getParameter("id");
			if (modelo.eliminarEmpresa(codigo) > 0) {
				request.setAttribute("exito", "Empresa eliminada exitosamente");

			} else {
				request.setAttribute("fracaso", "No se puede eliminar esta empresa");
			}
			request.getRequestDispatcher("/administrador.do?op=listare").forward(request, response);
		} catch (SQLException | ServletException | IOException ex) {
			Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	private void aceptarOferta(HttpServletRequest request, HttpServletResponse response) {
		try {
			int codigo = Integer.parseInt(request.getParameter("id"));
			if (modelo.aceptarOferta(codigo) > 0) {
				request.setAttribute("exito", "Oferta aceptada exitosamente");
			} else {
				request.setAttribute("fracaso", "No se puede aceptar esta oferta");
			}
			request.getRequestDispatcher("/administrador.do?op=listaro").forward(request, response);
		} catch (SQLException | IOException | ServletException ex) {
			Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	private void descartarOferta(HttpServletRequest request, HttpServletResponse response) {
		try {
			int codigo = Integer.parseInt(request.getParameter("id"));
			if (modelo.descartarOferta(codigo) > 0) {
				request.setAttribute("exito", "Oferta descartada exitosamente");
			} else {
				request.setAttribute("fracaso", "No se puede descartar esta oferta");
			}
			request.getRequestDispatcher("/administrador.do?op=listaro").forward(request, response);
		} catch (SQLException | IOException | ServletException ex) {
			Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}*/
}