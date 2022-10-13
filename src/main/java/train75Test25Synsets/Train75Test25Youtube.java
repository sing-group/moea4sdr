package train75Test25Synsets;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import types.CachedBabelUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bdp4j.dataset.CSVDatasetReader;
import org.bdp4j.transformers.Enum2IntTransformer;
import org.bdp4j.types.Dataset;
import org.bdp4j.types.Transformer;
import org.nlpa.util.BabelUtils;

import wekaClassifers.Call2Classifiers;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

/**
 *
 * @author María Novo
 */
public class Train75Test25Youtube {
	
	public static ArrayList<Integer> chromosome;
	public static int numSynsets, trainNumSynsets;
	public static Dataset fullClonedDataset, trainClonedDataset, testClonedDataset;
	public static List<String> fullSynsetListOriginal, synsetListTest, synsetListTrain;
	public static List<String> synsetList;
	public static int fp, fn, dim;
	public static CachedBabelUtils cachedBabelUtils;
	public static Instances clonedWekaDataset, trainClonedWekaDataset, testClonedWekaDataset;
	public static Map<String, String> changesInSynsetToReduce;
	public int  numberOfObjectives=3; //2 para optimizar dos objetivos. 3 para optimizar 3
	
	
	//Generamos un cromosoma para hacer las pruebas. Luego cargarémos el bueno desde el fichero.
	public static void generateChromosome() {
		String cadena ="0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 1 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0";
		String str[] = cadena.split(" ");
		List<String> al = new ArrayList<String>();
		al = Arrays.asList(str);
		chromosome = new ArrayList<Integer>();
		for(String s: al){
		   chromosome.add(Integer.parseInt(s));
		}

	}
	
	//Carga del fichero de synsets cacheados
	public static void loadSynsetCache() {
		File toRead=new File("outputsyns_youtube_last_all.map");
		try(FileInputStream fis=new FileInputStream(toRead);
				ObjectInputStream ois=new ObjectInputStream(fis);){
			cachedBabelUtils = new CachedBabelUtils();
			HashMap<String,List<String>> mapInFile=(HashMap<String,List<String>>)ois.readObject();
			cachedBabelUtils.setMapOfHypernyms(mapInFile);
		}catch(Exception e){ System.out.println("7: Exception in file read.");}
	}

	//Cargamos la lista de synsets, común para los ficheros de train y test
	public static void cargarSynsetList() {
		//Creamos el mapa para la lista de transformadores para sustituir ham por 0, spam por 1
		Map<String, Integer> fullTransformList = new HashMap<>();
		fullTransformList.put("ham", 0);
		fullTransformList.put("spam", 1);
		
		//Creamos el mapa para aplicar los transformadores
		Map<String, Transformer> fullTransformersList = new HashMap<>();
		
		fullTransformersList.put("target", new Enum2IntTransformer(fullTransformList));

		//Cargamos el fichero csv con el 100%.
		String fullFilePath = "outputsyns_youtube_last.csv";

		// Aplicamos los transformadores y creamos el dataset con los cambios de los transformadores
		CSVDatasetReader fullFiledataset = new CSVDatasetReader(fullFilePath, fullTransformersList);
		Dataset fullOriginalDataset = fullFiledataset.loadFile();

		//Clonamos el dataset para empezar a trabajar con la copia
		fullClonedDataset = fullOriginalDataset.clone();

		//Dejamos el dataset clonado solo con los synsets y el campo target
		fullClonedDataset.filterColumns("^bn:|target");
		
		//Creamos una lista sólo con los synsets del dataset sin la columna de la clase
		fullSynsetListOriginal = fullClonedDataset.filterColumnNames("^bn:");
		numSynsets = fullSynsetListOriginal.size();
		//System.out.println("Número de synsets en la lista: " + numSynsets);
		synsetListTrain = new ArrayList<String>(fullSynsetListOriginal);
		synsetListTest = new ArrayList<String>(fullSynsetListOriginal);
	}
	
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
	
