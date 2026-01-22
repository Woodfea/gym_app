package com.gym_app.backend.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gym_app.backend.dto.LoginRequest;
import com.gym_app.backend.dto.RegisterRequest;
import com.gym_app.backend.models.User;
import com.gym_app.backend.repositories.UserRepository;
import com.gym_app.backend.services.JwtService;

import com.gym_app.backend.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired private JwtService jwtService;
    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;


    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request) {
        Optional<User> user = userRepository.findByUsername(request.getUsername());
        
        if (!user.isPresent()) {
            return ResponseEntity.badRequest().body("Wrong username"); 
        }

        if (!passwordEncoder.matches(request.getPassword(), user.get().getPasswordHash())) {
            return ResponseEntity.badRequest().body("Wrong password");
        }

        return ResponseEntity.ok(jwtService.generateToken(request.getUsername()));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        try {
            userService.register(request.getUsername(), request.getPassword(), request.getEmail(), request.getIcon_path());
            return ResponseEntity.ok("Utilisateur créé avec succès !");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error during the subcription");
        }
    }
}