package sources;

import information.Information;

import java.util.*;

import destinations.DestinationInterface;

public class SourceAleatoire extends Source<Boolean>{

   /** 
   * constructeur d'une SourceAleatoire 
   * @param occurence taille de la séquence de nombres aléatoires
   */
	public SourceAleatoire(int occurence){
        super();
        informationGeneree = new Information();
        generateurDeSequenceAleatoire(occurence);
	}
	
   /**
    * création de la séquence aléatoire
    */
    private void generateurDeSequenceAleatoire (int occurence){
        for (int i = 0; i < occurence; i++){
            Random r = new Random();
            informationGeneree.add(r.nextBoolean());
        }
    }
    
    /**
     * retourne l'information émise
     * @return informationEmise
     */
    public Information<Boolean> getInformationEmise(){
    	return informationEmise;
    }
    /**
     * retour l'information générée
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