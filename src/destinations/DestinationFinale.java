package destinations;

import information.Information;
import information.InformationNonConforme;

/** 
 * Classe  d'un composant destinationFinale d'informations dont les éléments sont de type Boolean
 */
public class DestinationFinale extends Destination<Boolean>{

    /** 
    * l'information reçue par la destination 
    */
    public DestinationFinale() {
        informationRecue = new Information();
     }
	
    /**
     * recoit une information 
     * @param information  l'information  a recevoir
     */
	public void recevoir(Information<Boolean> information) throws InformationNonConforme {
		for(int i = 0; i<information.nbElements(); i++){
			informationRecue.add(information.iemeElement(i));
		}
	}
	
	public static void main(String[] args) {
	}
}
