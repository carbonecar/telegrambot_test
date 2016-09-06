package ar.com.espherika.chatstrategies;

import static ar.com.espherika.MenuKeyboardFactory.getRequestInfoSleep;
import static ar.com.espherika.keyboard.ReplyKeyboardSleep.getValidResponsesSleepTime;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;

import ar.com.espherika.MenuKeyboardFactory;
import ar.com.espherika.MyFirstBot;
import ar.com.espherika.keyboard.ReplyKeyboardSleep.SLEEP_WELL_STATE;
import ar.com.espherika.utils.Time24HoursUtils;

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
		if(SLEEP_WELL_STATE.REQUEST_NO_HELPS.equals(SLEEP_WELL_STATE.getByName(message.getText()))){
			bot.sendControlledMessage(sendMessage, "Según la OMS duermes bien!");
			this.finishChat(sendMessage, message, bot);
		}
		
		if(SLEEP_WELL_STATE.MENOS_7.equals(SLEEP_WELL_STATE.getByName(message.getText()))){ 
			sendMessage.setReplyMarkup(MenuKeyboardFactory.getRequestYesNo());
			bot.sendControlledMessage(sendMessage, "Querés que te ayudemos a mejorar éste hábito?");
			return;
		}
		
		
		if(SLEEP_WELL_STATE.YES_INCREASE_HOUR.equals(SLEEP_WELL_STATE.getByName(message.getText()))){
			
			bot.sendControlledMessage(sendMessage, "A qué hora te despertás habitualmente? (usa el formato hh:mm)");
			chatIdStates.put(message.getChatId(), SLEEP_WELL_STATE.WAIT_INCREASE_HOUR);
			return;
		}
		
		if(SLEEP_WELL_STATE.WAIT_INCREASE_HOUR.equals(this.getSafeState(message))){
			String horaActualDespierta=message.getText();
			if(new Time24HoursUtils().validate(horaActualDespierta)){
				try {
					bot.sendControlledMessage(sendMessage, "te despierto a las :"+Time24HoursUtils.increase(horaActualDespierta,2)+"?");
				} catch (ParseException e) {
					bot.sendControlledMessage(sendMessage, "Dime una hora en formato hh:mm");
				}
				chatIdStates.put(message.getChatId(), SLEEP_WELL_STATE.WAIT_INCREASE_HOUR);
			}else{
				bot.sendControlledMessage(sendMessage, "Dime una hora en formato hh:mm");
			}
			return;
		}
		
		this.finishChat(sendMessage, message, bot);
		
		
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
