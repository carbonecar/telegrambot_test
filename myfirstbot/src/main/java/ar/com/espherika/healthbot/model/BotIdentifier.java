package ar.com.espherika.healthbot.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 
 * @author carbonecar
 *
 */
@Entity
public class BotIdentifier {
	
	@Id
	private String botUsername;
	private String botToken;
	
	
	public String getBotUsername() {
		return botUsername;
	}
	public void setBotUsername(String botUsername) {
		this.botUsername = botUsername;
	}
	public String getBotToken() {
		return botToken;
	}
	public void setBotToken(String botToken) {
		this.botToken = botToken;
	}
}
