package org.pileka.fitness_tracker_api;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.pileka.fitness_tracker_api.domain.User;
import org.pileka.fitness_tracker_api.domain.Workout;
import org.pileka.fitness_tracker_api.domain.WorkoutType;
import org.pileka.fitness_tracker_api.dto.workout.CreateUpdateWorkoutDto;
import org.pileka.fitness_tracker_api.mapper.WorkoutMapper;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class QuickMapperTest {
    @Test
    void testToModel() {
        WorkoutMapper workoutMapper = Mappers.getMapper(WorkoutMapper.class);

        CreateUpdateWorkoutDto testCreateUpdateDto = CreateUpdateWorkoutDto.builder()
                .name("Morning Run")
                .type(WorkoutType.CARDIO)
                .date(LocalDate.of(2024, 1, 1))
                .duration(30)
                .calories(300)
                .build();

        User testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("username");
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword");

        Workout workout = workoutMapper.toModel(testCreateUpdateDto, testUser);

        assertNull(workout.getId());

        assertEquals(testCreateUpdateDto.getName(), workout.getName());
        assertEquals(testCreateUpdateDto.getType(), workout.getType());
        assertEquals(testCreateUpdateDto.getDate(), workout.getDate());
        assertEquals(testCreateUpdateDto.getDuration(), workout.getDuration());
        assertEquals(testCreateUpdateDto.getCalories(), workout.getCalories());

        assertEquals(testUser.getId(), workout.getUser().getId());
        assertEquals(testUser.getUsername(), workout.getUser().getUsername());
        assertEquals(testUser.getPassword(), workout.getUser().getPassword());
        assertEquals(testUser.getEmail(), workout.getUser().getEmail());

    }
}
