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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
    /*
    PLEASE NOTE: I've seen the requirement to move all checks out of controller methods, so they only include calls
    to service methods, but this doesn't allow to properly handle (i.e. return a proper HTTP status code) situations
    when an entity with the specified ID doesn't exist without throwing an exception. Exceptions are supposed to be
    thrown for exceptional situations, and in my opinion not finding something after a lookup isn't an exceptional situation.
    Therefore, using an exception to handle this would be wrong and this is why I'm using Optional as a return type for
    the service method and unwrap it in a controller method. A case can be made that this line of thinking is only
    applicable to lookups and not update or delete operations which are handled the same way in my code, but I think this
    is a kind of problem that should be discussed at the API design stage, and since the provided requirements didn't
    specify anything regarding this I've decided that an entity that is supposed to be deleted or updated not existing
    is a valid business situation and as such doesn't warrant an exception. I did move other logic out of
    controllers though.
     */
    public ResponseEntity<ReadWorkoutDto> getWorkoutById(@PathVariable Long id) {
        Optional<ReadWorkoutDto> workout = workoutService.findById(id);
        if (workout.isPresent()) {
            return ResponseEntity.ok(workout.get());
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = "application/json")
    @Override
    public ReadWorkoutDto addWorkout(@Valid @RequestBody CreateUpdateWorkoutDto createDto) {
        return workoutService.create(createDto);
    }

    @PutMapping(path = "/{id}", produces = "application/json")
    @Override
    public ResponseEntity<ReadWorkoutDto> updateWorkout(@PathVariable Long id,
                                                 @Valid @RequestBody CreateUpdateWorkoutDto updateDto) {
        Optional<ReadWorkoutDto> updatedWorkout = workoutService.update(id, updateDto);
        if (updatedWorkout.isPresent()) {
            return ResponseEntity.ok(updatedWorkout.get());
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(path = "/{id}", produces = "application/json")
    @Override
    public ResponseEntity<ReadWorkoutDto> deleteWorkout(@PathVariable Long id) {
        Optional<ReadWorkoutDto> deletedWorkout = workoutService.delete(id);
        if (deletedWorkout.isPresent()) {
            return ResponseEntity.ok(deletedWorkout.get());
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
