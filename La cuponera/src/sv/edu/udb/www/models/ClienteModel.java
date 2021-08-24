package sv.edu.udb.www.models;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
//import java.util.ArrayList;
//import java.util.List;
import java.util.Properties;
import java.util.UUID;
//import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import sv.edu.udb.www.beans.Oferta;
//import sv.edu.udb.www.beans.Dependiente;
//import sv.edu.udb.www.beans.Empresa;
import sv.edu.udb.www.beans.Cupon;
import sv.edu.udb.www.beans.Usuario;

public class ClienteModel extends Conexion {

	
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
	
	public Usuario validarLogin(String correo, String password) throws SQLException {
		try {
			String sql = "SELECT *  FROM usuarios where correo = ? and password = ? and estado = 1 and tipo = \"Cliente\"";
			this.conectar();
			st = conexion.prepareStatement(sql);
			st.setString(1, correo);
			st.setString(2, password);
			rs = st.executeQuery();
			if (rs.next()) {
				Usuario usuario = new Usuario();
				usuario.setDui(rs.getString("dui"));
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
			Logger.getLogger(ClienteModel.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		} finally {
			this.desconectar();
		}
	}
	
	public int activarCuenta(String dui) throws SQLException {
		try {
			int filasAfectadas = 0;
			String sql = "UPDATE usuarios SET estado = 1 WHERE dui = ?";
			this.conectar();
			st = conexion.prepareStatement(sql);
			st.setString(1, dui);
			filasAfectadas = st.executeUpdate();
			this.desconectar();
			return filasAfectadas;
		} catch (SQLException ex) {
			Logger.getLogger(ClienteModel.class.getName()).log(Level.SEVERE, null, ex);
			this.desconectar();
			return 0;

		}
	}
	
	public String generarClave() {

		// UUID Unique Universal Identifier
		String clave = "";
		clave = UUID.randomUUID().toString().toUpperCase().substring(0, 6);
		return clave;

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
			Logger.getLogger(ClienteModel.class.getName()).log(Level.SEVERE, null, ex);
			this.desconectar();
			return 0;
		}
	}
	
	public int insertarCliente(Usuario cliente, String url) throws SQLException {
		try {
			int filasAfectadas = 0;
			String sql = "CALL sp_insertarCliente(?,?,?,?,?,?,?)";
			this.conectar();
			cs = conexion.prepareCall(sql);
			cs.setString(1, cliente.getDui());
			cs.setString(2, cliente.getNombres());
			cs.setString(3, cliente.getApellidos());
			cs.setInt(4, cliente.getTelefono());
			cs.setString(5, cliente.getDireccion());
			cs.setString(6, cliente.getCorreo());
			cs.setString(7, cliente.getPassword());
			filasAfectadas = cs.executeUpdate();
			if (filasAfectadas > 0) {
				String correo = cliente.getCorreo();
				enviarCorreo(correo, url);
			}
			this.desconectar();
			return filasAfectadas;
		} catch (SQLException ex) {
			Logger.getLogger(ClienteModel.class.getName()).log(Level.SEVERE, null, ex);
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
			Logger.getLogger(ClienteModel.class.getName()).log(Level.SEVERE, null, ex);
			this.desconectar();
			return 0;

		}
	}
	
	
	//	ENVIA CORREO PARA CAMBIAR CONTRASEÑA
	
	public int sendRecoveryEmail(String recipient, String key)  {
		// para enviar la clave al correo
		Properties properties = new Properties();
		properties.setProperty("mail.smtp.host", "smtp.gmail.com");
		properties.setProperty("mail.smtp.starttls.enable", "true");
		properties.setProperty("mail.smtp.port", "587");
		properties.setProperty("mail.smtp.auth", "true");
		Session sesion = Session.getDefaultInstance(properties);
		String emisor = "couponstoresv@gmail.com";
		String password = "6Avq*V$$gQ35=tJoKPeofd";
		String asunto = "RECUPERA TU CONTRASEï¿½A";
		String mensaje = "Hemos recibido una solicitud para recuperar tu contrase\\\\u00f1a. Para iniciar sesi\\\\u00D3n  nuevamente, utiliza esta nueva contrase\\\\u00f1a: " + key;
		MimeMessage mail = new MimeMessage(sesion);
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
			Logger.getLogger(ClienteModel.class.getName()).log(Level.SEVERE, null, ex);
			return 0;
		} catch (MessagingException ex) {
			Logger.getLogger(ClienteModel.class.getName()).log(Level.SEVERE, null, ex);
			return 0;
		}
	}
	
