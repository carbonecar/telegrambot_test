package ar.com.espherika.chatstrategies;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;

import ar.com.espherika.MyFirstBot;

public class BotSleepWellChatStrategy implements BotChatStrategy {

	@Override
	public void run(Message message, MyFirstBot bot) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(message.getChatId().toString());
		bot.sendControlledMessage(sendMessage, "Duermes bien?");

		bot.sendControlledMessage(sendMessage, "todavía estamos agregando hábitos saludables.");
		bot.setRandomChatStrategy(sendMessage, message);

	}

	@Override
	public void init(SendMessage sendMessage, Message message, MyFirstBot bot) {
		bot.sendControlledMessage(sendMessage, "Duermes realmente bien?");
		
	}

}
