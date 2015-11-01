package transmetteursTest;

import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;

import sources.*;
import destinations.*;
import transmetteurs.*;

import information.*;

import visualisations.*;

import java.util.regex.*;
import java.util.*;
import java.lang.Math;

	
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.Before;
import org.junit.Test;

import sources.SourceFixe;
import transmetteurs.EmetteurAnalogique;

public class RecepteurAnalogiqueTest {
	private float aMin;
	private float aMax;
	private Boolean[] decalage = new Boolean[] {false,false,false,false,false};
	private int[] dt = new int[5];
	private Float[] ar = new Float[5];
	
	Source<Boolean> source;
	EmetteurAnalogique eaRZ;
	EmetteurAnalogique eaNRZ;
	EmetteurAnalogique eaNRZT;
	TransmetteurParfaitAnalogique tpaRZ;
	TransmetteurParfaitAnalogique tpaNRZ;
	TransmetteurParfaitAnalogique tpaNRZT;
	RecepteurAnalogique raRZ;
	RecepteurAnalogique raNRZ;
	RecepteurAnalogique raNRZT;
	
	Information<Boolean> inf;
	

	@Before
	public void setUp() throws Exception {
		aMin = 1.0f;
		aMax = 5.0f;
		
		
		source = new SourceFixe("0001");
		eaRZ = new EmetteurAnalogique(aMin, aMax, "RZ", 30);
		eaNRZ = new EmetteurAnalogique(aMin, aMax, "NRZ", 30);
		eaNRZT = new EmetteurAnalogique(aMin, aMax, "NRZT", 30);
		tpaRZ = new TransmetteurParfaitAnalogique();
		tpaNRZ = new TransmetteurParfaitAnalogique();
		tpaNRZT = new TransmetteurParfaitAnalogique();
		raRZ = new RecepteurAnalogique(aMin, aMax, "RZ", 30, decalage, dt, ar);
		raNRZ = new RecepteurAnalogique(aMin, aMax, "NRZ", 30, decalage, dt, ar);
		raNRZT = new RecepteurAnalogique(aMin, aMax, "NRZT", 30, decalage, dt, ar);
		
		source.connecter(eaNRZ);
		source.connecter(eaRZ);
		source.connecter(eaNRZT);
		eaRZ.connecter(tpaRZ);
		eaNRZ.connecter(tpaNRZ);
		eaNRZT.connecter(tpaNRZT);
		tpaRZ.connecter(raRZ);
		tpaNRZ.connecter(raNRZ);
		tpaNRZT.connecter(raNRZT);
		
		source.emettre();
		eaRZ.coder();
		eaNRZ.coder();
		eaNRZT.coder();
		eaRZ.emettre();
		eaNRZ.emettre();
		eaNRZT.emettre();
		tpaRZ.emettre();
		tpaNRZ.emettre();
		tpaNRZT.emettre();
		raRZ.decoder();
		raNRZ.decoder();
		raNRZT.decoder();
		
		inf = new Information<Boolean>();
		inf.add(false);
		inf.add(false);
		inf.add(false);
		inf.add(true);
	}

	@Test
	public void testDecoderRZ() {
	  	assertTrue(raRZ.getInformationDecodee().equals(inf));
	}
	@Test
	public void testDecoderNRZ() {
	  	assertTrue(raNRZ.getInformationDecodee().equals(inf));
	}
	@Test
	public void testDecoderNRZT() {
	  	assertTrue(raNRZT.getInformationDecodee().equals(inf));
	}

}
