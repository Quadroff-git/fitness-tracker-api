package org.pileka.fitness_tracker_api.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pileka.fitness_tracker_api.domain.Workout;
import org.pileka.fitness_tracker_api.dto.workout.CreateUpdateWorkoutDto;
import org.pileka.fitness_tracker_api.dto.workout.ReadWorkoutDto;
import org.pileka.fitness_tracker_api.exception.EntityDoesntBelongToUserException;
import org.pileka.fitness_tracker_api.mapper.WorkoutMapper;
import org.pileka.fitness_tracker_api.repository.UserRepository;
import org.pileka.fitness_tracker_api.repository.WorkoutRepository;
import org.pileka.fitness_tracker_api.service.impl.WorkoutServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.pileka.fitness_tracker_api.util.AuthTestUtil.*;
import static org.pileka.fitness_tracker_api.util.UserTestUtil.*;
import static org.pileka.fitness_tracker_api.util.WorkoutTestUtil.*;

@ExtendWith(MockitoExtension.class)
class WorkoutServiceImplTest {

    WorkoutRepository workoutRepository;

    UserRepository userRepository;

    WorkoutServiceImpl workoutService;

    public WorkoutServiceImplTest() {
        this.workoutRepository = mock(WorkoutRepository.class);
        this.userRepository = mock(UserRepository.class);

        this.workoutService = new WorkoutServiceImpl(workoutRepository,
                userRepository,
                Mappers.getMapper(WorkoutMapper.class));
    }

