package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.models.Payment;
import edu.sabanciuniv.projectbackend.repositories.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    @Mock PaymentRepository paymentRepository;
    @InjectMocks PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllPayments_returnsList() {
        Payment p1 = new Payment();
        when(paymentRepository.findAll()).thenReturn(List.of(p1));

        List<Payment> result = paymentService.getAllPayments();

        assertEquals(1, result.size());
    }

    @Test
    void deletePayment_deletesById() {
        paymentService.deletePayment("pay1");
        verify(paymentRepository).deleteById("pay1");
    }

    @Test
    void getPaymentById_returnsPayment() {
        Payment p = new Payment();
        when(paymentRepository.findById("pid")).thenReturn(Optional.of(p));

        Payment result = paymentService.getPaymentById("pid");
        assertEquals(p, result);
    }

    @Test
    void savePayment_savesAndReturns() {
        Payment p = new Payment();
        when(paymentRepository.save(p)).thenReturn(p);

        Payment result = paymentService.savePayment(p);
        assertEquals(p, result);
    }
}