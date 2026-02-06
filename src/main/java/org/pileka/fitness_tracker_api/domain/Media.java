package org.pileka.fitness_tracker_api.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "media")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Media extends BaseEntity {
    @Column(nullable = false, columnDefinition = "BYTEA")
    private byte[] image;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
