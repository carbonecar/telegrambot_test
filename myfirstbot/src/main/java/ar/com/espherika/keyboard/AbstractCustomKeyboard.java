package ar.com.espherika.keyboard;

import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

public class AbstractCustomKeyboard extends ReplyKeyboardMarkup {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String HABITOS_SALUDABLES = "Consejos saludables";

	
	protected KeyboardRow buildRow(String text) {
		KeyboardRow keyboardRow = new KeyboardRow();
		keyboardRow.add(text);
		return keyboardRow;
	}

	public AbstractCustomKeyboard() {
		super();
	}

}