package org.pileka.fitness_tracker_api.controller.impl;

import lombok.RequiredArgsConstructor;
import org.pileka.fitness_tracker_api.controller.MediaController;
import org.pileka.fitness_tracker_api.dto.media.impl.MediaDtoImpl;
import org.pileka.fitness_tracker_api.service.MediaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/media")
@RequiredArgsConstructor
public class MediaControllerImpl implements MediaController {
    private final MediaService mediaService;

    @PostMapping(produces = "plain/text", consumes="multipart/form-data")
    @Override
    public ResponseEntity<String> addMedia(@RequestParam MultipartFile image) {
        mediaService.create(new MediaDtoImpl(image));
        return ResponseEntity.ok().build();
    }
}
