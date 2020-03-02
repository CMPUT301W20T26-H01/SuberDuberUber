package com.example.suberduberuber;

import java.util.Objects;

/**
 * This class defines a Request object.  Which is either in the state of initiated, in-progress, or completed
 */
public class Request {

    private int requestID;
    private User requestingUser;
    private Path path;
    private String time;
    private String status;

    /**
     * Change the status of the request
     * @param newStatus
     * The new status of the request, either "initiated", "in-progress", or "completed"
     * @throws IllegalArgumentException
     * If newStatus is not one of the three accepted options
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

}
