package com.gym_app.backend.repositories;


import com.gym_app.backend.models.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface WorkoutRepository extends JpaRepository<Workout, UUID> {
}   
