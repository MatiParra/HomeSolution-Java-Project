package entidades;

import java.util.Iterator;

public class Cliente {
	private String nombre;
	private String telefono;
	private String email;

	public Cliente(String nombre, String email, String telefono) {
		this.nombre = nombre;
		this.telefono = telefono;
		this.email = email;
	}

	public Cliente() {
	}

	

	public String obtenerTelefono() {
		String telefonoAux = this.telefono;
		return telefonoAux;
	}

	public void modificarTelefono(String telefono) { //Sigo dudando si esto no es un int
		this.telefono = telefono;
	}

	public String obtenerEmail() {
		String emailAux = this.email;
		return emailAux;
	}

	public void modificarEmail(String email) {
		if (email.length()>0 && verificaEmail(email)== true)
		this.email = email;
		else {
			throw new RuntimeException ("El email ingresado esta vacio");
		}
	}

	
	
	private boolean verificaEmail(String email) {
		boolean verificador = false;
		for (int i = 1; i < email.length()-1; i++) {
			if (email.charAt(i) == '@') {
				verificador = true;
			}
		}
		return verificador;
	}
	
	
	public String toString() {
	    return this.nombre + " (Email: " + this.email + ", Tel: " + this.telefono + ")";
	}
	
	public boolean equals(Cliente c) {
		if (c != null) {
			return this.email.equals(c.obtenerEmail());
		} else {
			return false;
		}
	}

}
