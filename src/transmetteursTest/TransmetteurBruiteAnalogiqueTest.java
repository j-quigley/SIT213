package transmetteursTest;

import static org.junit.Assert.*;

import java.text.DecimalFormat;

import information.Information;
import information.InformationNonConforme;
import org.junit.Before;
import org.junit.Test;

import transmetteurs.TransmetteurBruiteAnalogique;

public class TransmetteurBruiteAnalogiqueTest {

	private Information<Float> inf_recue;
	private Information<Float> inf_generee;
	private TransmetteurBruiteAnalogique transmetteur;
	private float snr = 0.0f;
	private float aMax = 2.0f;
	private float aMin = 0.0f;
	private Double sigma = 2.0;
	private int longueurMsg = 10000;
	
	@Before
	public void setUp() throws Exception {
		transmetteur = new TransmetteurBruiteAnalogique(snr);
		inf_recue = new Information<Float>();
		inf_generee = new Information<Float>();
		inf_recue.add(aMin);
		inf_recue.add(aMin);
		inf_recue.add(aMax);
		inf_recue.add(aMin);
		inf_recue.add(aMax);
		inf_recue.add(aMax);
		inf_recue.add(aMin);
		transmetteur.recevoir(inf_recue);
	}

	@Test
	public void testEmettre() {
		try {
			transmetteur.emettre();
		} catch (InformationNonConforme e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
<<<<<<< HEAD
		assertEquals("L'information n'a pas Å½tÅ½ bien gÅ½nÅ½rÅ½e", transmetteur.getInformationGeneree() , transmetteur.getInformationEmise());
=======
		assertEquals("L'information n'a pas ŽtŽ bien gŽnŽrŽe", transmetteur.getInformationGeneree() , transmetteur.getInformationEmise());
>>>>>>> branch 'master' of https://github.com/j-quigley/SIT213
	}

	@Test
	public void testGenererBruit() {
		transmetteur.setSigma(sigma);
		for(int i = 0; i<longueurMsg; i++){
			inf_generee.add(transmetteur.genererBruit());
		}
		Float moyTheorique = 0.0f;
		Float moyMesuree = 0.0f;
		for(int i = 0; i<longueurMsg;i++){
			moyMesuree =+ inf_generee.iemeElement(i);
		}
		moyMesuree = (float) round(moyMesuree / longueurMsg, 2);
		
		assertEquals("La moyenne du bruit n'est pas nulle", moyTheorique, moyMesuree);
		 
		Double varMesuree = 0.0;
		Double varTheorique = Math.pow(sigma, 2);
		for(int i = 0; i<longueurMsg; i++){ 
			varMesuree+=Math.pow(inf_generee.iemeElement(i)-moyMesuree, 2);
		}
		varMesuree = round(varMesuree / longueurMsg, 0);
		assertEquals("La variance du bruit n'est pas correcte", varTheorique, varMesuree);
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}

}
