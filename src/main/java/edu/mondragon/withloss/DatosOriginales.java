/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mondragon.withloss;

import edu.mondragon.types.CachedBabelUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bdp4j.dataset.CSVDatasetReader;
import org.bdp4j.transformers.attribute.Enum2IntTransformer;
import org.bdp4j.types.Dataset;
import org.bdp4j.types.Transformer;
import weka.core.Attribute;
import weka.core.Instance;

/**
 *
 * @author María Novo
 */
//Cargamos los datos originales de los ficheros para no tener que leerlos más que una vez del disco.
public class DatosOriginales {

    private CachedBabelUtils cachedBabelUtils;
    private Dataset clonedDataset;
    private List<String> synsetListOriginal;
    private int numHam = 0;
    private int numSpam = 0;
    private int numFeatures = 0;

    public DatosOriginales() {
        loadSynsetCache();
        cargarDataset();
    }

    public int getNumHam() {
        return numHam;
    }

    
    public int getNumSpam() {
        return numSpam;
    }
    
    
    public int getNumFeatures() {
        return numFeatures;
    }
    
    public void loadSynsetCache() {
//        File toRead = new File("sms_all.map");
        File toRead = new File("ultimo_outputsyns_youtube_cache.map");
        //File toRead=new File("outputsyns_medium_manopla_all.map");
        try (FileInputStream fis = new FileInputStream(toRead);
                ObjectInputStream ois = new ObjectInputStream(fis);) {
            cachedBabelUtils = new CachedBabelUtils();
            HashMap<String, List<String>> mapInFile = (HashMap<String, List<String>>) ois.readObject();
            cachedBabelUtils.setMapOfHypernyms(mapInFile);
        } catch (Exception e) {
            System.out.println("6: Exception in file read.");
        }
    }

    public void cargarDataset() {
        //Creamos el mapa para la lista de transformadores para sustituir ham por 0, spam por 1
        Map<String, Integer> transformList = new HashMap<>();
        transformList.put("ham", 0);
        transformList.put("spam", 1);

        //Creamos el mapa para aplicar los transformadores
        Map<String, Transformer> transformersList = new HashMap<>();

        transformersList.put("target", new Enum2IntTransformer(transformList));

        //Cargamos el fichero csv.
        //String filePath = "outputsyns_medium_manopla.csv";
        String filePath = "ultimo_outputsyns_100.csv";

        // Aplicamos los transformadores y creamos el dataset con los cambios de los transformadores
        CSVDatasetReader filedataset = new CSVDatasetReader(filePath, transformersList);
        Dataset originalDataset = filedataset.loadFile();

        //calculateAttributes(originalDataset);
        //Clonamos el dataset para empezar a trabajar con la copia
        clonedDataset = originalDataset.clone();

        //Dejamos el dataset clonado solo con los synsets y el campo target
        clonedDataset.filterColumns("^bn:|target");
        calculateAttributes(clonedDataset);
        //Creamos una lista sólo con los synsets del dataset sin la columna de la clase
        synsetListOriginal = clonedDataset.filterColumnNames("^bn:");
    }

    private void calculateAttributes(Dataset originalDataset) {
        this.numFeatures = originalDataset.getAttributes().size()-1;
        //System.out.println("OriginalDataset.getAtributes:" + originalDataset.getAttributes());
    	//this.numFeatures = 1685;
        for (Instance instance : originalDataset.getInstances()) {
            Attribute att = instance.attribute(numFeatures); // Nos quedamos con el último attributo, que debería ser el target
            if (att.name().equals("target")) {
                if (instance.value(att) == 0) {
                    this.numHam++;
                } else {
                    this.numSpam++;
                }
            } else {
                System.out.println("ERROR: target debería ser el último atributo");
            }
        }
        System.out.println("numFeatures: " + this.numFeatures + " - numHam: " + this.numHam + " - numSpam: " + this.numSpam);
    }

    public CachedBabelUtils getCachedBabelUtils() {
        return cachedBabelUtils;
    }

    public Dataset getClonedDataset() {
        return clonedDataset;
    }

    public List<String> getSynsetListOriginal() {
        return synsetListOriginal;
    }

}
