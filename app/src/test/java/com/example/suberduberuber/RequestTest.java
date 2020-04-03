package com.example.suberduberuber;

import com.example.suberduberuber.Models.CustomLocation;
import com.example.suberduberuber.Models.Path;
import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.Models.Rider;
import com.example.suberduberuber.Models.User;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;

public class RequestTest {
    Date d;

    private Request mockRequest() {
        d = new Date();
        Request request = new Request(null, null, d);
        return request;
    }

    @Test
    public void testGetRequestingUser() {
        Request request = mockRequest();
        assertNull(request.getRequestingUser());
    }

    @Test
    public void testGetPath() {
        Request request = mockRequest();
        assertNull(request.getPath());
    }

    @Test
    public void testGetTime() {
        Request request = mockRequest();
        assertTrue(Objects.equals(d, request.getTime()));
    }

    @Test
    public void testGetStatus() {
        Request request = mockRequest();
        assertTrue(Objects.equals("PENDING_ACCEPTANCE", request.getStatus()));
    }

    @Test
    public void testSetRequestingUser() {
        Request request = mockRequest();
        Rider user = new Rider("username", "email", "123");
        request.setRequestingUser(user);
        assertEquals(user, request.getRequestingUser());
    }

    @Test
    public void testSetPath() {
        Request request = mockRequest();
        Path path = new Path(new CustomLocation(), new CustomLocation());
        request.setPath(path);
        assertEquals(path, request.getPath());
    }

    @Test
    public void testSetTime() {
        Request request = mockRequest();
        Date d2 = new Date();
        request.setTime(d2);
        assertEquals(d2, request.getTime());
    }

    @Test
    public void testStatusChange() {
        Request request = mockRequest();
        request.accept(null);
        assertEquals("ACCEPTED", request.getStatus());

        request.pickup();
        assertEquals("IN_PROGRESS", request.getStatus());

        request.complete();
        assertEquals("COMPLETED", request.getStatus());
    }
}
