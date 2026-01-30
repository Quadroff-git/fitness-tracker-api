package org.pileka.fitness_tracker_api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.pileka.fitness_tracker_api.domain.WorkoutType;
import org.pileka.fitness_tracker_api.dto.workout.CreateUpdateWorkoutDto;
import org.pileka.fitness_tracker_api.dto.workout.ReadWorkoutDto;
import org.pileka.fitness_tracker_api.service.WorkoutService;
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

@RestController
@RequestMapping("api/workouts")
@RequiredArgsConstructor
public class WorkoutController {

    private final WorkoutService workoutService;

    @GetMapping
    Page<ReadWorkoutDto> getWorkouts(@AuthenticationPrincipal UserDetails userDetails,
                                     @RequestParam Optional<WorkoutType> type,
                                     @RequestParam Optional<LocalDate> startDate,
                                     @RequestParam Optional<LocalDate> endDate,
                                     @RequestParam Optional<Integer> minDuration,
                                     @RequestParam Optional<Integer> maxDuration,
                                     @PageableDefault(size = 20, sort = "date", direction = Sort.Direction.DESC) Pageable pageable) {
        return workoutService.findAll(userDetails,
                type,
                startDate,
                endDate,
                minDuration,
                maxDuration,
                pageable);
    }

    @GetMapping(path = "/{id}")
    ResponseEntity<ReadWorkoutDto> getWorkoutById(@AuthenticationPrincipal UserDetails userDetails,
                                                  @PathVariable Long id) {
        Optional<ReadWorkoutDto> workout = workoutService.findById(id, userDetails);
        if (workout.isPresent()) {
            return ResponseEntity.ok(workout.get());
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    ReadWorkoutDto addWorkout(@AuthenticationPrincipal UserDetails userDetails,
                              @Valid @RequestBody CreateUpdateWorkoutDto createDto) {
        return workoutService.create(createDto, userDetails);
    }

    @PutMapping(path = "/{id}")
    ResponseEntity<ReadWorkoutDto> updateWorkout(@AuthenticationPrincipal UserDetails userDetails,
                                                 @PathVariable Long id,
                                                 @Valid @RequestBody CreateUpdateWorkoutDto updateDto) {
        Optional<ReadWorkoutDto> updatedWorkout = workoutService.update(id, userDetails, updateDto);
        if (updatedWorkout.isPresent()) {
            return ResponseEntity.ok(updatedWorkout.get());
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(path = "/{id}")
    ResponseEntity<ReadWorkoutDto> deleteWorkout(@AuthenticationPrincipal UserDetails userDetails,
                                                 @PathVariable Long id) {
        Optional<ReadWorkoutDto> deletedWorkout = workoutService.delete(id, userDetails);
        if (deletedWorkout.isPresent()) {
            return ResponseEntity.ok(deletedWorkout.get());
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
