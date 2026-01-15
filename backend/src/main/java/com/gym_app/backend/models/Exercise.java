package com.gym_app.backend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.util.UUID;
import java.time.OffsetDateTime;

@Entity
@Table(name = "exercises")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "name", unique = true, nullable = false, length = 100)
    private String name;

    @Column(name = "muscle_group", nullable = false, length = 50)
    private String muscleGroup;

    @Column(name = "created_by", length = 50)
    private String created_by;

    @Column(name = "created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_by", length = 50)
    private String updated_by;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt = OffsetDateTime.now();

      // --- Constructors ---
    public Exercise() {
    }

    // --- GETTERS AND SETTERS ---
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getMuscleGroup() {
        return muscleGroup;
    }
    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }

    public String getCreated_by() {
        return created_by;
    }
    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdated_by() {
        return updated_by;
    }
    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}