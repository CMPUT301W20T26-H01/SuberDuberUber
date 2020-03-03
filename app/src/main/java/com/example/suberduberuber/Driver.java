package com.example.suberduberuber;

import java.util.ArrayList;

public class Driver extends User {
    private Car car;
    private String currentRide; // TODO: Need to change once ride class is created
    private ArrayList<Request> acceptedRequests; // TODO: Need to change once request class is created
    private boolean isAvailable;

    /**
     * Creates a driver user
     * @param username
     *      User's identifier
     * @param phoneNumber
     *      User's phone number
     * @param emailAddress
     *      User's unique email address
     */
    public Driver(String username, String phoneNumber, String emailAddress) {
        super(username, phoneNumber, emailAddress);
        this.car = null;
        this.currentRide = "";
        this.acceptedRequests = new ArrayList<Request>();
        this.isAvailable = true;
    }

    /**
     * Set the car of the driver
     * @param car
     *      Driver's car
     */
    public void setCar(Car car) {
        this.car = car;
    }

    /**
     * Gets the car of the driver
     * @return
     *      Driver's car
     */
    public Car getCar() {
        return this.car;
    }

    /**
     * Sets the current ride of the driver
     * @param ride
     *      Ride to be set as current
     */
    public void setCurrentRide(String ride) {
        this.currentRide = ride;
    }

    /**
     * Gets the current ride of the driver
     * @return
     *      Current ride of the driver
     */
    public String getCurrentRide() {
        return this.currentRide;
    }

    /**
     * Adds a request that has been accepted
     * @param request
     *      New accepted request
     */
    public void addAcceptedRequests(Request request) {
        if (this.acceptedRequests.contains(request)) {
            throw new IllegalArgumentException();
        }
        this.acceptedRequests.add(request);
    }

    /**
     * Removes a request from the accepted requests
     * @param request
     *      Request to be removed
     */
    public void removeAcceptedRequests(Request request) {
        if (this.acceptedRequests.contains(request)) {
            this.acceptedRequests.remove(request);
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Gets the list of all accepted requests of the driver
     * @return
     *      Array list of all accepted requests
     */
    public ArrayList<Request> getAcceptedRequests() {
        return this.acceptedRequests;
    }
}
