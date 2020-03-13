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

Path class to keep track of the path for a given ride. This will keep track of the pickup and drop-off
locations as well as the route between them and estimated fare. This will be updated to implement
GoogleRoutes API.
 */

public class Path {

    private static final double COST_FACTOR = 0.05;
    private CustomLocation startLocation;
    private CustomLocation destination;
    private double estimatedFare;

    // Empty public constructor needed by Cloud Firestore for serializability
    public Path() { }

    /**
     * Path Object Constructor
     * @param startLocation
     * The Starting Location for the Path (Location Object)
     * @param destination
     * The Destination Location for the Path (Location Object)
     */
    public Path(CustomLocation startLocation, CustomLocation destination) {
        this.startLocation = startLocation;
        this.destination = destination;
        this.generateEstimatedFare();
    }

    /**
     * Generates Estimated Fare for the Path
     * Uses distance between two locations and a set COST_FACTOR
     */
    public void generateEstimatedFare() {
        double distance = 0;
        if (startLocation != null && destination != null) {
            distance = startLocation.getDistanceBetween(destination);
        }
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
    public void setDestination(CustomLocation destination) {
        this.destination = destination;
        this.generateEstimatedFare();
    }

    /**
     * Setter for Start Location
     * @param startLocation
     * Location Object to be set as the Start Location of the Path
     */
    public void setStartLocation(CustomLocation startLocation) {
        this.startLocation = startLocation;
        this.generateEstimatedFare();
    }

    /**
     * Getter for Destination Location
     * @return
     * Returns a Location Object which is the Destination Location for the Path
     */
    public CustomLocation getDestination() {
        return destination;
    }

    /**
     * Getter for the Start Location
     * @return
     * Retunrs as Location Object which is the Start Location for the Path
     */
    public CustomLocation getStartLocation() {
        return startLocation;
    }
}
