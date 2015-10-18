package tsp.search;

import tsp.Tour;
import tsp.mutator.Mutator;

/**
 * Created by Matthew on 18/10/2015.
 */
public class RepeatBestImprovementSearch extends BestImprovementSearch {

    private final int NUM_ITERATIONS = 3;

    public Tour performLocalSearch(final Tour originalTour, Mutator mutator) {
        Tour tour = originalTour;
        for (int i=0;i<NUM_ITERATIONS;i++){
            tour =  super.performLocalSearch(tour,mutator);
        }
        return tour;
    }
}
