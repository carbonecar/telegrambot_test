package ar.com.espherika;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import ar.com.espherika.healthbot.model.Habito;
import ar.com.espherika.healthbot.persistence.HabitoRepository;


public class DataService {

	private static DataService instance;
	private  Map<String, Habito> habitos = new HashMap<String, Habito>();
	
	@Autowired
	private HabitoRepository habitoRepository;
	
	
	
	public HabitoRepository getHabitoRepository() {
		return habitoRepository;
	}

	public void setHabitoRepository(HabitoRepository habitoRepository) {
		this.habitoRepository = habitoRepository;
	}

	private DataService(){
		buildHabits();
	}
	
	public static DataService getInstance(){
		if(instance==null){
			instance=new DataService();
		}
		return instance;
	}
	
	public Habito getHabitByCode(String habito){
		return this.habitoRepository.findByCodigo(habito);
	}
	
	private  void buildHabits() {
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

		
		habitos.put(beberAgua.getCodigo(), beberAgua);
		
		habitoRepository.save(beberAgua);
		
		
	}
}
