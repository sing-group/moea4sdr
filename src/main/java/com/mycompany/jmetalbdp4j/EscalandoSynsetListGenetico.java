package com.mycompany.jmetalbdp4j;

import java.util.ArrayList;
import java.util.HashMap;
import org.nlpa.util.BabelUtils;


public class EscalandoSynsetListGenetico {

	public static void main(String[] args) {
		ArrayList<String> synsetList = new ArrayList<String>();
		HashMap<String, String> synsetsToChange = new HashMap<String, String>();

		//Creamos un synsetList a manopla
		synsetList.add("bn:00071570n"); // Viagra o sildenafil
		synsetList.add("bn:00019048n");// cialis o tadalafil
		synsetList.add("bn:00015556n");// Rome Italian capital
		synsetList.add("bn:00015620n");// Madrid Spain capital
		synsetList.add("bn:00007309n");// Car vehicle
		synsetList.add("bn:00010248n");// Bike vehicle
		synsetList.add("bn:00008364n");// bank financial institution
		synsetList.add("bn:00044994n");// House home
		synsetList.add("bn:00061314n");// pen to write with ink
		synsetList.add("bn:00004622n");// tranquilizer
		synsetList.add("bn:00104407a");// hungry
		synsetList.add("bn:00090460v");// search look for
		synsetList.add("bn:00114671r");// carefully adverb

		BabelUtils babel;
		babel = BabelUtils.getDefault();
		ArrayList<Integer> cromosoma = new ArrayList<Integer>();
		int pos, posDesdeInicio, posHastaFinal;
		int longitud = synsetList.size();


		// generamos el cromosoma
		//		Random random;
		//		for (int i = 0; i < 10; i++) {
		//			cromosoma.add((int) (Math.random() * 3));
		//		}

		//Ponemos en el cromosoma lo que nos interesa para ver si funciona
		cromosoma.add(0);cromosoma.add(1);cromosoma.add(0);cromosoma.add(1);cromosoma.add(0);
		cromosoma.add(0);cromosoma.add(1);cromosoma.add(0);cromosoma.add(0);cromosoma.add(2);
		cromosoma.add(2);cromosoma.add(4);cromosoma.add(2);


		//imprimimos el cromosoma
		System.out.println("Cromosoma:" + cromosoma);

		//copiamos el synsetList
		System.out.println("SynsetList original: " + synsetList.toString());
		ArrayList <String> originalSynsetList = new ArrayList<String>(synsetList);

		// escalamos los synsets de acuerdo al cromosoma
		for (pos = 0; pos < longitud; pos++) {
			if (cromosoma.get(pos) != 0) {
				synsetList.set(pos, babel.getSynsetHypernymFromLevel(synsetList.get(pos), cromosoma.get(pos)));				
			}
		}


		//imprimimos el synsetList escalado según lo que nos dice el genético
		System.out.println("SynsetList escalado: " + synsetList.toString());

		// miramos si los synsets que hemos generalizado son los padres de algún otro
		// synset
		String padre;
		// recorremos toda la lista de synsets
		for (pos = 0; pos < longitud; pos++) {
			// Si se ha pedido que se escale el synset miramos si es padre de otro, sino no hacemos
			// nada con el synset y pasamos al siguiente.
			if (cromosoma.get(pos) != 0) {
				// cogemos el synset con su valor después de escalarlo
				padre = synsetList.get(pos);
				//System.out.println("synset escalado en posicion " + pos + " es [" + padre + "]");
				// recorremos la lista de synsets desde el principio hasta esa posición para ver
				// si hay synsets cuyo padre sea este synset
				//System.out.println("Primer cacho");
				for (posDesdeInicio = 0; posDesdeInicio < pos; posDesdeInicio++) {
					//System.out.println("Comparando [" + synsetList.get(posDesdeInicio).toString() + 
					//		"] con [" + padre + "]");
					if ( babel.isSynsetHypernymOf(synsetList.get(posDesdeInicio), padre) ) {
						//	System.out.println(" es hijo, así que se cambia.");
						synsetList.set(posDesdeInicio, padre);
					}


				}
				//System.out.println("segundo cacho");
				for (posHastaFinal = pos+1; posHastaFinal < longitud; posHastaFinal++) {
					//System.out.println("Comparando [" + synsetList.get(posHastaFinal).toString() + 
					//		"] con [" + padre + "]");
					if ( babel.isSynsetHypernymOf(synsetList.get(posHastaFinal), padre) ) {
						//	System.out.println(" es hijo, así que se cambia.");
						synsetList.set(posHastaFinal, padre);
					}
					else {
						//	System.out.println(" no es hijo, así que no se cambia.");
					}
				}
			}
		}

		for (int i=0; i<synsetList.size(); i++) {
			if ( !(originalSynsetList.get(i).equals(synsetList.get(i))) ) {
				synsetsToChange.put(originalSynsetList.get(i), synsetList.get(i));
			}
		}
		System.out.println("SynsetList desalida: " + synsetList.toString());
		System.out.println(synsetsToChange.toString());
	}

}


