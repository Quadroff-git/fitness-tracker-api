package org.pileka.fitness_tracker_api.domain;

import jakarta.persistence.*;
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
    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkoutType type;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Integer duration; // в минутах

    @Column(nullable = false)
    private Integer calories;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
