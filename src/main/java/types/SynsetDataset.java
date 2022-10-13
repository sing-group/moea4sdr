/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package types;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bdp4j.types.Dataset;
import weka.core.Attribute;
import weka.core.Instances;

/**
 *
 * @author María Novo
 */
public class SynsetDataset extends Dataset {

    Instances dataset;

    public SynsetDataset(Dataset dataset) {
        super(dataset);
    }

    /**
     * Get a list of synsets in Dataset
     *
     * @return a list of synsets in Dataset
     */
    public List<String> getSynsets() {
        List<String> synsetsList = new ArrayList<>();
        List<String> attributes = this.getAttributes();
        for (String attribute : attributes) {
            if (attribute.contains("bn:")) {
                synsetsList.add(attribute);
            }
        }
        return synsetsList;
    }

    /**
     * Replace synsets list with its hyperonym
     *
     * @param hyperonymList Hiperonyms list to replace the original synsets
     * @return Dataset with hyperonyms instead of original synsets
     */
    public Dataset replaceSynsetWithHyperonym(Map<String, String> hyperonymList) {
        Instances instances = this.dataset;

        for (Map.Entry<String, String> entry : hyperonymList.entrySet()) {
            String oldValue = entry.getKey();
            String newValue = entry.getValue();
            Attribute att = instances.attribute(oldValue);
            instances.renameAttribute(att, newValue);
        }

        return this;
    }

    /**
     * Delete all attributes from Dataset but synstetIds and target attributes
     *
     * @return Dataset only with synsetIds and target attributes
     */
    public Dataset getOnlySynsetIdColumns() {

        Instances instances = this.dataset;
        List<String> attributesToDelete = new ArrayList<>();
        List<String> attributes = this.getAttributes();
        for (String attribute : attributes) {
            if (!attribute.contains("bn:") && !attribute.equals("target")) {
                attributesToDelete.add(attribute);
            }
        }
        return this.deleteAttributeColumns(attributesToDelete);
    }
}
