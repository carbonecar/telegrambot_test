package ar.com.osde.bot;


import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.logging.BotLogger;

public class Application {

    public static void main(String[] args) {


        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        try {
            telegramBotsApi.registerBot(new CartillaBot());
        } catch (TelegramApiException e) {
            BotLogger.error("ERRROR: ", e);
        }
    }

}

