package org.pileka.fitness_tracker_api.mapper;

import org.mapstruct.*;
import org.pileka.fitness_tracker_api.domain.User;
import org.pileka.fitness_tracker_api.domain.Workout;
import org.pileka.fitness_tracker_api.dto.workout.CreateUpdateWorkoutDto;
import org.pileka.fitness_tracker_api.dto.workout.ReadWorkoutDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, builder = @Builder(disableBuilder = true))
public interface WorkoutMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userUsername", source = "user.username")
    ReadWorkoutDto toDto(Workout entity);

    @Mapping(target = "id", expression = "java(null)") // maps user.id to workout.id without this line
    Workout toModel(CreateUpdateWorkoutDto dto, User user);

    void update(CreateUpdateWorkoutDto dto, @MappingTarget Workout workout);
}
