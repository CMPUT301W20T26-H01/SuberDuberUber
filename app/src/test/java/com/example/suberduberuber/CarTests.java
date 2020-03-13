package com.example.suberduberuber;
import com.example.suberduberuber.Models.Car;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



public class CarTests {

    private Car mockCar() {
        return new Car("Ram", "1500", "333915", "Black", 2018);
    }

    @Test
    public void testGetters() {
        Car car = mockCar();
        assertEquals("Ram", car.getMake());
        assertEquals("1500", car.getModel());
        assertEquals("333915", car.getLicensePlate());
        assertEquals("Black", car.getColor());
        assertEquals(2018, car.getYear());
    }
    @Test
    public void testSetters() {
        Car car = mockCar();
        car.setColor("White");
        car.setModel("Denali 3500 HD");
        car.setMake("GMC");
        car.setYear(2015);

        assertEquals("GMC", car.getMake());
        assertEquals("Denali 3500 HD", car.getModel());
        assertEquals("White", car.getColor());
        assertEquals(2015, car.getYear());
    }

    @Test
    public void testBasicConstructor() {
        Car car = new Car("333915");
        assertEquals("Default", car.getMake());
        assertEquals("Default", car.getModel());
        assertEquals("333915", car.getLicensePlate());
        assertEquals("Default", car.getColor());
        assertEquals(-1, car.getYear());

        car.setColor("White");
        car.setModel("Denali 3500 HD");
        car.setMake("GMC");
        car.setYear(2015);

        assertEquals("GMC", car.getMake());
        assertEquals("Denali 3500 HD", car.getModel());
        assertEquals("White", car.getColor());
        assertEquals(2015, car.getYear());
    }
}
