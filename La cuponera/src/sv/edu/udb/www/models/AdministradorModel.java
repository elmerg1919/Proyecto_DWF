package sv.edu.udb.www.models;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import sv.edu.udb.www.beans.Cupon;
import sv.edu.udb.www.beans.Empresa;
import sv.edu.udb.www.beans.Oferta;
import sv.edu.udb.www.beans.Rubro;
import sv.edu.udb.www.beans.Usuario;

public class AdministradorModel extends Conexion {
	
	
	public Usuario validarLogin(String correo, String password) throws SQLException {
		try {
			String sql = "SELECT *  FROM usuarios where correo = ? and password = ? and tipo=\"Administrador\" and estado=1";
			this.conectar();
			st = conexion.prepareStatement(sql);
			st.setString(1, correo);
			st.setString(2, password);
			rs = st.executeQuery();
			if (rs.next()) {
				Usuario usuario = new Usuario();
				usuario.setDui(rs.getString("dui"));// nombre de los campos de la bdd
				usuario.setNombres(rs.getString("nombres"));
				usuario.setApellidos(rs.getString("apellidos"));
				usuario.setTelefono(rs.getInt("telefono"));
				usuario.setDireccion(rs.getString("direccion"));
				usuario.setTipo(rs.getString("tipo"));
				usuario.setEstado(rs.getInt("estado"));
				usuario.setCorreo(rs.getString("correo"));
				usuario.setPassword(rs.getString("password"));
				this.desconectar();
				return usuario;
			}
			this.desconectar();
			return null;
		} catch (SQLException ex) {
			Logger.getLogger(AdministradorModel.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		} finally {
			this.desconectar();
		}
	}
	
	public int actualizarVencimientosOfertas(String fechaActual) throws SQLException {
		try {
			int filasAfectadas = 0;
			String sql = "UPDATE ofertas SET estado = \"Vencida\" WHERE fechaFinal <= ?";
			this.conectar();
			st = conexion.prepareStatement(sql);
			st.setString(1, fechaActual);
			filasAfectadas = st.executeUpdate();
			this.desconectar();
			return filasAfectadas;
		} catch (SQLException ex) {
			Logger.getLogger(EmpresaModel.class.getName()).log(Level.SEVERE, null, ex);
			this.desconectar();
			return 0;
		}
	}
	
	public int actualizarVencimientosCupones(String fechaActual) throws SQLException {
		try {
			int filasAfectadas = 0;
			String sql = "UPDATE cupones SET estado = \"Vencido\" WHERE fechaVencimiento <= ?";
			this.conectar();
			st = conexion.prepareStatement(sql);
			st.setString(1, fechaActual);
			filasAfectadas = st.executeUpdate();
			this.desconectar();
			return filasAfectadas;
		} catch (SQLException ex) {
			Logger.getLogger(AdministradorModel.class.getName()).log(Level.SEVERE, null, ex);
			this.desconectar();
			return 0;
		}
	}

	public int recuperarPassword(String correo) throws SQLException {
		try {
			int filasAfectadas = 0;
			String newpassword = generarClave();
			String sql = "UPDATE usuarios SET password = ? WHERE correo = ?";
			this.conectar();
			st = conexion.prepareStatement(sql);
			st.setString(1,newpassword);
			st.setString(2, correo);
			filasAfectadas = st.executeUpdate();
			this.desconectar();
			if(filasAfectadas > 0 ) {
				sendRecoveryEmail(correo, newpassword);
			}
			return filasAfectadas;
		} catch (SQLException ex) {
			Logger.getLogger(AdministradorModel.class.getName()).log(Level.SEVERE, null, ex);
			this.desconectar();
			return 0;
		}
	}
	
	//RECUPERACION DE CORREO
	
	public int sendRecoveryEmail(String recipient, String key) {
		// para enviar la clave al correo
		Properties properties = new Properties();
		properties.setProperty("mail.smtp.host", "smtp.gmail.com");
		properties.setProperty("mail.smtp.starttls.enable", "true");
		properties.setProperty("mail.smtp.port", "587");
		properties.setProperty("mail.smtp.auth", "true");
		Session sesion = Session.getDefaultInstance(properties);
		String emisor = "couponstoresv@gmail.com";
		String password = "6Avq*V$$gQ35=tJoKPeofd";
		String asunto = "RECUPERA TU CONTRASE�A";
		MimeMessage mail = new MimeMessage(sesion);
		String mensaje = "Hemos recibido una solicitud para recuperar tu contrase\\u00f1a. Para iniciar sesi\\u00D3n  nuevamente, utiliza esta nueva contrase\\u00f1a: " + key;
		try {
			mail.setFrom(new InternetAddress(emisor));
			mail.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
			mail.setSubject(asunto);
			mail.setText(mensaje);
			Transport transport = sesion.getTransport("smtp");
			transport.connect(emisor, password);
			transport.sendMessage(mail, mail.getRecipients(Message.RecipientType.TO));
			transport.close();
			return 1;
		} catch (AddressException ex) {
			Logger.getLogger(AdministradorModel.class.getName()).log(Level.SEVERE, null, ex);
			return 0;
		} catch (MessagingException ex) {
			Logger.getLogger(AdministradorModel.class.getName()).log(Level.SEVERE, null, ex);
			return 0;
		}
	}
	
	
	
	public List<Usuario> listarUsuarios() throws SQLException {
		try {
			List<Usuario> lista = new ArrayList<>();
			String sql = "CALL sp_listarUsuarios()";
			this.conectar();
			cs = conexion.prepareCall(sql);
			rs = cs.executeQuery();
			String estado = "";
			while (rs.next()) {
				Usuario usuario = new Usuario();
				usuario.setDui(rs.getString("dui"));// nombre de los campos de la bdd
				usuario.setNombres(rs.getString("nombres"));
				usuario.setApellidos(rs.getString("apellidos"));
				usuario.setTelefono(rs.getInt("telefono"));
				usuario.setDireccion(rs.getString("direccion"));
				usuario.setTipo(rs.getString("tipo"));
				if (rs.getInt("estado") == 0) {
					estado = "Inactivo";
				}
				if (rs.getInt("estado") == 1) {
					estado = "Activo";
				}
				usuario.setStringEstado(estado);
				usuario.setCorreo(rs.getString("correo"));
				lista.add(usuario);
			}
			this.desconectar();
			return lista;
		} catch (SQLException ex) {
			Logger.getLogger(AdministradorModel.class.getName()).log(Level.SEVERE, null, ex);
			this.desconectar();
			return null;
		}
	}
	

	public List<Empresa> listarEmpresas() throws SQLException {
		try {
			List<Empresa> lista = new ArrayList<>();
			String sql = "CALL sp_listarEmpresas()";
			this.conectar();
			cs = conexion.prepareCall(sql);
			rs = cs.executeQuery();
			while (rs.next()) {
				Empresa empresa = new Empresa();
				// nombres de los campos de la bdd
				empresa.setCodigo(rs.getString("codigo"));
				empresa.setNombre(rs.getString("nombre"));
				empresa.setContacto(rs.getString("contacto"));
				empresa.setDireccion(rs.getString("direccion"));
				empresa.setCorreo(rs.getString("correo"));
				empresa.setStringCodigoRubro(rs.getString("nombreRubro"));
				empresa.setComision(rs.getInt("comision"));
				empresa.setCodigoRubro(rs.getInt("codigoRubro"));
				empresa.setTelefono(rs.getInt("telefono"));
				empresa.setCorrelativo(rs.getInt("correlativo"));
				lista.add(empresa);
			}
			this.desconectar();
			return lista;
		} catch (SQLException ex) {
			Logger.getLogger(AdministradorModel.class.getName()).log(Level.SEVERE, null, ex);
			this.desconectar();
			return null;
		}
	}

	public List<Rubro> listarRubros() throws SQLException {
		try {
			List<Rubro> lista = new ArrayList<>();
			String sql = "CALL sp_listarRubros()";
			this.conectar();
			cs = conexion.prepareCall(sql);
			rs = cs.executeQuery();
			while (rs.next()) {
				Rubro rubro = new Rubro();
				rubro.setCodigo(rs.getInt("codigo"));// nombre de los campos de la bdd
				rubro.setNombre(rs.getString("nombre"));
				rubro.setDescripcion(rs.getString("descripcion"));
				lista.add(rubro);
			}
			this.desconectar();
			return lista;
		} catch (SQLException ex) {
			Logger.getLogger(AdministradorModel.class.getName()).log(Level.SEVERE, null, ex);
			this.desconectar();
			return null;
		}
	}

	public int totalUsuarios() throws SQLException {
		try {
			int totalUsuarios = 0;
			String sql = "SELECT COUNT(dui) as totalusuarios FROM usuarios where tipo=\"cliente\"";
			this.conectar();
			st = conexion.prepareStatement(sql);
			rs = st.executeQuery();
			while (rs.next()) {
				totalUsuarios = rs.getInt("totalusuarios");
			}
			return totalUsuarios;
		} catch (SQLException ex) {
			Logger.getLogger(AdministradorModel.class.getName()).log(Level.SEVERE, null, ex);
			return 0;
		} finally {
			this.desconectar();
		}
	}

	public int totalEmpresas() throws SQLException {
		try {
			int totalEmpresas = 0;
			String sql = "SELECT COUNT(codigo) as totalempresas FROM empresasofertantes";
			this.conectar();
			st = conexion.prepareStatement(sql);
			rs = st.executeQuery();
			while (rs.next()) {
				totalEmpresas = rs.getInt("totalempresas");
			}
			return totalEmpresas;
		} catch (SQLException ex) {
			Logger.getLogger(AdministradorModel.class.getName()).log(Level.SEVERE, null, ex);
			return 0;
		} finally {
			this.desconectar();
		}
	}

	public int totalRubros() throws SQLException {
		try {
			int totalRubros = 0;
			String sql = "SELECT COUNT(codigo) as totalRubros FROM rubros";
			this.conectar();
			st = conexion.prepareStatement(sql);
			rs = st.executeQuery();
			while (rs.next()) {
				totalRubros = rs.getInt("totalRubros");
			}
			return totalRubros;
		} catch (SQLException ex) {
			Logger.getLogger(AdministradorModel.class.getName()).log(Level.SEVERE, null, ex);
			return 0;
		} finally {
			this.desconectar();
		}
	}
	
	public int totalOfertasPendientes() throws SQLException {
		try {
			int totalOfertasPendientes = 0;
			String sql = "SELECT COUNT(codigo) as totalOfertasPendientes FROM ofertas WHERE estado ='Pendiente'";
			this.conectar();
			st = conexion.prepareStatement(sql);
			rs = st.executeQuery();
			while (rs.next()) {
				totalOfertasPendientes = rs.getInt("totalOfertasPendientes");
			}
			return totalOfertasPendientes;
		} catch (SQLException ex) {
			Logger.getLogger(AdministradorModel.class.getName()).log(Level.SEVERE, null, ex);
			return 0;
		} finally {
			this.desconectar();
		}
	}
	
	public int totalOfertas() throws SQLException {
		try {
			int totalOfertas = 0;
			String sql = "SELECT COUNT(codigo) as totalOfertas FROM ofertas";
			this.conectar();
			st = conexion.prepareStatement(sql);
			rs = st.executeQuery();
			while (rs.next()) {
				totalOfertas = rs.getInt("totalOfertas");
			}
			return totalOfertas;
		} catch (SQLException ex) {
			Logger.getLogger(AdministradorModel.class.getName()).log(Level.SEVERE, null, ex);
			return 0;
		} finally {
			this.desconectar();
		}
	}

	public String obtenerCodigoEmpresa() throws SQLException {
		try {
			String codigo = "";
			String numero = "";
			String ceros = "";
			String sql = "SELECT max(correlativo+1) as maximo FROM `empresasofertantes`";
			this.conectar();
			st = conexion.prepareStatement(sql);
			rs = st.executeQuery();
			while (rs.next()) {
				numero = rs.getString("maximo");
			}
			int size = numero.length();
			if (size == 1) {
				ceros = "00";
			} else if (size == 2) {
				ceros = "0";
			} else if (size == 3) {
				ceros = "";
			}
			codigo = "EMP" + ceros + numero;
			return codigo;
		} catch (SQLException ex) {
			Logger.getLogger(AdministradorModel.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		} finally {
			this.desconectar();
		}
	}

	public Usuario obtenerUsuario(String codigo) throws SQLException {
		try {
			String sql = "CALL sp_obtenerUsuario(?)";
			this.conectar();
			cs = conexion.prepareCall(sql);
			cs.setString(1, codigo);
			rs = cs.executeQuery();
			String estado = "";
			if (rs.next()) {
				Usuario usuario = new Usuario();
				usuario.setDui(rs.getString("dui"));// nombre de los campos de la bdd
				usuario.setNombres(rs.getString("nombres"));
				usuario.setApellidos(rs.getString("apellidos"));
				usuario.setTelefono(rs.getInt("telefono"));
				usuario.setDireccion(rs.getString("direccion"));
				usuario.setTipo(rs.getString("tipo"));
				if (rs.getInt("estado") == 0) {
					estado = "Inactivo";
				}
				if (rs.getInt("estado") == 1) {
					estado = "Activo";
				}
				usuario.setStringEstado(estado);
				usuario.setEstado(rs.getInt("estado"));
				usuario.setCorreo(rs.getString("correo"));
				this.desconectar();
				return usuario;
			}
			this.desconectar();
			return null;
		} catch (SQLException ex) {
			Logger.getLogger(AdministradorModel.class.getName()).log(Level.SEVERE, null, ex);
			this.desconectar();
			return null;
		}

	}

	public Empresa obtenerEmpresa(String codigo) throws SQLException {
		try {
			String sql = "CALL sp_obtenerEmpresa(?)";
			this.conectar();
			cs = conexion.prepareCall(sql);
			cs.setString(1, codigo);
			rs = cs.executeQuery();
			if (rs.next()) {
				Empresa empresa = new Empresa();
				// nombres de los campos de la bdd
				empresa.setCodigo(rs.getString("codigo"));
				empresa.setNombre(rs.getString("nombre"));
				empresa.setContacto(rs.getString("contacto"));
				empresa.setDireccion(rs.getString("direccion"));
				empresa.setCorreo(rs.getString("correo"));
				empresa.setStringCodigoRubro(rs.getString("nombreRubro"));
				empresa.setComision(rs.getInt("comision"));
				empresa.setCodigoRubro(rs.getInt("codigoRubro"));
				empresa.setTelefono(rs.getInt("telefono"));
				empresa.setCorrelativo(rs.getInt("correlativo"));
				this.desconectar();
				return empresa;
			}
			this.desconectar();
			return null;
		} catch (SQLException ex) {
			Logger.getLogger(AdministradorModel.class.getName()).log(Level.SEVERE, null, ex);
			this.desconectar();
			return null;
		}

	}
	
	public Oferta obtenerOferta(String codigo) throws SQLException {
		try {
			String sql = "CALL sp_obtenerOferta(?)";
			this.conectar();
			cs = conexion.prepareCall(sql);
			cs.setString(1, codigo);
			rs = cs.executeQuery();
			if (rs.next()) {
				Oferta oferta = new Oferta();
				// nombres de los campos de la bdd
				oferta.setTitulo(rs.getString("titulo"));
				oferta.setDescripcion(rs.getString("descripcion"));
				oferta.setDetalles(rs.getString("detalles"));
				oferta.setCodigoEmpresa(rs.getString("codigoEmpresa"));
				oferta.setStringCodigoEmpresa(rs.getString("nombreEmpresa"));
				oferta.setIngresosTotales(rs.getString("cuponesVendidos"), rs.getDouble("precio"));
				oferta.setCargoPorServicio(rs.getString("comisionEmpresa"));
				oferta.setEstado(rs.getString("estado"));
				oferta.setFechaInicio(rs.getString("fechaInicio"));
				oferta.setFechaFinal(rs.getString("fechaFinal"));
				oferta.setFechaFinalCupon(rs.getString("fechaFinalCupon"));
				oferta.setCodigo(rs.getInt("codigo"));
				oferta.setCuponesIniciales(rs.getInt("cuponesIniciales"));
				oferta.setCuponesDisponibles(rs.getInt("cuponesDisponibles"));
				oferta.setCuponesVendidos(rs.getInt("cuponesVendidos"));
				oferta.setPrecio(rs.getDouble("precio"));
				oferta.setJustificacion(rs.getString("justificacion"));
				return oferta;
			}
			this.desconectar();
			return null;
		} catch (SQLException ex) {
			Logger.getLogger(AdministradorModel.class.getName()).log(Level.SEVERE, null, ex);
			this.desconectar();
			return null;
		}

	}

	public Rubro obtenerRubro(String codigo) throws SQLException {
		try {
			String sql = "CALL sp_obtenerRubro(?)";
			this.conectar();
			cs = conexion.prepareCall(sql);
			cs.setString(1, codigo);
			rs = cs.executeQuery();
			if (rs.next()) {
				Rubro rubro = new Rubro();
				rubro.setCodigo(rs.getInt("codigo"));
				rubro.setNombre(rs.getString("nombre"));
				rubro.setDescripcion(rs.getString("descripcion"));
				this.desconectar();
				return rubro;
			}
			this.desconectar();
			return null;
		} catch (SQLException ex) {
			Logger.getLogger(AdministradorModel.class.getName()).log(Level.SEVERE, null, ex);
			this.desconectar();
			return null;
		}

	}

	public List<Cupon> listarCupones(String codigo) throws SQLException {
		try {
			List<Cupon> lista = new ArrayList<>();
			String sql = "CALL sp_obtenerCupones(?)";
			this.conectar();
			cs = conexion.prepareCall(sql);
			cs.setString(1, codigo);
			rs = cs.executeQuery();
			while (rs.next()) {
				Cupon cupon = new Cupon();
				cupon.setCodigo(rs.getString("codigo"));// nombre de los campos de la bdd
				cupon.setTituloOferta(rs.getString("Oferta"));
				cupon.setEstado(rs.getString("estado"));
				lista.add(cupon);
			}
			this.desconectar();
			return lista;
		} catch (SQLException ex) {
			Logger.getLogger(AdministradorModel.class.getName()).log(Level.SEVERE, null, ex);
			this.desconectar();
			return null;
		}
	}

	public List<Oferta> listarOfertas(String codigo) throws SQLException {
		try {
			List<Oferta> lista = new ArrayList<>();
			String sql = "CALL sp_listarOfertasDeEmpresa(?)";
			this.conectar();
			cs = conexion.prepareCall(sql);
			cs.setString(1, codigo);
			rs = cs.executeQuery();
			while (rs.next()) {
				Oferta oferta = new Oferta();
				oferta.setTitulo(rs.getString("titulo"));
				oferta.setDescripcion(rs.getString("descripcion"));
				oferta.setDetalles(rs.getString("detalles"));
				oferta.setCodigoEmpresa(rs.getString("codigoEmpresa"));
				oferta.setEstado(rs.getString("estado"));
				oferta.setFechaInicio(rs.getString("fechaInicio"));
				oferta.setFechaFinal(rs.getString("fechaFinal"));
				oferta.setFechaFinalCupon(rs.getString("fechaFinalCupon"));
				oferta.setCodigo(rs.getInt("codigo"));
				oferta.setCuponesIniciales(rs.getInt("cuponesIniciales"));
				oferta.setCuponesDisponibles(rs.getInt("cuponesDisponibles"));
				oferta.setCuponesVendidos(rs.getInt("cuponesVendidos"));
				oferta.setPrecio(rs.getDouble("precio"));
				lista.add(oferta);
			}
			this.desconectar();
			return lista;
		} catch (SQLException ex) {
			Logger.getLogger(AdministradorModel.class.getName()).log(Level.SEVERE, null, ex);
			this.desconectar();
			return null;
		}
	}
	
	public List<Oferta> listarOfertasPendientes() throws SQLException {
		try {
			List<Oferta> lista = new ArrayList<>();
			String sql = "CALL sp_listarOfertasPendientes()";
			this.conectar();
			cs = conexion.prepareCall(sql);
			rs = cs.executeQuery();
			while (rs.next()) {
				Oferta oferta = new Oferta();
				// nombres de los campos de la bdd
				oferta.setTitulo(rs.getString("titulo"));
				oferta.setDescripcion(rs.getString("descripcion"));
				oferta.setDetalles(rs.getString("detalles"));
				oferta.setCodigoEmpresa(rs.getString("codigoEmpresa"));
				oferta.setStringCodigoEmpresa(rs.getString("nombreEmpresa"));
				oferta.setEstado(rs.getString("estado"));
				oferta.setFechaInicio(rs.getString("fechaInicio"));
				oferta.setFechaFinal(rs.getString("fechaFinal"));
				oferta.setFechaFinalCupon(rs.getString("fechaFinalCupon"));
				oferta.setCodigo(rs.getInt("codigo"));
				oferta.setCuponesIniciales(rs.getInt("cuponesIniciales"));
				oferta.setCuponesDisponibles(rs.getInt("cuponesDisponibles"));
				oferta.setCuponesVendidos(rs.getInt("cuponesVendidos"));
				oferta.setPrecio(rs.getDouble("precio"));
				lista.add(oferta);
			}
			this.desconectar();
			return lista;
		} catch (SQLException ex) {
			Logger.getLogger(AdministradorModel.class.getName()).log(Level.SEVERE, null, ex);
			this.desconectar();
			return null;
		}
	}
	
	public List<Oferta> listarOfertas() throws SQLException {
		try {
			List<Oferta> lista = new ArrayList<>();
			String sql = "CALL sp_listarOfertas()";
			this.conectar();
			cs = conexion.prepareCall(sql);
			rs = cs.executeQuery();
			while (rs.next()) {
				Oferta oferta = new Oferta();
				// nombres de los campos de la bdd
				oferta.setTitulo(rs.getString("titulo"));
				oferta.setDescripcion(rs.getString("descripcion"));
				oferta.setDetalles(rs.getString("detalles"));
				oferta.setCodigoEmpresa(rs.getString("codigoEmpresa"));
				oferta.setStringCodigoEmpresa(rs.getString("nombreEmpresa"));
				oferta.setEstado(rs.getString("estado"));
				oferta.setFechaInicio(rs.getString("fechaInicio"));
				oferta.setFechaFinal(rs.getString("fechaFinal"));
				oferta.setFechaFinalCupon(rs.getString("fechaFinalCupon"));
				oferta.setCodigo(rs.getInt("codigo"));
				oferta.setCuponesIniciales(rs.getInt("cuponesIniciales"));
				oferta.setCuponesDisponibles(rs.getInt("cuponesDisponibles"));
				oferta.setCuponesVendidos(rs.getInt("cuponesVendidos"));
				oferta.setPrecio(rs.getDouble("precio"));
				lista.add(oferta);
			}
			this.desconectar();
			return lista;
		} catch (SQLException ex) {
			Logger.getLogger(AdministradorModel.class.getName()).log(Level.SEVERE, null, ex);
			this.desconectar();
			return null;
		}
	}

	public int insertarRubro(Rubro rubro) throws SQLException {
		try {
			int filasAfectadas = 0;
			String sql = "CALL sp_insertarRubro(?,?)";
			this.conectar();
			cs = conexion.prepareCall(sql);
			cs.setString(1, rubro.getNombre());
			cs.setString(2, rubro.getDescripcion());
			filasAfectadas = cs.executeUpdate();
			this.desconectar();
			return filasAfectadas;
		} catch (SQLException ex) {
			Logger.getLogger(AdministradorModel.class.getName()).log(Level.SEVERE, null, ex);
			this.desconectar();
			return 0;
		}
	}

	public int insertarEmpresa(Empresa empresa) throws SQLException {
		try {
			int filasAfectadas = 0;
			String clave = generarClave();
			String sql = "CALL sp_insertarEmpresa(?,?,?,?,?,?,?,?,?)";
			this.conectar();
			cs = conexion.prepareCall(sql);
			cs.setString(1, empresa.getCodigo());
			cs.setString(2, empresa.getNombre());
			cs.setInt(3, empresa.getTelefono());
			cs.setString(4, empresa.getContacto());
			cs.setString(5, empresa.getDireccion());
			cs.setInt(6, empresa.getComision());
			cs.setInt(7, empresa.getCodigoRubro());
			cs.setString(8, empresa.getCorreo());
			cs.setString(9, clave);
			filasAfectadas = cs.executeUpdate();
			if (filasAfectadas > 0) {
				String correo = empresa.getCorreo();
				enviarCorreo(correo, clave);
			}
			this.desconectar();
			return filasAfectadas;
		} catch (SQLException ex) {
			Logger.getLogger(AdministradorModel.class.getName()).log(Level.SEVERE, null, ex);
			this.desconectar();
			return 0;
		}
	}

	public int enviarCorreo(String receptor, String clave) {
		// para enviar la clave al correo
		Properties properties = new Properties();
		properties.setProperty("mail.smtp.host", "smtp.gmail.com");
		properties.setProperty("mail.smtp.starttls.enable", "true");
		properties.setProperty("mail.smtp.port", "587");
		properties.setProperty("mail.smtp.auth", "true");
		Session sesion = Session.getDefaultInstance(properties);
		String emisor = "couponstoresv@gmail.com";
		String password = "6Avq*V$$gQ35=tJoKPeofd";
		String asunto = "TE DAMOS LA BIENVENIDA";
		String mensaje = "Te enviamos una clave para que puedas iniciar sesion en nuestro sistema. Tu clave es: "
				+ clave;
		MimeMessage mail = new MimeMessage(sesion);
		try {
			mail.setFrom(new InternetAddress(emisor));
			mail.addRecipient(Message.RecipientType.TO, new InternetAddress(receptor));
			mail.setSubject(asunto);
			mail.setText(mensaje);
			Transport transport = sesion.getTransport("smtp");
			transport.connect(emisor, password);
			transport.sendMessage(mail, mail.getRecipients(Message.RecipientType.TO));
			transport.close();
			return 1;
		} catch (AddressException ex) {
			Logger.getLogger(AdministradorModel.class.getName()).log(Level.SEVERE, null, ex);
			return 0;
		} catch (MessagingException ex) {
			Logger.getLogger(AdministradorModel.class.getName()).log(Level.SEVERE, null, ex);
			return 0;
		}
	}

	public String generarClave() {

		// UUID Unique Universal Identifier
		String clave = "";
		clave = UUID.randomUUID().toString().toUpperCase().substring(0, 6);
		
		return clave;

	}

	public int modificarRubro(Rubro rubro) throws SQLException {
		try {
			int filasAfectadas = 0;
			String sql = "UPDATE rubros SET nombre = ?, descripcion = ? WHERE codigo = ?";
			this.conectar();
			st = conexion.prepareStatement(sql);
			st.setString(1, rubro.getNombre());
			st.setString(2, rubro.getDescripcion());
			st.setInt(3, rubro.getCodigo());
			filasAfectadas = st.executeUpdate();
			this.desconectar();
			return filasAfectadas;
		} catch (SQLException ex) {
			Logger.getLogger(AdministradorModel.class.getName()).log(Level.SEVERE, null, ex);
			this.desconectar();
			return 0;

		}
	}
	
	public int modificarPassword(String codigo, String newpassword) throws SQLException {
		try {
			int filasAfectadas = 0;
			String sql = "UPDATE usuarios SET password = ? WHERE dui = ?";
			this.conectar();
			st = conexion.prepareStatement(sql);
			st.setString(1,newpassword);
			st.setString(2, codigo);
			filasAfectadas = st.executeUpdate();
			this.desconectar();
			return filasAfectadas;
		} catch (SQLException ex) {
			Logger.getLogger(AdministradorModel.class.getName()).log(Level.SEVERE, null, ex);
			this.desconectar();
			return 0;

		}
	}

	public int modificarEmpresa(Empresa empresa) throws SQLException {
		try {
			int filasAfectadas = 0;
			String sql = "UPDATE empresasofertantes SET nombre = ?, contacto = ?, telefono = ?, direccion = ?, codigoRubro = ?, comision = ?, correo = ? WHERE codigo = ?";
			this.conectar();
			st = conexion.prepareStatement(sql);
			st.setString(1, empresa.getNombre());
			st.setString(2, empresa.getContacto());
			st.setInt(3, empresa.getTelefono());
			st.setString(4, empresa.getDireccion());
			st.setInt(5, empresa.getCodigoRubro());
			st.setInt(6, empresa.getComision());
			st.setString(7, empresa.getCorreo());
			st.setString(8, empresa.getCodigo());
			filasAfectadas = st.executeUpdate();
			this.desconectar();
			return filasAfectadas;
		} catch (SQLException ex) {
			Logger.getLogger(AdministradorModel.class.getName()).log(Level.SEVERE, null, ex);
			this.desconectar();
			return 0;

		}
	}

	public int eliminarUsuario(String codigo) throws SQLException {
		try {
			int filasAfectadas = 0;
			String sql = "CALL sp_eliminarUsuario(?)";
			this.conectar();
			cs = conexion.prepareCall(sql);
			cs.setString(1, codigo);
			filasAfectadas = cs.executeUpdate();
			this.desconectar();
			return filasAfectadas;
		} catch (SQLException ex) {
			Logger.getLogger(AdministradorModel.class.getName()).log(Level.SEVERE, null, ex);
			this.desconectar();
			return 0;
		}

	}

	public int eliminarRubro(String codigo) throws SQLException {
		try {
			int filasAfectadas = 0;
			String sql = "CALL sp_eliminarRubro(?)";
			this.conectar();
			cs = conexion.prepareCall(sql);
			cs.setString(1, codigo);
			filasAfectadas = cs.executeUpdate();
			this.desconectar();
			return filasAfectadas;
		} catch (SQLException ex) {
			Logger.getLogger(AdministradorModel.class.getName()).log(Level.SEVERE, null, ex);
			this.desconectar();
			return 0;
		}

	}

	public int eliminarEmpresa(String codigo) throws SQLException {
		try {
			int filasAfectadas = 0;
			String sql = "CALL sp_eliminarEmpresa(?)";
			this.conectar();
			cs = conexion.prepareCall(sql);
			cs.setString(1, codigo);
			filasAfectadas = cs.executeUpdate();
			this.desconectar();
			return filasAfectadas;
		} catch (SQLException ex) {
			Logger.getLogger(AdministradorModel.class.getName()).log(Level.SEVERE, null, ex);
			this.desconectar();
			return 0;
		}

	}
	
	public int aceptarOferta(int codigo) throws SQLException {
		try {
			int filasAfectadas = 0;
			String sql = "UPDATE ofertas SET estado = 'Aprobada' WHERE codigo = ?";
			this.conectar();
			st = conexion.prepareStatement(sql);
			st.setInt(1, codigo);
			filasAfectadas = st.executeUpdate();
			this.desconectar();
			return filasAfectadas;
		} catch (SQLException ex) {
			Logger.getLogger(AdministradorModel.class.getName()).log(Level.SEVERE, null, ex);
			this.desconectar();
			return 0;

		}
	}
	
	public int descartarOferta(int codigo, String justificacion) throws SQLException {
		try {
			int filasAfectadas = 0;
			String sql = "UPDATE ofertas SET estado = 'Descartada', justificacion = ?  WHERE codigo = ?";
			this.conectar();
			st = conexion.prepareStatement(sql);
			st.setString(1, justificacion);
			st.setInt(2, codigo);
			filasAfectadas = st.executeUpdate();
			this.desconectar();
			return filasAfectadas;
		} catch (SQLException ex) {
			Logger.getLogger(AdministradorModel.class.getName()).log(Level.SEVERE, null, ex);
			this.desconectar();
			return 0;

		}
	}

}
