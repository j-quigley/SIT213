
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


/** La classe Simulateur permet de construire et simuler une chaîne de transmission composée d'une Source, d'un nombre variable de Transmetteur(s) et d'une Destination.  
 * @author cousin
 * @author prou
 *
 */
   public class Simulateur {
      	
   /** indique si le Simulateur utilise des sondes d'affichage */
      private          boolean affichage = true	;
   /** indique si le Simulateur utilise un message généré de manière aléatoire */
      private          boolean messageAleatoire = false;
   /** indique si le Simulateur utilise un germe pour initialiser les générateurs aléatoires */
      private          boolean aleatoireAvecGerme = false;
   /** la valeur de la semence utilisée pour les générateurs aléatoires */
      private          Integer seed = null;
   /** la longueur du message aléatoire à transmettre si un message n'est pas impose */
      private          int nbBitsMess = 100; 
   /** la chaîne de caractères correspondant à m dans l'argument -mess m */
      private          String messageString = "100";
   /** indique au simulateur le type de codage utilisŽ **/
      private		   String codage = "RZ";
   /** indique au simulateur le nombre d'Žchantillon ˆ utiliser **/
      private		   int nbEchantillons = 30;
   /** indique au simulateur la tension du niveau 0 **/
      private		   float aMin = 0.0f;
   /** indique au simulateur la tension du niveau 1 **/
      private		   float aMax = 1.0f;
      /** indique au simulateur le rapport signal ˆ bruit du transmetteur bruitŽ **/
      private		   float snr = 1f;
      /** indique au simulateur les trajectoires de décalage. decalage[x] = true si on genere un signal decale x  **/
      private		   Boolean []  decalage = new Boolean [5];
      /** indique au simulateur le décalage temporel pour chaque trajectoire **/
      private		   int []  dt = new int [5];
      /** indique au simulateur l'amplitude relative au decalage temporel **/
      private		   Float []  ar = new Float [5];
   	
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
      /** le  composant Destination de la chaine de transmission analogique avec bruit reel */
      private			  Destination <Boolean>  destinationAnalogiqueBruiteReel = null;
      
   /** le composant Sonde de la Source de la chaine de transmission */
      private SondeLogique sondeSource = null;
   /** le composant Sonde de la Destination de la chaine de transmission */
      private SondeLogique sondeDestination = null;
   /** le composant Sonde bruitŽ de la Destination de la chaine de transmission */
      private SondeLogique sondeDestinationBruite = null;   
      
   /** le composant Sonde analogique de la Source de la chaine de transmission */
      private SondeAnalogique sondeSourceAnalogique = null;
   /** le composant Sonde analogique de la Destination de la chaine de transmission */
      private SondeAnalogique sondeDestinationAnalogique = null;
      /** le composant Sonde analogique de la Destination de la chaine de transmission avec bruit*/
      private SondeAnalogique sondeDestinationAnalogiqueBruite = null;
      /** le composant Sonde analogique de la Destination de la chaine de transmission avec bruit réel*/
      private SondeAnalogique sondeDestinationAnalogiqueBruiteReel = null;


   /** le  composant Emetteur analogique de la chaine de transmission */
      private			  EmetteurAnalogique emetteurAnalogique = null;
      
   /** le  composant Transmetteur analogique parfait de la chaine de transmission */
      private			  Transmetteur <Float, Float>  transmetteurAnalogique = null;
   /** le  composant Recepteur analogique de la chaine de transmission */
      private			  RecepteurAnalogique recepteurAnalogique = null;
      
      /** le  composant Transmetteur analogique bruitŽ de la chaine de transmission */
      private			  Transmetteur <Float, Float>  transmetteurAnalogiqueBruite = null; 
      /** le  composant Transmetteur analogique bruité réel de la chaine de transmission */
      private			  Transmetteur <Float, Float>  transmetteurAnalogiqueBruiteReel = null; 
      /** le  composant Recepteur analogique bruitŽ de la chaine de transmission */
      private			  RecepteurAnalogique recepteurAnalogiqueBruite = null;
      /** le  composant Recepteur analogique bruitŽ de la chaine de transmission */
      private			  RecepteurAnalogique recepteurAnalogiqueBruiteReel = null;
 
      
   /** Le constructeur de Simulateur construit une chaîne de transmission composée d'une Source <Boolean>, d'une Destination <Boolean> et de Transmetteur(s) [voir la méthode analyseArguments]...  
   * <br> Les différents composants de la chaîne de transmission (Source, Transmetteur(s), Destination, Sonde(s) de visualisation) sont créés et connectés.
   * @param args le tableau des différents arguments.
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
			transmetteurAnalogiqueBruiteReel = new TransmetteurBruiteAnalogiqueReel(snr,decalage,dt,ar);
			destinationAnalogiqueBruite = new DestinationFinale();
			destinationAnalogiqueBruiteReel = new DestinationFinale();
			recepteurAnalogiqueBruite = new RecepteurAnalogique(aMin, aMax, codage, nbEchantillons);
			recepteurAnalogiqueBruiteReel = new RecepteurAnalogique(aMin, aMax, codage, nbEchantillons);
			
			
			//Connexion//
			emetteurAnalogique.connecter(transmetteurAnalogiqueBruite);	
			emetteurAnalogique.connecter(transmetteurAnalogiqueBruiteReel);	
			transmetteurAnalogiqueBruite.connecter(recepteurAnalogiqueBruite);
			transmetteurAnalogiqueBruiteReel.connecter(recepteurAnalogiqueBruiteReel);
			recepteurAnalogiqueBruite.connecter(destinationAnalogiqueBruite);
			recepteurAnalogiqueBruiteReel.connecter(destinationAnalogiqueBruiteReel);
			
			
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
				
				//Message analogique bruitŽ//
				sondeDestinationAnalogiqueBruite = new SondeAnalogique("Sonde Destination Analogique avec Bruit");
				sondeDestinationAnalogiqueBruiteReel = new SondeAnalogique("Sonde Destination Analogique avec Bruit Reel");
				sondeDestinationBruite = new SondeLogique("Sonde Destination Logique sans Bruit", 100);
				//Connexion//
				transmetteurAnalogiqueBruite.connecter(sondeDestinationAnalogiqueBruite);
				transmetteurAnalogiqueBruiteReel.connecter(sondeDestinationAnalogiqueBruiteReel);
				recepteurAnalogiqueBruite.connecter(sondeDestinationBruite);
			}
			
      }
   
   
   
   /** La méthode analyseArguments extrait d'un tableau de chaînes de caractères les différentes options de la simulation. 
   * Elle met à jour les attributs du Simulateur.
   *
   * @param args le tableau des différents arguments.
   * <br>
   * <br>Les arguments autorisés sont : 
   * <br> 
   * <dl>
   * <dt> -mess m  </dt><dd> m (String) constitué de 7 ou plus digits à 0 | 1, le message à transmettre</dd>
   * <dt> -mess m  </dt><dd> m (int) constitué de 1 à 6 digits, le nombre de bits du message "aléatoire" à  transmettre</dd> 
   * <dt> -s </dt><dd> utilisation des sondes d'affichage</dd>
   * <dt> -seed v </dt><dd> v (int) d'initialisation pour les générateurs aléatoires</dd> 
   * <br>
   * <dt> -form f </dt><dd>  codage (String) RZ, NRZR, NRZT, la forme d'onde du signal à transmettre (RZ par défaut)</dd>
   * <dt> -nbEch ne </dt><dd> ne (int) le nombre d'échantillons par bit (ne >= 6 pour du RZ, ne >= 9 pour du NRZT, ne >= 18 pour du RZ,  30 par défaut))</dd>
   * <dt> -ampl min max </dt><dd>  min (float) et max (float), les amplitudes min et max du signal analogique à transmettre ( min < max, 0.0 et 1.0 par défaut))</dd> 
   * <br>
   * <dt> -snr s </dt><dd> s (float) le rapport signal/bruit en dB</dd>
   * <br>
   * <dt> -ti i dt ar </dt><dd> i (int) numero du trajet indirect (de 1 à 5), dt (int) valeur du decalage temporel du ième trajet indirect 
   * en nombre d'échantillons par bit, ar (float) amplitude relative au signal initial du signal ayant effectué le ième trajet indirect</dd>
   * <br>
   * <dt> -transducteur </dt><dd> utilisation de transducteur</dd>
   * <br>
   * <dt> -aveugle </dt><dd> les récepteurs ne connaissent ni l'amplitude min et max du signal, ni les différents trajets indirects (s'il y en a).</dd>
   * <br>
   * </dl>
   * <br> <b>Contraintes</b> :
   * Il y a des interdépendances sur les paramètres effectifs. 
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
            	if(args[i].matches("-?[1-9][0-9]*")){
            		snr = Float.parseFloat(args[i]);
            	}
            	else{
               		throw new ArgumentsException("Valeur de parametre -snr invalide : " + args[i]);	
            	}
            }
            
            else if (args[i].matches("-ti")){
            	i++; 
            	for(int j=0;j<5;j++){
            		decalage[j] = false;
            	}
            	if(args[i].matches("[1]")){
            		decalage[0] = true;
            		i++;
            		if(args[i].matches("[1-9][0-9]*"))
            			dt[0] = (int)Float.parseFloat(args[i]);
            		i++;
            		if(args[i].matches("[0].[0-9][0-9]*"))
            			ar[0] = Float.parseFloat(args[i]);
            	}
            	else if(args[i].matches("[2]")){
            		decalage[1] = true;
            		i++;
            		if(args[i].matches("[1-9][0-9]*"))
            			dt[1] = (int)Float.parseFloat(args[i]);
            		i++;
            		if(args[i].matches("[0].[0-9][0-9]*"))
            			ar[1] = Float.parseFloat(args[i]);
            	}
            	else if(args[i].matches("[3]")){
            		decalage[2] = true;
            		i++;
            		if(args[i].matches("[1-9][0-9]*"))
            			dt[2] = (int)Float.parseFloat(args[i]);
            		i++;
            		if(args[i].matches("[0].[0-9][0-9]*"))
            			ar[2] = Float.parseFloat(args[i]);
            	}
            	else if(args[i].matches("[4]")){
            		decalage[3] = true;
            		i++;
            		if(args[i].matches("[1-9][0-9]*"))
            			dt[3] = (int)Float.parseFloat(args[i]);
            		i++;
            		if(args[i].matches("[0].[0-9][0-9]*"))
            			ar[3] = Float.parseFloat(args[i]);
            	}
            	else if(args[i].matches("[5]")){
            		decalage[4] = true;
            		i++;
            		if(args[i].matches("[1-9][0-9]*"))
            			dt[4] = (int)Float.parseFloat(args[i]);
            		i++;
            		if(args[i].matches("[0].[0-9][0-9]*"))
            			ar[4] = Float.parseFloat(args[i]);
            	}

            	else{
               		throw new ArgumentsException("Valeur de parametre -ti invalide : " + args[i]);	
            	}
            }
            
            
            else throw new ArgumentsException("Option invalide :"+ args[i]);
                  
         }
  
      }
     
    
   	
   /** La méthode execute effectue un envoi de message par la source de la chaîne de transmission du Simulateur. 
   * @return les options explicites de simulation.
   *
   * @throws Exception si un problème survient lors de l'exécution
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
	         transmetteurAnalogiqueBruiteReel.emettre();
	         
	         recepteurAnalogiqueBruite.decoder();
	         recepteurAnalogiqueBruite.emettre();
	         recepteurAnalogiqueBruiteReel.emettre();
	         
	         transmetteurAnalogiqueBruite.lInfo();
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
   
   
   
   
   /** La fonction main instancie un Simulateur à l'aide des arguments paramètres et affiche le résultat de l'exécution d'une transmission.
   *  @param args les différents arguments qui serviront à l'instanciation du Simulateur.
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

