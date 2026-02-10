package org.pileka.fitness_tracker_api.util;

import lombok.experimental.UtilityClass;
import org.pileka.fitness_tracker_api.domain.Workout;
import org.pileka.fitness_tracker_api.domain.WorkoutType;
import org.pileka.fitness_tracker_api.dto.workout.CreateUpdateWorkoutDto;
import org.pileka.fitness_tracker_api.dto.workout.WorkoutSpecDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.pileka.fitness_tracker_api.util.UserTestUtil.testUser;

@UtilityClass
public class WorkoutTestUtil {

    public final Long WORKOUT_ID = 1L;

    // Very ugly anonymous Pageable implementation, but i'm too lazy to mock it
    public final Pageable testPageable = new Pageable() {
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
    };

    public final WorkoutSpecDto testWorkoutSpecDto = new WorkoutSpecDto(
            WorkoutType.RUNNING,
            LocalDate.of(2025, 1, 1),
            LocalDate.of(2025, 1, 2),
            30,
                        60
    );

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

    public CreateUpdateWorkoutDto getTestCreateUpdateWorkoutDto() {
        CreateUpdateWorkoutDto dto = new CreateUpdateWorkoutDto();
        dto.setName("Morning run");
        dto.setType(WorkoutType.RUNNING);
        dto.setDate(LocalDate.of(2024, 1, 1));
        dto.setDuration(30);
        dto.setCalories(300);

        return dto;
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

}
