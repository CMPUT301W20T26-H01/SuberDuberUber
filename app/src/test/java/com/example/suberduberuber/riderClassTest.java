package com.example.suberduberuber;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class riderClassTest {
    Rider r;
    private Rider createRider() {
        Rider r = new Rider("testUsername", "000-000-0000", "test@test.ca");
        return r;
    }

    @Before
    public void setup() {
        r = createRider();
    }

    @Test
    public void testGetInfo() {
        ArrayList<String> info = r.getInfo();
        assertEquals("000-000-0000", info.get(0));
        assertEquals("test@test.ca", info.get(1));
    }

    @Test
    public void testSetInfo() {
        String newPh = "123-456-7890";
        String newEmail = "newtest@new.ca";
        r.setInfo(newPh, newEmail);

        assertEquals(newPh, r.getInfo().get(0));
        assertEquals(newEmail, r.getInfo().get(1));
    }

    @Test
    public void testSetGetRating() {
        assertEquals(0, r.getRating());

        r.setRating(50);
        assertEquals(50, r.getRating());
    }

    @Test
    public void testGetWallet() {
        assertEquals("", r.getQRBucksWallet());
    }

    @Test
    public void testAddRide() {
        String ride = "Ride1";
        r.addRide(ride);

        assertEquals(ride, r.getRides().get(0));
        r.addRide("Ride2");

        assertEquals(2, r.getRides().size());
        assertEquals("Ride2", r.getRides().get(1));
    }

    @Test
    public void testRemoveRide() {
        String ride = "Ride1";
        r.addRide(ride);
        r.removeRide(ride);

        assertEquals(0, r.getRides().size());
    }

    @Test
    public void testAddRequest() {
        String req = "Request1";
        r.addRequest(req);

        assertEquals(req, r.getRequests().get(0));
        r.addRequest("Request2");

        assertEquals(2, r.getRequests().size());
        assertEquals("Request2", r.getRequests().get(1));
    }

    @Test
    public void testRemoveRequest() {
        String req = "Request1";
        r.addRequest(req);
        r.removeRequest(req);

        assertEquals(0, r.getRequests().size());
    }


}
