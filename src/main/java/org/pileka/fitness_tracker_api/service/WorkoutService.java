package org.pileka.fitness_tracker_api.service;

import org.pileka.fitness_tracker_api.domain.WorkoutType;
import org.pileka.fitness_tracker_api.dto.workout.CreateUpdateWorkoutDto;
import org.pileka.fitness_tracker_api.dto.workout.ReadWorkoutDto;
import org.pileka.fitness_tracker_api.dto.workout.WorkoutSpecDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WorkoutService {

    /**
     * Create a new workout belonging to user
     *
     * @param createDto DTO containing values for the new workout
     * @return A DTO representing the newly created workout
     */
    ReadWorkoutDto create(CreateUpdateWorkoutDto createDto);

    /**
     * Get user's workout by id
     *
     * @param id workout id
     * @return workout DTO
     * */
    public ReadWorkoutDto findById(Long id);

    /**
     * Get user's workouts with pagination, optionally according to filters
     *
     * @param specDto search specification dto
     * @param pageable pagination configuration
     * @return Page of workout DTOs that fit the filters
     */
    Page<ReadWorkoutDto> findAll(WorkoutSpecDto specDto, Pageable pageable);

    /**
     * Update user's workout by id
     *
     * @param id id of the workout to update
     * @param updateDto DTO containing new values for the workout
     * @return updated workout DTO
     * */
    ReadWorkoutDto update(Long id, CreateUpdateWorkoutDto updateDto);

    /**
     * Delete user's workout by id
     *
     * @param id id of the workout to delete
     * @return deleted workout DTO
     */
    ReadWorkoutDto delete(Long id);
}
