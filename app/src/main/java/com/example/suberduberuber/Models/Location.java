package com.example.suberduberuber.Models;

public class Location {
    private String coordinate;
    private String locationName;
    private String address;

    // Empty public constructor needed by Cloud Firestore for serializability
    public Location() { }

    /**
     * Constructor for Location Object
     * @param coordinate
     * String Representation of Coordinates
     * @param locationName
     * String Representation of Location Name
     * @param address
     * String Representation of Address
     */
    public Location(String coordinate, String locationName, String address) {
        this.coordinate = coordinate;
        this. locationName = locationName;
        this. address = address;
    }

    /**
     * Getter for Address
     * @return
     * Returns a String storing Address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Getter for Coordinates
     * @return
     * Returns a String storing Coordinates
     */
    public String getCoordinate() {
        return coordinate;
    }

    /**
     * Getter for Location Name
     * @return
     * Returns a String storing Location Name
     */
    public String getLocationName() {
        return locationName;
    }

    /**
     * Get Distance Between Function to get Distance Between two Locations
     * @param location
     * Location Object to get Distance To
     * @return
     * Returns a double which stores Distance to Given Location from This Location
     */
    public double getDistanceBetween(Location location) {
        return 0; // TODO: Implement way to determine distance based on coordinates
    }
}
