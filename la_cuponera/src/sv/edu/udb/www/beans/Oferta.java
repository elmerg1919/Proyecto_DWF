package sv.edu.udb.www.beans;

public class Oferta {

	private String titulo;
	private String descripcion;
	private String detalles;
	private String stringCodigoEmpresa;
	private String codigoEmpresa;
	private String estado;
	private String fechaInicio;
	private String fechaFinal;
	private String fechaFinalCupon;
	private String justificacion;
	private int codigo;
	private int cuponesIniciales;
	private int cuponesDisponibles;
	private int cuponesVendidos;
	private double precio;
	private double ingresosTotales;
	private double cargoPorServicio;

	public double getIngresosTotales() {
		return ingresosTotales;
	}

	public void setIngresosTotales(String cupones, double precio) {
		Double it = Double.parseDouble(cupones) * precio;
		this.ingresosTotales = it;
	}

	public double getCargoPorServicio() {
		return cargoPorServicio;
	}

	public void setCargoPorServicio(String comision) {
		Double porcentaje = Double.parseDouble(comision)/100;
		Double cps = this.getIngresosTotales() * porcentaje;
		this.cargoPorServicio = cps;
	}

	
	
	
	
	
	
	public Oferta(String titulo, String descripcion, String detalles, String stringCodigoEmpresa, String codigoEmpresa,
			String estado, String fechaInicio, String fechaFinal, String fechaFinalCupon, int codigo,
			int cuponesIniciales, int cuponesDisponibles, int cuponesVendidos, double precio, String justificacion) {
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.detalles = detalles;
		this.stringCodigoEmpresa = stringCodigoEmpresa;
		this.codigoEmpresa = codigoEmpresa;
		this.estado = estado;
		this.fechaInicio = fechaInicio;
		this.fechaFinal = fechaFinal;
		this.fechaFinalCupon = fechaFinalCupon;
		this.codigo = codigo;
		this.cuponesIniciales = cuponesIniciales;
		this.cuponesDisponibles = cuponesDisponibles;
		this.cuponesVendidos = cuponesVendidos;
		this.precio = precio;
		this.justificacion = justificacion;
	}

	public Oferta() {
		this.titulo = "";
		this.descripcion = "";
		this.detalles = "";
		this.stringCodigoEmpresa = "";
		this.codigoEmpresa = "";
		this.estado = "";
		this.fechaInicio = "";
		this.fechaFinal = "";
		this.fechaFinalCupon = "";
		this.codigo = 0;
		this.cuponesIniciales = 0;
		this.cuponesDisponibles = 0;
		this.cuponesVendidos = 0;
		this.precio = 0;
		this.justificacion = "";
	}

	public String getJustificacion() {
		return justificacion;
	}

	public void setJustificacion(String justificacion) {
		this.justificacion = justificacion;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDetalles() {
		return detalles;
	}

	public void setDetalles(String detalles) {
		this.detalles = detalles;
	}

	public String getStringCodigoEmpresa() {
		return stringCodigoEmpresa;
	}

	public void setStringCodigoEmpresa(String stringCodigoEmpresa) {
		this.stringCodigoEmpresa = stringCodigoEmpresa;
	}

	public String getCodigoEmpresa() {
		return codigoEmpresa;
	}

	public void setCodigoEmpresa(String codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public String getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	public String getFechaFinalCupon() {
		return fechaFinalCupon;
	}

	public void setFechaFinalCupon(String fechaFinalCupon) {
		this.fechaFinalCupon = fechaFinalCupon;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public int getCuponesIniciales() {
		return cuponesIniciales;
	}

	public void setCuponesIniciales(int cuponesIniciales) {
		this.cuponesIniciales = cuponesIniciales;
	}

	public int getCuponesDisponibles() {
		return cuponesDisponibles;
	}

	public void setCuponesDisponibles(int cuponesDisponibles) {
		this.cuponesDisponibles = cuponesDisponibles;
	}

	public int getCuponesVendidos() {
		return cuponesVendidos;
	}

	public void setCuponesVendidos(int cuponesVendidos) {
		this.cuponesVendidos = cuponesVendidos;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

}
