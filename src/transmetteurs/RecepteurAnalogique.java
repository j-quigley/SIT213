package transmetteurs;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConforme;

public class RecepteurAnalogique extends Transmetteur<Float, Boolean>{

	private float aMin, aMax;
	private String codage;
	private int nbEchantillons;
	private Information <Boolean> informationDecodee;
	
	public RecepteurAnalogique(float aMin, float aMax, String codage, int nbEchantillons) {
		super();
	    informationRecue = new Information<Float>();
	    informationEmise = new Information<Boolean>();
	    informationDecodee = new Information<Boolean>();
	    this.aMin = aMin;
	    this.aMax = aMax;
	    this.codage = codage;
	    this.nbEchantillons = nbEchantillons;
	}
	
	@Override
	public  void recevoir(Information <Float> information) throws InformationNonConforme{
		for(int i = 0; i<information.nbElements(); i++){
			informationRecue.add(information.iemeElement(i));
		}
	}
	
	public void decoder(){
		Boolean valeurDecodee = false;
		for(int i = 0; i<informationRecue.nbElements(); i++){
			if(informationRecue.iemeElement(i) == aMax){
				valeurDecodee = true;
			}
			if((i+1)%nbEchantillons == 0){
				informationDecodee.add(valeurDecodee);
			}
		}
	}

	@Override
	public void emettre() throws InformationNonConforme {
		// émission vers les composants connectés  
        for (DestinationInterface <Boolean> destinationConnectee : destinationsConnectees) {
           destinationConnectee.recevoir(informationDecodee);
        }
        this.informationEmise = informationDecodee; 
	}


	public static void main(String[] args) {
		
	}
}
