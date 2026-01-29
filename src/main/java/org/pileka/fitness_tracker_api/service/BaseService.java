package org.pileka.fitness_tracker_api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Base service interface with CRUD operations
 *
 * @param <T> the type of DTO for reading
 * @param <C> the type of DTO for creating
 * @param <U> the type of DTO for updating
 * @param <ID> the type of identifier
 */
public interface BaseService<T, C, U, ID> {

    /**
     * Create a new entity
     *
     * @param createDto DTO for creation
     * @return created DTO
     */
    T create(C createDto);

    /**
     * Get all entities
     *
     * @return List of entity DTOs
     */
    List<T> findAll();

    /**
     * Get all entities with pagination
     *
     * @param pageable pagination and sorting parameters
     * @return page of entity DTOs
     */
    Page<T> findAll(Pageable pageable);

    /**
     * Find entity by ID
     *
     * @param id entity identifier
     * @return entity DTO or null wrapped in Optional if no entity
     * with specified ID is found
     */
    Optional<T> findById(ID id);

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
     * @param id entity identifier
     * @param updateDto DTO for update
     * @return updated entity DTO or null wrapped in Optional if no entity
     * with specified ID is found
     */
    Optional<T> update(ID id, U updateDto);

    /**
     * Delete an entity by ID
     *
     * @param id entity identifier
     * @return deleted entity DTO or null wrapped in Optional if no entity
     * with specified ID is found
     */
    Optional<T> delete(ID id);
}
