package ar.com.espherika.healthbot;

import org.junit.Assert;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import ar.com.espherika.healthbot.model.Ciudadano;

public class TestKie {

	
	/**
	 * Con este test validamos que la configuracion de drools este correctamente seteada
	 */
	@Test
	public void testClassLoaderKie() {
		KieServices kieSerivces = KieServices.Factory.get();
		// Leemos del classpath las reglas que aqui hay.
		KieContainer kieContainer = kieSerivces.getKieClasspathContainer();

		KieSession kieSession = kieContainer.newKieSession("ksession");
		int rulesFired=kieSession.fireAllRules();
		Assert.assertEquals(2, rulesFired);
		kieSession.insert(new Ciudadano(17,"carlos"));
		Assert.assertEquals(1,kieSession.fireAllRules());
	}
	
}
