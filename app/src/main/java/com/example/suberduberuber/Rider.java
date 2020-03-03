package com.example.suberduberuber;

import java.util.ArrayList;

public class Rider extends User {
    private ArrayList<String> rides; // TODO: Change String to Ride when class is created
    private ArrayList<String> requests; // TODO: Change String to Request when class is created

    public Rider(String username, String emailAddress) {
        super(username, emailAddress);
        this.rides = new ArrayList<String>();
        this.requests = new ArrayList<String>();
    }

    public void addRide(String ride) {
        this.rides.add(ride);
    }

    public void removeRide(String ride) {
        if (this.rides.contains(ride)) {
            this.rides.remove(ride);
        }
    }

    public void addRequest(String request) {
        this.requests.add(request);
    }

    public void removeRequest(String request) {
        if (this.requests.contains(request)) {
            this.requests.remove(request);
        }
    }

    public ArrayList<String> getRides() {
        return this.rides;
    }

    public ArrayList<String> getRequests() {
        return this.requests;
    }
}
