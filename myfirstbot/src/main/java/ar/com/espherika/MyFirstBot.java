package ar.com.espherika;

import static ar.com.espherika.MenuKeyboardFactory.getWaterBenefitKeyboard;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.core.io.ClassPathResource;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendAudio;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.methods.send.SendVideo;
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

	public Map<Long, StateStrategy> chatIdStates = new HashMap<Long, StateStrategy>();
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
					this.sendVoiceTo(message,"presentacion_diego.opus");
					return;
				}

				if (message.getText().equals("/presentacion_off")) {
					Ciudadano ciudadano = this.getCiudadano(message.getChatId().toString());
					ciudadano.getPreferenciasChat().setPresentacionApagada(true);
					this.ciudadanoRepository.save(ciudadano);

					return;
				}

				if (message.getText().equals("/presentacion_on")) {
					Ciudadano ciudadano = this.getCiudadano(message.getChatId().toString());
					ciudadano.getPreferenciasChat().setPresentacionApagada(false);
					this.ciudadanoRepository.save(ciudadano);
					return;
				}

				if (message.getText().equals("/audio")) {
					this.sendAudioTo(message);
					return;
				}

				if (message.getText().equals("/photo")) {
					this.sendPhotoTo(message, "test.jpg");
					return;
				}
				if (message.getText().equals("Mas hábitos...") || message.getText().equals("Consejos saludables")) {
					this.setRandomChatStrategy(new SendMessage(), message);
					return;
				}
				BotChatStrategy strategy = this.chatIdStates.get(message.getChatId()).getBotChatStrategy();
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
		this.sendControlledMessage(sendMessage, "Solucion al problema de concurrencia. Map de estado por cliente. Refactor urgente a drools");
		this.sendControlledMessage(sendMessage, "Agregado de algunas fotos y videos");
		this.sendControlledMessage(sendMessage, "Borrado de la hora y la alerta de prueba en el habito 'Tomar agua'");

	}

	public void sendPhotoTo(Message message, String photoName) {
		SendPhoto sendPhotoRequest = new SendPhoto();
		sendPhotoRequest.setChatId(message.getChatId().toString());
		try {
			InputStream inputStreamPhoto = new ClassPathResource("pictures/" + photoName).getInputStream();
			sendPhotoRequest.setNewPhoto(photoName, inputStreamPhoto);
			this.sendPhoto(sendPhotoRequest);
		} catch (IOException e) {
			LOG.error(e);
		} catch (TelegramApiException e) {
			e.printStackTrace();
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
	 * 
	 * 
	 * @param message
	 * @param filename 
	 */
	public void sendVoiceTo(Message message, String filename) {

		SendVoice sendVoiceRequest = new SendVoice();
		sendVoiceRequest.setChatId(message.getChatId().toString());
		// sendVoiceRequest.setNewVoice("/Users/carbonecar/testprojects/testbot/telegrambotosde/pictures/opus_sample.opus",
		// "presentacion");
		try {
			InputStream inputStreamPresentacion = new ClassPathResource("sound/"+filename).getInputStream();
			sendVoiceRequest.setNewVoice("presentacion", inputStreamPresentacion);
			sendVoiceRequest.setDuration(7);
			sendVoice(sendVoiceRequest);
		} catch (TelegramApiException tae) {
			LOG.error(tae);
		} catch (IOException e) {
			LOG.error(e);
		}

	}

	public void sendVideoTo(Message message) {
		SendVideo sendVideoMessage = new SendVideo();
		sendVideoMessage.setChatId(message.getChatId().toString());
		// sendVoiceRequest.setNewVoice("/Users/carbonecar/testprojects/testbot/telegrambotosde/pictures/opus_sample.opus",
		// "presentacion");
		try {
			InputStream inputStreamPresentacion = new ClassPathResource("pictures/pulmon.mp4").getInputStream();
			// this.getClass().getClassLoader().getResourceAsStream("presentacion.opus");
			sendVideoMessage.setNewVideo("pulmon dañado", inputStreamPresentacion);
			// sendVoiceRequest.setNewVoice(new
			// File("/Users/carbonecar/testprojects/testbot/telegrambotosde/pictures/presentacion.opus"));
			sendVideo(sendVideoMessage);
		} catch (TelegramApiException tae) {
			LOG.error(tae);
		} catch (IOException e) {
			LOG.error(e);
		}

	}

	private ChatStates safeGetStates(Long chatId) {
		StateStrategy stateStrategy = this.chatIdStates.get(chatId);
		if (stateStrategy == null) {
			stateStrategy = new StateStrategy(ChatStates.PRESENTACION,
					this.chatStrategies.get(ChatStates.PRESENTACION));
			this.chatIdStates.put(chatId, stateStrategy);
			Ciudadano ciudadano = new Ciudadano(chatId.toString());
			ciudadano.setPreferenciasChat(new PreferenciasChat());
			this.ciudadanoRepository.save(ciudadano);
		}
		return stateStrategy.getChatState();
	}

	public Ciudadano getCiudadano(String id) {
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

	
	public void setRandomChatStrategy(SendMessage sendMessage, Message message) {
		 setRandomChatStrategy(sendMessage, message,this.safeGetStates(message.getChatId()));

	}

	/**
	 * TODO validar que ya se hubiera presentado.
	 * 
	 * @param chatId
	 */
	public void setRandomChatStrategy(SendMessage sendMessage, Message message,ChatStates exclude) {
		sendMessage.setChatId(message.getChatId().toString());
		int statesToExcludeCount=exclude==null?0:1;
		statesToExcludeCount+=1;
		int chatStrategiesCount = this.chatStrategies.keySet().size() - statesToExcludeCount;
		int indexRandomState = ((int) (Math.random() * chatStrategiesCount));

		ChatStates[] keySetArray = new ChatStates[chatStrategiesCount];
		this.chatStrategies.keySet();
		int index = 0;
		for (ChatStates chatState : this.chatStrategies.keySet()) {

			if (!chatState.equals(ChatStates.PRESENTACION) && !chatState.equals(exclude)) {
				keySetArray[index++] = chatState;
			}	
		}
		ChatStates chatState = keySetArray[indexRandomState];
		BotChatStrategy botChatStrategy;
		try {
			botChatStrategy = this.chatStrategies.get(chatState).getClass().newInstance();
			this.chatIdStates.put(message.getChatId(), new StateStrategy(chatState, botChatStrategy));
			BotChatStrategy chatStrategy = this.chatIdStates.get(message.getChatId()).getBotChatStrategy();
			chatStrategy.run(message, this);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

	}

	public void iniciarBeberAgua(SendMessage sendMessage, Message message) {
		Habito beberAgua = getHabitoBeberAgua();
		sendMessage.setReplyMarkup(getWaterBenefitKeyboard());
		sendControlledMessage(sendMessage, beberAgua.getMensajeIntroductorio());
		chatIdStates.put(message.getChatId(),
				new StateStrategy(ChatStates.HABITO_BEBER_AGUA_INICIADO, new BotDrinkWaterChatStrategy()));

	}

	public CiudadanoRepository getCiudadanoRepository() {
		return this.ciudadanoRepository;
	}

	public void setCiudadanoRepository(CiudadanoRepository ciudadanoRepository) {
		this.ciudadanoRepository = ciudadanoRepository;

	}

}
