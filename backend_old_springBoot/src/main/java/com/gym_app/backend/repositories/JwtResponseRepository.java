package com.gym_app.backend.repositories;

import java.util.UUID;

public record JwtResponseRepository(String token, UUID id, String username, String email) {
}
