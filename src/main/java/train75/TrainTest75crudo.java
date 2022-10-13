package train75;

import java.io.BufferedReader;
import java.io.FileReader;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;

public class TrainTest75crudo {

		static public void main(String[] args) throws Exception{
	      
			String trainFilePath = "youtube75.weka.gui.stwv.ngram.infogain.idf.arff";
			String testFilePath = "youtube25.arff";

			Instances trainingData = new Instances(new BufferedReader(new FileReader(trainFilePath)));
			Instances testingData  = new Instances(new BufferedReader(new FileReader(testFilePath)));
			//Instances trainInstances = loader.getStructure();
			trainingData.setClassIndex(trainingData.numAttributes() - 1);
			testingData.setClassIndex(testingData.numAttributes() - 1);
			
			System.out.println("num train data: " + trainingData.numInstances());
			System.out.println("num test data : " + testingData.numInstances());
			
	        try {
	            System.out.println("------------------------------------------");
	            System.out.println("--------- Naive Bayes Classifier ---------");
	            System.out.println("------------------------------------------");
	            Evaluation nbEvaluation = new Evaluation(testingData);
	            NaiveBayes naiveBayes = new NaiveBayes();
	            naiveBayes.buildClassifier(trainingData);
	            nbEvaluation.evaluateModel(naiveBayes, testingData);
	            String confusionMatrix = nbEvaluation.toMatrixString("Confusion matrix: ");
	            System.out.println("Summary: " + nbEvaluation.toSummaryString());
	            System.out.println("------------------------------------------");
	            System.out.println(confusionMatrix);
	            System.out.println(">> TN: " + nbEvaluation.confusionMatrix()[0][0]);
	            System.out.println(">> FP: " + nbEvaluation.confusionMatrix()[0][1]);
	            System.out.println(">> FN: " + nbEvaluation.confusionMatrix()[1][0]);
	            System.out.println(">> TP: " + nbEvaluation.confusionMatrix()[1][1]);
	        } catch (Exception ex) {
	            System.out.println("Error " + ex);
	            ex.printStackTrace();
	        }
			
			//Para guardar el modelo entrenado
	        //SerializationHelper.write(new FileOutputStream("tmp"), j48);
	        //J48 j48Read = (J48)SerializationHelper.read(new FileInputStream("tmp"));
			
			
}
		
		
}
    
