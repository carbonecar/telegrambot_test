package ar.com.espherika.chatstrategies;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;

import ar.com.espherika.MyFirstBot;

public class BotSuggestionsChatStrategy extends AbstractBotChatStrategy {

	@Override
	protected void setInitialState(Long chatId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run(Message message, MyFirstBot bot) {
		SendMessage sendMessage =new SendMessage();
		sendMessage.setChatId(message.getChatId().toString());
		bot.sendControlledMessage(sendMessage,"Ingresa tu sugerencia. Recuerda se lo mas preciso posible, tu esfuerzo en ser preciso me ayudará a comprender mejor y podré tener"
				+ " la seguridad de estar haciendo lo que necesitas.");
		
	}

}
