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


public class EmetteurAnalogiqueTest {

	private Source <Boolean> source;
	private EmetteurAnalogique eaRZ;
	private EmetteurAnalogique eaNRZ;
	private EmetteurAnalogique eaNRZT;
	private Information<Float> infRZ;
	private Information<Float> infNRZ;
	private Information<Float> infNRZT;
	private float aMin;
	private float aMax;
	
	@Before
	public void setUp() throws Exception {
		aMin = 1.0f;
		aMax = 5.0f;
		
		source = new SourceFixe("10");
		
		eaRZ = new EmetteurAnalogique(aMin, aMax, "RZ", 30);
		eaNRZ = new EmetteurAnalogique(aMin, aMax, "NRZ", 30);
		eaNRZT = new EmetteurAnalogique(aMin, aMax, "NRZT", 30);
		
		infRZ = new Information<Float>();
		infNRZ = new Information<Float>();
		infNRZT = new Information<Float>();
		
		source.connecter(eaRZ);
		source.connecter(eaNRZ);
		source.connecter(eaNRZT);
		source.emettre();
		
		eaRZ.coder();
		eaNRZ.coder();
		eaNRZT.coder();
		
		//Initialisation de infRZ
		for (int i =0; i<60; i++){
			if ((i>=10)&&(i<20)){
				infRZ.add(aMax);
			}
			else {
				infRZ.add(0.0f);
			}
		}

		//Initialisation de infNRZ
		for (int i =0; i<60; i++){
			if (i<30){
				infNRZ.add(aMax);
			}
			else{
				infNRZ.add(aMin);
			}
		}
		
		//Initialisation de infNRZT
		for(int i = 0; i<60; i++){
			if(i<10){
				infNRZT.add(aMin+((aMax-aMin)/10)*i);
			}
			else if((i>=10)&&(i<20)){
				infNRZT.add(aMax);
			}
			else if((i>=20)&&(i<30)){
				infNRZT.add(aMin+((aMax-aMin)/10)*(29-i));
			}
			else{
				infNRZT.add(aMin);
			}	
		}
		

	}

	@Test
	public void testCoderRZ() {
		assertTrue(infRZ.equals(eaRZ.getInformationCodee()));
	}
	@Test
	public void testCoderNRZ() {
		assertTrue(infNRZ.equals(eaNRZ.getInformationCodee()));
	}
	@Test
	public void testCoderNRZT() {
		assertTrue(infNRZT.equals(eaNRZT.getInformationCodee()));
	}
}
