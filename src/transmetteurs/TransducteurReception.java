package transmetteurs;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConforme;

public class TransducteurReception extends Transmetteur<Boolean, Boolean>{

	private Information<Boolean> informationDecodee;
	
	public TransducteurReception(){
		super();
		informationRecue = new Information<Boolean>();
		informationEmise = new Information<Boolean>();
		informationDecodee = new Information<Boolean>();
	}
	
	@Override
	public void recevoir(Information<Boolean> information) throws InformationNonConforme {
		for(int i=0; i<information.nbElements(); i++){
			informationRecue.add(information.iemeElement(i));
		}
	}
	
	public void decoder(){
		for(int i=0;i<informationRecue.nbElements();i++){
			if(informationRecue.iemeElement(i)){
				i++;
				if(informationRecue.iemeElement(i)){
					i++;
					if(informationRecue.iemeElement(i)){
						informationDecodee.add(true);
					}
					else{
						informationDecodee.add(false);
					}
				}
				else{
					i++;
					if(informationRecue.iemeElement(i)){
						informationDecodee.add(true);
					}
					else{
						informationDecodee.add(true);
					}
				}
			}
			else{
				i++;
				if(informationRecue.iemeElement(i)){
					i++;
					if(informationRecue.iemeElement(i)){
						informationDecodee.add(false);
					}
					else{
						informationDecodee.add(false);
					}
				}
				else{
					i++;
					if(informationRecue.iemeElement(i)){
						informationDecodee.add(true);
					}
					else{
						informationDecodee.add(false);
					}
				}
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

	@Override
	public void lInfo() {
		// TODO Auto-generated method stub
		
	}

}
