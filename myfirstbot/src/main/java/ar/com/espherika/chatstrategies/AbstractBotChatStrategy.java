package ar.com.espherika.chatstrategies;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;

import ar.com.espherika.MenuKeyboardFactory;
import ar.com.espherika.MyFirstBot;

public abstract class AbstractBotChatStrategy {

	public AbstractBotChatStrategy() {
		super();
	}

	protected void finishChat(SendMessage sendMessage, Message message, MyFirstBot bot) {
		this.setInitialState(message.getChatId());
		sendMessage.setReplyMarkup(MenuKeyboardFactory.getMainMenuKeyboard());
		bot.sendControlledMessage(sendMessage, "Puedo ayudarte en algo más?");
	}

	abstract protected  void setInitialState(Long chatId);
		

}