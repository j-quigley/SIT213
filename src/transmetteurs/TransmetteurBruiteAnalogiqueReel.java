package transmetteurs;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConforme;

public class TransmetteurBruiteAnalogiqueReel extends Transmetteur<Float, Float> {

	private double ps = 0;
	
	private Information <Float>  informationGeneree;
	private Information <Float> [] informationDecalee= new Information[5];
	
	private Boolean decalage[] = new Boolean[5];
	private int dt[] = new int[5];
	private Float ar[] = new Float[5];
	
	public TransmetteurBruiteAnalogiqueReel(Boolean decalage[],int dt [], Float ar[]) {
		super();
		informationRecue = new Information<Float>();
		informationGeneree = new Information<Float>();
	    informationEmise = new Information<Float>();
	    this.ar = ar;
	    this.dt = dt;
	    this.decalage = decalage;
	}

	
	public Information <Float> decalerSignal (float ar, int dt){

		Information <Float>  info = new Information<Float>();
		int compteur = 0;
		for(int i=0; i<informationRecue.nbElements(); i++){
			if (i<dt){
				info.add(0.0f);
			}
			else {	
				info.add((ar*informationRecue.iemeElement(compteur)));
				compteur++;
			}
		}
		return info;
	}
	

	
	@Override
	public void recevoir(Information<Float> information)
			throws InformationNonConforme {
		for(int i = 0; i<information.nbElements(); i++){
			informationRecue.add(information.iemeElement(i));
			ps = ps + (information.iemeElement(i)*information.iemeElement(i)); 
		}
	}
	
	public void ajouterDecalage() {
		for(int i=0;i<5;i++){
			if (decalage[i]) {
				informationDecalee[i] = decalerSignal(ar[i],dt[i]);
			}
			else {
				informationDecalee[i] = decalerSignal(0,0);
			}
		}
		for(int i = 0; i<informationRecue.nbElements(); i++){
			informationGeneree.add(informationRecue.iemeElement(i)+informationDecalee[0].iemeElement(i)
					+informationDecalee[1].iemeElement(i)+informationDecalee[2].iemeElement(i)
					+informationDecalee[3].iemeElement(i)+informationDecalee[4].iemeElement(i));
		}
	}
	
	@Override
	public void emettre() throws InformationNonConforme {
		ajouterDecalage();
		
        for (DestinationInterface <Float> destinationConnectee : destinationsConnectees) {
            destinationConnectee.recevoir(informationGeneree);
         }
         this.informationEmise = informationGeneree; 

	}

	public void lInfo() {
		System.out.println(informationGeneree.toString());
	}
	
	public static void main(String[] args) {
		
	}

}
