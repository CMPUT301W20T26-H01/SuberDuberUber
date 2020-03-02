package com.example.suberduberuber;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class driverClassTest {
    Driver d;
    private Driver createDriver() {
        Driver r = new Driver("testUsername", "000-000-0000", "test@test.ca");
        return r;
    }

    @Before
    public void setup() {
        d = createDriver();
    }

    @Test
    public void testGetInfo() {
        ArrayList<String> info = d.getInfo();
        assertEquals("000-000-0000", info.get(0));
        assertEquals("test@test.ca", info.get(1));
    }

    @Test
    public void testSetInfo() {
        String newPh = "123-456-7890";
        String newEmail = "newtest@new.ca";
        d.setInfo(newPh, newEmail);

        assertEquals(newPh, d.getInfo().get(0));
        assertEquals(newEmail, d.getInfo().get(1));
    }

    @Test
    public void testSetGetRating() {
        assertEquals(0, d.getRating());

        d.setRating(50);
        assertEquals(50, d.getRating());
    }

    @Test
    public void testGetWallet() {
        assertEquals("", d.getQRBucksWallet());
    }

    @Test
    public void testSetGetCar() {
        assertEquals("", d.getCar());

        d.setCar("NewCar");
        assertEquals("NewCar", d.getCar());
    }

    @Test
    public void testSetGetCurrentRide() {
        assertEquals("", d.getCurrentRide());

        d.setCurrentRide("Current Ride");
        assertEquals("Current Ride", d.getCurrentRide());
    }

    @Test
    public void testAddAcceptedReq() {
        String req1 = "Accepted Req 1";
        String req2 = "Accepted Req 2";
        d.addAcceptedRequests(req1);

        assertEquals(req1, d.getAcceptedRequests().get(0));
        d.addAcceptedRequests(req2);
        assertEquals(req2, d.getAcceptedRequests().get(1));
    }

    @Test
    public void testRemoveAcceptedReq() {
        String req1 = "Req 1";
        d.addAcceptedRequests(req1);
        d.removeAcceptedRequests(req1);
        assertEquals(0, d.getAcceptedRequests().size());
    }

    @Test
    public void testGetAcceptedReq() {
        String req1 = "Req1";
        String req2 = "Req2";
        ArrayList<String> a = new ArrayList<>();
        a.add(req1);
        a.add(req2);

        d.addAcceptedRequests(req1);
        d.addAcceptedRequests(req2);
        for (int i = 0; i < a.size(); i++) {
            assertEquals(a.get(i), d.getAcceptedRequests().get(i));
        }
    }
}
