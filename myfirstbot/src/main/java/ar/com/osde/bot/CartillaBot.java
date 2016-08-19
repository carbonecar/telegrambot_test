package ar.com.osde.bot;

import ar.com.espherika.BotConfig;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

public class CartillaBot extends TelegramLongPollingBot {
	private Logger LOGGER=Logger.getLogger(CartillaBot.class);
	@Override
	public String getBotUsername() {
		return ar.com.espherika.BotConfig.BOT_USERNAME;
	}

	@Override
	public void onUpdateReceived(Update update) {

	}


	@Override
	public String getBotToken() {
		return BotConfig.BOT_TOKEN;
	}

	
	private void sendControlledMessage(SendMessage sendMessage,String text) {
		try {
			sendMessage.setText(text);
			sendMessage(sendMessage); 
		} catch (TelegramApiException e) {
			//do some error handling
		}
	}

}
