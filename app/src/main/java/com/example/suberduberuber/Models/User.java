package com.example.suberduberuber.Models;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    @DocumentId
    private String email;
    private String username;
    private String phone;
    private int rating;
    private boolean driver;

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
        this.phone = "";
        this.email = emailAddress;
        this.rating = 0;
    }

    public String getEmail() {
        return email;
    }
    public String getUsername() {return username ;}
    public String getPhoneNumber() {return phone;}
    public int getRating() {
        return this.rating;
    }
    public boolean getDriver() {
        return this.driver;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDriver(boolean driver) {
        this.driver = driver;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
