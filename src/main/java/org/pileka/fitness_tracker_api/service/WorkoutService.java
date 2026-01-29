package org.pileka.fitness_tracker_api.service;

import org.pileka.fitness_tracker_api.domain.User;
import org.pileka.fitness_tracker_api.dto.workout.CreateUpdateWorkoutDto;
import org.pileka.fitness_tracker_api.dto.workout.ReadWorkoutDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WorkoutService extends BaseService<ReadWorkoutDto, CreateUpdateWorkoutDto, CreateUpdateWorkoutDto, Long> {

    /**
     * Get user's workout by id
     * @param id workout id
     * @param user user entity to whom the workout must belong
     * @return workout DTO or null wrapped in Optional if no workout
     * with this id belonging to this user is found
     * */
    public Optional<ReadWorkoutDto> getById(Long id, User user);

    /**
     * Get user's workouts, optionally according to some filters
     *
     * @param user user entity to whom the workouts must belong
     * @param type string representing one of the WorkoutType values
     * @param startDate start of the date interval
     * @param endDate end of the date interval
     * @param minDuration minimum workout duration
     * @param maxDuration maximum workout duration
     * @return List of workout DTOs that fit the filters
     */
    // Using Optional in parameters because that's what we get from the request in the controllers
    public List<ReadWorkoutDto> getAll(User user,
                                               Optional<String> type,
                                               Optional<LocalDate> startDate,
                                               Optional<LocalDate> endDate,
                                               Optional<Integer> minDuration,
                                               Optional<Integer> maxDuration);

    /**
     * Get user's workouts with pagination, optionally according to filters
     *
     * @param user user entity to whom the workouts must belong
     * @param type string representing one of the WorkoutType values
     * @param startDate start of the date interval
     * @param endDate end of the date interval
     * @param minDuration minimum workout duration
     * @param maxDuration maximum workout duration
     * @param pageable pagination configuration
     * @return Page of workout DTOs that fit the filters
     */
    Page<ReadWorkoutDto> getAll(User user,
                                        Optional<String> type,
                                        Optional<LocalDate> startDate,
                                        Optional<LocalDate> endDate,
                                        Optional<Integer> minDuration,
                                        Optional<Integer> maxDuration,
                                        Pageable pageable);

    /**
     * Update user's workout by id
     *
     * @param id id of the workout to update
     * @param user user to whom the workout must belong
     * @param updateDto DTO containing new values for the workout
     * @return updated workout DTO or null wrapped in Optional if no workout
     * with this id belonging to this user is found
     * */
    Optional<ReadWorkoutDto> update(Long id, User user, CreateUpdateWorkoutDto updateDto);

    /**
     * Delete user's workout by id
     *
     * @param id id of the workout to delete
     * @param user user to whom the workout must belong
     * @return deleted workout DTO or null wrapped in Optional if no workout
     * with this id belonging to this user is found
     */
    Optional<ReadWorkoutDto> delete(Long id, User user);
}
