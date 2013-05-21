package vector;

/**
 * 
 * Classifier suite developed for DEFT2013 Machine Learning and Classification Task 
 * applied focusing on the automatic analysis of recipes in French.
 * 
 * http://deft.limsi.fr/2013/index.php?id=1&lang=en
 * 
 * Paper is on ResearchGate at:
 * http://goo.gl/vSVBU
 
 * The code authors : 
 * 			Dr Eric Charton http://www.echarton.con twitter/ericcharton
 * 			Dr Marie-Jean Meurs http://mjmrsc.com/research/ twitter.com/mjmrsc
 * 
 * The scientific contributors
 * 	        Dr Eric Charton (1), Dr Marie-jean Meurs (2), Dr Ludovic Jean-Louis (1), Pr Michel Gagnon (1)
 * 			(1)(Polytechnique Montréal)(2)Concordia university
 * 
 * This java tool uses Weka, LibSVM to solve the classification Task of DEFT2013 ML campaign. It is 
 * publicly released to allow experience verification and student training. 
 * 
 * Please free to use it with official corpus resources or with the ARFF provided. 
 * 
 * This software is free to use, modifiy and redistribute under Creative Commons by-nc/3.0 Licence Term
 * http://creativecommons.org/licenses/by-nc/3.0/
 * 
 * @author eric, MJ
 *
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import variables.vars;

/**
 * 
 * This class is used to generate each arff line in a model according to vars configuration. It is mainly called by buildmodel.java
 * 
 * 
 * @author eric
 *
 */
public class buildvector {

	
	// variables
	ArrayList<String> lexFeatures = new ArrayList<String>(); 
	// variables
	ArrayList<String> weightedLexFeatures = new ArrayList<String>();
	// variables
	ArrayList<String> NgramsFeatures = new ArrayList<String>(); 
	// variables
	ArrayList<String> VerbFeatures = new ArrayList<String>(); 
	HashMap<String, Integer> VerbFeaturesWeight = new HashMap<String, Integer>();
	
	vars pto_variables = null;
	
