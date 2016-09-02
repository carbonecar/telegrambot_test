package ar.com.espherika.keyboard;

import java.util.ArrayList;
import java.util.List;

import org.drools.compiler.lang.dsl.DSLMapParser.mapping_file_return;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import com.fasterxml.jackson.core.sym.Name;

public class ReplyKeyboardSleep extends AbstractCustomKeyboard {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String MENOS_7 = "Menos de 7 hs";
	private static final String CANTIDAD_SALUDABLE = "Entre 7 y 8 hs";
	private static final String MAS_9 = "Más de 9 hs";

	public enum SLEEP_WELL_STATE {
		INTRODUCE_SLEEP("INTRODUCE_SLEEP"), REQUEST_INFO_SLEEP("REQUEST_INFO_SLEEP"), MENOS_7(
				"Menos de 7 hs"), REQUEST_NO_HELPS("Entre 7 y 8 hs"), MAS_9("Mas de 9 hs");
		private SLEEP_WELL_STATE(String myName) {
			name = myName;
		}

		
		public String getName() {
			return name;
		}
		
		public static SLEEP_WELL_STATE getByName(String name){
			SLEEP_WELL_STATE state=null;
			
			switch (name) {
			case "INTRODUCE_SLEEP":
				 state=INTRODUCE_SLEEP;
				break;
			case "REQUEST_INFO_SLEEP":
				state=REQUEST_INFO_SLEEP;
				break;
			case "Menos de 7 hs":
				state=MENOS_7;
				break;
			case "Entre 7 y 8 hs":
				state=REQUEST_NO_HELPS;
				break;
			case "Mas de 9 hs":
				state=MAS_9;
				break;
			default:
				break;
			}
			return state;
		}
		
		@Override
		public String toString() {
			return name;
		}

		private String name;
	}


	private static List<String> validResponses;

	public static List<String> getValidResponsesSleepTime() {
		if (validResponses == null) {
			validResponses = new ArrayList<String>();
			validResponses.add(MENOS_7);
			validResponses.add(CANTIDAD_SALUDABLE);
			validResponses.add(MAS_9);
		}

		return validResponses;
	}

	public ReplyKeyboardSleep() {

		this.setSelective(true);
		this.setResizeKeyboard(true);
		this.setOneTimeKeyboad(true);

		List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
		keyboard.add(buildRow(MENOS_7));
		keyboard.add(buildRow(CANTIDAD_SALUDABLE));
		keyboard.add(buildRow(MAS_9));
		setKeyboard(keyboard);

	}
}
