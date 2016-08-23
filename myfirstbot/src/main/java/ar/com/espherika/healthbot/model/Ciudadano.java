package ar.com.espherika.healthbot.model;

public class Ciudadano {

	private int edad;
	private String nombre;
	private String userId;
	
	public Ciudadano(){
		
	}
	
	public Ciudadano(int edad,String nombre){
		this.edad=edad;
		this.nombre=nombre;
	}
	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
