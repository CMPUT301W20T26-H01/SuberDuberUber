package com.example.suberduberuber;

import java.util.ArrayList;

public abstract class User {
    private String username;
    private String phoneNumber;
    private String emailAddress;
    private int rating;
    private String QRBucksWallet; // TODO: Change to Wallet class when it is created

    /**
     * This generates an user with an unique username, phone number and email address
     * @param username
     *      User's identifier
     * @param phoneNumber
     *      User's phone number
     * @param emailAddress
     *      User's unique email address
     *
     */
    public User(String username, String phoneNumber, String emailAddress) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.rating = 0;
        this.QRBucksWallet = "";
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

    /**
     * Retrieves the rating value of the user
     * @return
     *      Rating value of the user
     */
    public int getRating() {
        return this.rating;
    }

    /**
     * Gets the wallet of the user
     * @return
     *      QR Bucks Wallet of the user
     */
    public String getQRBucksWallet() {
        return this.QRBucksWallet;
    }
}
