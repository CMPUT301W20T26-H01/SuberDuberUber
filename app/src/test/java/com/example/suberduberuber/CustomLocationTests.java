package com.example.suberduberuber;
import com.example.suberduberuber.Models.CustomLocation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CustomLocationTests {

    private CustomLocation mockLocation() {
        return new CustomLocation("X, Y", "Mother Do You Think They'll Drop The Bomb", "Mother Do You Think They'll Like This Song");
    }

    @Test
    public void testGetters() {
        CustomLocation location = mockLocation();

        assertEquals("X, Y", location.getCoordinate());
        assertEquals("Mother Do You Think They'll Drop The Bomb", location.getLocationName());
        assertEquals("Mother Do You Think They'll Like This Song", location.getAddress());
        assertNotEquals("Mother Do You Think They'll Try To Break My Balls", location.getAddress());
        assertNotEquals("Ooh, Ah", location.getCoordinate());
        assertNotEquals("Mother Should I Build The Wall", location.getLocationName());
    }

}
