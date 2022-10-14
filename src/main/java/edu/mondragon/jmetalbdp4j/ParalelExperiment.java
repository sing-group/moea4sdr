package edu.mondragon.jmetalbdp4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bdp4j.dataset.CSVDatasetReader;
import org.bdp4j.transformers.attribute.Enum2IntTransformer;
import org.bdp4j.types.Dataset;
import org.bdp4j.types.Transformer;

import edu.mondragon.types.CachedBabelUtils;

public class ParalelExperiment {

	public static CachedBabelUtils cachedBabelUtils;
	public static Dataset copyOfDataset;
	
	public static void loadSynsetCache() {
		File toRead=new File("youtube_all.map");
		try(FileInputStream fis=new FileInputStream(toRead);
				ObjectInputStream ois=new ObjectInputStream(fis);){
			cachedBabelUtils = new CachedBabelUtils();
			HashMap<String,List<String>> mapInFile=(HashMap<String,List<String>>)ois.readObject();
			cachedBabelUtils.setMapOfHypernyms(mapInFile);
		}catch(Exception e){ System.out.println("5: Exception in file read.");}
	}
	
	public static Dataset cargarDataset () { 
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
		CSVDatasetReader filedataset = new CSVDatasetReader(filePath, transformersList);
		Dataset originalDataset = filedataset.loadFile();

		//Dejamos el dataset solo con los synsets y el campo target
		Dataset filteredDataset=originalDataset.clone();
		filteredDataset.filterColumns("^bn:|target");
		return filteredDataset;
	}
	

	
	
	
	public static void main(String[] args) {
		
		loadSynsetCache();
		copyOfDataset = cargarDataset();
		System.out.println("Ejecutando1");
		DatasetToEvaluate exp1 = new DatasetToEvaluate();
		exp1.loadSynsetCache(cachedBabelUtils.getMapOfHypernyms());
		exp1.setDataset(copyOfDataset);
		exp1.generateSynsetList();
		exp1.generateChromosome();
		exp1.escalateSynsets();
		exp1.generateWekaDataset();
		exp1.reduceDataset();
		exp1.generateWekaDataset();
		exp1.runClassifier();
		System.out.println("fp:" + exp1.getfp());
		System.out.println("fn:" + exp1.getfn());
		System.out.println("dim:" + exp1.getdim());
		
		System.out.println("Ejecutando2");
		DatasetToEvaluate exp2 = new DatasetToEvaluate();
		exp2.loadSynsetCache(cachedBabelUtils.getMapOfHypernyms());
		exp2.setDataset(copyOfDataset);
		exp2.generateSynsetList();
		exp2.generateChromosome();
		exp2.escalateSynsets();
		exp2.generateWekaDataset();
		exp2.reduceDataset();
		exp2.generateWekaDataset();
		exp2.runClassifier();
		System.out.println("fp:" + exp2.getfp());
		System.out.println("fn:" + exp2.getfn());
		System.out.println("dim:" + exp2.getdim());
		
	}

}
