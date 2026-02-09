package org.pileka.fitness_tracker_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.pileka.fitness_tracker_api.dto.media.MediaDto;
import org.pileka.fitness_tracker_api.exception.InvalidFileUploadedException;
import org.pileka.fitness_tracker_api.mapper.MediaMapper;
import org.pileka.fitness_tracker_api.repository.UserRepository;
import org.pileka.fitness_tracker_api.repository.MediaRepository;
import org.pileka.fitness_tracker_api.security.AuthUserUtil;
import org.pileka.fitness_tracker_api.service.MediaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {

    private final MediaRepository mediaRepository;
    private final UserRepository userRepository;
    private final MediaMapper mediaMapper;

    @Value("${media.max-file-size}")
    private int MAX_FILE_SIZE;

    public void create(MediaDto createDto) {
        if (createDto.isEmpty()) {
            throw new InvalidFileUploadedException("No file uploaded or the uploaded file has no content");
        }

        if (!createDto.getType().startsWith("image")) {
            throw new InvalidFileUploadedException("File must be an image");
        }

        if (createDto.getSize() > MAX_FILE_SIZE) {
            throw new InvalidFileUploadedException("File size must not exceed " + MAX_FILE_SIZE + " bytes");
        }

        try {
            mediaRepository.save(
                    mediaMapper.toModel(
                            createDto,
                            userRepository.findByUsername(
                                    AuthUserUtil.getCurrentUser().getUsername()
                            ).get()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
