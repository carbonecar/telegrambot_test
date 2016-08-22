package ar.com.espherika;

import java.util.HashMap;
import java.util.Map;

import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.logging.BotLogger;

import ar.com.espherika.healthbot.model.Habito;

public class Application {

	public static final Map<String, Habito> habitos = new HashMap<String, Habito>();

	public static void main(String[] args) {

		buildHabits();
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

		try {
			telegramBotsApi.registerBot(new MyFirstBot());
		} catch (TelegramApiException e) {
			BotLogger.error("ERRROR: ", e);
		}
	}

	private static void buildHabits() {
		Habito beberAgua = new Habito("BEBER_AGUA", "Beber agua");
		beberAgua.addBeneficio(
				"Una adecuada hidrataci�n es importante para un funcionamiento correcto del cerebro. Cuando estamos adecuadamente hidratados, las c�lulas del cerebro reciben sangre oxigenada y el cerebro se mantiene alerta.");
		beberAgua.addBeneficio(
				"El consumo adecuado de agua es esencial para que los ri�ones funcionen bien, ayud�ndolos a eliminar residuos y nutrientes innecesarios a trav�s de la orina.");
		beberAgua.addBeneficio(
				"Mejora el tracto digestivo ya que el agua es necesaria en la disoluci�n de nutrientes para que estos puedan ser absorbidos por la sangre y transportados a las c�lulas.");
		beberAgua.addBeneficio(
				"El agua es un gran aliado para la piel ayudando a mantener la elasticidad de la misma y su tonicidad.");
		beberAgua.addBeneficio(
				"El agua act�a como un lubricante para los m�sculos y las articulaciones: ayuda a proteger a las articulaciones y a que los m�sculos funcionen correctamente.");

		habitos.put(beberAgua.getCodigo(), beberAgua);

	}
}
