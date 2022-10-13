package train75;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.Prediction;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.rules.PART;
import weka.classifiers.trees.DecisionStump;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

public class TrainMain {

		static public void main(String[] args) {
	        try {
	            /* Cargamos el fichero con los datos */
	            BufferedReader trainFile = new BufferedReader(new FileReader("car_train.arff"));
	            Instances trainData = new Instances(trainFile);
	            BufferedReader testFile = new BufferedReader(new FileReader("car_test.arff"));
	            Instances testData = new Instances(testFile);
	             
	            /* Seleccionamos la columna de los datos a estimar */
	            trainData.setClassIndex(trainData.numAttributes() - 1);
	            testData.setClassIndex(testData.numAttributes() - 1);
	 
	            /* Creamos dos grupos uno de entreno y otro de test */
	            int numFolds=10;
	            Instances[] trainingSplits=new Instances[numFolds];
	            Instances[] testingSplits= new Instances[numFolds];
	              
	            for (int i = 0; i < numFolds; i++) {
	                trainingSplits[i] = trainData.trainCV(numFolds, i);
	                testingSplits[i] = testData.testCV(numFolds, i);
	                }
	      
	            /* Ejecutamos cada diferentes modelos */
	            ejecutarModelo( new J48(), trainingSplits, testingSplits); // a decision tree
	            ejecutarModelo( new PART(), trainingSplits, testingSplits);
	            ejecutarModelo( new DecisionTable(), trainingSplits, testingSplits);//decision table majority classifier
	            ejecutarModelo( new DecisionStump(), trainingSplits, testingSplits); //one-level decision tree
	        } catch (Exception ex) { ex.printStackTrace(); }
	        }
	     
	    static public void ejecutarModelo(Classifier model, Instances[] trainingSplits, Instances[] testingSplits) throws Exception {
	        ArrayList<Prediction> predictions = new ArrayList<Prediction>();
	 
	        /* Entrenamos el modelo */
	        for (int i = 0; i < trainingSplits.length; i++) {
	            model.buildClassifier(trainingSplits[i]);
	            }
	         
	        /* Testenamos el modelo */
	        for (int i = 0; i < testingSplits.length; i++) {
	            Evaluation evaluation = new Evaluation(testingSplits[i]);
	            evaluation.evaluateModel(model, testingSplits[i]);
	            predictions.addAll( evaluation.predictions() );
	            }
	         
	        /* Calculamos la exactitud del modelo */
	        double accuracy = calculateAccuracy(predictions);
	        System.out.println("Exactitud del modelo " + model.getClass().getSimpleName() + ": " + String.format("%.2f%%", accuracy));
	        }
	     
	    public static double calculateAccuracy(ArrayList<Prediction> predictions) {
	        double correct = 0;
	  
	        for (Prediction np:predictions) {
	            if (np.predicted() == np.actual())
	                correct++;
	            }
	  
	        return 100 * correct / predictions.size();
	        }  
}
    
