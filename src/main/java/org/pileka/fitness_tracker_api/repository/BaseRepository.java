package org.pileka.fitness_tracker_api.repository;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BaseRepository<T, ID> extends ListCrudRepository<T, ID>, PagingAndSortingRepository<T, ID> { }
