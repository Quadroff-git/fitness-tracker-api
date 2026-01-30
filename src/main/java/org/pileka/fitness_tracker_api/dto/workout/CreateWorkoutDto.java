package org.pileka.fitness_tracker_api.dto.workout;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pileka.fitness_tracker_api.domain.WorkoutType;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateWorkoutDto {
    @NotBlank(message = "Workout name is required")
    @Size(max = 100, message = "Workout name must be less than or equal to 100 characters")
    private String name;

    @NotNull(message = "Workout type is required")
    private WorkoutType type;

    @NotNull(message = "Workout date is required")
    @PastOrPresent(message = "Workout date must be in the past or present")
    private LocalDate date;

    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be greater than 0")
    private Integer duration; // в минутах

    @NotNull(message = "Calories burned is required")
    @Positive(message = "Calories burned must be greater than 0")
    private Integer calories;

    @NotNull(message = "User id is required")
    private Long userId;
}
