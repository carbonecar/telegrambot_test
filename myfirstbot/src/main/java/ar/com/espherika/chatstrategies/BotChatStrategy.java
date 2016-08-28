package ar.com.espherika.chatstrategies;

import org.telegram.telegrambots.api.objects.Message;

import ar.com.espherika.MyFirstBot;

public interface BotChatStrategy {

	public void run(Message message,MyFirstBot bot);
	
}
