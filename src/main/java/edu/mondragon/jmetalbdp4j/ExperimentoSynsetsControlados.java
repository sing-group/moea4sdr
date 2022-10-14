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
import org.nlpa.util.BabelUtils;

import edu.mondragon.wekaclassifers.Call2Classifiers;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

/**
 *
 * @author María Novo
 */
public class ExperimentoSynsetsControlados {
	
	public static ArrayList<Integer> chromosome;
	public static int numSynsets;
	public static Dataset clonedDataset;
	public static List<String> synsetListOriginal;
	public static List<String> synsetList;
	public int fp, fn, dim;
	public static CachedBabelUtils cachedBabelUtils;
	public Instances clonedWekaDataset;
	public static Map<String, String> changesInSynsetToReduce;
	public int  numberOfObjectives=3; //2 para optimizar dos objetivos. 3 para optimizar 3
	
	public  ExperimentoSynsetsControlados(){
			chromosome = new ArrayList<Integer>();
			System.out.println("Experimento creado");
	}
		
	public static void loadSynsetCache() {
		File toRead=new File("outputsyns_medium_manopla_all.map");
		try(FileInputStream fis=new FileInputStream(toRead);
				ObjectInputStream ois=new ObjectInputStream(fis);){
			cachedBabelUtils = new CachedBabelUtils();
			
	

			HashMap<String,List<String>> mapInFile=(HashMap<String,List<String>>)ois.readObject();

			cachedBabelUtils.setMapOfHypernyms(mapInFile);
		}catch(Exception e){ System.out.println("4: Exception in file read.");}
	}
	
	public void saveSynsetCache() {
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
	

	
	public static void generateChromosome() {
		//generamos un chromosome, que es lo que nos debería de devolver JMetal
		chromosome = new ArrayList<Integer>();
		for (int i = 0; i < numSynsets; i++) {
			chromosome.add((int) (Math.random() * 2));
		}
	}
	

	
	public static void cargarDataset () { 
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
	
	public void generateWekaDataset() {
		//Creamos el dataset para pasarsélo a Weka
		clonedWekaDataset = clonedDataset.getWekaDataset(); 
		//La última columna es la clase.
		clonedWekaDataset.setClassIndex(clonedWekaDataset.numAttributes() -1);
	}
	
	public void saveClonedWekaDataset() throws IOException {
    //Guardamos como arff el dataset clonado para ver que todo esta correcto
	ArffSaver saver = new ArffSaver();
	saver.setInstances(clonedWekaDataset);
	saver.setFile(new File("clonedWekaDataset.arff"));
	saver.writeBatch();
	}
	
	public void runClassifier() {
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
				c1.setDataset(clonedWekaDataset);
				c1.execute();
				//Si no queremos información del clasificador comentamos esta línea
				//System.out.println(c1.toString());
				fp = c1.getExecutionValues().get("fp");
				fn = c1.getExecutionValues().get("fn");
				dim = c1.getExecutionValues().get("dim");


			} catch (Exception ex) {
				System.out.println("Error al llamar al clasificador" + ex);
			}
        }
		
	}
	
	public static void escalateSynsets() {
		//Escalamos los synsets y los nuevos synsets se meten en el mapa
		int pos;
		int longitud = synsetList.size();

		for (pos = 0; pos < longitud; pos++) {
			if (chromosome.get(pos) != 0) {
				if ( !cachedBabelUtils.existsSynsetInMap(synsetList.get(pos)) ) {
					cachedBabelUtils.addSynsetToCacheAutomatic(synsetList.get(pos));
				}
				synsetList.set(pos, cachedBabelUtils.getCachedHypernym( synsetList.get(pos),chromosome.get(pos) ) );
				
			}
		}
		//Por si hay nuevos synsets, guardamos el mapa en el fichero.
		//guardar(cachedBabelUtils.getMapOfHypernyms());

		//imprimimos el synsetList escalado según lo que nos dice el genético
		System.out.println("SynsetList escalado: " + synsetList);


		// miramos si los synsets que hemos generalizado son los padres de algún otro synsets en cuyo caso se cambian.
		for (pos = 0; pos < longitud; pos++) {
			// Si se ha pedido que se escale el synset miramos si es padre de otro, sino no hacemos
			// nada con el synset y pasamos al siguiente.
			if (chromosome.get(pos) != 0) {
				for (int recorrido=0; recorrido<longitud; recorrido++)
				{
					if (cachedBabelUtils.isSynsetFatherOf(synsetList.get(recorrido), synsetList.get(pos))) {
						synsetList.set(recorrido, synsetList.get(pos));
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
	
	public static void changesToReduceDataset() {
		changesInSynsetToReduce = new HashMap<String, String>();
		for (int k=0; k<synsetList.size(); k++) {
			if ( !synsetListOriginal.get(k).equals(synsetList.get(k)) ) {
				changesInSynsetToReduce.put(synsetListOriginal.get(k), synsetList.get(k));
			}
		}
		
		//Creamos el mapa para la lista de transformadores para sustituir ham por 0, spam por 1
		Map<String, Integer> transformList = new HashMap<>();
		transformList.put("ham", 0);
		transformList.put("spam", 1);

		//Creamos el mapa para aplicar los transformadores
		Map<String, Transformer> transformersList = new HashMap<>();

		transformersList.put("target", new Enum2IntTransformer(transformList));

		clonedDataset.generateARFFWithComments(transformersList, "");
		clonedDataset.replaceColumnNames(changesInSynsetToReduce, Dataset.COMBINE_SUM); 
		clonedDataset.generateARFFWithComments(transformersList, "expsynsetcontol.arff");
	}
	
	
	public static void main(String[] args) throws IOException {
		System.out.println("Ejecutando main de experimento");
		loadSynsetCache();
		System.out.println("Elementos en cache: " + cachedBabelUtils.getMapOfHypernyms().size());
		cargarDataset();
		//completeSynsetCache();
		//saveSynsetCache();
		//generateChromosome();
		chromosome = new ArrayList<Integer>();
		chromosome.add(3);chromosome.add(0);chromosome.add(2);chromosome.add(0);chromosome.add(1);
		chromosome.add(0);chromosome.add(0);chromosome.add(0);chromosome.add(0);chromosome.add(2);
		System.out.println("Synsetlist Original: " + synsetListOriginal);
		System.out.println("chromosome: " + chromosome);
		escalateSynsets();
		System.out.println("SynsetList final:    " + synsetList);
		//System.out.println("Valor chromosome: " + chromosome);
		//generateWekaDataset();
		//runClassifier();
		//System.out.println("SynsetList Original: " + synsetListOriginal);
		//escalateSynsets();
		//System.out.println("SynsetList final   : " + synsetList);
		changesToReduceDataset();
		System.out.println(changesInSynsetToReduce.toString());

		//System.out.println("Mapa de cambios: " + changesInSynsetToReduce.toString());
		//System.out.println("Final");
		//System.out.println("FP: " + fp + " FN:" + fn + " DIM:" + dim);
		//generateWekaDataset();
		//runClassifier();
		//System.out.println("FP: " + fp + " FN:" + fn + " DIM:" + dim);
		

	}
}
	
