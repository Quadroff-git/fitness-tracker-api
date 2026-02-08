package org.pileka.fitness_tracker_api.service;

import org.pileka.fitness_tracker_api.dto.media.MediaDto;
import org.pileka.fitness_tracker_api.exception.InvalidFileUploadedException;

public interface MediaService {

    /**
     * Save media associated with the user
     *
     * @param createDto Framework-specific dto implementation containing the uploaded file
     * @throws InvalidFileUploadedException if the file doesn't meet business criteria (size, format) or is empty
     */
    void create(MediaDto createDto) throws InvalidFileUploadedException;
}
