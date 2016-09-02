package ar.com.espherika.keyboard;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

public class ReplyKeyboardSmoke extends AbstractCustomKeyboard {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String SI="Si";
	public static final String NO="No";
	public static final String NO_GRCIAS="No, gracias";
	public static final String NO_PERO_LO_FUI="No, pero lo fui";
	public static final String FUMADOR_SOCIAL="Soy fumador social";
	public static final String HABITOS_SALUDABLES="Consejos saludables";
	
	
	public ReplyKeyboardSmoke (){
		
		this.setSelective(true);
		this.setResizeKeyboard(true);
		this.setOneTimeKeyboad(true);

		List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
		keyboard.add(buildRow(SI));
		keyboard.add(buildRow(NO));
		keyboard.add(buildRow(NO_PERO_LO_FUI));
		keyboard.add(buildRow(FUMADOR_SOCIAL));
		keyboard.add(buildRow(HABITOS_SALUDABLES));
		
		this.setKeyboard(keyboard);
	}
	
	
}
