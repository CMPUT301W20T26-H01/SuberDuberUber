package com.example.suberduberuber;

import org.junit.Test;

import static org.junit.Assert.*;

public class riderClassTest {
    private Rider createRider() {
        Rider r = new Rider("testUsername", "000-000-0000", "test@test.ca");
        return r;
    }

    @Test
    void testAddRide() {
        Rider r = createRider();
    }


}
