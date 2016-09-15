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

	public enum SLEEP_WELL_STATE {
		INTRODUCE_SLEEP("INTRODUCE_SLEEP"), REQUEST_INFO_SLEEP("REQUEST_INFO_SLEEP"), MENOS_7(
				"Menos de 7 hs"), REQUEST_NO_HELPS("Entre 7 y 8 hs"), MAS_9("Mas de 9 hs"),
		YES_INCREASE_HOUR("Si, quiero dormir más"),NO_INCREASE_HOUR("No tengo interes en eso"), 
		WAIT_INCREASE_HOUR("WAIT_INCREASE_HOUR"),WAIT_DECREASE_HOUR("WAIT_DECREASE_HOUR"),
		CONSEJOS_SALUDABLE("Otro Consejo saludable"), CONFIRM_WAKE_UP_HOUR("Si, despiertame a esa hora"),
		DENY_WAKE_UP_HOUR("No, no me despiertes a esa hora"),
		YES_REDUCE_HOUR("Si, quiero dormir menos");
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
			case "Si, quiero dormir más":
				state= SLEEP_WELL_STATE.YES_INCREASE_HOUR;
				break;
			case "No tengo interes en eso":
				state=NO_INCREASE_HOUR;
			case "WAIT_INCREASE_HOUR":
				state=SLEEP_WELL_STATE.WAIT_INCREASE_HOUR;
				break;
			case "WAIT_REDUCE_HOUR":
				state=WAIT_DECREASE_HOUR;
				break;
			case "Otro Consejo saludable":
				state=CONSEJOS_SALUDABLE;
				break;
			case "Si, despiertame a esa hora":
				state=SLEEP_WELL_STATE.CONFIRM_WAKE_UP_HOUR;
				break;
			case "Si, quiero dormir menos":
				state=SLEEP_WELL_STATE.YES_REDUCE_HOUR;
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
			validResponses.add(SLEEP_WELL_STATE.MAS_9.getName());
			validResponses.add(SLEEP_WELL_STATE.WAIT_INCREASE_HOUR.getName());
			validResponses.add(SLEEP_WELL_STATE.WAIT_DECREASE_HOUR.getName());
			validResponses.add(SLEEP_WELL_STATE.YES_INCREASE_HOUR.getName());
			validResponses.add(SLEEP_WELL_STATE.NO_INCREASE_HOUR.getName());
			validResponses.add(SLEEP_WELL_STATE.YES_REDUCE_HOUR.getName());
			
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
		keyboard.add(buildRow(SLEEP_WELL_STATE.MAS_9.getName()));
		keyboard.add(buildRow("Consejos saludables"));
		setKeyboard(keyboard);

	}
}	
