package variables;

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


/**
 * 
 * Please configure those variables to generate test and train arff model 
 * and to make inline experiments
 * 
 * 
 */
public class vars {

	/** please define a deft13 folder with all the files in your
	 home folder and then point on it through this complete uri */
	public static String myPath = "/home/myname/recherche/";
	
	
	// you do not have to modify paths below if you keep the structure of the zip file
	
	/** ARFF output files path */
	public String modelOutputPath = myPath + "deft13/arff/";
	
	
	/** train and dev corpus (cross validation made through Weka)
	    Please consider that those corpora are copyrighted. To regenerate the ARFF Files from them, 
	    you need a license on the evaluation corpus from DEFT organization
	    we obtained those XML under Linux by cat *.xml > Tx.xml */
	public String pathToCorpus = myPath + "deft13/corpus/corpus.xml";
	
	
	/** Test corpus input path.
	    Those corpus are copyrighted, we cannot provide them. To regenerate the ARFF Files from them, 
	    you need a license on the evaluation corpus from DEFT organization
	    we obtained those XML under Linux by cat *.xml > Tx.xml */
	public String xmlT1 = myPath + "deft13/corpus_test/T1.xml";
	/** Corpus for task 2 */
	public String xmlT2 = myPath + "deft13/corpus_test/T2.xml";
	

	//----------------------------------------
    // This is to configure the model according
	// to the model number
	//----------------------------------------
	
	
	/** variable set to define model to generate <br>
	   model = 1 type of recipe / 2  difficulty of recipe<br>
	   ! Caution ! Those numbers are not compliant with the task numbers (inverted 1=2 and 2=1 sorry) */
	public int modelType = 2;
	
	/** feature selection */
	public int featureapparitionsminimum = 10; // minimal number of occurrences for taking the feature into account
	
	/** Ingredients features file path : see paper */
	public String lexicalfeatures = myPath + "deft13/corpus/features_ACP.txt";
	
	/** Weighted lexical features file path : see paper */
	public String weightedlexicalfeats = myPath + "deft13/corpus/distrib_features_from_vocab_bestfirst_greedystepwise_moydiff.txt";
	
	/** verb features : see paper */
	public String pathtoweightedverbfeats = myPath + "deft13/corpus/features_verbs.txt";

	/** ngrams feature file : see paper */
	public String pathToNgramsFeatures = myPath + "deft13/corpus/features_ngrams_bestfirst_greedystepwise.txt";
	
	//----------------------------------------
	// Following values are default for
	// task 1 / type of meal
	// If variable modelType = 2 / difficulty
	// the constructor updates some of the variables
	//----------------------------------------
	
	
	/** Reduction of parameters - quantity (top) or -1 (all in file) */
	public int parmsreduce = -1;
	
	/** ngrams size */
	public int ngramsSize = 2;
	
	// From model type -> 1
	// selected features
	public boolean uselexfeat = true;
	public boolean useweightedlexfeat = false;
	public boolean usengramfeat = true;
	public boolean useverbfeat = true;
	public boolean usecost = true;
	// internal features
	public boolean sizeoftext = true; // amount of words in text and title
	public boolean amountofingreds = true; // amount of ingredients according to list
	
	
	/** this is activated as constructor
	 *  and will eventually fix vars on model 2 
	 *  according to modeltype (else default values are set)
	 */
	public vars(){
		
		// set values according to generation mode
		if ( this.modelType == 2 ) {
			
			// model type = 2 difficulty
			//pathToNgramsFeatures = myPath + "deft13/corpus/features_ngrams_bestfirst_greedystepwise.txt";
			
			/* model difficulty */
			// selected features
			uselexfeat = false;
			useweightedlexfeat = true;
			usengramfeat = true;
			useverbfeat = true;
			usecost = true;
			// internal features
			sizeoftext = true; // amount of words in text and title
			amountofingreds = true; // amount of ingredients according to list
		}
		
	}
}
