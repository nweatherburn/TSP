/*
* TSP_GA.java
* Create a tour and evolve a solution
*/

package tsp;

import tsp.mutator.LinKernighanMutator;
import tsp.mutator.Mutator;
import tsp.mutator.RandomMutator;
import tsp.mutator.SwapMutator;
import tsp.search.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class TSP_GA {

    public static String QATAR_DATA = "data/qatar.txt";

    public static void main(String[] args) {
        try {
            File file = new File(QATAR_DATA);
            Scanner scan = new Scanner(file);

            while (scan.hasNext()) {
                double xPoint = scan.nextDouble();
                double yPoint = scan.nextDouble();

                City city = new City((int) xPoint, (int) yPoint);
                TourManager.addCity(city);
            }

        } catch (Exception e) {
            System.err.println(e.toString());
        }

        // Initialize population
        final int generations = 500;
        final int populationSize = 50;

        Population pop = new Population(populationSize, true);
        System.out.println("Initial distance: " + pop.getFittest().getDistance());
        System.out.println("Optimal tour distance: " + 9352);

        Mutator[] mutators = { new SwapMutator(), new LinKernighanMutator(), new RandomMutator()};
        Search[] searches = { new FirstImprovementSearch(), new FirstChangeSearch(), new BestImprovementSearch(), new DeepSearch()};

        for (Mutator mutator : mutators) {
            System.out.println();
            System.out.println(mutator.getClass().getSimpleName());
            for (Search search : searches) {
                evolvePopulation(pop, search, mutator, generations);
            }
        }

        System.out.println();
        System.out.println("Finished");
    }
    private static void evolvePopulation(Population pop, Search search, Mutator mutator, int generations) {

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < generations; i++) {
            pop = GA.evolvePopulation(pop, search, mutator);
        }
        long endTime = System.currentTimeMillis();

        // Print final results
        System.out.println(search.getClass().getSimpleName() + " distance: " + pop.getFittest().getDistance());
        System.out.println(search.getClass().getSimpleName() + " running time: " + (endTime - startTime) + "ms");
    }
}