package tsp.search;

import tsp.Tour;
import tsp.mutator.Mutator;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by nweatherburn on 12/10/15.
 */
public class BestImprovementSearch implements Search {

    private static final int NUM_TOURS = 100;

    public Tour performLocalSearch(final Tour originalTour, Mutator mutator) {
        Set<Tour> tours = new HashSet<>();
        tours.add(originalTour);

        for (int i = 0; i < NUM_TOURS; i++) {
            Tour tour = new Tour(originalTour.getTour());
            mutator.mutate(tour);

            tours.add(tour);
        }

        return getBestTour(tours);
    }

    private Tour getBestTour(Set<Tour> tours) {
        Tour best = tours.iterator().next();
        for (Tour tour : tours) {
            if (tour.getDistance() < best.getDistance()) {
                best = tour;
            }
        }

        return best;
    }
}
