package edu.mondragon.jmetalbdp4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.nlpa.util.BabelUtils;

import it.uniroma1.lcl.babelnet.BabelSynset;
import edu.mondragon.types.CachedBabelUtils;


public class ControlListaSynsets implements Serializable{
	
	public static List<String> createExampleSynsetList () {
		List<String> exampleList = new ArrayList<>();
		//Creamos un synsetList a manopla
		exampleList.add("bn:00071570n"); // Viagra o sildenafil
		exampleList.add("bn:00019048n");// cialis o tadalafil
		exampleList.add("bn:00015556n");// Rome Italian capital
		exampleList.add("bn:00015620n");// Madrid Spain capital
		exampleList.add("bn:00007309n");// Car vehicle
		exampleList.add("bn:00010248n");// Bike vehicle
		exampleList.add("bn:00008364n");// bank financial institution
		exampleList.add("bn:00044994n");// House home
		exampleList.add("bn:00061314n");// pen to write with ink
		exampleList.add("bn:00004622n"); // tranquilizer
		
		return exampleList;
	}
	
	
	public static void guardar(Map<String, List<String>> mapOfHypernyms){
		try{
			File fileOne=new File("lista1.map");
			FileOutputStream fos=new FileOutputStream(fileOne);
			ObjectOutputStream oos=new ObjectOutputStream(fos);

			oos.writeObject(mapOfHypernyms);
			oos.flush();
			oos.close();
			fos.close();
		}catch(Exception e){}
	}
	
	public static Map<String , List<String>> leer () {
	   try{
	        File toRead=new File("lista1.map");
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
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		List<String> synsetList = createExampleSynsetList();
		CachedBabelUtils cachedBabelUtils = new CachedBabelUtils();
		
		BabelUtils babel;
		babel = BabelUtils.getDefault();
		BabelSynset by;
		String hiperonimo;
		ArrayList<Integer> cromosoma = new ArrayList<Integer>();
		int pos, posDesdeInicio, posHastaFinal;
		int longitud = synsetList.size();


		// generamos el cromosoma
//		Random random;
//		for (int i = 0; i < 10; i++) {
//			cromosoma.add((int) (Math.random() * 3));
//		}
		
		//Ponemos en el cromosoma lo que nos interesa para ver si funciona
		cromosoma.add(1);cromosoma.add(0);cromosoma.add(0);cromosoma.add(0);cromosoma.add(0);
		cromosoma.add(0);cromosoma.add(0);cromosoma.add(0);cromosoma.add(0);cromosoma.add(2);

		
		//imprimimos el cromosoma
		System.out.println("Cromosoma:" + cromosoma);

		//imprimimos el synsetList
		System.out.println("SynsetList original: " + synsetList.toString());
		List<String> synsetListOriginal = new ArrayList<String>(synsetList);
		
		//Cargamos el mapa desde el fichero
		cachedBabelUtils.setMapOfHypernyms(leer());
		
		for (String s : synsetList) {
			if ( !cachedBabelUtils.existsSynsetInMap(s) ) {
				cachedBabelUtils.addSynsetToCache(s, BabelUtils.getDefault().getAllHypernyms(s) );
			}
		}
		
		System.out.println("Longitud del mapa: " + cachedBabelUtils.getMapOfHypernyms().size());

		// escalamos los synsets de acuerdo al cromosoma y los añadimos a la cache
		for (pos = 0; pos < longitud; pos++) {
			if (cromosoma.get(pos) != 0) {		
				synsetList.set(pos, cachedBabelUtils.getCachedHypernym( synsetList.get(pos),cromosoma.get(pos) ) );
				if ( !cachedBabelUtils.existsSynsetInMap(synsetList.get(pos)) ) {
					cachedBabelUtils.addSynsetToCacheAutomatic(synsetList.get(pos));
				}
			}
		}
		System.out.println("Longitud del mapa: " + cachedBabelUtils.getMapOfHypernyms().size());
		//imprimimos el synsetList escalado según lo que nos dice el genético
		System.out.println("SynsetList escalado: " + synsetList.toString());
		
		
		// miramos si los synsets que hemos generalizado son los padres de algún otro
		// synset
		// recorremos toda la lista de synsets/cromosoma
		for (pos = 0; pos < longitud; pos++) {
			// Si se ha pedido que se escale el synset miramos si es padre de otro, sino no hacemos
			// nada con el synset y pasamos al siguiente.
			if (cromosoma.get(pos) != 0) {
				for (int recorrido=0; recorrido<=synsetList.size()-1; recorrido++)
				{
					if (cachedBabelUtils.isSynsetFatherOf(synsetList.get(recorrido), synsetList.get(pos))) {
						synsetList.set(recorrido, synsetList.get(pos));
					}
				}
			}
		}
		System.out.println("SynsetList final:    " + synsetList.toString());
		System.out.println(cachedBabelUtils.getMapOfHypernyms().toString());
		System.out.println("Longitud final del mapa : " + cachedBabelUtils.getMapOfHypernyms().size());
	
		guardar(cachedBabelUtils.getMapOfHypernyms());
		cachedBabelUtils.clearCache();
		System.out.println("Longitud mapa borrado: " + cachedBabelUtils.getMapOfHypernyms().size());

		System.out.println("Longitud mapa cargado: " + cachedBabelUtils.getMapOfHypernyms().size());
		
		Map<String, String> changesInSynsetToReduce = new HashMap<String, String>();
		for (int k=0; k<synsetList.size(); k++) {
			if ( !synsetListOriginal.get(k).equals(synsetList.get(k)) ) {
				changesInSynsetToReduce.put(synsetListOriginal.get(k), synsetList.get(k));
			}
		System.out.println(changesInSynsetToReduce);
		}
	}
	
	

}




