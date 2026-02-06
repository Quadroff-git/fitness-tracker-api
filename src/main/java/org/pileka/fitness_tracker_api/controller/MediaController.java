package org.pileka.fitness_tracker_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public interface MediaController {
    @Operation(summary = "Upload media",
            tags = {"media"},
            description = "Adds new media associated with the user",
            responses = {@ApiResponse(description = "Success message")}
    )
    ResponseEntity<String> addMedia(@AuthenticationPrincipal UserDetails userDetails, @RequestParam MultipartFile image);
}
