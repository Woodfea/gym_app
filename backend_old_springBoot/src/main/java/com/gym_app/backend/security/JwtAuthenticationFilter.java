package com.gym_app.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.gym_app.backend.services.JwtService;

import java.io.IOException;
import java.util.Collection;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService = new JwtService();

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // 1. Inspect the Header (Material conditions of the request)
        final String authHeader = request.getHeader("Authorization");

        // If no header or doesn't start with "Bearer ", pass the request down the chain.
        // It will be rejected later by the SecurityFilterChain if the endpoint requires auth.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);

        final String username = jwtService.validateToken(jwt);
        if (username == null) {
            filterChain.doFilter(request, response);
            return;
        }
        
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
             UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                username,
                null,
                null // No roles/authorities for now
             );
             SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        // Pass the request to the next filter (eventually reaching the DispatcherServlet)
        filterChain.doFilter(request, response);
    }
}