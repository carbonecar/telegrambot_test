package ar.com.espherika.healthbot.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Habito {

	private String descipcion;
	@Id
	private String codigo;

	private String mensajeIntroductorio = "Beber 2 litros de agua por día ayuda a mejorar tu calidad de vida. Una excelente manera de comenzar, es tomando 2 vasos de agua en ayunas.";

	private String mensajeAlerta = "Buen dia recorda arrancar bebiendo 2 vasos de agua en ayunas";

	@OneToMany(cascade=CascadeType.ALL,fetch = FetchType.EAGER)
	private  List<Beneficio> beneficios = new ArrayList<Beneficio>();

	public Habito() {

	}

	public Habito(String codigo, String descripcion) {
		this.codigo = codigo;
		this.descipcion = descripcion;
	}

	public List<Beneficio> getBeneficios() {
		return beneficios;
	}

	public void setBeneficios(List<Beneficio> beneficios) {
		this.beneficios = beneficios;
	}

	public void addBeneficio(String descripcion) {
		this.beneficios.add(new Beneficio(descripcion));
	}

	public String getDescripcion() {
		return this.descipcion;
	}

	public String getCodigo() {
		return codigo;
	}

	public String getMensajeIntroductorio() {
		return this.mensajeIntroductorio;
	}

	public void setMensajeIntroductorio(String mensajeIntroductorio){
		this.mensajeIntroductorio=mensajeIntroductorio;
	}
	public String getMensajeAlerta() {
		return mensajeAlerta;
	}

	public void setMensajeAlerta(String mensajeAlerta) {
		this.mensajeAlerta = mensajeAlerta;
	}
}
