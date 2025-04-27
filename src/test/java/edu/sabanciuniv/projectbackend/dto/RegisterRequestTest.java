package edu.sabanciuniv.projectbackend.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RegisterRequestTest {
    @Test
    void constructorAndGetters() {
        RegisterRequest req = new RegisterRequest("Ali", "Veli", "ali@su.edu", "1234");

        assertEquals("Ali", req.getFirstName());
        assertEquals("Veli", req.getLastName());
        assertEquals("ali@su.edu", req.getEmail());
        assertEquals("1234", req.getPassword());
    }

    @Test
    void settersWork() {
        RegisterRequest req = new RegisterRequest("A", "B", "a@b.com", "pw");
        req.setFirstName("Mehmet");
        req.setLastName("Yılmaz");
        req.setEmail("mehmet@su.edu");
        req.setPassword("4321");

        assertEquals("Mehmet", req.getFirstName());
        assertEquals("Yılmaz", req.getLastName());
        assertEquals("mehmet@su.edu", req.getEmail());
        assertEquals("4321", req.getPassword());
    }
}