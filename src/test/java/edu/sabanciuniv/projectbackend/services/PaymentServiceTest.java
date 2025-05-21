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

    //ADDED AFTER PROGRESS DEMO (NEW UNIT TESTS)
    @Test
    void getAllPayments_emptyList_returnsEmptyList() {
        when(paymentRepository.findAll()).thenReturn(List.of());

        List<Payment> result = paymentService.getAllPayments();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    //ADDED AFTER PROGRESS DEMO (NEW UNIT TESTS)
    @Test
    void getPaymentById_notFound_returnsNull() {
        when(paymentRepository.findById("notfound")).thenReturn(Optional.empty());

        Payment result = paymentService.getPaymentById("notfound");
        assertNull(result);
    }

    //ADDED AFTER PROGRESS DEMO (NEW UNIT TESTS)
    @Test
    void getAllPayments_withMultiplePayments_returnsAll() {
        Payment p1 = new Payment(); p1.setPaymentId("p1");
        Payment p2 = new Payment(); p2.setPaymentId("p2");
        Payment p3 = new Payment(); p3.setPaymentId("p3");
        when(paymentRepository.findAll()).thenReturn(List.of(p1, p2, p3));

        List<Payment> result = paymentService.getAllPayments();
        assertEquals(3, result.size());
        assertEquals("p1", result.get(0).getPaymentId());
        assertEquals("p2", result.get(1).getPaymentId());
        assertEquals("p3", result.get(2).getPaymentId());
    }

    //ADDED AFTER PROGRESS DEMO (NEW UNIT TESTS)
    @Test
    void savePayment_withExistingPayment_updatesPayment() {
        Payment payment = new Payment();
        payment.setPaymentId("existing");
        when(paymentRepository.save(payment)).thenReturn(payment);

        Payment result = paymentService.savePayment(payment);
        assertEquals("existing", result.getPaymentId());
    }
}