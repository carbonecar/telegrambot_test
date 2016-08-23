package ar.com.espherika;

import static ar.com.espherika.MenuKeyboardFactory.*;

import java.util.HashMap;
import java.util.Iterator;
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
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import ar.com.espherika.healthbot.model.Beneficio;
import ar.com.espherika.healthbot.model.Habito;

public class MyFirstBot extends TelegramLongPollingBot {
	private Logger LOG = Logger.getLogger(MyFirstBot.class);
	private Map<Long, KieSession> chatIdKieSession = new HashMap<Long, KieSession>();

	@Override
	public void onUpdateReceived(Update update) {
		if (update.hasMessage()) {
			Message message = update.getMessage();
			KieSession kieSession = this.safeGetKieSession(message.getChatId());
			// check if the message has text. it could also contain for example
			// a location ( message.hasLocation() )
			if (message.hasText()) {

				// create an object that contains the information to send back
				// the message
				String firstName = message.getFrom().getFirstName();
				SendMessage sendMessage = new SendMessage();
				sendMessage.setChatId(message.getChatId().toString());
				if (message.getText().equals("Masculino")) {
					sendMessage.setReplyMarkup(getHideKeyboard());
					sendControlledMessage(sendMessage, "Cuándo naciste? (Ingrese en formato dd/mm/aaaa)");
					return;
				}

				if (message.getText().equals("10/10/2015")) {
					sendMessage.setReplyMarkup(getWaterBenefitKeyboard());
					sendControlledMessage(sendMessage, "Recordare esa fecha y te saludaré en tu compleaños");
					sendControlledMessage(sendMessage, " Muchas gracias " + firstName + ".");
					Habito beberAgua = DataService.getInstance().getHabitByCode("BEBER_AGUA");
					sendControlledMessage(sendMessage, beberAgua.getMensajeIntroductorio());

					return;
				}

				if (message.getText().equals("Por que?")) {
					Habito beberAgua = DataService.getInstance().getHabitByCode("BEBER_AGUA");
					for (Beneficio beneficio : beberAgua.getBeneficios()) {
						sendControlledMessage(sendMessage, beneficio.getDesripcion());
					}
					return;
				}
				
				if(message.getText().equals("Ya tengo el hábito")){
					sendMessage.setReplyMarkup(getAdoptHabitWaterKeyboard());
					sendControlledMessage(sendMessage, "Excelente "+firstName+" te gustaría que de todas forma te recuerde sobre esto?");
					return ;
				}

				sendControlledMessage(sendMessage, "Hola " + message.getFrom().getFirstName());
				sendControlledMessage(sendMessage, "En que puedo ayudarte?");
				sendControlledMessage(sendMessage, "tenme paciencia, estoy aprendiendo");

				sendMessage.setReplyMarkup(MenuKeyboardFactory.getGenderMenuKeyboard());
				sendControlledMessage(sendMessage, "Qué sexo eres?");

			}
		}

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

	private void sendControlledMessage(SendMessage sendMessage, String text) {
		try {
			sendMessage.setText(text);
			sendMessage.enableMarkdown(true);
			sendMessage(sendMessage);
		} catch (TelegramApiException e) {
			// Manejar el rror
		}
	}

}
