package com.example.suberduberuber;
import com.example.suberduberuber.Models.CustomLocation;
import com.example.suberduberuber.Models.Path;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PathTests {

    private Path mockPath() {
        CustomLocation location1 = new CustomLocation("X1, Y1", "Is This The Real Life", "Is This Just Fantasy");
        CustomLocation location2 = new CustomLocation("X2, Y2", "A Modern Day Warrior Mean Mean Stride", "Today's Tom Sawyer Mean Mean Pride");
        return new Path(location1, location2);
    }

    @Test
    public void testGettersAndSetters() {
        Path path = mockPath();
        assertEquals(0, path.getEstimatedFare()); //TODO update once location tests are complete

        CustomLocation test1 = new CustomLocation("X3, Y3", "Ever Since I Was A Young Boy", "I Played The Silver Ball");
        CustomLocation test2 = new CustomLocation("X4, Y4", "Yesterday", "All My Troubles Seemed So Far Away");

        path.setDestination(test1);
        path.setStartLocation(test2);

        assertEquals(test1, path.getDestination());
        assertEquals(test2, path.getStartLocation());
        assertEquals(0, path.getEstimatedFare());
    }
}
