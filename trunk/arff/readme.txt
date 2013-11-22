**********************************************************
*
* DEFT2013 evaluation campaign
* http://deft.limsi.fr/2013/index.php?id=1&lang=en 
* Wikimeta Lab System
*
* Paper is on ResearchGate at:
* http://goo.gl/vSVBU
* 
* For information contact
* eric.charton@polymtl.ca
* twitter/ericcharton
*
* This software is free to use, modify and redistribute under 
* Creative Commons by-nc/3.0 License Term
* http://creativecommons.org/licenses/by-nc-sa/3.0/
*
* This license means, do anything with it but make money.
* (if you make money, share it with us :-)
*
* (c) Dr Eric Charton & Dr Marie-Jean Meurs
*
**********************************************************

----------------------------------------------------------
Train and test ARFF files used for DEFT 2013 Evaluation
campaign.

http://deft.limsi.fr/2013/index.php?id=1&lang=en

This include the training and test 
models. 

Java software used to generate those 
models will be released on 
http://www.wikimeta.org/deft2013

Paper is on ResearchGate at:
http://tinyurl.com/c7ybj67

For information contact
eric.charton@polymtl.ca
twitter/ericcharton

----------------------------------------------------------

The model Difficulty have to be evaluated with test_t1.arff
The model Type have to be evaluated with test_t2.arff

Those ARFF files are ready to use with Weka 3-7-9 :
http://www.cs.waikato.ac.nz/ml/weka/downloading.html

- Vars configuration :

Global :

ngramsSize = 2;

* on model difficulty

	/* model difficulty */
	uselexfeat = false;
	useweightedlexfeat = true;
	usengramfeat = true;
	useverbfeat = true;
	usecost = true;

	// internal features
	sizeoftext = true; // amount of words in text and title
	amountofingreds = true; // amount of ingredients according to list


* on model type of recipes

	// model type -> 1
	// feature selection
	public boolean uselexfeat = true;
	public boolean useweightedlexfeat = false;
	public boolean usengramfeat = true;
	public boolean useverbfeat = true;
	public boolean usecost = true;

	// internal features
	public boolean sizeoftext = true; // amount of words in text and title
	public boolean amountofingreds = true; // amount of ingredients according to list
