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
import java.util.*;

public class TSP_GA {

    public static String QATAR_DATA = "data/qatar.txt";

    public static void main(String[] args) {

        Set<DoublePoint> points = new HashSet<>();

        try {
            File file = new File(QATAR_DATA);
            Scanner scan = new Scanner(file);

            while (scan.hasNext()) {
                double xPoint = scan.nextDouble();
                double yPoint = scan.nextDouble();

                points.add(new DoublePoint(new double[] {xPoint, yPoint}));
                City city = new City((int) xPoint, (int) yPoint);
                TourManager.addCity(city);
            }

        } catch (Exception e) {
            System.err.println(e.toString());
        }

        // Initialize population
        final int generations = 100;
        final int populationSize = 50;

//        Population pop = new Population(populationSize, true);
        Population pop = createInitialPopulation(points, populationSize);
        System.out.println("Initial distance: " + pop.getFittest().getDistance());
        System.out.println("Optimal tour distance: " + 9352);


        compareSearchMethods(pop, generations);
//        compareSubtourLength(pop, generations);

        System.out.println();
        System.out.println("Finished");
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

    private static void compareSearchMethods(Population population, int generations) {
//        Mutator[] mutators = { new LinKernighanMutator(), new RandomMutator(), new SwapMutator()};
//        Search[] searches = { new FirstImprovementSearch(), new FirstChangeSearch(), new BestImprovementSearch(), new DeepSearch()};

        Mutator[] mutators = {new RandomMutator(), new SwapMutator()};
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

        Tour fittest = pop.getFittest();
        for (int i = 0; i < fittest.tourSize(); i++) {
            for (int j = i + 1; j < fittest.tourSize(); j++) {
                if (fittest.getCity(i) == fittest.getCity(j)) {
                    System.out.println("OOPS");
                } else if (fittest.getCity(i).getX() == fittest.getCity(j).getX() && fittest.getCity(i).getY() == fittest.getCity(j).getY()) {
                    System.out.println("AHA!");
                }
            }
        }

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