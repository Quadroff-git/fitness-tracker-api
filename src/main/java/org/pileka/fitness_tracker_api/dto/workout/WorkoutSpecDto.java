package org.pileka.fitness_tracker_api.dto.workout;

import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.pileka.fitness_tracker_api.domain.WorkoutType;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutSpecDto {

    private WorkoutType type;

    @Setter(AccessLevel.NONE)
    @PastOrPresent(message = "startDate must be in the past or present")
    private LocalDate startDate;

    @Setter(AccessLevel.NONE)
    @PastOrPresent(message = "endDate must be in the past or present")
    private LocalDate endDate;

    @Setter(AccessLevel.NONE)
    @Positive(message = "minDuration must be greater than 0")
    private Integer minDuration;

    @Setter(AccessLevel.NONE)
    @Positive(message = "maxDuration must be greater than 0")
    private Integer maxDuration;

    public void setStartDate(LocalDate startDate) {
        if (endDate != null && startDate.isAfter(this.endDate)) {
            this.startDate = endDate;
            this.endDate = startDate;
        }
        else {
            this.startDate = startDate;
        }
    }

    public void setEndDate(LocalDate endDate) {
        if (this.startDate != null && endDate.isBefore(this.startDate)) {
            this.endDate = this.startDate;
            this.startDate = endDate;
        }
        else {
            this.endDate = endDate;
        }
    }

    public void setMinDuration(Integer minDuration) {
        if (this.maxDuration != null && minDuration > this.maxDuration) {
            this.minDuration = this.maxDuration;
            this.maxDuration = minDuration;
        }
        else {
            this.minDuration = minDuration;
        }
    }

    public void setMaxDuration(Integer maxDuration) {
        if (this.minDuration != null && maxDuration < this.minDuration) {
            this.maxDuration = this.minDuration;
            this.minDuration = maxDuration;
        }
        else {
            this.maxDuration = maxDuration;
        }
    }
}
