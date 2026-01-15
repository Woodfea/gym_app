package com.gym_app.backend.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

@Service
public class JwtService {
    
    @Value("${jwt.secret}")
    private String secretKey;
    
    @Value("${jwt.expiration}")
    private long expirationTime;
    
    private static final String ALGORITHM = "HmacSHA256";
    
    /**
     * Generate a JWT token for the given user
     */
    public String generateToken(String username, UUID userId, String email) {
        try {
            // 1. Create header
            String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
            String encodedHeader = Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(header.getBytes(StandardCharsets.UTF_8));
            
            // 2. Create payload (claims)
            long currentTime = System.currentTimeMillis();
            long tokenExpirationTime = currentTime + expirationTime;
            String payload = "{\"sub\":\"" + username + "\"," +
                    "\"userId\":\"" + userId + "\"," +
                    "\"email\":\"" + email + "\"," +
                    "\"iat\":" + (currentTime / 1000) + "," +
                    "\"exp\":" + (tokenExpirationTime / 1000) + "}";
            String encodedPayload = Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(payload.getBytes(StandardCharsets.UTF_8));
            
            // 3. Create signature
            String message = encodedHeader + "." + encodedPayload;
            String signature = generateSignature(message);
            
            // 4. Return complete JWT token
            return message + "." + signature;
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate JWT token", e);
        }
    }
    
    /**
     * Validate JWT token and return username if valid
     */
    public String validateToken(String token) {
        try {
            if (token == null || token.isEmpty()) {
                return null;
            }
            
            // Split token into parts
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return null;
            }
            
            // Verify signature
            String message = parts[0] + "." + parts[1];
            String expectedSignature = generateSignature(message);
            
            if (!expectedSignature.equals(parts[2])) {
                return null; // Invalid signature
            }
            
            // Decode and parse payload
            String decodedPayload = new String(
                    Base64.getUrlDecoder().decode(parts[1]),
                    StandardCharsets.UTF_8
            );
            
            // Extract expiration time
            int expIndex = decodedPayload.indexOf("\"exp\":");
            if (expIndex == -1) {
                return null;
            }
            
            String expPart = decodedPayload.substring(expIndex + 6);
            long expirationTime = Long.parseLong(expPart.split("}")[0].split(",")[0]);
            
            // Check if token is expired
            long currentTime = System.currentTimeMillis() / 1000;
            if (currentTime > expirationTime) {
                return null; // Token expired
            }
            
            // Extract username
            int subIndex = decodedPayload.indexOf("\"sub\":\"");
            if (subIndex == -1) {
                return null;
            }
            
            int startIndex = subIndex + 7;
            int endIndex = decodedPayload.indexOf("\"", startIndex);
            return decodedPayload.substring(startIndex, endIndex);
            
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Generate HMAC-SHA256 signature
     */
    private String generateSignature(String message) throws Exception {
        Mac mac = Mac.getInstance(ALGORITHM);
        SecretKeySpec secretKeySpec = new SecretKeySpec(
                secretKey.getBytes(StandardCharsets.UTF_8),
                0,
                secretKey.getBytes(StandardCharsets.UTF_8).length,
                ALGORITHM
        );
        mac.init(secretKeySpec);
        byte[] signatureBytes = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(signatureBytes);
    }
}
