package com.example.suberduberuber;

import com.example.suberduberuber.Models.Car;
import com.example.suberduberuber.Models.Driver;
import com.example.suberduberuber.Models.Rider;
import com.example.suberduberuber.Models.Transaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class TransactionTests {
    Rider rider;
    Driver driver;
    Transaction t;
    Date d;

    @BeforeEach
    public void setup() {
        rider = new Rider("rider", "rider@test.ca", "12345");

        Car car = new Car("plate");
        driver = new Driver("driver", "driver@test.ca", "67890", car);

        d = new Date();
        t = new Transaction(rider, driver, 10, d);
    }

    @Test
    public void testSetGetUserPaying() {
        assertEquals(rider, t.getUserPaying());

        Rider r = new Rider("r2", "r2@test.ca", "123456");
        t.setUserPaying(r);
        assertEquals(r, t.getUserPaying());
    }

    @Test
    public void testSetGetUserPaid() {
        assertEquals(driver, t.getUserPaid());

        Driver drive = new Driver("d2", "d2@test.ca", "123456", new Car("LP"));
        t.setUserPaid(drive);
        assertEquals(drive, t.getUserPaid());
    }

    @Test
    public void testSetGetPaymentAmount() {
        assertEquals(10, t.getPaymentAmount());

        t.setPaymentAmount(20);
        assertEquals(20, t.getPaymentAmount());
    }

    @Test
    public void testSetGetTime() {
        assertEquals(d, t.getTime());

        Date d2 = new Date();
        assertEquals(d2, t.getTime());
    }

}
