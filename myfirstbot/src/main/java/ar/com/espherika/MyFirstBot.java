package ar.com.espherika;

import static ar.com.espherika.MenuKeyboardFactory.getWaterBenefitKeyboard;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.core.io.ClassPathResource;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendAudio;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.methods.send.SendVoice;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import ar.com.espherika.chatstrategies.BotChatStrategy;
import ar.com.espherika.chatstrategies.BotDrinkWaterChatStrategy;
import ar.com.espherika.chatstrategies.BotIntroduceChatStrategy;
import ar.com.espherika.chatstrategies.BotSleepWellChatStrategy;
import ar.com.espherika.chatstrategies.BotSmokerChatStrategy;
import ar.com.espherika.healthbot.model.BotIdentifier;
import ar.com.espherika.healthbot.model.Ciudadano;
import ar.com.espherika.healthbot.model.Habito;
import ar.com.espherika.healthbot.model.PreferenciasChat;
import ar.com.espherika.healthbot.persistence.CiudadanoRepository;
import ar.com.espherika.healthbot.persistence.HabitoRepository;

public class MyFirstBot extends TelegramLongPollingBot {
	private Logger LOG = Logger.getLogger(MyFirstBot.class);
	private Map<Long, KieSession> chatIdKieSession = new HashMap<Long, KieSession>();

	// TODO es evidente que esto va a pinchar feo con mas de 1 usuario, no?
	public Map<Long, ChatStates> chatIdStates = new HashMap<Long, ChatStates>();
	public Map<ChatStates, BotChatStrategy> chatStrategies = new HashMap<ChatStates, BotChatStrategy>();

	private HabitoRepository habitoRepository;
	private CiudadanoRepository ciudadanoRepository;
	private BotIdentifier botIdentifier;

