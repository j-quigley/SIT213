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

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testDecoder() {
		float aMax = 5.0f;
		float aMin = 1.0f;
		Source <Boolean> source = new SourceFixe("0001");
		EmetteurAnalogique EA_NRZ = new EmetteurAnalogique(aMin,aMax,"NRZ",30);
		TransmetteurParfaitAnalogique TPA = new TransmetteurParfaitAnalogique();
		RecepteurAnalogique RA_NRZ = new RecepteurAnalogique(aMin,aMax,"NRZ",30);
		source.connecter(EA_NRZ);
		EA_NRZ.connecter(TPA);
		TPA.connecter(RA_NRZ);
		Information <Boolean> inf_NRZ = new Information <Boolean>();
		
	  	 try{
	         source.emettre();
  	 }
  	 catch (InformationNonConforme e){
  	 }
	  	EA_NRZ.coder();
		 try{
	         EA_NRZ.emettre();
  	 }
  	 catch (InformationNonConforme e){
  	 }
		 try{
	         TPA.emettre();
  	 }
  	 catch (InformationNonConforme e){
  	 }
	  	
	  	RA_NRZ.decoder();
	  	
	  	inf_NRZ.add(false);
	  	inf_NRZ.add(false);
	  	inf_NRZ.add(false);
	  	inf_NRZ.add(true);
	  	
		System.out.println(inf_NRZ.toString());
		System.out.println(RA_NRZ.getInformationDecodee().toString());
	  	
	  	assertTrue(RA_NRZ.getInformationDecodee().equals(inf_NRZ));
	  	
	  	 
	}

}
