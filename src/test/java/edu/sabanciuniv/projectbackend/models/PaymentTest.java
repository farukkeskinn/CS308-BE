package edu.sabanciuniv.projectbackend.models;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {
    @Test
    void setAndGetFields() {
        Payment p = new Payment();
        p.setPaymentId("p1");
        p.setAmount(123.45);
        p.setPaymentDate(LocalDateTime.now());
        p.setPaymentStatus("DONE");

        assertEquals("p1", p.getPaymentId());
        assertEquals(123.45, p.getAmount());
        assertEquals("DONE", p.getPaymentStatus());
        assertNotNull(p.getPaymentDate());
    }
}