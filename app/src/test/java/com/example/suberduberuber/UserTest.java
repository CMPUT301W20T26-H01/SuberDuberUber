package com.example.suberduberuber;

import com.example.suberduberuber.Models.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    User rider;
    User driver;

    @BeforeEach
    public void setup() {
        rider = new User("rider", "rider@test.ca", "123", false);
        driver = new User("driver", "driver@test.ca", "456", true);
    }

    @Test
    public void getSetUsername() {
        assertEquals("rider", rider.getUsername());
        assertEquals("driver", driver.getUsername());

        rider.setUsername("new rider");
        assertEquals("new rider", rider.getUsername());

        driver.setUsername("new driver");
        assertEquals("new driver", driver.getUsername());
    }

    @Test
    public void getSetEmail() {
        assertEquals("rider@test.ca", rider.getEmail());
        assertEquals("driver@test.ca", driver.getEmail());

        rider.setEmail("newR@test.com");
        assertEquals("newR@test.com", rider.getEmail());

        driver.setEmail("newD@test.com");
        assertEquals("newD@test.com", driver.getEmail());
    }

    @Test
    public void getSetPhone() {
        assertEquals("123", rider.getPhone());
        assertEquals("456", driver.getPhone());

        rider.setPhone("1234567890");
        assertEquals("1234567890", rider.getPhone());

        driver.setPhone("0987654321");
        assertEquals("0987654321", driver.getPhone());
    }

    @Test
    public void getSetRating() {
        assertEquals(0, rider.getRating());
        assertEquals(0, driver.getRating());

        rider.setRating(40);
        assertEquals(40, rider.getRating());

        driver.setRating(90);
        assertEquals(90, driver.getRating());
    }

    @Test
    public void getSetDriverFlag() {
        assertEquals(false, rider.getDriver());
        assertEquals(true, driver.getDriver());

        rider.setDriver(true);
        assertEquals(true, rider.getDriver());

        driver.setDriver(false);
        assertEquals(false, driver.getDriver());
    }
}
