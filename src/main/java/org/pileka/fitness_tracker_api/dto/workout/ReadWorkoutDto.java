package org.pileka.fitness_tracker_api.dto.workout;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pileka.fitness_tracker_api.domain.WorkoutType;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadWorkoutDto {
    private Long id;

    private String name;

    private WorkoutType type;

    private LocalDate date;

    private Integer duration; // в минутах

    private Integer calories;

    private Long userId;

    private String userUsername;
}
