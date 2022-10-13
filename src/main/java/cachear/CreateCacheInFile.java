package cachear;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import types.CachedBabelUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
public class CreateCacheInFile {
	static String cacheFileName;
	static String datasetFileName;
	
	//Para guardar el mapa de los synsets y sus hiperónimos en el disco
	public static void guardar(Map<String, List<String>> mapOfHypernyms){
		try{
			File fileOne=new File("outputsyns_medium_manopla.map");
			FileOutputStream fos=new FileOutputStream(fileOne);
			ObjectOutputStream oos=new ObjectOutputStream(fos);

			oos.writeObject(mapOfHypernyms);
			oos.flush();
			oos.close();
			fos.close();
		}catch(Exception e){}
	}
	
	
	//Para leer el fichero que contiene el mapa de hiperónimos y esta en el disco
	public static Map<String , List<String>> leer () {
	   try{
	        File toRead=new File("outputsyns_medium_manopla.map");
	        FileInputStream fis=new FileInputStream(toRead);
	        ObjectInputStream ois=new ObjectInputStream(fis);

	        HashMap<String,List<String>> mapInFile=(HashMap<String,List<String>>)ois.readObject();

	        ois.close();
	        fis.close();
	        //print All data in MAP
	        return mapInFile;
	    }catch(Exception e){}
	return null;
	  }
	
	
     public static void main(String[] args) throws IOException {
    	  
 		//Creamos el mapa para la lista de transformadores para sustituir ham por 0, spam por 1
 		Map<String, Integer> transformList = new HashMap<>();
 		transformList.put("ham", 0);
 		transformList.put("spam", 1);
 		
		//Creamos el mapa para aplicar los transformadores
		Map<String, Transformer> transformersList = new HashMap<>();
		
		transformersList.put("target", new Enum2IntTransformer(transformList));
		
		//datasetFileName = JOptionPane.showInputDialog("Que dataset quieres cargar: ");
		//cacheFileName = JOptionPane.showInputDialog("Nombre del fichero de cache:");
		
		//Cargamos el fichero csv.
		String filePath = "outputsyns_medium_manopla.csv";
		
		// Aplicamos los transformadores y creamos el dataset con los cambios de los transformadores
		//ESTO ES NECESARIO O LO PUEDO HACER DE OTRA FORMA MAS RAPIDA?
		CSVDatasetReader filedataset = new CSVDatasetReader(filePath, transformersList);
		Dataset originalDataset = filedataset.loadFile();
		System.out.println("csv");
		System.out.println("Dataset cargado");
		
		//Creamos una lista sólo con los synsets del dataset sin la columna de la clase
		List<String> synsetList = originalDataset.filterColumnNames("^bn:");
		System.out.println("SynsetList original:" + synsetList);
		
		//Creamos el mapa para la cache
		//Primero leemos el fichero.
		CachedBabelUtils cachedBabelUtils = new CachedBabelUtils();
		System.out.println("Elementos en la cache antes de leer: " +cachedBabelUtils.getMapOfHypernyms().size());
		//cachedBabelUtils.setMapOfHypernyms(leer());
		System.out.println("Elementos en la cache después de leer: " +cachedBabelUtils.getMapOfHypernyms().size());
		
		for (String s : synsetList) {
			if ( !cachedBabelUtils.existsSynsetInMap(s) ) {
				cachedBabelUtils.addSynsetToCache(s, BabelUtils.getDefault().getAllHypernyms(s) );
				System.out.println("Añadiendo " + s);
				for (String h : cachedBabelUtils.getCachedSynsetHypernymsList(s)) {
					if ( !cachedBabelUtils.existsSynsetInMap(h) ) {
						System.out.println("Añadiendo " + h);
						cachedBabelUtils.addSynsetToCache(h, BabelUtils.getDefault().getAllHypernyms(h) );
					}
				}
			}
		}	

		guardar(cachedBabelUtils.getMapOfHypernyms());
		System.out.println("Elementos al final: " +cachedBabelUtils.getMapOfHypernyms().size());

	 
    	
    }
}
