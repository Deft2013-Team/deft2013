package analyse;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import variables.vars;

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
public class NgramExtractor {

	
	
	public static void main(String[] args) {
		
		
		// store all ngrams
		HashMap<String, Integer> allgrams = new HashMap<String, Integer>();
		
		// by difficulty
		HashMap<String,Map<String,Integer>> c_dif  = new HashMap<String,Map<String,Integer>>();
	
		
		
		vars variable = new vars();
		
		try 
	    {
			
			BufferedReader reader = new BufferedReader(new FileReader(variable.pathToCorpus));
	       
	   
	        //--------------------------------------------------
	        // repeat until all lines from input file are read
	        //--------------------------------------------------
			String text = null;
			String content = null;
			
			ArrayList<String> ingredients = new ArrayList<String>(); 
			String recette = "";
			String currentType = "";
			String currentDiff = "";
			
	        while (( text = reader.readLine()) != null) {
	        	
	        	// recipe detection
	        	if (text.contains("<recette id=")){
	        		
	        		String id = text.replace("<recette id=\"", "");
	        		id = id.replace("\">", "");
	        		// System.out.println(id);
	        		
	        		// continue to read
	        		content = reader.readLine();
	        		content = content.replaceAll("\t", "");
	        		content = content.replaceFirst("\\s+", "");
	        		
	        		
	        		while ( ! content.contentEquals("</recette>") ) {
	        			
	        			if (content.contains("<titre>")){
	        				
	        				content = content.replace("<titre>", "");
	        				content = content.replace("</titre>", "");
	        				
	        				// System.out.println("Titre : " + content);
	        			}
	        			
	        			if (content.contains("<type>")){
	        				
	        				content = content.replace("<type>", "");
	        				content = content.replace("</type>", "");
	        				currentType = content;
	        				
	        			}
	        			
	        			if (content.contains("<niveau>")){
	        				
	        				content = content.replace("<niveau>", "");
	        				content = content.replace("</niveau>", "");
	        				currentDiff = content;
	        			
	        			}
	        			
	        			if (content.contains("<cout>")){
	        				
	        				content = content.replace("<cout>", "");
	        				content = content.replace("</cout>", "");
	        				// System.out.println("Cout:" + content);
	        			}
	        			
	        			
	        			if (content.contains("<ingredients>")){
	        				
	        				while ( ! content.contains("</ingredients>") ) {
	        					
	        					content = reader.readLine();
	        				}
	        				
	        			}
	        			
	        			// recipe text
	        			if (content.contains("<preparation>")){
	        				recette = "";
	        				while ( ! content.contains("</preparation>") ) {
	        			
	        					recette = recette + content;
	        					
	        					content = reader.readLine();
	        				}
	        				
	        				// clean
	        				recette = recette.replace("<preparation>", "");
	        				recette = recette.replace("<![CDATA[", "");
	        				recette = recette.replace("]]>", "");
	        				
	        				recette = recette.replaceAll("\\.", " \\. "); // space dots
	        				recette = recette.replaceAll("\n", " "); // delete carriage return
	        				recette = recette.replaceAll("[,();:’!?\\.]", " , "); // space segments, replace by unique char
	        				
	        				// replace double spaces by unique space
	        				recette = recette.replaceAll("\\s+", " ");
	        				recette = recette.toLowerCase();
	        				
	        				// extract n-grams
	        				String[] wordsinrecepy = recette.split(" ");
	        				
	        				int granumbers = variable.ngramsSize;
	        				
	        				for (int u = 0; u< wordsinrecepy.length -  granumbers - 1; u++){
	        					
	        					String grams ="";
	        					// 2 grams
	        					if ( granumbers == 2) { 
	        							grams = wordsinrecepy[u] + "_" + wordsinrecepy[u+1];
	        					}else{
		        					// 3 grams
		        					grams = wordsinrecepy[u] + "_" + wordsinrecepy[u+1]+ "_"+ wordsinrecepy[u+2];
	        					}
	        					
	        					if (! grams.contains(",")){
	        						
	        						// fill general index
		        					if (allgrams.containsKey(grams) ) { 
		        							int value = allgrams.get(grams) +1;
		        							allgrams.put(grams, value);
		        							
		        							// fill multidimensional hash
		        							if ( c_dif.get(grams).containsKey(currentDiff) ){
		        								int actualvalue = c_dif.get(grams).get(currentDiff);
		        								c_dif.get(grams).put(currentDiff, actualvalue + 1);
		        							}else{
		        								
		        								c_dif.get(grams).put(currentDiff, 1);
		        							}
		        							
		        					}else{
		        							allgrams.put(grams, 1);
		        							
		        							
		        							// fill multidimensional hash
		        							c_dif.put(grams, new HashMap<String,Integer>());
		        							c_dif.get(grams).put(currentDiff, 1);
		        					}
		        					
		        					// fill type index
		        					
	        					}
	        				}
	        				
	        			}
	        			
	        			content = reader.readLine();
	        			content = content.replaceAll("\t", "");
	        			content = content.replaceFirst("\\s+", "");
	        		}
	        		
	        	}
	        
	        }
	        
	        reader.close();

	    }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } 
        
        
        
