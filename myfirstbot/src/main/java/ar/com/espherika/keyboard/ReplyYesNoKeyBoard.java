package ar.com.espherika.keyboard;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

public class ReplyYesNoKeyBoard extends AbstractCustomKeyboard {

	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ReplyYesNoKeyBoard(){

			this.setSelective(true);
			this.setResizeKeyboard(true);
			this.setOneTimeKeyboad(true);

			List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
			
			keyboard.add(buildRow(ReplyKeyboardSleep.SLEEP_WELL_STATE.YES_INCREASE_HOUR.getName()));
			keyboard.add(buildRow(ReplyKeyboardSleep.SLEEP_WELL_STATE.NO_INCREASE_HOUR.getName()));
			keyboard.add(buildRow(ReplyKeyboardSleep.SLEEP_WELL_STATE.CONSEJOS_SALUDABLE.getName()));
		
			setKeyboard(keyboard);
			
	 }
	
	protected KeyboardRow buildRow(String text) {
		KeyboardRow keyboardRow = new KeyboardRow();
		keyboardRow.add(text);
		return keyboardRow;
	}
	

}
