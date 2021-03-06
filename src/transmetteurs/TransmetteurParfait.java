package transmetteurs;


import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConforme;

public class TransmetteurParfait extends Transmetteur<Boolean, Boolean>{

	/**
	 * Constructeur d'un transmetteur logique parfait
	 */
    public TransmetteurParfait() {
        super();
        informationRecue = new Information<Boolean>();
        informationEmise = new Information<Boolean>();
     }

    @Override
	public  void recevoir(Information <Boolean> information) throws InformationNonConforme{
		for(int i = 0; i<information.nbElements(); i++){
			informationRecue.add(information.iemeElement(i));
		}
	}

    @Override
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
