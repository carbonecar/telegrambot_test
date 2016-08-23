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
		CRON_REQUEST, CRON;
	}

	private Map<Long, SET_CRON_STATE> chatIdStates = new HashMap<Long, SET_CRON_STATE>();

	@Override
	public void run(Message message, MyFirstBot bot) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(message.getChatId().toString());

		if (message.getText().equals("Por que?")) {
			Habito beberAgua = bot.getHabitoBeberAgua();
			for (Beneficio beneficio : beberAgua.getBeneficios()) {
				bot.sendControlledMessage(sendMessage, beneficio.getDesripcion());
			}
			return;
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

		if (SET_CRON_STATE.CRON_REQUEST.equals(this.chatIdStates.get(message.getChatId()))) {
			getTimeToCronHabit(message, bot, sendMessage);
			return;
		}
		
		if(SET_CRON_STATE.CRON.equals(this.chatIdStates.get(message.getChatId()))){
			try{
				Integer hora=Integer.valueOf(message.getText());
				Calendar cal = Calendar.getInstance();
		        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		        System.out.println( sdf.format(cal.getTime()) );
		        bot.sendControlledMessage(sendMessage, "la hora local es: "+sdf.format(cal.getTime()));
		        bot.sendControlledMessage(sendMessage, "para validar que fue agendado te lo recordare en 1 minuto");
		        bot.sendControlledMessage(sendMessage, "luego de dicho recordatorio aplicará el horario que seleccionaste");
		        int horaPrueba=cal.get(Calendar.HOUR_OF_DAY);
		        int minutoPrueba=cal.get(Calendar.MINUTE)+1;
		        bot.sendControlledMessage(sendMessage, "agendnado para :"+horaPrueba+":"+minutoPrueba);
				TimerExecutor.getInstance().startExecutionEveryDayAt(new CustomTimerTask("test task", 1) {
					@Override
					public void execute() {
						bot.sendControlledMessage(sendMessage, "toma agua!");
					}
				}, horaPrueba, minutoPrueba, 00);
			}catch(NumberFormatException nef){
				bot.sendControlledMessage(sendMessage, "\""+message.getText()+"\""+" no es una hora.");
				bot.sendControlledMessage(sendMessage, "A que hora te gustaría que te recuerte tu hábito?" );
			}
			return ;
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
		}

		if (message.getText().equals("1 ves cada 3 días")) {
			sendMessage.setReplyMarkup(getSubscribeHabit());
			bot.sendControlledMessage(sendMessage, "A que hora te gustaría que te recuerde tu hábito?");
		}
	}

	public SET_CRON_STATE getSafeState(Long chatId) {
		SET_CRON_STATE state = this.chatIdStates.get(chatId);
		if (state == null) {
			state = SET_CRON_STATE.CRON_REQUEST;
			this.chatIdStates.put(chatId, state);
		}
		return state;
	}

}
