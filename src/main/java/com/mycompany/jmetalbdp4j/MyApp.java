package com.mycompany.jmetalbdp4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import it.uniroma1.lcl.babelnet.BabelNet;

public class MyApp {

	public static void main(String[] args) {
		Experimento exp = new Experimento();

		System.out.println("Ejecutando main de experimento");
		exp.loadSynsetCache();
		exp.cargarDataset();
		//exp.completeSynsetCache();
		//saveSynsetCache();
		exp.generateWekaDataset();
		//System.out.println("SynsetListOriginal: " + exp.synsetListOriginal);
		try {
			exp.saveClonedWekaDataset();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		exp.runClassifier();
		System.out.println("FP: " + exp.fp + " FN:" + exp.fn + " DIM:" + exp.dim);
		//exp.generateChromosome();
			//generamos un cromosoma, que es lo que nos debería de devolver JMetal
		exp.chromosome = new ArrayList<Integer>();
			for (int i = 0; i < exp.numSynsets; i++) {
				exp.chromosome.add(3);
			}
			
		//exp.chromosome = new ArrayList<Integer>();
		//exp.chromosome.add(2);exp.chromosome.add(0);exp.chromosome.add(2);exp.chromosome.add(0);exp.chromosome.add(1);
		//exp.chromosome.add(0);exp.chromosome.add(0);exp.chromosome.add(0);exp.chromosome.add(0);exp.chromosome.add(2);
		System.out.println("Vector: " + exp.chromosome.toString());
		System.out.println("Tamaño cloned dataset antes de reduccion: " + exp.clonedDataset.numAttributes());
		exp.escalateSynsets();
		exp.changesToReduceDataset();
		System.out.println("Tamaño cloned dataset después de reduccion: " + exp.clonedDataset.numAttributes());
		exp.generateWekaDataset();
		exp.runClassifier();
		List<String> listado = exp.clonedDataset.getAttributes();
		
		BabelNet bn = BabelNet.getInstance();
	
//		for (String s: listado) {
//			try {
//			BabelSynset by = bn.getSynset(new BabelSynsetID(s));
//			System.out.println(s +" = " + by.getMainSense());
//			} catch (Exception e) {
//				System.out.println("Error en synset s " + e);
//			}
//		}


		//System.out.println("Mapa de cambios: " + changesInSynsetToReduce.toString());
		//System.out.println("Final");
		System.out.println("FP: " + exp.fp + " FN:" + exp.fn + " DIM:" + exp.dim);
		//generateWekaDataset();
		//runClassifier();
		//System.out.println("FP: " + fp + " FN:" + fn + " DIM:" + dim);
		

	}

}
