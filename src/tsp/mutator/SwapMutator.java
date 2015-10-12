package tsp.mutator;

import tsp.City;
import tsp.Tour;

/**
 * Created by nweatherburn on 12/10/15.
 */
public class SwapMutator implements Mutator {

    private static final int NUMBER_OF_SWAPS = 1;

    public void mutate(Tour tour) {
        for (int i = 0; i < NUMBER_OF_SWAPS; i++) {
            swap(tour);
        }
    }

    private void swap(Tour tour) {
        int firstPosition = (int) (Math.random() * tour.tourSize());
        int secondPosition = (int) (Math.random() * tour.tourSize());
        while (firstPosition == secondPosition) {
            secondPosition = (int) (Math.random() * tour.tourSize());
        }

        // Swap positions
        City temp = tour.getCity(firstPosition);
        tour.setCity(firstPosition, tour.getCity(secondPosition));
        tour.setCity(secondPosition, temp);
    }
}