	public int enviarCorreo(String receptor, String url) {
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
		String mensaje = "Te haz registrado como cliente en La Cuponera. Para activar tu cuenta ingresa al siguiente link: "
				+ url;
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
			Logger.getLogger(ClienteModel.class.getName()).log(Level.SEVERE, null, ex);
			return 0;
		} catch (MessagingException ex) {
			Logger.getLogger(ClienteModel.class.getName()).log(Level.SEVERE, null, ex);
			return 0;
		}
	}
	
	public List<Oferta> listarOfertas(String fecha) throws SQLException {
		try {
			List<Oferta> lista = new ArrayList<>();
			String sql = "CALL sp_listarOfertasDisponibles(?)";
			this.conectar();
			cs = conexion.prepareCall(sql);
			cs.setString(1, fecha);
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
			Logger.getLogger(ClienteModel.class.getName()).log(Level.SEVERE, null, ex);
			this.desconectar();
			return null;
		}
	}
	
	public int totalOfertas(String fecha) throws SQLException {
		try {
			int totalOfertas = 0;
			String sql = "SELECT COUNT(codigo) as totalOfertas FROM ofertas WHERE estado = \"Aprobada\" and cuponesDisponibles > 0 and fechaInicio <= ?";
			this.conectar();
			st = conexion.prepareStatement(sql);
			st.setString(1, fecha);
			rs = st.executeQuery();
			while (rs.next()) {
				totalOfertas = rs.getInt("totalOfertas");
			}
			return totalOfertas;
		} catch (SQLException ex) {
			Logger.getLogger(ClienteModel.class.getName()).log(Level.SEVERE, null, ex);
			return 0;
		} finally {
			this.desconectar();
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
			Logger.getLogger(ClienteModel.class.getName()).log(Level.SEVERE, null, ex);
			this.desconectar();
			return null;
		}

	}
	
	public String obtenerCodigoCupon() throws SQLException {
		try {
			String numero = "";
			String sql = "SELECT max(correlativo+1) as maximo FROM `cupones`";
			this.conectar();
			st = conexion.prepareStatement(sql);
			rs = st.executeQuery();
			while (rs.next()) {
				numero = rs.getString("maximo");
			}
			String aleatorio = "-" + String.format("%040d", new BigInteger(UUID.randomUUID().toString().replace("-", ""), 16)).substring(0,7) + "-" +numero;
			return aleatorio;
		} catch (SQLException ex) {
			Logger.getLogger(ClienteModel.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		} finally {
			this.desconectar();
		}
	}
	
	public int reducirCupones(String codigoOferta) throws SQLException {
		try {
			int filasAfectadas = 0;
			String sql = "UPDATE ofertas SET cuponesDisponibles = cuponesDisponibles-1, cuponesVendidos= cuponesVendidos+1 WHERE codigo = ?";
			this.conectar();
			st = conexion.prepareStatement(sql);
			st.setString(1, codigoOferta);
			filasAfectadas = st.executeUpdate();
			this.desconectar();
			return filasAfectadas;
		} catch (SQLException ex) {
			Logger.getLogger(ClienteModel.class.getName()).log(Level.SEVERE, null, ex);
			this.desconectar();
			return 0;

		}
	}
	
	public int insertarCupon(Cupon cupon, String correo) throws SQLException {
		try {
			int filasAfectadas = 0;
			String sql = "CALL sp_insertarCupon(?,?,?,?,?)";
			this.conectar();
			cs = conexion.prepareCall(sql);
			cs.setString(1, cupon.getCodigo());
			cs.setString(2, cupon.getDui());
			cs.setInt(3, cupon.getCodigoOferta());
			cs.setString(4, cupon.getEstado());
			cs.setString(5, cupon.getFechaVencimiento());
			filasAfectadas = cs.executeUpdate();
			if(filasAfectadas > 0 ) {
				enviarCorreoCompra(correo, cupon.getCodigo());
			}
			this.desconectar();
			return filasAfectadas;
		} catch (SQLException ex) {
			Logger.getLogger(ClienteModel.class.getName()).log(Level.SEVERE, null, ex);
			this.desconectar();
			return 0;
		}
	}
	
	public int enviarCorreoCompra(String receptor, String codigo) {
		// para enviar la clave al correo
		Properties properties = new Properties();
		properties.setProperty("mail.smtp.host", "smtp.gmail.com");
		properties.setProperty("mail.smtp.starttls.enable", "true");
		properties.setProperty("mail.smtp.port", "587");
		properties.setProperty("mail.smtp.auth", "true");
		Session sesion = Session.getDefaultInstance(properties);
		String emisor = "couponstoresv@gmail.com";
		String password = "6Avq*V$$gQ35=tJoKPeofd";
		String asunto = "CONFIRMACIï¿½N DE COMPRA";
		String mensaje = "Tu compra de cupï¿½n fue exitosa. Al momento de canjearlo presenta tu DUI y el cï¿½digo del cupon: " + codigo;
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
			Logger.getLogger(ClienteModel.class.getName()).log(Level.SEVERE, null, ex);
			return 0;
		} catch (MessagingException ex) {
			Logger.getLogger(ClienteModel.class.getName()).log(Level.SEVERE, null, ex);
			return 0;
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
				cupon.setCodigoOferta(rs.getInt("codigoOferta"));
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


	/*public int totalOfertasPendientes(String codigo) throws SQLException {
		try {
			int totalOfertasPendientes = 0;
			String sql = "SELECT COUNT(codigo) as totalOfertasPendientes FROM ofertas WHERE estado ='Pendiente' and codigoEmpresa=\""+codigo+"\"";
			this.conectar();
			st = conexion.prepareStatement(sql);
			rs = st.executeQuery();
			while (rs.next()) {
				totalOfertasPendientes = rs.getInt("totalOfertasPendientes");
			}
			return totalOfertasPendientes;
		} catch (SQLException ex) {
			Logger.getLogger(ClienteModel.class.getName()).log(Level.SEVERE, null, ex);
			return 0;
		} finally {
			this.desconectar();
		}
	}
	
	public int totalOfertas(String codigo) throws SQLException {
		try {
			int totalOfertas = 0;
			String sql = "SELECT COUNT(codigo) as totalOfertas FROM ofertas WHERE codigoEmpresa=\""+codigo+"\"";
			this.conectar();
			st = conexion.prepareStatement(sql);
			rs = st.executeQuery();
			while (rs.next()) {
				totalOfertas = rs.getInt("totalOfertas");
			}
			return totalOfertas;
		} catch (SQLException ex) {
			Logger.getLogger(ClienteModel.class.getName()).log(Level.SEVERE, null, ex);
			return 0;
		} finally {
			this.desconectar();
		}
	}
	
	public int totalDependientes(String codigo) throws SQLException {
		try {
			int totalDependientes = 0;
			String sql = "SELECT COUNT(correo) as totalDependientes FROM dependientes WHERE codigoEmpresa=\""+codigo+"\"";
			this.conectar();
			st = conexion.prepareStatement(sql);
			rs = st.executeQuery();
			while (rs.next()) {
				totalDependientes = rs.getInt("totalDependientes");
			}
			return totalDependientes;
		} catch (SQLException ex) {
			Logger.getLogger(ClienteModel.class.getName()).log(Level.SEVERE, null, ex);
			return 0;
		} finally {
			this.desconectar();
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
			Logger.getLogger(ClienteModel.class.getName()).log(Level.SEVERE, null, ex);
			this.desconectar();
			return null;
		}

	}
	
	public Dependiente obtenerDependiente(String correo) throws SQLException {
		try {
			String sql = "CALL sp_obtenerDependiente(?)";
			this.conectar();
			cs = conexion.prepareCall(sql);
			cs.setString(1, correo);
			rs = cs.executeQuery();
			if (rs.next()) {
				Dependiente dependiente = new Dependiente();
				// nombres de los campos de la bdd
				dependiente.setCorreo(rs.getString("correo"));
				dependiente.setNombres(rs.getString("nombres"));
				dependiente.setApellidos(rs.getString("apellidos"));
				return dependiente;
			}
			this.desconectar();
			return null;
		} catch (SQLException ex) {
			Logger.getLogger(ClienteModel.class.getName()).log(Level.SEVERE, null, ex);
			this.desconectar();
			return null;
		}

	}
	
	public int insertarOferta(Oferta oferta, String codigo) throws SQLException {
		try {
			int filasAfectadas = 0;
			String sql = "CALL sp_insertarOferta(?,?,?,?,?,?,?,?,?)";
			this.conectar();
			cs = conexion.prepareCall(sql);
			cs.setString(1, oferta.getTitulo());
			cs.setDouble(2, oferta.getPrecio());
			cs.setString(3, oferta.getFechaInicio());
			cs.setString(4, oferta.getFechaFinal());
			cs.setString(5, oferta.getFechaFinalCupon());
			cs.setInt(6, oferta.getCuponesDisponibles());
			cs.setString(7, oferta.getDescripcion());
			cs.setString(8, oferta.getDetalles());
			cs.setString(9, codigo);
			filasAfectadas = cs.executeUpdate();
			this.desconectar();
			return filasAfectadas;
		} catch (SQLException ex) {
			Logger.getLogger(ClienteModel.class.getName()).log(Level.SEVERE, null, ex);
			this.desconectar();
			return 0;
		}
	}
	
	
	
	public String generarClave() {

		// UUID Unique Universal Identifier
		String clave = "";
		clave = UUID.randomUUID().toString().toUpperCase().substring(0, 6);
		return clave;

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
			Logger.getLogger(ClienteModel.class.getName()).log(Level.SEVERE, null, ex);
			this.desconectar();
			return null;
		}
	}
	
	public List<Dependiente> listarDependientes(String codigo) throws SQLException {
		try {
			List<Dependiente> lista = new ArrayList<>();
			String sql = "CALL sp_listarDependientes(?)";
			this.conectar();
			cs = conexion.prepareCall(sql);
			cs.setString(1, codigo);
			rs = cs.executeQuery();
			while (rs.next()) {
				Dependiente dependiente = new Dependiente();
				dependiente.setCorreo(rs.getString("correo"));
				dependiente.setNombres(rs.getString("nombres"));
				dependiente.setApellidos(rs.getString("apellidos"));
				dependiente.setPassword(rs.getString("password"));
				dependiente.setCodigoEmpresa(rs.getString("codigoEmpresa"));
				lista.add(dependiente);
			}
			this.desconectar();
			return lista;
		} catch (SQLException ex) {
			Logger.getLogger(ClienteModel.class.getName()).log(Level.SEVERE, null, ex);
			this.desconectar();
			return null;
		}
	}
	
	public List<Oferta> listarOfertasPendientes(String codigo) throws SQLException {
		try {
			List<Oferta> lista = new ArrayList<>();
			String sql = "CALL sp_listarOfertasPendientesDeEmpresa(?)";
			this.conectar();
			cs = conexion.prepareCall(sql);
			cs.setString(1, codigo);
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
			Logger.getLogger(ClienteModel.class.getName()).log(Level.SEVERE, null, ex);
			this.desconectar();
			return null;
		}
	}
	
	
	
	public int modificarOferta(Oferta oferta, int codigoOferta) throws SQLException {
		try {
			int filasAfectadas = 0;
			String sql = "UPDATE ofertas SET titulo = ?, precio = ?, fechaInicio = ?, fechaFinal = ?, fechaFinalCupon = ?, cuponesIniciales = ?, cuponesDisponibles = ?, descripcion = ?, detalles = ?, codigoEmpresa = ?, estado = ?, justificacion = ? WHERE codigo = ?";
			this.conectar();
			st = conexion.prepareStatement(sql);
			st.setString(1, oferta.getTitulo());
			st.setDouble(2, oferta.getPrecio());
			st.setString(3, oferta.getFechaInicio());
			st.setString(4, oferta.getFechaFinal());
			st.setString(5, oferta.getFechaFinalCupon());
			st.setInt(6, oferta.getCuponesDisponibles());
			st.setInt(7, oferta.getCuponesDisponibles());
			st.setString(8, oferta.getDescripcion());
			st.setString(9, oferta.getDetalles());
			st.setString(10, oferta.getCodigoEmpresa());
			st.setString(11, "Pendiente");
			st.setString(12, null);
			st.setInt(13, codigoOferta);
			filasAfectadas = st.executeUpdate();
			this.desconectar();
			return filasAfectadas;
		} catch (SQLException ex) {
			Logger.getLogger(ClienteModel.class.getName()).log(Level.SEVERE, null, ex);
			this.desconectar();
			return 0;

		}
	}
	
	public int eliminarDependiente(String codigo) throws SQLException {
		try {
			int filasAfectadas = 0;
			String sql = "CALL sp_eliminarDependiente(?)";
			this.conectar();
			cs = conexion.prepareCall(sql);
			cs.setString(1, codigo);
			filasAfectadas = cs.executeUpdate();
			this.desconectar();
			return filasAfectadas;
		} catch (SQLException ex) {
			Logger.getLogger(ClienteModel.class.getName()).log(Level.SEVERE, null, ex);
			this.desconectar();
			return 0;
		}

	}
	
	
	
	public int modificarDependiente(Dependiente dependiente) throws SQLException {
		try {
			int filasAfectadas = 0;
			String sql = "UPDATE dependientes SET correo = ?, nombres = ?, apellidos = ? WHERE correo = ?";
			this.conectar();
			st = conexion.prepareStatement(sql);
			st.setString(1, dependiente.getCorreo());
			st.setString(2, dependiente.getNombres());
			st.setString(3, dependiente.getApellidos());
			st.setString(4, dependiente.getCorreo());
			filasAfectadas = st.executeUpdate();
			this.desconectar();
			return filasAfectadas;
		} catch (SQLException ex) {
			Logger.getLogger(ClienteModel.class.getName()).log(Level.SEVERE, null, ex);
			this.desconectar();
			return 0;

		}
	}*/


}
