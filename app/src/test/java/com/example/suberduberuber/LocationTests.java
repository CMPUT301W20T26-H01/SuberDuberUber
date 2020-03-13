package com.example.suberduberuber;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LocationTests {

    private Location mockLocation() {
        return new Location("X, Y", "Mother Do You Think They'll Drop The Bomb", "Mother Do You Think They'll Like This Song");
    }

    @Test
    public void testGetters() {
        Location location = mockLocation();

        assertEquals("X, Y", location.getCoordinate());
        assertEquals("Mother Do You Think They'll Drop The Bomb", location.getLocationName());
        assertEquals("Mother Do You Think They'll Like This Song", location.getAddress());
        assertNotEquals("Mother Do You Think They'll Try To Break My Balls", location.getAddress());
        assertNotEquals("Ooh, Ah", location.getCoordinate());
        assertNotEquals("Mother Should I Build The Wall", location.getLocationName());
    }

    @Test
    public void testGetDistanceBetween() {
        Location location = mockLocation();
        Location location1 = mockLocation();
        assertEquals(0, location.getDistanceBetween(location1)); //TODO Update once function has been implemented
    }
}
