package org.pileka.fitness_tracker_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.pileka.fitness_tracker_api.domain.User;
import org.pileka.fitness_tracker_api.dto.auth.RegistrationDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toModel(RegistrationDto dto);
}
