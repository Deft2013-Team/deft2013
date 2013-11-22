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
public class FeatureExtractor {
	
	
	public static void main(String[] args) {
	
		
				// Stats
				HashMap<String, Integer> niveau = new HashMap<String, Integer>();
				HashMap<String, Integer> type = new HashMap<String, Integer>();
				HashMap<String, Integer> ingredientrefs = new HashMap<String, Integer>();
				
				// cross-stats 
				HashMap<String, String> c_type = new HashMap<String, String>();
				HashMap<String, String> c_niv = new HashMap<String, String>();
				HashMap<String, String> c_recette = new HashMap<String, String>();
				
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
					
			        while (( text = reader.readLine()) != null) {
			        	
			        	// recipe detection
			        	if (text.contains("<recette id=")){
			        		
			        		String id = text.replace("<recette id=\"", "");
			        		id = id.replace("\">", "");
			        		System.out.println(id);
			        		
			        		// continue to read
			        		content = reader.readLine();
			        		content = content.replaceAll("\t", "");
			        		content = content.replaceFirst("\\s+", "");
			        		
			        		
			        		while ( ! content.contentEquals("</recette>") ) {
			        			
			        			if (content.contains("<titre>")){
			        				
			        				content = content.replace("<titre>", "");
			        				content = content.replace("</titre>", "");
			        				
			        				System.out.println("Titre : " + content);
			        			}
			        			
			        			if (content.contains("<type>")){
			        				
			        				content = content.replace("<type>", "");
			        				content = content.replace("</type>", "");
			        				
			        				if ( type.containsKey(content)) { type.put(content, type.get(content)+1); } else { type.put(content, 1); }
			        				
			        				// store the type
			        				c_type.put(id,content);
			        				
			        				//System.out.println("Type:" + content);
			        			}
			        			
			        			if (content.contains("<niveau>")){
			        				
			        				content = content.replace("<niveau>", "");
			        				content = content.replace("</niveau>", "");
			        				
			        				if ( niveau.containsKey(content)) { niveau.put(content, niveau.get(content)+1); } else { niveau.put(content, 1); }
			        				
			        				// store the difficulty level
			        				c_niv.put(id,content);
			        				
			        				//System.out.println("Niveau:" + content);
			        			}
			        			
			        			if (content.contains("<cout>")){
			        				
			        				content = content.replace("<cout>", "");
			        				content = content.replace("</cout>", "");
			        				// System.out.println("Cout:" + content);
			        			}
			        			
			        			
			        			if (content.contains("<ingredients>")){
			        				
			        				while ( ! content.contains("</ingredients>") ) {
			        			
			        					// split 
			        					// oeufs- g sucre- g de farine-  sachet de sucre vanill�- cl de cr�me liquide-  cuill�res � soupe de lait
			        					// xxx:brousse (rayon de la mascarpone�.) parenth�ses
			        					
			        					// clean
			        					String ingredient = content;
			        					ingredient = ingredient.toLowerCase();
			        					
			        					ingredient = ingredient.replaceAll("<ingredients>", "");
			        					ingredient = ingredient.replaceAll("[0-9]+", "");
			        					
			        					ingredient = ingredient.replaceAll(" g ", " ");
			        					ingredient = ingredient.replaceAll(" cl ", " ");
			        					ingredient = ingredient.replaceAll(" lb ", " ");
			        					ingredient = ingredient.replaceAll(" lbs ", " ");
			        					ingredient = ingredient.replaceAll(" oz ", " ");
			        					ingredient = ingredient.replaceAll(" cc ", " ");
			        					ingredient = ingredient.replaceAll(" gr ", " ");
			        					ingredient = ingredient.replaceAll(" grs ", " ");
			        					ingredient = ingredient.replaceAll(" kg ", " ");
			        					ingredient = ingredient.replaceAll(" l ", " ");
			        					ingredient = ingredient.replaceAll("dl ", " ");
			        					ingredient = ingredient.replaceAll("litre", " ");
			        					
			        					ingredient = ingredient.replaceFirst("^g ",  " ");
			        					
			        					ingredient = ingredient.replaceAll("<p>", "");
			        					ingredient = ingredient.replaceAll("</p>", "");
			        					ingredient = ingredient.replaceAll("pincée de", "");
			        					
			        					ingredient = ingredient.replaceAll("cuillère à café", "");
			        					ingredient = ingredient.replaceAll("cuillères à café", "");
			        					ingredient = ingredient.replaceAll("cuillère à soupe", "");
			        					ingredient = ingredient.replaceAll("cuillères à soupe", "");
			        					ingredient = ingredient.replaceAll("cuillerées à soupe", "");
			        					
			        					ingredient = ingredient.replaceAll("pointe de couteau", "");
			        					ingredient = ingredient.replaceAll("belles tranches", "");
			        					ingredient = ingredient.replaceAll("belle tranche", "");
			        					ingredient = ingredient.replaceAll("petites poignées", "");
			        					ingredient = ingredient.replaceAll("petite poignée", "");
			        					ingredient = ingredient.replaceAll("rondelle", "");

			        					ingredient = ingredient.replaceAll("un peu de", "");
			        					ingredient = ingredient.replaceAll("bien mûre", "");
			        					ingredient = ingredient.replaceAll("morceau de", " ");
			        					ingredient = ingredient.replaceAll("frais", " ");
			        					ingredient = ingredient.replaceAll("marinée", " ");
			        					ingredient = ingredient.replaceAll("mariné", " ");
				        				
			        
			        					// text sequence cleaning
			        					
			        					
			        					
			        					ingredient = ingredient.replaceAll("/", " ");
			        					ingredient = ingredient.replaceAll("\\s+", " "); // multiple spaces
			        					
			        					ingredient = ingredient.replaceAll("\\s$", ""); // spaces end line 
			        					ingredient = ingredient.replaceAll("^\\s", ""); // spaces begin line
			        					
			        					ingredient = ingredient.replaceFirst("^d'", "");
			        					ingredient = ingredient.replaceFirst("^g de ", "");
			        					ingredient = ingredient.replaceFirst("^de ", "");
			        					ingredient = ingredient.replaceFirst("^du ", "");
			        					ingredient = ingredient.replaceFirst("^le ", "");
			        					ingredient = ingredient.replaceFirst("^la ", "");
			        					ingredient = ingredient.replaceFirst("^des ", "");
			        					ingredient = ingredient.replaceFirst("^à ", "");
			        					ingredient = ingredient.replaceFirst("^une ", "");
			        					ingredient = ingredient.replaceFirst("^un ", "");
			        					ingredient = ingredient.replaceFirst("^pour la ", "");
			        					ingredient = ingredient.replaceFirst("^,", "");
			        					ingredient = ingredient.replaceFirst("^\\. ", "");
			        					ingredient = ingredient.replaceFirst("^-", "");
			        					
			        					ingredient = ingredient.replaceFirst("^et d'", "");
			        					ingredient = ingredient.replaceFirst("^ d'", "");
			        					
			        					
			        					ingredient = ingredient.replaceFirst(",$", "");
			        					ingredient = ingredient.replaceFirst("\\.$", "");
			        					ingredient = ingredient.replaceFirst(" :$", "");
			        					
			        					// patches
			        					ingredient = ingredient.replaceFirst("^de ", "");
			        					ingredient = ingredient.replaceFirst("^ de ", "");
			        					ingredient = ingredient.replaceFirst("^s de ", "");
			        					ingredient = ingredient.replaceAll("\\s$", ""); // spaces end line 
			        					ingredient = ingredient.replaceAll("^\\s", ""); // spaces begin line
			        					
			        					// add
			        					if ( ! ingredient.isEmpty() && ingredient.length() > 2) {
			        							ingredients.add(ingredient); 
			        							// System.out.println("xxx:" + ingredient);	
			        							
			        							if ( ingredientrefs.containsKey(ingredient)) { ingredientrefs.put(ingredient, ingredientrefs.get(ingredient)+1); } else { ingredientrefs.put(ingredient, 1); }
			        							
			        					}
			        					
			        					content = reader.readLine();
			        				}
			        				// System.out.println(ingredients.size());
			        				
			        			}
			        			
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
			        				recette = recette.replaceFirst("\\s+", "");
			        				//
			        				// System.out.println("Recette:" + recette);
			        				recette = recette.toLowerCase();
			        				// store the recipe text
			        				c_recette.put(id,recette);
			        				
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
		    	System.out.println("Statistiques");
		        
		    	 // Initialize difficulty count
		        HashMap<String, Integer> countdiff = new HashMap<String, Integer>();
		        HashMap<String, Integer> counttype = new HashMap<String, Integer>();
			        
		    	
		        Iterator<String> itn = type.keySet().iterator();
		        while(itn.hasNext()){
		        	String key = itn.next();
		        	counttype.put(key, 0);
		        	System.out.println("Type de plat: " + key + " " + type.get(key));
		        	
		        }
		        
		        Iterator<String> itt = niveau.keySet().iterator();
		        while(itt.hasNext()){
		        	String key = itt.next();
		        	countdiff.put(key,0);
		        	System.out.println("Niveau: " + key + " " + niveau.get(key)); // string du niveau / type
		        }
		        
		    	System.out.println("<ingredients>Ingrédients collectés</ingredients>");
		        
		        
		       // Iterator<String> iti = ingredientrefs.keySet().iterator();
		    	// while(iti.hasNext()){
		    	//	String key = (String) iti.next();
		    	//	System.out.println("Ingrédient: " + key + " " + ingredientrefs.get(key));
		    	//}
		        
		        
		        ArrayList<?> as = new ArrayList<Object>( ingredientrefs.entrySet() );  
		          
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
		    	   // current word
		    	   // beurre=2998
		    	   String ingredseq = (String)i.next().toString();
		    	   String[] ingredname = ingredseq.split("=");// ingredname[0] contains ingredient name
		          
		           
		           // search presence in recipe
		           Iterator<String> itirec = c_recette.keySet().iterator();
		           // browse all

		           //  keyword relevance - if lower than x, do not take into account
		           int pertinence =0; 
		           
		           // go over all the recipes
		           while(itirec.hasNext()){
		        	   
		        	    // id number
			    		String idnumber = itirec.next();
			    		
			    		// get recipe text using recipe idnumber 
			    		String textrecette = c_recette.get(idnumber);
			    		
			    		// find current word
			    		if (textrecette.contains(ingredname[0])){
			    			
			    			pertinence++;
			    			
			    			// Increment difficulty
			    			String difficulty = c_niv.get(idnumber);
			    			countdiff.put(difficulty , countdiff.get(difficulty )+1);
			    			// Increment type
			    			String niveauforthisrecipy = c_type.get(idnumber);
			    			counttype.put( niveauforthisrecipy , counttype.get(niveauforthisrecipy)+1);
			    			// verbose
			    			// System.out.println("Recette:" + idnumber + " " + difficulty + " " + niveauforthisrecipy);
			    		}
			    		
			       }
		           
		           if (pertinence > variable.featureapparitionsminimum ){
		        	   
		        	   // print ingredient feature
		        	   System.out.print( ingredseq  + "\t\t\t"); 
		        	   
			            // display results for this keyword
			           // Difficulty
			           Iterator<String> tcross = counttype.keySet().iterator();
				       while(tcross.hasNext()){
				        	String key = tcross.next();
				        	System.out.print( key + "\t" + counttype.get(key) + "\t" ); 
				        	counttype.put(key,0); // reset
				       }
				       // type
				       System.out.print("\t\t"); 
				       
				       Iterator<String> itcross = countdiff.keySet().iterator();
				       while(itcross.hasNext()){
				        	String key = itcross.next();
				        	System.out.print( key + "\t" + countdiff.get(key) + "\t"); 
				        	countdiff.put(key,0); // reset
				        }
				       
				        System.out.println(""); 
		           }
		           pertinence = 0;
		       }  
		        
		       
		      
		       
		        
		        
		        
	}
        	
        	
}
