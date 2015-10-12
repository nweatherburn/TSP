package tsp.search;

import tsp.Tour;
import tsp.mutator.Mutator;

/**
 * Created by nweatherburn on 12/10/15.
 */
public class FirstChangeSearch implements Search {

    public Tour performLocalSearch(Tour tour, Mutator mutator) {
        mutator.mutate(tour);

        return tour;
    }
}
