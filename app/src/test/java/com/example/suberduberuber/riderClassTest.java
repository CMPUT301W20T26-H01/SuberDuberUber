package com.example.suberduberuber;

import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.Models.Rider;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class riderClassTest {
    Rider r;

    @BeforeEach
    public void setup() {
        r = new Rider("testUsername", "test@test.ca");
    }


    @Test
    public void testSetGetRating() {
        assertEquals(0, r.getRating());

        r.setRating(50);
        assertEquals(50, r.getRating());
    }

    @Test
    public void testAddRider() {
        String ride = "Ride1";
        r.addRide(ride);

        assertEquals(ride, r.getRides().get(0));
        r.addRide("Ride2");

        assertEquals(2, r.getRides().size());
        assertEquals("Ride2", r.getRides().get(1));
        assertThrows(IllegalArgumentException.class, () -> { r.addRide(ride); });
    }

    @Test
    public void testRemoveRide() {
        String ride = "Ride1";
        r.addRide(ride);
        r.removeRide(ride);

        assertEquals(0, r.getRides().size());
        assertThrows(IllegalArgumentException.class, () -> { r.removeRide(ride); });
    }

    @Test
    public void testAddRequest() {
        Request req1 = new Request(1, r, null, "time", "initiated");
        r.addRequest(req1);

        assertEquals(req1, r.getRequests().get(0));
        Request req2 = new Request(2, r, null, "time", "initiated");
        r.addRequest(req2);

        assertEquals(2, r.getRequests().size());
        assertEquals(2, r.getRequests().get(1).getRequestID());
        assertThrows(IllegalArgumentException.class, () -> { r.addRequest(req1); });
    }

    @Test
    public void testRemoveRequest() {
        Request req = new Request(1, r, null, "time", "initiated");
        r.addRequest(req);
        r.removeRequest(req);

        assertEquals(0, r.getRequests().size());
        assertThrows(IllegalArgumentException.class, () -> { r.removeRequest(req); });
    }


}
