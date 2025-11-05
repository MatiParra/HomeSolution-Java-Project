package entidades;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class HomeSolution implements IHomeSolution {
	HashMap<String, Cliente> clientes;
	HashMap<Integer, Empleado> empleados;
	HashMap<Integer, Proyecto> proyectos;
	
	
	public HomeSolution() {
		this.clientes = new HashMap<String, Cliente>();
		this.empleados = new HashMap<Integer, Empleado>();
		this.proyectos = new HashMap<Integer, Proyecto>();
	}

	@Override
	public void registrarEmpleado(String nombre, double valor) throws IllegalArgumentException {
		
		if ((nombre == null || nombre.length() == 0) || valor < 0) {
			throw new IllegalArgumentException("Nombre o valor inválido");
		}
		
		Empleado empleadoAux = new EmpleadoSimple(nombre, valor);
		this.empleados.put(empleadoAux.consultarLegajo(), empleadoAux);
	}

	@Override
	public void registrarEmpleado(String nombre, double valor, String categoria) throws IllegalArgumentException {
		
		boolean categoriaValida = categoria.equals("INICIAL")  || categoria.equals("TECNICO") || categoria.equals("EXPERTO");
		if ((nombre == null || nombre.length() == 0) || valor < 0 || !(categoriaValida)) {
			throw new IllegalArgumentException("Nombre, valor o categoria inválido");
		}
		
		Empleado empleadoAux = new EmpleadoDePlanta(nombre, valor, categoria);
		this.empleados.put(empleadoAux.consultarLegajo(), empleadoAux);
		
	}

	@Override
	public void registrarProyecto(String[] titulos, String[] descripcion, double[] dias, String domicilio,
			String[] cliente, String inicio, String fin) throws IllegalArgumentException {
		
		boolean titulosInvalidos = false;
		for(String t : titulos) {
			titulosInvalidos |=  t == null;
		}
		
		boolean descripcionesInvalidas = false;
		for(String d : descripcion) {
			descripcionesInvalidas |= d == null;
		}
		
		boolean diasInvalidos = false;
		for(double d : dias) {
			diasInvalidos |= d < 0.5;
		}
		

		
		
		if (!this.clientes.containsKey(cliente[1]) && fechaValida(inicio, fin) && !diasInvalidos && !descripcionesInvalidas && !titulosInvalidos) {
				
				agregarCliente(cliente[0], cliente[1], cliente[2]);
				Proyecto proyectoAux = new Proyecto(titulos, descripcion, dias, domicilio, cliente, inicio, fin);
				this.proyectos.put(proyectoAux.consultarCodigo(), proyectoAux);
				
		 } else if (this.clientes.containsKey(cliente[1]) && fechaValida(inicio, fin) && !diasInvalidos && !descripcionesInvalidas && !titulosInvalidos) {
			 
			Proyecto proyectoAux = new Proyecto(titulos, descripcion, dias, domicilio, cliente, inicio, fin);
			this.proyectos.put(proyectoAux.consultarCodigo(), proyectoAux);
			
		 } else {
			 
			 throw new IllegalArgumentException("Datos inválidos");	
		 }
		
	}

	
	private boolean fechaValida(String inicio, String fin) {
		

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate fechaInicio = LocalDate.parse(inicio, formatter);
		LocalDate fechaFin = LocalDate.parse(fin, formatter);
		
		return fechaInicio.isBefore(fechaFin);
	}
	
	
	
	private Empleado empleadoDisponible() {
		
		for (Empleado e : this.empleados.values()) {
			
			if (!e.estaAsignado()) {
				
				return e;
			}
			
			
		}
		return null;
	}
	
	
	
	@Override
	public void asignarResponsableEnTarea(Integer numero, String titulo) throws Exception {
		
		if (this.proyectos.containsKey(numero) && !this.estaFinalizado(numero)) {
			Empleado empleado = this.empleadoDisponible();
			if(empleado == null) {
				this.proyectos.get(numero).cambiarEstado(Estado.pendiente);
				throw new Exception("No hay empleados disponibles");
			}
			
			this.proyectos.get(numero).asignarResponsableEnTarea(titulo, empleadoDisponible());
			
		} else {
			
			throw new Exception();
		}
		
		
		
		
	}
	
	
	
	private Empleado empleadoConMenosRetraso() {
	    Empleado empleadoMin = null;
	    
	    int minRetrasos = Integer.MAX_VALUE;

	    for (Empleado e : this.empleados.values()) {
	        if (!e.estaAsignado()) {
	            if (e.consultarCantidadRetrasos() < minRetrasos) {
	                minRetrasos = e.consultarCantidadRetrasos();
	                empleadoMin = e;
	            }
	        }
	    }

	    return empleadoMin;
	}
	
	
	

	@Override
	public void asignarResponsableMenosRetraso(Integer numero, String titulo) throws Exception {
		Empleado empleadoMenosRetraso = this.empleadoConMenosRetraso();
		if (this.proyectos.containsKey(numero) && !this.estaFinalizado(numero)) {
			if (empleadoMenosRetraso == null) {
				this.proyectos.get(numero).cambiarEstado(Estado.pendiente);
				throw new Exception("No hay empleados disponibles");
			}
			
			this.proyectos.get(numero).asignarResponsableEnTarea(titulo, this.empleadoConMenosRetraso());
			
			
		} else {
			
			throw new Exception();
		}
		
	}

	@Override
	public void registrarRetrasoEnTarea(Integer numero, String titulo, double cantidadDias) 
			throws IllegalArgumentException {
		if (this.proyectos.containsKey(numero) && !this.estaFinalizado(numero)) {
			
			this.proyectos.get(numero).registrarRetraso(titulo, cantidadDias);			
			
		} else {
			
			throw new IllegalArgumentException();
		}
		
	}
		
	

	@Override
	public void agregarTareaEnProyecto(Integer numero, String titulo, String descripcion, double dias)
			throws IllegalArgumentException {
		
		if (this.proyectos.containsKey(numero) && !this.estaFinalizado(numero)) {
			
			if((titulo != null && titulo.length() > 0) && (descripcion != null && descripcion.length() > 0) && dias >= 0.5) {
				this.proyectos.get(numero).agregarTarea(titulo, descripcion, dias);
				
			} else {
				throw new IllegalArgumentException();
			}
			
		} else {
			throw new IllegalArgumentException();
		}
		
	}

	@Override
	public void finalizarTarea(Integer numero, String titulo) throws Exception {
		
		if (this.proyectos.containsKey(numero) && !this.proyectos.get(numero).consultarEstado().equals("FINALIZADO") ) {
			this.proyectos.get(numero).finalizarTarea(titulo);
			
		} else {
			throw new Exception("El proyecto no se encuentra o esta finalizado");
		}
		
	}

	@Override
	public void finalizarProyecto(Integer numero, String fechaFin) throws IllegalArgumentException {
		
		String estadoProyecto = this.proyectos.get(numero).consultarEstado();
		String fechaInicio = this.proyectos.get(numero).consultarFechaInicio();
		//System.out.println("Fecha Inicio" + fechaInicio + "Fecha fin" + fechaFin);
		if (this.proyectos.containsKey(numero) && !estadoProyecto.equals("FINALIZADO") && fechaValida(fechaInicio, fechaFin)) {
				
			this.proyectos.get(numero).finalizarProyecto(fechaFin);
	
		} else {
			throw new IllegalArgumentException();
		}
		
	}

	@Override
	public void reasignarEmpleadoEnProyecto(Integer numero, Integer legajo, String titulo) throws Exception {
		
		if (this.reasignacionValida(numero, titulo)) {

			Empleado empleadoAux = this.empleados.get(legajo);
			
			if(empleadoAux == null || empleadoAux.estaAsignado()) {
				
				throw new Exception("Empleado no válido o no disponible");
			}
			
			
			this.proyectos.get(numero).reasignarEmpleado(empleadoAux, titulo);
		
		} else {
			
			throw new Exception();
		}
		
		
	}
	
	
	

	@Override
	public void reasignarEmpleadoConMenosRetraso(Integer numero, String titulo) throws Exception {
		
		if (this.reasignacionValida(numero, titulo)) {
			Empleado empleadoAux = this.empleadoConMenosRetraso();
			this.proyectos.get(numero).reasignarEmpleado(empleadoAux, titulo);
			
		} else {
			
			throw new Exception();
		}
		
	}
	
	private boolean reasignacionValida(Integer numero, String titulo) {
		
		
		return this.proyectos.containsKey(numero) && this.proyectos.get(numero).existeTarea(titulo) && this.proyectos.get(numero).consultarTarea(titulo).tieneEmpleadoAsignado();
		
	}
	
	
	

	@Override
	public double costoProyecto(Integer numero) {
		
		if(this.proyectos.containsKey(numero)) {
			return this.proyectos.get(numero).consultarCosto();
		}
		
			return 0;
		
	}

	@Override
	public List<Tupla<Integer, String>> proyectosFinalizados() {
		
		return this.consultarProyectosFinalizados();
	}
	
	
	
	private List<Tupla<Integer, String>> consultarProyectosFinalizados(){
		List<Tupla<Integer,String>> proyectosFinalizados = new LinkedList<>();
		
		for (Proyecto p : this.proyectos.values()) {
			if(p.consultarEstado().equals("FINALIZADO")) {
				Tupla<Integer, String> datosProyecto = new Tupla<Integer,String>(p.consultarCodigo(), p.consultarDomicilio());
				
				proyectosFinalizados.add(datosProyecto);
			}
		}
		return proyectosFinalizados;
		
	}
	
	private List<Tupla<Integer, String>> consultarProyectosPendientes(){
		List<Tupla<Integer,String>> proyectosPendientes = new LinkedList<>();
		
		for (Proyecto p : this.proyectos.values()) {
			if(p.consultarEstado().equals("PENDIENTE")) {
				Tupla<Integer, String> datosProyecto = new Tupla<Integer,String>(p.consultarCodigo(), p.consultarDomicilio());
				
				proyectosPendientes.add(datosProyecto);
			}
		}
		return proyectosPendientes;
		
	}
	
	
	private List<Tupla<Integer, String>> consultarProyectosActivos(){
		List<Tupla<Integer,String>> proyectosActivos = new LinkedList<>();
		
		for (Proyecto p : this.proyectos.values()) {
			if(p.consultarEstado().equals("ACTIVO")) {
				Tupla<Integer, String> datosProyecto = new Tupla<Integer,String>(p.consultarCodigo(), p.consultarDomicilio());
				
				proyectosActivos.add(datosProyecto);
			}
		}
		return proyectosActivos;
		
	}

	@Override
	public List<Tupla<Integer, String>> proyectosPendientes() {
		
		return this.consultarProyectosPendientes();
	}

	@Override
	public List<Tupla<Integer, String>> proyectosActivos() {
		
		return this.consultarProyectosActivos();
	}

	@Override
	public Object[] empleadosNoAsignados() {
		Empleado[] empleadosNoAsignados = new Empleado[this.empleados.values().size()];
		int cont = 0;
		for (Empleado e : this.empleados.values()) {
			if (!e.estaAsignado()) {
				empleadosNoAsignados[cont] = e;
				cont += 1;
			}
		}
		
		return Arrays.copyOf(empleadosNoAsignados, cont);
	}

	@Override
	public boolean estaFinalizado(Integer numero) {
		if(this.proyectos.containsKey(numero) && this.proyectos.get(numero).consultarEstado().equals("FINALIZADO")){
			return true;
		}
		return false;
	}

	@Override
	public int consultarCantidadRetrasosEmpleado(Integer legajo) {
		if(this.empleados.containsKey(legajo)) {
			return this.empleados.get(legajo).consultarCantidadRetrasos();
		}
		return -1;
	}

	@Override
	public List<Tupla<Integer, String>> empleadosAsignadosAProyecto(Integer numero) {
		
		if(this.existeProyecto(numero)){
			return this.proyectos.get(numero).empleadosAsignados();
		}
		
		return null;
	}

	private boolean existeProyecto(Integer numero) {
		if(this.proyectos.containsKey(numero)) {
			return true;
		}
		return false;
	}

	@Override
	public Object[] tareasProyectoNoAsignadas(Integer numero) {
		if (this.proyectos.containsKey(numero) && !this.proyectos.get(numero).consultarEstado().equals("FINALIZADO")) {
			
			return this.proyectos.get(numero).tareasNoAsignadas();
			
		} else {
			
			throw new IllegalArgumentException("Error: El proyecto con número " + numero + " no existe o esta finalizado.");
		}
	}

	@Override
	public Object[] tareasDeUnProyecto(Integer numero) {
		if (this.proyectos.containsKey(numero)) {
			Tarea listaTareas[] = this.proyectos.get(numero).listarTareas();
			return listaTareas; 
			// Talvez haya que modificar consultarTareas y que devuelva una lista y no un hashmap, sino crear otro metodo
		}
		throw new RuntimeException("Error: El proyecto con número " + numero + " no existe.");
	}

	@Override
	public String consultarDomicilioProyecto(Integer numero) {
		if (this.proyectos.containsKey(numero)) {
			return this.proyectos.get(numero).consultarDomicilio();
		}
		throw new RuntimeException("Error: El proyecto con número " + numero + " no existe.");
	}

	@Override
	public boolean tieneRestrasos(Integer legajo) { //Revisar si pasamos el legajo a que sea integer, no se si se usa en otro lado
		if (this.empleados.containsKey(legajo)) {
			return this.empleados.get(legajo).tieneRetrasos();
		}
		throw new RuntimeException("Error: El empleado con legajo " + legajo + " no existe.");
	}

	@Override
	public List<Tupla<Integer, String>> empleados() {
		List<Tupla<Integer, String>> empleadosAux = new LinkedList<>();
		
		for (Empleado e : this.empleados.values()) {
			
			Tupla<Integer, String> emp = new Tupla<Integer, String>(e.consultarLegajo(), e.consultarNombre());
			empleadosAux.add(emp);
		}
		return empleadosAux;
	}

	@Override
	public String consultarProyecto(Integer numero) {
		if (this.proyectos.containsKey(numero)) {
			return this.proyectos.get(numero).toString();
		}
		throw new RuntimeException("Error: El proyecto con número " + numero + " no existe.");
	}
	private void agregarCliente(String domicilio, String email, String telefono) {
		Cliente nuevo = new Cliente(domicilio, email, telefono);
		clientes.put(email, nuevo);
	}
	
	
	
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("========================================\n");
	    sb.append("         Resumen de HomeSolution\n");
	    sb.append("========================================\n");
	    sb.append("Total de Empleados Registrados: ").append(this.empleados.size()).append("\n");
	    sb.append("Total de Clientes Registrados: ").append(this.clientes.size()).append("\n");
	    sb.append("Total de Proyectos: ").append(this.proyectos.size()).append("\n");

	    if (this.proyectos.isEmpty()) {
	        sb.append("\nNo hay proyectos registrados.\n");
	    } else {
	        sb.append("\n--- Detalle de Proyectos ---");
	        for (Proyecto p : this.proyectos.values()) {
	            sb.append(p.toString()); // Llama al Proyecto.toString() que acabamos de crear
	        }
	    }
	    sb.append("\n========================================\n");
	    return sb.toString();
	}

}