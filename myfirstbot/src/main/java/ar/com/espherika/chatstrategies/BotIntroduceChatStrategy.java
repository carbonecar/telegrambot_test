package ar.com.espherika.chatstrategies;

import static ar.com.espherika.MenuKeyboardFactory.getHideKeyboard;
import static ar.com.espherika.MenuKeyboardFactory.getWaterBenefitKeyboard;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;

import ar.com.espherika.CHAT_STATES;
import ar.com.espherika.MenuKeyboardFactory;
import ar.com.espherika.MyFirstBot;
import ar.com.espherika.healthbot.model.Habito;

public class BotIntroduceChatStrategy implements BotChatStrategy {

	private enum INTRODUCE_STATE {
		GENDER, DATE_REQUEST,DATE;
	}

	private Map<Long, INTRODUCE_STATE> chatIdStates = new HashMap<Long, INTRODUCE_STATE>();

	@Override
	public void run(Message message, MyFirstBot bot) {
		INTRODUCE_STATE state = this.getSafeState(message.getChatId());
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(message.getChatId().toString());
		if (INTRODUCE_STATE.GENDER.equals(state)) {
			bot.sendControlledMessage(sendMessage, "Hola " + message.getFrom().getFirstName());
			bot.sendControlledMessage(sendMessage, "En que puedo ayudarte?");
			bot.sendControlledMessage(sendMessage, "tenme paciencia, estoy aprendiendo");

			sendMessage.setReplyMarkup(MenuKeyboardFactory.getGenderMenuKeyboard());
			sendMessage.setChatId(message.getChatId().toString());

			bot.sendControlledMessage(sendMessage, "Qué sexo eres?");
			this.chatIdStates.put(message.getChatId(), INTRODUCE_STATE.DATE_REQUEST);
			
			return;
		}
		
		if(INTRODUCE_STATE.DATE_REQUEST.equals(state)){
			sendMessage.setReplyMarkup(getHideKeyboard());
			bot.sendControlledMessage(sendMessage, "Cuándo naciste? (Ingrese en formato dd/mm/aaaa)");
			this.chatIdStates.put(message.getChatId(), INTRODUCE_STATE.DATE);
		}
		if (INTRODUCE_STATE.DATE.equals(state)) {
			try {
				Date date = new SimpleDateFormat("dd/MM/yyyy").parse(message.getText());
			} catch (ParseException e) {
				bot.sendControlledMessage(sendMessage, "por favor ingrese la fecha en formato dd/MM/yyyy");
				return ;
			}

			sendMessage.setReplyMarkup(getWaterBenefitKeyboard());
			bot.sendControlledMessage(sendMessage, "Recordare esa fecha y te saludaré en tu compleaños");
			bot.sendControlledMessage(sendMessage, " Muchas gracias " + message.getFrom().getFirstName() + ".");
			Habito beberAgua = bot.getHabitoBeberAgua();
			bot.sendControlledMessage(sendMessage, beberAgua.getMensajeIntroductorio());
			bot.chatIdStates.put(message.getChatId(), CHAT_STATES.HABITO_BEBER_AGUA_INICIADO);

			return;
		}
	}

	private INTRODUCE_STATE getSafeState(Long chatId) {
		INTRODUCE_STATE state = this.chatIdStates.get(chatId);
		if (state == null) {
			state=INTRODUCE_STATE.GENDER;
			this.chatIdStates.put(chatId,state);
		}
		return state;
	}

}
