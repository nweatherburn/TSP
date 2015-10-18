package tsp.search;

import tsp.Tour;
import tsp.mutator.Mutator;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by nweatherburn on 12/10/15.
 */
public class DeepSearch implements Search {

    private final int NUMBER_OF_MUTATIONS;
    private final int SEARCH_DEPTH;

    public DeepSearch(int numMutations, int searchDepth) {
        this.NUMBER_OF_MUTATIONS = numMutations;
        this.SEARCH_DEPTH = searchDepth;
    }

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
