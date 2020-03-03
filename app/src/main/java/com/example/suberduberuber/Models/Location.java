package com.example.suberduberuber.Models;

public class Location {
    private String coordinate;
    private String locationName;
    private String address;

    // Empty public constructor needed by Cloud Firestore for serializability
    public Location() { }

    public Location(String coordinate, String locationName, String address) {
        this.coordinate = coordinate;
        this. locationName = locationName;
        this. address = address;
    }

    public String getAddress() {
        return address;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public String getLocationName() {
        return locationName;
    }

    public double getDistanceBetween(Location location) {
        return 0; // TODO: Implement way to determine distance based on coordinates
    }
}
