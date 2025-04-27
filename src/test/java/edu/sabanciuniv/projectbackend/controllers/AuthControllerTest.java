package edu.sabanciuniv.projectbackend.controllers;

import edu.sabanciuniv.projectbackend.dto.LoginRequest;
import edu.sabanciuniv.projectbackend.services.AuthService;
import edu.sabanciuniv.projectbackend.services.CustomerService;
import edu.sabanciuniv.projectbackend.utils.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock AuthService authService;
    @Mock CustomerService customerService;

    @InjectMocks AuthController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_successfulCustomer_returnsTokenAndCustomerId() {
        LoginRequest req = new LoginRequest();
        LoginRequest request = mock(LoginRequest.class);
        when(request.getEmail()).thenReturn("ali@su.edu");
        when(request.getPassword()).thenReturn("1234");

        when(authService.login("ali@su.edu", "1234")).thenReturn("CUSTOMER");
        when(customerService.getCustomerIdByEmail("ali@su.edu")).thenReturn("c123");

        // JWTUtil.generateToken statik olduğu için, testte gerçek token dönecek
        ResponseEntity<?> response = controller.login(request);

        assertEquals(200, response.getStatusCodeValue());
        Map<?,?> body = (Map<?,?>) response.getBody();
        assertEquals("Login successful.", body.get("message"));
        assertEquals("CUSTOMER", body.get("role"));
        assertEquals("c123", body.get("customerId"));
        assertTrue(body.containsKey("token"));
    }

    @Test
    void login_invalidCredentials_returns401() {
        LoginRequest request = mock(LoginRequest.class);
        when(request.getEmail()).thenReturn("wrong@su.edu");
        when(request.getPassword()).thenReturn("wrongpass");

        when(authService.login("wrong@su.edu", "wrongpass")).thenReturn(null);

        ResponseEntity<?> response = controller.login(request);

        assertEquals(401, response.getStatusCodeValue());
        Map<?,?> body = (Map<?,?>) response.getBody();
        assertEquals("Login failed. Invalid email or password!", body.get("message"));
    }

    @Test
    void login_missingFields_returns400() {
        LoginRequest request = mock(LoginRequest.class);
        when(request.getEmail()).thenReturn(null);
        when(request.getPassword()).thenReturn(null);

        ResponseEntity<?> response = controller.login(request);

        assertEquals(400, response.getStatusCodeValue());
        Map<?,?> body = (Map<?,?>) response.getBody();
        assertEquals("Email and password are required!", body.get("message"));
    }
}