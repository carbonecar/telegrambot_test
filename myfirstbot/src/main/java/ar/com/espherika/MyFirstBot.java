package ar.com.espherika;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardHide;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

public class MyFirstBot extends TelegramLongPollingBot {
	private Logger LOG=Logger.getLogger(MyFirstBot.class);
	private Map<Long, KieSession> chatIdKieSession=new HashMap<Long,KieSession>();

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
	            if(message.getText().equals("Masculino")){
	            	sendMessage.setReplyMarkup(getMainMenuKeyboard("castellano"));
	            	sendControlledMessage(sendMessage, "machito dijo la partera.");
	            	return;
	            }
	            sendControlledMessage(sendMessage,"Hola "+message.getFrom().getFirstName());
	            sendControlledMessage(sendMessage,"En que puedo ayudarte?");
	            sendControlledMessage(sendMessage, "tenme paciencia, estoy aprendiendo");

	            sendMessage.setReplyMarkup(getSexMenuKeyboard("Castellano"));
	            sendControlledMessage(sendMessage, "Qué sexo eres?");

	        }
	    }

	}


	private ReplyKeyboard getHideKeyboard() {
		ReplyKeyboardHide replyKeyboardHide=new ReplyKeyboardHide();
		
		return replyKeyboardHide;
	}


	private  ReplyKeyboard getMainMenuKeyboard(String languange){
		ReplyKeyboardMarkup replyKeyboard =new ReplyKeyboardMarkup();
		List<KeyboardRow> keyboard=new ArrayList<KeyboardRow>();
		KeyboardRow keyboardFirsRow=new KeyboardRow();
		keyboardFirsRow.add("Consejos saludables");
		replyKeyboard.setSelective(true);
		replyKeyboard.setResizeKeyboard(true);
		keyboard.add(keyboardFirsRow);
		
		replyKeyboard.setKeyboard(keyboard);
		return replyKeyboard;
	}
	
	private  ReplyKeyboardMarkup getSexMenuKeyboard(String language) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(true);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        
        keyboardFirstRow.add("Masculino");
        
        KeyboardRow keyboardSecondRow=new KeyboardRow();
        keyboardSecondRow.add("Femenino");
        
        
        keyboard.add(keyboardFirstRow);        
        keyboard.add(keyboardSecondRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
        
        
        return replyKeyboardMarkup;
    }
	
	@Override
	public String getBotUsername() {
		return BotConfig.BOT_USERNAME_PRUEBACARLOS_BOT;
	}
	@Override
	public String getBotToken() {
		return BotConfig.BOT_TOKEN_PRUEBA_CARLOS;
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
			sendMessage.enableMarkdown(true);
			sendMessage(sendMessage); 
		} catch (TelegramApiException e) {
			//Manejar el rror
		}
	}

}
