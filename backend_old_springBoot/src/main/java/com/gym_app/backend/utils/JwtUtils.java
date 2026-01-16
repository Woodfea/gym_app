package com.gym_app.backend.utils;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;

/**
 * Classe qui centralise les opérations de validation et de génération d'un token "métier", c'est-à-dire dédié à cette application.
 *
 * 
 */
public class JwtUtils {
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private long expirationTime;
    private final String Issuer = "MY_GYM_APP";

    private Algorithm algorithm;
    private JWTVerifier connectVerifier;

    @PostConstruct
    private void init() {
        this.algorithm = Algorithm.HMAC256(secretKey);
        this.connectVerifier = JWT.require(algorithm).withClaim("connected", true).build();
    }

    /**
     * Check if the token is valid and return the login contained in the token
     *
     * @param token
     * @param origin
     * @return login
     */
    public String verifyToken(String token, @NotNull String origin) throws NullPointerException, JWTVerificationException {
        JWTVerifier authenticationVerifier = JWT.require(algorithm)
                .withIssuer(Issuer)
                .withAudience(origin) // Non-reusable verifier instance
                .build();

        authenticationVerifier.verify(token);
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("sub").asString();
    }

    /**
     * Check if the token contains a connected=true claim
     *
     * @param token
     * @return connected status (boolean)
     */
    public boolean verifyConnect(String token) {
        try {
            connectVerifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    /**
     * Create a signed JWT token
     *
     * @param subject   the login of the user
     * @param connected if the user is connected or not
     * @param origin    the origin of the request
     * @return          the signed token
     * @throws JWTCreationException if the parameters do not allow to create a token
     */
    public String generateToken(String subject, boolean connected, String origin) throws JWTCreationException {
        long currentTime = System.currentTimeMillis();
        return JWT.create()
                .withIssuer(Issuer)
                .withSubject(subject)
                .withAudience(origin)
                .withClaim("connected", connected)
                .withExpiresAt(new Date(currentTime + expirationTime))
                .sign(algorithm);
    }

}
