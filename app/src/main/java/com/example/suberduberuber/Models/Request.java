package com.example.suberduberuber.Models;

import com.google.firebase.firestore.DocumentId;

import java.util.Date;
import java.util.Objects;

/**
 * This class defines a Request object.  Which is either in the state of initiated, in-progress, or completed
 */
public class Request {

    @DocumentId
    private String requestID; // Stored as a string as @DocumentID (Firestore) requires String type
    private Rider requestingUser;
    private Path path;
    private Date time;
    private String status;

    /**
     * Request object constructor
     * @param requestingUser    The user submitting the request
     * @param path              The path requested by the user
     * @param time              The chosen time of pickup for the ride request
     * @param status            The current status of the ride request
     */
    public Request(Rider requestingUser, Path path, Date time, String status) {
        this.requestingUser = requestingUser;
        this.path = path;
        this.time = time;
        this.status = status;
    }

    /**
     * Empty public constructor needed by Cloud Firestore for serializability
      */
    public Request() { }

    /**
     * Change the status of the request
     * @param newStatus                     The new status of the request, either "initiated", "in-progress", or "completed"
     * @throws IllegalArgumentException     If newStatus is not one of the three accepted options
     */
    public void changeStatus(String newStatus) throws IllegalArgumentException {
        if (Objects.equals(newStatus, "initiated")) {
            this.status = "initiated";
        } else if (Objects.equals(newStatus, "in-progress")) {
            this.status = "in-progress";
        } else if (Objects.equals(newStatus, "completed")) {
            this.status = "completed";
        } else {
            throw new IllegalArgumentException();
        }
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

    /**
     * Returns a String (either initiated, in-progress, or completed)
     * @return      The status of the ride request
     */
    public String getStatus() {
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
     *      New desired time for request
     */
    public void setTime(Date time) {
        this.time = time;
    }

    /**
     * Changes the status of a request
     * @param status
     *      New desired status for request
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public Ride createRide(Driver driver) {
        return new Ride(driver, requestingUser, path, time, 0);
    }
}
