package com.example.suberduberuber;

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
}
