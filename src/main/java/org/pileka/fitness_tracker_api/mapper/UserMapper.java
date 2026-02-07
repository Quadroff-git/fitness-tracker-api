package org.pileka.fitness_tracker_api.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.pileka.fitness_tracker_api.domain.User;
import org.pileka.fitness_tracker_api.dto.auth.RegistrationDto;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class UserMapper {
    public abstract User toModel(RegistrationDto dto, @Context PasswordEncoder passwordEncoder);

    protected String encodePassword(String password, @Context PasswordEncoder passwordEncoder) {
        return passwordEncoder.encode(password);
    }
}
