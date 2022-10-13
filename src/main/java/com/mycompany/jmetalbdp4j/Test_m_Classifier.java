package com.mycompany.jmetalbdp4j;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bdp4j.dataset.CSVDatasetReader;
import org.bdp4j.transformers.Date2MillisTransformer;
import org.bdp4j.transformers.Enum2IntTransformer;
import org.bdp4j.types.Dataset;
import org.bdp4j.types.Transformer;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import wekaClassifers.Call2Classifiers;

public class Test_m_Classifier {

	public static void main (String[] args) throws Exception {
		
		//Creamos el mapa para la lista de transformadores para sustituir ham por 0, spam por 1
		Map<String, Integer> transformList = new HashMap<>();
		transformList.put("ham", 0);
		transformList.put("spam", 1);
		
		//Creamos el mapa para aplicar los transformadores
		Map<String, Transformer> transformersList = new HashMap<>();
		transformersList.put("date", new Date2MillisTransformer());
		transformersList.put("target", new Enum2IntTransformer(transformList));
		
		//Cargamos el fichero csv.
        String filePath = "outputsyns.csv";
        

      // Aplicamos los transformadores
       
        CSVDatasetReader jml = new CSVDatasetReader(filePath, transformersList);
        Dataset dataset = jml.loadFile();

        System.out.println(dataset.getAttributes().toString());
        
        //Generamos el ARFF en el disco
        dataset.generateARFFWithComments(transformersList, "Fichero.arff");
        
       

        
        //Creamos el dataset de Weka
        Instances wekaDataset = dataset.getWekaDataset(); 
        
        
		//El índice comienza en cero. La última columna es la clase.
        wekaDataset.setClassIndex(wekaDataset.numAttributes() -1);
        
       
       // nuevodataset=dataset.clone();
        
        //Quitamos los datos de longitud del mensaje, ubicación, etc...y dejamos solo los synsets.
        Remove remove = new Remove();
        remove.setOptions(new String[] {"-R", "1-7"});
        remove.setInputFormat(wekaDataset);
        Instances filteredOriginalDataset = Filter.useFilter(wekaDataset, remove);
    
        System.out.println("--------MAIN------------");
        System.out.println("Instances: " + filteredOriginalDataset.numInstances());
        System.out.println("Attributes: " + filteredOriginalDataset.numAttributes());
        System.out.println("--------MAIN------------");
        

        //Guardamos como arff el dataset original y el modificado para ver que esta correcto
		ArffSaver saver = new ArffSaver();
		saver.setInstances(wekaDataset);
		saver.setFile(new File("originalDataset.arff"));
		saver.writeBatch();

		saver.setInstances(filteredOriginalDataset);
		saver.setFile(new File("filteredOriginalDataset.arff"));
		saver.writeBatch();
		
		//Creamos un clasificador abstracto al que luego le daremos nombre.
       // Classifier clasificador = null;
        
        //Array de los clasificadores para usarlos
		ArrayList<String> classificatorList = new ArrayList<String>();
		//classificatorList.add("weka.classifiers.bayes.BayesNet");
		//classificatorList.add("weka.classifiers.bayes.NaiveBayes");
		classificatorList.add("weka.classifiers.bayes.NaiveBayesMultinomial");
		//classificatorList.add("weka.classifiers.bayes.NaiveBayesMultinomialText");
		//classificatorList.add("weka.classifiers.bayes.NaiveBayesMultinomialUpdateable");
		//classificatorList.add("weka.classifiers.bayes.NaiveBayesUpdateable");
		//classificatorList.add("weka.classifiers.trees.J48");
		classificatorList.add("weka.classifiers.trees.RandomForest");
		
		
		//Ejecutamos la lista de los clasificadores y sacamos los datos que nos interesan
		for (String c : classificatorList)
		{
			try {
				System.out.println("------------------------------------------");
				System.out.println("-" + c + "-");
				System.out.println("------------------------------------------");
				Call2Classifiers c1 = new Call2Classifiers();
				String name= c;
				String[] options = new String[1];
				options[0] = "";
				c1.setClassifier(name, options);
				
				//Les pasamos a los clasificadores el dataset original
				//El clasificador saca una copia, no toca para nada este dataset.
				c1.setDataset(filteredOriginalDataset);
				c1.execute();

				System.out.println(c1.toString());

			} catch (Exception ex) {
				Logger.getLogger(TestWekaFile.class.getName()).log(Level.SEVERE, null, ex);
				ex.printStackTrace();
			}
        }
		
/*		//imprimimos la lista de synsets
		List<String> synsetList = new ArrayList<String>();
		synsetList = dataset.getSynsets();
	    System.out.println("-------Lista de synsets-------");
	    System.out.println(synsetList);
	    System.out.println("---fin Lista de synsets-------");
	    
	    //escalamos el dataset e imprimimos los synsets
	    Map<String, String> synsetHypernymsTable = new HashMap<String, String>();
	    synsetHypernymsTable = BabelUtils.getDefault().getHypernymsFromBabelnet(synsetList);
	    List<String> hypernyms = new ArrayList<String>();
	    System.out.println(synsetHypernymsTable.toString());
	    for ( String syn : synsetList)
	    {
	    	System.out.print(syn + "--");
	    	System.out.print(synsetHypernymsTable.get(syn));
	    	System.out.print( "     ");
	    }*/

	    
		
        
        
	}
}