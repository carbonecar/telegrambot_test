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

import ar.com.espherika.chatstrategies.BotChatStrategy;
import ar.com.espherika.chatstrategies.BotDrinkWaterChatStrategy;
import ar.com.espherika.chatstrategies.BotIntroduceChatStrategy;
import ar.com.espherika.healthbot.model.Habito;
import ar.com.espherika.healthbot.persistence.HabitoRepository;


public class MyFirstBot extends TelegramLongPollingBot {
	private Logger LOG = Logger.getLogger(MyFirstBot.class);
	private Map<Long, KieSession> chatIdKieSession = new HashMap<Long, KieSession>();
	
	public Map<Long,CHAT_STATES> chatIdStates=new HashMap<Long,CHAT_STATES>();
	public Map<CHAT_STATES,BotChatStrategy> chatStrategies=new HashMap<CHAT_STATES,BotChatStrategy>();
	
	private HabitoRepository habitoRepository;
	
	public MyFirstBot(){
		this.chatStrategies.put(CHAT_STATES.PRESENTACION, new BotIntroduceChatStrategy());
		this.chatStrategies.put(CHAT_STATES.HABITO_BEBER_AGUA_INICIADO, new BotDrinkWaterChatStrategy());
	}
	public HabitoRepository getHabitoRepository() {
		return habitoRepository;
	}

	public void setHabitoRepository(HabitoRepository habitoRepository) {
		this.habitoRepository = habitoRepository;
	}

	@Override
	public void onUpdateReceived(Update update) {
		if (update.hasMessage()) {
			Message message = update.getMessage();
			//KieSession kieSession = this.safeGetKieSession(message.getChatId());
			CHAT_STATES state=this.safeGetStates(message.getChatId());
			// check if the message has text. it could also contain for example
			// a location ( message.hasLocation() )
			if (message.hasText()) {

				// create an object that contains the information to send back
				// the message
				BotChatStrategy strategy=this.chatStrategies.get(state);
				if(strategy!=null){
					strategy.run(message, this);
					return;
				}
			}
		}

	}

	private CHAT_STATES safeGetStates(Long chatId) {
		CHAT_STATES state=this.chatIdStates.get(chatId);
		if (state==null){
			state=CHAT_STATES.PRESENTACION;
			this.chatIdStates.put(chatId, CHAT_STATES.PRESENTACION);
		}
		return state;
	}

	public Habito getHabitoBeberAgua() {
		return habitoRepository.findByCodigo("BEBER_AGUA");
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
	 * Devuelve session. Ya sea la existente o una nueva. TODO potencial
	 * factory.
	 * 
	 * @param chatId
	 * @return
	 */
	private KieSession safeGetKieSession(Long chatId) {

		KieSession kieSession = this.chatIdKieSession.get(chatId);
		if (kieSession == null) {
			KieServices kieSerivces = KieServices.Factory.get();
			// Leemos del classpath las reglas que aqui hay.
			KieContainer kieContainer = kieSerivces.getKieClasspathContainer();
			kieSession = kieContainer.newKieSession("ksession");
			this.chatIdKieSession.put(chatId, kieSession);
			LOG.info("Se crea una nueva session: " + chatId);
		}
		return kieSession;
	}

	public void sendControlledMessage(SendMessage sendMessage, String text) {
		try {
			sendMessage.setText(text);
			sendMessage.enableMarkdown(true);
			sendMessage(sendMessage);
		} catch (TelegramApiException e) {
			// Manejar el rror
		}
	}

}
