package entidades;

import java.util.HashMap;
import java.util.List;

public interface IProyecto {
	
	
	public void asignarResponsableEnTarea(String titulo, Empleado empleado) throws Exception;

	public void registrarRetraso(String titulo, double cantDias);
	
	public void agregarTarea(String titulo, String descripcion, double dias);
	
	public void finalizarTarea(String titulo) throws Exception;
	
	public void finalizarProyecto(String fechaFin);
	
	public void reasignarEmpleado(Empleado empleado, String titulo);
	
	
	public double consultarCosto();
	
	
	public Tarea[] tareasAsignadas();
	
	public Tarea[] tareasNoAsignadas();
	
	public String consultarDomicilio();
	
	public List<Tupla<Integer, String>> empleadosAsignados();
	
	public String consultarEstado();
	
	public void cambiarEstado(String estado);

}
