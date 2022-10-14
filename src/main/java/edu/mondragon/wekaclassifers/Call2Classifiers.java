package edu.mondragon.wekaclassifers;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.OptionHandler;
import weka.core.Utils;
import weka.filters.Filter;

import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.mondragon.jmetalbdp4j.TestWekaFile;

/**
 * A class to call Weka classifiers to obtain measurements
 * 
 */

public class Call2Classifiers {
  /** the classifier used internally */
  protected Classifier m_Classifier = null;
 
  /** the training instances */
  protected Instances dataset = null;
  
  /** for evaluating the classifier */
  protected Evaluation m_Evaluation = null;


  /**
   * initializes the class
   */
  public Call2Classifiers() {
    super();
  }

  /**
   * sets the classifier to use
   * @param name        the classname of the classifier
   * @param options     the options for the classifier
   */
  public void setClassifier(String name, String[] options) throws Exception {
	  m_Classifier = AbstractClassifier.forName(name, options);
  }
  
  /**
   * Makes a copy of the dataset and sets the Instances dataset to use
   * @param dataset the dataset
   */
  public void setDataset(Instances dataset) {
	  this.dataset = new Instances(dataset);
  }
  
  /**
   * runs 10fold CV
   */
//  public void execute() throws Exception {
//	  m_Classifier.buildClassifier(dataset);
//      m_Evaluation = new Evaluation(dataset);
//      m_Evaluation.crossValidateModel(m_Classifier, dataset, 10, new Random(1));
//      
//  }
  
  public void execute() throws Exception {
	  try {
		  m_Classifier.buildClassifier(dataset);
		  m_Evaluation = new Evaluation(dataset);
		  //Chequear si hay más elementos que carpetas hay que crear. Si es más pequeño falla.
		  if (dataset.size() >11) {
			  m_Evaluation.crossValidateModel(m_Classifier, dataset, 10, new Random(1));
		  }
	  } catch (Exception ex) {
		  System.out.println("Error en execute " + ex);
		  ex.printStackTrace();
	  }

  }
  
  public Map<String, Integer> getExecutionValues () {
	  Map<String, Integer> executeValues = new HashMap<String, Integer>();
	  executeValues.put("fp", (int)m_Evaluation.confusionMatrix()[0][1]);
	  executeValues.put("fn", (int)m_Evaluation.confusionMatrix()[1][0]);
	  executeValues.put("dim", dataset.numAttributes());
	  return executeValues;
	  
  }


  /**
   * outputs some data about the classifier
   */
  public String toString() {
    StringBuffer        result=null;
    try {
    	result = new StringBuffer();
    	result.append("Classifier results\n===========\n\n");
    	result.append("Nº de mensajes = " + dataset.numInstances() + "\n");
    	result.append("Atributos (dimensión) = " + dataset.numAttributes() + "\n");
    	result.append("FP: " + m_Evaluation.confusionMatrix()[0][1] + "\n");
    	result.append("Classifier...: " 
    			+ m_Classifier.getClass().getName() + "\n "); 
    	//+ Utils.joinOptions(m_Classifier.getOptions()) + "\n");
    	result.append("Summary: " + m_Evaluation.toSummaryString());
    	result.append("------------------------------------------\n");
    	result.append(m_Evaluation.toMatrixString());

    	
    	try {
    		//result.append(m_Evaluation.toClassDetailsString() + "\n");
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	try {
    		//result.append(m_Evaluation.toClassDetailsString() + "\n");
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
  	} catch (Exception ex) {
      Logger.getLogger(TestWekaFile.class.getName()).log(Level.SEVERE, null, ex);
      ex.printStackTrace();
  }
    	return result.toString();
    }
 }

   



