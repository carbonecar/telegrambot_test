package ar.com.espherika.healthbot.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import ar.com.espherika.healthbot.model.Habito;

public interface HabitoRepository extends JpaRepository<Habito, String> {

	public Habito findByCodigo(String codigo);
}