	/**
	 *  	constructeur, initialise le tableau de futures
	 * 
	 */
	public buildvector(vars variables){
		
		
		pto_variables = variables;
		
		//-------------------------------------------------
		// Charger les features lexicales - ingrédients
		//-------------------------------------------------
		String pathToLexFeatures = variables.lexicalfutures;
		// fill the list of features
		try {
			
			String featureline = "";
			
			BufferedReader reader = new BufferedReader(new FileReader(pathToLexFeatures));
			int featcount = 0;
			while (( featureline = reader.readLine()) != null) {
					 
					 String[] featureref = featureline.split("\t"); 
					 String[] featurename = featureref[0].split("=");
					 
					 // garder tout sauf balises et commentaires
					 if (! featurename[0].contains("#") && ! featurename[0].contains("<")){
			
						 lexFeatures.add(featurename[0].toLowerCase());
						 featcount++;
						 // System.out.println(featurename[0]);

					 }
					 
					 // si reduction de parametres - fin de boucle
					 if ( featcount >= variables.parmsreduce && variables.parmsreduce != -1 ) { break;}
					 
			}
			System.out.println("Lex Feature count:"+ featcount);

		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//-------------------------------------------------
		// Charger les features lexicales pour ponderation - vocab moydiff
		//-------------------------------------------------
		String pathToWeightedLexFeatures = variables.weightedlexicalfeats;
		// fill the list of features
		try {
			
			String featureline = "";
			
			BufferedReader reader = new BufferedReader(new FileReader(pathToWeightedLexFeatures));
			int featcount = 0;
			while (( featureline = reader.readLine()) != null) {
					 
					 String[] featureref = featureline.split("\t"); 
					 String[] featurename = featureref[0].split("=");
					 
					 // garder tout sauf balises et commentaires
					 if (! featurename[0].contains("#") && ! featurename[0].contains("<")){
			
						 weightedLexFeatures.add(featurename[0].toLowerCase());
						 featcount++;
						 // System.out.println(featurename[0]);

					 }
					 
					 // si reduction de parametres - fin de boucle
					 if ( featcount >= variables.parmsreduce && variables.parmsreduce != -1 ) { break;}
					 
			}
			System.out.println("weighted Lex Feature count:"+ featcount);
			reader.close();

		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		//-------------------------------------------------
		// Charger les features ngrams - actions
		//-------------------------------------------------
		String pathToNgramsFeatures = variables.pathToNgramsFeatures;
		// fill the list of features
		try {
			
			String featureline = "";
			
			BufferedReader reader = new BufferedReader(new FileReader(pathToNgramsFeatures));
			int featcount = 0;
			while (( featureline = reader.readLine()) != null) {
					 
					 String[] featureref = featureline.split("\t"); 
					 String[] featurename = featureref[0].split("=");
					 
					 // garder tout sauf balises et commentaires
					 if (! featurename[0].contains("#") && ! featurename[0].contains("<")){
						 
						 featurename[0] = featurename[0].replaceAll("_", " ");
						 
						 NgramsFeatures.add(featurename[0].toLowerCase());
						 
						 featcount++;
						

					 }
					 
					 // si reduction de parametres - fin de boucle
					 if ( featcount >= variables.parmsreduce && variables.parmsreduce != -1 ) { break;}
					 
			}
			System.out.println("Ngrams Feature count:"+ featcount);
			reader.close();

		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//-------------------------------------------------
		// Charger les features verbs par poids
		//-------------------------------------------------
		String pathtoweightedverbfeats = variables.pathtoweightedverbfeats;
		
		// fill the list of features
		try {
			
			String featureline = "";
			
			BufferedReader reader = new BufferedReader(new FileReader(pathtoweightedverbfeats));
			int featcount = 0;
			while (( featureline = reader.readLine()) != null) {
					 
					 String[] featureref = featureline.split("\t"); 
					 String[] featurename = featureref[0].split("=");
					 
					 // garder tout sauf balises et commentaires
					 if (! featurename[0].contains("#") && ! featurename[0].contains("<")){
			
						 VerbFeatures.add(featurename[0].toLowerCase());
						 featcount++;
						 
						 int VWeight = Integer.parseInt(featurename[1]);
						 VerbFeaturesWeight.put(featurename[0].toLowerCase(), VWeight );
						 
						 // System.out.println(featurename[0] + "-" + VWeight);

					 }
					 
					 
			}
			System.out.println("Verb Feature count:"+ featcount);
			reader.close();

		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	/**
	 * @param model = 1 pour le type de plat / 2 pour la difficulté
	 * @return
	 */
	public String genArffHeader(int model){
		
		String headerArff = "% Fichier d'entrainement Weka pour Deft 2013\n";
		headerArff = headerArff  + "\n";
		
		if (model == 1){
			
			headerArff = headerArff + "@RELATION typedeplat\n";
			
			if (pto_variables.sizeoftext){
				headerArff = headerArff + "@ATTRIBUTE sizeoftitle \tREAL \t\t%size of title\n";
				headerArff = headerArff + "@ATTRIBUTE sizeofrecipy \tREAL \t\t%size of title\n";
			}
			
			if (pto_variables.uselexfeat){
			
				// sortir les features
				for( int x =0; x < lexFeatures.size(); x++){
					
					String feature = lexFeatures.get(x);
					String namefeature = feature.replaceAll("\\s", "-");
					namefeature= namefeature.replaceAll("[,:=+']", "-");
					String ref = "lx" + String.valueOf(x) + namefeature; 
					
					headerArff = headerArff + "@ATTRIBUTE " + ref + "\tREAL \t\t%" + feature + "\n";
					
				}
			}

			if (pto_variables.useweightedlexfeat){
			
				// sortir les features ponderees
				for( int x =0; x < weightedLexFeatures.size(); x++){
					
					String feature = weightedLexFeatures.get(x);
					String namefeature = feature.replaceAll("\\s", "-");
					namefeature= namefeature.replaceAll("[,:=+']", "-");
					String ref = "lx" + String.valueOf(x) + namefeature; 
					
					headerArff = headerArff + "@ATTRIBUTE " + ref + "\tREAL \t\t%" + feature + "\n";
					
				}
			}
			
			if (pto_variables.usengramfeat){	
			
				// sortir les features ngrams
				for( int x =0; x < NgramsFeatures.size(); x++){
					
					String feature = NgramsFeatures.get(x);
					String namefeature = feature.replaceAll("\\s", "-");
					namefeature= namefeature.replaceAll("[,:=+']", "-");
					String ref = "lx" + String.valueOf(x) + namefeature; 
					
					headerArff = headerArff + "@ATTRIBUTE " + ref + "\tREAL \t\t%" + feature + "\n";
					
				}
				
				
			}
			
			if (pto_variables.amountofingreds){
				
				headerArff = headerArff + "@ATTRIBUTE foundingreds\tREAL \t\t%foundingreds\n";
				
			}
			
			if (pto_variables.useverbfeat){
				
				headerArff = headerArff + "@ATTRIBUTE verb1\tREAL \t\t%verb1\n";
				headerArff = headerArff + "@ATTRIBUTE verb2\tREAL \t\t%verb2\n";
				headerArff = headerArff + "@ATTRIBUTE verb3\tREAL \t\t%verb3\n";
				
			}
			
			
			if (pto_variables.usecost){
				
				headerArff = headerArff + "@ATTRIBUTE cost\t{bon-marche, moyen, cher}\n";
				
			}	
			
			// sortir les classes
			headerArff = headerArff + "@ATTRIBUTE class 	{plat-principal, dessert, entree}\n\n";
			headerArff = headerArff + "@DATA\n";
		}
		
		if (model == 2){
			
			headerArff = headerArff + "@RELATION difficulte\n";
			
			if (pto_variables.sizeoftext){
				headerArff = headerArff + "@ATTRIBUTE sizeoftitle \tREAL \t\t%size of title\n";
				headerArff = headerArff + "@ATTRIBUTE sizeofrecipy \tREAL \t\t%size of title\n";
			}
					
			if (pto_variables.uselexfeat){
					// sortir les features sémantiques
					for( int x =0; x < lexFeatures.size(); x++){
						
						String feature = lexFeatures.get(x);
						String namefeature = feature.replaceAll("\\s", "-");
						namefeature= namefeature.replaceAll("[,:=+']", "-");
						String ref = "lx" + String.valueOf(x) + namefeature; 
						
						headerArff = headerArff + "@ATTRIBUTE " + ref + "\tREAL \t\t%" + feature + "\n";
						
					}
			}

			if (pto_variables.useweightedlexfeat){
			
				// sortir les features ponderees
				for( int x =0; x < weightedLexFeatures.size(); x++){
					
					String feature = weightedLexFeatures.get(x);
					String namefeature = feature.replaceAll("\\s", "-");
					namefeature= namefeature.replaceAll("[,:=+']", "-");
					String ref = "lx" + String.valueOf(x) + namefeature; 
					
					headerArff = headerArff + "@ATTRIBUTE " + ref + "\tREAL \t\t%" + feature + "\n";
					
				}
			}
			
			if (pto_variables.usengramfeat){
				
					// sortir les features ngrams
					for( int x =0; x < NgramsFeatures.size(); x++){
						
						String feature = NgramsFeatures.get(x);
						String namefeature = feature.replaceAll("\\s", "-");
						namefeature= namefeature.replaceAll("[,:=+']", "-");
						String ref = "lx" + String.valueOf(x) + namefeature; 
						
						headerArff = headerArff + "@ATTRIBUTE " + ref + "\tREAL \t\t%" + feature + "\n";
						
					}
			}

			
			if (pto_variables.amountofingreds){
				
				headerArff = headerArff + "@ATTRIBUTE foundingreds\tREAL \t\t%foundingreds\n";
				
			}
			
			if (pto_variables.useverbfeat){
				
				headerArff = headerArff + "@ATTRIBUTE verb1\tREAL \t\t%verb1\n";
				headerArff = headerArff + "@ATTRIBUTE verb2\tREAL \t\t%verb2\n";
				headerArff = headerArff + "@ATTRIBUTE verb3\tREAL \t\t%verb3\n";
				
			}
			
			if (pto_variables.usecost){
				
				headerArff = headerArff + "@ATTRIBUTE cost\t{bon-marche, moyen, cher}\n";
				
			}			
			
			// sortir les classes
			headerArff = headerArff + "@ATTRIBUTE class 	{tres-facile, facile, moyennement-difficile, difficile}\n";
			headerArff = headerArff + "@DATA\n";
		}
		
		
		
		
		return headerArff;
		
	}
	

	/**
	 * 
	 * 
	 * 
	 * @param textseq Texte de la recette
	 * @param classtype Type de recette
	 * @param classcost Cout de la recette
	 * @param nbing Nombre d'ingredients de la recette (lignes de la liste)
	 * @param classdif Difficulté de la recette
	 * @return
	 */
	public String getArffLine(String title, String textseq, String classtype, String classcost, int nbing, String classdif, int model){
		
		String vectorArff = "";
		
		// clean text sequence 
		textseq = textseq.replaceAll("\\s+", " ");
		textseq = textseq.toLowerCase();
		
		// fill the sizes
		if (pto_variables.sizeoftext){
			
			String[] titlecontent = title.split(" ");
			int sizeoftgitle = titlecontent.length;
			
			String[] recipycontent = textseq.split(" ");
			int sizeofrecipy= recipycontent.length;
			
			vectorArff = vectorArff + sizeoftgitle + "," + sizeofrecipy +",";
		}
		
		// fill the features values
		if (pto_variables.uselexfeat){
			// sortir les features lexicales
			for( int x =0; x < lexFeatures.size(); x++){
		
				if (textseq.contains(lexFeatures.get(x))){
					
					vectorArff = vectorArff + "1,";
					
				}else{
					
					vectorArff = vectorArff + "0,";
				}
				
			}
		}

		// fill the weighted features values
		if (pto_variables.useweightedlexfeat){
			// sortir les features lexicales ponderees
			for( int x =0; x < weightedLexFeatures.size(); x++){
		
				if (textseq.contains(weightedLexFeatures.get(x))){
					
					//vectorArff = vectorArff + "10,";
					//vectorArff = vectorArff + "20,";
					vectorArff = vectorArff + "1,";
					
				}else{
					
					vectorArff = vectorArff + "0,";
				}
				
			}
		}
		
		// sortir les features ngrams
		if (pto_variables.usengramfeat){
			
			for( int x =0; x < NgramsFeatures.size(); x++){
		
				//String smatcher = NgramsFeatures.get(x);
				//smatcher = smatcher.replace("_", " ");
				
				if (textseq.contains(NgramsFeatures.get(x))){
					
					vectorArff = vectorArff + "1,";
					
				}else{
					
					vectorArff = vectorArff + "0,";
				}
				
			}
		}
		
		
		// sortir le nombre d'ingrédients
		if (pto_variables.amountofingreds){
			/*// extraction depuis la liste globale
			int numoflexfeat = 0;
			// sortir les features lexicales
			for( int x =0; x < lexFeatures.size(); x++){
		
				if (textseq.contains(lexFeatures.get(x))){
					numoflexfeat++;
					
					
				}
				
			}
			vectorArff = vectorArff + numoflexfeat + ",";
			*/
			// nb de ligne de la liste de la recette
			vectorArff = vectorArff + nbing + ",";
		}
		
		
		// sortir les features verbes (3)
		//
		// ArrayList<String> VerbFeatures = new ArrayList<String>(); 
		// HashMap<String, Integer> VerbFeaturesWeight = new HashMap<String, Integer>();
		if (pto_variables.useverbfeat){
			
			int verb1 = 0;
			int verb2 = 0;
			int verb3 = 0;
			
			// compter les occurences
			String[] wordsintext = textseq.split(" ");
			
			for( int x =0; x < VerbFeatures.size(); x++){
				
				
				for( int y =0; y <  wordsintext.length; y++){
					if (wordsintext[y].contains(VerbFeatures.get(x))){
						
						// incrément
						int refdiff = VerbFeaturesWeight.get(VerbFeatures.get(x)); // numero de diff 1 2 ou 3
						
						if ( refdiff == 1 ) {verb1++;}
						if ( refdiff == 2 ) {verb2++;}
						if ( refdiff == 3 ) {verb3++;}
						
					}
				}
				
			}
			
			// ajouter les 3 numéros de verbes
			vectorArff = vectorArff + verb1 + "," + verb2 + "," + verb3 + ",";
			
			
		}
		
		// sortir le cout de la recette
		if (pto_variables.usecost){
			//System.out.println(classcost);
			if ( classcost.contains("Bon") ) 
				{ vectorArff = vectorArff + "bon-marche,"; }
			else if ( classcost.contains("Moyen") ) 
				{ vectorArff = vectorArff + "moyen,"; }
			else { vectorArff = vectorArff + "cher,"; }
		}		
		
		
		// types de plats
		if (model == 1){
			//System.out.println(classtype);
			if ( classtype.contains("principal") ) 
				{ vectorArff = vectorArff + "plat-principal"; }
			else if ( classtype.contains("Dessert") ) 
				{ vectorArff = vectorArff + "dessert"; }
			else { vectorArff = vectorArff + "entree"; }
			// sortir les classes
		}
		
		// difficulté
		if (model == 2){
			//System.out.println(classtype);
			if ( classdif.contains("facile") ) 
				{ vectorArff = vectorArff + "tres-facile"; }
			else if ( classdif.contains("Facile") ) 
				{ vectorArff = vectorArff + "facile"; }
			else if ( classdif.contains("Moyennement") ) 
			{ vectorArff = vectorArff + "moyennement-difficile"; }
			else { vectorArff = vectorArff + "difficile"; }
			// sortir les classes
		}
		
		vectorArff = vectorArff;
		
		return vectorArff;
		
	}
	
	
	
}