        // stats
    	System.out.println("Statistiques n-grams");
		
    	ArrayList<Object> as = new ArrayList<Object>( allgrams.entrySet() );  
        
        Collections.sort( as , new Comparator<Object>() {  
        	public int compare( Object o2 , Object o1  )  
            {  
                Map.Entry e1 = (Map.Entry)o1 ;  
                Map.Entry e2 = (Map.Entry)o2 ;  
                Integer first = (Integer)e1.getValue();  
                Integer second = (Integer)e2.getValue();  
                return first.compareTo( second );  
            }  
        });  
          
       Iterator<?> i = as.iterator();  
       while ( i.hasNext() )  
       {  
    	   String ngram = (String)i.next().toString();
    	   String[] solgram = ngram.split("=");
    	   
    	   int niv0 = 0;
    	   int niv1 = 0;
    	   int niv2 = 0;
    	   int niv3 = 0;
    	   
    	   int maxn0 = 6962;
    	   int maxn1 = 5752;
    	   int maxn2 = 1068;
    	   int maxn3 = 80;
    	   
    	   try{
    		   niv0 = c_dif.get(solgram[0]).get("Très facile");
        	   niv1 = c_dif.get(solgram[0]).get("Facile");
        	   niv2 = c_dif.get(solgram[0]).get("Moyennement difficile");
        	   niv3 = c_dif.get(solgram[0]).get("Difficile");
        	   
    	    }catch (Exception e) {
	            //e.printStackTrace();
	        } 
    	   
    	    float rap0 = (float)niv0 / (float)maxn0;
    	    float rap1 = (float)niv1 / (float)maxn1;
    	    float rap2 = (float)niv2 / (float)maxn2;
    	    // float rap3 = (float)niv3 / (float)maxn3;
    	    float rap3 = 0;
    	    
    	    System.out.print( ngram + "\tTrès facile:\t" + rap0 + "\tFacile:\t" + rap1 + "\tMoyennement difficile:\t" + rap2 + "\tDifficile:\t" + rap3 + "   ");
    	   
    	    float factor = (float) 1.5;
    	    if (rap0 > (rap1 * factor) && rap0 > (rap2 * factor) && rap0 > (rap3 * factor)) { System.out.print("TF");}
    	    if (rap1 > (rap0 * factor) && rap1 > (rap2 * factor) && rap1 > (rap3 * factor)) { System.out.print("F");}
    	    if (rap2 > (rap1 * factor) && rap2 > (rap0 * factor) && rap2 > (rap3 * factor)) { System.out.print("MD");}
    	    if (rap3 > (rap1 * factor) && rap3 > (rap2 * factor) && rap3 > (rap0 * factor)) { System.out.print("D");}
    	    
    	    System.out.println("");
    	   
       }
       
       
	}
	
}
