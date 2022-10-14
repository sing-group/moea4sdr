/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mondragon.jmetalbdp4j;

import edu.mondragon.types.CachedBabelUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bdp4j.types.Dataset;

import edu.mondragon.wekaclassifers.Call2Classifiers;
import weka.core.Instances;

/**
 *
 * @author María Novo
 */
public class DatasetToEvaluate {
	private CachedBabelUtils cachedBabelUtils; 
	private Dataset dataset;
	private Instances wekaDataset;
	private List<String> synsetListOriginal, synsetList;
	private int numSynsets;
	private ArrayList<Integer> chromosome;
	private int fp, fn, dim;
	
	public DatasetToEvaluate() {
	}
	
	public void loadSynsetCache (Map<String, List<String>> mapa) {
		cachedBabelUtils = new CachedBabelUtils();
		cachedBabelUtils.setMapOfHypernyms(mapa); 
	}
	
	public void setDataset (Dataset dataset) {
		this.dataset = dataset.clone();	
	}
	
	public void generateSynsetList() {
		synsetListOriginal = dataset.filterColumnNames("^bn:");
		numSynsets=synsetListOriginal.size();
		//Sacamos una copia de la lista de synset antes de empezar a modificarla
		synsetList = new ArrayList<String>(synsetListOriginal);
		//System.out.println("Original=" + synsetList);
	}

	public void generateChromosome() {
		//generamos un cromosoma, que es lo que nos debería de devolver JMetal
		chromosome = new ArrayList<Integer>();
		for (int i = 0; i < numSynsets; i++) {
			chromosome.add((int) (Math.random() * 2));
		}
		//System.out.println(chromosome);
	}
	
	public void escalateSynsets() {
		//Escalamos los synsets y los nuevos synsets se meten en el mapa
		int pos;
		int longitud = synsetList.size()-1;

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
		//System.out.println("Escalado=" + synsetList);


		// miramos si los synsets que hemos generalizado son los padres de algún otro synsets en cuyo caso se cambian.
		for (pos = 0; pos < longitud; pos++) {
			// Si se ha pedido que se escale el synset miramos si es padre de otro, sino no hacemos
			// nada con el synset y pasamos al siguiente.
			if (chromosome.get(pos) != 0) {
				for (int recorrido=0; recorrido<=synsetList.size()-1; recorrido++)
				{
					if (cachedBabelUtils.isSynsetFatherOf(synsetList.get(recorrido), synsetList.get(pos))) {
						synsetList.set(recorrido, synsetList.get(pos));
					}
				}
			}
		}
		//System.out.println("Final====" + synsetList);
	}
	
	public void reduceDataset() {
		Map<String, String> changesInSynsetToReduce;
		changesInSynsetToReduce = new HashMap<String, String>();
		for (int k=0; k<synsetList.size(); k++) {
			if ( !synsetListOriginal.get(k).equals(synsetList.get(k)) ) {
				changesInSynsetToReduce.put(synsetListOriginal.get(k), synsetList.get(k));
			}
		}
		//System.out.println(changesInSynsetToReduce);
		dataset.replaceColumnNames(changesInSynsetToReduce, Dataset.COMBINE_SUM);
	}
	
	public void generateWekaDataset() {
		//Creamos el dataset para pasarsélo a Weka
		wekaDataset = dataset.getWekaDataset(); 
		//La última columna es la clase.
		wekaDataset.setClassIndex(wekaDataset.numAttributes() -1);
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
				//System.out.println("------------------------------------------");
				//System.out.println("-" + c + "-");
				//System.out.println("------------------------------------------");
				Call2Classifiers c1 = new Call2Classifiers();
				String name= c;
				String[] options = new String[1];
				options[0] = "";
				c1.setClassifier(name, options);
				
				//Les pasamos a los clasificadores el dataset a evaluar
				//El clasificador saca una copia, no toca para nada este dataset.
				c1.setDataset(wekaDataset);
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
		//System.out.println("FP:" + fp);
		//System.out.println("FN:" + fn);
		//System.out.println("dim:" + dim);
		
	}
	
	public int getfp() {
		return fp;
	}
	
	public int getfn() {
		return fn;
	}
	
	public int getdim() {
		return dim;
	}
	
	


}
	
