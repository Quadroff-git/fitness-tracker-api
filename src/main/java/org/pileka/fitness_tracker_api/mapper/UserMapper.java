package org.pileka.fitness_tracker_api.mapper;

import org.mapstruct.*;
import org.pileka.fitness_tracker_api.domain.User;
import org.pileka.fitness_tracker_api.dto.auth.RegistrationDto;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class UserMapper {
    @Mapping(target = "password", ignore = true)
    public abstract User toModel(RegistrationDto dto, @Context PasswordEncoder passwordEncoder);

    @AfterMapping
    protected void encodePassword(RegistrationDto dto, @MappingTarget User user,  @Context PasswordEncoder passwordEncoder) {
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
    }
}
