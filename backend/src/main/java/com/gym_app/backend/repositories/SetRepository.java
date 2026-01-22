package com.gym_app.backend.repositories;

import com.gym_app.backend.models.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SetRepository extends JpaRepository<Set, UUID> {
}   
