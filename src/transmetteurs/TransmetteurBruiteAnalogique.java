package transmetteurs;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConforme;
import java.util.*;
import java.io.*;
import  java.lang.*;

public class TransmetteurBruiteAnalogique extends Transmetteur<Float, Float> {

	private double snr = 1;
	private double ps = 0;
	private double pb = 0;
	private double sigma_b = 0;
	private int seed = -100000;
	
	private Information <Float>  informationGeneree;
	
	public TransmetteurBruiteAnalogique(float snr) {
		super();
		informationRecue = new Information<Float>();
		informationGeneree = new Information<Float>();
	    informationEmise = new Information<Float>();
	    this.snr = snr;
	}
	
	public TransmetteurBruiteAnalogique(float snr, int seed) {
		super();
		informationRecue = new Information<Float>();
		informationGeneree = new Information<Float>();
	    informationEmise = new Information<Float>();
	    this.snr = snr;
	    this.seed = seed;
	}
	
	public float genererBruit (){
		Random rand = new Random();
		if(seed != -100000){
			rand.setSeed(seed);
		}
		double a1 = rand.nextInt(1000);
		a1 /= 1000;
		double a2 = rand.nextInt(1000);
		a2 /= 1000;
		double b = sigma_b*(Math.sqrt(-2*(Math.log(1-a1)))*Math.cos(2*Math.PI*a2));
		return (float)b;
		
	}
	
	@Override
	public void recevoir(Information<Float> information)
			throws InformationNonConforme {
		for(int i = 0; i<information.nbElements(); i++){
			informationRecue.add(information.iemeElement(i));
			ps = ps + (information.iemeElement(i)*information.iemeElement(i)); 
		}

	}
	
	public void ajouterBruit() {
		ps /= informationRecue.nbElements();
		pb = ps / Math.pow(10,snr/10);
		sigma_b = Math.sqrt(pb);
		for(int i = 0; i<informationRecue.nbElements(); i++){		
			informationGeneree.add(informationRecue.iemeElement(i)+genererBruit());
		}
	}
	
	@Override
	public void emettre() throws InformationNonConforme {
		ajouterBruit();
        for (DestinationInterface <Float> destinationConnectee : destinationsConnectees) {
            destinationConnectee.recevoir(informationGeneree);
         }
         this.informationEmise = informationGeneree; 

	}

	public void lInfo() {
		System.out.println(informationGeneree.toString());
	}
	
	public Information<Float> getInformationGeneree(){
		return informationGeneree;
	}
	
	public void setSigma(Double sigma){
		sigma_b = sigma;
	}
	
	public static void main(String[] args) {
		//Génération d'un bruit pour montrer son caractère gaussien
		Information<Float> infGeneree = new Information<Float>();
		TransmetteurBruiteAnalogique transmetteur = new TransmetteurBruiteAnalogique(0.0f);
		transmetteur.setSigma(2.0);
		for(int i = 0; i<1000; i++){
			infGeneree.add(transmetteur.genererBruit());
		}
		PrintStream l_out = null;
		try {
			l_out = new PrintStream(new FileOutputStream("histogramme.csv"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		try { 
			for(int i = 0; i<1000; i++){
				l_out.print(infGeneree.iemeElement(i));
				l_out.print(";");
			}
			//on ferme le fichier : 
			l_out.flush(); 
			l_out.close(); 
			l_out=null; 
		} 
		catch(Exception e){System.out.println(e.toString()); } 

	}

}
