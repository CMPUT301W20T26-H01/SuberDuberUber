package com.example.suberduberuber;

public class Path {
<<<<<<< HEAD

=======
    private static final double COST_FACTOR = 0.05;
    private Location startLocation;
    private Location destination;
    private double estimatedFare;

    public Path(Location startLocation, Location destination) {
        this.startLocation = startLocation;
        this.destination = destination;
        this.generateEstimatedFare();
    }

    public void generateEstimatedFare() {
        double distance = startLocation.getDistanceBetween(destination);
        this.estimatedFare = distance * COST_FACTOR;
    }

    public double getEstimatedFare() {
        return estimatedFare;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
        this.generateEstimatedFare();
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
        this.generateEstimatedFare();
    }
>>>>>>> fb98ccea1fd9ea1fefaeaabd8b4ac8acc5f230fd
}
