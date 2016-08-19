
package ar.com.osde.service;


import ar.com.osde.service.wsdl.GetRubrosCartilla;
import ar.com.osde.service.wsdl.GetRubrosCartillaResponse;
import ar.com.osde.service.wsdl.Rubro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import java.util.Iterator;
import java.util.List;

public class RubroClient extends WebServiceGatewaySupport {

	private static final Logger log = LoggerFactory.getLogger(RubroClient.class);

	public GetRubrosCartillaResponse getRubrosCartilla() {

		GetRubrosCartilla request = new GetRubrosCartilla();

		log.info("Requesting Rubros de la cartilla");

		GetRubrosCartillaResponse response = (GetRubrosCartillaResponse) getWebServiceTemplate()
				.marshalSendAndReceive(
						"http://servicios.osde.com.ar/ESB/jbi/unsecured/RubroSecurityProxyInternet/",
						request);

		return response;
	}

	public void printResponse(GetRubrosCartillaResponse response) {

		List<Rubro> rubroList = response.getOut().getRubro();

		for (Iterator<Rubro> rubroIterator = rubroList.iterator(); rubroIterator.hasNext(); ) {
			Rubro rubro = rubroIterator.next();
			log.info("Id: " + rubro.getId());
			log.info("Nombre: "+rubro.getNombre().getValue());
			log.info("");

		}
	}

}
