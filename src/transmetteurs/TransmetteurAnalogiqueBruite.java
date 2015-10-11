package transmetteurs;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConforme;
import java.util.*;
import java.io.*;
import  java.lang.*;

public class TransmetteurAnalogiqueBruite extends Transmetteur<Float, Float> {

	private double snr = 1;
	private double ps = 0;
	private double pb = 0;
	private double sigma_b = 0;
	
	private Information <Float>  informationGeneree;
	
	public TransmetteurAnalogiqueBruite(float snr) {
		super();
		informationRecue = new Information<Float>();
		informationGeneree = new Information<Float>();
	    informationEmise = new Information<Float>();
	    this.snr = snr;
	}
	
	public float genererBruit (){
		Random rand = new Random();
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
		System.out.println("nombre d'éléments reçus transmetteur bruité" +informationRecue.nbElements());
	}
	
	public static void main(String[] args) {

	}

}
