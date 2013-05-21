package corpus;

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
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import variables.vars;
import vector.buildvector;

/**
 * 
 * Build a arff model from XML files according to vars parameters. This is used to generate a train model that can be used in weka gui
 * 
 * 
 * @author eric
 *
 */
public class buildmodel {

	
	public static void main(String[] args) {
		
		
		vars variables = new vars();
		buildvector vectorgenerator = new buildvector(variables);
		System.out.println("Features loaded ...");
		
		// noms de fichier des modèles
		String modelFileNameDiff = "mj_verb_cost_nbing_wfeat_1_bestgreedy_mdif_ngram_bestgreedy.arff"; //0.612 BN5
		String modelTypeNameDiff = "modele-type-mixed-2.arff";
		// String modelTypeNameDiff = "mj_verb_cost_nbing_wfeat_1_bestgreedy_mdif_type.arff";
		
		try 
	    {
		
			// ouvrir le fichier ARFF de sortie pour les modèles d'apprentissage
			String sortarffFileName = variables.modelOutputPath + modelTypeNameDiff; // par defaut valeur 1
			if (variables.modelType == 2) { 
				// sortarffFileName = variables.modelOutputPath + "modele-diff-mixed-num.arff"; 
				sortarffFileName = variables.modelOutputPath + modelFileNameDiff; 
				
			}
			
			// Create file 
			FileWriter fstream = new FileWriter(sortarffFileName);
			BufferedWriter out = new BufferedWriter(fstream);
			 
			// récupérer le header ARFF et le sortir
			String outHeaderArff = vectorgenerator.genArffHeader(variables.modelType);
			// System.out.println(outHeaderArff); // verbose
			out.write(outHeaderArff + "\n");
		
			
			// reader for corpus
			BufferedReader reader = new BufferedReader(new FileReader(variables.pathToCorpus));
	        
	   
	        //--------------------------------------------
	        // repeat until all lines is read
	        // from the file
	        //--------------------------------------------
			String text = null;
			String content = null;
			
			String recette = "";
			String titre = "";
			String classeDiff = "";
			String classeType = "";
			String classeCout = "";
			int nbIngred = 0;
			
			int recettecount = 0;
	        while (( text = reader.readLine()) != null) {
	        	
	        	
	        	
	        	// detecte une recette
	        	if (text.contains("<recette id=")){
	        		
	        		String id = text.replace("<recette id=\"", "");
	        		id = id.replace("\">", "");
	        		// System.out.println(id);
	        		
	        		// remettre a zero le compteur de lignes d'ingredients
	        		nbIngred = 0;
	        		
	        		// lire la suite
	        		content = reader.readLine();
	        		content = content.replaceAll("\t", "");
	        		content = content.replaceFirst("\\s+", "");
	        		
	        		
	        		while ( ! content.contentEquals("</recette>") ) {
	        			
	        			if (content.contains("<titre>")){
	        				
	        				content = content.replace("<titre>", "");
	        				content = content.replace("</titre>", "");
	        				titre = content;
	        				// System.out.println("Titre : " + content);
	        			}
	        			
	        			if (content.contains("<type>")){
	        				
	        				content = content.replace("<type>", "");
	        				content = content.replace("</type>", "");
	        				// System.out.println("Type:" + content);
	        				classeType = content;
	        			}
	        			
	        			if (content.contains("<niveau>")){
	        				
	        				content = content.replace("<niveau>", "");
	        				content = content.replace("</niveau>", "");
	        				// System.out.println("Niveau:" + content);
	        				classeDiff = content;
	        			}
	        			
	        			if (content.contains("<cout>")){
	        				
	        				content = content.replace("<cout>", "");
	        				content = content.replace("</cout>", "");
	        				// System.out.println("Cout:" + content);
	        				classeCout = content;
	        			}
	        			
	        			
	        			if (content.contains("<ingredients>")){
	        				
	        				while ( ! content.contains("</ingredients>") ) {
	    	        			if (content.contains("<p>")){
	    	        				nbIngred ++;
	    	        			}	        					
	        					content = reader.readLine();
	        				}
	        				
	        			}
	        			
	        			if (content.contains("<preparation>")){
	        				
	        				recette = ""; // raz
	        				
	        				while ( ! content.contains("</preparation>") ) {
	        			
	        					recette = recette + content;
	        					
	        					content = reader.readLine();
	        				}
	        				
	        				// clean
	        				recette = recette.replace("<preparation>", "");
	        				recette = recette.replace("<![CDATA[", "");
	        				recette = recette.replace("]]>", "");
	        				recette = recette.replaceFirst("\\s+", "");
	        				// System.out.println("Recette:" + recette);
	        				
	        				recettecount++;
	        				recette = recette.toLowerCase();
	        			}
	        			
	        			
	        			content = reader.readLine();
	        			content = content.replaceAll("\t", "");
	        			content = content.replaceFirst("\\s+", "");
	        		}
	        		
	        		// fin du if collecter les données et produire le ARFF
	        		String Arffline = vectorgenerator.getArffLine(titre, recette, classeType , classeCout, nbIngred, classeDiff, variables.modelType); 
	        		
	        		// sortir la ligne sur le disque
	        		out.write(Arffline+ "\n");
	        		// out.write(id + " " + Arffline + "\n"); // avec num devant pour expe michel
	        		
	        	}
	        	
	        	
	        }
	        
	        System.out.println("Recettes traitées:" + recettecount);
	        out.write("\n");
	        out.close();
	        
	    }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } 
		
		
	}
	
	
}
