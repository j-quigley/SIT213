package transmetteurs;

import java.util.LinkedList;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConforme;

public class TransmetteurParfaitAnalogique extends Transmetteur<Float, Float>{

    public TransmetteurParfaitAnalogique() {
        super();
        informationRecue = new Information();
        informationEmise = new Information();
     }

	
	public  void recevoir(Information <Float> information) throws InformationNonConforme{
		for(int i = 0; i<information.nbElements(); i++){
			informationRecue.add(information.iemeElement(i));
		}
	}

	public void emettre() throws InformationNonConforme {
		// émission vers les composants connectés  
        for (DestinationInterface <Float> destinationConnectee : destinationsConnectees) {
           destinationConnectee.recevoir(informationRecue);
        }
        this.informationEmise = informationRecue; 
	}
	
	public void lInfo() {
		System.out.println("nombre d'éléments reçus transmetteur parfait" +informationRecue.nbElements());
	}

	public static void main(String[] args) {
		
	}

}
