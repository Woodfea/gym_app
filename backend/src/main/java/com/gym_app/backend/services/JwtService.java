package com.gym_app.backend.services;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

@Service
public class JwtService {
    private final Algorithm algorithm = Algorithm.HMAC256("ma_cle_secrete_super_longue");

    public String generateToken(String username) {
        String token = "";
        try {
            token = JWT.create()
                .withIssuer("gym-app-server")
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + 86400000)) // 1 jour
                .sign(algorithm);
            return token;
        } catch (JWTCreationException exception){
            // Invalid Signing configuration / Couldn't convert Claims.
        }
        return token;
    }

    public String validateTokenAndGetUsername(String token) {
        return JWT.require(algorithm).build().verify(token).getSubject();
    }
}