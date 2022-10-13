package com.mycompany.jmetalbdp4j;

import org.nlpa.util.BabelUtils;

import it.uniroma1.lcl.babelnet.BabelNet;
import it.uniroma1.lcl.babelnet.BabelSynset;
import it.uniroma1.lcl.babelnet.BabelSynsetID;
import it.uniroma1.lcl.jlt.util.Language;

public class GetHypernymFromLevel {

	public static void main (String[] args) throws Exception {
			String hiperonimo;
			int niveles;
			
			
	        String synset="bn:00003095n";
	        BabelNet bn = BabelNet.getInstance();
    		BabelSynset by = bn.getSynset(new BabelSynsetID(synset));
    		
    		//Imprimimos el significado del synset
    		System.out.println("Significado del synset " + by.getID() + ": " + by.getMainSense(Language.EN).get().getFullLemma());
    		
    		niveles=3;
    		hiperonimo = BabelUtils.getDefault().getSynsetHypernymFromLevel(synset, niveles);
    		
    		bn = BabelNet.getInstance();
    		by = bn.getSynset(new BabelSynsetID(hiperonimo));
    		
    		//Imprimimos el significado del synset
    		System.out.println("Significado del synset " + by.getID() + ": " + by.getMainSense(Language.EN).get().getFullLemma());
    		
    		
    		
    		

	}

}
