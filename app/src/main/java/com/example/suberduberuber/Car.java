package com.example.suberduberuber;

public class Car {
    public static final String DEFAULT = "Default"; // Default value for basic constructor
    private String make;
    private String model;
    private int year;
    private String licensePlate;
    private String color;

    /**
     * Full Constructor for Car Objects
     * @param make
     * String Representation of Car Make
     * @param model
     * String Representation of Car Model
     * @param licensePlate
     * String Representation of Car License Plate
     * @param color
     * String Representation of Car Colour
     * @param year
     * Int Representation of Car Year
     */
    public Car(String make, String model, String licensePlate, String color, int year) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.licensePlate = licensePlate;
        this.color = color;
    }

    /**
     * Basic Constructor for Car
     * @param licensePlate
     * String Representation of Car License Plate
     */
    public Car(String licensePlate) {
        this.licensePlate = licensePlate;
        this.make = DEFAULT;
        this.model = DEFAULT;
        this.color = DEFAULT;
        this.year = -1;
    }

    /**
     * Getter for Year Attribute
     * @return
     * Returns an Int storing Year
     */
    public int getYear() {
        return year;
    }

    /**
     * Getter for Color Attribute
     * @return
     * Returns a String storing Color
     */
    public String getColor() {
        return color;
    }

    /**
     * Getter fro License Plate
     * @return
     * Returns a String storing License Plate
     */
    public String getLicensePlate() {
        return licensePlate;
    }

    /**
     * Getter for Make
     * @return
     * Returns a String storing Make
     */
    public String getMake() {
        return make;
    }

    /**
     * Getter for Model
     * @return
     * Returns a String storing Model
     */
    public String getModel() {
        return model;
    }

    /**
     * Setter for Year
     * @param year
     * Int Representation of Year
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Setter for Model
     * @param model
     * String Representation of Model
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Setter for Make
     * @param make
     * String Representation for Make
     */
    public void setMake(String make) {
        this.make = make;
    }

    /**
     * Setter for Color
     * @param color
     * String Representation for Color
     */
    public void setColor(String color) {
        this.color = color;
    }
}
