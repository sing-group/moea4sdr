package com.mycompany.jmetalbdp4j;

import java.util.ArrayList;
import java.util.List;

import org.nlpa.util.BabelUtils;

import it.uniroma1.lcl.babelnet.BabelNet;
import it.uniroma1.lcl.babelnet.BabelSynset;
import it.uniroma1.lcl.babelnet.BabelSynsetID;
import it.uniroma1.lcl.babelnet.BabelSynsetRelation;
import it.uniroma1.lcl.babelnet.data.BabelPointer;
import it.uniroma1.lcl.jlt.util.Language;

public class HiperonimosDeUnSynset {

	public static void main(String[] args) {
		ArrayList<String> synsetList = new ArrayList<String>();

		synsetList.add("bn:00071570n"); //Viagra o sildenafil
		synsetList.add("bn:00019048n");//cialis o tadalafil
		synsetList.add("bn:00015556n");//Rome Italian capital
		synsetList.add("bn:00015620n");//Madrid Spain capital
		synsetList.add("bn:00007309n");//Car vehicle
		synsetList.add("bn:00010248n");//Bike vehicle
		synsetList.add("bn:00008364n");//bank financial institution
		synsetList.add("bn:00044994n");//House home
		synsetList.add("bn:00104407a");//hungry
		synsetList.add("bn:00090460v");//search look for
		synsetList.add("bn:00114671r");//carefylly adverb

		BabelNet bn = BabelNet.getInstance();
		BabelUtils babel;
		babel = BabelUtils.getDefault();
		BabelSynset by;
		String hiperonimo;
		int niveles=20;

		for (int i=0; i<=synsetList.size()-1; i++)
		{
			by = bn.getSynset(new BabelSynsetID(synsetList.get(i)));
			System.out.println("Significado del synset " + by.getID() + ": " + by.getMainSense(Language.EN).get().getFullLemma());
			for (int j=1; j<=niveles; j++)
			{
				hiperonimo = babel.getSynsetHypernymFromLevel(synsetList.get(i), j);
				by = bn.getSynset(new BabelSynsetID(hiperonimo));
				System.out.print("Hiperonimo " + j + ": " + by.getID().toString() + " " + by.getMainSense(Language.EN).get().getFullLemma() + "\t");
			}
			System.out.println();
			System.out.println();
		}


	}

}
