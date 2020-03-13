package com.example.suberduberuber.Models;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private String username;
    private String phoneNumber;

    @DocumentId
    private String emailAddress;
    private int rating;
    private String QRBucksWallet; // TODO: Change to Wallet class when it is created

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
    public User(String username, String emailAddress) {
        this.username = username;
        this.phoneNumber = "";
        this.emailAddress = emailAddress;
        this.rating = 0;
        this.QRBucksWallet = "";
    }

    public String getEmail() {
        return emailAddress;
    }
    public String getUsername() {return username ;}
    public String getPhoneNumber() {return phoneNumber;}
    public int getRating() {
        return this.rating;
    }
    public String getQRBucksWallet() {
        return this.QRBucksWallet;
    }

    /**
     * Gets the info of an user (phone number and email address)
     * @return
     *      An array list of phone number [0] and email address [1]
     */
    public ArrayList<String> getInfo() {
        ArrayList<String> info = new ArrayList<String>();
        info.add(this.phoneNumber);
        info.add(this.emailAddress);
        return info;
    }

    /**
     * Set the info of a user
     * @param phoneNumber
     *      New phone number
     * @param emailAddress
     *      New email address
     */
    public void setInfo(String phoneNumber, String emailAddress) {
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    /**
     * Sets a new rating value for the user
     * @param rating
     *      New rating value
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setQRBucksWallet(String QRBucksWallet) {
        this.QRBucksWallet = QRBucksWallet;
    };

    public void setUsername(String username) {
        this.username = username;
    }
}
