package ar.com.espherika;

import static ar.com.espherika.MenuKeyboardFactory.getWaterBenefitKeyboard;

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
import ar.com.espherika.chatstrategies.BotSleepWellChatStrategy;
import ar.com.espherika.chatstrategies.BotSmokerChatStrategy;
import ar.com.espherika.healthbot.model.Habito;
import ar.com.espherika.healthbot.persistence.HabitoRepository;


public class MyFirstBot extends TelegramLongPollingBot {
	private Logger LOG = Logger.getLogger(MyFirstBot.class);
	private Map<Long, KieSession> chatIdKieSession = new HashMap<Long, KieSession>();
	
	//TODO es evidente que esto va a pinchar feo con mas de 1 usuario, no?
	public Map<Long,ChatStates> chatIdStates=new HashMap<Long,ChatStates>();
	public Map<ChatStates,BotChatStrategy> chatStrategies=new HashMap<ChatStates,BotChatStrategy>();
	
	private HabitoRepository habitoRepository;
	
	public MyFirstBot(){
		this.chatStrategies.put(ChatStates.PRESENTACION, new BotIntroduceChatStrategy());
		this.chatStrategies.put(ChatStates.HABITO_BEBER_AGUA_INICIADO, new BotDrinkWaterChatStrategy());
		this.chatStrategies.put(ChatStates.HABITO_FUMAR, new BotSmokerChatStrategy());
		this.chatStrategies.put(ChatStates.HABITO_DORMIR, new BotSleepWellChatStrategy());
		
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
			ChatStates state=this.safeGetStates(message.getChatId());
			
			if (message.hasText()) {
				if (message.getText().equals("Mas hábitos...")|| message.getText().equals("Consejos saludables")) {
					this.setRandomChatStrategy(new SendMessage(),message);
					return;
				}
				BotChatStrategy strategy=this.chatStrategies.get(state);
				if(strategy!=null){
					strategy.run(message, this);
					return;
				}
			}
		}

	}

	
	private ChatStates safeGetStates(Long chatId) {
		ChatStates state=this.chatIdStates.get(chatId);
		if (state==null){
			state=ChatStates.PRESENTACION;
			this.chatIdStates.put(chatId, ChatStates.PRESENTACION);
		}
		return state;
	}

	public Habito getHabitoBeberAgua() {
		return habitoRepository.findByCodigo("BEBER_AGUA");
	}

	@Override
	public String getBotUsername() {
		return BotConfig.BOT_USERNAME_CARBONECAR;
	}

	@Override
	public String getBotToken() {
		return BotConfig.BOT_TOKEN_CARBONECAR_BOT;
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
	
	
	/**
	 * TODO validar que ya se hubiera presentado. 
	 * @param chatId
	 */
	public void setRandomChatStrategy(SendMessage sendMessage,Message message){
		sendMessage.setChatId(message.getChatId().toString());
		int chatStrategiesCount=this.chatStrategies.keySet().size()-1;
		int indexRandomState=((int)(Math.random()*chatStrategiesCount));
		
		ChatStates[] keySetArray=new ChatStates[chatStrategiesCount];
		this.chatStrategies.keySet();
		int index=0;
		for (ChatStates chatState : this.chatStrategies.keySet()) {
			
			if(!chatState.equals(ChatStates.PRESENTACION)){
				keySetArray[index++]=chatState;
			}
		}
		this.chatIdStates.put(message.getChatId(), keySetArray[indexRandomState]);
		BotChatStrategy chatStrategy=this.chatStrategies.get(this.chatIdStates.get(message.getChatId()));
		System.out.println(chatStrategy.getClass().getName());
		chatStrategy.run(message, this);
		
	}
	
	
	
	public void iniciarBeberAgua(SendMessage sendMessage, Message message) {
		Habito beberAgua = getHabitoBeberAgua();
		sendMessage.setReplyMarkup(getWaterBenefitKeyboard());
		sendControlledMessage(sendMessage, beberAgua.getMensajeIntroductorio());
		chatIdStates.put(message.getChatId(), ChatStates.HABITO_BEBER_AGUA_INICIADO);
		
	}

}
