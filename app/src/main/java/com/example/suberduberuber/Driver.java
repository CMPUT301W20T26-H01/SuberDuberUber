package com.example.suberduberuber;

import java.util.ArrayList;

public class Driver extends User {
    private String car; // TODO: Need to change once car class is created
    private String currentRide; // TODO: Need to change once ride class is created
    private ArrayList<String> acceptedRequests; // TODO: Need to change once request class is created
    private boolean isAvailable;

    public Driver(String username, String phoneNumber, String emailAddress) {
        super(username, phoneNumber, emailAddress);
        this.car = "";
        this.currentRide = "";
        this.acceptedRequests = new ArrayList<String>();
        this.isAvailable = true;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getCar() {
        return this.car;
    }

    public void setCurrentRide(String ride) {
        this.currentRide = ride;
    }

    public String getCurrentRide() {
        return this.currentRide;
    }

    public void addAcceptedRequests(String request) {
        this.acceptedRequests.add(request);
    }

    public void removeAcceptedRequests(String request) {
        if (this.acceptedRequests.contains(request)) {
            this.acceptedRequests.remove(request);
        }
    }

    public ArrayList<String> getAcceptedRequests() {
        return this.acceptedRequests;
    }
}
