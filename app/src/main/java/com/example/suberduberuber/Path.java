package com.example.suberduberuber;

public class Path {

    private static final double COST_FACTOR = 0.05;
    private Location startLocation;
    private Location destination;
    private double estimatedFare;

    /**
     * Path Object Constructor
     * @param startLocation
     * The Starting Location for the Path (Location Object)
     * @param destination
     * The Destination Location for the Path (Location Object)
     */
    public Path(Location startLocation, Location destination) {
        this.startLocation = startLocation;
        this.destination = destination;
        this.generateEstimatedFare();
    }

    /**
     * Generates Estimated Fare for the Path
     * Uses distance between two locations and a set COST_FACTOR
     */
    public void generateEstimatedFare() {
        double distance = startLocation.getDistanceBetween(destination);
        this.estimatedFare = distance * COST_FACTOR;
    }

    /**
     * Getter for Estimated Fare
     * @return
     * Returns a double Containing the Estimated Fare for the Path
     */
    public double getEstimatedFare() {
        return estimatedFare;
    }

    /**
     * Setter for Destination Location
     * @param destination
     * Location Object to be set as the Destination Location of the Path
     */
    public void setDestination(Location destination) {
        this.destination = destination;
        this.generateEstimatedFare();
    }

    /**
     * Setter for Start Location
     * @param startLocation
     * Location Object to be set as the Start Location of the Path
     */
    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
        this.generateEstimatedFare();
    }

    /**
     * Getter for Destination Location
     * @return
     * Returns a Location Object which is the Destination Location for the Path
     */
    public Location getDestination() {
        return destination;
    }

    /**
     * Getter for the Start Location
     * @return
     * Retunrs as Location Object which is the Start Location for the Path
     */
    public Location getStartLocation() {
        return startLocation;
    }
}