	public static void runClassifierTrain() {
		//Creamos un clasificador abstracto al que luego le daremos nombre.
        //Classifier clasificador = null;
        
        //Array de los clasificadores para usarlos
		ArrayList<String> classificatorList = new ArrayList<String>();
		//classificatorList.add("weka.classifiers.bayes.BayesNet");
		//classificatorList.add("weka.classifiers.bayes.NaiveBayes");
		classificatorList.add("weka.classifiers.bayes.NaiveBayesMultinomial");
		//classificatorList.add("weka.classifiers.bayes.NaiveBayesMultinomialText");
		//classificatorList.add("weka.classifiers.bayes.NaiveBayesMultinomialUpdateable");
		//classificatorList.add("weka.classifiers.bayes.NaiveBayesUpdateable");
		//classificatorList.add("weka.classifiers.trees.J48");
		//classificatorList.add("weka.classifiers.trees.RandomForest");
		
		
		//Ejecutamos la lista de los clasificadores y sacamos los datos que nos interesan
		for (String c : classificatorList)
		{
			try {
				System.out.println("------------------------------------------");
				System.out.println("-" + c + "-");
				System.out.println("------------------------------------------");
				Call2Classifiers c1 = new Call2Classifiers();
				String name= c;
				String[] options = new String[1];
				options[0] = "";
				c1.setClassifier(name, options);
				
				//Les pasamos a los clasificadores el dataset a evaluar
				//El clasificador saca una copia, no toca para nada este dataset.
				c1.setDataset(trainClonedWekaDataset);
				c1.execute();
				//Si no queremos información del clasificador comentamos esta línea
				System.out.println(c1.toString());
				fp = c1.getExecutionValues().get("fp");
				fn = c1.getExecutionValues().get("fn");
				dim = c1.getExecutionValues().get("dim");


			} catch (Exception ex) {
				System.out.println("Error al llamar al clasificador" + ex);
			}
        }
		
	}
	
	public static void runClassifierTest() {
		//Creamos un clasificador abstracto al que luego le daremos nombre.
        //Classifier clasificador = null;
        
        //Array de los clasificadores para usarlos
		ArrayList<String> classificatorList = new ArrayList<String>();
		//classificatorList.add("weka.classifiers.bayes.BayesNet");
		//classificatorList.add("weka.classifiers.bayes.NaiveBayes");
		classificatorList.add("weka.classifiers.bayes.NaiveBayesMultinomial");
		//classificatorList.add("weka.classifiers.bayes.NaiveBayesMultinomialText");
		//classificatorList.add("weka.classifiers.bayes.NaiveBayesMultinomialUpdateable");
		//classificatorList.add("weka.classifiers.bayes.NaiveBayesUpdateable");
		//classificatorList.add("weka.classifiers.trees.J48");
		//classificatorList.add("weka.classifiers.trees.RandomForest");
		
		
		//Ejecutamos la lista de los clasificadores y sacamos los datos que nos interesan
		for (String c : classificatorList)
		{
			try {
				System.out.println("------------------------------------------");
				System.out.println("-" + c + "-");
				System.out.println("------------------------------------------");
				Call2Classifiers c1 = new Call2Classifiers();
				String name= c;
				String[] options = new String[1];
				options[0] = "";
				c1.setClassifier(name, options);
				
				//Les pasamos a los clasificadores el dataset a evaluar
				//El clasificador saca una copia, no toca para nada este dataset.
				c1.setDataset(testClonedWekaDataset);
				c1.execute();
				//Si no queremos información del clasificador comentamos esta línea
				System.out.println(c1.toString());
				fp = c1.getExecutionValues().get("fp");
				fn = c1.getExecutionValues().get("fn");
				dim = c1.getExecutionValues().get("dim");


			} catch (Exception ex) {
				System.out.println("Error al llamar al clasificador" + ex);
			}
        }
		
	}

