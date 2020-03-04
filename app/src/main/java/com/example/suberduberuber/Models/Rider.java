package com.example.suberduberuber.Models;

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
    public Rider(String username, String emailAddress) {
        super(username, emailAddress);
        this.rides = new ArrayList<String>();
        this.requests = new ArrayList<Request>();
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
