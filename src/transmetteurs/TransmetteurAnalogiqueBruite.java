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
	
	public TransmetteurAnalogiqueBruite(float snr) {
		super();
	    informationRecue = new Information<Float>();
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
		System.out.println(b);
		return (float)b;
		
	}
	
	@Override
	public void recevoir(Information<Float> information)
			throws InformationNonConforme {
		for(int i = 0; i<information.nbElements(); i++){
			informationRecue.add(information.iemeElement(i) + genererBruit());
			ps = ps + (information.iemeElement(i)*information.iemeElement(i)); 
		}
			ps /= information.nbElements();
			pb = ps / Math.pow(10,snr/10);
			sigma_b = Math.sqrt(pb);
	}

	@Override
	public void emettre() throws InformationNonConforme {
        for (DestinationInterface <Float> destinationConnectee : destinationsConnectees) {
            destinationConnectee.recevoir(informationRecue);
         }
         this.informationEmise = informationRecue; 

	}
	
	public static void main(String[] args) {
		TransmetteurAnalogiqueBruite tab = new TransmetteurAnalogiqueBruite(1);
		
	}

}
