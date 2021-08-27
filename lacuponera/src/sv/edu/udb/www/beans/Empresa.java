package sv.edu.udb.www.beans;

public class Empresa {
	private String codigo;
	private String nombre;
	private String contacto;
	private String direccion;
	private String correo;
	private String stringCodigoRubro;
	private String password;
	private int comision;
	private int codigoRubro;
	private int telefono;
	private int correlativo;
	

	public Empresa(String codigo, String nombre, String contacto, String direccion, String correo,
			String stringCodigoRubro, int comision, int codigoRubro, int telefono, int correlativo, String password) {
		this.codigo = codigo;
		this.nombre = nombre;
		this.contacto = contacto;
		this.direccion = direccion;
		this.correo = correo;
		this.stringCodigoRubro = stringCodigoRubro;
		this.comision = comision;
		this.codigoRubro = codigoRubro;
		this.telefono = telefono;
		this.correlativo = correlativo;
		this.password = password;
	}

	public Empresa() {
		this.codigo = "";
		this.nombre = "";
		this.contacto = "";
		this.direccion = "";
		this.correo = "";
		this.stringCodigoRubro = "";
		this.comision = 0;
		this.codigoRubro = 0;
		this.telefono = 0;
		this.correlativo = 0;
		this.password = "";
	}

	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getContacto() {
		return contacto;
	}

	public void setContacto(String contacto) {
		this.contacto = contacto;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getStringCodigoRubro() {
		return stringCodigoRubro;
	}

	public void setStringCodigoRubro(String stringCodigoRubro) {
		this.stringCodigoRubro = stringCodigoRubro;
	}

	public int getComision() {
		return comision;
	}

	public void setComision(int comision) {
		this.comision = comision;
	}

	public int getCodigoRubro() {
		return codigoRubro;
	}

	public void setCodigoRubro(int codigoRubro) {
		this.codigoRubro = codigoRubro;
	}

	public int getTelefono() {
		return telefono;
	}

	public void setTelefono(int telefono) {
		this.telefono = telefono;
	}

	public int getCorrelativo() {
		return correlativo;
	}

	public void setCorrelativo(int correlativo) {
		this.correlativo = correlativo;
	}

}
