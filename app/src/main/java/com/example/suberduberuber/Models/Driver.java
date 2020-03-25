package com.example.suberduberuber.Models;

import java.util.ArrayList;

public class Driver extends User {
    private Car car;
    private ArrayList<Request> acceptedRequests; // TODO: Need to change once request class is created
    private boolean isAvailable;

    // Empty public constructor needed by Cloud Firestore for serializability
    public Driver() { }

    /**
     * Creates a driver user
     * @param username
     *      User's identifier
     * @param emailAddress
     *      User's unique email address
     */
    public Driver(String username, String emailAddress, Car car) {
        super(username, emailAddress, true);
        this.car = car;
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
