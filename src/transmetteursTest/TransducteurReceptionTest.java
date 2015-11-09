package transmetteursTest;

import static org.junit.Assert.*;
import information.Information;
import information.InformationNonConforme;

import org.junit.Before;
import org.junit.Test;

import transmetteurs.TransducteurEmission;
import transmetteurs.TransducteurReception;
import destinations.DestinationFinale;

public class TransducteurReceptionTest {

	private Information<Boolean> inf_recue;
	private Information<Boolean> inf_generee;
	private TransducteurReception transducteur;
	private DestinationFinale d;

	
	@Before
	public void setUp() throws Exception {
		transducteur = new TransducteurReception();
		inf_recue = new Information<Boolean>();
		inf_generee = new Information<Boolean>();
		d = new DestinationFinale();
		inf_recue.add(false);
		inf_recue.add(true);
		inf_recue.add(false);
		inf_recue.add(true);
		inf_recue.add(false);
		inf_recue.add(true);
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
		assertEquals("L'information n'a pas ete bien generee", info , inf_generee);
	}

}
