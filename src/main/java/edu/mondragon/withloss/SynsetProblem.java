package edu.mondragon.withloss;

import edu.mondragon.types.CachedBabelUtils;

import org.bdp4j.types.Dataset;
import org.uma.jmetal.problem.impl.AbstractIntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class SynsetProblem extends AbstractIntegerProblem {

    private CachedBabelUtils cachedBabelUtils;
    private Dataset clonedDataset;
    DatosOriginales datosOriginales;

    public SynsetProblem() {
        try {

            datosOriginales = new DatosOriginales();

            cachedBabelUtils = datosOriginales.getCachedBabelUtils();
            clonedDataset = datosOriginales.getClonedDataset();
            //Creamos una lista sólo con los synsets del dataset sin la columna de la clase
            setNumberOfVariables(clonedDataset.filterColumnNames("^bn:").size());
            setNumberOfObjectives(3);
            //List<Integer> upperLimits=new ArrayList<>();
            //List<Integer> lowerLimits=new ArrayList<>();
            //for (int i=0;i<this.getNumberOfObjectives();i++){
            //    upperLimits.add(1);
            //    lowerLimits.add(0);
            //}
            
            //this.setUpperLimit(upperLimits);
            //this.setLowerLimit(lowerLimits);
            setName("SynsetProblem");
            System.out.println("Tamaño cache: " + cachedBabelUtils.getMapOfHypernyms().size());
            System.out.println("Tamaño dataset: " + clonedDataset.filterColumnNames("^bn:").size());


            List<Integer> lowerLimit = new ArrayList<>(/*getNumberOfVariables()*/);
            List<Integer> upperLimit = new ArrayList<>(/*getNumberOfVariables()*/);

            for (int i = 0; i < getNumberOfVariables(); i++) {
                lowerLimit.add(-1); //########## Valor minimo del cromosoma
                upperLimit.add(2); //########### Valor máximo del cromosoma
                }

            setLowerLimit(lowerLimit);
            setUpperLimit(upperLimit);
            System.out.println("SynsetProblem terminado.");
        } catch (Exception ex) {
            System.out.println("SynsetProblem: " + ex.getMessage());
        }
    }

    public void evaluate(IntegerSolution solution) {

        List<Integer> chromosome = new ArrayList<Integer>();

        for (int i = 0; i < solution.getNumberOfVariables(); i++) {
            chromosome.add(solution.getVariableValue(i));
        }
        Result resultado = new Result(cachedBabelUtils, clonedDataset.clone(), chromosome);
        Map<String, Integer> resultados;
        resultados = resultado.generateResult();
        solution.setObjective(0, ((double)(resultados.get("fp"))) / ((double)(datosOriginales.getNumHam())));
        solution.setObjective(1, ((double)(resultados.get("fn"))) / ((double)(datosOriginales.getNumSpam())));
        solution.setObjective(2, ((double)(resultados.get("dim"))) / ((double)(datosOriginales.getNumFeatures())-1d));
        //System.out.print(".");
        //System.out.println("FP: " + resultados.get("fp") + " FN: " + resultados.get("fn") + " DIM: " + resultados.get("dim"));
        //System.out.println("nuham: " + datosOriginales.getNumHam() + " numspam: " + datosOriginales.getNumSpam() + " features: " + (datosOriginales.getNumFeatures()-1));
        //System.out.println("FP: " + ((double)(resultados.get("fp")) / (double)(datosOriginales.getNumHam())) + " FN: " + ((double)(resultados.get("fn"))/ (double)(datosOriginales.getNumSpam())) + " DIM: " + ((double)(resultados.get("dim"))/((double)(datosOriginales.getNumFeatures())-1d)));
    }
}
