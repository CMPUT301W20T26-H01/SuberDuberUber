package com.example.suberduberuber.Models;

public class Path {

    private static final double COST_FACTOR = 0.05;
    private Location startLocation;
    private Location destination;
    private double estimatedFare;

    // Empty public constructor needed by Cloud Firestore for serializability
    public Path() { }

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

    public Location getStartLocation() {
        return this.startLocation;
    }

    public Location getDestination() {
        return this.destination;
    }

}
