package org.pileka.fitness_tracker_api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.pileka.fitness_tracker_api.repository.BaseRepository;
import org.pileka.fitness_tracker_api.service.impl.BaseServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BaseServiceImplTest {

    BaseRepository<TestEntity, Long> repository;
    TestServiceImpl service;

    TestEntity testEntity;
    TestCreateDto testCreateDto;

    private static final Long ENTITY_ID = 1L;
    private static final String NAME = "test";

    BaseServiceImplTest() {
        this.repository = mock(BaseRepository.class);
        this.service = new TestServiceImpl(repository, new ModelMapper());
    }

    @BeforeEach
    void setUpTestEntities() {
        testEntity = new TestEntity();
        testEntity.setId(ENTITY_ID);
        testEntity.setName(NAME);

        testCreateDto = new TestCreateDto();
        testCreateDto.setName(NAME);
    }

    @Test
    void createReturnsReadDto() {
        when(repository.save(any(TestEntity.class))).thenReturn(testEntity);

        TestReadDto result = service.create(testCreateDto);

        assertEquals(ENTITY_ID, result.getId());
        assertEquals(NAME, result.getName());

        verify(repository).save(any(TestEntity.class));
    }

    @Test
    void findAllReturnsAllMappedDtos() {
        TestEntity second = new TestEntity();
        second.setId(2L);
        second.setName("second");

        when(repository.findAll()).thenReturn(List.of(testEntity, second));

        List<TestReadDto> result = service.findAll();

        assertEquals(2, result.size());
        assertEquals(NAME, result.get(0).getName());
        assertEquals("second", result.get(1).getName());
    }

    @Test
    void findAllWithPageableReturnsPagedDtos() {
        Page<TestEntity> page = new PageImpl<>(List.of(testEntity));

        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        Page<TestReadDto> result = service.findAll(Pageable.unpaged());

        assertEquals(1, result.getTotalElements());
        assertEquals(NAME, result.getContent().get(0).getName());
    }

    @Test
    void findByIdReturnsMappedDtoWhenPresent() {
        when(repository.findById(ENTITY_ID)).thenReturn(Optional.of(testEntity));

        Optional<TestReadDto> result = service.findById(ENTITY_ID);

        assertTrue(result.isPresent());
        assertEquals(NAME, result.get().getName());
    }

    @Test
    void findByIdReturnsEmptyOptionalWhenNotFound() {
        when(repository.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Optional<TestReadDto> result = service.findById(ENTITY_ID);

        assertTrue(result.isEmpty());
    }

    @Test
    void existsByIdDelegatesToRepository() {
        when(repository.existsById(ENTITY_ID)).thenReturn(true);

        boolean exists = service.existsById(ENTITY_ID);

        assertTrue(exists);
        verify(repository).existsById(ENTITY_ID);
    }

    @Test
    void deleteReturnsMappedDtoWhenEntityExists() {
        when(repository.findById(ENTITY_ID)).thenReturn(Optional.of(testEntity));
        doNothing().when(repository).delete(testEntity);

        Optional<TestReadDto> result = service.delete(ENTITY_ID);

        assertTrue(result.isPresent());
        assertEquals(NAME, result.get().getName());

        verify(repository).delete(testEntity);
    }

    @Test
    void deleteReturnsEmptyOptionalWhenEntityNotFound() {
        when(repository.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Optional<TestReadDto> result = service.delete(ENTITY_ID);

        assertTrue(result.isEmpty());
        verify(repository, never()).delete(any());
    }


    // Test-only classes to get an actual instance of BaseServiceImpl to test
    // Not particularly clean, but I don't feel like introducing dto and entity superclasses
    static class TestServiceImpl
            extends BaseServiceImpl<TestEntity, TestReadDto, TestCreateDto, Void, Long> {

        TestServiceImpl(BaseRepository<TestEntity, Long> repository, ModelMapper mapper) {
            super(repository, mapper);
        }

        @Override
        public Optional<TestReadDto> update(Long id, Void updateDto) {
            throw new UnsupportedOperationException();
        }
    }

    static class TestEntity {
        private Long id;
        private String name;

        public TestEntity() {}

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    static class TestReadDto {
        private Long id;
        private String name;

        public TestReadDto() {}

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    static class TestCreateDto {
        private String name;

        public TestCreateDto() {}

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
