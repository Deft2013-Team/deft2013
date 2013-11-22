package classifier;

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
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

import variables.vars;
import weka.classifiers.*;
import weka.core.Instances;
import weka.core.converters.ArffLoader;


/**
 * 
 * Generate test results according to a trained model. 
 * 
 * Important : you will need to configure your eclipse
 * or java editor with a classpath to specific libs if
 * you use external classifiers like LibSVM
 * 
 * @author eric
 *
 */
public class test implements Serializable {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		vars variables = new vars();
		
		// path for model T1 / internal task 2 - difficulty
		// String pathToModel = variables.myPath + "deft13/models/t1-N-bayes.model";
		String pathToModel = variables.myPath + "deft13/models/t1-LMT.model";
		String pathToModelArff = variables.myPath + "deft13/arff/model_difficulty.arff";
		String pathToModelTestArff = variables.myPath + "deft13/arff/test_T1.arff";
		String outputRes = "tache1-diff-2.csv";
		
		// path du modele T2 / interne tâche 1 - type
		if (variables.modelType == 1){
			pathToModel = variables.myPath + "deft13/models/t2-SVM.model";
			pathToModelArff = variables.myPath + "deft13/arff/modele_type.arff";
			pathToModelTestArff = variables.myPath + "deft13/arff/test_T2.arff";
			outputRes = "tache2-type.csv";
		}
		
		// Create result file 
		FileWriter Resfstream = null;
		BufferedWriter ResOut = null; 
		try {
			Resfstream = new FileWriter(outputRes);
			ResOut = new BufferedWriter(Resfstream );
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		// load train data (for verification purpose)
		ArffLoader tloader = new ArffLoader();
		Instances train = null;
		
		// load test data
		ArffLoader loader = new ArffLoader();
		Instances test = null;
		
		//Classifier cls = new LibSVM();
		
		try {
			
			// de-serialize classifier (en gros ça veut dire qu'on le charge :-)
			Classifier cls = (Classifier) weka.core.SerializationHelper.read(pathToModel);
			// System.out.println("Reloading classifier : " + cls.toString() + "Classifier loaded");
			// cls = (Classifier) weka.core.SerializationHelper.read(pathToModel);
			System.out.println("Reloading classifier : " + cls.toString() + "Classifier loaded");
			
			 /*
				Load arff to test
			 */
			 BufferedReader reader = new BufferedReader(
             new FileReader(pathToModelTestArff));
			 test = new Instances(reader);
			 reader.close();
			 System.out.println("Arff loaded " + test.size() + " " + test.numInstances());
			 // Make the last attribute be the class 
			 test.setClassIndex(test.numAttributes() - 1); 
			 
			
			
			
			 
			 // Control classifier and print some statistics
			 /*
				Load arff used to train
			 */
			 BufferedReader reader2 = new BufferedReader(new FileReader(pathToModelTestArff));
			 train = new Instances(reader2);
			 reader.close();
			 System.out.println("Arff train loaded " + test.size() + " " + test.numInstances());
			// Make the last attribute be the class
			 train.setClassIndex(train.numAttributes() - 1); 
			 // evaluate
			 Evaluation eval = new Evaluation(train);
			 eval.evaluateModel(cls, test);
			 System.out.println(eval.toSummaryString("\nResults\n======\n", false));
			 
			 
			 // -------------------------------
			 // evaluation per instances		 
			 //--------------------------------
			 for (int i = 0; i < test.numInstances(); i++) {
				  int pred = (int)cls.classifyInstance(test.instance(i));
				  System.out.print(i + " ID: " + test.instance(i).value(0));
				  System.out.print(", actual: " + test.classAttribute().value((int) test.instance(i).classValue()));
				  System.out.println(", predicted: " + test.classAttribute().value(pred));
			 }
			 
			 
			 System.out.println("---------------------------");
			 
			// -------------------------------
			// evaluation per instances from ARFF		 
			//--------------------------------
			 BufferedReader reader3 = new BufferedReader(new FileReader(pathToModelTestArff));
			 String text = ""; int z=0; 
			 String headarff = "";
			 String arff = "";
			 
			 // browse data
			 while (( text = reader3.readLine()) != null) {
				 
				 headarff = headarff + text + "\n"; 
				 if (text.contains("@DATA")) break;
			 }
			 // handle each line
			 while (( text = reader3.readLine()) != null) {
				 
				 // read data
				 if (text.startsWith("%")){
					 
					 // extract data
					 String seqcontent = reader3.readLine(); // datas
					 
					 // extract number
					 String numref = text.replace("%", "");
					 
					 // output ARFF
					 arff = headarff + "\n" + seqcontent + "\n";
					// Create test ARFF file on disk
					 try{
							  // Create file 
							  FileWriter fstream = new FileWriter("test.arff");
							  BufferedWriter out = new BufferedWriter(fstream);
							  out.write(arff);
							  //Close the output stream
							  out.close();
							  
					 }catch (Exception e){//Catch exception if any
							System.err.print("[TrainModel] Test Error:" + e.getMessage());
					 }
					 ArffLoader loadertest = new ArffLoader();
					 Instances featuretest = null;
					 // load and test
					 loadertest.setFile(new File("test.arff"));
					 featuretest = loadertest.getStructure();
					 featuretest = loadertest.getDataSet();
					 featuretest.setClassIndex(test.numAttributes() - 1);
					 
					 // System.out.println(z+ " Seq:" + text + ":" + seqcontent + );
					 int prediction = (int)cls.classifyInstance(featuretest.instance(0));
					 System.out.println(z+ " Seq:" + numref + " predicted:" + featuretest.classAttribute().value(prediction));
					 
					 // output on result file
					 ResOut.write(numref + "\t" + featuretest.classAttribute().value(prediction) + "\n");
					 
				 }
				 z++;
			 }
			
			 reader.close();
			 reader2.close();
			 reader3.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			ResOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
	
	}

}
