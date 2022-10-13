package com.mycompany.jmetalbdp4j;

import java.util.ArrayList;
import java.util.List;

import it.uniroma1.lcl.babelnet.BabelNet;
import it.uniroma1.lcl.babelnet.BabelSynset;
import it.uniroma1.lcl.babelnet.BabelSynsetID;
import it.uniroma1.lcl.babelnet.BabelSynsetRelation;
import it.uniroma1.lcl.babelnet.data.BabelPointer;
import it.uniroma1.lcl.jlt.util.Language;

public class CheckHypernymFromLevel {

	public static void main (String[] args) throws Exception {
			
			//Creamos una lista de synsets
	        List<String> synsetList = new ArrayList<String>();
	        synsetList.add("bn:00084331v"); //buy
	        synsetList.add("bn:01236701n"); //mp3
	        synsetList.add("bn:15373517n"); //No existe
	        synsetList.add("bn:00060799n"); //player
	        
	        //Probamos que pasa con el synset de mp3 bn:01236701n
	        String synset="bn:01236701n";
	        BabelNet bn = BabelNet.getInstance();
    		BabelSynset by = bn.getSynset(new BabelSynsetID(synset));
    		
    		//Imprimimos el significado del synset
    		System.out.println("Significado del synset " + by.getID() + ": " + by.getMainSense(Language.EN).get().getFullLemma());
    		
    		List<BabelSynsetRelation> elementsInAnyHypernymPointer, elementsInHypernymPointer;
    		elementsInAnyHypernymPointer = by.getOutgoingEdges(BabelPointer.ANY_HYPERNYM);
    		elementsInHypernymPointer = by.getOutgoingEdges(BabelPointer.HYPERNYM);
    		System.out.println("Elementos en ANY_HYPERNYM para " + by.getMainSense(Language.EN).get().getFullLemma()
    							+ ": " + elementsInAnyHypernymPointer.size());
    		System.out.println("Elementos en HYPERNYM para " + by.getMainSense(Language.EN).get().getFullLemma()
    							+ ": " + elementsInHypernymPointer.size());
    		try {
    		System.out.println("Hypernym: " + by.getOutgoingEdges(BabelPointer.ANY_HYPERNYM).get(0).getBabelSynsetIDTarget().toString());
    		} catch (Exception e) {
    			System.out.println("No se ha podido conseguir el término.\n" + e.getMessage());
    		}
    		

	}

}
