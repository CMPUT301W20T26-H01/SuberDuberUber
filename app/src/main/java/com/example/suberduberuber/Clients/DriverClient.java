package com.example.suberduberuber.Clients;

import android.app.Application;

import com.example.suberduberuber.Models.Driver;

public class DriverClient extends Application {
    private Driver driver = null;

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

}