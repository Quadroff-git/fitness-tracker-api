package org.pileka.fitness_tracker_api.service.impl;

import org.modelmapper.ModelMapper;
import org.pileka.fitness_tracker_api.repository.BaseRepository;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public abstract class BaseServiceImpl<T, R, C, U, ID> implements org.pileka.fitness_tracker_api.service.BaseService<T, R, C, U, ID> {

    private final BaseRepository<T, ID> repository;
    protected final ModelMapper modelMapper;

    private final Class<T> entityClass;
    private final Class<R> readDtoClass;


    public BaseServiceImpl(BaseRepository<T, ID> repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;

        // ModelMapper relies on class objects, so we get them here using Spring's GenericTypeResolver
        var classes = GenericTypeResolver.resolveTypeArguments(getClass(), BaseServiceImpl.class);
        this.entityClass = (Class<T>) classes[0];
        this.readDtoClass = (Class<R>) classes[1];
    }

    @Override
    public R create(C createDto) {
        T newEntity = modelMapper.map(createDto, entityClass);
        return modelMapper.map(repository.save(newEntity), readDtoClass);
    }

    @Override
    public List<R> findAll() {
        return repository.findAll().stream().map(entity -> modelMapper.map(entity, readDtoClass)).toList();
    }

    @Override
    public Page<R> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(entity -> modelMapper.map(entity, readDtoClass));
    }

    @Override
    public Optional<R> findById(ID id) {
        return Optional.ofNullable(modelMapper.map(repository.findById(id), readDtoClass));
    }

    @Override
    public boolean existsById(ID id) {
        return repository.existsById(id);
    }

    @Override
    public abstract Optional<R> update(ID id, U updateDto);

    @Override
    public Optional<R> delete(ID id) {
        Optional<T> entityToDelete = repository.findById(id);

        if (entityToDelete.isPresent()) {
            repository.delete(entityToDelete.get());

            return Optional.ofNullable(modelMapper.map(entityToDelete, readDtoClass));
        }
        else {
            return Optional.empty();
        }
    }
}
