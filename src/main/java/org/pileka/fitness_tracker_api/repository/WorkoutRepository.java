package org.pileka.fitness_tracker_api.repository;

import org.pileka.fitness_tracker_api.domain.Workout;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface WorkoutRepository extends ListCrudRepository<Workout, Long>, PagingAndSortingRepository<Workout, Long>, JpaSpecificationExecutor<Workout> {}
