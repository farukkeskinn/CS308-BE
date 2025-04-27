package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.models.Order;
import edu.sabanciuniv.projectbackend.dto.PaymentRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceGeneratorServiceTest {

    @InjectMocks InvoiceGeneratorService invoiceGeneratorService;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    void generateInvoicePdf_returnsPdfPath() {
        Order order = new Order();
        PaymentRequest request = new PaymentRequest();
        String result = invoiceGeneratorService.generateInvoicePdf(order, request);
        assertNotNull(result);
        assertTrue(result.endsWith(".pdf"));
    }
}