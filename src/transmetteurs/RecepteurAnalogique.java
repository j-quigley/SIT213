package transmetteurs;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConforme;

public class RecepteurAnalogique extends Transmetteur<Float, Boolean>{

	/**
	 * aMin, aMax : amplitude minimum et maximum du signal
	 * codage : type de codage du signal
	 * nbEchantillons : nombre d'Žchantillons pour le codage
	 * informationDecodee : tableau contenant l'information decodee
	 */
	private float aMin, aMax;
	private String codage;
	private int nbEchantillons;
	private Information <Boolean> informationDecodee;
	
	/**
	 * @param aMin
	 * @param aMax
	 * @param codage
	 * @param nbEchantillons
	 * 
	 * Constructeur du RecepteurAnalogique qui recoit des informations de type float et renvoit des informations de type booleen
	 */
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
	
	/**
	 * Fonction de decodage RZ,NRZ ou NRZT
	 */
	public void decoder(){
		Boolean valeurDecodee = false;
		int compteur = 0;
		float sommes = 0;
		float moyenne;
		for(int i = 0; i<informationRecue.nbElements(); i++){
			if(((i+1)%nbEchantillons > nbEchantillons/3)&&((i+1)%nbEchantillons <= 2*nbEchantillons/3)){
				compteur++;
				sommes += informationRecue.iemeElement(i);
			}
			if((i+1)%nbEchantillons == 0){
				moyenne = sommes/compteur;
				if((moyenne > (aMax-(aMax/5)))&&(moyenne < (aMax+(aMax/5)))){
					valeurDecodee = true;
				}
				informationDecodee.add(valeurDecodee);
				valeurDecodee = false;
				compteur = 0;
				sommes = 0;
			}
		}
	}

	@Override
	public void emettre() throws InformationNonConforme {
		decoder();
		// Žmission vers les composants connectŽs  
        for (DestinationInterface <Boolean> destinationConnectee : destinationsConnectees) {
           destinationConnectee.recevoir(informationDecodee);
        }
        this.informationEmise = informationDecodee; 
	}

	/**
	 * Getteur de l'information decodee
	 * @return informationDecodee
	 */
	public Information <Boolean> getInformationDecodee (){
		return informationDecodee;
	}
	
	public void lInfo() {
		System.out.println("nombre d'éléments reçus" +informationRecue.nbElements());
	}

	public static void main(String[] args) {
		
	}
}
