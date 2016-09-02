package ar.com.espherika.chatstrategies;

import java.util.HashMap;
import java.util.Map;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;

import ar.com.espherika.MenuKeyboardFactory;
import ar.com.espherika.MyFirstBot;
import static  ar.com.espherika.MenuKeyboardFactory.*;
import static ar.com.espherika.keyboard.ReplyKeyboardSleep.*;

public class BotSleepWellChatStrategy extends AbstractBotChatStrategy implements BotChatStrategy {


	private Map<Long, SLEEP_WELL_STATE> chatIdStates = new HashMap<Long, SLEEP_WELL_STATE>();

	@Override
	public void run(Message message, MyFirstBot bot) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(message.getChatId().toString());
		if (SLEEP_WELL_STATE.INTRODUCE_SLEEP.equals(getSafeState(message))) {
			sendMessage.setReplyMarkup(getRequestInfoSleep());
			bot.sendControlledMessage(sendMessage, "Cuántas horas duermes por dia?");
			chatIdStates.put(message.getChatId(), SLEEP_WELL_STATE.REQUEST_INFO_SLEEP);
			return;
		}

		if(SLEEP_WELL_STATE.REQUEST_INFO_SLEEP.equals(getSafeState(message))){
			String howManyHourSleep=message.getText();
			if(!getValidResponsesSleepTime().contains(howManyHourSleep)){
				bot.sendControlledMessage(sendMessage, "Todavía no estoy entrenado lo sufiente para entenderte. Usa el teclado que puse para tí.");
				return;
			}
		}
		if(SLEEP_WELL_STATE.getByName(message.getText()).equals(SLEEP_WELL_STATE.REQUEST_NO_HELPS)){
			bot.sendControlledMessage(sendMessage, "Según la OMS duermes bien!");
			this.finishChat(sendMessage, message, bot);
		}
		// bot.sendControlledMessage(sendMessage, "todavía estamos agregando
		// hábitos saludables.");
		// bot.setRandomChatStrategy(sendMessage, message);
		// this.finishChat(sendMessage, message, bot);

	}

	@Override
	protected void setInitialState(Long chatId) {
		this.chatIdStates.put(chatId, SLEEP_WELL_STATE.INTRODUCE_SLEEP);

	}

	private SLEEP_WELL_STATE getSafeState(Message message) {
		SLEEP_WELL_STATE myState = chatIdStates.get(message.getChatId());

		if (myState == null) {
			myState = SLEEP_WELL_STATE.INTRODUCE_SLEEP;
			this.chatIdStates.put(message.getChatId(), myState);
		}
		return myState;
	}

}
