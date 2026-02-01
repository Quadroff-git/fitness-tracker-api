package org.pileka.fitness_tracker_api.service.impl;

import org.modelmapper.ModelMapper;
import org.pileka.fitness_tracker_api.domain.Media;
import org.pileka.fitness_tracker_api.dto.media.MediaDto;
import org.pileka.fitness_tracker_api.repository.UserRepository;
import org.pileka.fitness_tracker_api.repository.MediaRepository;
import org.pileka.fitness_tracker_api.service.MediaService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MediaServiceImpl extends BaseServiceImpl<Media, MediaDto, MediaDto, MediaDto, Long> implements MediaService {

    MediaRepository mediaRepository;
    UserRepository userRepository;

    public MediaServiceImpl(MediaRepository mediaRepository, UserRepository userRepository, ModelMapper modelMapper) {
        super(mediaRepository, modelMapper);

        this.mediaRepository = mediaRepository;
        this.userRepository = userRepository;
    }

    @Override
    public MediaDto create(MediaDto createDto) {
        throw new UnsupportedOperationException("Can't save media without user information");
    }

    @Override
    public MediaDto create(MediaDto createDto, UserDetails userDetails) {
        Media newMedia = modelMapper.map(createDto, Media.class);
        newMedia.setUser(userRepository.findByUsername(userDetails.getUsername()).get());

        return modelMapper.map(mediaRepository.save(newMedia), MediaDto.class);
    }

    @Override
    public Optional<MediaDto> update(Long id, MediaDto updateDto) {
        Optional<Media> mediaToUpdate = mediaRepository.findById(id);

        if (mediaToUpdate.isPresent()) {
            Media media = mediaToUpdate.get();

            media.setImage(updateDto.getImage());

            media = mediaRepository.save(media);

            return Optional.ofNullable(modelMapper.map(media, MediaDto.class));
        }
        else {
            return Optional.empty();
        }
    }
}
