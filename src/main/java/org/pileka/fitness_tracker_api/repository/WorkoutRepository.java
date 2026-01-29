package org.pileka.fitness_tracker_api.repository;

import org.pileka.fitness_tracker_api.domain.User;
import org.pileka.fitness_tracker_api.domain.Workout;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface WorkoutRepository extends ListCrudRepository<Workout, Long>, PagingAndSortingRepository<Workout, Long>, JpaSpecificationExecutor<Workout> {
    List<Workout> findByUser(User user);
    List<Workout> findByUser(User user, Pageable pageable);
}
