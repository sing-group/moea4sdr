package cachear;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class CargarSynsetsCacheados {
	

	//Para leer el fichero que contiene el mapa de hiperónimos y esta en el disco
	public static Map<String , List<String>> leer () {
	   try{
		   //Poner aquí el nombre del fichero a cargar. Extensión ".map"
	        File toRead=new File("outputsyns_youtube_last_all.map");
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

    	 //Cargamos el fichero con los synsets y todos sus hiperónimos cacheados.
    	 Map<String, List<String>> mapOfHypernyms = new HashMap<String, List<String>>();
    	 mapOfHypernyms = leer();
    	 System.out.println("El mapa tiene cacheados los hiperónimos de " + mapOfHypernyms.size() + " synsets.");
    	 	 
    	
    }
}
