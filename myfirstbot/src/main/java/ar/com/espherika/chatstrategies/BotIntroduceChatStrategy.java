package ar.com.espherika.chatstrategies;

import static ar.com.espherika.MenuKeyboardFactory.getHideKeyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;

import ar.com.espherika.MenuKeyboardFactory;
import ar.com.espherika.MyFirstBot;
import ar.com.espherika.healthbot.model.Ciudadano;

public class BotIntroduceChatStrategy implements BotChatStrategy {

	private enum INTRODUCE_STATE {
		GENDER, AGE_REQUEST, AGE, WAIT_SEXUALITY_RESPONSE;
	}

	private Map<Long, INTRODUCE_STATE> chatIdStates = new HashMap<Long, INTRODUCE_STATE>();

	@Override
	public void run(Message message, MyFirstBot bot) {
		INTRODUCE_STATE state = this.getSafeState(message.getChatId());
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(message.getChatId().toString());
		if (INTRODUCE_STATE.GENDER.equals(state)) {
			Ciudadano ciudadano = bot.getCiudadano((message.getChatId().toString()));
			if (!ciudadano.getPreferenciasChat().isPresentacionApagada()) {
				String userFirstName = message.getFrom().getFirstName();
				try {
					Runtime.getRuntime().exec("./saludo.sh " + userFirstName).waitFor();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				bot.sendVoiceTo(message, "./presentacion_" + userFirstName + ".opus");

			}
			bot.sendControlledMessage(sendMessage, "Comencemos con algunas preguntas así nos conocemos un poco más.");

			sendMessage.setReplyMarkup(MenuKeyboardFactory.getGenderMenuKeyboard());
			sendMessage.setChatId(message.getChatId().toString());

			bot.sendControlledMessage(sendMessage, "¿Qué sexo eres?");
			this.chatIdStates.put(message.getChatId(), INTRODUCE_STATE.WAIT_SEXUALITY_RESPONSE);

			return;
		}

		// TODO sacar esto y hacerlo como en el habito dormir
		Collection<String> validSexualityRequest = new ArrayList<>();
		validSexualityRequest.add("Masculino");
		validSexualityRequest.add("Femenino");
		validSexualityRequest.add("Prefiero no brindar esa información");

		if (INTRODUCE_STATE.WAIT_SEXUALITY_RESPONSE.equals(state)) {
			if (validSexualityRequest.contains(message.getText())) {
				sendMessage.setReplyMarkup(getHideKeyboard());
				bot.sendControlledMessage(sendMessage, "¿Qué edad tienes?");
				this.chatIdStates.put(message.getChatId(), INTRODUCE_STATE.AGE);
				return;
			} else {
				bot.sendControlledMessage(sendMessage,
						"Mis respuestas son limitadas. Usa el teclado que puse para tí.");
			}
		}

		if (INTRODUCE_STATE.AGE.equals(state)) {
			try {
				Integer edad = new Integer(message.getText());
			} catch (NumberFormatException e) {
				bot.sendControlledMessage(sendMessage, "para tu edad, necesito que ingreses un número.");
				return;
			}

			bot.sendControlledMessage(sendMessage, " Muchas gracias " + message.getFrom().getFirstName() + ".");
			bot.sendControlledMessage(sendMessage,
					"Voy a sugerirte algunos hábitos saludables. A medida que nos conozcamos mas, intentaré recomendarte los que mejor son para tí.");

			bot.iniciarBeberAgua(sendMessage, message);

			return;
		}
	}

	private INTRODUCE_STATE getSafeState(Long chatId) {
		INTRODUCE_STATE state = this.chatIdStates.get(chatId);
		if (state == null) {
			state = INTRODUCE_STATE.GENDER;
			this.chatIdStates.put(chatId, state);
		}
		return state;
	}

}
