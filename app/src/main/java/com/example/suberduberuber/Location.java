package com.example.suberduberuber;

public class Location {
    private String coordinate;
    private String locationName;
    private String address;

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
