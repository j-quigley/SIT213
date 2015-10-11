package transmetteurs;

import java.util.LinkedList;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConforme;

public class TransmetteurParfait extends Transmetteur<Boolean, Boolean>{

    public TransmetteurParfait() {
        super();
        informationRecue = new Information();
        informationEmise = new Information();
     }

	
	public  void recevoir(Information <Boolean> information) throws InformationNonConforme{
		for(int i = 0; i<information.nbElements(); i++){
			informationRecue.add(information.iemeElement(i));
		}
	}

	public void emettre() throws InformationNonConforme {
		// �mission vers les composants connect�s  
        for (DestinationInterface <Boolean> destinationConnectee : destinationsConnectees) {
           destinationConnectee.recevoir(informationRecue);
        }
        this.informationEmise = informationRecue; 
	}
	
	public void lInfo() {
		System.out.println("nombre d'�l�ments re�us" +informationRecue.nbElements());
	}

	public static void main(String[] args) {
		
	}

}
