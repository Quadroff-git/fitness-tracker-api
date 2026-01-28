package org.pileka.fitness_tracker_api.repository;

import org.pileka.fitness_tracker_api.domain.User;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface UserRepository extends ListCrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
