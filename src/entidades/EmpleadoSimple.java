package entidades;

public class EmpleadoSimple extends Empleado {
	
	private double valorPorHora;
	
	public EmpleadoSimple() {
		
	}

	public EmpleadoSimple(String nombre, double valor) {
		super(nombre);
		this.valorPorHora = valor;
	}

	@Override
	public double calcularCosto(double cantDias) {
		double horasDeTrabajo;
		
	      if (cantDias == 0.5) {
	    	  
	          horasDeTrabajo = 4;
	          
	      } else {
	    	  
	          horasDeTrabajo = cantDias * 8;
	      }
	      
	      return this.valorPorHora * horasDeTrabajo;
	}

}
