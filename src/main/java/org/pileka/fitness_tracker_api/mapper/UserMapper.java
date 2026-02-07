package org.pileka.fitness_tracker_api.mapper;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.pileka.fitness_tracker_api.domain.User;
import org.pileka.fitness_tracker_api.dto.auth.RegistrationDto;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class UserMapper {

    private final PasswordEncoder passwordEncoder;

    @Mapping(target = "password", expression = "java(passwordEncoder.encode(dto.getPassword())")
    public abstract User toModel(RegistrationDto dto);
}
