package org.pileka.fitness_tracker_api.repository.specification;

import org.pileka.fitness_tracker_api.domain.User;
import org.pileka.fitness_tracker_api.domain.Workout;
import org.pileka.fitness_tracker_api.domain.WorkoutType;
import org.pileka.fitness_tracker_api.dto.workout.WorkoutSpecDto;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Optional;

/**
 * A convenience class for getting specification objects without the service interacting with Spring Data-specific API
 * All intervals are inclusive on both ends
 */
public class WorkoutSpecs {

    public static Specification<Workout> belongsToUser(User user) {
        return (from, query, builder) -> builder.equal(from.get("user"), user);
    }

    public static Specification<Workout> isOfType(WorkoutType type) {
        return (from, query, builder) -> builder.equal(from.get("type"), type);
    }

    public static Specification<Workout> happenedAfter(LocalDate date) {
        return (from, query, builder) -> builder.greaterThanOrEqualTo(from.get("date"), date);
    }

    public static Specification<Workout> happenedBefore(LocalDate date) {
        return (from, query, builder) -> builder.lessThanOrEqualTo(from.get("date"), date);
    }

    public static Specification<Workout> isLongerThan(Integer duration) {
        return (from, query, builder) -> builder.greaterThanOrEqualTo(from.get("duration"), duration);
    }

    public static Specification<Workout> isShorterThan(Integer duration) {
        return (from, query, builder) -> builder.lessThanOrEqualTo(from.get("duration"), duration);
    }

    public static Specification<Workout> getFullSpec(User user, WorkoutSpecDto specDto) {
        Specification<Workout> predicate = belongsToUser(user);

        if (specDto.getType() != null) {
            predicate = predicate.and(isOfType(specDto.getType()));
        }
        if (specDto.getStartDate() != null) {
            predicate = predicate.and(happenedAfter(specDto.getStartDate()));
        }
        if (specDto.getEndDate() != null) {
            predicate = predicate.and(happenedBefore(specDto.getEndDate()));
        }
        if (specDto.getMinDuration() != null) {
            predicate = predicate.and(isLongerThan(specDto.getMinDuration()));
        }
        if (specDto.getMaxDuration() != null) {
            predicate = predicate.and(isShorterThan(specDto.getMaxDuration()));
        }

        return predicate;
    }
}

