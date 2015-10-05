/*
* TSP_GA.java
* Create a tour and evolve a solution
*/

package tsp;

import java.io.File;
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
        Population pop = new Population(50, true);
        System.out.println("Initial distance: " + pop.getFittest().getDistance());

        // Evolve population for 100 generations
        pop = GA.evolvePopulation(pop);
        for (int i = 0; i < 100000; i++) {
            pop = GA.evolvePopulation(pop);
        }

        // Print final results
        System.out.println("Finished");
        System.out.println("Final distance: " + pop.getFittest().getDistance());
        System.out.println("Solution:");
        System.out.println(pop.getFittest());
    }
}