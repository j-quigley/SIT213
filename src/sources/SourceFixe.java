package sources;

import information.Information;

import java.util.*;

public class SourceFixe extends Source<Boolean>{
	
   /** 
   * constructeur d'une SourceFixe
   * @param messageFixe message � retranscrire en boolean
   */
	public SourceFixe(String messageFixe){
        super();
        informationGeneree = new Information();
        generateurDeSequenceFixe(messageFixe);
	} 
	
   /**
    * cr�ation de la s�quence fixe
    */
    private void generateurDeSequenceFixe (String messageFixe){
    	String messageSplit[] = messageFixe.split("");
    	for (int i = 0; i <= messageFixe.length(); i++){
    		if(messageSplit[i].equals("0")){
    			informationGeneree.add(false);
    		}
    		else if(messageSplit[i].equals("1")){
    			informationGeneree.add(true);
    		}
        }
    }
    
    /**
     * retourne l'information �mise
     * @return une information
     */
    public Information<Boolean> getInformationEmise(){
    	return informationEmise;
    }
	
	public static void main(String[] args) {
		SourceFixe a = new SourceFixe("1001");
        System.out.println(a.informationGeneree.toString());
    }

}