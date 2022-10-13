/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conPerdida;

import types.CachedBabelUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.bdp4j.transformers.Enum2IntTransformer;
import org.bdp4j.types.Dataset;
import org.bdp4j.types.Transformer;

import wekaClassifers.Call2Classifiers;

import weka.core.Instances;


/**
 *
 * @author María Novo
 */
public class Result {
	
	private List<Integer> chromosome, chromosome_tmp;
	private Dataset clonedDataset;
	private List<String> synsetListOriginal, synsetList, synsetListOriginal_tmp, synsetListToDelete;
	private int fp, fn, dim;
	private CachedBabelUtils cachedBabelUtils;
	private Instances clonedWekaDataset;
	private Map<String, String> changesInSynsetToReduce;
        
	public  Result(){
	}
	
	public Result (CachedBabelUtils cachedBabelUtils, Dataset clonedDataset, List<Integer> chromosome) {
		this.cachedBabelUtils = cachedBabelUtils;
		this.clonedDataset = clonedDataset;
		this.chromosome_tmp = chromosome; 
	}
	
	public Map<String, Integer> generateResult(){
		//System.out.println("cromosoma1:           " + chromosome_tmp);
		deleteNegativeElements();
		//Sacamos una copia de la lista de synsets antes de empezar a modificarla
		synsetList = new ArrayList<String>(synsetListOriginal);
		//System.out.println("cromosoma2:           " + chromosome);
		//System.out.println("synsetList original :" + synsetList.toString());
		
		escalateSynsets();
		changesToReduceDataset();
		generateWekaDataset();
		runClassifier();
		
		Map<String, Integer> resultados = new HashMap<String, Integer>();
		resultados.put("fp", fp);
		resultados.put("fn", fn);
		resultados.put("dim", dim);
		return resultados;
	}
   	
	private void generateWekaDataset() {
		//Creamos el dataset para pasarsélo a Weka
		clonedWekaDataset = clonedDataset.getWekaDataset(); 
		//La última columna es la clase.
		clonedWekaDataset.setClassIndex(clonedWekaDataset.numAttributes() -1);
	}
	
	
	private void runClassifier() {
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
				c1.setDataset(clonedWekaDataset);
				c1.execute();
				//Si no queremos información del clasificador comentamos esta línea
				//System.out.println(c1.toString());
				fp = c1.getExecutionValues().get("fp");
				fn = c1.getExecutionValues().get("fn");
				dim = c1.getExecutionValues().get("dim");
				//System.out.println("fp:" + fp + " fn:" + fn + " dim:" + dim);


			} catch (Exception ex) {
				System.out.println("Error al llamar al clasificador" + ex);
			}
        }
		
	}
	
	private void deleteNegativeElements() {
		int pos;
		
		synsetListToDelete = new ArrayList<String>();
		synsetListOriginal_tmp = clonedDataset.filterColumnNames("^bn:");
		chromosome = new ArrayList<Integer>();
		synsetListOriginal = new ArrayList<String>();
		
		//System.out.println("Empieza la reduccion");
		for (pos=0; pos<synsetListOriginal_tmp.size(); pos++) {
			if (chromosome_tmp.get(pos) == -1) { //Si tenemos un -1 en el cromosoma añadimos ese synset a la lista a eliminar del dataset
												 //y pasamos al siguiente
				synsetListToDelete.add(synsetListOriginal_tmp.get(pos)); //añadimos en synset a eliminar a la lista synsetListToDelete
			}
			else { //En caso de no tener un -1 copiamos el valor del chromosome_tmp en "chromosome" y el synset en synsetListOriginal
				chromosome.add(chromosome_tmp.get(pos));
				synsetListOriginal.add(synsetListOriginal_tmp.get(pos));
			}
		}
		//Borramos las columnas de los synsets del dataset
		//System.out.println("numero de atributos en el dataset antes de reducir: " + clonedDataset.getAttributes().size());
		clonedDataset.deleteAttributeColumns(synsetListToDelete);
		//System.out.println("numero de atributos en el dataset despues de reducir: " + clonedDataset.getAttributes().size());
		//System.out.println("Reduccion hecha");
		//System.out.println("Longitud SynsetListOriginal_tmp : " + synsetListOriginal_tmp.size());
		//System.out.println("longitud cromosoma original :" + chromosome_tmp.size());
		//System.out.println("longitud SynsetlistOriginal red :" + synsetListOriginal.size());
		//System.out.println("longitud nuevo cromosoma : " + chromosome.size());
		
	}
	
	
	private void escalateSynsets() {
		//Escalamos los synsets y los nuevos synsets se meten en el mapa
		int pos;
		int longitud = synsetList.size();

		for (pos = 0; pos < longitud; pos++) {
			if ( (chromosome.get(pos) != 0) && (chromosome.get(pos) != -1) ) {
				//if ( !cachedBabelUtils.existsSynsetInMap(synsetList.get(pos)) ) {
				//	cachedBabelUtils.addSynsetToCacheAutomatic(synsetList.get(pos));
				//}
				synsetList.set(pos, cachedBabelUtils.getCachedHypernym( synsetList.get(pos),chromosome.get(pos) ) );
				
			}
		}
		//Por si hay nuevos synsets, guardamos el mapa en el fichero.
		//guardar(cachedBabelUtils.getMapOfHypernyms());

		//imprimimos el synsetList escalado según lo que nos dice el genético
		//System.out.println("SynsetList escalado: " + synsetList);

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

	
	private void changesToReduceDataset() {
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

		//clonedDataset.generateARFFWithComments(transformersList, "");
		clonedDataset.replaceColumnNames(changesInSynsetToReduce, Dataset.COMBINE_SUM); 
		//clonedDataset.generateARFFWithComments(transformersList, "replaceColumnNames.arff");
	}


	

}
	
