package transmetteurs;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConforme;
import java.util.*;
import java.io.*;
import  java.lang.*;

public class TransmetteurAnalogiqueBruite extends Transmetteur<Float, Float> {

	private float variance = 1;
	
	public TransmetteurAnalogiqueBruite(float variance) {
		super();
	    informationRecue = new Information<Float>();
	    informationEmise = new Information<Float>();
	    this.variance = variance;
	}
	
	public float genererBruit (){
		Random rand = new Random();
		double a1 = rand.nextInt(1000);
		a1 /= 1000;
		double a2 = rand.nextInt(1000);
		a2 /= 1000;
		double b = Math.sqrt(-2*(Math.log(1-a1)))*Math.cos(2*Math.PI*a2);
		return (float)b;
		
	}
	
	@Override
	public void recevoir(Information<Float> information)
			throws InformationNonConforme {
		for(int i = 0; i<information.nbElements(); i++){
			informationRecue.add(information.iemeElement(i) + genererBruit());
		}

	}

	@Override
	public void emettre() throws InformationNonConforme {
        for (DestinationInterface <Float> destinationConnectee : destinationsConnectees) {
            destinationConnectee.recevoir(informationRecue);
         }
         this.informationEmise = informationRecue; 

	}
	
	public static void main(String[] args) {
	}

}
