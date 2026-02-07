package org.pileka.fitness_tracker_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.pileka.fitness_tracker_api.domain.Workout;
import org.pileka.fitness_tracker_api.domain.WorkoutType;
import org.pileka.fitness_tracker_api.dto.workout.CreateUpdateWorkoutDto;
import org.pileka.fitness_tracker_api.dto.workout.ReadWorkoutDto;
import org.pileka.fitness_tracker_api.exception.EntityDoesntBelongToUserException;
import org.pileka.fitness_tracker_api.mapper.WorkoutMapper;
import org.pileka.fitness_tracker_api.repository.UserRepository;
import org.pileka.fitness_tracker_api.repository.WorkoutRepository;
import org.pileka.fitness_tracker_api.repository.specification.WorkoutSpecs;
import org.pileka.fitness_tracker_api.service.WorkoutService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkoutServiceImpl implements WorkoutService {
    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;
    private final WorkoutMapper workoutMapper;

    @Override
    public ReadWorkoutDto create(CreateUpdateWorkoutDto createDto, UserDetails userDetails) {
        Workout newWorkout = workoutMapper.toModel(createDto, userRepository.findByUsername(userDetails.getUsername()).get());

        return workoutMapper.toDto(workoutRepository.save(newWorkout));
    }

    @Override
    public Optional<ReadWorkoutDto> findById(Long id, UserDetails userDetails) {
        Optional<Workout> workoutAtId = workoutRepository.findById(id);
        if (workoutAtId.isPresent()) {
            if (workoutAtId.get().getUser().getUsername().equals(userDetails.getUsername())) {
                return Optional.ofNullable(workoutMapper.toDto(workoutAtId.get()));
            }
            else {
                throw new EntityDoesntBelongToUserException("User " + userDetails.getUsername() +
                        " attempted accessing a workout with id " + id + " that doesn't belong to them");
            }
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    public List<ReadWorkoutDto> findAll(UserDetails userDetails,
                                        Optional<WorkoutType> type,
                                        Optional<LocalDate> startDate,
                                        Optional<LocalDate> endDate,
                                        Optional<Integer> minDuration,
                                        Optional<Integer> maxDuration) {
        return workoutRepository.findAll(
                WorkoutSpecs.getFullSpec(
                    userRepository.findByUsername(userDetails.getUsername()).get(),
                    type,
                    startDate,
                    endDate,
                    minDuration,
                    maxDuration
                )
        ).stream().map(workoutMapper::toDto).toList();
    }

    @Override
    public Page<ReadWorkoutDto> findAll(UserDetails userDetails,
                                        Optional<WorkoutType> type,
                                        Optional<LocalDate> startDate,
                                        Optional<LocalDate> endDate,
                                        Optional<Integer> minDuration,
                                        Optional<Integer> maxDuration,
                                        Pageable pageable) {
        return workoutRepository.findAll(
                WorkoutSpecs.getFullSpec(
                        userRepository.findByUsername(userDetails.getUsername()).get(),
                        type,
                        startDate,
                        endDate,
                        minDuration,
                        maxDuration
                ),
                pageable
        ).map(workoutMapper::toDto);
    }

    @Override
    public Optional<ReadWorkoutDto> update(Long id, UserDetails userDetails, CreateUpdateWorkoutDto updateDto) {
        Optional<Workout> workoutAtId = workoutRepository.findById(id);
        if (workoutAtId.isPresent()) {
            if (workoutAtId.get().getUser().getUsername().equals(userDetails.getUsername())) {
                Workout workout = workoutAtId.get();

                workoutMapper.update(updateDto, workout);

                workout = workoutRepository.save(workout);

                return Optional.ofNullable(workoutMapper.toDto(workout));
            }
            else {
                throw new EntityDoesntBelongToUserException("User " + userDetails.getUsername() +
                        " attempted updating a workout with id " + id + " that doesn't belong to them");
            }
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<ReadWorkoutDto> delete(Long id, UserDetails userDetails) {
        Optional<Workout> optionalWorkout = workoutRepository.findById(id);
        if (optionalWorkout.isPresent()) {
            if (optionalWorkout.get().getUser().getUsername().equals(userDetails.getUsername())){
                workoutRepository.delete(optionalWorkout.get());
                return Optional.ofNullable(workoutMapper.toDto(optionalWorkout.get()));
            }
            else {
                throw new EntityDoesntBelongToUserException("User " + userDetails.getUsername() +
                        " attempted deleting a workout with id " + id + " that doesn't belong to them");
            }
        }
        else {
            return Optional.empty();
        }
    }
}
