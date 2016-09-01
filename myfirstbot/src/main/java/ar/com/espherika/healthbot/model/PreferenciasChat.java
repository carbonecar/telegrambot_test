package ar.com.espherika.healthbot.model;

import javax.annotation.processing.SupportedSourceVersion;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Preferencias de chat para cada persona.
 * @author carbonecar
 *
 */
@Entity
public class PreferenciasChat {

	@Id
	@GeneratedValue
	private Long id;
	
	private boolean presentacionApagada;
	/*private Ciudadano ciudadano;
	
	
	
    @OneToOne(mappedBy = "preferenciasChat")
	public Ciudadano getCiudadano() {
		return ciudadano;
	}

	public void setCiudadano(Ciudadano ciudadano) {
		this.ciudadano = ciudadano;
	}*/

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isPresentacionApagada() {
		return presentacionApagada;
	}

	public void setPresentacionApagada(boolean presentacionApagada) {
		this.presentacionApagada = presentacionApagada;
	}

	
	
}
