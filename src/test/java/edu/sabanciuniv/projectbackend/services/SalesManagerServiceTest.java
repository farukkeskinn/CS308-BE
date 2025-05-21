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

    //ADDED AFTER PROGRESS DEMO (NEW UNIT TESTS)
    @Test
    void getAllSalesManagers_emptyList_returnsEmptyList() {
        when(salesManagerRepository.findAll()).thenReturn(List.of());

        List<SalesManager> result = salesManagerService.getAllSalesManagers();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    //ADDED AFTER PROGRESS DEMO (NEW UNIT TESTS)
    @Test
    void getAllSalesManagers_withMultipleManagers_returnsAll() {
        SalesManager sm1 = new SalesManager(); sm1.setSmId("sm1");
        SalesManager sm2 = new SalesManager(); sm2.setSmId("sm2");
        SalesManager sm3 = new SalesManager(); sm3.setSmId("sm3");
        when(salesManagerRepository.findAll()).thenReturn(List.of(sm1, sm2, sm3));

        List<SalesManager> result = salesManagerService.getAllSalesManagers();
        assertEquals(3, result.size());
        assertEquals("sm1", result.get(0).getSmId());
        assertEquals("sm2", result.get(1).getSmId());
        assertEquals("sm3", result.get(2).getSmId());
    }

    //ADDED AFTER PROGRESS DEMO (NEW UNIT TESTS)
    @Test
    void saveSalesManager_withExistingManager_updatesManager() {
        SalesManager manager = new SalesManager();
        manager.setSmId("existing");
        when(salesManagerRepository.save(manager)).thenReturn(manager);

        SalesManager result = salesManagerService.saveSalesManager(manager);
        assertEquals("existing", result.getSmId());
    }

    //ADDED AFTER PROGRESS DEMO (NEW UNIT TESTS)
    @Test
    void getSalesManager_withInvalidId_returnsNull() {
        when(salesManagerRepository.findById("invalid")).thenReturn(Optional.empty());

        SalesManager result = salesManagerService.getSalesManager("invalid");
        assertNull(result);
    }

    //ADDED AFTER PROGRESS DEMO (NEW UNIT TESTS)
    @Test
    void getAllOrders_emptyList_returnsEmptyList() {
        when(salesManagerRepository.findAllOrders()).thenReturn(List.of());

        List<Order> result = salesManagerService.getAllOrders();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    //ADDED AFTER PROGRESS DEMO (NEW UNIT TESTS)
    @Test
    void getAllOrders_withMultipleOrders_returnsAll() {
        Order o1 = new Order(); o1.setOrderId("o1");
        Order o2 = new Order(); o2.setOrderId("o2");
        Order o3 = new Order(); o3.setOrderId("o3");
        when(salesManagerRepository.findAllOrders()).thenReturn(List.of(o1, o2, o3));

        List<Order> result = salesManagerService.getAllOrders();
        assertEquals(3, result.size());
        assertEquals("o1", result.get(0).getOrderId());
        assertEquals("o2", result.get(1).getOrderId());
        assertEquals("o3", result.get(2).getOrderId());
    }
}