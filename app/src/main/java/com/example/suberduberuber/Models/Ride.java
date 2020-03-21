package com.example.suberduberuber.Models;

import com.google.firebase.firestore.DocumentId;

import java.util.Date;
import java.util.Objects;


public class Ride {

    @DocumentId
    private String rideID; // Stored as a string as @DocumentID (Firestore) requires String type
    private Rider rider;
    private Driver driver;
    private Path path;
    private Date pickupTime;
    private Date estimatedDropoffTime;
    private double price;

    public Ride(Driver driver, Rider rider, Path path, Date pickupTime, double price) {
        this.driver = driver;
        this.rider = rider;
        this.path = path;
        this.pickupTime = pickupTime;
        this.price = price;
    }

    /**
     * Empty public constructor needed by Cloud Firestore for serializability
     */
    public Ride() { }

    public Rider getRider() {
        return rider;
    }

    public Driver getDriver() {
        return driver;
    }

    public String getRideID() {
        return rideID;
    }

    public Path getPath() {
        return path;
    }

    public Date getPickupTime() {
        return pickupTime;
    }

    public Date getEstimatedDropoffTime() {
        return estimatedDropoffTime;
    }

    public double getPrice() {
        return price;
    }
}
