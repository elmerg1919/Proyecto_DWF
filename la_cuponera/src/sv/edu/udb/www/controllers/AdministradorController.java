

/*
	VALIDACION
*/
package sv.edu.udb.www.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import sv.edu.udb.www.beans.Usuario;
import sv.edu.udb.www.beans.Cupon;
import sv.edu.udb.www.beans.Empresa;
import sv.edu.udb.www.beans.Oferta;
import sv.edu.udb.www.beans.Rubro;
import sv.edu.udb.www.models.AdministradorModel;
import sv.edu.udb.www.utils.Validaciones;

/**
 *
 * @author Melissa Viana
 */
@WebServlet(name = "AdministradorController", urlPatterns = { "/administrador.do" })
public class AdministradorController extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<String> listaErrores = new ArrayList<>();
	AdministradorModel modelo = new AdministradorModel();

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		response.setContentType("text/html;charset=UTF-8");
		try (PrintWriter out = response.getWriter()) {
			if (request.getParameter("op") == "" || request.getParameter("op") == null) {
				request.getRequestDispatcher("/administrador/indexAdministrador.jsp").forward(request, response);
				return;
			}
			String operacion = request.getParameter("op");

			switch (operacion) {
			case "listaru":
				listarUsuarios(request, response);
				break;
			case "listarr":
				listarRubros(request, response);
				break;
			case "listare":
				listarEmpresas(request, response);
				break;
			case "listaro":
				listarOfertas(request, response);
				break;
			case "nuevor":
				request.getRequestDispatcher("/administrador/nuevoRubro.jsp").forward(request, response);
				break;
			case "nuevae":
				nuevaEmpresa(request, response);
				break;
			case "insertarr":
				insertarRubro(request, response);
				break;
			case "insertare":
				insertarEmpresa(request, response);
				break;
			case "obtener":
				obtener(request, response);
				break;
			case "obtenere":
				obtenerEmpresa(request, response);
				break;
			case "obtenerr":
				obtenerRubro(request, response);
				break;
			case "obtenerpe":
				obtenerEmpresaParaEditar(request, response);
				break;
			case "obtenero":
				obtenerOferta(request, response);
				break;
			case "modificarr":
				modificarRubro(request, response);
				break;
			case "modificare":
				modificarEmpresa(request, response);
				break;
			case "eliminarr":
				eliminarRubro(request, response);
				break;
			case "eliminaru":
				eliminarUsuario(request, response);
				break;
			case "eliminare":
				eliminarEmpresa(request, response);
				break;
			case "aceptar":
				aceptarOferta(request, response);
				break;
			case "descartar":
				descartarOferta(request, response);
				break;
			case "cambiarp":
				modificarPassword(request, response);
				break;
			case "validarl":
				validarLogin(request, response);
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
	
	private void recuperarPassword(HttpServletRequest request, HttpServletResponse response) {
		try {
			listaErrores.clear();
			String correo = request.getParameter("correo");
			
			if (Validaciones.isEmpty(correo)) {
				listaErrores.add("El correo es obligatorio para realizar la recuperación");
			}
			if (!Validaciones.esCorreo(correo)) {
				listaErrores.add("El correo tiene que ser válido.");
			}
			if (listaErrores.size() > 0) {
				request.setAttribute("listaErrores", listaErrores);
				request.getSession().setAttribute("fracaso", "El proceso de recuperación no puedo terminarse. " + " Hubo un error");
				response.sendRedirect(request.getContextPath() + "/passAdministrador.jsp");
			} else {
				if (modelo.recuperarPassword(correo) > 0) {
					request.getSession().setAttribute("exito", "Información enviada. Revisa tu bandeja de entrada");
					response.sendRedirect(request.getContextPath() + "/loginAdministrador.jsp");
				} else {
					request.getSession().setAttribute("fracaso", "El proceso de recuperación no puedo terminarse. " + " Hubo un error");
					response.sendRedirect(request.getContextPath() + "/passAdministrador.jsp");
				}
			}
		} catch (IOException | SQLException  ex) {
			Logger.getLogger(AdministradorController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	//VALIDACION DE LOGIN
	
	private void validarLogin(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		try {
			String correo = request.getParameter("correo");
			String password = request.getParameter("password");
			Usuario miUsuario = new Usuario();
			miUsuario = modelo.validarLogin(correo, password);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			String fechaActual = sdf.format(date);
			modelo.actualizarVencimientosOfertas(fechaActual);
			modelo.actualizarVencimientosCupones(fechaActual);
			if(miUsuario != null) {
				request.setAttribute("tipoUsuario", miUsuario.getTipo());
				request.setAttribute("duiUsuario", miUsuario.getDui());
				request.setAttribute("passwordUser", miUsuario.getPassword());
				String nombreCompleto = miUsuario.getNombres() + " " + miUsuario.getApellidos();
				request.setAttribute("nombreUsuario", nombreCompleto);
				request.getRequestDispatcher("/loginAdministrador.jsp").forward(request, response);
			}else {
				request.getSession().setAttribute("fracaso", "Correo o contraseña inválidos");
				response.sendRedirect(request.getContextPath() + "/loginAdministrador.jsp");
			}
			
		} catch (SQLException | ServletException | IOException ex) {
			Logger.getLogger(AdministradorController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	//SOLICITANDO INFORMACION

	private void listarUsuarios(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		try {
			request.setAttribute("listaClientes", modelo.listarUsuarios());
			request.setAttribute("totalUsuarios", modelo.totalUsuarios());
			request.getRequestDispatcher("/administrador/listaUsuarios.jsp").forward(request, response);
		} catch (SQLException | ServletException | IOException ex) {
			Logger.getLogger(AdministradorController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void listarEmpresas(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		try {
			request.setAttribute("listaEmpresas", modelo.listarEmpresas());
			request.setAttribute("totalEmpresas", modelo.totalEmpresas());
			request.getRequestDispatcher("/administrador/listaEmpresas.jsp").forward(request, response);
		} catch (SQLException | ServletException | IOException ex) {
			Logger.getLogger(AdministradorController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void listarRubros(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		try {
			request.setAttribute("listaRubros", modelo.listarRubros());
			request.setAttribute("totalRubros", modelo.totalRubros());
			request.getRequestDispatcher("/administrador/listaRubros.jsp").forward(request, response);
		} catch (SQLException | ServletException | IOException ex) {
			Logger.getLogger(AdministradorController.class.getName()).log(Level.SEVERE, null, ex);
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
			Logger.getLogger(AdministradorController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void nuevaEmpresa(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setAttribute("listaRubros", modelo.listarRubros());
			request.setAttribute("nuevoCodigo", modelo.obtenerCodigoEmpresa());
			request.getRequestDispatcher("/administrador/nuevaEmpresa.jsp").forward(request, response);
		} catch (SQLException | ServletException | IOException ex) {
			Logger.getLogger(AdministradorController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	//		ERROR EN REGISTARR RUBRO

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
				listaErrores.add("La descipción del rubro es obligatoria");
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
			Logger.getLogger(AdministradorController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	//		REGISTRO DE EMPRESAS
	
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
			
						//VALIDACIONES  DE REGISTRO DE EMPRSAS
			
			if (Validaciones.isEmpty(miEmpresa.getCodigo())) {
				listaErrores.add("El código de la empresa es obligatorio.");
			}
			if (!Validaciones.esCodigoEmpresa(miEmpresa.getCodigo())) {
				listaErrores.add("El código de la empresa debe tener un formato \"EMP###\".");
			}
			if (Validaciones.isEmpty(miEmpresa.getNombre())) {
				listaErrores.add("El nombre de la empresa es obligatorio");
			}
			if (Validaciones.isEmpty(miEmpresa.getContacto())) {
				listaErrores.add("El contacto de la empresa es obligatorio");
			}
			if (!Validaciones.esTelefono(miEmpresa.getTelefono())) {
				listaErrores.add("El teléfono de la empresa es obligatorio");
			}
			if (Validaciones.isEmpty(miEmpresa.getDireccion())) {
				listaErrores.add("La dirección de la empresa es obligatoria");
			}
			if (!Validaciones.esEnteroPositivo(miEmpresa.getCodigoRubro())) {
				listaErrores.add("El rubro de la empresa es obligatorio");
			}
			if (!Validaciones.esEnteroPositivo(miEmpresa.getComision())) {
				listaErrores.add("La comisión para la empresa es obligatoria");
			}
			if (!Validaciones.esCorreo(miEmpresa.getCorreo())) {
				listaErrores.add("El correo de la empresa es obligatorio");
			}
			
			
					// ERRORES AL REGISTRAR EMPRESA
			
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
			Logger.getLogger(AdministradorController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
		
				//LISATR USUARIOS

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
			Logger.getLogger(AdministradorController.class.getName()).log(Level.SEVERE, null, ex);
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
			Logger.getLogger(AdministradorController.class.getName()).log(Level.SEVERE, null, ex);
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
			Logger.getLogger(AdministradorController.class.getName()).log(Level.SEVERE, null, ex);
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
			Logger.getLogger(AdministradorController.class.getName()).log(Level.SEVERE, null, ex);
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
			Logger.getLogger(AdministradorController.class.getName()).log(Level.SEVERE, null, ex);
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
				listaErrores.add("El código es obligatorio");
			}
			if (Validaciones.isEmpty(miRubro.getNombre())) {
				listaErrores.add("El nombre del rubro es obligatorio");
			}
			if (Validaciones.isEmpty(miRubro.getDescripcion())) {
				listaErrores.add("La descipción del rubro es obligatoria");
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
			Logger.getLogger(AdministradorController.class.getName()).log(Level.SEVERE, null, ex);
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
				listaErrores.add("El código es obligatorio");
			}
			if (Validaciones.isEmpty(contraActual)) {
				listaErrores.add("La contraseña antigua es obligatoria");
			}
			if (Validaciones.isEmpty(actualPassword)) {
				listaErrores.add("La contraseña actual es obligatoria");
			}
			if (Validaciones.isEmpty(newPassword)) {
				listaErrores.add("La nueva contraseña es obligatoria");
			}
			
			//CAMBIAR PASSWORD
			
			if (listaErrores.size() > 0) {
				request.setAttribute("listaErrores", listaErrores);
				request.getRequestDispatcher("/administrador/cambiarPassword.jsp").forward(request, response);
			} else {
				if(contraActual.equals(actualPassword)) {
					if (modelo.modificarPassword(codigo, newPassword) > 0) {
						request.getSession().removeAttribute("passwordActual");
						request.getSession().setAttribute("passwordActual", newPassword);
						request.getSession().setAttribute("exito", "Contraseña actualizada exitosamente");
						request.getRequestDispatcher("/administrador/indexAdministrador.jsp").forward(request, response);
					} 
				}else {
					request.getSession().setAttribute("fracaso", "La contraseña no ha sido modificada. " + "Hubo un error");
					request.getRequestDispatcher("/administrador/indexAdministrador.jsp").forward(request, response);
				}
				
			}
		} catch (IOException | SQLException | ServletException ex) {
			Logger.getLogger(AdministradorController.class.getName()).log(Level.SEVERE, null, ex);
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
				listaErrores.add("El código de la empresa es obligatorio.");
			}
			if (!Validaciones.esCodigoEmpresa(miEmpresa.getCodigo())) {
				listaErrores.add("El código de la empresa debe tener un formato \"EMP###\".");
			}
			if (Validaciones.isEmpty(miEmpresa.getNombre())) {
				listaErrores.add("El nombre de la empresa es obligatorio");
			}
			if (Validaciones.isEmpty(miEmpresa.getContacto())) {
				listaErrores.add("El contacto de la empresa es obligatorio");
			}
			if (!Validaciones.esTelefono(miEmpresa.getTelefono())) {
				listaErrores.add("El teléfono de la empresa es obligatorio");
			}
			if (Validaciones.isEmpty(miEmpresa.getDireccion())) {
				listaErrores.add("La dirección de la empresa es obligatoria");
			}
			if (!Validaciones.esEnteroPositivo(miEmpresa.getCodigoRubro())) {
				listaErrores.add("El rubro de la empresa es obligatorio");
			}
			if (!Validaciones.esEnteroPositivo(miEmpresa.getComision())) {
				listaErrores.add("La comisión para la empresa es obligatoria");
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
			Logger.getLogger(AdministradorController.class.getName()).log(Level.SEVERE, null, ex);
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
			Logger.getLogger(AdministradorController.class.getName()).log(Level.SEVERE, null, ex);
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
			Logger.getLogger(AdministradorController.class.getName()).log(Level.SEVERE, null, ex);
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
			Logger.getLogger(AdministradorController.class.getName()).log(Level.SEVERE, null, ex);
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
			Logger.getLogger(AdministradorController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	private void descartarOferta(HttpServletRequest request, HttpServletResponse response) {
		try {
			int codigo = Integer.parseInt(request.getParameter("id"));
			String justificacion = request.getParameter("justificacion");
			if (modelo.descartarOferta(codigo, justificacion) > 0) {
				request.setAttribute("exito", "Oferta descartada exitosamente");
			} else {
				request.setAttribute("fracaso", "No se puede descartar esta oferta");
			}
			request.getRequestDispatcher("/administrador.do?op=listaro").forward(request, response);
		} catch (SQLException | IOException | ServletException ex) {
			Logger.getLogger(AdministradorController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}