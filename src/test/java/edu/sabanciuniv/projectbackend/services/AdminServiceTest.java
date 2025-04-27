package edu.sabanciuniv.projectbackend.services;

import edu.sabanciuniv.projectbackend.models.Admin;
import edu.sabanciuniv.projectbackend.repositories.AdminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceTest {

    @Mock AdminRepository adminRepository;
    @InjectMocks AdminService adminService;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    void getAdmin_returnsAdmin() {
        Admin admin = new Admin();
        admin.setAdminId("a1");
        when(adminRepository.findById("a1")).thenReturn(Optional.of(admin));

        Admin result = adminService.getAdmin("a1");
        assertNotNull(result);
        assertEquals("a1", result.getAdminId());
    }

    @Test
    void getAdmin_notFound_returnsNull() {
        when(adminRepository.findById("notfound")).thenReturn(Optional.empty());

        Admin result = adminService.getAdmin("notfound");
        assertNull(result);
    }

    @Test
    void getAllAdmins_returnsList() {
        Admin a1 = new Admin(); a1.setAdminId("a1");
        Admin a2 = new Admin(); a2.setAdminId("a2");
        when(adminRepository.findAll()).thenReturn(List.of(a1, a2));

        List<Admin> result = adminService.getAllAdmins();
        assertEquals(2, result.size());
        assertEquals("a1", result.get(0).getAdminId());
    }

    @Test
    void saveAdmin_savesAndReturns() {
        Admin admin = new Admin();
        when(adminRepository.save(admin)).thenReturn(admin);

        Admin result = adminService.saveAdmin(admin);
        assertEquals(admin, result);
    }

    @Test
    void deleteAdmin_callsRepositoryDelete() {
        adminService.deleteAdmin("a1");
        verify(adminRepository).deleteById("a1");
    }
}