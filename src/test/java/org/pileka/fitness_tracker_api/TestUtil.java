package org.pileka.fitness_tracker_api;

import lombok.experimental.UtilityClass;
import org.pileka.fitness_tracker_api.domain.User;
import org.pileka.fitness_tracker_api.domain.Workout;
import org.pileka.fitness_tracker_api.domain.WorkoutType;
import org.pileka.fitness_tracker_api.dto.workout.CreateUpdateWorkoutDto;
import org.pileka.fitness_tracker_api.security.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@UtilityClass
public class TestUtil {
    public final String USERNAME = "username";
    public final String ANOTHER_USERNAME = "another username";

    public final Long WORKOUT_ID = 1L;

    public final User testUser = new User(USERNAME, null, "password", null, null);
    public final User anotherTestUser = new User(ANOTHER_USERNAME, null, "securepassword", null, null);

    public final UserDetails testUserDetails = new CustomUserDetails(USERNAME, "password");

    public Workout getTestWorkout() {
        Workout workout = new Workout();
        workout.setId(WORKOUT_ID);
        workout.setName("Morning run");
        workout.setType(WorkoutType.RUNNING);
        workout.setDate(LocalDate.of(2024, 1, 1));
        workout.setDuration(30);
        workout.setCalories(300);
        workout.setUser(testUser);

        return workout;
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

    public List<Workout> getTestWorkouts(int n) {
        List<Workout> workouts = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            workouts.add(getTestWorkout(i));
        }

        return workouts;
    }

    public CreateUpdateWorkoutDto getTestCreateUpdateWorkoutDto() {
        CreateUpdateWorkoutDto dto = new CreateUpdateWorkoutDto();
        dto.setName("Morning run");
        dto.setType(WorkoutType.RUNNING);
        dto.setDate(LocalDate.of(2024, 1, 1));
        dto.setDuration(30);
        dto.setCalories(300);

        return dto;
    }


}
