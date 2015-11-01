package transmetteurs;

import visualisations.SondeAnalogique;
import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConforme;
import java.lang.Math;

public class RecepteurAnalogique extends Transmetteur<Float, Boolean>{

	/**
	 * aMin, aMax : amplitude minimum et maximum du signal
	 * codage : type de codage du signal
	 * nbEchantillons : nombre d'Žchantillons pour le codage
	 * informationDecodee : tableau contenant l'information decodee
	 * decalage : tableau de Boolean indiquant les trajets mutltiples actifs
	 * dt : tableau d'entier indiquant le dŽcalage temporelle en nombre d'Žchantillons du trajet correspondant
	 * ar : tableau de float infiquant l'attŽnuation du trajet correspondant
	 */
	private float aMin, aMax;
	private String codage;
	private int nbEchantillons;
	private Information <Float> informationMultiTrajetDecodee;
	private Information <Boolean> informationDecodee;
	private Boolean[] decalage;
	private int[] dt;
	private Float[] ar;
	
	
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
	    informationMultiTrajetDecodee = new Information<Float>();
	    informationEmise = new Information<Boolean>();
	    informationDecodee = new Information<Boolean>();
	    this.aMin = aMin;
	    this.aMax = aMax;
	    this.codage = codage;
	    this.nbEchantillons = nbEchantillons;
	}
	/**
	 * 
	 * @param aMin
	 * @param aMax
	 * @param codage
	 * @param nbEchantillons
	 * @param decalage
	 * @param dt
	 * @param ar
	 * 
	 * Constructeur du RecepteurAnalogique qui recoit des informations de type float et renvoit des informations de type booleen
	 */
	public RecepteurAnalogique(float aMin, float aMax, String codage, int nbEchantillons, Boolean[] decalage, int []  dt, Float []  ar) {
		super();
	    informationRecue = new Information<Float>();
	    informationMultiTrajetDecodee = new Information<Float>();
	    informationEmise = new Information<Boolean>();
	    informationDecodee = new Information<Boolean>();
	    this.aMin = aMin;
	    this.aMax = aMax;
	    this.codage = codage;
	    this.nbEchantillons = nbEchantillons;
	    this.decalage = decalage;
	    this.dt = dt;
	    this.ar = ar;
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

		for(int i=0; i<informationRecue.nbElements(); i++){
			
			//RŽduction des multi-trajets//
			informationMultiTrajetDecodee.add(0.0f); //initialisation des ŽlŽments au fil de l'eau
			float retrait = 0;
			for(int j=0; j<5; j++){
				if(decalage[j]&&i>=dt[j]){
					int floor = (int)Math.floor((i-dt[j])/nbEchantillons);
					if(informationDecodee.iemeElement(floor)){
						retrait += Math.abs(aMax*ar[j]);
					}
					else{
						retrait += Math.abs(aMin*ar[j]);
					}
				}
			}
			
		
			informationMultiTrajetDecodee.setIemeElement(i, informationRecue.iemeElement(i)-retrait);
			
			//RŽduction du bruit//
			if(((i+1)%nbEchantillons > nbEchantillons/3)&&((i+1)%nbEchantillons <= 2*nbEchantillons/3)){
				compteur++;
				sommes += informationMultiTrajetDecodee.iemeElement(i);
			}
			if((i+1)%nbEchantillons == 0){
				moyenne = sommes/compteur;
				if((moyenne > (aMax-(aMax/5)))/*&&(moyenne < (aMax+(aMax/5)))*/){
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
