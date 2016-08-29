package ar.com.espherika.chatstrategies;

import java.util.HashMap;
import java.util.Map;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;

import ar.com.espherika.MenuKeyboardFactory;
import ar.com.espherika.MyFirstBot;

public class BotSmokerChatStrategy implements BotChatStrategy {

	private enum SMOKER_STATES {
		INTRODUCE, REQUEST_TARGET, WAITING_TARGET,WAITING_TARGET_REDUCE_NUMBER,WAITING_TARGET_ACTUAL_NUMBER,TARGET_SET, STOP_SMOKING_TARGET, REDUCE_SMOKING_TARGET;
	}

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
			sendMessage.setReplyMarkup(MenuKeyboardFactory.getReduceSmokeKeyboard());
			bot.sendControlledMessage(sendMessage,
					"Quieres que te ayude a dejar o reducir el consumo de tabaco diario?");
			this.chatIdStates.put(message.getChatId(), SMOKER_STATES.WAITING_TARGET);
			return;
		}

		if(this.getSafeState(message).equals(SMOKER_STATES.WAITING_TARGET)){
			sendMessage.setReplyMarkup(MenuKeyboardFactory.getMainMenuKeyboard());
			
			bot.sendControlledMessage(sendMessage,"Cuantos cigarrilos fumas?");
			this.chatIdStates.put(message.getChatId(), SMOKER_STATES.WAITING_TARGET_ACTUAL_NUMBER);
			
			return ;
		}
		
		if(this.getSafeState(message).equals(SMOKER_STATES.WAITING_TARGET_ACTUAL_NUMBER)){
			try{
				int actualCigaretteNumber=new Integer(message.getText());
				this.chatIdStates.put(message.getChatId(), SMOKER_STATES.WAITING_TARGET_REDUCE_NUMBER);
				
				
			}catch (NumberFormatException nfe){
				bot.sendControlledMessage(sendMessage,"Cuantos cigarrilos fumas? (Debe ser un numero)");
			}
		}
		
		
		if(this.getSafeState(message).equals(SMOKER_STATES.WAITING_TARGET_REDUCE_NUMBER)){
			try{								
				bot.sendControlledMessage(sendMessage,"A cuántos cigarrillos diarios te gustaría reducir)");
				this.chatIdStates.put(message.getChatId(), SMOKER_STATES.WAITING_TARGET_REDUCE_NUMBER);
				return;
			}catch (NumberFormatException nfe){
				bot.sendControlledMessage(sendMessage,"Cuantos cigarrilos fumas? (Debe ser un numero)");
			}
		}
		
		
		if(this.getSafeState(message).equals(SMOKER_STATES.WAITING_TARGET_REDUCE_NUMBER)){
			try{
				new Integer(message.getText());
				
				bot.sendControlledMessage(sendMessage,"A cuántos cigarrillos diarios te gustaría reducir?)");
				this.chatIdStates.put(message.getChatId(), SMOKER_STATES.TARGET_SET);
				return;
			}catch (NumberFormatException nfe){
				bot.sendControlledMessage(sendMessage,"A cuántos cigarrillos diarios te gustaría reducir? (Debe ser un numero)");
			}
		}

		
		if(this.getSafeState(message).equals(SMOKER_STATES.TARGET_SET)){
			try{
				int targetCigaretteNumber=new Integer(message.getText());
				bot.sendControlledMessage(sendMessage,"Excelente, a partir de ahora te preguntaré a lo largo del día para saber cuántos vas?)");
				bot.sendControlledMessage(sendMessage,"qué otro hábito saludable te gustaría adoptar?)");
				
				this.chatIdStates.put(message.getChatId(), SMOKER_STATES.INTRODUCE);
				bot.setRandomChatStrategy(sendMessage, message);
				return;
				
			}catch (NumberFormatException nfe){
				bot.sendControlledMessage(sendMessage,"A cuántos cigarrillos diarios te gustaría reducir? (Debe ser un numero)");
			}
		}

		// bot.setRandomChatStrategy(sendMessage, message);

	}

	private SMOKER_STATES getSafeState(Message message) {
		SMOKER_STATES myState = chatIdStates.get(message.getChatId());

		if (myState == null) {
			myState=SMOKER_STATES.INTRODUCE;
			this.chatIdStates.put(message.getChatId(), myState);
		}
		return myState;
	}

}