	//Escalamos los synsets del fichero de train
	public static void escalateSynsetsTrain() {
		int pos;
		int longitud = numSynsets;

		for (pos = 0; pos < longitud; pos++) {
			if (chromosome.get(pos) != 0) {
				if ( !cachedBabelUtils.existsSynsetInMap(synsetListTrain.get(pos)) ) {
					cachedBabelUtils.addSynsetToCacheAutomatic(synsetListTrain.get(pos));
				}
				synsetListTrain.set(pos, cachedBabelUtils.getCachedHypernym( synsetListTrain.get(pos),chromosome.get(pos) ) );
				
			}
		}
		//Por si hay nuevos synsets, guardamos el mapa en el fichero.
		//guardar(cachedBabelUtils.getMapOfHypernyms());

		//imprimimos el synsetList escalado según lo que nos dice el genético
		//System.out.println("SynsetList escalado: " + synsetListTest);


		// miramos si los synsets que hemos generalizado son los padres de algún otro synsets en cuyo caso se cambian.
		for (pos = 0; pos < longitud; pos++) {
			// Si se ha pedido que se escale el synset miramos si es padre de otro, sino no hacemos
			// nada con el synset y pasamos al siguiente.
			if (chromosome.get(pos) != 0) {
				for (int recorrido=0; recorrido<longitud; recorrido++)
				{
					if (cachedBabelUtils.isSynsetFatherOf(synsetListTrain.get(recorrido), synsetListTrain.get(pos))) {
						synsetListTrain.set(recorrido, synsetListTrain.get(pos));
					}
				}
			}
		}
	}
	
	//Escalamos los synsets del fichero de test
	public static void escalateSynsetsTest() {
		int pos;
		int longitud = numSynsets;

		for (pos = 0; pos < longitud; pos++) {
			if (chromosome.get(pos) != 0) {
				if ( !cachedBabelUtils.existsSynsetInMap(synsetListTest.get(pos)) ) {
					cachedBabelUtils.addSynsetToCacheAutomatic(synsetListTest.get(pos));
				}
				synsetListTest.set(pos, cachedBabelUtils.getCachedHypernym( synsetListTest.get(pos),chromosome.get(pos) ) );
				
			}
		}
		//Por si hay nuevos synsets, guardamos el mapa en el fichero.
		//guardar(cachedBabelUtils.getMapOfHypernyms());

		//imprimimos el synsetList escalado según lo que nos dice el genético
		//System.out.println("SynsetList escalado: " + synsetListTest);


		// miramos si los synsets que hemos generalizado son los padres de algún otro synsets en cuyo caso se cambian.
		for (pos = 0; pos < longitud; pos++) {
			// Si se ha pedido que se escale el synset miramos si es padre de otro, sino no hacemos
			// nada con el synset y pasamos al siguiente.
			if (chromosome.get(pos) != 0) {
				for (int recorrido=0; recorrido<longitud; recorrido++)
				{
					if (cachedBabelUtils.isSynsetFatherOf(synsetListTest.get(recorrido), synsetListTest.get(pos))) {
						synsetListTest.set(recorrido, synsetListTest.get(pos));
					}
				}
			}
		}
	}
	

	

	
	public void completeSynsetCache() {
		for (String s : synsetList) {
			if ( !cachedBabelUtils.existsSynsetInMap(s) ) {
				cachedBabelUtils.addSynsetToCache(s, BabelUtils.getDefault().getAllHypernyms(s) );
			}
		}
	}
	
	
	public static void changesToReduceDatasetTrain() {
		changesInSynsetToReduce = new HashMap<String, String>();
		for (int k=0; k<fullSynsetListOriginal.size(); k++) {
			if ( !fullSynsetListOriginal.get(k).equals(synsetListTrain.get(k)) ) {
				changesInSynsetToReduce.put(fullSynsetListOriginal.get(k), synsetListTrain.get(k));
			}
		}
		//Creamos el mapa para la lista de transformadores para sustituir ham por 0, spam por 1
		Map<String, Integer> transformList = new HashMap<>();
		transformList.put("ham", 0);
		transformList.put("spam", 1);

		//Creamos el mapa para aplicar los transformadores
		Map<String, Transformer> transformersList = new HashMap<>();

		transformersList.put("target", new Enum2IntTransformer(transformList));

		//clonedDataset.generateARFFWithComments(transformersList, "");
		trainClonedDataset.replaceColumnNames(changesInSynsetToReduce, Dataset.COMBINE_SUM); 
		//clonedDataset.generateARFFWithComments(transformersList, "replaceColumnNames.arff");
	}
	
