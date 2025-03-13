package edu.sabanciuniv.projectbackend.controllers;

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
    public String login(@RequestParam String email,
                        @RequestParam String password) {

        String role = authService.login(email, password);
        if (role != null) {
            // Create JWT token
            String token = JWTUtil.generateToken(role, email);

            // Return JSON string
            return "{ \"message\": \"Login successful.\", \"role\": \"" + role + "\", \"token\": \"" + token + "\" }";
        } else {
            return "{ \"message\": \"Login failed. Invalid email or password!\" }";
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
