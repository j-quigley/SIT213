package sources;

import information.Information;

import java.util.*;

import destinations.DestinationInterface;

public class SourceAleatoire extends Source<Boolean>{

   /** 
   * constructeur d'une SourceAleatoire 
   * @param occurence taille de la s�quence de nombres al�atoires
   */
	public SourceAleatoire(int occurence){
        super();
        informationGeneree = new Information();
        generateurDeSequenceAleatoire(occurence);
	}
	
   /**
    * cr�ation de la s�quence al�atoire
    */
    private void generateurDeSequenceAleatoire (int occurence){
        for (int i = 0; i < occurence; i++){
            Random r = new Random();
            informationGeneree.add(r.nextBoolean());
        }
    }
    
    /**
     * retourne l'information �mise
     * @return informationEmise
     */
    public Information<Boolean> getInformationEmise(){
    	return informationEmise;
    }
    /**
     * retour l'information g�n�r�e
     * @return informationGeneree
     */
    public Information<Boolean> getInformationGeneree(){
    	return informationGeneree;
    }
    
	public static void main(String[] args) {
        SourceAleatoire a = new SourceAleatoire(10);
        System.out.println(a.informationGeneree.toString());
    }
}