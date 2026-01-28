package org.pileka.fitness_tracker_api.repository;

import org.pileka.fitness_tracker_api.domain.User;

import java.util.Optional;

public interface UserRepository extends BaseRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
