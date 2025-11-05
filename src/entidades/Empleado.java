package entidades;

import java.util.*;

public abstract class Empleado {
	
	private static Integer proximoLegajo = 1;
	private String nombre;
	private Integer legajo;
	private Tarea tareaEnCurso;
	private int cantidadDeRetrasos;
	private List<Tarea> tareasRealizadas;
	private double costo;

	public Empleado() {
		
	}
	public Empleado (String nombre) {
		this.nombre = nombre;
		this.legajo = proximoLegajo;
		proximoLegajo += 1;
		
	}

	public Integer consultarLegajo() {
		Integer legajoAux = this.legajo;
		
		return legajoAux;
	}
	public boolean estaAsignado() {
		if (tareaEnCurso != null) {
			return true;
		}
		return false;
	}
	public void asignarTarea(Tarea t) {
		 
			this.tareaEnCurso = t;
		
	}
	public void liberarEmpleado() {
		if(estaAsignado()) {
			tareaEnCurso = null;
		}
		
	}
	public void incrementarRetrasos() {
		cantidadDeRetrasos +=1;
	}
	public void eliminarTarea() { // no se utilizó en la implementacion
		
	}
	public List<Tarea> obtenerTareas(){
		return tareasRealizadas;
	}
	public abstract double calcularCosto(double cantDias);

	public int consultarCantidadRetrasos() {		
		return cantidadDeRetrasos;
	}
	public boolean tieneRetrasos() {
		if (cantidadDeRetrasos >=1) {
			return true;
		}
		else 
			return false;
	}
	public String consultarNombre() {
		String nombreAux = this.nombre;
		return nombreAux;
	}
	@Override
	public String toString() {
		//return "Empleado[" + "Legajo: " + this.legajo + " Nombre: " + this.nombre + "]";  
		/*
		 * en el test testCalculaCostoSinRetrasosYReasignacion() solicita el legajo haciendo 
		 * Integer legajo=Integer.parseInt(emp[0].toString()); por lo que empleado.toString() 
		 * debe devolver el legajo en formato String
		 * */
		return this.legajo.toString();
	}
	
	
	public boolean equals(Empleado emp) {
		if (emp != null) {
			return emp.consultarLegajo() == this.legajo;
			
		} else {
			return false;
		}
	}

}
