package vector;

/**
 * 
 * Classifier suite developed for DEFT2013 Machine Learning and Classification Task 
 * focusing on the automatic analysis of recipes in French.
 * 
 * http://deft.limsi.fr/2013/index.php?id=1&lang=en
 * 
 * Paper is on ResearchGate at:
 * http://goo.gl/vSVBU
 
 * The code authors: 
 * 			Dr Eric Charton http://www.echarton.com twitter.com/ericcharton
 * 			Dr Marie-Jean Meurs http://mjmrsc.com/research/ twitter.com/mjmrsc
 * 
 * The scientific contributors
 * 	        Dr Eric Charton (1), Dr Marie-Jean Meurs (2), Dr Ludovic Jean-Louis (1), Pr Michel Gagnon (1)
 * 			(1)(Polytechnique Montréal)(2)Concordia university
 * 
 * This java tool uses Weka, LibSVM to solve the classification Task of DEFT2013 ML campaign. It is 
 * publicly released to allow experience verification and student training. 
 * 
 * Please feel free to use it with official corpus resources or with the provided ARFF files. 
 * 
 * This software is free to use, modify and redistribute under Creative Commons by-nc/3.0 License Term
 * http://creativecommons.org/licenses/by-nc/3.0/
 * 
 * @author Eric, MJ
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
 * @author Eric
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
	 *  	constructor, initialize feature table
	 * 
	 */
	public buildvector(vars variables){
		
		
		pto_variables = variables;
		
		//-------------------------------------------------
		// Load lexical features - ingredients
		//-------------------------------------------------
		String pathToLexFeatures = variables.lexicalfeatures;
		// fill the list of features
		try {
			
			String featureline = "";
			
			BufferedReader reader = new BufferedReader(new FileReader(pathToLexFeatures));
			int featcount = 0;
			while (( featureline = reader.readLine()) != null) {
					 
					 String[] featureref = featureline.split("\t"); 
					 String[] featurename = featureref[0].split("=");
					 
					 // keep everything but markups and comments
					 if (! featurename[0].contains("#") && ! featurename[0].contains("<")){
			
						 lexFeatures.add(featurename[0].toLowerCase());
						 featcount++;
						 // System.out.println(featurename[0]);

					 }
					 
					 // if less params - end of loop
					 if ( featcount >= variables.parmsreduce && variables.parmsreduce != -1 ) { break;}
					 
			}
			System.out.println("Lex Feature count:"+ featcount);
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
		// Load weighted lexical features - vocab moydiff
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
					 
					 // keep everything but markups and comments
					 if (! featurename[0].contains("#") && ! featurename[0].contains("<")){
			
						 weightedLexFeatures.add(featurename[0].toLowerCase());
						 featcount++;
						 // System.out.println(featurename[0]);

					 }
					 
					 // if less params - end of loop
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
		// Load ngram features - actions
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
					 
					 // keep everything but markups and comments
					 if (! featurename[0].contains("#") && ! featurename[0].contains("<")){
						 
						 featurename[0] = featurename[0].replaceAll("_", " ");
						 
						 NgramsFeatures.add(featurename[0].toLowerCase());
						 
						 featcount++;
						

					 }
					 
					 // if less params - end of loop
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
		// Load weighted verb features
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
					 
					 // keep everything but markups and comments
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
	 * @param model = 1 for meal type / 2 for difficulty
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
			
				// output features
				for( int x =0; x < lexFeatures.size(); x++){
					
					String feature = lexFeatures.get(x);
					String namefeature = feature.replaceAll("\\s", "-");
					namefeature= namefeature.replaceAll("[,:=+']", "-");
					String ref = "lx" + String.valueOf(x) + namefeature; 
					
					headerArff = headerArff + "@ATTRIBUTE " + ref + "\tREAL \t\t%" + feature + "\n";
					
				}
			}

			if (pto_variables.useweightedlexfeat){
			
				// output weighted features
				for( int x =0; x < weightedLexFeatures.size(); x++){
					
					String feature = weightedLexFeatures.get(x);
					String namefeature = feature.replaceAll("\\s", "-");
					namefeature= namefeature.replaceAll("[,:=+']", "-");
					String ref = "lx" + String.valueOf(x) + namefeature; 
					
					headerArff = headerArff + "@ATTRIBUTE " + ref + "\tREAL \t\t%" + feature + "\n";
					
				}
			}
			
			if (pto_variables.usengramfeat){	
			
				// output ngram features
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
			
			// write classes
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
					// write semantic features
					for( int x =0; x < lexFeatures.size(); x++){
						
						String feature = lexFeatures.get(x);
						String namefeature = feature.replaceAll("\\s", "-");
						namefeature= namefeature.replaceAll("[,:=+']", "-");
						String ref = "lx" + String.valueOf(x) + namefeature; 
						
						headerArff = headerArff + "@ATTRIBUTE " + ref + "\tREAL \t\t%" + feature + "\n";
						
					}
			}

			if (pto_variables.useweightedlexfeat){
			
				// write weighted features
				for( int x =0; x < weightedLexFeatures.size(); x++){
					
					String feature = weightedLexFeatures.get(x);
					String namefeature = feature.replaceAll("\\s", "-");
					namefeature= namefeature.replaceAll("[,:=+']", "-");
					String ref = "lx" + String.valueOf(x) + namefeature; 
					
					headerArff = headerArff + "@ATTRIBUTE " + ref + "\tREAL \t\t%" + feature + "\n";
					
				}
			}
			
			if (pto_variables.usengramfeat){
				
					// write ngram features
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
			
			// write classes
			headerArff = headerArff + "@ATTRIBUTE class 	{tres-facile, facile, moyennement-difficile, difficile}\n";
			headerArff = headerArff + "@DATA\n";
		}
		
		
		
		
		return headerArff;
		
	}
	

	/**
	 * 
	 * 
	 * 
	 * @param textseq recipe text
	 * @param classtype recipe type
	 * @param classcost recipe cost
	 * @param nbing Nb ingredient in recipe (lines of list)
	 * @param classdif recipe difficulty
	 * @return
	 */
	public String getArffLine(String title, String textseq, String classtype, String classcost, int nbing, String classdif, int model){
		
		String vectorArff = "";
		
		// clean text sequence 
		textseq = textseq.replaceAll("\\s+", " ");
		textseq = textseq.toLowerCase();
		
		// fill sizes
		if (pto_variables.sizeoftext){
			
			String[] titlecontent = title.split(" ");
			int sizeoftgitle = titlecontent.length;
			
			String[] recipycontent = textseq.split(" ");
			int sizeofrecipy= recipycontent.length;
			
			vectorArff = vectorArff + sizeoftgitle + "," + sizeofrecipy +",";
		}
		
		// fill feature values
		if (pto_variables.uselexfeat){
			// lexical features
			for( int x =0; x < lexFeatures.size(); x++){
		
				if (textseq.contains(lexFeatures.get(x))){
					
					vectorArff = vectorArff + "1,";
					
				}else{
					
					vectorArff = vectorArff + "0,";
				}
				
			}
		}

		// fill weighted feature values
		if (pto_variables.useweightedlexfeat){
			// weighted lexical features
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
		
		// ngram features
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
		
		
		// nb of ingredients
		if (pto_variables.amountofingreds){
			/*// extraction from global list
			int numoflexfeat = 0;
			// lexical features
			for( int x =0; x < lexFeatures.size(); x++){
		
				if (textseq.contains(lexFeatures.get(x))){
					numoflexfeat++;
					
					
				}
				
			}
			vectorArff = vectorArff + numoflexfeat + ",";
			*/
			// nb lines of recipe list
			vectorArff = vectorArff + nbing + ",";
		}
		
		
		// verb features (3)
		//
		// ArrayList<String> VerbFeatures = new ArrayList<String>(); 
		// HashMap<String, Integer> VerbFeaturesWeight = new HashMap<String, Integer>();
		if (pto_variables.useverbfeat){
			
			int verb1 = 0;
			int verb2 = 0;
			int verb3 = 0;
			
			// occurrence count
			String[] wordsintext = textseq.split(" ");
			
			for( int x =0; x < VerbFeatures.size(); x++){
				
				
				for( int y =0; y <  wordsintext.length; y++){
					if (wordsintext[y].contains(VerbFeatures.get(x))){
						
						// increment
						int refdiff = VerbFeaturesWeight.get(VerbFeatures.get(x)); // numero de diff 1 2 ou 3
						
						if ( refdiff == 1 ) {verb1++;}
						if ( refdiff == 2 ) {verb2++;}
						if ( refdiff == 3 ) {verb3++;}
						
					}
				}
				
			}
			
			// add verb numbers (3)
			vectorArff = vectorArff + verb1 + "," + verb2 + "," + verb3 + ",";
			
			
		}
		
		// recipe cost
		if (pto_variables.usecost){
			//System.out.println(classcost);
			if ( classcost.contains("Bon") ) 
				{ vectorArff = vectorArff + "bon-marche,"; }
			else if ( classcost.contains("Moyen") ) 
				{ vectorArff = vectorArff + "moyen,"; }
			else { vectorArff = vectorArff + "cher,"; }
		}		
		
		
		// meal types
		if (model == 1){
			//System.out.println(classtype);
			if ( classtype.contains("principal") ) 
				{ vectorArff = vectorArff + "plat-principal"; }
			else if ( classtype.contains("Dessert") ) 
				{ vectorArff = vectorArff + "dessert"; }
			else { vectorArff = vectorArff + "entree"; }
			// classes
		}
		
		// difficulty
		if (model == 2){
			//System.out.println(classtype);
			if ( classdif.contains("facile") ) 
				{ vectorArff = vectorArff + "tres-facile"; }
			else if ( classdif.contains("Facile") ) 
				{ vectorArff = vectorArff + "facile"; }
			else if ( classdif.contains("Moyennement") ) 
			{ vectorArff = vectorArff + "moyennement-difficile"; }
			else { vectorArff = vectorArff + "difficile"; }
			// classes
		}
		
//		vectorArff = vectorArff;
		
		return vectorArff;
		
	}
	
	
	
}
