/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mondragon.jmetalbdp4j;

import edu.mondragon.types.CachedBabelUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.bdp4j.dataset.CSVDatasetReader;
import org.bdp4j.transformers.attribute.Enum2IntTransformer;
import org.bdp4j.types.Dataset;
import org.bdp4j.types.Transformer;



import weka.core.Instances;


/**
 *
 * @author María Novo
 */
public class CachearSynsetsEnFichero {
	
	public ArrayList<Integer> chromosome;
	public static int numSynsets;
	public static Dataset clonedDataset;
	public static List<String> synsetListOriginal;
	public static List<String> synsetList;
	public int fp, fn, dim;
	public static CachedBabelUtils cachedBabelUtils;
	public Instances clonedWekaDataset;
	public Map<String, String> changesInSynsetToReduce;
	public int  numberOfObjectives=3; //2 para optimizar dos objetivos. 3 para optimizar 3
	
	
	public static void loadSynsetCache() {
		File toRead=new File("outputsyns_medium_manopla_all.map");
		try(FileInputStream fis=new FileInputStream(toRead);
				ObjectInputStream ois=new ObjectInputStream(fis);){
			cachedBabelUtils = new CachedBabelUtils();
			HashMap<String,List<String>> mapInFile=(HashMap<String,List<String>>)ois.readObject();
			cachedBabelUtils.setMapOfHypernyms(mapInFile);
		}catch(Exception e){ System.out.println("2: Exception in file read.");}
	}
	
	public static void saveSynsetCache() {
		try{
			File fileOne=new File("outputsyns_medium_manopla_all.map");
			FileOutputStream fos=new FileOutputStream(fileOne);
			ObjectOutputStream oos=new ObjectOutputStream(fos);

			oos.writeObject(cachedBabelUtils.getMapOfHypernyms());
			oos.flush();
			oos.close();
			fos.close();
		}catch(Exception e){}
	}
	

	public static void cargarDataset () {
		System.out.println("Cargando el dataset outputsyns.csv.");
		//Creamos el mapa para la lista de transformadores para sustituir ham por 0, spam por 1
		Map<String, Integer> transformList = new HashMap<>();
		transformList.put("ham", 0);
		transformList.put("spam", 1);

		//Creamos el mapa para aplicar los transformadores
		Map<String, Transformer> transformersList = new HashMap<>();

		transformersList.put("target", new Enum2IntTransformer(transformList));

		//Cargamos el fichero csv.
		String filePath = "outputsyns_medium_manopla.csv";

		// Aplicamos los transformadores y creamos el dataset con los cambios de los transformadores
		CSVDatasetReader filedataset = new CSVDatasetReader(filePath, transformersList);
		Dataset originalDataset = filedataset.loadFile();

		//Clonamos el dataset para empezar a trabajar con la copia
		clonedDataset = originalDataset.clone();

		//Dejamos el dataset clonado solo con los synsets y el campo target
		clonedDataset.filterColumns("^bn:|target");
		
		//Creamos una lista sólo con los synsets del dataset sin la columna de la clase
		synsetListOriginal = clonedDataset.filterColumnNames("^bn:");
		numSynsets = synsetListOriginal.size();
		//Sacamos una copia de la lista de synset antes de empezar a modificarla
		synsetList = new ArrayList<String>(synsetListOriginal);
	}

	
	public static void createCompleteSynsetCache() {
		for (String s : synsetList) {
			if ( !cachedBabelUtils.existsSynsetInMap(s) ) {
				cachedBabelUtils.addSynsetToCacheAutomatic(s);
				for (String h : cachedBabelUtils.getCachedSynsetHypernymsList(s)) {
					if ( !cachedBabelUtils.existsSynsetInMap(h) ) {
						cachedBabelUtils.addSynsetToCacheAutomatic(h);
					}
				}
			}
		}
	}
		
	
	public static void main(String[] args) throws IOException {
		System.out.println("Ejecutando el cacheo de synsets a fichero.");
		loadSynsetCache();
		System.out.println("Cache creada con elementos: " + cachedBabelUtils.getMapOfHypernyms().size());
		cargarDataset();
		//cachedBabelUtils = new CachedBabelUtils();
		createCompleteSynsetCache();
		saveSynsetCache();
		System.out.println("Cache creada con elementos: " + cachedBabelUtils.getMapOfHypernyms().size());
		
		
	}
}
	
