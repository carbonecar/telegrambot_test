
package ar.com.osde.service;

import ar.com.osde.service.wsdl.GetPlanesCartilla;
import ar.com.osde.service.wsdl.GetPlanesCartillaResponse;
import ar.com.osde.service.wsdl.GetRubrosCartillaResponse;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}

//	@Bean
//    CommandLineRunner lookup(RubroClient rubroClient) {
//		return args -> {
//
//
//			GetRubrosCartillaResponse response = rubroClient.getRubrosCartilla();
//            rubroClient.printResponse(response);
//            System.out.println(response);
//		};
//	}

	@Bean
	CommandLineRunner lookup(PlanCartillaClient planCartillaClient) {
		return args -> {


			GetPlanesCartillaResponse response = planCartillaClient.getPlanesCartilla();
			planCartillaClient.printResponse(response);
			System.out.println(response);
		};
	}

}
