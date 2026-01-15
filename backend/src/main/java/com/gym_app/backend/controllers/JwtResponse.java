package com.gym_app.backend.controllers;

import java.util.UUID;

public record JwtResponse(String token, UUID id, String username, String email) {
}
