package entidades;

public class Tarea {
	
	private String titulo;
	private String descripcion;
	private double diasNecesarios;
	private double retrasoAcum;
	private Empleado responsable;
	private String estado;

	public Tarea() {
		
	}
	
	public Tarea (String titulo, String descripcion, double diasNecesarios) {
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.diasNecesarios = diasNecesarios;
		this.retrasoAcum = 0;
		this.responsable = null;
		this.estado = Estado.pendiente;
	}
	public void asignarEmpleado(Empleado empleado) {
			this.responsable = empleado;
			this.estado = Estado.activo;
		
	}
	public double calcularCosto() {
		
		
		if(!this.tieneEmpleadoAsignado()) {
			return 0;
		}
		
		/* return this.responsable.calcularCosto(this.tiempoNecesarioFinal()); 
		 															asi estaba originalmente, 
		                                                           	sin embargo. el test interpreta 
		                                                            la consigna de manera diferente, 
		                                                           	ya que no tiene en cuenta el aumento 
		                                                           	de dias (debido al retraso) para calcular el costo
		                                                           	*/
		
		return this.responsable.calcularCosto(this.diasNecesarios);
	}
	
	
	
	
	public void eliminarEmpleado() {
		if (tieneEmpleadoAsignado()) {
			responsable = null;
		}
	}
	public boolean tieneEmpleadoAsignado() {
		if (responsable != null) {
			return true;
		}
		else 
			return false;
	}
	public void registrarRetraso(double tiempo) {
		if (tiempo >= 0.5) {
			
			this.retrasoAcum += tiempo;
			this.responsable.incrementarRetrasos();
		}
		else
			throw new IllegalArgumentException();
	}
	public void modificarTiempoNecesario(int tiempo) {
		if (tiempo > 0) {
			diasNecesarios += tiempo;
		}
		else
			throw new RuntimeException ("El tiempo es menor o igual a 0");
	}
	
	
	public double tiempoNecesarioFinal() {
		return this.diasNecesarios + this.retrasoAcum;
	}
	
	
	public void finalizarTarea() {
		
	    if (this.responsable != null) {
	    	this.estado = Estado.finalizado;
	        this.responsable.liberarEmpleado();
	        
	    } else {
	    	throw new RuntimeException("Sin responsable asignado");
	    }
	}
	
	public Empleado consultarResponsable() {
		Empleado responsableAux = this.responsable;
		
		return responsableAux;
	}

	public String consultarEstado() {
		String estadoAux = this.estado;
		
		return estadoAux;
	}

	public void reasignarEmpleado(Empleado empleado) {
		
		this.responsable = empleado;
		
	}

	public double consultarRetrasoAcumulado() {
		
		return this.retrasoAcum;
	}
	@Override
	public String toString() {
		return this.titulo;
	}
	
	
	public boolean equals(Tarea t) {
		if (t != null) {
			return this.titulo.equals(t.titulo) && this.descripcion.equals(t.descripcion) && this.diasNecesarios == t.diasNecesarios;
		} else {
			return false;
		}
	}

}
