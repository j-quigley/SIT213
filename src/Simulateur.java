
   import sources.*;
   import destinations.*;
   import transmetteurs.*;

   import information.*;

   import visualisations.*;

   import java.util.regex.*;
   import java.util.*;
   import java.lang.Math;

	
   import java.io.FileInputStream;
   import java.io.FileNotFoundException;
   import java.io.BufferedWriter;
   import java.io.FileWriter;
   import java.io.IOException;
   import java.io.PrintWriter;


/** La classe Simulateur permet de construire et simuler une cha�ne de transmission compos�e d'une Source, d'un nombre variable de Transmetteur(s) et d'une Destination.  
 * @author cousin
 * @author prou
 *
 */
   public class Simulateur {
      	
   /** indique si le Simulateur utilise des sondes d'affichage */
      private          boolean affichage = false;
   /** indique si le Simulateur utilise un message g�n�r� de mani�re al�atoire */
      private          boolean messageAleatoire = false;
   /** indique si le Simulateur utilise un germe pour initialiser les g�n�rateurs al�atoires */
      private          boolean aleatoireAvecGerme = false;
   /** la valeur de la semence utilis�e pour les g�n�rateurs al�atoires */
      private          Integer seed = null;
   /** la longueur du message al�atoire � transmettre si un message n'est pas impose */
      private          int nbBitsMess = 100; 
   /** la cha�ne de caract�res correspondant � m dans l'argument -mess m */
      private          String messageString = "100";
   /** indique au simulateur le type de codage utilis� **/
      private		   String codage = "RZ";
   /** indique au simulateur le nombre d'�chantillon � utiliser **/
      private		   int nbEchantillons = 30;
   /** indique au simulateur la tension du niveau 0 **/
      private		   float aMin = 0.0f;
   /** indique au simulateur la tension du niveau 1 **/
      private		   float aMax = 1.0f;
      /** indique au simulateur le rapport signal � bruit du transmetteur bruit� **/
      private		   float snr = 1f;
   	
   /** le  composant Source de la chaine de transmission */
      private			  Source <Boolean>  source = null;
   /** le  composant Transmetteur parfait logique de la chaine de transmission */
      private			  Transmetteur <Boolean, Boolean>  transmetteurLogique = null;
   /** le  composant Destination de la chaine de transmission */
      private			  Destination <Boolean>  destination = null;
      /** le  composant Destination de la chaine de transmission analogique */
      private			  Destination <Boolean>  destinationAnalogique = null;
      /** le  composant Destination de la chaine de transmission analogique avec bruit */
      private			  Destination <Boolean>  destinationAnalogiqueBruite = null;
      
   /** le composant Sonde de la Source de la chaine de transmission */
      private SondeLogique sondeSource = null;
   /** le composant Sonde de la Destination de la chaine de transmission */
      private SondeLogique sondeDestination = null;
   /** le composant Sonde bruit� de la Destination de la chaine de transmission */
      private SondeLogique sondeDestinationBruite = null;   
      
   /** le composant Sonde analogique de la Source de la chaine de transmission */
      private SondeAnalogique sondeSourceAnalogique = null;
   /** le composant Sonde analogique de la Destination de la chaine de transmission */
      private SondeAnalogique sondeDestinationAnalogique = null;
   /** le composant Sonde analogique de la Destination de la chaine de transmission avec bruit*/
      private SondeAnalogique sondeDestinationAnalogiqueBruite = null;


   /** le  composant Emetteur analogique de la chaine de transmission */
      private			  EmetteurAnalogique emetteurAnalogique = null;
      
   /** le  composant Transmetteur analogique parfait de la chaine de transmission */
      private			  Transmetteur <Float, Float>  transmetteurAnalogique = null;
   /** le  composant Recepteur analogique de la chaine de transmission */
      private			  RecepteurAnalogique recepteurAnalogique = null;
      
   /** le  composant Transmetteur analogique bruit� de la chaine de transmission */
      private			  Transmetteur <Float, Float>  transmetteurAnalogiqueBruite = null;       
   /** le  composant Recepteur analogique bruit� de la chaine de transmission */
      private			  RecepteurAnalogique recepteurAnalogiqueBruite = null;
 
      
   /** Le constructeur de Simulateur construit une cha�ne de transmission compos�e d'une Source <Boolean>, d'une Destination <Boolean> et de Transmetteur(s) [voir la m�thode analyseArguments]...  
   * <br> Les diff�rents composants de la cha�ne de transmission (Source, Transmetteur(s), Destination, Sonde(s) de visualisation) sont cr��s et connect�s.
   * @param args le tableau des diff�rents arguments.
   *
   * @throws ArgumentsException si un des arguments est incorrect
   *
   */   
      public  Simulateur(String [] args) throws ArgumentsException {
			analyseArguments(args);
			
			//////////////////////
			//Simulation logique//
			//////////////////////
			if(messageAleatoire == true){
    		    source = new SourceAleatoire(nbBitsMess);
    	    }
    	    else{
    		    source = new SourceFixe(messageString);
    	    }
			transmetteurLogique = new TransmetteurParfait();
			destination = new DestinationFinale();  
			
			//Connexion//
			source.connecter(transmetteurLogique);
			transmetteurLogique.connecter(destination);
			
			
			//////////////////////////////////
			//Simulation Analogique Parfaite//
			//////////////////////////////////
			emetteurAnalogique = new EmetteurAnalogique(aMin, aMax, codage, nbEchantillons);
			transmetteurAnalogique = new TransmetteurParfaitAnalogique();
			recepteurAnalogique = new RecepteurAnalogique(aMin, aMax, codage, nbEchantillons);
			destinationAnalogique = new DestinationFinale();
			
			//Connexion//
			source.connecter(emetteurAnalogique);
			emetteurAnalogique.connecter(transmetteurAnalogique);
			transmetteurAnalogique.connecter(recepteurAnalogique);
			recepteurAnalogique.connecter(destinationAnalogique);
			
			
			/////////////////////////////////
			//Simulation Analogique Bruite//
			/////////////////////////////////
			transmetteurAnalogiqueBruite = new TransmetteurBruiteAnalogique(snr);
			destinationAnalogiqueBruite = new DestinationFinale();
			recepteurAnalogiqueBruite = new RecepteurAnalogique(aMin, aMax, codage, nbEchantillons);
			
			
			//Connexion//
			emetteurAnalogique.connecter(transmetteurAnalogiqueBruite);	
			transmetteurAnalogiqueBruite.connecter(recepteurAnalogiqueBruite);	
			recepteurAnalogiqueBruite.connecter(destinationAnalogiqueBruite);
			
			
			/////////////
			//Affichage//
			/////////////
			if (affichage == true){
				//Message de base//
				sondeSource = new SondeLogique("Sonde Source", 100);
				sondeDestination = new SondeLogique("Sonde Destination", 100);
				//Connextion//
				source.connecter(sondeSource);
				transmetteurLogique.connecter(sondeDestination);
				
				//Message analogique parfait//
				sondeSourceAnalogique = new SondeAnalogique("Sonde Source Analogique");
				sondeDestinationAnalogique = new SondeAnalogique("Sonde Destination Analogique");
				//Connexion//
				emetteurAnalogique.connecter(sondeSourceAnalogique);
				transmetteurAnalogique.connecter(sondeDestinationAnalogique);	
				
				//Message analogique bruit�//
				sondeDestinationAnalogiqueBruite = new SondeAnalogique("Sonde Destination Analogique avec Bruit");
				sondeDestinationBruite = new SondeLogique("Sonde Destination Logique sans Bruit", 100);
				//Connexion//
				transmetteurAnalogiqueBruite.connecter(sondeDestinationAnalogiqueBruite);
				recepteurAnalogiqueBruite.connecter(sondeDestinationBruite);
			}
			
      }
   
   
   
   /** La m�thode analyseArguments extrait d'un tableau de cha�nes de caract�res les diff�rentes options de la simulation. 
   * Elle met � jour les attributs du Simulateur.
   *
   * @param args le tableau des diff�rents arguments.
   * <br>
   * <br>Les arguments autoris�s sont : 
   * <br> 
   * <dl>
   * <dt> -mess m  </dt><dd> m (String) constitu� de 7 ou plus digits � 0 | 1, le message � transmettre</dd>
   * <dt> -mess m  </dt><dd> m (int) constitu� de 1 � 6 digits, le nombre de bits du message "al�atoire" � transmettre</dd> 
   * <dt> -s </dt><dd> utilisation des sondes d'affichage</dd>
   * <dt> -seed v </dt><dd> v (int) d'initialisation pour les g�n�rateurs al�atoires</dd> 
   * <br>
   * <dt> -form f </dt><dd>  codage (String) RZ, NRZR, NRZT, la forme d'onde du signal � transmettre (RZ par d�faut)</dd>
   * <dt> -nbEch ne </dt><dd> ne (int) le nombre d'�chantillons par bit (ne >= 6 pour du RZ, ne >= 9 pour du NRZT, ne >= 18 pour du RZ,  30 par d�faut))</dd>
   * <dt> -ampl min max </dt><dd>  min (float) et max (float), les amplitudes min et max du signal analogique � transmettre ( min < max, 0.0 et 1.0 par d�faut))</dd> 
   * <br>
   * <dt> -snr s </dt><dd> s (float) le rapport signal/bruit en dB</dd>
   * <br>
   * <dt> -ti i dt ar </dt><dd> i (int) numero du trajet indirect (de 1 � 5), dt (int) valeur du decalage temporel du i�me trajet indirect 
   * en nombre d'�chantillons par bit, ar (float) amplitude relative au signal initial du signal ayant effectu� le i�me trajet indirect</dd>
   * <br>
   * <dt> -transducteur </dt><dd> utilisation de transducteur</dd>
   * <br>
   * <dt> -aveugle </dt><dd> les r�cepteurs ne connaissent ni l'amplitude min et max du signal, ni les diff�rents trajets indirects (s'il y en a).</dd>
   * <br>
   * </dl>
   * <br> <b>Contraintes</b> :
   * Il y a des interd�pendances sur les param�tres effectifs. 
   *
   * @throws ArgumentsException si un des arguments est incorrect.
   *
   */   
      public  void analyseArguments(String[] args)  throws  ArgumentsException {
      		
         for (int i=0;i<args.length;i++){ 
         
              
            if (args[i].matches("-s")){
               affichage = true;
            }
            else if (args[i].matches("-seed")) {
               aleatoireAvecGerme = true;
               i++; 
            	// traiter la valeur associee
               try { 
                  seed =new Integer(args[i]);
               }
                  catch (Exception e) {
                     throw new ArgumentsException("Valeur du parametre -seed  invalide :" + args[i]);
                  }           		
            }
            
            else if (args[i].matches("-mess")){
               i++; 
            	// traiter la valeur associee
               messageString = args[i];
               if (args[i].matches("[0,1]{7,}")) {
                  messageAleatoire = false;
                  nbBitsMess = args[i].length();
               } 
               else if (args[i].matches("[0-9]{1,6}")) {
                  messageAleatoire = true;
                  nbBitsMess = new Integer(args[i]);
                  if (nbBitsMess < 1) 
                     throw new ArgumentsException ("Valeur du parametre -mess invalide : " + nbBitsMess);
               }
               else 
                  throw new ArgumentsException("Valeur du parametre -mess invalide : " + args[i]);
            }
            else if (args[i].matches("-form")){
            	i++; 
            	if((args[i].matches("RZ"))|(args[i].matches("NRZ"))|(args[i].matches("NRZT"))){
            		codage = args[i];
            	}
            	else{
            		throw new ArgumentsException("Valeur de parametre -form invalide : " + args[i]);
            	}
            }
            else if (args[i].matches("-nbEch")){
            	i++; 
            	if(args[i].matches("[1-9][0-9]*")){
            		nbEchantillons = Integer.parseInt(args[i]);
            	}
            	else{
            		throw new ArgumentsException("Valeur de parametre -nbEch invalide : " + args[i]);
            	}
            }
            else if (args[i].matches("-ampl")){
            	i++; 
            	if(args[i].matches("-?[0-9]+(\\.[0-9]+)?")){
            		aMin = Float.parseFloat(args[i]);
            		i++; 
            		if((args[i].matches("-?[0-9]+(\\.[0-9]+)?"))&&(aMin<Float.parseFloat(args[i]))){
            			aMax = Float.parseFloat(args[i]);
            		}
            		else{
                		throw new ArgumentsException("Valeur de parametre -ampl invalide : " + args[i]);
                	}
            	}
            	else{
            		throw new ArgumentsException("Valeur de parametre -ampl invalide : " + args[i]);
            	}
            }             
            else if (args[i].matches("-snr")){
            	i++; 
            	if(args[i].matches("[0]\\.[0-9]*[0-9]")){
            		snr = Float.parseFloat(args[i]);
            	}
            	else{
               		throw new ArgumentsException("Valeur de parametre -snr invalide : " + args[i]);	
            	}
            }
            else throw new ArgumentsException("Option invalide :"+ args[i]);
         }
      
      }
     
    
   	
   /** La m�thode execute effectue un envoi de message par la source de la cha�ne de transmission du Simulateur. 
   * @return les options explicites de simulation.
   *
   * @throws Exception si un probl�me survient lors de l'ex�cution
   *
   */ 
      public void execute() throws Exception {      
    	 try{
	         source.emettre();
	         transmetteurLogique.emettre();
	         emetteurAnalogique.coder();
	         emetteurAnalogique.emettre();       
	         transmetteurAnalogique.emettre();
	         transmetteurAnalogiqueBruite.emettre();
	         recepteurAnalogiqueBruite.decoder();
	         recepteurAnalogiqueBruite.emettre();
    	 }
    	 catch (Exception e){
    		 throw new Exception("Erreur lors de l'envoi sur la chaine de transmission");
    	 }
      }
     	   
      public float  calculTauxErreurBinaire() {
      
      	Information<Boolean> informationEmise = source.getInformationEmise();
      	Information<Boolean> informationRecue = destinationAnalogiqueBruite.getInformationRecue();
      	
      	float nbErreur = 0;
      	
      	for(int i = 0; i < informationEmise.nbElements(); i++){
      		if(informationEmise.iemeElement(i)!=informationRecue.iemeElement(i)){
      			nbErreur++;
      		}
      	}
      	
         return  nbErreur/(float)informationEmise.nbElements();
      }
   
   
   
   
   /** La fonction main instancie un Simulateur � l'aide des arguments param�tres et affiche le r�sultat de l'ex�cution d'une transmission.
   *  @param args les diff�rents arguments qui serviront � l'instanciation du Simulateur.
   */
      public static void main(String [] args) { 
      
         Simulateur simulateur = null;

         try {
            simulateur = new Simulateur(args);
         }
            catch (Exception e) {
               System.out.println(e); 
               System.exit(-1);
            } 
      		
         try {
            simulateur.execute();
            float tauxErreurBinaire = simulateur.calculTauxErreurBinaire();
            String s = "java  Simulateur  ";
            for (int i = 0; i < args.length; i++) {
         		s += args[i] + "  ";
         	}
            System.out.println(s + "  =>   TEB : " + tauxErreurBinaire);
         }
            catch (Exception e) {
               System.out.println(e);
               e.printStackTrace();
               System.exit(-2);
            }              	
      }
   }

