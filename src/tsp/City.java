/*
* City.java
* Models a city
*/

package tsp;

public class City {
    private final double x;
    private final double y;

    // Constructs a randomly placed city
    public City(){
        this.x = Math.random() * 200;
        this.y = Math.random() * 200;
    }

    // Constructs a city at chosen x, y location
    public City(double x, double y){
        this.x = x;
        this.y = y;
    }

    // Gets city's x coordinate
    public double getX(){
        return this.x;
    }

    // Gets city's y coordinate
    public double getY(){
        return this.y;
    }

    // Gets the distance to given city
    public double distanceTo(City city) {
        double xDistance = Math.abs(getX() - city.getX());
        double yDistance = Math.abs(getY() - city.getY());

        double distance = Math.sqrt( (xDistance*xDistance) + (yDistance*yDistance) );

        return Math.round(distance);
    }

    @Override
    public String toString(){
        return getX()+", "+getY();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        City city = (City) o;

        if (x != city.x) return false;
        return y == city.y;

    }

    @Override
    public int hashCode() {
        double result = x;
        result = 31 * result + y;
        return (int) result;
    }
}