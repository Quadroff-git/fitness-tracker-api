package org.pileka.fitness_tracker_api.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "workout")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Workout extends BaseEntity {
    @NotBlank(message = "Workout name is required")
    @Size(max = 100, message = "Workout name must be less than or equal to 100 characters")
    @Column(nullable = false, length = 100)
    private String name;

    @NotNull(message = "Workout type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkoutType type;

    @NotNull(message = "Workout date is required")
    @PastOrPresent(message = "Workout date must be in the past or present")
    @Column(nullable = false)
    private LocalDate date;

    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be greater than 0")
    @Column(nullable = false)
    private Integer duration; // в минутах

    @NotNull(message = "Calories burned is required")
    @Positive(message = "Calories burned must be greater than 0")
    @Column(nullable = false)
    private Integer calories;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
