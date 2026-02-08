package org.pileka.fitness_tracker_api.controller.impl;

import lombok.RequiredArgsConstructor;
import org.pileka.fitness_tracker_api.controller.MediaController;
import org.pileka.fitness_tracker_api.dto.media.MediaDto;
import org.pileka.fitness_tracker_api.service.MediaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
        if (image.getContentType() == null || !image.getContentType().startsWith("image/")) {
            return ResponseEntity.badRequest().body("Only image uploading is allowed");
        }

        if (image.getSize() > 10 * 1024 * 1024) {
            return ResponseEntity.badRequest().body("Image size exceeds 10MB limit");
        }

        MediaDto mediaDto;
        try {
            mediaDto = new MediaDto(image.getBytes());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Something failed when reading the image");
        }

        mediaService.create(mediaDto);

        return ResponseEntity.ok("Saved successfully");
    }
}
