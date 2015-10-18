package transmetteurs;

import information.Information;
import information.InformationNonConforme;

import java.util.LinkedList;

import visualisations.SondeAnalogique;
import destinations.DestinationInterface;

public class EmetteurAnalogique extends Transmetteur<Boolean, Float>{

	/**
	 * aMin, aMax : amplitude minimum et maximum du signal
	 * codage : type de codage du signal
	 * nbEchantillons : nombre d'Žchantillons pour le codage
	 * informationCodee : tableau contenant l'information codee
	 */
	private float aMin, aMax;
	private String codage;
	private int nbEchantillons;
	private Information <Float> informationCodee;
	
	/**
	 * @param aMin
	 * @param aMax
	 * @param codage
	 * @param nbEchantillons
	 * 
	 * Constructeur de l'EmetteurAnalogique qui recoit des informations de type booleen et renvoit des informations de type float
	 */
	public EmetteurAnalogique(float aMin, float aMax, String codage, int nbEchantillons) {
		super();
	    informationRecue = new Information<Boolean>();
	    informationEmise = new Information<Float>();
	    informationCodee = new Information<Float>();
	    this.aMin = aMin;
	    this.aMax = aMax;
	    this.codage = codage;
	    this.nbEchantillons = nbEchantillons;
	}
		
	@Override
	public  void recevoir(Information <Boolean> information) throws InformationNonConforme{
		for(int i = 0; i<information.nbElements(); i++){
			informationRecue.add(information.iemeElement(i));
		}
	}
	
	/**
	 * Fonction de codage RZ,NRZ ou NRZT
	 */
	public void coder(){
		if(codage.equals("RZ")){
			for(int i = 0; i<informationRecue.nbElements(); i++){
				for(int j = 1; j<=nbEchantillons; j++){
					if(informationRecue.iemeElement(i) == true){
						if((j>(nbEchantillons/3))&&(j<=(nbEchantillons*2/3))){
							informationCodee.add(aMax);
						}
						else{
							informationCodee.add(0.0f);
						}
					}
					else{
						informationCodee.add(0.0f);
					}
				}
			}
		}
		else if(codage.equals("NRZ")){
			for(int i = 0; i<informationRecue.nbElements(); i++){
				for(int j = 1; j<=nbEchantillons; j++){
					if(informationRecue.iemeElement(i) == true){
						informationCodee.add(aMax);
					}
					else{
						informationCodee.add(aMin);	
					}
				}
			}			
		}
		else if(codage.equals("NRZT")){
			for(int i = 0; i<informationRecue.nbElements(); i++){
				for(int j = 1; j<=nbEchantillons; j++){
					if(informationRecue.iemeElement(i) == true){
						if(j<=nbEchantillons/3){
							informationCodee.add(aMin+((aMax-aMin)/(nbEchantillons/3))*(j-1));
						}
						else if((j>nbEchantillons/3)&&(j<=nbEchantillons/3*2)){
							informationCodee.add(aMax);
						}
						else{
							informationCodee.add(aMin+((aMax-aMin)/(nbEchantillons/3))*(nbEchantillons-j));
						}
					}
					else{
						informationCodee.add(aMin);	
					}
				}
			}
		}	
	}

	@Override
	public void emettre() throws InformationNonConforme {
		coder();
		// Žmission vers les composants connectŽs  
        for (DestinationInterface <Float> destinationConnectee : destinationsConnectees) {
           destinationConnectee.recevoir(informationCodee);
        }
        this.informationEmise = informationCodee; 
	}
	
	/**
	 * Getteur de l'information codee
	 * @return informationCodee
	 */
	public Information <Float> getInformationCodee (){
		return informationCodee;
	}
	public void lInfo() {
		System.out.println("nombre d'éléments reçus" +informationRecue.nbElements());
	}


	public static void main(String[] args) {
	}
}