    @Test
    void createReturnsWorkoutDto() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(testUser));
        when(workoutRepository.save(any(Workout.class))).thenReturn(getTestWorkout());

        ReadWorkoutDto result = doWithMockedAuthUserUtil(
                testUserDetails,
                () -> workoutService.create(getTestCreateUpdateWorkoutDto())
        );
        assertReadDtoEqualsWorkout(result, getTestWorkout());

        verify(userRepository).findByUsername(USERNAME);
        verify(workoutRepository).save(any(Workout.class));
    }

    @Test
    void findByIdReturnsWorkoutDtoWhenWorkoutBelongsToUser() {
        when(workoutRepository.findById(WORKOUT_ID)).thenReturn(Optional.of(getTestWorkout()));

        Optional<ReadWorkoutDto> result = doWithMockedAuthUserUtil(
                testUserDetails,
                () -> workoutService.findById(WORKOUT_ID)
        );

        assertTrue(result.isPresent());
        ReadWorkoutDto dto = result.get();

        assertReadDtoEqualsWorkout(dto, getTestWorkout());
        verify(workoutRepository).findById(WORKOUT_ID);
    }

    @Test
    void findByIdThrowsExceptionWhenWorkoutDoesNotBelongToUser() {
        Workout somebodysWorkout = getTestWorkout();
        somebodysWorkout.setUser(anotherTestUser);

        when(workoutRepository.findById(WORKOUT_ID)).thenReturn(Optional.of(somebodysWorkout));

        doWithMockedAuthUserUtil(
                otherUserDetails,
                () -> assertThrows(EntityDoesntBelongToUserException.class,
                        () -> workoutService.findById(WORKOUT_ID)
                )
        );

        verify(workoutRepository).findById(WORKOUT_ID);
    }

    @Test
    void updateUpdatesSuccessfullyWhenWorkoutBelongsToUser() {
        final String UPDATED_NAME = "updated name";

        Workout updatedWorkout = getTestWorkout();
        updatedWorkout.setName(UPDATED_NAME);

        CreateUpdateWorkoutDto updateDto = getTestCreateUpdateWorkoutDto();
        updateDto.setName(UPDATED_NAME);

        when(workoutRepository.findById(WORKOUT_ID)).thenReturn(Optional.of(getTestWorkout()));
        when(workoutRepository.save(any(Workout.class))).thenReturn(updatedWorkout);

        Optional<ReadWorkoutDto> result = doWithMockedAuthUserUtil(
                testUserDetails,
                () -> workoutService.update(WORKOUT_ID, updateDto)
        );

        assertTrue(result.isPresent());
        ReadWorkoutDto dto = result.get();

        assertCreateUpdateDtoEqualsReadDto(updateDto, dto);

        verify(workoutRepository).save(any(Workout.class));
    }

    @Test
    void updateThrowsExceptionWhenWorkoutDoesNotBelongToUser() {
        final String UPDATED_NAME = "updated name";

        Workout updatedWorkout = getTestWorkout();
        updatedWorkout.setName(UPDATED_NAME);

        CreateUpdateWorkoutDto updateDto = getTestCreateUpdateWorkoutDto();
        updateDto.setName(UPDATED_NAME);

        when(workoutRepository.findById(WORKOUT_ID)).thenReturn(Optional.of(getTestWorkout()));

        doWithMockedAuthUserUtil(otherUserDetails,
                () -> assertThrows(
                        EntityDoesntBelongToUserException.class,
                        () -> workoutService.update(WORKOUT_ID, updateDto)
                )
        );

        verify(workoutRepository, never()).save(any(Workout.class));
    }

    @Test
    void updateReturnsEmptyOptionalWhenWorkoutNotFound() {
        when(workoutRepository.findById(WORKOUT_ID)).thenReturn(Optional.empty());

        Optional<ReadWorkoutDto> result = doWithMockedAuthUserUtil(testUserDetails,
                () -> workoutService.update(WORKOUT_ID, getTestCreateUpdateWorkoutDto())
        );

        assertTrue(result.isEmpty());
        verify(workoutRepository, never()).save(any(Workout.class));
    }

    @Test
    void deleteDeletesSuccessfullyWhenWorkoutBelongsToUser() {
        when(workoutRepository.findById(WORKOUT_ID)).thenReturn(Optional.of(getTestWorkout()));
        doNothing().when(workoutRepository).delete(getTestWorkout());

        Optional<ReadWorkoutDto> result = doWithMockedAuthUserUtil(
                testUserDetails,
                () -> workoutService.delete(WORKOUT_ID)
        );

        assertTrue(result.isPresent());
        ReadWorkoutDto dto = result.get();

        assertReadDtoEqualsWorkout(dto, getTestWorkout());

        verify(workoutRepository).delete(getTestWorkout());
    }

    @Test
    void deleteThrowsExceptionWhenWorkoutDoesNotBelongToUser() {
        Workout somebodysWorkout = getTestWorkout();
        somebodysWorkout.setUser(anotherTestUser);
        when(workoutRepository.findById(WORKOUT_ID)).thenReturn(Optional.of(somebodysWorkout));

        doWithMockedAuthUserUtil(
                otherUserDetails,
                () -> assertThrows(
                        EntityDoesntBelongToUserException.class,
                        () -> workoutService.delete(WORKOUT_ID)
                )
        );

        verify(workoutRepository, never()).delete(any(Workout.class));
    }

    @Test
    void deleteReturnsEmptyOptionalWhenWorkoutNotFound() {
        when(workoutRepository.findById(WORKOUT_ID)).thenReturn(Optional.empty());

        Optional<ReadWorkoutDto> result = doWithMockedAuthUserUtil(
                testUserDetails,
                () -> workoutService.delete(WORKOUT_ID)
        );

        assertTrue(result.isEmpty());
        verify(workoutRepository, never()).delete(any(Workout.class));
    }

    @Test
    void findAllReturnsAllFitting() {
        final int LIST_SIZE = 5;
        Page<Workout> filteredWorkouts = new PageImpl<>(getTestWorkouts(LIST_SIZE));

        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(testUser));
        when(workoutRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(filteredWorkouts);

        Page<ReadWorkoutDto> result = doWithMockedAuthUserUtil(
                testUserDetails,
                () -> workoutService.findAll(testWorkoutSpecDto, testPageable)
        );

        assertEquals(LIST_SIZE, result.getTotalElements());
        for (int i = 0; i < LIST_SIZE; i++) {
            assertReadDtoEqualsWorkout(result.stream().toList().get(i), filteredWorkouts.stream().toList().get(i));
        }
    }

    @Test
    void findAllReturnsEmptyPageWhenNothingFits() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(testUser));
        when(workoutRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(new PageImpl<Workout>(List.of()));

        Page<ReadWorkoutDto> result = doWithMockedAuthUserUtil(
                testUserDetails,
                () -> workoutService.findAll(testWorkoutSpecDto, testPageable)
        );

        assertTrue(result.isEmpty());
    }

    private void assertCreateUpdateDtoEqualsReadDto(CreateUpdateWorkoutDto createUpdateWorkoutDto, ReadWorkoutDto readWorkoutDto) {
        assertEquals(createUpdateWorkoutDto.getName(), readWorkoutDto.getName());
        assertEquals(createUpdateWorkoutDto.getType(), readWorkoutDto.getType());
        assertEquals(createUpdateWorkoutDto.getDate(), readWorkoutDto.getDate());
        assertEquals(createUpdateWorkoutDto.getDuration(), readWorkoutDto.getDuration());
        assertEquals(createUpdateWorkoutDto.getCalories(), readWorkoutDto.getCalories());
    }

    private void assertReadDtoEqualsWorkout(ReadWorkoutDto readWorkoutDto, Workout workout) {
        assertEquals(readWorkoutDto.getId(), workout.getId());
        assertEquals(readWorkoutDto.getName(), workout.getName());
        assertEquals(readWorkoutDto.getType(), workout.getType());
        assertEquals(readWorkoutDto.getDate(), workout.getDate());
        assertEquals(readWorkoutDto.getDuration(), workout.getDuration());
        assertEquals(readWorkoutDto.getCalories(), workout.getCalories());
        assertEquals(readWorkoutDto.getUserId(), workout.getUser().getId());
        assertEquals(readWorkoutDto.getUserUsername(), workout.getUser().getUsername());
    }
}
