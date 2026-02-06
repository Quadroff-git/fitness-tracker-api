package org.pileka.fitness_tracker_api.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Entity
@Table(name = "media")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Media extends BaseEntity {
    @NotEmpty()
    @Column(nullable = false, columnDefinition = "BYTEA")
    private byte[] image;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
