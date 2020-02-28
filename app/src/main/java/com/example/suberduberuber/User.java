package com.example.suberduberuber;

import com.google.firebase.firestore.DocumentId;

import java.util.ArrayList;

public abstract class User {
    private String username;
    private String phoneNumber;

    @DocumentId
    private String emailAddress;
    private int rating;
    private String QRBucksWallet; // TODO: Change to Wallet class when it is created

    public User(String username, String emailAddress) {
        this.username = username;
        this.phoneNumber = "";
        this.emailAddress = emailAddress;
        this.rating = 0;
        this.QRBucksWallet = "";
    }

    public ArrayList<String> getInfo() {
        ArrayList<String> info = new ArrayList<String>();
        info.add(this.phoneNumber);
        info.add(this.emailAddress);
        return info;
    }

    public void setInfo(String phoneNumber, String emailAddress) {
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getRating() {
        return this.rating;
    }

    public String getQRBucksWallet() {
        return this.QRBucksWallet;
    }
}
