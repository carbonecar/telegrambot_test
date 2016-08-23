package ar.com.espherika.healthbot.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Beneficio {

	@Id
	@GeneratedValue
	private Long id;

	private String desripcion;

	public Beneficio() {

	}

	public Beneficio(String descripcion) {
		this.desripcion = descripcion;
	}

	public String getDesripcion() {
		return desripcion;
	}

}
