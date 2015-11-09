package transmetteursTest;

import static org.junit.Assert.*;
import information.Information;
import information.InformationNonConforme;

import org.junit.Before;
import org.junit.Test;

import destinations.DestinationFinale;

import transmetteurs.TransducteurEmission;

public class TransducteurEmissionTest {

	private Information<Boolean> inf_recue;
	private Information<Boolean> inf_generee;
	private TransducteurEmission transducteur;
	private DestinationFinale d;

	
	@Before
	public void setUp() throws Exception {
		transducteur = new TransducteurEmission();
		inf_recue = new Information<Boolean>();
		inf_generee = new Information<Boolean>();
		d = new DestinationFinale();
		inf_recue.add(false);
		inf_recue.add(true);
		inf_recue.add(false);
		transducteur.recevoir(inf_recue);
		transducteur.connecter(d);
	}

	@Test
	public void testEmettre() {
		try {
			transducteur.emettre();
		} catch (InformationNonConforme e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		inf_generee = d.getInformationRecue();
		Information<Boolean> info = new Information<Boolean>();
		info.add(false);
		info.add(true);
		info.add(false);
		info.add(true);
		info.add(false);
		info.add(true);
		info.add(false);
		info.add(true);
		info.add(false);
		assertEquals("L'information n'a pas ete bien generee", info , inf_generee);
	}

}
