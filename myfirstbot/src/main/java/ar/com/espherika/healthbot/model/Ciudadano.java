package ar.com.espherika.healthbot.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Ciudadano {

	private int edad;
	private String nombre;
	
	@Id
	private String userId;
	
	@OneToOne(cascade=CascadeType.ALL)
	private PreferenciasChat preferenciasChat;

	public Ciudadano(){
		
	}
	
	public Ciudadano(String userId){
		this();
		this.userId=userId;
	}
	public Ciudadano(String userId,String nombre){
		this(userId);
		this.nombre=nombre;
	}
	public Ciudadano(String userId, int edad,String nombre){
		this(userId,nombre);
		this.edad=edad;
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

	public PreferenciasChat getPreferenciasChat() {
		return preferenciasChat;
	}

	public void setPreferenciasChat(PreferenciasChat preferenciasChat) {
		this.preferenciasChat = preferenciasChat;
	}
}
