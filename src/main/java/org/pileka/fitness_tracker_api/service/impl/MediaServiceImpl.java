package org.pileka.fitness_tracker_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.pileka.fitness_tracker_api.domain.Media;
import org.pileka.fitness_tracker_api.dto.media.MediaDto;
import org.pileka.fitness_tracker_api.repository.UserRepository;
import org.pileka.fitness_tracker_api.repository.MediaRepository;
import org.pileka.fitness_tracker_api.service.MediaService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {

    private final MediaRepository mediaRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public MediaDto create(MediaDto createDto, UserDetails userDetails) {
        Media newMedia = modelMapper.map(createDto, Media.class);
        newMedia.setUser(userRepository.findByUsername(userDetails.getUsername()).get());

        return modelMapper.map(mediaRepository.save(newMedia), MediaDto.class);
    }
}
