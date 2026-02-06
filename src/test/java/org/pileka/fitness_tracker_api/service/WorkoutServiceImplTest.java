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
import org.pileka.fitness_tracker_api.security.CustomUserDetails;
import org.pileka.fitness_tracker_api.service.impl.WorkoutServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkoutServiceImplTest {

    WorkoutRepository workoutRepository;

    UserRepository userRepository;

    WorkoutServiceImpl workoutService;

    private User testUser;
    private UserDetails testUserDetails;
    private User differentUser;
    private Workout testWorkout;
    private CreateUpdateWorkoutDto testCreateUpdateDto;

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
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername(USERNAME);
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword");

        testUserDetails = new CustomUserDetails(testUser.getUsername(), testUser.getPassword());

        differentUser = new User();
        differentUser.setId(2L);
        differentUser.setUsername(DIFFERENT_USERNAME);
        differentUser.setEmail("different@example.com");
        differentUser.setPassword("password");

        testWorkout = new Workout();
        testWorkout.setId(WORKOUT_ID);
        testWorkout.setName("Morning Run");
        testWorkout.setType(WorkoutType.CARDIO);
        testWorkout.setDate(LocalDate.of(2024, 1, 1));
        testWorkout.setDuration(30);
        testWorkout.setCalories(300);
        testWorkout.setUser(testUser);

        testCreateUpdateDto = CreateUpdateWorkoutDto.builder()
                .name("Morning Run")
                .type(WorkoutType.CARDIO)
                .date(LocalDate.of(2024, 1, 1))
                .duration(30)
                .calories(300)
                .build();
    }

    @Test
    void createWithUserDetailsReturnsWorkoutDto() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(testUser));
        when(workoutRepository.save(any(Workout.class))).thenReturn(testWorkout);

        ReadWorkoutDto result = workoutService.create(testCreateUpdateDto, testUserDetails);

        assertReadDtoEqualsWorkout(result, testWorkout);

        verify(userRepository).findByUsername(USERNAME);
        verify(workoutRepository).save(any(Workout.class));
    }

    @Test
    void findByIdReturnsWorkoutDtoWhenWorkoutBelongsToUser() {
        when(workoutRepository.findById(WORKOUT_ID)).thenReturn(Optional.of(testWorkout));

        Optional<ReadWorkoutDto> result = workoutService.findById(WORKOUT_ID, testUserDetails);

        assertTrue(result.isPresent());
        ReadWorkoutDto dto = result.get();

        assertReadDtoEqualsWorkout(dto, testWorkout);
    }

    @Test
    void findByIdThrowsExceptionWhenWorkoutDoesNotBelongToUser() {
        testWorkout.setUser(differentUser);
        when(workoutRepository.findById(WORKOUT_ID)).thenReturn(Optional.of(testWorkout));

        assertThrows(EntityDoesntBelongToUserException.class, () -> {
            workoutService.findById(WORKOUT_ID, testUserDetails);
        });
    }

    @Test
    void updateWithUserDetailsUpdatesSuccessfullyWhenWorkoutBelongsToUser() {
        when(workoutRepository.findById(WORKOUT_ID)).thenReturn(Optional.of(testWorkout));
        when(workoutRepository.save(any(Workout.class))).thenReturn(testWorkout);

        CreateUpdateWorkoutDto updateDto = CreateUpdateWorkoutDto.builder()
                .name("Updated Run")
                .type(WorkoutType.STRENGTH)
                .date(LocalDate.of(2024, 1, 2))
                .duration(45)
                .calories(400)
                .build();

        Optional<ReadWorkoutDto> result = workoutService.update(WORKOUT_ID, testUserDetails, updateDto);

        assertTrue(result.isPresent());
        ReadWorkoutDto dto = result.get();

        assertCreateUpdateDtoEqualsReadDto(updateDto, dto);

        verify(workoutRepository).save(any(Workout.class));
    }

    @Test
    void updateWithUserDetailsThrowsExceptionWhenWorkoutDoesNotBelongToUser() {
        testWorkout.setUser(differentUser);
        when(workoutRepository.findById(WORKOUT_ID)).thenReturn(Optional.of(testWorkout));

        assertThrows(EntityDoesntBelongToUserException.class, () -> {
            workoutService.update(WORKOUT_ID, testUserDetails, testCreateUpdateDto);
        });

        verify(workoutRepository, never()).save(any(Workout.class));
    }

    @Test
    void updateWithUserDetailsReturnsEmptyOptionalWhenWorkoutNotFound() {
        when(workoutRepository.findById(WORKOUT_ID)).thenReturn(Optional.empty());

        Optional<ReadWorkoutDto> result = workoutService.update(WORKOUT_ID, testUserDetails, testCreateUpdateDto);

        assertTrue(result.isEmpty());
        verify(workoutRepository, never()).save(any(Workout.class));
    }

    @Test
    void deleteDeletesSuccessfullyWhenWorkoutBelongsToUser() {
        when(workoutRepository.findById(WORKOUT_ID)).thenReturn(Optional.of(testWorkout));
        doNothing().when(workoutRepository).delete(testWorkout);

        Optional<ReadWorkoutDto> result = workoutService.delete(WORKOUT_ID, testUserDetails);

        assertTrue(result.isPresent());
        ReadWorkoutDto dto = result.get();

        assertReadDtoEqualsWorkout(dto, testWorkout);

        verify(workoutRepository).delete(testWorkout);
    }

    @Test
    void deleteThrowsExceptionWhenWorkoutDoesNotBelongToUser() {
        testWorkout.setUser(differentUser);
        when(workoutRepository.findById(WORKOUT_ID)).thenReturn(Optional.of(testWorkout));

        assertThrows(EntityDoesntBelongToUserException.class, () -> {
            workoutService.delete(WORKOUT_ID, testUserDetails);
        });

        verify(workoutRepository, never()).delete(any(Workout.class));
    }

    @Test
    void deleteReturnsEmptyOptionalWhenWorkoutNotFound() {
        when(workoutRepository.findById(WORKOUT_ID)).thenReturn(Optional.empty());

        Optional<ReadWorkoutDto> result = workoutService.delete(WORKOUT_ID, testUserDetails);

        assertTrue(result.isEmpty());
        verify(workoutRepository, never()).delete(any(Workout.class));
    }

    @Test
    void findAllWithoutPageableReturnsAllFitting() {
        final int LIST_SIZE = 5;
        List<Workout> filteredWorkouts = getTestWorkouts(LIST_SIZE);

        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(testUser));
        when(workoutRepository.findAll(any(Specification.class))).thenReturn(filteredWorkouts);

        List<ReadWorkoutDto> result = workoutService.findAll(
                testUserDetails,
                Optional.of(WorkoutType.YOGA),
                Optional.of(LocalDate.of(2024, 1, 1)),
                Optional.of(LocalDate.of(2024, 1, 31)),
                Optional.of(30),
                Optional.of(90)
        );

        assertEquals(LIST_SIZE, result.size());
        for (int i = 0; i < LIST_SIZE; i++) {
            assertReadDtoEqualsWorkout(result.get(i), filteredWorkouts.get(i));
        }
    }

    @Test
    void findAllWithPageableReturnsAllFitting() {
        final int LIST_SIZE = 5;
        Page<Workout> filteredWorkouts = new PageImpl<>(getTestWorkouts(LIST_SIZE));

        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(testUser));
        when(workoutRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(filteredWorkouts);

        Page<ReadWorkoutDto> result = workoutService.findAll(
                testUserDetails,
                Optional.of(WorkoutType.YOGA),
                Optional.of(LocalDate.of(2024, 1, 1)),
                Optional.of(LocalDate.of(2024, 1, 31)),
                Optional.of(30),
                Optional.of(90),
                new Pageable() {
                    @Override
                    public int getPageNumber() {
                        return 0;
                    }

                    @Override
                    public int getPageSize() {
                        return 0;
                    }

                    @Override
                    public long getOffset() {
                        return 0;
                    }

                    @Override
                    public Sort getSort() {
                        return null;
                    }

                    @Override
                    public Pageable next() {
                        return null;
                    }

                    @Override
                    public Pageable previousOrFirst() {
                        return null;
                    }

                    @Override
                    public Pageable first() {
                        return null;
                    }

                    @Override
                    public Pageable withPage(int pageNumber) {
                        return null;
                    }

                    @Override
                    public boolean hasPrevious() {
                        return false;
                    }
                }
        );

        assertEquals(LIST_SIZE, result.getTotalElements());
        for (int i = 0; i < LIST_SIZE; i++) {
            assertReadDtoEqualsWorkout(result.stream().toList().get(i), filteredWorkouts.stream().toList().get(i));
        }
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

    private Workout getTestWorkout(int i) {
        Random random = new Random();

        Workout workout = new Workout();
        workout.setId((long) i);
        workout.setName("Test workout " + i);
        workout.setType(WorkoutType.values()[random.nextInt(WorkoutType.values().length)]);
        workout.setDate(LocalDate.of(2024, 1, 1 + i));
        workout.setDuration(30 + i);
        workout.setCalories(300 + i * 50);
        workout.setUser(testUser);

        return workout;
    }

    private List<Workout> getTestWorkouts(int n) {
        List<Workout> workouts = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            workouts.add(getTestWorkout(i));
        }

        return workouts;
    }
}
