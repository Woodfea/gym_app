package com.gym_app.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import jakarta.validation.Valid;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.gym_app.backend.models.User;
import com.gym_app.backend.repositories.JwtResponseRepository;
import com.gym_app.backend.repositories.UserRepository;
import com.gym_app.backend.utils.JwtUtils;
import com.gym_app.backend.repositories.LoginRequestRepository;

import java.time.OffsetDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtils JwtUtils;
    
    public AuthController(UserRepository userRepository, JwtUtils JwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.JwtUtils = JwtUtils;
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
    public ResponseEntity<?> authenticateUser(
            @RequestBody LoginRequestRepository loginRequestRepository,
            @RequestHeader("Origin") String origin) {
        
        Optional<User> userOpt = userRepository.findByUsername(loginRequestRepository.getLogin());

        if (userOpt.isEmpty() || !passwordEncoder.matches(loginRequestRepository.getPassword(), userOpt.get().getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: Invalid login or password");
        }

        User user = userOpt.get();
        String token = JwtUtils.generateToken(user.getUsername(), true, origin);
        return ResponseEntity.ok(new JwtResponseRepository(
            token,
            user.getId(),
            origin,
            user.getEmail()
        ));
    }
}