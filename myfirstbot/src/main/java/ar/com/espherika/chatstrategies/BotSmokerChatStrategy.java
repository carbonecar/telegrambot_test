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

		sendMessage.setReplyMarkup(MenuKeyboardFactory.getSmokeKeyboard());
		bot.sendControlledMessage(sendMessage, "todavía estamos agregando hábitos saludables.");
		//bot.setRandomChatStrategy(sendMessage, message);
	
	
	}
	


}
