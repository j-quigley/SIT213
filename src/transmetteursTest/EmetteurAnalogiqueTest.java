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
	
	@Before
	public void setUp() throws Exception {
		
		
	}

	@Test
	public void testCoder() {
		
		float aMax = 5.0f;
		float aMin = 1.0f;
		source = new SourceFixe("10");
		EmetteurAnalogique EA_NRZ = new EmetteurAnalogique(aMin,aMax,"NRZ",30);
		source.connecter(EA_NRZ);
		
	  	 try{
	         source.emettre();
  	 }
  	 catch (InformationNonConforme e){
  		
  	 }
	  	EA_NRZ.coder();
		Information <Float> inf_NRZ = new Information <Float>();
		
		for (int i =0; i<30; i++){
			inf_NRZ.add(aMax);
		}
		for (int i =0; i<30; i++){
			inf_NRZ.add(aMin);
		}
		
		assertTrue(inf_NRZ.equals(EA_NRZ.getInformationCodee()));
		
		
		EmetteurAnalogique EA_RZ = new EmetteurAnalogique(1,5,"RZ",30);
		source.connecter(EA_RZ);
		
	  	 try{
	         source.emettre();
  	 }
  	 catch (InformationNonConforme e){
  		
  	 }
		EA_RZ.coder();
		Information <Float> inf_RZ = new Information <Float>();
		
		for (int i =0; i<10; i++){
			inf_RZ.add(1.0f);
		}
		for (int i =10; i<20; i++){
			inf_RZ.add(5.0f);
		}
		for (int i =20; i<30; i++){
			inf_RZ.add(1.0f);
		}
		for (int i =0; i<30; i++){
			inf_RZ.add(1.0f);
		}
		
		assertTrue(inf_RZ.equals(EA_RZ.getInformationCodee()));
		
		EmetteurAnalogique EA_NRZT = new EmetteurAnalogique(1,5,"NRZT",30);
		source.connecter(EA_NRZT);
		
	  	 try{
	         source.emettre();
  	 }
  	 catch (InformationNonConforme e){
  		
  	 }
		EA_NRZT.coder();
		Information <Float> inf_NRZT = new Information <Float>();
		
		
			for(int j = 0; j<10; j++){
				inf_NRZT.add(aMin+((aMax-aMin)/10)*j);
			}
			for(int j = 0; j<10; j++){
				inf_NRZT.add(aMax);
			}
			for(int j = 0; j<10; j++){
				inf_NRZT.add(aMin+((aMax-aMin)/10)*(9-j));
			}
			
			for(int j = 0; j<30; j++){
				inf_NRZT.add(aMin);
			}
			

		
		assertTrue(inf_NRZT.equals(EA_NRZT.getInformationCodee()));
	
	}




}
