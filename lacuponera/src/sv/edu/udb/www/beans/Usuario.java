package sv.edu.udb.www.beans;

public class Usuario {
	private String dui;
	private String nombres;
	private String apellidos;
	private int telefono;
	private String direccion;
	private String tipo;
	private int estado;
	private String correo;
	private String password;
	private int correlativo;
	private String stringEstado;

	public Usuario(String dui, String nombres, String apellidos, int telefono, String direccion, String tipo,
			int estado, String correo, String password, int correlativo, String stringEstado) {
		this.dui = dui;
		this.nombres = nombres;
		this.apellidos = apellidos;
		this.telefono = telefono;
		this.direccion = direccion;
		this.tipo = tipo;
		this.estado = estado;
		this.correo = correo;
		this.password = password;
		this.correlativo = correlativo;
		this.stringEstado = stringEstado;
	}

	public Usuario() {
		this.dui = "";
		this.nombres = "";
		this.apellidos = "";
		this.telefono = 0;
		this.direccion = "";
		this.tipo = "";
		this.estado = 0;
		this.correo = "";
		this.password = "";
		this.correlativo = 0;
		this.stringEstado = "";
	}

	public String getDui() {
		return dui;
	}

	public void setDui(String dui) {
		this.dui = dui;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public int getTelefono() {
		return telefono;
	}

	public void setTelefono(int telefono) {
		this.telefono = telefono;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getCorrelativo() {
		return correlativo;
	}

	public void setCorrelativo(int correlativo) {
		this.correlativo = correlativo;
	}

	public String getStringEstado() {
		return stringEstado;
	}

	public void setStringEstado(String stringEstado) {
		this.stringEstado = stringEstado;
	}

}
