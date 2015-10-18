/*
* TSP_GA.java
* Create a tour and evolve a solution
*/

package tsp;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import tsp.mutator.LinKernighanMutator;
import tsp.mutator.Mutator;
import tsp.mutator.RandomMutator;
import tsp.mutator.SwapMutator;
import tsp.search.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class TSP_GA {

    public static String QATAR_DATA = "data/qatar.txt";
    public static String LIN_KERNIGHAN_BY_GENERATION = "data/LINK_500_1000.csv";
    public static String RANDOM_BY_GENERATION = "data/RANDOM_1000_1000.csv";
    public static String SWAP_BY_GENERATION =  "data/SWAP_500_100.csv";

    public static void main(String[] args) {

        Set<DoublePoint> points = new HashSet<>();

        try {
            File file = new File(QATAR_DATA);
            Scanner scan = new Scanner(file);

            while (scan.hasNext()) {
                double xPoint = scan.nextDouble();
                double yPoint = scan.nextDouble();

                points.add(new DoublePoint(new double[] {xPoint, yPoint}));
                City city = new City(xPoint, yPoint);
                TourManager.addCity(city);
            }

        } catch (Exception e) {
            System.err.println(e.toString());
        }

        // Initialize population
        final int populationSize = 500;
        final int generations = 1000;
        final int repetitions = 5;

//        Population pop = new Population(populationSize, true);
        final Population pop = createInitialPopulation(points, populationSize);
        System.out.println("Initial distance: " + pop.getFittest().getDistance());
        System.out.println("Optimal tour distance: " + 9352);

        runTest(pop, new BestImprovementSearch(), repetitions, generations);

        System.out.println();
        System.out.println("Finished");
    }

    /**
     * Run N repetitions of a particular search technique. Uses a RandomMutator.
     * It expects a directory named "data/{SearchTechniqueClassName}/" to exist
     *
     * @param pop
     * @param search
     * @param repetitions
     * @param generations
     */
    private static void runTest(final Population pop, final Search search, final int repetitions, final int generations) {
        for (int i = 0; i < repetitions; i += 1) {
            final int runNumber = i;
            new Thread() {
                public void run() {
                    String filename = "data/" +
                            search.getClass().getSimpleName() +
                            "/P" + pop.populationSize() + "_G" + generations + "_" + runNumber + ".csv";
                    testOneSearchTechnique(pop, search, filename, generations);
                    System.out.println(filename + " done");
                }
            }.start();
        }
    }

    private static Population createInitialPopulation(Set<DoublePoint> points, int populationSize) {
        DBSCANClusterer<DoublePoint> clusterer = new DBSCANClusterer<DoublePoint>(50, 0);
        List<Cluster<DoublePoint>> clusters = clusterer.cluster(points);

        Population population = new Population(populationSize, false);

        for (int i = 0; i < population.populationSize(); i++) {

            Tour tour = new Tour();
            int j = 0;

            for (Cluster cluster : clusters) {

                List<DoublePoint> clusterPoints = cluster.getPoints();
                Collections.shuffle(clusterPoints);

                for (DoublePoint clusterPoint : clusterPoints) {
                    double[] point = clusterPoint.getPoint();
                    tour.setCity(j, new City((int) point[0], (int) point[1]));

                    j++;
                }
            }
            population.setTour(i, tour);
            
        }

        return population;
    }

    private static void testOneSearchTechnique(final Population population, Search search, String filename, int generations) {
        Mutator mutator = new RandomMutator();

        try {
            PrintWriter pw = new PrintWriter(new File(filename));
            pw.print(",");
            pw.print(search.getClass().getSimpleName());
            pw.println();

            pw.print("0");
            pw.print("," + population.getFittest().getDistance());
            pw.println();

            for (int i = 1; i <= generations; i++) {
                pw.print(i);

                Population evolved = GA.evolvePopulation(population, search, mutator);
                pw.print("," + evolved.getFittest().getDistance());
                pw.println();
            }

            pw.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void compareSearchMethods(Population population, int generations) {
        Mutator[] mutators = {new LinKernighanMutator(), new RandomMutator(), new SwapMutator()};
        Search[] searches = { new FirstImprovementSearch(), new FirstChangeSearch(), new BestImprovementSearch()};

        // This code currently prints out tabs in a format that can be pasted into Excel
        for (Search search: searches) {
            System.out.print("\t" + search.getClass().getSimpleName());
        }

        for (Mutator mutator : mutators) {
            System.out.println();
            System.out.print(mutator.getClass().getSimpleName());
            for (Search search : searches) {
                EvolutionResult evolutionResult = evolvePopulation(population, search, mutator, generations);
                System.out.print("\t" + evolutionResult.distance);
            }
        }
    }

    private static void compareSubtourLength(Population population, int generations) {
        LinKernighanMutator mutator = new LinKernighanMutator();
        Search firstImprovementSearch = new FirstImprovementSearch();
        Search bestImprovementSearch = new BestImprovementSearch();

        for (int subtourLength = 1; subtourLength <= 2; subtourLength += 1) {
            mutator.setSubtourLength(subtourLength);
            int firstImprovementDistance = evolvePopulation(population, firstImprovementSearch, mutator, generations).distance;
            int bestImprovementDistance = evolvePopulation(population, bestImprovementSearch, mutator, generations).distance;
            System.out.println(subtourLength + "\t" + firstImprovementDistance + "\t" + bestImprovementDistance);
        }
    }

    private static EvolutionResult evolvePopulation(Population pop, Search search, Mutator mutator, int generations) {

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < generations; i++) {
            pop = GA.evolvePopulation(pop, search, mutator);
        }
        long endTime = System.currentTimeMillis();

        EvolutionResult evolutionResult = new EvolutionResult(pop.getFittest().getDistance(), endTime - startTime);

//        System.out.println();
//        System.out.println(pop.getFittest());

//        System.out.println(search.getClass().getSimpleName() + " distance: " + evolutionResult.distance);
//        System.out.println(search.getClass().getSimpleName() + " running time: " + evolutionResult.runningTime + "ms");

//        Tour fittest = pop.getFittest();
//        for (int i = 0; i < fittest.tourSize(); i++) {
//            for (int j = i + 1; j < fittest.tourSize(); j++) {
//                if (fittest.getCity(i) == fittest.getCity(j)) {
//                    System.out.println("OOPS");
//                } else if (fittest.getCity(i).getX() == fittest.getCity(j).getX() && fittest.getCity(i).getY() == fittest.getCity(j).getY()) {
//                    System.out.println("AHA!");
//                }
//            }
//        }

        return evolutionResult;
    }

    private static class EvolutionResult {
        int distance;
        long runningTime;

        public EvolutionResult(int distance, long runningTime) {
            this.distance = distance;
            this.runningTime = runningTime;
        }
    }
}