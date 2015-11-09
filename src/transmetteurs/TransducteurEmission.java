package transmetteurs;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConforme;

public class TransducteurEmission extends Transmetteur<Boolean, Boolean> {

	private Information<Boolean> informationCodee;
	/**
	 * Constructeur du transducteur d'Žmission
	 */
	public TransducteurEmission(){
		super();
		informationRecue = new Information<Boolean>();
		informationEmise = new Information<Boolean>();
		informationCodee = new Information<Boolean>();
	}

	@Override
	public void recevoir(Information<Boolean> information) throws InformationNonConforme {
		for(int i=0; i<information.nbElements(); i++){
			informationRecue.add(information.iemeElement(i));
		}
	}
	
	/**
	 * Codage de l'information
	 * 1 -> 101
	 * 0 -> 010
	 */
	public void coder(){
		for(int i=0; i<informationRecue.nbElements(); i++){
			if(informationRecue.iemeElement(i)){
				informationCodee.add(true);
				informationCodee.add(false);
				informationCodee.add(true);
			}
			else{	
			informationCodee.add(false);
			informationCodee.add(true);
			informationCodee.add(false);
			}
		}
	}

	@Override
	public void emettre() throws InformationNonConforme {
		coder();
		// Žmission vers les composants connectŽs  
        for (DestinationInterface <Boolean> destinationConnectee : destinationsConnectees) {
           destinationConnectee.recevoir(informationCodee);
        }
        this.informationEmise = informationCodee; 
	}

	@Override
	public void lInfo() {
		// TODO Auto-generated method stub
	}

}
