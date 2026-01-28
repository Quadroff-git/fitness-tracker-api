package org.pileka.fitness_tracker_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.pileka.fitness_tracker_api.domain.Workout;
import org.pileka.fitness_tracker_api.domain.WorkoutType;
import org.pileka.fitness_tracker_api.dto.workout.CreateUpdateWorkoutDto;
import org.pileka.fitness_tracker_api.dto.workout.ReadWorkoutDto;
import org.pileka.fitness_tracker_api.exception.EntityDoesntBelongToUserException;
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
    private final ModelMapper modelMapper;

    @Override
    public ReadWorkoutDto create(CreateUpdateWorkoutDto createDto, UserDetails userDetails) {
        // Most of the mapping is done by ModelMapper, but User is injected manually
        Workout newWorkout = modelMapper.map(createDto, Workout.class);
        newWorkout.setUser(userRepository.findByUsername(userDetails.getUsername()).get());

        return modelMapper.map(workoutRepository.save(newWorkout), ReadWorkoutDto.class);
    }

    @Override
    public Optional<ReadWorkoutDto> findById(Long id, UserDetails userDetails) {
        Optional<Workout> workoutAtId = workoutRepository.findById(id);
        if (workoutAtId.isPresent()) {
            if (workoutAtId.get().getUser().equals(userDetails)) {
                return Optional.ofNullable(modelMapper.map(workoutAtId, ReadWorkoutDto.class));
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
        ).stream().map(workout -> modelMapper.map(workout, ReadWorkoutDto.class)).toList();
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
        ).map(workout -> modelMapper.map(workout, ReadWorkoutDto.class));
    }

    @Override
    public Optional<ReadWorkoutDto> update(Long id, UserDetails userDetails, CreateUpdateWorkoutDto updateDto) {
        Optional<Workout> workoutAtId = workoutRepository.findById(id);
        if (workoutAtId.isPresent()) {
            if (workoutAtId.get().getUser().equals(userDetails)) {
                Workout workout = workoutAtId.get();

                workout.setName(updateDto.getName());
                workout.setType(updateDto.getType());
                workout.setDate(updateDto.getDate());
                workout.setDuration(updateDto.getDuration());
                workout.setCalories(updateDto.getCalories());

                workout = workoutRepository.save(workout);

                return Optional.ofNullable(modelMapper.map(workout, ReadWorkoutDto.class));
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
            if (optionalWorkout.get().getUser().equals(userDetails)){
                workoutRepository.delete(optionalWorkout.get());
                return Optional.ofNullable(modelMapper.map(optionalWorkout, ReadWorkoutDto.class));
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
