package tsp.search;

import tsp.Tour;
import tsp.mutator.Mutator;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by nweatherburn on 12/10/15.
 */
public class DeepSearch implements Search {

    private static final int NUMBER_OF_MUTATIONS = 3;
    private static final int SEARCH_DEPTH = 4;

    /*
     *
     * Depth      Number of Nodes
     *   4                1
     *   3                3
     *   2                9
     *   1                27
     *   0                81
     *
     *   Total:           121
     */

    public Tour performLocalSearch(final Tour originalTour, Mutator mutator) {
        return deepMutation(originalTour, mutator, SEARCH_DEPTH);
    }

    public Tour deepMutation(final Tour originalTour, Mutator mutator, int depth) {
        Set<Tour> tours = new HashSet<>();
        tours.add(originalTour);

        for (int i = 0; i < NUMBER_OF_MUTATIONS; i++) {
            Tour tour = new Tour(originalTour.getTour());
            mutator.mutate(tour);

            if (depth > 0) {
                tour = deepMutation(tour, mutator, depth - 1);
            }

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
