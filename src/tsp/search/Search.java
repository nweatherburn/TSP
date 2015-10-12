package tsp.search;

import tsp.Tour;
import tsp.mutator.Mutator;

/**
 * Created by nweatherburn on 12/10/15.
 */
public interface Search {

    /**
     * Performs a local search around the given tour and returns a possibly mutated version of the original tour.
     *
     * @param tour
     * @return
     */
    public Tour performLocalSearch(Tour tour, Mutator mutator);
}