	public MyFirstBot(BotIdentifier botIdentifier) {
		this.botIdentifier = botIdentifier;
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

			// KieSession kieSession =
			// this.safeGetKieSession(message.getChatId());
			ChatStates state = this.safeGetStates(message.getChatId());

			if (message.hasText()) {
				if (message.getText().equals("/news")) {
					this.showNews(message);
					return;
				}

				if (message.getText().equals("/voice")) {
					this.sendVoiceTo(message);
					return;
				}

				if (message.getText().equals("/presentacion_off")) {
					Ciudadano ciudadano = this.getCiudadano(message.getChatId().toString());
					ciudadano.getPreferenciasChat().setPresentacionApagada(true);
					this.ciudadanoRepository.save(ciudadano);

					return;
				}

				if (message.getText().equals("/audio")) {
					this.sendAudioTo(message);
					return;
				}

				if (message.getText().equals("/photo")) {
					this.sendPhotoTo(message);
					return;
				}
				if (message.getText().equals("Mas hábitos...") || message.getText().equals("Consejos saludables")) {
					this.setRandomChatStrategy(new SendMessage(), message);
					return;
				}
				BotChatStrategy strategy = this.chatStrategies.get(state);
				if (strategy != null) {
					strategy.run(message, this);
					return;
				}
			}
		}

	}

	/**
	 * TODO: que envie un archivo con los change set que se saca de git Envia un
	 * mensaje con los cambios que se hicieron
	 * 
	 * @param message
	 */
	private void showNews(Message message) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(message.getChatId().toString());
		this.sendControlledMessage(sendMessage, "Agregado de preferencias de chat del usuario (en memoria)");
		this.sendControlledMessage(sendMessage, "Parametrizacion del bot (username y token)");
		this.sendControlledMessage(sendMessage, "Borrado de la hora y la alerta de prueba en el habito 'Tomar agua'");
		this.sendControlledMessage(sendMessage, "Funcion de off para el apagado de la presentacion");
		this.sendControlledMessage(sendMessage, "Si no se es fumador sugiere otro habito en lugar de continuar como si lo fuera");


	}

	private void sendPhotoTo(Message message) {
		SendPhoto sendPhotoRequest = new SendPhoto();
		sendPhotoRequest.setChatId(message.getChatId().toString());
		// path: String, photoName: String
		sendPhotoRequest.setNewPhoto("/Users/carbonecar/testprojects/testbot/telegrambotosde/pictures/te_verde.jpg",
				"te_verde.jpg"); //
		try {
			sendPhoto(sendPhotoRequest);
		} catch (TelegramApiException e) {

			LOG.error(e);
			/*
			 * Do some error handling e.printStackTrace();
			 */
		}

	}

	/**
	 * For testing..
	 * 
	 * @param message
	 */
	private void sendAudioTo(Message message) {
		SendAudio sendAudioRequest = new SendAudio();
		sendAudioRequest.setChatId(message.getChatId().toString());
		// sendAudioRequest.setNewAudio("/Users/carbonecar/testprojects/testbot/telegrambotosde/pictures/presentacion.mp3",
		// "presentacion");
		sendAudioRequest.setNewAudio(
				new File("/Users/carbonecar/testprojects/testbot/telegrambotosde/pictures/presentacion.mp3"));
		try {
			sendAudio(sendAudioRequest);
		} catch (TelegramApiException tae) {
			LOG.error(tae);
		}

	}

	/**
	 * For testing..
	 * 
	 * @param message
	 */
	public void sendVoiceTo(Message message) {

		SendVoice sendVoiceRequest = new SendVoice();
		sendVoiceRequest.setChatId(message.getChatId().toString());
		// sendVoiceRequest.setNewVoice("/Users/carbonecar/testprojects/testbot/telegrambotosde/pictures/opus_sample.opus",
		// "presentacion");
		try {
			InputStream inputStreamPresentacion = new ClassPathResource("sound/presentacion.opus").getInputStream();
			// this.getClass().getClassLoader().getResourceAsStream("presentacion.opus");
			sendVoiceRequest.setNewVoice("presentacion", inputStreamPresentacion);
			// sendVoiceRequest.setNewVoice(new
			// File("/Users/carbonecar/testprojects/testbot/telegrambotosde/pictures/presentacion.opus"));
			sendVoiceRequest.setDuration(7);
			sendVoice(sendVoiceRequest);
		} catch (TelegramApiException tae) {
			LOG.error(tae);
		} catch (IOException e) {
			LOG.error(e);
		}

	}

	private ChatStates safeGetStates(Long chatId) {
		ChatStates state = this.chatIdStates.get(chatId);
		if (state == null) {
			state = ChatStates.PRESENTACION;
			this.chatIdStates.put(chatId, ChatStates.PRESENTACION);
			Ciudadano ciudadano = new Ciudadano(chatId.toString());
			ciudadano.setPreferenciasChat(new PreferenciasChat());
			this.ciudadanoRepository.save(ciudadano);
		}
		return state;
	}

	
	public Ciudadano getCiudadano(String id){
		return ciudadanoRepository.findOne(id);
	}
	public Habito getHabitoBeberAgua() {
		return habitoRepository.findByCodigo("BEBER_AGUA");
	}

	@Override
	public String getBotUsername() {
		return this.botIdentifier.getBotUsername();
	}

	@Override
	public String getBotToken() {
		return this.botIdentifier.getBotToken();
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
	 * 
	 * @param chatId
	 */
	public void setRandomChatStrategy(SendMessage sendMessage, Message message) {
		sendMessage.setChatId(message.getChatId().toString());
		int chatStrategiesCount = this.chatStrategies.keySet().size() - 1;
		int indexRandomState = ((int) (Math.random() * chatStrategiesCount));

		ChatStates[] keySetArray = new ChatStates[chatStrategiesCount];
		this.chatStrategies.keySet();
		int index = 0;
		for (ChatStates chatState : this.chatStrategies.keySet()) {

			if (!chatState.equals(ChatStates.PRESENTACION)) {
				keySetArray[index++] = chatState;
			}
		}
		this.chatIdStates.put(message.getChatId(), keySetArray[indexRandomState]);
		BotChatStrategy chatStrategy = this.chatStrategies.get(this.chatIdStates.get(message.getChatId()));
		chatStrategy.run(message, this);

	}

	public void iniciarBeberAgua(SendMessage sendMessage, Message message) {
		Habito beberAgua = getHabitoBeberAgua();
		sendMessage.setReplyMarkup(getWaterBenefitKeyboard());
		sendControlledMessage(sendMessage, beberAgua.getMensajeIntroductorio());
		chatIdStates.put(message.getChatId(), ChatStates.HABITO_BEBER_AGUA_INICIADO);

	}

	public CiudadanoRepository getCiudadanoRepository() {
		return this.ciudadanoRepository;
	}

	public void setCiudadanoRepository(CiudadanoRepository ciudadanoRepository) {
		this.ciudadanoRepository = ciudadanoRepository;

	}

}
