package transmetteursTest;

import static org.junit.Assert.*;
import junit.framework.Assert;
import information.Information;
import information.InformationNonConforme;

import org.junit.Before;
import org.junit.Test;

import transmetteurs.RecepteurAnalogique;
import transmetteurs.TransmetteurBruiteAnalogique;
import transmetteurs.TransmetteurBruiteAnalogiqueReel;

public class TransmetteurBruiteAnalogiqueReelTest {

	private Information<Float> inf_recue;
	private Information<Float> inf_generee;
	private TransmetteurBruiteAnalogiqueReel transmetteur;
	private float aMax = 2.0f;
	private float aMin = 0.0f;
	private Double sigma = 2.0;
	private int longueurMsg = 10000;
	private int dt[] = {4};
	private Float ar[] = {1f};
	private Boolean decalage[] = {true,false,false,false,false};
	
	@Before
	public void setUp() throws Exception {
		transmetteur = new TransmetteurBruiteAnalogiqueReel(decalage,dt,ar);
		inf_recue = new Information<Float>();
		inf_generee = new Information<Float>();
		inf_recue.add(aMax);
		inf_recue.add(aMin);
		inf_recue.add(aMin);
		inf_recue.add(aMin);
		inf_recue.add(aMin);
		inf_recue.add(aMin);
		inf_recue.add(aMin);
		transmetteur.recevoir(inf_recue);
	}

	@Test
	public void testAjouterDecalage() {
		transmetteur.ajouterDecalage();
		inf_generee = transmetteur.getInformationGeneree();
		Float f = 0f;
		for(int i = 0;i<inf_generee.nbElements();i++){
			f = (float) round(inf_generee.iemeElement(i),3);
			if((i==dt[0])||(i==0)){
				Assert.assertEquals("La valeur du "+i+"eme ŽlŽment devrait tre "+aMax, aMax, f);
			}
			else Assert.assertEquals("La valeur du "+i+"eme ŽlŽment devrait tre "+aMin,  aMin, f);
		}
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}

}
