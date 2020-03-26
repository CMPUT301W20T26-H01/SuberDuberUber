package com.example.suberduberuber.Models;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    private String email;
    private String username;
    private String phone;
    private int rating;
    private boolean driver;
    private double balance;
    private int numberOfRatings;

    public User() {
        // Firestore needs this to serialize properly
    }

    /**
     * This generates an user with an unique username, phone number and email address
     * @param username
     *      User's identifier
     * @param emailAddress
     *      User's unique email address
     *
     */
    public User(String username, String emailAddress, String phoneNum, boolean isDriver) {
        this.username = username;
        this.phone = phoneNum;
        this.email = emailAddress;
        this.rating = 0;
        this.driver = isDriver;
        this.balance = 0;
        this.numberOfRatings = 0;
    }

    /**
     * Returns user's email
     * @return
     *      User's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns user's username
     * @return
     *      User's username
     */
    public String getUsername() {return username ;}

    /**
     * Returns user's phone number
     * @return
     *      User's phone number
     */
    public String getPhone() {return phone;}

    /**
     * Returns user's rating
     * @return
     *      User's rating
     */
    public int getRating() {
        return this.rating;
    }

    /**
     * Returns whether a user is a driver
     * @return
     *      Boolean regarding if user is a driver or not
     */
    public boolean getDriver() {
        return this.driver;
    }

    /**
     * Changes a user's email
     * @param email
     *      New desired email for user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Changes a user's username
     * @param username
     *      New desired username for user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Changes user's driver flag
     * @param driver
     *      Boolean value to change if user is a driver or not
     */
    public void setDriver(boolean driver) {
        this.driver = driver;
    }

    /**
     * Changes a user's phone number
     * @param phone
     *      New desired phone for user
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Changes a user's rating
     * @param rating
     *      New desired rating for user
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * Returns the number of ratings of a user
     * @return
     *      User's number of ratings
     */
    public int getNumberOfRatings() {
        return numberOfRatings;
    }

    /**
     * Sets the new number of ratings of a user
     * @param numberOfRatings
     *      New User's number of ratings
     */
    public void setNumberOfRatings(int numberOfRatings) {
        this.numberOfRatings = numberOfRatings;
    }

    /**
     * Returns the amount balance of a user
     * @return
     *      Amount balance of a user
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Sets the new balance of a user
     * @param balance
     *      New amount balance of a user
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }
}
