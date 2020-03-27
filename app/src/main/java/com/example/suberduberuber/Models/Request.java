package com.example.suberduberuber.Models;

import android.util.Log;

import com.google.firebase.firestore.DocumentId;

import java.util.Date;
import java.util.Objects;

/**
 * This class defines a Request object.  Which is either in the state of initiated, in-progress, or completed
 */
public class Request {

    private static final String TAG = "REQUEST_CLASS";

    enum Status {
        PENDING_ACCEPTANCE,
        IN_PROGRESS,
        COMPLETED
    }

    @DocumentId
    private String requestID; // Stored as a string as @DocumentID (Firestore) requires String type
    private Rider requestingUser;
    private Driver driver;
    private Path path;
    private Date time;
    private Status status;
    private double price;

    /**
     * Request object constructor
     * @param requestingUser    The user submitting the request
     * @param path              The path requested by the user
     * @param time              The chosen time of pickup for the ride request
     */
    public Request(Rider requestingUser, Path path, Date time) {
        this.requestingUser = requestingUser;
        this.path = path;
        this.time = time;
        this.status = Status.PENDING_ACCEPTANCE;
    }

    /**
     * Empty public constructor needed by Cloud Firestore for serializability
      */
    public Request() { }

    public void accept(Driver driver) {
        if(status != Status.PENDING_ACCEPTANCE) {
            Log.d(TAG, "Cannot accept request that is not pending acceptance");
        }
        status = Status.IN_PROGRESS;
        this.driver = driver;
    }

    public void complete() {
        if(status != Status.IN_PROGRESS) {
            Log.d(TAG, "Cannot complete request that is not in progress");
        }
        status = Status.COMPLETED;
    }


    /**
     * Returns an ID number
     * @return      The ID number of the request
     */
    public String getRequestID() {
        return this.requestID;
    }

    /**
     * Returns a User
     * @return      The User that sent the request
     */
    public User getRequestingUser() {
        return this.requestingUser;
    }

    /**
     * Returns a Path
     * @return      The path in which the request's corresponding ride would follow
     */
    public Path getPath() {
        return this.path;
    }

    /**
     * Returns a time in String format
     * @return      The pickup time of the requested ride
     */
    public Date getTime() {
        return this.time;
    }

    public Driver getDriver() {
        return this.driver;
    }

    /**
     * Returns a String (either initiated, in-progress, or completed)
     * @return      The status of the ride request
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Changes the requesting user of a request
     * @param user
     *      New requesting user
     */
    public void setRequestingUser(Rider user) {
        this.requestingUser = user;
    }

    /**
     * Changes the path of a request
     * @param path
     *      New desired path for request
     */
    public void setPath(Path path) {
        this.path = path;
    }

    /**
     * Changes the time of a request
     * @param time
     *      New desired time for request as Date type
     */
    public void setTime(Date time) {
        this.time = time;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}
