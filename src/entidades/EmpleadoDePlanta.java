package entidades;

public class EmpleadoDePlanta extends Empleado {
	
	private String categoria;
	private double valorPorDia;

	public EmpleadoDePlanta(String nombre, double valor, String categoria) {
		super(nombre);
		
		this.valorPorDia = valor;
		this.categoria = categoria;
	}
	
	@Override
	public double calcularCosto(double cantDias) {
		// Medio día se redondea a 1 día completo 
	    double diasACobrar = Math.ceil(cantDias); // Math.ceil() redondea para arriba y devuelve un double (ej.: si le paso 4.3 devuelve 5.0)
	    
	    double costoBase = this.valorPorDia * diasACobrar;
	    
	    // El bonus del 2% se aplica si ESE empleado no tiene retrasos 
	    if (this.consultarCantidadRetrasos() == 0) {
	        return costoBase * 1.02; // 2% adicional
	    } else {
	        return costoBase;
	    }
	}

	
}
