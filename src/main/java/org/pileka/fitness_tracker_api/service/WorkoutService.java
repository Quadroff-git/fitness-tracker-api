package org.pileka.fitness_tracker_api.service;

import org.pileka.fitness_tracker_api.domain.WorkoutType;
import org.pileka.fitness_tracker_api.dto.workout.CreateUpdateWorkoutDto;
import org.pileka.fitness_tracker_api.dto.workout.ReadWorkoutDto;
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
    ReadWorkoutDto findById(Long id);

    /**
     * Get userDetails's workouts, optionally according to some filters
     *
     * @param type string representing one of the WorkoutType values
     * @param startDate start of the date interval
     * @param endDate end of the date interval
     * @param minDuration minimum workout duration
     * @param maxDuration maximum workout duration
     * @return List of workout DTOs that fit the filters
     */
    // Using Optional in parameters because that's what we get from the request in the controllers
    List<ReadWorkoutDto> findAll(Optional<WorkoutType> type,
                                        Optional<LocalDate> startDate,
                                        Optional<LocalDate> endDate,
                                        Optional<Integer> minDuration,
                                        Optional<Integer> maxDuration);

    /**
     * Get user's workouts with pagination, optionally according to filters
     *
     * @param type string representing one of the WorkoutType values
     * @param startDate start of the date interval
     * @param endDate end of the date interval
     * @param minDuration minimum workout duration
     * @param maxDuration maximum workout duration
     * @param pageable pagination configuration
     * @return Page of workout DTOs that fit the filters
     */
    Page<ReadWorkoutDto> findAll(Optional<WorkoutType> type,
                                        Optional<LocalDate> startDate,
                                        Optional<LocalDate> endDate,
                                        Optional<Integer> minDuration,
                                        Optional<Integer> maxDuration,
                                        Pageable pageable);

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
