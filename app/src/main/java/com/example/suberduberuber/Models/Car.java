package com.example.suberduberuber.Models;

public class Car {
    public static final String DEFAULT = "Default"; // Default value for basic constructor
    private String make;
    private String model;
    private int year;
    private String licensePlate;
    private String color;

    public Car() {
        // Firestore needs empty constructor for serializability
    }

    public Car(String make, String model, String licensePlate, String color, int year) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.licensePlate = licensePlate;
        this.color = color;
    }

    public Car(String licensePlate) {
        this.licensePlate = licensePlate;
        this.make = DEFAULT;
        this.model = DEFAULT;
        this.color = DEFAULT;
        this.year = -1;
    }

    public int getYear() {
        return year;
    }

    public String getColor() {
        return color;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
