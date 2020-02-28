package com.example.suberduberuber;

public class Car {
    private String make;
    private String model;
    private int year;
    private String licensePlate;
    private String color;

    public Car(String make, String model, String licensePlate, String color, int year) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.licensePlate = licensePlate;
        this.color = color;
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

}
