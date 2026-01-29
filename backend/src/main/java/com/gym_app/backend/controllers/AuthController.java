package com.gym_app.backend.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gym_app.backend.dto.LoginRequest;
import com.gym_app.backend.dto.RegisterRequest;

import com.gym_app.backend.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired private UserService userService;
    
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request) {
        String tokenJWT = userService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(tokenJWT);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        userService.register(request.getUsername(), request.getPassword(), request.getEmail(), request.getIcon_path());
        return new ResponseEntity<String>("User created with success !", HttpStatus.CREATED);
    }
}