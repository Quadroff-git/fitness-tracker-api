package org.pileka.fitness_tracker_api.service;

import org.pileka.fitness_tracker_api.domain.Media;
import org.pileka.fitness_tracker_api.dto.media.MediaDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface MediaService extends BaseService<Media, MediaDto, MediaDto, MediaDto, Long> {
    MediaDto create(MediaDto createDto, UserDetails userDetails);
}
