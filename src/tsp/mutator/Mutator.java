package tsp.mutator;

import tsp.Tour;

/**
 * Created by nweatherburn on 12/10/15.
 */
public interface Mutator {

    /**
     * Mutates the given tour
     *
     * @param tour
     * @return
     */
    public void mutate(Tour tour);
}
