package org.pileka.fitness_tracker_api.dto.media.impl;

import lombok.AllArgsConstructor;
import org.pileka.fitness_tracker_api.dto.media.MediaDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

// A Spring-specific implementation using MutlipartFile
@AllArgsConstructor
public class MediaDtoImpl implements MediaDto {

    private MultipartFile file;

    @Override
    public String getType() {
        return file.getContentType();
    }

    @Override
    public long getSize() {
        return file.getSize();
    }

    @Override
    public byte[] getBytes() throws IOException {
        return file.getBytes();
    }

    @Override
    public boolean isEmpty() {
        return file.isEmpty();
    }
}
