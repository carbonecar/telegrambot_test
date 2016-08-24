package ar.com.espherika.chatstrategies;

import static ar.com.espherika.MenuKeyboardFactory.getAdoptHabitWaterKeyboard;
import static ar.com.espherika.MenuKeyboardFactory.getHideKeyboard;
import static ar.com.espherika.MenuKeyboardFactory.getSubscribeHabit;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;

import ar.com.espherika.MyFirstBot;
import ar.com.espherika.healthbot.model.Beneficio;
import ar.com.espherika.healthbot.model.Habito;
import ar.com.espherika.service.CustomTimerTask;
import ar.com.espherika.service.TimerExecutor;

public class BotDrinkWaterChatStrategy implements BotChatStrategy {

	private enum SET_CRON_STATE {
		CRON_REQUEST, CRON, CRON_TWICE;
	}

	private Map<Long, SET_CRON_STATE> chatIdStates = new HashMap<Long, SET_CRON_STATE>();

	@Override
	public void run(Message message, MyFirstBot bot) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(message.getChatId().toString());
		//TODO switch?
		if (message.getText().equals("Por que?")) {
			Habito beberAgua = bot.getHabitoBeberAgua();
			for (Beneficio beneficio : beberAgua.getBeneficios()) {
				bot.sendControlledMessage(sendMessage, beneficio.getDesripcion());
			}
			return ;
		}

		if (message.getText().equals("Ya tengo el hábito")) {
			sendMessage.setReplyMarkup(getAdoptHabitWaterKeyboard());
			bot.sendControlledMessage(sendMessage, "Excelente " + message.getFrom().getFirstName()
					+ " te gustaría que de todas forma te recuerde sobre esto?");
			return;
		}

		if (message.getText().equals("Adoptar hábito")) {
			sendMessage.setReplyMarkup(getAdoptHabitWaterKeyboard());
			bot.sendControlledMessage(sendMessage,
					"Excelente " + message.getFrom().getFirstName() + " te gustaría que  te recuerde sobre esto?");
			this.chatIdStates.put(message.getChatId(), SET_CRON_STATE.CRON_REQUEST);
			return;
			
		}

		if (SET_CRON_STATE.CRON_REQUEST.equals(this.getSafeState(message.getChatId()))) {
			getTimeToCronHabit(message, bot, sendMessage);
			return;
		}

		if (message.getText().equals("Mas hábitos...")) {
			this.chatIdStates.put(message.getChatId(), null);
			return;
		}

		if (SET_CRON_STATE.CRON.equals(this.getSafeState(message.getChatId()))){
			doCron(message, bot, sendMessage);
			return;
			
		}

		if( SET_CRON_STATE.CRON_TWICE.equals(this.getSafeState(message.getChatId()))) {
			doCron(message, bot, sendMessage);
			this.chatIdStates.put(message.getChatId(), SET_CRON_STATE.CRON);
			return;
		}

	}

	private void doCron(Message message, MyFirstBot bot, SendMessage sendMessage) {

		try {
			Integer hora = Integer.valueOf(message.getText());
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			System.out.println(sdf.format(cal.getTime()));
			bot.sendControlledMessage(sendMessage, "la hora local es: " + sdf.format(cal.getTime()));
			bot.sendControlledMessage(sendMessage, "para validar que fue agendado te lo recordare en 1 minuto");
			bot.sendControlledMessage(sendMessage, "luego de dicho recordatorio aplicará el horario que seleccionaste");
			int horaPrueba = cal.get(Calendar.HOUR_OF_DAY);
			int minutoPrueba = cal.get(Calendar.MINUTE) + 1;
			bot.sendControlledMessage(sendMessage, "agendado para :" + horaPrueba + ":" + minutoPrueba);

			TimerExecutor.getInstance().startExecutionEveryDayAt(new CustomTimerTask("test task", 1) {

				@Override
				public void execute() {
					bot.sendControlledMessage(sendMessage, bot.getHabitoBeberAgua().getMensajeAlerta());
				}
			}, horaPrueba, minutoPrueba, 00);

			if(this.getSafeState(message.getChatId()).equals(SET_CRON_STATE.CRON_TWICE)){
				bot.sendControlledMessage(sendMessage, "Ingresa la otra hora a la cual quieres ser informado");
			}
		} catch (NumberFormatException nef) {
			bot.sendControlledMessage(sendMessage, "\"" + message.getText() + "\"" + " no es una hora.");
			bot.sendControlledMessage(sendMessage, "A que hora te gustaría que te recuerte tu hábito?");
		}
	}

	private void getTimeToCronHabit(Message message, MyFirstBot bot, SendMessage sendMessage) {
		if (message.getText().equals("1 vez por día")) {
			sendMessage.setReplyMarkup(getSubscribeHabit());
			bot.sendControlledMessage(sendMessage, "A que hora te gustaría que te recuerde tu hábito?");

			sendMessage.setReplyMarkup(getHideKeyboard());
			this.chatIdStates.put(message.getChatId(), SET_CRON_STATE.CRON);
		}

		if (message.getText().equals("2 veces por día")) {
			sendMessage.setReplyMarkup(getSubscribeHabit());
			bot.sendControlledMessage(sendMessage, "A que hora te gustaría que te recuerde tu hábito?");
			this.chatIdStates.put(message.getChatId(), SET_CRON_STATE.CRON_TWICE);
		}

		if (message.getText().equals("1 ves cada 3 días")) {
			sendMessage.setReplyMarkup(getSubscribeHabit());
			bot.sendControlledMessage(sendMessage, "A que hora te gustaría que te recuerde tu hábito?");
		}
	}

	private SET_CRON_STATE getSafeState(Long chatId) {
		SET_CRON_STATE state = this.chatIdStates.get(chatId);
		if (state == null) {
			state = SET_CRON_STATE.CRON_REQUEST;
			this.chatIdStates.put(chatId, state);
		}
		return state;
	}

}
