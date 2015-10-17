package transmetteurs;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConforme;
import java.util.*;
import java.io.*;
import  java.lang.*;

public class TransmetteurBruiteAnalogiqueReel extends Transmetteur<Float, Float> {

	private double snr = 1;
	private double ps = 0;
	private double pb = 0;
	private double sigma_b = 0;
	
	private Information <Float>  informationGeneree;
	private Information <Float>  informationDeDecalage;
	private Information <Float>  informationDecalee1;
	private Information <Float>  informationDecalee2;
	private Information <Float>  informationDecalee3;
	private Information <Float>  informationDecalee4;
	private Information <Float>  informationDecalee5;
	private Information <Float> informationATransmettre;
	
	private Information <Float> [] informationDecalee= new Information  [5];
	
	private Boolean decalage[] = new Boolean[5];
	private int dt[] = new int[5];
	private Float ar[] = new Float[5];
	
	public TransmetteurBruiteAnalogiqueReel(float snr, Boolean decalage[],int dt [], Float ar[]) {
		super();
		informationRecue = new Information<Float>();
		informationGeneree = new Information<Float>();
	    informationEmise = new Information<Float>();
	    informationDecalee1 = new Information<Float>();
	    informationDecalee2 = new Information<Float>();
	    informationDecalee3 = new Information<Float>();
	    informationDecalee4 = new Information<Float>();
	    informationDecalee5 = new Information<Float>();
	    informationATransmettre = new Information<Float>();
	    this.snr = snr;
	    this.ar = ar;
	    this.dt =dt;
	    this.decalage = decalage;
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
	
	public Information <Float> decalerSignal (float ar, int dt){

		Information <Float>  info = new Information<Float>();
		
		for(int i = 0; i<informationRecue.nbElements(); i++){
			if (i>=dt){
				info.add(0.0f);
			}
			else {	
				info.add((ar*informationGeneree.iemeElement(i))); 
			}
		}
		return info;
	}
	
	public Information <Float> additionnerSignaux (Information <Float>  informationDecalee){

		for(int i = 0; i<informationRecue.nbElements(); i++){
			informationGeneree.add((informationDeDecalage.iemeElement(i)+informationDecalee.iemeElement(i))); 
		}
		return informationDecalee;
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
	
	public void ajouterDecalage() {
		for(int i=0;i<5;i++){
			informationDecalee[i] = decalerSignal(ar[i],dt[i]);		
		}
		for(int i = 0; i<informationRecue.nbElements(); i++){
			informationATransmettre.add(informationGeneree.iemeElement(i)+informationDecalee[0].iemeElement(i)
					+informationDecalee[1].iemeElement(i)+informationDecalee[2].iemeElement(i)
					+informationDecalee[3].iemeElement(i)+informationDecalee[4].iemeElement(i));
		}
	}
	
	@Override
	public void emettre() throws InformationNonConforme {
		ajouterBruit();
		ajouterDecalage();
		
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
