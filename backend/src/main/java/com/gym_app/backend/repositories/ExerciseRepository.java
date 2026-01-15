package com.gym_app.backend.repositories;

import com.gym_app.backend.models.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ExerciseRepository extends JpaRepository<Exercise, UUID> {
    
}
