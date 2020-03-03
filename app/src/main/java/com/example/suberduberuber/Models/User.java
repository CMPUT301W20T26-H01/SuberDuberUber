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
}
