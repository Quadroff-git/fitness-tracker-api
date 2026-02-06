package org.pileka.fitness_tracker_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.pileka.fitness_tracker_api.domain.WorkoutType;
import org.pileka.fitness_tracker_api.dto.workout.CreateUpdateWorkoutDto;
import org.pileka.fitness_tracker_api.dto.workout.ReadWorkoutDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

public interface WorkoutController {
    @GetMapping(produces = "application/json")
    @Operation(summary = "Get user's workouts",
            tags = {"workouts"},
            description = "Returns user's workouts with filtering by type, date and duration intervals, sorting and pagination. " +
                    "Despite pageable being marked as required there are actually defaults defined for it which the doc generation tool" +
                    "doesn't register",
            responses = {@ApiResponse(description = "Workouts")}
    )
    Page<ReadWorkoutDto> getWorkouts(@AuthenticationPrincipal UserDetails userDetails,
                                     @RequestParam Optional<WorkoutType> type,
                                     @RequestParam Optional<LocalDate> startDate,
                                     @RequestParam Optional<LocalDate> endDate,
                                     @RequestParam Optional<Integer> minDuration,
                                     @RequestParam Optional<Integer> maxDuration,
                                     @PageableDefault(size = 20, sort = "date", direction = Sort.Direction.DESC) Pageable pageable);

    @GetMapping(path = "/{id}", produces = "application/json")
    @Operation(summary = "Get user's workout by id",
            tags = {"workouts"},
            description = "Returns user's workout with specified id if one exists",
            responses = {
                    @ApiResponse(description = "The workout"),
                    @ApiResponse(responseCode = "404", description = "Workout not found")}
    )
    ResponseEntity<ReadWorkoutDto> getWorkoutById(@AuthenticationPrincipal UserDetails userDetails,
                                                  @PathVariable Long id);

    @PostMapping(produces = "application/json")
    @Operation(summary = "Add workout",
            tags = {"workouts"},
            description = "Adds a new workout associated with the user and returns a representation of the " +
                    "newly created workout",
            responses = {@ApiResponse(description = "The created workout")}
    )
    ReadWorkoutDto addWorkout(@AuthenticationPrincipal UserDetails userDetails,
                              @Valid @RequestBody CreateUpdateWorkoutDto createDto);

    @PutMapping(path = "/{id}", produces = "application/json")
    @Operation(summary = "Update workout",
            tags = {"workouts"},
            description = "Updates a workout with the specified id if one exists and belongs to the user and returns " +
                    "the updated workout",
            responses = {
                    @ApiResponse(description = "The updated workout"),
                    @ApiResponse(responseCode = "404", description = "Workout not found")}
    )
    ResponseEntity<ReadWorkoutDto> updateWorkout(@AuthenticationPrincipal UserDetails userDetails,
                                                 @PathVariable Long id,
                                                 @Valid @RequestBody CreateUpdateWorkoutDto updateDto);

    @DeleteMapping(path = "/{id}", produces = "application/json")
    @Operation(summary = "Delete workout",
            tags = {"workouts"},
            description = "Updates a workout with the specified id if one exists and belongs to the user and returns" +
                    "the representation of the deleted workout",
            responses = {
                    @ApiResponse(description = "The deleted workout"),
                    @ApiResponse(responseCode = "404", description = "Workout not found")}
    )
    ResponseEntity<ReadWorkoutDto> deleteWorkout(@AuthenticationPrincipal UserDetails userDetails,
                                                 @PathVariable Long id);
}