	public static void changesToReduceDatasetTest() {
	
		//clonedDataset.generateARFFWithComments(transformersList, "");
		testClonedDataset.replaceColumnNames(changesInSynsetToReduce, Dataset.COMBINE_SUM); 
		//clonedDataset.generateARFFWithComments(transformersList, "replaceColumnNames.arff");
	}
		
//		
//		//Creamos el mapa para la lista de transformadores para sustituir ham por 0, spam por 1
//		Map<String, Integer> transformList = new HashMap<>();
//		transformList.put("ham", 0);
//		transformList.put("spam", 1);
//
//		//Creamos el mapa para aplicar los transformadores
//		Map<String, Transformer> transformersList = new HashMap<>();
//
//		transformersList.put("target", new Enum2IntTransformer(transformList));
//
//		clonedDataset.generateARFFWithComments(transformersList, "");
//		clonedDataset.replaceColumnNames(changesInSynsetToReduce, Dataset.COMBINE_SUM); 
//		clonedDataset.generateARFFWithComments(transformersList, "expsynsetcontol.arff");
//	}
	
	
	public static void main(String[] args) throws IOException {
		System.out.println("Ejecutando main.");
		loadSynsetCache();
		System.out.println("Elementos en cache: " + cachedBabelUtils.getMapOfHypernyms().size());
		cargarSynsetList();
		generateChromosome();
		System.out.println("Cromosoma: " + chromosome);
		cargarDatasetTrain();
		cargarDatasetTest();
		generateTrainWekaDataset();
		generateTestWekaDataset();

	


		escalateSynsetsTrain();
		escalateSynsetsTest();
		changesToReduceDatasetTrain();
		changesToReduceDatasetTest();
		generateTrainWekaDataset();
		generateTestWekaDataset();
		saveTrainClonedWekaDataset();
		saveTestClonedWekaDataset();
		
//		System.out.println("Train con reducción:");
//		runClassifierTrain();
//		System.out.println("Test con reducción:");
//		runClassifierTest();

//		chromosome = new ArrayList<Integer>();
//		chromosome.add(3);chromosome.add(0);chromosome.add(2);chromosome.add(0);chromosome.add(1);
//		chromosome.add(0);chromosome.add(0);chromosome.add(0);chromosome.add(0);chromosome.add(2);
//		System.out.println("Synsetlist Original: " + synsetListOriginal);
//		System.out.println("chromosome: " + chromosome);
//		escalateSynsets();
//		System.out.println("SynsetList final:    " + synsetList);
		//System.out.println("Valor chromosome: " + chromosome);
		//generateWekaDataset();
		//runClassifier();
		//System.out.println("SynsetList Original: " + synsetListOriginal);
		//escalateSynsets();
		//System.out.println("SynsetList final   : " + synsetList);
//		changesToReduceDataset();
//		System.out.println(changesInSynsetToReduce.toString());

		//System.out.println("Mapa de cambios: " + changesInSynsetToReduce.toString());
		//System.out.println("Final");
		//System.out.println("FP: " + fp + " FN:" + fn + " DIM:" + dim);
		//generateWekaDataset();
		//runClassifier();
		//System.out.println("FP: " + fp + " FN:" + fn + " DIM:" + dim);
		

	}
}
	
