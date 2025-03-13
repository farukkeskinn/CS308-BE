package edu.sabanciuniv.projectbackend.controllers;

import edu.sabanciuniv.projectbackend.dto.LoginRequest;
import edu.sabanciuniv.projectbackend.services.AuthService;
import edu.sabanciuniv.projectbackend.utils.JWTUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("========== LOGIN REQUEST RECEIVED ==========");
        System.out.println("Email: " + loginRequest.getEmail());
        System.out.println("Password: " + loginRequest.getPassword());

        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        if (email == null || password == null) {
            System.out.println("ERROR: Email or password is null!");
            return ResponseEntity.status(400).body(Map.of("message", "Email and password are required!"));
        }

        String role = authService.login(email, password);
        System.out.println("AuthService Response: " + role);

        if (role != null) {
            String token = JWTUtil.generateToken(role, email);
            System.out.println("Generated Token: " + token);
            System.out.println("============================================");
            return ResponseEntity.ok(Map.of("message", "Login successful.", "role", role, "token", token));
        } else {
            System.out.println("ERROR: Invalid email or password!");
            System.out.println("============================================");
            return ResponseEntity.status(401).body(Map.of("message", "Login failed. Invalid email or password!"));
        }
    }
}