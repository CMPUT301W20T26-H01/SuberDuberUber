package com.example.suberduberuber;

import org.junit.Test;

import static org.junit.Assert.*;

public class riderClassTest {
    private Rider createRider() {
        Rider r = new Rider("testUsername", "000-000-0000", "test@test.ca");
        return r;
    }

    @Test
    public void testAddRide() {
        Rider r = createRider();
        String ride = "Ride1";
        r.addRide(ride);

        assertEquals(ride, r.getRides().get(0));
        r.addRide("Ride2");

        assertEquals(2, r.getRides().size());
        assertEquals("Ride2", r.getRides().get(1));
    }

    @Test
    public void testRemoveRide() {
        Rider r = createRider();
        String ride = "Ride1";
        r.addRide(ride);
        r.removeRide(ride);

        assertEquals(0, r.getRides().size());
    }

    @Test
    public void testAddRequest() {
        Rider r = createRider();
        String req = "Request1";
        r.addRequest(req);

        assertEquals(req, r.getRequests().get(0));
        r.addRequest("Request2");

        assertEquals(2, r.getRequests().size());
        assertEquals("Request2", r.getRequests().get(1));
    }

    @Test
    public void testRemoveRequest() {
        Rider r = createRider();
        String req = "Request1";
        r.addRequest(req);
        r.removeRequest(req);

        assertEquals(0, r.getRequests().size());
    }


}
