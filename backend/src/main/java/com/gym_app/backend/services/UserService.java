package com.gym_app.backend.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gym_app.backend.exceptions.EmailAlreadyExistsException;
import com.gym_app.backend.exceptions.UserAlreadyExistsException;
import com.gym_app.backend.models.User;
import com.gym_app.backend.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(String username, String password, String email, String icon_path) {
        Optional<User> usernameAlreadyExist = userRepository.findByUsername(username);
        Optional<User> emailAlreadyExist = userRepository.findByEmail(email);

        if (usernameAlreadyExist.isPresent()) {
            throw new UserAlreadyExistsException("username: " + usernameAlreadyExist.get().getUsername() + " is already subscribed");
        }

        else if (emailAlreadyExist.isPresent()) {
            throw new EmailAlreadyExistsException("email: " + emailAlreadyExist.get().getEmail() + " is already subscribed");
        }

        String passwordHash = passwordEncoder.encode(password);
        User user = new User(username, passwordHash, email, icon_path);
        userRepository.save(user);
    }
}