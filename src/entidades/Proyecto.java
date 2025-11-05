package entidades;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Proyecto implements IProyecto{
	private static Integer codigoStatic = 0;
	private Integer codigo;
	private String estado;
	private Cliente cliente;
	private HashMap<String, Tarea> tareas;
	private HashMap<Integer, Empleado> empleadosAsignados;
	private String direccionVivienda;
	private String fechaInicio;
	private String fechaEstimadaFin;
	private String fechaRealFin;
	private double costoFinal;

	public Proyecto(String[] titulos, String[] descripcion, double[] dias, String domicilio, String[] cliente,
			String inicio, String fin) {

		this.codigo = codigoStatic;
		Proyecto.codigoStatic += 1;		
		this.estado = Estado.pendiente;
		this.fechaInicio = inicio;
		this.fechaEstimadaFin = fin;
		this.fechaRealFin = fin;
		this.tareas = new HashMap<>();
		this.empleadosAsignados = new HashMap<>();
		this.direccionVivienda = domicilio;
		this.cliente = new Cliente(cliente[0], cliente[1], cliente[2]);
		
		
		if (titulos.length == descripcion.length && titulos.length == dias.length) {
			for (int i = 0; i< titulos.length; i++) {
				agregarTarea(titulos[i], descripcion[i], dias[i]);
			}
		}
	}

	public Integer consultarCodigo() {
		Integer codigoAux = this.codigo;
		
		return codigoAux;
	}
	
	
	
	public Tarea[] listarTareas(){
		Tarea listaTareas[] = this.tareas.values().toArray(new Tarea[this.tareas.values().size()]);
		return listaTareas;
	}

	@Override
	public Tarea[] tareasAsignadas() {
		
		Tarea[] tareasAsignadas = new Tarea[this.tareas.values().size()];	
		int contador = 0;
		for (Tarea t : this.tareas.values()) {
			if (t.tieneEmpleadoAsignado()) {
				tareasAsignadas[contador] = t;
			}
		}
		
		return tareasAsignadas;
	}



	public String consultarEstado() {
		String estadoAux = this.estado;
		return estadoAux;
	}

	@Override
	public void asignarResponsableEnTarea(String titulo, Empleado empleado) throws Exception {
		
		if (this.tareas.containsKey(titulo)) {
			this.empleadosAsignados.put(empleado.consultarLegajo(), empleado);
			this.tareas.get(titulo).asignarEmpleado(empleado);
			empleado.asignarTarea(this.tareas.get(titulo));
			
			boolean todasActivas = true;
			
			for (Tarea t : this.tareas.values()) {
				todasActivas &= t.consultarEstado().equals("ACTIVO");
			}
			
			if (todasActivas) {
				this.estado = Estado.activo;
			}
			
		} else {
			
			throw new Exception();
		}
		
		
	}

	@Override
	public void registrarRetraso(String titulo, double cantDias) {
		
		if(this.tareas.containsKey(titulo) && this.tareas.get(titulo).tieneEmpleadoAsignado()) {
			this.tareas.get(titulo).registrarRetraso(cantDias);
			
			
		} else {
			
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void agregarTarea(String titulo, String descripcion, double dias) {
		Tarea tarea = new Tarea(titulo, descripcion, dias);
		
		this.tareas.put(titulo, tarea);
		
		/*
		 * si sumo los dias de la tarea a la fecha de fin, la comparacion para determinar si hubo algun retraso implicito se rompe
		 * */
	}

	@Override
	public void finalizarTarea(String titulo) throws Exception {
		
		Tarea tareaAux = this.tareas.get(titulo);
		
		if (this.tareas.containsKey(titulo) && !tareaAux.consultarEstado().equals("FINALIZADO")) {
			this.tareas.get(titulo).finalizarTarea();
			
		} else {
			throw new Exception();
		}
		
	}

	@Override
	public void finalizarProyecto(String fechaFin) {
		
		this.fechaRealFin = fechaFin;
		this.estado = Estado.finalizado;
		
		for (Tarea t : this.tareas.values()) {
			
			if (t.tieneEmpleadoAsignado()) {
				
				t.consultarResponsable().liberarEmpleado();
			}
		}
		
		this.costoFinal = this.consultarCosto();
	}

	@Override
	public void reasignarEmpleado(Empleado empleado, String titulo) {
		Tarea tarea = this.tareas.get(titulo);
		
		if (tarea.tieneEmpleadoAsignado()) {
			
			tarea.consultarResponsable().liberarEmpleado();
		}
		
		this.tareas.get(titulo).reasignarEmpleado(empleado);
		empleado.asignarTarea(this.tareas.get(titulo));
		
	}


	@Override
	public double consultarCosto() {
		
		if (this.estado.equals(Estado.finalizado) && this.costoFinal != 0) {
	        return this.costoFinal;
	    }
		
		
		boolean retrasoEnProyecto = this.tuvoRetraso();
		double costoTotalTareas = this.costoTotalTareas();		
	    
	    /*
	     agregando la validacion de arriba, se determina que, si la fecha real de fin es posterior a la fecha estimada de fin,
	      hay un retraso no registrado, pero que modifica el calculo de costos.
	     */
	    
	    
		if (retrasoEnProyecto) {
			
			return costoTotalTareas * 1.25;
		} else {
			return costoTotalTareas * 1.35;
		}
		
		
		
	}

	@Override
	public Tarea[] tareasNoAsignadas() {
		
		if (this.estado.equals("FINALIZADO")) {
			return new Tarea[0];
		}
		
		Tarea[] tareasAux = new Tarea[this.tareas.values().size()];
		int cont = 0;
		for (Tarea t : this.tareas.values()) {
			
			if (!t.tieneEmpleadoAsignado()) {
				tareasAux[cont] = t;
				cont += 1;
			}
		}
		
		
		
		return Arrays.copyOf(tareasAux, cont);
	}

	@Override
	public String consultarDomicilio() {
		String domicilio = this.direccionVivienda;
		
		return domicilio;
	}

	@Override
	public List<Tupla<Integer, String>> empleadosAsignados() {
		
		List<Tupla<Integer,String>> empleados = new LinkedList<>();
		
		for (Empleado e : this.empleadosAsignados.values()) {
			Tupla<Integer, String> empleado = new Tupla<>(e.consultarLegajo(), e.consultarNombre());
			empleados.add(empleado);
		}
		
		return empleados;
	}

	public String consultarFechaInicio() {
		String fechaAux = this.fechaInicio;
		
		return fechaAux;
	}
	public String consultarFechaRealFin() {
		String fechaAux = this.fechaRealFin;
		
		return fechaAux;
	}

	public Tarea consultarTarea(String titulo) {
		
		if (this.tareas.containsKey(titulo)) {
			
			return this.tareas.get(titulo);
		}
		return null;
	}

	public boolean existeTarea(String titulo) {
		
		return this.tareas.containsKey(titulo);
	}

	@Override
	public void cambiarEstado(String estado) {
		if (estado == null) {
			throw new RuntimeException();		
		} else if(estado.equals("FINALIZADO")) {
			this.estado = estado;
		} else if (estado.equals("ACTIVO")) {
			this.estado = estado;
		} else {
			throw new IllegalArgumentException();
		}
		
	}
	
	@Override
	public String toString() {
		
	    StringBuilder sb = new StringBuilder();
	    sb.append("\n  --- Proyecto N°: ").append(this.codigo).append(" ---");
	    sb.append("\n  Estado: ").append(this.estado);
	    sb.append("\n  Domicilio: ").append(this.direccionVivienda);
	    sb.append("\n  Cliente: ").append(this.cliente.toString()); // Llama al toString() de Cliente
	    sb.append("\n  Fecha Inicio: ").append(this.fechaInicio);
	    sb.append("\n  Fecha Fin Estimada: ").append(this.fechaEstimadaFin);
	    sb.append("\n  Fecha Fin Real: ").append(this.fechaRealFin);

	    // Tareas
	    sb.append("\n  Tareas: ");
	    if (this.tareas.isEmpty()) {
	        sb.append("Ninguna.");
	    } else {
	        for (Tarea t : this.tareas.values()) {
	            sb.append("\n    - ").append(t.toString()); 
	            sb.append(" (").append(t.consultarEstado()).append(")");
	        }
	    }

	    // Costo y Retrasos 
	    boolean tuvoRetraso = this.tuvoRetraso();

	    

	    sb.append("\n  Tuvo Retrasos: ").append(tuvoRetraso ? "Sí" : "No");
	    sb.append("\n  Costo Final: ").append(String.format("%.2f", this.consultarCosto()));
	    sb.append("\n  -------------------------");

	    return sb.toString();
	}

	private boolean tuvoRetraso() {
		boolean retrasoEnProyecto = false;
		for (Tarea t : this.tareas.values()) {
			if (t.consultarRetrasoAcumulado() > 0) {

				retrasoEnProyecto = true;
			}
			
			
		}
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    LocalDate fEstimadaFin = LocalDate.parse(this.fechaEstimadaFin, formatter);
	    LocalDate fRealFin = LocalDate.parse(this.fechaRealFin, formatter);

	    if (fRealFin.isAfter(fEstimadaFin)) {
	        retrasoEnProyecto = true;
	    }
	    
	    return retrasoEnProyecto;
	}

	private double costoTotalTareas() {
		double costoTotalTareas = 0;
	
		for (Tarea t : this.tareas.values()) {
			costoTotalTareas += t.calcularCosto();
		
		}
		
		return costoTotalTareas;
	}
	
	
	public boolean equals(Proyecto p) {
		if(p != null) {
			return this.codigo == p.consultarCodigo();
		} else {
			return false;
		}
	}

}
