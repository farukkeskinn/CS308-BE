package edu.sabanciuniv.projectbackend.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {
    @Test
    void customerConstructorAndGetters() {
        Customer c = new Customer("Ali", "Veli", "ali@su.edu", "1234");
        assertEquals("Ali", c.getFirstName());
        assertEquals("Veli", c.getLastName());
        assertEquals("ali@su.edu", c.getEmail());
        assertEquals("1234", c.getPassword());
    }

    @Test
    void setAndGetCustomerId() {
        Customer c = new Customer();
        c.setCustomerId("c123");
        assertEquals("c123", c.getCustomerId());
    }
}