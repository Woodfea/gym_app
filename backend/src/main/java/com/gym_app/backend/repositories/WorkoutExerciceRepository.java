package com.gym_app.backend.repositories;


import com.gym_app.backend.models.WorkoutExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface WorkoutExerciceRepository extends JpaRepository<WorkoutExercise, UUID> {
}   
