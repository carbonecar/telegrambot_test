package ar.com.espherika.chatstrategies;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;

import ar.com.espherika.MyFirstBot;

public class BotSleepWellChatStrategy extends AbstractBotChatStrategy implements BotChatStrategy {

	@Override
	public void run(Message message, MyFirstBot bot) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(message.getChatId().toString());
		bot.sendControlledMessage(sendMessage, "Duermes bien?");

	
		bot.sendControlledMessage(sendMessage, "todavía estamos agregando hábitos saludables.");
		//bot.setRandomChatStrategy(sendMessage, message);
		this.finishChat(sendMessage, message, bot);
	

	}

	@Override
	protected void setInitialState(Long chatId) {
		// TODO Auto-generated method stub
		
	}

}
