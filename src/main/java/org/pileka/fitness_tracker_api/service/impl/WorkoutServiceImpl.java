package org.pileka.fitness_tracker_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.pileka.fitness_tracker_api.domain.User;
import org.pileka.fitness_tracker_api.domain.Workout;
import org.pileka.fitness_tracker_api.dto.workout.CreateWorkoutDto;
import org.pileka.fitness_tracker_api.dto.workout.ReadWorkoutDto;
import org.pileka.fitness_tracker_api.dto.workout.UpdateWorkoutDto;
import org.pileka.fitness_tracker_api.repository.WorkoutRepository;
import org.pileka.fitness_tracker_api.service.WorkoutService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkoutServiceImpl implements WorkoutService {
    private final WorkoutRepository workoutRepository;
    private final ModelMapper modelMapper;

    @Override
    public ReadWorkoutDto create(CreateWorkoutDto createDto) {
        return null;
    }

    @Override
    public Optional<ReadWorkoutDto> findById(Long id) {
        return Optional.ofNullable(modelMapper.map(workoutRepository.findById(id), ReadWorkoutDto.class));
    }

    @Override
    public Optional<ReadWorkoutDto> findById(Long id, User user) {
        Optional<Workout> workoutAtId = workoutRepository.findById(id);
        if (workoutAtId.isPresent() && workoutAtId.get().getUser().equals(user)) {
            return Optional.ofNullable(modelMapper.map(workoutAtId, ReadWorkoutDto.class));
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    public List<ReadWorkoutDto> findAll() {
        return workoutRepository.findAll().stream().map(workout -> modelMapper.map(workout, ReadWorkoutDto.class)).toList();
    }

    @Override
    public Page<ReadWorkoutDto> findAll(Pageable pageable) {
        return workoutRepository.findAll(pageable).map(workout -> modelMapper.map(workout, ReadWorkoutDto.class));
    }

    @Override
    public List<ReadWorkoutDto> findAll(User user, Optional<String> type, Optional<LocalDate> startDate, Optional<LocalDate> endDate, Optional<Integer> minDuration, Optional<Integer> maxDuration) {
        return List.of();
    }

    @Override
    public Page<ReadWorkoutDto> findAll(User user, Optional<String> type, Optional<LocalDate> startDate, Optional<LocalDate> endDate, Optional<Integer> minDuration, Optional<Integer> maxDuration, Pageable pageable) {
        return null;
    }

    @Override
    public boolean existsById(Long id) {
        return workoutRepository.existsById(id);
    }

    @Override
    public Optional<ReadWorkoutDto> update(Long id, User user, UpdateWorkoutDto updateDto) {
        Optional<Workout> workoutAtId = workoutRepository.findById(id);
        if (workoutAtId.isPresent() && workoutAtId.get().getUser().equals(user)) {
            Workout workout = workoutAtId.get();

            workout.setName(updateDto.getName());
            workout.setType(updateDto.getType());
            workout.setDate(updateDto.getDate());
            workout.setDuration(updateDto.getDuration());
            workout.setCalories(updateDto.getCalories());

            return Optional.ofNullable(modelMapper.map(workout, ReadWorkoutDto.class));
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<ReadWorkoutDto> update(Long id, UpdateWorkoutDto updateDto) {
        Optional<Workout> workoutAtId = workoutRepository.findById(id);
        if (workoutAtId.isPresent()) {
            Workout workout = workoutAtId.get();

            workout.setName(updateDto.getName());
            workout.setType(updateDto.getType());
            workout.setDate(updateDto.getDate());
            workout.setDuration(updateDto.getDuration());
            workout.setCalories(updateDto.getCalories());

            return Optional.ofNullable(modelMapper.map(workout, ReadWorkoutDto.class));
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<ReadWorkoutDto> delete(Long id, User user) {
        Optional<Workout> optionalWorkout = workoutRepository.findById(id);
        if (optionalWorkout.isPresent() && optionalWorkout.get().getUser().equals(user)) {
            workoutRepository.delete(optionalWorkout.get());

            return Optional.ofNullable(modelMapper.map(optionalWorkout, ReadWorkoutDto.class));
        }

        return Optional.empty();
    }

    @Override
    public Optional<ReadWorkoutDto> delete(Long id) {
        Optional<Workout> optionalWorkout = workoutRepository.findById(id);
        if (optionalWorkout.isPresent()) {
            workoutRepository.delete(optionalWorkout.get());

            return Optional.ofNullable(modelMapper.map(optionalWorkout, ReadWorkoutDto.class));
        }

        return Optional.empty();
    }
}
