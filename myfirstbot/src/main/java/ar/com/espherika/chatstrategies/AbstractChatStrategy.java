package ar.com.espherika.chatstrategies;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;

import ar.com.espherika.MyFirstBot;

public class AbstractChatStrategy implements BotChatStrategy {

	@Override
	public void run(Message message, MyFirstBot bot) {
		SendMessage sendMessage=new SendMessage();
		sendMessage.setText("No puedo entenderte");
	}

}
