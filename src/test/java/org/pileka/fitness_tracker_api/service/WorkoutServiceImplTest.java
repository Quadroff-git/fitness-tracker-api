package org.pileka.fitness_tracker_api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.pileka.fitness_tracker_api.domain.User;
import org.pileka.fitness_tracker_api.domain.Workout;
import org.pileka.fitness_tracker_api.domain.WorkoutType;
import org.pileka.fitness_tracker_api.dto.workout.CreateUpdateWorkoutDto;
import org.pileka.fitness_tracker_api.dto.workout.ReadWorkoutDto;
import org.pileka.fitness_tracker_api.exception.EntityDoesntBelongToUserException;
import org.pileka.fitness_tracker_api.repository.UserRepository;
import org.pileka.fitness_tracker_api.repository.WorkoutRepository;
import org.pileka.fitness_tracker_api.service.impl.WorkoutServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkoutServiceImplTest {

    WorkoutRepository workoutRepository;

    UserRepository userRepository;

    WorkoutServiceImpl workoutService;

    private User testUser;
    private User differentUser;
    private Workout testWorkout;
    private CreateUpdateWorkoutDto createUpdateWorkoutDto;

    private static final Long WORKOUT_ID = 1L;
    private static final String USERNAME = "testuser";
    private static final String DIFFERENT_USERNAME = "differentuser";

    WorkoutServiceImplTest() {
        this.workoutRepository = mock(WorkoutRepository.class);
        this.userRepository = mock(UserRepository.class);

        this.workoutService = new WorkoutServiceImpl(workoutRepository, userRepository, new ModelMapper());
    }

    @BeforeEach
    void setUpTestEntities() {
        testUser = User.builder()
                .id(1L)
                .username(USERNAME)
                .email("test@example.com")
                .password("encodedPassword")
                .build();

        differentUser = User.builder()
                .id(2L)
                .username(DIFFERENT_USERNAME)
                .email("different@example.com")
                .password("password")
                .build();

        testWorkout = Workout.builder()
                .id(WORKOUT_ID)
                .name("Morning Run")
                .type(WorkoutType.CARDIO)
                .date(LocalDate.of(2024, 1, 1))
                .duration(30)
                .calories(300)
                .user(testUser)
                .build();

        createUpdateWorkoutDto = CreateUpdateWorkoutDto.builder()
                .name("Morning Run")
                .type(WorkoutType.CARDIO)
                .date(LocalDate.of(2024, 1, 1))
                .duration(30)
                .calories(300)
                .build();
    }

    @Test
    void createWorkoutWithUserDetailsShouldReturnWorkoutDto() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(testUser));
        when(workoutRepository.save(any(Workout.class))).thenReturn(testWorkout);

        ReadWorkoutDto result = workoutService.create(createUpdateWorkoutDto, testUser);

        assertNotNull(result);
        assertEquals(WORKOUT_ID, result.getId());
        assertEquals("Morning Run", result.getName());
        assertEquals(WorkoutType.CARDIO, result.getType());
        assertEquals(LocalDate.of(2024, 1, 1), result.getDate());
        assertEquals(30, result.getDuration());
        assertEquals(300, result.getCalories());
        assertEquals(1L, result.getUserId());
        assertEquals(USERNAME, result.getUserUsername());

        verify(userRepository).findByUsername(USERNAME);
        verify(workoutRepository).save(any(Workout.class));
    }

    @Test
    void createWorkoutWithoutUserDetailsShouldThrowUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> {
            workoutService.create(createUpdateWorkoutDto);
        });
    }

    @Test
    void createWorkoutWhenUserNotFoundShouldThrowRuntimeException() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            workoutService.create(createUpdateWorkoutDto, testUser);
        });
    }

    @Test
    void findByIdWithExistingWorkoutShouldReturnWorkoutDto() {
        when(workoutRepository.findById(WORKOUT_ID)).thenReturn(Optional.of(testWorkout));

        Optional<ReadWorkoutDto> result = workoutService.findById(WORKOUT_ID);

        assertTrue(result.isPresent());
        ReadWorkoutDto dto = result.get();
        assertEquals(WORKOUT_ID, dto.getId());
        assertEquals("Morning Run", dto.getName());
        assertEquals(WorkoutType.CARDIO, dto.getType());
        assertEquals(1L, dto.getUserId());
        assertEquals(USERNAME, dto.getUserUsername());
        verify(workoutRepository).findById(WORKOUT_ID);
    }

    @Test
    void findByIdWithNonExistingWorkoutShouldReturnEmptyOptional() {
        when(workoutRepository.findById(WORKOUT_ID)).thenReturn(Optional.empty());

        Optional<ReadWorkoutDto> result = workoutService.findById(WORKOUT_ID);

        assertTrue(result.isEmpty());
        verify(workoutRepository).findById(WORKOUT_ID);
    }

    @Test
    void findByIdWithUserDetailsWhenWorkoutBelongsToUserShouldReturnWorkoutDto() {
        when(workoutRepository.findById(WORKOUT_ID)).thenReturn(Optional.of(testWorkout));

        Optional<ReadWorkoutDto> result = workoutService.findById(WORKOUT_ID, testUser);

        assertTrue(result.isPresent());
        ReadWorkoutDto dto = result.get();
        assertEquals(WORKOUT_ID, dto.getId());
        assertEquals(1L, dto.getUserId());
        verify(workoutRepository).findById(WORKOUT_ID);
    }

    @Test
    void findByIdWithUserDetailsWhenWorkoutDoesNotBelongToUserShouldThrowEntityDoesntBelongToUserException() {
        testWorkout.setUser(differentUser);
        when(workoutRepository.findById(WORKOUT_ID)).thenReturn(Optional.of(testWorkout));

        assertThrows(EntityDoesntBelongToUserException.class, () -> {
            workoutService.findById(WORKOUT_ID, testUser);
        });
    }

    @Test
    void findAllShouldReturnListOfWorkoutDtos() {
        Workout anotherWorkout = Workout.builder()
                .id(2L)
                .name("Evening Yoga")
                .type(WorkoutType.YOGA)
                .date(LocalDate.of(2024, 1, 2))
                .duration(45)
                .calories(200)
                .user(testUser)
                .build();

        when(workoutRepository.findAll()).thenReturn(List.of(testWorkout, anotherWorkout));

        List<ReadWorkoutDto> result = workoutService.findAll();

        assertEquals(2, result.size());

        ReadWorkoutDto firstDto = result.get(0);
        assertEquals(WORKOUT_ID, firstDto.getId());
        assertEquals("Morning Run", firstDto.getName());
        assertEquals(WorkoutType.CARDIO, firstDto.getType());

        ReadWorkoutDto secondDto = result.get(1);
        assertEquals(2L, secondDto.getId());
        assertEquals("Evening Yoga", secondDto.getName());
        assertEquals(WorkoutType.YOGA, secondDto.getType());

        verify(workoutRepository).findAll();
    }

    @Test
    void findAllWithPageableShouldReturnPageOfWorkoutDtos() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Workout> workoutPage = new PageImpl<>(List.of(testWorkout));
        when(workoutRepository.findAll(pageable)).thenReturn(workoutPage);

        Page<ReadWorkoutDto> result = workoutService.findAll(pageable);

        assertEquals(1, result.getTotalElements());
        ReadWorkoutDto dto = result.getContent().get(0);
        assertEquals(WORKOUT_ID, dto.getId());
        assertEquals("Morning Run", dto.getName());
        verify(workoutRepository).findAll(pageable);
    }

    @Test
    void existsByIdShouldReturnTrueWhenWorkoutExists() {
        when(workoutRepository.existsById(WORKOUT_ID)).thenReturn(true);

        boolean result = workoutService.existsById(WORKOUT_ID);

        assertTrue(result);
        verify(workoutRepository).existsById(WORKOUT_ID);
    }

    @Test
    void existsByIdShouldReturnFalseWhenWorkoutDoesNotExist() {
        when(workoutRepository.existsById(WORKOUT_ID)).thenReturn(false);

        boolean result = workoutService.existsById(WORKOUT_ID);

        assertFalse(result);
        verify(workoutRepository).existsById(WORKOUT_ID);
    }

    @Test
    void updateWorkoutWithUserDetailsWhenWorkoutBelongsToUserShouldUpdateSuccessfully() {
        when(workoutRepository.findById(WORKOUT_ID)).thenReturn(Optional.of(testWorkout));
        when(workoutRepository.save(any(Workout.class))).thenReturn(testWorkout);

        CreateUpdateWorkoutDto updateDto = CreateUpdateWorkoutDto.builder()
                .name("Updated Run")
                .type(WorkoutType.STRENGTH)
                .date(LocalDate.of(2024, 1, 2))
                .duration(45)
                .calories(400)
                .build();

        Optional<ReadWorkoutDto> result = workoutService.update(WORKOUT_ID, testUser, updateDto);

        assertTrue(result.isPresent());
        ReadWorkoutDto dto = result.get();
        assertEquals("Updated Run", dto.getName());
        assertEquals(WorkoutType.STRENGTH, dto.getType());
        assertEquals(LocalDate.of(2024, 1, 2), dto.getDate());
        assertEquals(45, dto.getDuration());
        assertEquals(400, dto.getCalories());
        verify(workoutRepository).save(any(Workout.class));
    }

    @Test
    void updateWorkoutWithUserDetailsWhenWorkoutDoesNotBelongToUserShouldThrowException() {
        testWorkout.setUser(differentUser);
        when(workoutRepository.findById(WORKOUT_ID)).thenReturn(Optional.of(testWorkout));

        assertThrows(EntityDoesntBelongToUserException.class, () -> {
            workoutService.update(WORKOUT_ID, testUser, createUpdateWorkoutDto);
        });

        verify(workoutRepository, never()).save(any(Workout.class));
    }

    @Test
    void updateWorkoutWithUserDetailsWhenWorkoutNotFoundShouldReturnEmptyOptional() {
        when(workoutRepository.findById(WORKOUT_ID)).thenReturn(Optional.empty());

        Optional<ReadWorkoutDto> result = workoutService.update(WORKOUT_ID, testUser, createUpdateWorkoutDto);

        assertTrue(result.isEmpty());
        verify(workoutRepository, never()).save(any(Workout.class));
    }

    @Test
    void updateWorkoutWithoutUserDetailsShouldUpdateWhenWorkoutExists() {
        when(workoutRepository.findById(WORKOUT_ID)).thenReturn(Optional.of(testWorkout));
        when(workoutRepository.save(any(Workout.class))).thenReturn(testWorkout);

        CreateUpdateWorkoutDto updateDto = CreateUpdateWorkoutDto.builder()
                .name("Updated Run")
                .type(WorkoutType.STRENGTH)
                .date(LocalDate.of(2024, 1, 2))
                .duration(45)
                .calories(400)
                .build();

        Optional<ReadWorkoutDto> result = workoutService.update(WORKOUT_ID, updateDto);

        assertTrue(result.isPresent());
        ReadWorkoutDto dto = result.get();
        assertEquals("Updated Run", dto.getName());
        assertEquals(WorkoutType.STRENGTH, dto.getType());
        verify(workoutRepository).save(any(Workout.class));
    }

    @Test
    void deleteWorkoutWithUserDetailsWhenWorkoutBelongsToUserShouldDeleteSuccessfully() {
        when(workoutRepository.findById(WORKOUT_ID)).thenReturn(Optional.of(testWorkout));
        doNothing().when(workoutRepository).delete(testWorkout);

        Optional<ReadWorkoutDto> result = workoutService.delete(WORKOUT_ID, testUser);

        assertTrue(result.isPresent());
        ReadWorkoutDto dto = result.get();
        assertEquals(WORKOUT_ID, dto.getId());
        assertEquals("Morning Run", dto.getName());
        verify(workoutRepository).delete(testWorkout);
    }

    @Test
    void deleteWorkoutWithUserDetailsWhenWorkoutDoesNotBelongToUserShouldThrowException() {
        testWorkout.setUser(differentUser);
        when(workoutRepository.findById(WORKOUT_ID)).thenReturn(Optional.of(testWorkout));

        assertThrows(EntityDoesntBelongToUserException.class, () -> {
            workoutService.delete(WORKOUT_ID, testUser);
        });

        verify(workoutRepository, never()).delete(any(Workout.class));
    }

    @Test
    void deleteWorkoutWithUserDetailsWhenWorkoutNotFoundShouldReturnEmptyOptional() {
        when(workoutRepository.findById(WORKOUT_ID)).thenReturn(Optional.empty());

        Optional<ReadWorkoutDto> result = workoutService.delete(WORKOUT_ID, testUser);

        assertTrue(result.isEmpty());
        verify(workoutRepository, never()).delete(any(Workout.class));
    }

    @Test
    void deleteWorkoutWithoutUserDetailsShouldDeleteWhenWorkoutExists() {
        when(workoutRepository.findById(WORKOUT_ID)).thenReturn(Optional.of(testWorkout));
        doNothing().when(workoutRepository).delete(testWorkout);

        Optional<ReadWorkoutDto> result = workoutService.delete(WORKOUT_ID);

        assertTrue(result.isPresent());
        ReadWorkoutDto dto = result.get();
        assertEquals(WORKOUT_ID, dto.getId());
        assertEquals("Morning Run", dto.getName());
        verify(workoutRepository).delete(testWorkout);
    }

    @Test
    void deleteWorkoutWithoutUserDetailsWhenWorkoutNotFoundShouldReturnEmptyOptional() {
        when(workoutRepository.findById(WORKOUT_ID)).thenReturn(Optional.empty());

        Optional<ReadWorkoutDto> result = workoutService.delete(WORKOUT_ID);

        assertTrue(result.isEmpty());
        verify(workoutRepository, never()).delete(any(Workout.class));
    }

    @Test
    void findAllWithFiltersShouldMapWorkoutToDtoCorrectly() {
        Workout filteredWorkout = Workout.builder()
                .id(3L)
                .name("Filtered Workout")
                .type(WorkoutType.YOGA)
                .date(LocalDate.of(2024, 1, 15))
                .duration(60)
                .calories(250)
                .user(testUser)
                .build();

        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(testUser));
        when(workoutRepository.findAll(any(Specification.class))).thenReturn(List.of(filteredWorkout));

        List<ReadWorkoutDto> result = workoutService.findAll(
                testUser,
                Optional.of(WorkoutType.YOGA),
                Optional.of(LocalDate.of(2024, 1, 1)),
                Optional.of(LocalDate.of(2024, 1, 31)),
                Optional.of(30),
                Optional.of(90)
        );

        assertEquals(1, result.size());
        ReadWorkoutDto dto = result.get(0);
        assertEquals(3L, dto.getId());
        assertEquals("Filtered Workout", dto.getName());
        assertEquals(WorkoutType.YOGA, dto.getType());
        assertEquals(LocalDate.of(2024, 1, 15), dto.getDate());
        assertEquals(60, dto.getDuration());
        assertEquals(250, dto.getCalories());
        assertEquals(1L, dto.getUserId());
        assertEquals(USERNAME, dto.getUserUsername());
    }

    @Test
    void testWorkoutFieldsAreCorrectlyMappedToDto() {
        Workout complexWorkout = Workout.builder()
                .id(5L)
                .name("Complex Workout")
                .type(WorkoutType.STRENGTH)
                .date(LocalDate.of(2024, 2, 1))
                .duration(90)
                .calories(500)
                .user(differentUser)
                .build();

        when(workoutRepository.findById(5L)).thenReturn(Optional.of(complexWorkout));

        Optional<ReadWorkoutDto> result = workoutService.findById(5L);

        assertTrue(result.isPresent());
        ReadWorkoutDto dto = result.get();
        assertEquals(5L, dto.getId());
        assertEquals("Complex Workout", dto.getName());
        assertEquals(WorkoutType.STRENGTH, dto.getType());
        assertEquals(LocalDate.of(2024, 2, 1), dto.getDate());
        assertEquals(90, dto.getDuration());
        assertEquals(500, dto.getCalories());
        assertEquals(2L, dto.getUserId());
        assertEquals(DIFFERENT_USERNAME, dto.getUserUsername());
    }
}
