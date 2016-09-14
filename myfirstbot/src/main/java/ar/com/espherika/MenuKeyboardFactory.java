package ar.com.espherika;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardHide;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import ar.com.espherika.keyboard.ReplyKeyboardSleep;
import ar.com.espherika.keyboard.ReplyKeyboardSmoke;
import ar.com.espherika.keyboard.ReplyYesNoKeyBoard;

/**
 * Aca
 * 
 * @author MT27789605
 *
 */
public class MenuKeyboardFactory {

	public static ReplyKeyboard getHideKeyboard() {
		ReplyKeyboardHide replyKeyboardHide = new ReplyKeyboardHide();

		return replyKeyboardHide;
	}

	
	
	public static ReplyKeyboard getMainMenuKeyboard() {
		ReplyKeyboardMarkup replyKeyboard = new ReplyKeyboardMarkup();
		List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
		KeyboardRow keyboardFirsRow = new KeyboardRow();
		keyboardFirsRow.add("Consejos saludables");
		replyKeyboard.setSelective(true);
		replyKeyboard.setResizeKeyboard(true);
		keyboard.add(keyboardFirsRow);

		replyKeyboard.setKeyboard(keyboard);
		return replyKeyboard;
	}

	public static ReplyKeyboardMarkup getGenderMenuKeyboard() {
		ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
		replyKeyboardMarkup.setSelective(true);
		replyKeyboardMarkup.setResizeKeyboard(true);
		replyKeyboardMarkup.setOneTimeKeyboad(true);

		List<KeyboardRow> keyboard = new ArrayList<>();
		KeyboardRow keyboardFirstRow = new KeyboardRow();

		keyboardFirstRow.add("Masculino");

		KeyboardRow keyboardSecondRow = new KeyboardRow();
		keyboardSecondRow.add("Femenino");

		KeyboardRow keyboardThirdRow = new KeyboardRow();
		keyboardSecondRow.add("Prefiero no brindar esa información");

		keyboard.add(keyboardFirstRow);
		keyboard.add(keyboardSecondRow);
		keyboard.add(keyboardThirdRow);
		replyKeyboardMarkup.setKeyboard(keyboard);

		return replyKeyboardMarkup;
	}

	public static ReplyKeyboard getWaterBenefitKeyboard() {
		ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
		replyKeyboardMarkup.setSelective(true);
		replyKeyboardMarkup.setResizeKeyboard(true);
		replyKeyboardMarkup.setOneTimeKeyboad(true);

		List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();

		keyboard.add(buildRow("¿Por qué?"));
		keyboard.add(buildRow("Adoptar hábito"));
		keyboard.add(buildRow("Ya tengo el hábito"));
		keyboard.add(buildRow("Consejos saludables"));

		replyKeyboardMarkup.setKeyboard(keyboard);
		return replyKeyboardMarkup;
	}

	public static ReplyKeyboard getAdoptHabitWaterKeyboard() {
		ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
		replyKeyboardMarkup.setSelective(true);
		replyKeyboardMarkup.setResizeKeyboard(true);
		replyKeyboardMarkup.setOneTimeKeyboad(true);

		List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();

		keyboard.add(buildRow("1 vez por día"));
		keyboard.add(buildRow("2 veces por día"));
		//keyboard.add(buildRow("1 vez cada 3 días"));
		keyboard.add(buildRow("No, gracias"));

		replyKeyboardMarkup.setKeyboard(keyboard);
		return replyKeyboardMarkup;
	}
	
	public static ReplyKeyboard getSubscribeHabit() {
		ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
		replyKeyboardMarkup.setSelective(true);
		replyKeyboardMarkup.setResizeKeyboard(true);
		replyKeyboardMarkup.setOneTimeKeyboad(false);

		List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
		
		
		keyboard.add(buildRow("4","5","6","7","8","9 ","10 "));
		keyboard.add(buildRow("11","12", "13","14","15","16","17"));
		keyboard.add(buildRow("18 ","19 ", "20","21","22","23","24"));

		
		replyKeyboardMarkup.setKeyboard(keyboard);
		return replyKeyboardMarkup;
	}
	
	
	/**
	 * TODO crear constructores en la libreria de telegegram
	 * 
	 * @param text
	 * @return
	 */
	private static KeyboardRow buildRow(String text) {
		KeyboardRow keyboardRow = new KeyboardRow();
		keyboardRow.add(text);
		return keyboardRow;
	}
	private static KeyboardRow buildRow(String... text) {
		KeyboardRow keyboardRow = new KeyboardRow();
		for (int i = 0; i < text.length; i++) {
			keyboardRow.add(text[i]);
		}
		return keyboardRow;
	}

	public static ReplyKeyboard getSmokeKeyboard() {
		return new ReplyKeyboardSmoke();
	}

	public static ReplyKeyboard getReduceSmokeKeyboard() {
		ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
		replyKeyboardMarkup.setSelective(true);
		replyKeyboardMarkup.setResizeKeyboard(true);
		replyKeyboardMarkup.setOneTimeKeyboad(true);

		List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();

		keyboard.add(buildRow("Quiero dejar"));
		keyboard.add(buildRow("Quiero reducir"));
		keyboard.add(buildRow("No, gracias"));
		keyboard.add(buildRow("Consejos saludables"));
		replyKeyboardMarkup.setKeyboard(keyboard);
		return replyKeyboardMarkup;
	}
	
	public static ReplyKeyboard moreHealtyHabit(String caption){
		ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
		replyKeyboardMarkup.setSelective(true);
		replyKeyboardMarkup.setResizeKeyboard(true);
		replyKeyboardMarkup.setOneTimeKeyboad(true);

		List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();

		
		keyboard.add(buildRow(caption));
		
		replyKeyboardMarkup.setKeyboard(keyboard);
		return replyKeyboardMarkup;
	}
	public static ReplyKeyboard getRequestInfoSleep() {
		return new ReplyKeyboardSleep();
	}
	
	public static ReplyKeyboard getRequestYesNo(){
		return new ReplyYesNoKeyBoard();
	}
}
