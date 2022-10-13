/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.jmetalbdp4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bdp4j.dataset.CSVDatasetReader;
import org.bdp4j.transformers.Enum2IntTransformer;
import org.bdp4j.types.Dataset;
import org.bdp4j.types.Transformer;
import org.nlpa.util.BabelUtils;


/**
 *
 * @author María Novo
 */
public class ConseguirTodosHiperonimos {
     public static void main(String[] args) throws IOException {
    	  
 		//Creamos el mapa para la lista de transformadores para sustituir ham por 0, spam por 1
 		Map<String, Integer> transformList = new HashMap<>();
 		transformList.put("ham", 0);
 		transformList.put("spam", 1);
 		
		//Creamos el mapa para aplicar los transformadores
		Map<String, Transformer> transformersList = new HashMap<>();
		
		transformersList.put("target", new Enum2IntTransformer(transformList));
		
		//Cargamos el fichero csv.
		String filePath = "outputsyns.csv";
		
		// Aplicamos los transformadores y creamos el dataset con los cambios de los transformadores
		//ESTO ES NECESARIO O LO PUEDO HACER DE OTRA FORMA MAS RAPIDA?
		CSVDatasetReader filedataset = new CSVDatasetReader(filePath, transformersList);
		Dataset originalDataset = filedataset.loadFile();
		
		//Clonamos el dataset para empezar a trabajar con la copia
		Dataset clonedDataset;
		clonedDataset=originalDataset.clone();
		
		//Dejamos el dataset clonado solo con los synsets y el campo target
		clonedDataset.filterColumns("^bn:|target");


		
		//generamos un cromosoma, que es lo que nos debería de devolver JMetal
		ArrayList<Integer> cromosoma = new ArrayList<Integer>();
		for (int i = 0; i < (clonedDataset.numAttributes())-1; i++) {
			cromosoma.add((int) (Math.random() * 3));
		}
		System.out.println("Cromosma:" + cromosoma.toString());
		
		
		//Creamos una lista con los synsets del dataset y sacamos una copia
		List<String> synsetList = clonedDataset.filterColumnNames("^bn:");
		
		ArrayList <String> todosLosHiperonimos = new ArrayList<String>();

		for (int s=0; s<synsetList.size(); s++) {
			System.out.println("synset [" + s + "] " + synsetList.get(s));;
			for (int j=0; j<=10; j++) {
				todosLosHiperonimos.add(BabelUtils.getDefault().getSynsetHypernymFromLevel(synsetList.get(s), j) );
				System.out.print(todosLosHiperonimos.get(j) );
			}
		}
		
		System.out.println("FIN");
		
    	 
    	 
    	 
    	
    }
}
