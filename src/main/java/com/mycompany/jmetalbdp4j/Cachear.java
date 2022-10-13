package com.mycompany.jmetalbdp4j;

import java.util.ArrayList;

import org.nlpa.util.BabelUtils;
import types.CachedBabelUtils;
public class Cachear {

	public static void main(String[] args) {
		CachedBabelUtils cachedBabelUtils = new CachedBabelUtils();
		ArrayList<String> synsetList = new ArrayList<String>();
		
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
		
		for (String s : synsetList) {
			cachedBabelUtils.addSynsetToCache(s, BabelUtils.getDefault().getAllHypernyms(s) );
		}	


		System.out.println("drug en el mapa: " + cachedBabelUtils.existsSynsetInMap("bn:00028872n"));
		System.out.println("Longitud lista." + synsetList.size());
		System.out.println("Viagra incluido en viagra?" + cachedBabelUtils.isSynsetFatherOf(synsetList.get(0), synsetList.get(0)));
		
		
		cachedBabelUtils.getCachedHypernym(synsetList.get(0), 2);
		
		System.out.println("Viagra en el mapa: " + cachedBabelUtils.existsSynsetInMap("bn:00071570n"));
		System.out.println("drug en el mapa: " + cachedBabelUtils.existsSynsetInMap("bn:00028872n"));
		
		System.out.println("Longitud lista." + synsetList.size());
	}

}
