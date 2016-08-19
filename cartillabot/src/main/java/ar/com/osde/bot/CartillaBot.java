package ar.com.osde.bot;

import ar.com.osde.service.PlanCartillaClient;
import ar.com.osde.service.wsdl.PlanCartilla;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

public class CartillaBot extends TelegramLongPollingBot {
	private Logger LOGGER=Logger.getLogger(CartillaBot.class);

	@Autowired
	private PlanCartillaClient planClient;

	@Override
	public String getBotUsername() {
		return BotConfig.BOT_USERNAME;
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
		    planClient.getPlanesCartilla().getOut().getPlanCartilla();
			sendMessage.setText(text);
			sendMessage(sendMessage); 
		} catch (TelegramApiException e) {
			//do some error handling
		}
	}

}
