package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.models.ProductManager;
import edu.sabanciuniv.projectbackend.repositories.ProductManagerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductManagerServiceTest {

    @Mock ProductManagerRepository productManagerRepository;
    @InjectMocks ProductManagerService productManagerService;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    void getProductManager_returnsManager() {
        ProductManager pm = new ProductManager();
        pm.setPmId("pm1");
        when(productManagerRepository.findById("pm1")).thenReturn(Optional.of(pm));

        ProductManager result = productManagerService.getProductManager("pm1");
        assertNotNull(result);
        assertEquals("pm1", result.getPmId());
    }

    @Test
    void getProductManager_notFound_returnsNull() {
        when(productManagerRepository.findById("notfound")).thenReturn(Optional.empty());

        ProductManager result = productManagerService.getProductManager("notfound");
        assertNull(result);
    }

    @Test
    void getAllProductManagers_returnsList() {
        ProductManager pm1 = new ProductManager(); pm1.setPmId("pm1");
        ProductManager pm2 = new ProductManager(); pm2.setPmId("pm2");
        when(productManagerRepository.findAll()).thenReturn(List.of(pm1, pm2));

        List<ProductManager> result = productManagerService.getAllProductManagers();
        assertEquals(2, result.size());
        assertEquals("pm1", result.get(0).getPmId());
    }

    @Test
    void saveProductManager_savesAndReturns() {
        ProductManager pm = new ProductManager();
        when(productManagerRepository.save(pm)).thenReturn(pm);

        ProductManager result = productManagerService.saveProductManager(pm);
        assertEquals(pm, result);
    }

    @Test
    void deleteProductManager_callsRepositoryDelete() {
        productManagerService.deleteProductManager("pm1");
        verify(productManagerRepository).deleteById("pm1");
    }
}