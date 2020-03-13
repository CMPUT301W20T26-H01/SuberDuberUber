package com.example.suberduberuber;

import com.example.suberduberuber.Models.Car;
import com.example.suberduberuber.Models.Driver;
import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.Models.Rider;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class driverClassTest {
    Driver d;
    private Driver createDriver() {
        Driver r = new Driver("testUsername", "test@test.ca");
        return r;
    }

    @BeforeEach
    public void setup() {
        d = createDriver();
    }


    @Test
    public void testSetGetRating() {
        assertEquals(0, d.getRating());

        d.setRating(50);
        assertEquals(50, d.getRating());
    }

    @Test
    public void testSetGetCar() {
        assertNull(d.getCar());
        Car c = new Car("ABC123");
        d.setCar(c);
        assertEquals(c, d.getCar());
    }

    @Test
    public void testSetGetCurrentRide() {
        assertEquals("", d.getCurrentRide());

        d.setCurrentRide("Current Ride");
        assertEquals("Current Ride", d.getCurrentRide());
    }

    @Test
    public void testAddAcceptedReq() {
        Rider r = new Rider("rider1", "ride@rider.ca");
        Request req1 = new Request(1, r, null, "time", "initiated");
        Request req2 = new Request(2, r, null, "time", "initiated");
        d.addAcceptedRequests(req1);

        assertEquals(req1, d.getAcceptedRequests().get(0));
        d.addAcceptedRequests(req2);
        assertEquals(req2, d.getAcceptedRequests().get(1));
        assertThrows(IllegalArgumentException.class, () -> { d.addAcceptedRequests(req1); });
    }

    @Test
    public void testRemoveAcceptedReq() {
        Rider r = new Rider("rider1", "ride@rider.ca");
        Request req1 = new Request(1, r, null, "time", "initiated");
        d.addAcceptedRequests(req1);
        d.removeAcceptedRequests(req1);
        assertEquals(0, d.getAcceptedRequests().size());
        assertThrows(IllegalArgumentException.class, () -> { d.removeAcceptedRequests(req1); });
    }

    @Test
    public void testGetAcceptedReq() {
        Rider r = new Rider("rider1", "ride@rider.ca");
        Request req1 = new Request(1, r, null, "time", "initiated");
        Request req2 = new Request(2, r, null, "time", "initiated");
        ArrayList<Request> a = new ArrayList<>();
        a.add(req1);
        a.add(req2);

        d.addAcceptedRequests(req1);
        d.addAcceptedRequests(req2);
        for (int i = 0; i < a.size(); i++) {
            assertEquals(a.get(i), d.getAcceptedRequests().get(i));
        }
    }
}
