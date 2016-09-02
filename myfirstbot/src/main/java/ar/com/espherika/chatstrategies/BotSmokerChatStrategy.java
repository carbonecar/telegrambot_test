package ar.com.espherika.chatstrategies;

import java.util.HashMap;
import java.util.Map;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;

import ar.com.espherika.ChatStates;
import ar.com.espherika.MenuKeyboardFactory;
import ar.com.espherika.MyFirstBot;
import ar.com.espherika.keyboard.ReplyKeyboardSmoke;

public class BotSmokerChatStrategy extends AbstractBotChatStrategy implements BotChatStrategy {

	private enum SMOKER_STATES {
		INTRODUCE, REQUEST_TARGET, REQUEST_TARGET_ACTUAL_NUMBER, WAITING_TARGET_REDUCE_NUMBER, REQUEST_TARGET_REDUCE_NUMBER, WAITING_TARGET_ACTUAL_NUMBER, STOP_SMOKING_TARGET, REDUCE_SMOKING_TARGET;
	}

	// Este mapa no es necesario.
	// TODO dejar solo el estado
	private Map<Long, SMOKER_STATES> chatIdStates = new HashMap<Long, SMOKER_STATES>();

	@Override
	public void run(Message message, MyFirstBot bot) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(message.getChatId().toString());

		if (this.getSafeState(message).equals(SMOKER_STATES.INTRODUCE)) {
			sendMessage.setReplyMarkup(MenuKeyboardFactory.getSmokeKeyboard());
			bot.sendControlledMessage(sendMessage,
					"Sos fumador actualmente  " + message.getFrom().getFirstName() + "?");
			this.chatIdStates.put(message.getChatId(), SMOKER_STATES.REQUEST_TARGET);
			return;
		}

		if (this.getSafeState(message).equals(SMOKER_STATES.REQUEST_TARGET)) {
			requestTarget(message, bot, sendMessage);
			return;
		}

		if (this.getSafeState(message).equals(SMOKER_STATES.REQUEST_TARGET_ACTUAL_NUMBER)) {
			if (message.getText().equals(ReplyKeyboardSmoke.NO_GRCIAS)) {
				this.endChatStrategy(message, bot, sendMessage);
			} else {
				sendMessage.setReplyMarkup(MenuKeyboardFactory.getMainMenuKeyboard());
				bot.sendControlledMessage(sendMessage, "Cuántos cigarrilos fumas?");
				this.chatIdStates.put(message.getChatId(), SMOKER_STATES.WAITING_TARGET_ACTUAL_NUMBER);
			}
			return;
		}

		if (this.getSafeState(message).equals(SMOKER_STATES.WAITING_TARGET_ACTUAL_NUMBER)) {
			try {
				new Integer(message.getText());
				this.chatIdStates.put(message.getChatId(), SMOKER_STATES.REQUEST_TARGET_REDUCE_NUMBER);
			} catch (NumberFormatException nfe) {
				bot.sendControlledMessage(sendMessage, "Cuántos cigarrilos fumás? (Debe ser un número)");
			}
		}

		if (this.getSafeState(message).equals(SMOKER_STATES.REQUEST_TARGET_REDUCE_NUMBER)) {
			bot.sendControlledMessage(sendMessage, "A cuántos cigarrillos diarios te gustaría reducir)");
			this.chatIdStates.put(message.getChatId(), SMOKER_STATES.WAITING_TARGET_REDUCE_NUMBER);
			return;

		}

		if (this.getSafeState(message).equals(SMOKER_STATES.WAITING_TARGET_REDUCE_NUMBER)) {
			try {
				new Integer(message.getText());
				int targetCigaretteNumber = new Integer(message.getText());
				bot.sendControlledMessage(sendMessage,
						"Excelente, a partir de ahora te preguntará a lo largo del día para saber cuántos vas?)");
				endChatStrategy(message, bot, sendMessage);
				return;
			} catch (NumberFormatException nfe) {
				bot.sendControlledMessage(sendMessage,
						"A cuántos cigarrillos diarios te gustaría reducir? (Debe ser un número)");
			}
		}

		// bot.setRandomChatStrategy(sendMessage, message);

	}

	/**
	 * Quiere hacer algo con su hábito
	 * 
	 * @param message
	 * @param bot
	 * @param sendMessage
	 */
	private void requestTarget(Message message, MyFirstBot bot, SendMessage sendMessage) {
		String userMessage = message.getText();
		switch (userMessage) {
		case ReplyKeyboardSmoke.SI:
			sendMessage.setReplyMarkup(MenuKeyboardFactory.getReduceSmokeKeyboard());
			bot.sendControlledMessage(sendMessage, "https://www.youtube.com/watch?v=71wtRD76vpc");
			bot.sendControlledMessage(sendMessage,
					"Quieres que te ayude a dejar o reducir el consumo de tabaco diario?");

			this.chatIdStates.put(message.getChatId(), SMOKER_STATES.REQUEST_TARGET_ACTUAL_NUMBER);
			break;
		case ReplyKeyboardSmoke.NO:
			this.endChatStrategy(message, bot, sendMessage);

			break;
		case ReplyKeyboardSmoke.NO_GRCIAS:
			this.endChatStrategy(message, bot, sendMessage);
		case ReplyKeyboardSmoke.NO_PERO_LO_FUI:
			bot.sendPhotoTo(message, "pulmon.jpg");
			bot.sendControlledMessage(sendMessage,
					"Incluso habiendo sido fumador algunos años es posible que exista un daño");
			this.endChatStrategy(message, bot, sendMessage);

			break;
		case ReplyKeyboardSmoke.FUMADOR_SOCIAL:
			bot.sendControlledMessage(sendMessage,
					"Incluso en bajas cantidades el tabaco sigue siendo perjudicial para tu salud.");

			bot.sendControlledMessage(sendMessage, "http://www.who.int/mediacentre/factsheets/fs339/es/");
			bot.sendControlledMessage(sendMessage,
					"Quieres que te ayude a dejar o reducir el consumo de tabaco diario?");
			this.chatIdStates.put(message.getChatId(), SMOKER_STATES.REQUEST_TARGET_ACTUAL_NUMBER);
			break;
		case ReplyKeyboardSmoke.HABITOS_SALUDABLES:
			this.endChatStrategy(message, bot, sendMessage);
			break;
		default:
			break;
		}
	}

	private void endChatStrategy(Message message, MyFirstBot bot, SendMessage sendMessage) {
		sendMessage.setReplyMarkup(MenuKeyboardFactory.getMainMenuKeyboard());
		this.chatIdStates.put(message.getChatId(), SMOKER_STATES.INTRODUCE);
		bot.sendControlledMessage(sendMessage, "qué otro hábito saludable te gustaría adoptar?");
		bot.setRandomChatStrategy(sendMessage, message);
	}

	private SMOKER_STATES getSafeState(Message message) {
		SMOKER_STATES myState = chatIdStates.get(message.getChatId());

		if (myState == null) {
			myState = SMOKER_STATES.INTRODUCE;
			this.chatIdStates.put(message.getChatId(), myState);
		}
		return myState;
	}

	@Override
	protected void setInitialState(Long chatId) {
		this.chatIdStates.put(chatId, SMOKER_STATES.INTRODUCE);

	}

}
