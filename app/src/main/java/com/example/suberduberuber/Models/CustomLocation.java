package com.example.suberduberuber.Models;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;

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
    private Double latitude;
    private Double longitude;
    private String locationName;
    private String address;
    private String placeID;
    public   boolean hasUniqueName = false;

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

    public CustomLocation(Place currentLocation) {
        this.locationName = currentLocation.getName();
        if (this.locationName != "Selected Location") {
            this.hasUniqueName = true;
        }
        this.address = currentLocation.getAddress();
        this.latitude = currentLocation.getLatLng().latitude;
        this.longitude = currentLocation.getLatLng().longitude;
        this.placeID = currentLocation.getId();
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

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    public LatLng getLatLng() {
        LatLng latLng = new LatLng(this.latitude, this.longitude);
        return latLng;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
