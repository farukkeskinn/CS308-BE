package edu.sabanciuniv.projectbackend.controllers;

import edu.sabanciuniv.projectbackend.dto.LoginRequest;
import edu.sabanciuniv.projectbackend.dto.RegisterRequest;
import edu.sabanciuniv.projectbackend.services.AuthService;
import edu.sabanciuniv.projectbackend.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // LOGIN
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        // Extract email & password from JSON body
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        String role = authService.login(email, password);
        if (role != null) {
            String token = JWTUtil.generateToken(role, email);
            return "{ \"message\": \"Login successful.\", \"role\": \"" + role + "\", \"token\": \"" + token + "\" }";
        } else {
            return "{ \"message\": \"Login failed. Invalid email or password!\" }";
        }
    }

    // REGISTER
    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        String role = authService.register(request.getFirstName(), request.getLastName(), request.getEmail(), request.getPassword());
        if (role != null) {
            String token = JWTUtil.generateToken(role, request.getEmail());
            return "{ \"message\": \"Registration successful.\", \"role\": \"" + role + "\", \"token\": \"" + token + "\" }";
        } else {
            return "{ \"message\": \"Registration failed. Email may already be in use.\" }";
        }
    }


    // LOGOUT
    @PostMapping("/logout")
    public String logout() {
        return "Logout successful!";
    }

    // OPTIONAL: Protected endpoint example
    @GetMapping("/testProtected")
    public String testProtected(@RequestHeader("Authorization") String authHeader) {
        if (!authHeader.startsWith("Bearer ")) {
            return "Invalid token format!";
        }
        String token = authHeader.substring(7);

        boolean valid = JWTUtil.validateToken(token);
        if (!valid) {
            return "Token invalid!";
        }

        String role = JWTUtil.getRoleFromToken(token);
        String email = JWTUtil.getEmailFromToken(token);
        return "Token is valid. Role = " + role + ", Email = " + email;
    }
}
