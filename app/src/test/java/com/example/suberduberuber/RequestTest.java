package com.example.suberduberuber;

import com.example.suberduberuber.Models.CustomLocation;
import com.example.suberduberuber.Models.Path;
import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.Models.User;

import org.junit.jupiter.api.Test;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;

public class RequestTest {

    private Request mockRequest() {
        Request request = new Request(123, null, null, "8:50", "initiated");
        return request;
    }

    @Test
    public void testChangeStatus() {
        Request request = mockRequest();
        assertThrows(IllegalArgumentException.class, () -> { request.changeStatus("12123435"); });
        assertThrows(IllegalArgumentException.class, () -> { request.changeStatus("destroyed"); });
        assertThrows(IllegalArgumentException.class, () -> { request.changeStatus("initiated_complete"); });
    }

    @Test
    public void testGetRequestID() {
        Request request = mockRequest();
        assertEquals(123, request.getRequestID());
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
        assertTrue(Objects.equals("8:50", request.getTime()));
    }

    @Test
    public void testGetStatus() {
        Request request = mockRequest();
        assertTrue(Objects.equals("initiated", request.getStatus()));
    }

    @Test
    public void testSetRequestingUser() {
        Request request = mockRequest();
        User user = new User("username", "email", false);
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
        request.setTime("6:00");
        assertEquals("6:00", request.getTime());
    }

    @Test
    public void testSetStatus() {
        Request request = mockRequest();
        request.setStatus("completed");
        assertEquals("completed", request.getStatus());
        assertThrows(IllegalArgumentException.class, () -> {request.changeStatus("invalid");});
    }
}
