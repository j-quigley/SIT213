package transmetteurs;

import information.Information;
import information.InformationNonConforme;

import java.util.LinkedList;

import visualisations.SondeAnalogique;
import destinations.DestinationInterface;

public class EmetteurAnalogique extends Transmetteur<Boolean, Float>{

	private float aMin, aMax;
	private String codage;
	private int nbEchantillons;
	private Information <Float> informationCodee;
	
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
		
	public  void recevoir(Information <Boolean> information) throws InformationNonConforme{
		for(int i = 0; i<information.nbElements(); i++){
			informationRecue.add(information.iemeElement(i));
		}
	}
	
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
							if(i==0){
								informationCodee.add(aMin+((aMax-aMin)/(nbEchantillons/3))*(j-1));
							}
							else if(informationRecue.iemeElement(i-1)==true){
								informationCodee.add(aMax);
							}
							else{
								informationCodee.add(aMin+((aMax-aMin)/(nbEchantillons/3))*(j-1));
							}
						}
						else if((j>nbEchantillons/3)&&(j<=nbEchantillons/3*2)){
							informationCodee.add(aMax);
						}
						else{
							if(i==informationRecue.nbElements()-1){
								informationCodee.add(aMin+((aMax-aMin)/(nbEchantillons/3))*(nbEchantillons-j));
							}
							else if(informationRecue.iemeElement(i+1)==true){
								informationCodee.add(aMax);
							}
							else{
								informationCodee.add(aMin+((aMax-aMin)/(nbEchantillons/3))*(nbEchantillons-j));
							}
						}
					}
					else{
						informationCodee.add(aMin);	
					}
				}
			}
		}	
	}

	public void emettre() throws InformationNonConforme {
		// émission vers les composants connectés  
        for (DestinationInterface <Float> destinationConnectee : destinationsConnectees) {
           destinationConnectee.recevoir(informationCodee);
        }
        this.informationEmise = informationCodee; 
	}

	public static void main(String[] args) {
	}
}
