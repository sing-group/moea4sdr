package com.mycompany.jmetalbdp4j;
import java.util.ArrayList;
import java.util.List;


import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Add;
public class PlayWekaDataset {

	public static void main(String[] args) throws Exception {
		Instances dataset = null; 
		
		try {
		 DataSource source = new DataSource("correo.arff");
		 dataset = source.getDataSet();
		 if (dataset.classIndex() == -1)
		   dataset.setClassIndex(dataset.numAttributes() - 1);

		} catch (Exception ex) {
			System.out.println("El archivo no existe. " + ex);
		}
		
		System.out.println("Numero de atributos: " + dataset.numAttributes());

		System.out.println(dataset.toString());
		
		//Esto no sirve, añade una fila con datos al final. Una mierda.
//	    double[] instanceValue1 = new double[dataset.numAttributes()];
//	    instanceValue1[0] = 244;
//	    instanceValue1[1] = 59;
//	    instanceValue1[2] = 2;
//	    instanceValue1[3] = 880606923;

	/*	Add filter;
        filter = new Add();
        filter.setAttributeIndex("first");
        filter.setAttributeName("NuevoAtributo");
        //filter.setAttributeType();
        filter.setInputFormat(dataset);
        dataset = Filter.useFilter(dataset, filter);*/
        
        dataset.insertAttributeAt(new Attribute("NewNumeric"), dataset.numAttributes());
        double[] valores1;      
        double[] valores2;
        double[] valores3;
        double[] numeros = new double[5];
		//las columnas empiezan de 0
		dataset.instance(0).setValue(9, 9);
		valores1 = dataset.instance(0).toDoubleArray();
		valores2 = dataset.instance(1).toDoubleArray();
		valores3 = new double[dataset.numAttributes()];
		System.out.println("El array tiene " + valores1.length + " elementos");
		for (int i=0; i<valores1.length; i++) {
			valores3[i] = valores1[i] + valores2[i];
			System.out.print((int)valores3[i] + ",");
		}
		System.out.println();
		System.out.println("Numero de atributos: " + dataset.numAttributes());
		System.out.println(dataset.toString());
		
		
		
	}
	
}
