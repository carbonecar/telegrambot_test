package ar.com.espherika.keyboard;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

public class KeyboardBuilder {

	
	private List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
	

	/**
	 * 
	 * @param row . La fila del teclado tal cual como se espera que se vea.
	 * @return
	 */
	public KeyboardBuilder with(String rowText){
		this.keyboard.add(buildRow(rowText));
		return this;
	}
	
	public ReplyKeyboardMarkup build(){
		ReplyKeyboardMarkup replykeyboard=new ReplyKeyboardMarkup();
		
		replykeyboard.setSelective(true);
		replykeyboard.setResizeKeyboard(true);
		replykeyboard.setOneTimeKeyboad(true);
		replykeyboard.setKeyboard(keyboard);
		
		return replykeyboard;
	}
	
	
	
	private KeyboardRow buildRow(String text) {
		KeyboardRow keyboardRow = new KeyboardRow();
		keyboardRow.add(text);
		return keyboardRow;
	}
}
