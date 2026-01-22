package com.gym_app.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gym_app.backend.models.User;
import com.gym_app.backend.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository; // Ton interface JpaRepository

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(String username, String password, String email, String icon_path) {
        String passwordHash = passwordEncoder.encode(password);
        User user = new User(username, passwordHash, email, icon_path);
        userRepository.save(user);
    }
}