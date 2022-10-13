package synsetListCsvToWekaArff;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import types.CachedBabelUtils;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bdp4j.dataset.CSVDatasetReader;
import org.bdp4j.transformers.Enum2IntTransformer;
import org.bdp4j.types.Dataset;
import org.bdp4j.types.Transformer;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

/**
 *
 * @author María Novo
 */
public class CsvToArff {
	
	public static int numSynsets, trainNumSynsets;
	public static Dataset fullClonedDataset, trainClonedDataset, testClonedDataset;
	public static List<String> fullSynsetListOriginal, synsetListTest, synsetListTrain;
	public static List<String> synsetList;
	public static int fp, fn, dim;
	public static CachedBabelUtils cachedBabelUtils;
	public static Instances clonedWekaDataset, trainClonedWekaDataset, testClonedWekaDataset;
	public static Map<String, String> changesInSynsetToReduce;
	public int  numberOfObjectives=3; //2 para optimizar dos objetivos. 3 para optimizar 3

	
	//Cargamos el fichero de Train
	public static void cargarDatasetTrain() {
		//Creamos el mapa para la lista de transformadores para sustituir ham por 0, spam por 1
		Map<String, Integer> trainTransformList = new HashMap<>();
		trainTransformList.put("ham", 0);
		trainTransformList.put("spam", 1);
		
		//Creamos el mapa para aplicar los transformadores
		Map<String, Transformer> trainTransformersList = new HashMap<>();
		
		trainTransformersList.put("target", new Enum2IntTransformer(trainTransformList));

		//Cargamos el fichero de train con el 75%.
		String trainFilePath = "out75_youtube.csv";

		// Aplicamos los transformadores y creamos el dataset con los cambios de los transformadores
		CSVDatasetReader trainFiledataset = new CSVDatasetReader(trainFilePath, trainTransformersList);
		Dataset trainOriginalDataset = trainFiledataset.loadFile();

		//Clonamos el dataset para empezar a trabajar con la copia
		trainClonedDataset = trainOriginalDataset.clone();

		//Dejamos el dataset clonado solo con los synsets y el campo target
		trainClonedDataset.filterColumns("^bn:|target");
	}
	
	//Cargamos el fichero de Test
	public static void cargarDatasetTest() {
		//Creamos el mapa para la lista de transformadores para sustituir ham por 0, spam por 1
		Map<String, Integer> testTransformList = new HashMap<>();
		testTransformList.put("ham", 0);
		testTransformList.put("spam", 1);
		
		//Creamos el mapa para aplicar los transformadores
		Map<String, Transformer> testTransformersList = new HashMap<>();
		
		testTransformersList.put("target", new Enum2IntTransformer(testTransformList));

		//Cargamos el fichero de train con el 75%.
		String testFilePath = "out25_youtube.csv";

		// Aplicamos los transformadores y creamos el dataset con los cambios de los transformadores
		CSVDatasetReader testFiledataset = new CSVDatasetReader(testFilePath, testTransformersList);
		Dataset testOriginalDataset = testFiledataset.loadFile();

		//Clonamos el dataset para empezar a trabajar con la copia
		testClonedDataset = testOriginalDataset.clone();

		//Dejamos el dataset clonado solo con los synsets y el campo target
		testClonedDataset.filterColumns("^bn:|target");
	}
	
	public static void generateTrainWekaDataset() {
		//Creamos el dataset de test para pasarsélo a Weka
		trainClonedWekaDataset = trainClonedDataset.getWekaDataset(); 
		//La última columna es la clase.
		trainClonedWekaDataset.setClassIndex(trainClonedWekaDataset.numAttributes()-1);
	}
	
	public static void generateTestWekaDataset() {
		//Creamos el dataset de train para paséselo a Weka
		testClonedWekaDataset = testClonedDataset.getWekaDataset();
		//La última columna es la clase
		testClonedWekaDataset.setClassIndex(testClonedWekaDataset.numAttributes()-1);
	}
	
	//Guardamos en disco el arff del fichero de train
	public static void saveTrainClonedWekaDataset() throws IOException {
    //Guardamos como arff el dataset clonado para ver que todo esta correcto
	ArffSaver saver = new ArffSaver();
	saver.setInstances(trainClonedWekaDataset);
	saver.setFile(new File("trainClonedWekaDataset.arff"));
	saver.writeBatch();
	}
	
	//Guardamos en disco el arff del fichero de test
	public static void saveTestClonedWekaDataset() throws IOException {
	    //Guardamos como arff el dataset clonado para ver que todo esta correcto
		ArffSaver saver = new ArffSaver();
		saver.setInstances(testClonedWekaDataset);
		saver.setFile(new File("testClonedWekaDataset.arff"));
		saver.writeBatch();
		}

	
	

	
	
	public static void main(String[] args) throws IOException {
		System.out.println("Ejecutando main.");


		cargarDatasetTrain();
		cargarDatasetTest();
		generateTrainWekaDataset();
		generateTestWekaDataset();
		saveTrainClonedWekaDataset();
		saveTestClonedWekaDataset();


		

	}
}
	
