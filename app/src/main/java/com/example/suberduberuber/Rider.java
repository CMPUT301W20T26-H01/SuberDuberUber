package com.example.suberduberuber;

import java.util.ArrayList;

public class Rider extends User {
    private ArrayList<String> rides; // TODO: Change String to Ride when class is created
    private ArrayList<String> requests; // TODO: Change String to Request when class is created

    /**
     * Creates a rider user
     * @param username
     *      User's identifier
     * @param phoneNumber
     *      User's phone number
     * @param emailAddress
     *      User's unique email address
     */
    public Rider(String username, String phoneNumber, String emailAddress) {
        super(username, phoneNumber, emailAddress);
        this.rides = new ArrayList<String>();
        this.requests = new ArrayList<String>();
    }

    /**
     * Adds a ride into the list of rides
     * @param ride
     *      Ride to be added
     */
    public void addRide(String ride) {
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
    }

    /**
     * Adds a request to the list of requests
     * @param request
     *      Request to be added
     */
    public void addRequest(String request) {
        this.requests.add(request);
    }

    /**
     * Removes a request to the list of requests
     * @param request
     *      Request to be removed
     */
    public void removeRequest(String request) {
        if (this.requests.contains(request)) {
            this.requests.remove(request);
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
    public ArrayList<String> getRequests() {
        return this.requests;
    }
}
