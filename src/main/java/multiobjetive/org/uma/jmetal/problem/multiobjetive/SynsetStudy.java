package multiobjetive.org.uma.jmetal.problem.multiobjetive;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.impl.crossover.IntegerSBXCrossover;
import org.uma.jmetal.operator.impl.mutation.IntegerPolynomialMutation;
import org.uma.jmetal.qualityindicator.impl.Spread;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.experiment.Experiment;
import org.uma.jmetal.util.experiment.ExperimentBuilder;
import org.uma.jmetal.util.experiment.component.ComputeQualityIndicators;
import org.uma.jmetal.util.experiment.component.ExecuteAlgorithms;
import org.uma.jmetal.util.experiment.component.GenerateBoxplotsWithR;
import org.uma.jmetal.util.experiment.component.GenerateLatexTablesWithStatistics;
import org.uma.jmetal.util.experiment.component.GenerateReferenceParetoFront;
import org.uma.jmetal.util.experiment.util.ExperimentAlgorithm;
import org.uma.jmetal.util.experiment.util.ExperimentProblem;

public class SynsetStudy {

    private static final int INDEPENDENT_RUNS = 25; //Hay que poner 25

    public static void main(String[] args) throws IOException {
        //Añadimos codigo para una recogida de tiempos
        long inicio = System.currentTimeMillis();

        String experimentBaseDirectory = "experimentBaseDirectory";

        List<ExperimentProblem<IntegerSolution>> problemList = new ArrayList<>();
        problemList.add(new ExperimentProblem<>(new SynsetProblem()));

        List<ExperimentAlgorithm<IntegerSolution, List<IntegerSolution>>> algorithmList
                = configureAlgorithmList(problemList);

        Experiment<IntegerSolution, List<IntegerSolution>> experiment = new ExperimentBuilder<IntegerSolution, List<IntegerSolution>>("SynsetStudy")
                .setAlgorithmList(algorithmList)
                .setProblemList(problemList)
                .setExperimentBaseDirectory(experimentBaseDirectory)
                .setOutputParetoFrontFileName("FUN")
                .setOutputParetoSetFileName("VAR")
                .setReferenceFrontDirectory(experimentBaseDirectory + "/SynsetStudy/pareto_fronts")
                .setIndicatorList(Arrays.asList(
                        new Spread<IntegerSolution>(),
                        new PISAHypervolume<IntegerSolution>()))
                .setIndependentRuns(INDEPENDENT_RUNS)
                .setNumberOfCores(25)
                .build();

        new ExecuteAlgorithms<>(experiment).run();
        new GenerateReferenceParetoFront(experiment).run();
        new ComputeQualityIndicators<>(experiment).run();
        new GenerateLatexTablesWithStatistics(experiment).run();
        new GenerateBoxplotsWithR<>(experiment).setRows(3).setColumns(3).run();

        long fin = System.currentTimeMillis();
        double tiempo = (double) ((fin - inicio) / 60000);
        try {
            PrintWriter out = new PrintWriter("tiempo.txt");

            out.println("Minutos: " + tiempo);
            out.close();
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println(tiempo + " minutos");
    }

    static List<ExperimentAlgorithm<IntegerSolution, List<IntegerSolution>>> configureAlgorithmList(
            List<ExperimentProblem<IntegerSolution>> problemList) {
        List<ExperimentAlgorithm<IntegerSolution, List<IntegerSolution>>> algorithms = new ArrayList<ExperimentAlgorithm<IntegerSolution, List<IntegerSolution>>>();

        for (int run = 0; run < INDEPENDENT_RUNS; run++) {
            for (int i = 0; i < problemList.size(); i++) {
                Algorithm<List<IntegerSolution>> algorithm = new NSGAIIBuilder<IntegerSolution>(
                        problemList.get(i).getProblem(),
                        new IntegerSBXCrossover(1.0, 5),
                        new IntegerPolynomialMutation(1.0 / problemList.get(i).getProblem().getNumberOfVariables(), 10.0))
                        .setMaxEvaluations(25000) //Aquí tiene que haber 25000
                        .setPopulationSize(100)
                        .build();
                algorithms.add(new ExperimentAlgorithm<IntegerSolution, List<IntegerSolution>>(algorithm, "NSGAII", problemList.get(i), run));
            }
        }
        return algorithms;
    }
}
