package corpus;

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
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import variables.vars;
import vector.buildvector;


/**
 * 
 * This is used to build test arff files. Those test files are submitted to the classifier
 * and a result is given in a csv file. This csv file is sent to the DEFT organizers
 * 
 * 
 * @author Eric
 *
 */
public class buildtest {

	
	public static void main(String[] args) {
		
		
		vars variables = new vars();
		buildvector vectorgenerator = new buildvector(variables);
		System.out.println("Features loaded ...");
		
		
		// output file names -> warning: set these in vars
		String outputT1 = "test_T1.arff"; // task 2 - type
		String outputT2 = "test_T2.arff"; // task 1 -difficulty
		
		// corpus path for task 1 or 2
		String corpuspath = "";
		String task = "";
		if (variables.modelType == 2){
			corpuspath = variables.xmlT1;
			task = "Difficulté";
		}
		if (variables.modelType == 1){
			corpuspath = variables.xmlT2;
			task = "Type";
		}
		
		
		System.out.println("ATTENTION, tâche " + task + " " + corpuspath + " ");
		
		try 
	    {
		
			// open output ARFF for training models
			String sortarffFileName = variables.modelOutputPath + outputT2; // default
			if (variables.modelType == 2) { 
				sortarffFileName = variables.modelOutputPath + outputT1; 
				
			}
			
			// Create file 
			FileWriter fstream = new FileWriter(sortarffFileName);
			BufferedWriter out = new BufferedWriter(fstream);
			 
			// get ARFF header then set output
			String outHeaderArff = vectorgenerator.genArffHeader(variables.modelType);
			// System.out.println(outHeaderArff); // verbose
			out.write(outHeaderArff + "\n");
		
			
			// reader for corpus
			BufferedReader reader = new BufferedReader(new FileReader(corpuspath));
	        
	   
	        //--------------------------------------------------
	        // repeat until all lines from input file are read
	        //--------------------------------------------------
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
	        	
	        	
	        	
	        	// recipe detection
	        	if (text.contains("<recette id=")){
	        		
	        		String id = text.replace("<recette id=\"", "");
	        		id = id.replace("\">", "");
	        		
	        		// initialize ingredient count
	        		nbIngred = 0;
	        		
	        		// continue reading
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
	        			
	        			// here: type and difficulty are deleted
	        			
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
	        				
	        				recette = ""; // reinitialize
	        				
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
	        		
	        		// end of if - get data and generate ARFF
	        		// default values for classes
	        		String Arffline = vectorgenerator.getArffLine(titre, recette, classeType , classeCout, nbIngred, classeDiff, variables.modelType); 
	        		
	        		// output line (Commented ID in front)
	        		out.write("%" + id + "\n");
	        		out.write(Arffline+ "\n");
	        		
	        	}
	        	
	        	
	        }
	        
	        System.out.println("Recettes traitées:" + recettecount);
	        out.write("\n");
	        out.close();
	        
	        reader.close();
	        
	    }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } 
		
		
	}
	
	
}
