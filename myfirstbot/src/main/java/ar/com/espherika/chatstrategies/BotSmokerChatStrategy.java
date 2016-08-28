package ar.com.espherika.chatstrategies;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;

import ar.com.espherika.ChatStates;
import ar.com.espherika.MenuKeyboardFactory;
import ar.com.espherika.MyFirstBot;
import ar.com.espherika.healthbot.model.Habito;

public class BotSmokerChatStrategy implements BotChatStrategy {

	@Override
	public void run(Message message, MyFirstBot bot) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(message.getChatId().toString());
		bot.sendControlledMessage(sendMessage, "Fumas "+message.getFrom().getFirstName()+"?");

		bot.sendControlledMessage(sendMessage, "todavía estamos agregando hábitos saludables.");
		bot.setRandomChatStrategy(sendMessage, message);
	}
	
	
	public void init(SendMessage sendMessage,Message message,MyFirstBot bot){
		Habito fumar =bot.getHabitoRepository().findByCodigo("FUMAR");
		sendMessage.setReplyMarkup(MenuKeyboardFactory.getSmokeKeyboard());
		bot.sendControlledMessage(sendMessage, fumar.getMensajeIntroductorio());
		bot.chatIdStates.put(message.getChatId(), ChatStates.HABITO_FUMAR);
	}

}
