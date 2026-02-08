package org.pileka.fitness_tracker_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.pileka.fitness_tracker_api.domain.Media;
import org.pileka.fitness_tracker_api.domain.User;
import org.pileka.fitness_tracker_api.dto.media.MediaDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MediaMapper {

    @Mapping(target = "id", expression = "java(null)") // maps user.id to media.id without this line
    @Mapping(target = "image", expression = "java(dto.getBytes())")
    Media toModel(MediaDto dto, User user);
}
