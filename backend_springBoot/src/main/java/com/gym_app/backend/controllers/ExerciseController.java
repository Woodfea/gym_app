package com.gym_app.backend.controllers;

import com.gym_app.backend.models.Exercise;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gym_app.backend.repositories.ExerciseRepository;

import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class ExerciseController {

    private final ExerciseRepository exerciseRepository;

    public ExerciseController(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    @GetMapping("/exercises")
    public List<Exercise> getAllExercises() {
        return exerciseRepository.findAll();
    }

    @PostMapping("/new-exercise")
    public ResponseEntity<?> createExercise(@Valid @RequestBody Exercise exercise, HttpServletRequest request) {
        try {
            // Set created_by and created_at if not present
            if (exercise.getCreated_by() == null || exercise.getCreated_by().isEmpty()) {
                String username = (String) request.getAttribute("username");
                exercise.setCreated_by(username);
            }
            
            // Set updated_by and updated_at
            if (exercise.getUpdated_by() == null || exercise.getUpdated_by().isEmpty()) {
                String username = (String) request.getAttribute("username");
                exercise.setUpdated_by(username);
            }
            
            Exercise savedExercise = exerciseRepository.save(exercise);
            return ResponseEntity.ok(savedExercise);
            
        } catch (Exception e) {
            // Handle duplicate name or other database constraints
            if (e.getMessage().contains("unique constraint") || e.getMessage().contains("duplicate")) {
                return ResponseEntity.badRequest().body("Error: Exercise name already exists");
            }
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}