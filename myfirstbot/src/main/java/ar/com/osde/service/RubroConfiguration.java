
package ar.com.osde.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class RubroConfiguration {

	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("ar.com.osde.service.wsdl");
		return marshaller;
	}

	@Bean
	public RubroClient weatherClient(Jaxb2Marshaller marshaller) {
		RubroClient client = new RubroClient();
		client.setDefaultUri("http://servicios.osde.com.ar/ESB/jbi/unsecured/RubroSecurityProxyInternet/");
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}

}
