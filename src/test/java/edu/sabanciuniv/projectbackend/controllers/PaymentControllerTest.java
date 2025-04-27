package edu.sabanciuniv.projectbackend.controllers;

import edu.sabanciuniv.projectbackend.models.Payment;
import edu.sabanciuniv.projectbackend.services.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentControllerTest {

    @Mock PaymentService paymentService;
    @InjectMocks PaymentController controller;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    void getPaymentById_returnsPayment() {
        Payment payment = new Payment();
        payment.setPaymentId("p1");
        when(paymentService.getPaymentById("p1")).thenReturn(payment);

        Payment result = controller.getPaymentById("p1");

        assertNotNull(result);
        assertEquals("p1", result.getPaymentId());
    }

    @Test
    void getPaymentById_notFound_returnsNull() {
        when(paymentService.getPaymentById("notfound")).thenReturn(null);

        Payment result = controller.getPaymentById("notfound");

        assertNull(result);
    }

    @Test
    void getAllPayments_returnsList() {
        Payment p1 = new Payment(); p1.setPaymentId("p1");
        Payment p2 = new Payment(); p2.setPaymentId("p2");
        when(paymentService.getAllPayments()).thenReturn(List.of(p1, p2));

        List<Payment> result = controller.getAllPayments();
        assertEquals(2, result.size());
        assertEquals("p1", result.get(0).getPaymentId());
    }
}