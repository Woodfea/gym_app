package com.gym_app.backend.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;

@Getter             
@Setter              
@NoArgsConstructor   
@AllArgsConstructor  
public class LoginRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}