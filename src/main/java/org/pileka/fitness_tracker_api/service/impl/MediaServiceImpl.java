package org.pileka.fitness_tracker_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.pileka.fitness_tracker_api.domain.Media;
import org.pileka.fitness_tracker_api.dto.media.MediaDto;
import org.pileka.fitness_tracker_api.mapper.MediaMapper;
import org.pileka.fitness_tracker_api.repository.UserRepository;
import org.pileka.fitness_tracker_api.repository.MediaRepository;
import org.pileka.fitness_tracker_api.security.AuthUserUtil;
import org.pileka.fitness_tracker_api.service.MediaService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {

    private final MediaRepository mediaRepository;
    private final UserRepository userRepository;
    private final MediaMapper mediaMapper;

    public MediaDto create(MediaDto createDto) {
        Media newMedia = mediaMapper.toModel(createDto,
                userRepository.findByUsername(
                        AuthUserUtil.getCurrentUser().getUsername()
                ).get());

        return mediaMapper.toDto(mediaRepository.save(newMedia));
    }
}
