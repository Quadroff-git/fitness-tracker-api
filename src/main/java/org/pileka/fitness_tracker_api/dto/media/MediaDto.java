package org.pileka.fitness_tracker_api.dto.media;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaDto {
    @NotEmpty(message="image cannot be empty")
    private byte[] image;
}
