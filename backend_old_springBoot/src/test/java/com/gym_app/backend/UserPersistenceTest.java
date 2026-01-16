package com.gym_app.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.OffsetDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.gym_app.backend.models.User;
import com.gym_app.backend.repositories.UserRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
class UserPersistenceTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveAndRetrieveUser() {
        // 1. Arrange (Setup)
        User user = new User();
        OffsetDateTime now = OffsetDateTime.now();
        user.setUsername("camarade_guy");
        user.setEmail("guy@exemple.com");
        user.setPasswordHash("hash123");
        user.setIconPath("/icons/guy.png");
        user.setCreated_by("admin");
        user.setCreatedAt(now);
        user.setUpdated_by("admina");
        user.setUpdatedAt(now);

        // 2. Act (Action)
        User savedUser = userRepository.save(user);

        // 3. Assert (Verification)
        assertNotNull(savedUser.getId());
        assertEquals("camarade_guy", userRepository.findById(savedUser.getId()).get().getUsername());
        assertEquals("guy@exemple.com", userRepository.findById(savedUser.getId()).get().getEmail());
        assertEquals("hash123", userRepository.findById(savedUser.getId()).get().getPasswordHash());
        assertEquals("/icons/guy.png", userRepository.findById(savedUser.getId()).get().getIconPath());
        assertEquals("admin", userRepository.findById(savedUser.getId()).get().getCreated_by());
        assertEquals("admina", userRepository.findById(savedUser.getId()).get().getUpdated_by());
        assertEquals(now, userRepository.findById(savedUser.getId()).get().getCreatedAt());
        assertEquals(now, userRepository.findById(savedUser.getId()).get().getUpdatedAt());
    }
}