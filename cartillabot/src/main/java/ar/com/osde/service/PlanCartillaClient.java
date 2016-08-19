
package ar.com.osde.service;


import ar.com.osde.service.wsdl.GetPlanesCartilla;
import ar.com.osde.service.wsdl.GetPlanesCartillaResponse;
import ar.com.osde.service.wsdl.PlanCartilla;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import java.util.Iterator;
import java.util.List;

public class PlanCartillaClient extends WebServiceGatewaySupport {

	private static final Logger log = LoggerFactory.getLogger(PlanCartillaClient.class);

	public GetPlanesCartillaResponse getPlanesCartilla() {

		GetPlanesCartilla request = new GetPlanesCartilla();

		log.info("Requesting Rubros de la cartilla");

		GetPlanesCartillaResponse response = (GetPlanesCartillaResponse) getWebServiceTemplate()
				.marshalSendAndReceive(
						"http://servicios.osde.com.ar/ESB/jbi/unsecured/PlanSecurityProxyInternet/",
						request);

		return response;
	}

	public void printResponse(GetPlanesCartillaResponse response) {

		List<PlanCartilla> rubroList = response.getOut().getPlanCartilla();

		for (Iterator<PlanCartilla> planIterator = rubroList.iterator(); planIterator.hasNext(); ) {
			PlanCartilla plan = planIterator.next();
			log.info("Id: " + plan.getId());
			log.info("Nombre: "+plan.getNombre().getValue());
			log.info("");

		}
	}

}
