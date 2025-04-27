package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.models.SalesManager;
import edu.sabanciuniv.projectbackend.models.Order;
import edu.sabanciuniv.projectbackend.repositories.SalesManagerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SalesManagerServiceTest {

    @Mock SalesManagerRepository salesManagerRepository;
    @InjectMocks SalesManagerService salesManagerService;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    void getSalesManager_returnsManager() {
        SalesManager sm = new SalesManager();
        sm.setSmId("sm1");
        when(salesManagerRepository.findById("sm1")).thenReturn(Optional.of(sm));

        SalesManager result = salesManagerService.getSalesManager("sm1");
        assertNotNull(result);
        assertEquals("sm1", result.getSmId());
    }

    @Test
    void getSalesManager_notFound_returnsNull() {
        when(salesManagerRepository.findById("notfound")).thenReturn(Optional.empty());

        SalesManager result = salesManagerService.getSalesManager("notfound");
        assertNull(result);
    }

    @Test
    void getAllSalesManagers_returnsList() {
        SalesManager sm1 = new SalesManager(); sm1.setSmId("sm1");
        SalesManager sm2 = new SalesManager(); sm2.setSmId("sm2");
        when(salesManagerRepository.findAll()).thenReturn(List.of(sm1, sm2));

        List<SalesManager> result = salesManagerService.getAllSalesManagers();
        assertEquals(2, result.size());
        assertEquals("sm1", result.get(0).getSmId());
    }

    @Test
    void saveSalesManager_savesAndReturns() {
        SalesManager sm = new SalesManager();
        when(salesManagerRepository.save(sm)).thenReturn(sm);

        SalesManager result = salesManagerService.saveSalesManager(sm);
        assertEquals(sm, result);
    }

    @Test
    void deleteSalesManager_callsRepositoryDelete() {
        salesManagerService.deleteSalesManager("sm1");
        verify(salesManagerRepository).deleteById("sm1");
    }

    @Test
    void getAllOrders_returnsOrderList() {
        Order o1 = new Order(); o1.setOrderId("o1");
        Order o2 = new Order(); o2.setOrderId("o2");
        when(salesManagerRepository.findAllOrders()).thenReturn(List.of(o1, o2));

        List<Order> result = salesManagerService.getAllOrders();
        assertEquals(2, result.size());
        assertEquals("o1", result.get(0).getOrderId());
    }
}