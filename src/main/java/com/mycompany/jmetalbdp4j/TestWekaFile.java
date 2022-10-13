package com.mycompany.jmetalbdp4j;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bdp4j.dataset.CSVDatasetReader;
import org.bdp4j.transformers.Date2MillisTransformer;
import org.bdp4j.transformers.Enum2IntTransformer;
import org.bdp4j.types.Dataset;
import org.bdp4j.types.Transformer;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class TestWekaFile {

	public static void main (String[] args) throws Exception {
		List<String> synsetList = new ArrayList<String>();
		
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
        

      // try {
       
        CSVDatasetReader jml = new CSVDatasetReader(filePath, transformersList);
        Dataset dataset = jml.loadFile();

       
        //} catch(IOException e) {
       // 	System.out.println("Fichero no encontrado. Error=" + e.getMessage());
       // }
        
        //imprimimos el dataset.
        //System.out.println("-----------Contenido dataset-----");
        //dataset.printLine();
        //System.out.println("-------fin Contenido dataset-----");
        
        //Generamos el ARFF 
        dataset.generateARFFWithComments(transformersList, "");
        
        //Sacamos la lista de synsets y la imprimimos
       // synsetList = dataset.getSynsets();
       // System.out.println("-------Lista de synsets-------");
       // System.out.println(synsetList);
       // System.out.println("---fin Lista de synsets-------");
        
        //Llamamos a la funcion gethypernyns. Ojo que falla con el fichero grande
        //Map<String, String> hypernymMap = new HashMap<String, String>();
        //hypernymMap=BabelUtils.getDefault().getHypernymsFromBabelnet(synsetList);
        
        //Imprimimos mapa de hiperónimos
        //System.out.println("-------Mapa de hyperonimos-------");
      /*  System.out.println(hypernymMap);
        System.out.println("---fin Mapa de hyperonimos-------");*/
        
        //Creamos el dataset de Weka
        Instances wekaDataset = dataset.getWekaDataset(); 
        
        
		//El índice comienza en cero. La última columna es la clase.
        wekaDataset.setClassIndex(wekaDataset.numAttributes() -1);
        
        
        //System.out.println(wekaDataset.toString());
        /*System.out.println(wekaDataset.toString());
        System.out.println("Sin atributos String");*/
        //wekaDataset.deleteStringAttributes();
        /*System.out.println("----Sin String----");
        System.out.println(wekaDataset.toString());*/
        
        //Tal cual esta ahora se puede generar un ARFF sin esos datos?
        //O hay que crear un csv?
        //dataset.generateCSV();
        

        
        //System.out.println("FIN");
        //System.out.println(wekaDataset.toString());
        
        //Classifier j48 = new J48();
        //j48.buildClassifier(wekaDataset);
        
        //Quitamos los datos de longitud del mensaje, etc...y dejamos solo los synsets.
        Remove remove = new Remove();
        remove.setOptions(new String[] {"-R", "1-7"});
        remove.setInputFormat(wekaDataset);
        Instances filteredOriginalDataset = Filter.useFilter(wekaDataset, remove);
    
        System.out.println("Instances: " + filteredOriginalDataset.numInstances());
        System.out.println("Attributes: " + filteredOriginalDataset.numAttributes());
        
        /*int numInstances = filteredOriginalDataset.numInstances();
        int beginInt = (numInstances * 80) / 100;
        int endInt = numInstances - beginInt;*/
        

        
        //Creacion manual de train y test, que de momento no voy a usar.
       /* Instances trainingData = new Instances(filteredOriginalDataset, 0, beginInt);
        System.out.println("num trainingData: " + trainingData.numInstances());
        Instances testingData = new Instances(filteredOriginalDataset, beginInt, endInt);
        System.out.println("num testingData: " + testingData.numInstances());
        System.out.println("");*/
        


		ArffSaver saver = new ArffSaver();
		saver.setInstances(wekaDataset);
		saver.setFile(new File("originalDataset.arff"));
		saver.writeBatch();

		saver.setInstances(filteredOriginalDataset);
		saver.setFile(new File("filteredOriginalDataset.arff"));
		saver.writeBatch();
		
		System.out.println(filteredOriginalDataset.get(1).toString());
		//Integer[] = new(filteredOriginalDataset.get(1).toDoubleArray());
		System.out.println(filteredOriginalDataset.get(2).toString());
		
		Thread.sleep(5000);
        try {
            System.out.println("------------------------------------------");
            System.out.println("--------- Bayes net-----------------------");
            System.out.println("------------------------------------------");
            BayesNet bayesNet = new BayesNet();
            bayesNet.setOptions(weka.core.Utils.splitOptions("-D -Q weka.classifiers.bayes.net.search.local.K2 -- -P 1 -S BAYES -E weka.classifiers.bayes.net.estimate.SimpleEstimator -- -A 0.5"));
            bayesNet.buildClassifier(filteredOriginalDataset);
            Evaluation bnEvaluation = new Evaluation(filteredOriginalDataset);
            bnEvaluation.crossValidateModel(bayesNet, filteredOriginalDataset, 10, new Random(1));
            String confusionMatrix = bnEvaluation.toMatrixString("Confusion matrix: ");
            System.out.println("Summary: " + bnEvaluation.toSummaryString());
            System.out.println("------------------------------------------");
            System.out.println(confusionMatrix);
            System.out.println(">> TN: " + bnEvaluation.confusionMatrix()[0][0]);
            System.out.println(">> FP: " + bnEvaluation.confusionMatrix()[0][1]);
            System.out.println(">> FN: " + bnEvaluation.confusionMatrix()[1][0]);
            System.out.println(">> TP: " + bnEvaluation.confusionMatrix()[1][1]);
            System.out.println(bnEvaluation.toClassDetailsString());
            
            //guardando el modelo en el disco
            // serialize model
            /*ObjectOutputStream oos = new ObjectOutputStream(
                                       new FileOutputStream("bayes.model"));
            oos.writeObject(bayesNet);
            oos.flush();
            oos.close();*/
        } catch (Exception ex) {
            Logger.getLogger(TestWekaFile.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        		 
        		 
        
        try {
            System.out.println("------------------------------------------");
            System.out.println("--------- Random Forest Classifier -------");
            System.out.println("------------------------------------------");
            RandomForest randomForest = new RandomForest();
            randomForest.setOptions(weka.core.Utils.splitOptions("-P 100 -I 100 -num-slots 1 -K 0 -M 1.0 -V 0.001 -S 1"));
            randomForest.buildClassifier(filteredOriginalDataset);
            Evaluation rfEvaluation = new Evaluation(filteredOriginalDataset);
            rfEvaluation.crossValidateModel(randomForest, filteredOriginalDataset, 10, new Random(1));
            String confusionMatrix = rfEvaluation.toMatrixString("Confusion matrix: ");
            System.out.println("Summary: " + rfEvaluation.toSummaryString());
            System.out.println("------------------------------------------");
            System.out.println(confusionMatrix);
            System.out.println(">> TN: " + rfEvaluation.confusionMatrix()[0][0]);
            System.out.println(">> FP: " + rfEvaluation.confusionMatrix()[0][1]);
            System.out.println(">> FN: " + rfEvaluation.confusionMatrix()[1][0]);
            System.out.println(">> TP: " + rfEvaluation.confusionMatrix()[1][1]);
            System.out.println(rfEvaluation.toClassDetailsString());
        } catch (Exception ex) {
            Logger.getLogger(TestWekaFile.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        
        try {
            System.out.println("------------------------------------------");
            System.out.println("--------- Random Forest Classifier -------");
            System.out.println("------------------------------------------");
            String name = "RandomForest";
            String[] parametros = new String[1];
            parametros[0]= "";
            Classifier m_Classifier = null;
      	    m_Classifier = AbstractClassifier.forName(name, parametros);

            m_Classifier.buildClassifier(filteredOriginalDataset);
            Evaluation rfEvaluation2 = new Evaluation(filteredOriginalDataset);
            rfEvaluation2.crossValidateModel(m_Classifier, filteredOriginalDataset, 10, new Random(1));
            String confusionMatrix = rfEvaluation2.toMatrixString("Confusion matrix: ");
            System.out.println("Summary: " + rfEvaluation2.toSummaryString());
            System.out.println("------------------------------------------");
            System.out.println(confusionMatrix);
            System.out.println(">> TN: " + rfEvaluation2.confusionMatrix()[0][0]);
            System.out.println(">> FP: " + rfEvaluation2.confusionMatrix()[0][1]);
            System.out.println(">> FN: " + rfEvaluation2.confusionMatrix()[1][0]);
            System.out.println(">> TP: " + rfEvaluation2.confusionMatrix()[1][1]);
            System.out.println(rfEvaluation2.toClassDetailsString());
        } catch (Exception ex) {
            Logger.getLogger(TestWekaFile.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        
     
        
        
		
		
		
	}


}
