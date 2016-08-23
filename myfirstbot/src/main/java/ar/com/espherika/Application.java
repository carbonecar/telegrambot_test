package ar.com.espherika;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.logging.BotLogger;

import ar.com.espherika.healthbot.model.Habito;
import ar.com.espherika.healthbot.persistence.HabitoRepository;

@EntityScan(basePackages={"ar.com.espherika.healthbot.model"})
@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages="ar.com.espherika.*")
public class Application {

	
	public static void main(String[] args) {
		SpringApplication.run(Application.class);

	}
	
	@Bean
	public CommandLineRunner demo(HabitoRepository repository){
		return (args)->{
			Habito beberAgua = new Habito("BEBER_AGUA", "Beber agua");
			beberAgua.addBeneficio(
					"Una adecuada hidratación es importante para un funcionamiento correcto del cerebro. Cuando estamos adecuadamente hidratados, las células del cerebro reciben sangre oxigenada y el cerebro se mantiene alerta.");
			beberAgua.addBeneficio(
					"El consumo adecuado de agua es esencial para que los riñones funcionen bien, ayudándolos a eliminar residuos y nutrientes innecesarios a través de la orina.");
			beberAgua.addBeneficio(
					"Mejora el tracto digestivo ya que el agua es necesaria en la disolución de nutrientes para que estos puedan ser absorbidos por la sangre y transportados a las células.");
			beberAgua.addBeneficio(
					"El agua es un gran aliado para la piel ayudando a mantener la elasticidad de la misma y su tonicidad.");
			beberAgua.addBeneficio(
					"El agua activa como un lubricante para los músculos y las articulaciones: ayuda a proteger a las articulaciones y a que los músculos funcionen correctamente.");

			
			repository.save(beberAgua);
			

			TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
			MyFirstBot mybot=new MyFirstBot();
			mybot.setHabitoRepository(repository);
			try {
				telegramBotsApi.registerBot(mybot);
			} catch (TelegramApiException e) {
				BotLogger.error("ERRROR: ", e);
			}
		};
	}
	
}
