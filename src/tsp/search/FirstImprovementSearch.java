package tsp.search;

import tsp.Tour;
import tsp.mutator.Mutator;

/**
 * Created by nweatherburn on 12/10/15.
 */
public class FirstImprovementSearch implements Search {

    private static final int MAX_SEARCHES = 100;

    public Tour performLocalSearch(final Tour originalTour, Mutator mutator) {
        // Ranges from 0 -> 1
        double originalDistance = originalTour.getDistance();

        for (int i = 0; i < MAX_SEARCHES; i++) {
            Tour tour = new Tour(originalTour.getTour());
            mutator.mutate(tour);

            if (tour.getDistance() < originalDistance) {
                return tour;
            }
        }

        return originalTour;
    }
}
