package com.gym_app.backend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.util.UUID;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "sets")
public class Set {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_exercise_id", nullable = false)
    private WorkoutExercise workoutExercise;

    @Column(name="reps", nullable = false)
    private Integer reps;

    @Column(name="weight", nullable = false, precision = 5, scale = 2)
    private BigDecimal weight;

    @Column(name="rpe", precision = 2, scale = 1)
    private BigDecimal rpe;

    @Column(name = "set_order")
    private Integer setOrder = 0;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    // --- CONSTRUCTORS ---

    public Set() {
    }

    // --- GETTERS AND SETTERS ---

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public WorkoutExercise getWorkoutExercise() {
        return workoutExercise;
    }

    public void setWorkoutExercise(WorkoutExercise workoutExercise) {
        this.workoutExercise = workoutExercise;
    }

    public Integer getReps() {
        return reps;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getRpe() {
        return rpe;
    }

    public void setRpe(BigDecimal rpe) {
        this.rpe = rpe;
    }

    public Integer getSetOrder() {
        return setOrder;
    }

    public void setSetOrder(Integer setOrder) {
        this.setOrder = setOrder;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}