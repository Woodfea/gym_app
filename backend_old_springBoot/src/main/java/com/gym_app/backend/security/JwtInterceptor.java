package com.gym_app.backend.security;

import com.gym_app.backend.services.JwtService;
import com.gym_app.backend.repositories.UserRepository;
import com.gym_app.backend.models.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.method.HandlerMethod;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    
    private final JwtService jwtService;
    private final UserRepository userRepository;
    
    public JwtInterceptor(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Skip token check for login and register endpoints
        String requestPath = request.getRequestURI();
        if (requestPath.contains("/api/auth/login") || requestPath.contains("/api/auth/register")) {
            return true;
        }
        
        // Get token from Authorization header
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"Missing or invalid Authorization header\"}");
            return false;
        }
        
        // Extract token (remove "Bearer " prefix)
        String token = authHeader.substring(7);
        
        // Validate token
        String username = jwtService.validateToken(token);
        if (username == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"Invalid or expired token\"}");
            return false;
        }
        
        // Check if method requires ADMIN role
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            RequireAdmin requireAdmin = handlerMethod.getMethodAnnotation(RequireAdmin.class);
            
            if (requireAdmin != null) {
                // Check if user is admin
                Optional<User> userOpt = userRepository.findByUsername(username);
                if (userOpt.isEmpty() || !userOpt.get().getRole().equals("ADMIN")) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("{\"error\":\"Admin access required\"}");
                    return false;
                }
            }
        }
        
        // Store username in request attribute for later use
        request.setAttribute("username", username);
        return true;
    }
}

