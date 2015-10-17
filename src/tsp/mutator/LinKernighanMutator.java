package tsp.mutator;

import tsp.City;
import tsp.Tour;

/**
 * Created by nweatherburn on 12/10/15.
 */
public class LinKernighanMutator implements Mutator {

    private static int SUBTOUR_LENGTH = 25;

    public void mutate(Tour tour) {
        int position = (int) (Math.random() * (tour.tourSize() - SUBTOUR_LENGTH));

        swapSubTour(tour, position, SUBTOUR_LENGTH);
    }

    //Swaps a section of the tour
    private void swapSubTour(Tour tour, int position, int length) {
        City holder;
        for (int i = 0; i < length / 2; i++) {
            //Save the right hand city
            holder = tour.getCity(position + length - i - 1);

            tour.setCity(position + length - i - 1, tour.getCity(position + i));
            tour.setCity(position + i, holder);
        }
    }

    public void setSubtourLength(int subtourLength) {
        SUBTOUR_LENGTH = subtourLength;
    }
}
