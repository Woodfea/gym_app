package com.gym_app.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import jakarta.validation.Valid;

import com.gym_app.backend.models.User;
import com.gym_app.backend.repositories.UserRepository;
import com.gym_app.backend.services.JwtService;
import java.time.OffsetDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    
    public AuthController(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.jwtService = jwtService;
    }

    // --- REGISTER ---
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: the username (" + user.getUsername() + ") is already in use!");
        }

        else if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: the email (" + user.getEmail() + ") is already in use!");
        }

        if (user.getPasswordHash() == null || user.getPasswordHash().length() < 6) {
            return ResponseEntity.badRequest().body("Error: the password must be at least 6 characters long!");
        }

        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));

        // Set created_by to username and created_at to current time if not present
        if (user.getCreated_by() == null || user.getCreated_by().isEmpty()) {
            user.setCreated_by(user.getUsername());
        }
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(OffsetDateTime.now());
        }

         // Set created_by to username and created_at to current time if not present
        if (user.getUpdated_by() == null || user.getUpdated_by().isEmpty()) {
            user.setUpdated_by(user.getUsername());
        }
        if (user.getUpdatedAt() == null) {
            user.setUpdatedAt(OffsetDateTime.now());
        }

        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully!");
    }

    // --- LOGIN ---
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        
        Optional<User> userOpt = userRepository.findByUsername(loginRequest.username());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            
            // Verify password matches the hash in database
            if (passwordEncoder.matches(loginRequest.password(), user.getPasswordHash())) {
                
                // Generate real JWT token
                String token = jwtService.generateToken(user.getUsername(), user.getId(), user.getEmail());
                
                return ResponseEntity.ok(new JwtResponse(
                        token, 
                        user.getId(), 
                        user.getUsername(), 
                        user.getEmail()
                ));
            }
        }

        // Authentication failed
        return ResponseEntity.status(401).body("Error: Invalid credentials");
    }
}