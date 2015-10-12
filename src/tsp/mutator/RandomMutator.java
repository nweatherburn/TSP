package tsp.mutator;

import tsp.City;
import tsp.Tour;
import tsp.search.Search;

/**
 * Created by nweatherburn on 12/10/15.
 */
public class RandomMutator implements Mutator {

    private static final double mutationRate = 0.015;

    // Mutate a tour using swap mutation
    public void mutate(Tour tour) {
        // Loop through tour cities
        for(int tourPos1=0; tourPos1 < tour.tourSize(); tourPos1++){
            // Apply mutation rate
            if(Math.random() < mutationRate){
                // Get a second random position in the tour
                int tourPos2 = (int) (tour.tourSize() * Math.random());

                // Get the cities at target position in tour
                City city1 = tour.getCity(tourPos1);
                City city2 = tour.getCity(tourPos2);

                // Swap them around
                tour.setCity(tourPos2, city1);
                tour.setCity(tourPos1, city2);
            }
        }
    }
}
