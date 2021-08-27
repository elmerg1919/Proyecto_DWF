package sv.edu.udb.www.beans;

//

public class Cupon {
	private String codigo;
	private String dui;
	private int codigoOferta;
	private String estado;
	private int correlativo;
	private String tituloOferta;
	private String fechaVencimiento;

		// RECUPERACION DE DATOS
	public Cupon(String codigo, String dui, int codigoOferta, String estado, int correlativo, String tituloOferta, String fechaVencimiento) {
		this.codigo = codigo;
		this.dui = dui;
		this.codigoOferta = codigoOferta;
		this.estado = estado;
		this.correlativo = correlativo;
		this.tituloOferta = tituloOferta;
		this.fechaVencimiento = fechaVencimiento;
	}

	public Cupon() {
		this.codigo = "";
		this.dui = "";
		this.codigoOferta = 0;
		this.estado = "";
		this.correlativo = 0;
		this.tituloOferta = "";
		this.fechaVencimiento = "";
	}

	//METODO GET= MOSTRAR VALOES Y SET=ENVIA A LOS CAMPOS 
	//RETORNA LOS DATOS INGRESADOS
	
	public String getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(String fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDui() {
		return dui;
	}

	public void setDui(String dui) {
		this.dui = dui;
	}

	public int getCodigoOferta() {
		return codigoOferta;
	}

	public void setCodigoOferta(int codigoOferta) {
		this.codigoOferta = codigoOferta;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public int getCorrelativo() {
		return correlativo;
	}

	public void setCorrelativo(int correlativo) {
		this.correlativo = correlativo;
	}

	public String getTituloOferta() {
		return tituloOferta;
	}

	public void setTituloOferta(String tituloOferta) {
		this.tituloOferta = tituloOferta;
	}
}
