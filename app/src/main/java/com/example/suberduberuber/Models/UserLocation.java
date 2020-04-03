package com.example.suberduberuber.Models;

/*
Copyright [2020] [SuberDuberUber]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

****************************************************************************************************

Model class used to store location of users by the LocationServices.
Code inspired by YouTube playlist on GoogleMaps by User CodingWithMitch
https://www.youtube.com/watch?v=pj4thEAYrGU&list=PLgCYzUzKIBE-SZUrVOsbYMzH7tPigT3gi&index=15

 */

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class UserLocation {

    private GeoPoint geoPoint = null;
    private @ServerTimestamp
    Date timeStamp;
    private User user;

    public UserLocation(GeoPoint geoPoint, Date timeStamp, User user) {
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

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
