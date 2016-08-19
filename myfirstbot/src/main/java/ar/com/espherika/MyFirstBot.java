package ar.com.espherika;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

public class MyFirstBot extends TelegramLongPollingBot {
	private Logger LOG=Logger.getLogger(MyFirstBot.class);
	private Map<Long, KieSession> chatIdKieSession=new HashMap<Long,KieSession>();
	@Override
	public String getBotUsername() {
		return BotConfig.BOT_USERNAME;
	}

	@Override
	public void onUpdateReceived(Update update) {
		if(update.hasMessage()){
	        Message message = update.getMessage();
	        KieSession kieSession=this.safeGetKieSession(message.getChatId());
	        //check if the message has text. it could also contain for example a location ( message.hasLocation() )
	        if(message.hasText()){
	        	
	            //create an object that contains the information to send back the message
	            SendMessage sendMessage = new SendMessage();
	            sendMessage.setChatId(message.getChatId().toString()); //who should get from the message the sender that sent it.
	            sendControlledMessage(sendMessage,"Hola "+message.getFrom().getFirstName());
	            sendControlledMessage(sendMessage,"En que puedo ayudarte?");
	            sendControlledMessage(sendMessage, "tenme paciencia, estoy aprendiendo");
	            sendControlledMessage(sendMessage, "me dijiste: "+message.getText()+"? ");

	        }
	    }

	}


	@Override
	public String getBotToken() {
		return BotConfig.BOT_TOKEN;
	}

	
	/**
	 * Devuelve session. Ya sea la existente o una nueva.
	 * TODO potencial factory.
	 * @param chatId
	 * @return
	 */
	private KieSession safeGetKieSession(Long chatId){

		KieSession kieSession=this.chatIdKieSession.get(chatId);
		if(kieSession==null){
			KieServices kieSerivces = KieServices.Factory.get();
			// Leemos del classpath las reglas que aqui hay.
			KieContainer kieContainer = kieSerivces.getKieClasspathContainer();
			kieSession = kieContainer.newKieSession("ksession");
			this.chatIdKieSession.put(chatId, kieSession);
			LOG.info("Se crea una nueva session: "+chatId);
		}
		return kieSession;
	}
	
	private void sendControlledMessage(SendMessage sendMessage,String text) {
		try {
			
			sendMessage.setText(text);
			sendMessage(sendMessage); 
		} catch (TelegramApiException e) {
			//Manejar el rror
		}
	}

}
