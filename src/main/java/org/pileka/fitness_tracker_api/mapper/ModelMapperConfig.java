package org.pileka.fitness_tracker_api.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public static ModelMapper configModelMapper() {
        return new ModelMapper();
    }
}
