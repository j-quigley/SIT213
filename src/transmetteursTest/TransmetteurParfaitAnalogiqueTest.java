package transmetteursTest;

import static org.junit.Assert.*;
import junit.framework.Assert;
import information.Information;
import information.InformationNonConforme;

import org.junit.Before;
import org.junit.Test;

import transmetteurs.TransmetteurBruiteAnalogique;
import transmetteurs.TransmetteurParfaitAnalogique;
import visualisations.SondeAnalogique;

public class TransmetteurParfaitAnalogiqueTest {

	private Information<Float> inf_recue;
	private Information<Float> inf_emise;
	private TransmetteurParfaitAnalogique transmetteur;
	private float aMax = 2.0f;
	private float aMin = 0.0f;
	private SondeAnalogique s;
	
	@Before
	public void setUp() throws Exception {
		transmetteur = new TransmetteurParfaitAnalogique();
		s = new SondeAnalogique("sonde");
		inf_recue = new Information<Float>();
		inf_emise = new Information<Float>();
		inf_recue.add(aMin);
		inf_recue.add(aMin);
		inf_recue.add(aMax);
		inf_recue.add(aMin);
		inf_recue.add(aMax);
		inf_recue.add(aMax);
		inf_recue.add(aMin);
		transmetteur.recevoir(inf_recue);
		transmetteur.connecter(s);
	}

	@Test
	public void testEmettre() {
		try {
			transmetteur.emettre();
		} catch (InformationNonConforme e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		inf_emise = s.getInformationRecue();
		Assert.assertEquals("L'information n'a pas été bien transmise", inf_recue, inf_emise);
	}

}
