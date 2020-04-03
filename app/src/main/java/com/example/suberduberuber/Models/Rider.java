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
 */

import java.util.ArrayList;

public class Rider extends User {
    private ArrayList<String> rides; // TODO: Change String to Ride when class is created
    private ArrayList<Request> requests; // TODO: Change String to Request when class is created

    /**
     * Creates a rider user
     * @param username
     *      User's identifier
     * @param emailAddress
     *      User's unique email address
     */
    public Rider(String username, String emailAddress, String phoneNum) {
        super(username, emailAddress, phoneNum, false);
        this.rides = new ArrayList<String>();
        this.requests = new ArrayList<Request>();
    }

    public Rider() {

    }

    /**
     * Adds a ride into the list of rides
     * @param ride
     *      Ride to be added
     */
    public void addRide(String ride) {
        if (this.rides.contains(ride)) {
            throw new IllegalArgumentException();
        }
        this.rides.add(ride);
    }

    /**
     * Removes a ride from the list of rides
     * @param ride
     *      Ride to be removed
     */
    public void removeRide(String ride) {
        if (this.rides.contains(ride)) {
            this.rides.remove(ride);
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Adds a request to the list of requests
     * @param request
     *      Request to be added
     */
    public void addRequest(Request request) {
        if (this.requests.contains(request)) {
            throw new IllegalArgumentException();
        }
        this.requests.add(request);
    }

    /**
     * Removes a request to the list of requests
     * @param request
     *      Request to be removed
     */
    public void removeRequest(Request request) {
        if (this.requests.contains(request)) {
            this.requests.remove(request);
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Gets the list of rides
     * @return
     *      Array list of rides
     */
    public ArrayList<String> getRides() {
        return this.rides;
    }

    /**
     * Gets the list of requests
     * @return
     *      Array list of requests
     */
    public ArrayList<Request> getRequests() {
        return this.requests;
    }
}
