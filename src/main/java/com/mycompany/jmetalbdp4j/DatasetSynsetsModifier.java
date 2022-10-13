package com.mycompany.jmetalbdp4j;

import java.util.ArrayList;

import weka.core.Instance;
import weka.core.Instances;

/**
 * A class to Modify the dataset to generalize or filter
 * 
 */
public class DatasetSynsetsModifier {
	/** the weka dataset used internally */
	protected Instances internalDataset = null;
	protected Instances newDataset = null;
	
	/** the chromosome of genetic algorithm used internally */
	protected ArrayList<Integer> howToDo;

	/**
	 * initializes the class
	 */
	public DatasetSynsetsModifier() {
		super();
	}
	
   /**
    * copy the dataset to internal dataset
    * @param dataset        the dataset to copy in the internal dataset
    */
	public void setinternalDataset (Instances dataset) {
		internalDataset = new Instances (dataset);
	}
	
	/**
	 * copy the JMetal  to internal dataset
	 * @param jmetalArray	the jmetalArray chromosome
	 */
	
	public void sethowToDo (ArrayList<Integer> jmetalArray) {
		howToDo = new ArrayList<Integer>(jmetalArray);
	}
	
	public Instances execute() {
		
		return newDataset;
	}
	

	
	
	
	
	
	
}