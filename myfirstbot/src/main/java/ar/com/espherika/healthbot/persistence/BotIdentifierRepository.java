package ar.com.espherika.healthbot.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import ar.com.espherika.healthbot.model.BotIdentifier;

public interface BotIdentifierRepository  extends JpaRepository<BotIdentifier, String>{

}
