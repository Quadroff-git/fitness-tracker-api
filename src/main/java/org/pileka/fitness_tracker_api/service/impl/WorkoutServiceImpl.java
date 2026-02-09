package org.pileka.fitness_tracker_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.pileka.fitness_tracker_api.domain.Workout;
import org.pileka.fitness_tracker_api.dto.workout.CreateUpdateWorkoutDto;
import org.pileka.fitness_tracker_api.dto.workout.ReadWorkoutDto;
import org.pileka.fitness_tracker_api.dto.workout.WorkoutSpecDto;
import org.pileka.fitness_tracker_api.exception.EntityDoesntBelongToUserException;
import org.pileka.fitness_tracker_api.mapper.WorkoutMapper;
import org.pileka.fitness_tracker_api.repository.UserRepository;
import org.pileka.fitness_tracker_api.repository.WorkoutRepository;
import org.pileka.fitness_tracker_api.repository.specification.WorkoutSpecs;
import org.pileka.fitness_tracker_api.security.AuthUserUtil;
import org.pileka.fitness_tracker_api.service.WorkoutService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkoutServiceImpl implements WorkoutService {
    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;
    private final WorkoutMapper workoutMapper;

    @Override
    public ReadWorkoutDto create(CreateUpdateWorkoutDto createDto) {
        Workout newWorkout = workoutMapper.toModel(createDto,
                userRepository.findByUsername(
                        AuthUserUtil.getCurrentUser().getUsername()
                ).get());

        return workoutMapper.toDto(workoutRepository.save(newWorkout));
    }

    @Override
    public Optional<ReadWorkoutDto> findById(Long id) {
        Optional<Workout> workoutAtId = workoutRepository.findById(id);
        UserDetails userDetails = AuthUserUtil.getCurrentUser();

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
    public Page<ReadWorkoutDto> findAll(WorkoutSpecDto specDto, Pageable pageable) {
        return workoutRepository.findAll(
                WorkoutSpecs.getFullSpec(
                        userRepository.findByUsername(
                                AuthUserUtil.getCurrentUser().getUsername()
                        ).get(),
                        specDto
                ),
                pageable
        ).map(workoutMapper::toDto);
    }

    @Override
    public Optional<ReadWorkoutDto> update(Long id, CreateUpdateWorkoutDto updateDto) {
        Optional<Workout> workoutAtId = workoutRepository.findById(id);
        UserDetails userDetails = AuthUserUtil.getCurrentUser();

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
    public Optional<ReadWorkoutDto> delete(Long id) {
        Optional<Workout> optionalWorkout = workoutRepository.findById(id);
        UserDetails userDetails = AuthUserUtil.getCurrentUser();

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
