package ar.com.espherika;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

public class MyFirstBot extends TelegramLongPollingBot {
	private Logger LOGGER=Logger.getLogger(MyFirstBot.class);
	@Override
	public String getBotUsername() {
		return BotConfig.BOT_USERNAME;
	}

	@Override
	public void onUpdateReceived(Update update) {
		if(update.hasMessage()){
	        Message message = update.getMessage();

	        //check if the message has text. it could also contain for example a location ( message.hasLocation() )
	        if(message.hasText()){
	            //create an object that contains the information to send back the message
	            SendMessage sendMessage = new SendMessage();
	            System.out.println(message.getText());
	            sendMessage.setChatId(message.getChatId().toString()); //who should get from the message the sender that sent it.
	            
	            sendControlledMessage(sendMessage,"Hola "+message.getFrom().getFirstName());
	            sendControlledMessage(sendMessage,"En que puedo ayudarte?");
	            sendControlledMessage(sendMessage, "tenme paciencia, estoy aprendiendo");
	            sendControlledMessage(sendMessage, "me dijiste: "+message.getText()+"?");

	            
	        }
	    }

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
