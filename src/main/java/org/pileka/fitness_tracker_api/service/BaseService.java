package org.pileka.fitness_tracker_api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Base service interface with CRUD operations
 *
 * @param <T> Entity class
 * @param <R> DTO class for reading
 * @param <C> DTO class for creating
 * @param <U> DTO class for updating
 * @param <ID> Identifier class
 */
public interface BaseService<T, R, C, U, ID> {
    /**
     * Create a new entity
     *
     * @param createDto DTO for creation
     * @return created DTO
     */
    R create(C createDto);

    /**
     * Get all entities
     *
     * @return List of entity DTOs
     */
    List<R> findAll();

    /**
     * Get all entities with pagination
     *
     * @param pageable pagination and sorting parameters
     * @return page of entity DTOs
     */
    Page<R> findAll(Pageable pageable);

    /**
     * Find entity by ID
     *
     * @param id entity identifier
     * @return entity DTO or null wrapped in Optional if no entity
     * with specified ID is found
     */
    Optional<R> findById(ID id);

    /**
     * Check if entity exists by ID
     *
     * @param id entity identifier
     * @return true if entity exists
     */
    boolean existsById(ID id);

    /**
     * Update an existing entity
     *
     * @param id        entity identifier
     * @param updateDto DTO for update
     * @return updated entity DTO or null wrapped in Optional if no entity
     * with specified ID is found
     */
    Optional<R> update(ID id, U updateDto);

    /**
     * Delete an entity by ID
     *
     * @param id entity identifier
     * @return deleted entity DTO or null wrapped in Optional if no entity
     * with specified ID is found
     */
    Optional<R> delete(ID id);
}
