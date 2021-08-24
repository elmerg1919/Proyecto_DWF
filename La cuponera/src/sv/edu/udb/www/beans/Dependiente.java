package sv.edu.udb.www.beans;

public class Dependiente {
	private String correo;
	private String nombres;
	private String apellidos;
	private String password;
	private String codigoEmpresa;

	public Dependiente(String correo, String nombres, String apellidos, String password, String codigoEmpresa) {
		this.correo = correo;
		this.nombres = nombres;
		this.apellidos = apellidos;
		this.password = password;
		this.codigoEmpresa = codigoEmpresa;
	}

	public Dependiente() {
		this.correo = "";
		this.nombres = "";
		this.apellidos = "";
		this.password = "";
		this.codigoEmpresa = "";
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCodigoEmpresa() {
		return codigoEmpresa;
	}

	public void setCodigoEmpresa(String codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}

}
