package com.example.suberduberuber.Models;

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

public class UserLocation {

    private GeoPoint geoPoint;
    private @ServerTimestamp String timeStamp;
    private User user;

    public UserLocation(GeoPoint geoPoint, String timeStamp, User user) {
        this.geoPoint = geoPoint;
        this.timeStamp = timeStamp;
        this.user = user;
    }

    public UserLocation() {

    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
