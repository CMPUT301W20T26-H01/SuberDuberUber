package com.example.suberduberuber.Models;
/*
Copyright [2020] [SuberDuberUber]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

****************************************************************************************************

CustomLocation class to track relevant location details for rides and requests. Will be implemented
to take information from a passed in Place object. Will be implemented to work with Path class and
GoogleRoutes API
 */
public class CustomLocation {
    private String coordinate;
    private String locationName;
    private String address;

    // Empty public constructor needed by Cloud Firestore for serializability
    public CustomLocation() { }

    /**
     * Constructor for Location Object
     * @param coordinate
     * String Representation of Coordinates
     * @param locationName
     * String Representation of Location Name
     * @param address
     * String Representation of Address
     */
    public CustomLocation(String coordinate, String locationName, String address) {
        this.coordinate = coordinate;
        this.locationName = locationName;
        this.address = address;
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
    public double getDistanceBetween(CustomLocation location) {
        return 0; // TODO: Implement way to determine distance based on coordinates
    }
}
