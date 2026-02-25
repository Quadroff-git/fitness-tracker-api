package org.pileka.fitness_tracker_api.controller.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.pileka.fitness_tracker_api.controller.WorkoutController;
import org.pileka.fitness_tracker_api.dto.workout.CreateUpdateWorkoutDto;
import org.pileka.fitness_tracker_api.dto.workout.ReadWorkoutDto;
import org.pileka.fitness_tracker_api.dto.workout.WorkoutSpecDto;
import org.pileka.fitness_tracker_api.service.WorkoutService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/workouts")
@RequiredArgsConstructor
public class WorkoutControllerImpl implements WorkoutController {

    private final WorkoutService workoutService;

    @GetMapping(produces = "application/json")
    @Override
    public Page<ReadWorkoutDto> getWorkouts(@Valid WorkoutSpecDto workoutSpecDto,
                                            @PageableDefault(size = 20, sort = "date", direction = Sort.Direction.DESC) Pageable pageable) {
        return workoutService.findAll(workoutSpecDto, pageable);
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    @Override
    public ReadWorkoutDto getWorkoutById(@PathVariable Long id) {
        return workoutService.findById(id);
    }

    @PostMapping(produces = "application/json")
    @Override
    public ReadWorkoutDto addWorkout(@Valid @RequestBody CreateUpdateWorkoutDto createDto) {
        return workoutService.create(createDto);
    }

    @PutMapping(path = "/{id}", produces = "application/json")
    @Override
    public ReadWorkoutDto updateWorkout(@PathVariable Long id,
                                                 @Valid @RequestBody CreateUpdateWorkoutDto updateDto) {
        return workoutService.update(id, updateDto);
    }

    @DeleteMapping(path = "/{id}", produces = "application/json")
    @Override
    public ReadWorkoutDto deleteWorkout(@PathVariable Long id) {
        return workoutService.delete(id);
    }
}
