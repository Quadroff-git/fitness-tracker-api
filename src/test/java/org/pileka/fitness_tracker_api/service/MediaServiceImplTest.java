package org.pileka.fitness_tracker_api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.pileka.fitness_tracker_api.domain.Media;
import org.pileka.fitness_tracker_api.domain.User;
import org.pileka.fitness_tracker_api.dto.media.MediaDto;
import org.pileka.fitness_tracker_api.repository.MediaRepository;
import org.pileka.fitness_tracker_api.repository.UserRepository;
import org.pileka.fitness_tracker_api.service.impl.MediaServiceImpl;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MediaServiceImplTest {

    MediaRepository mediaRepository;
    UserRepository userRepository;
    MediaServiceImpl mediaService;

    Media testMedia;
    MediaDto testDto;
    User testUser;

    private static final Long MEDIA_ID = 1L;
    private static final String USERNAME = "testuser";
    private static final byte[] IMAGE_BYTES = new byte[] { 1, 2, 3 };

    MediaServiceImplTest() {
        this.mediaRepository = mock(MediaRepository.class);
        this.userRepository = mock(UserRepository.class);
        this.mediaService = new MediaServiceImpl(
                mediaRepository,
                userRepository,
                new ModelMapper()
        );
    }

    @BeforeEach
    void setUpTestEntities() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername(USERNAME);

        testMedia = new Media();
        testMedia.setId(MEDIA_ID);
        testMedia.setImage(IMAGE_BYTES);
        testMedia.setUser(testUser);

        testDto = new MediaDto();
        testDto.setImage(IMAGE_BYTES);
    }

    @Test
    void createWithoutUserDetailsThrowsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> {
            mediaService.create(testDto);
        });
    }

    @Test
    void createWithUserDetailsSavesMediaAndReturnsDto() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(testUser));
        when(mediaRepository.save(any(Media.class))).thenReturn(testMedia);

        MediaDto result = mediaService.create(testDto, testUser);

        assertNotNull(result);
        assertArrayEquals(IMAGE_BYTES, result.getImage());

        verify(userRepository).findByUsername(USERNAME);
        verify(mediaRepository).save(any(Media.class));
    }

    @Test
    void updateReturnsUpdatedDtoWhenMediaExists() {
        when(mediaRepository.findById(MEDIA_ID)).thenReturn(Optional.of(testMedia));
        when(mediaRepository.save(any(Media.class))).thenReturn(testMedia);

        byte[] updatedImage = new byte[] { 9, 9, 9 };
        MediaDto updateDto = new MediaDto();
        updateDto.setImage(updatedImage);

        Optional<MediaDto> result = mediaService.update(MEDIA_ID, updateDto);

        assertTrue(result.isPresent());
        assertArrayEquals(updatedImage, result.get().getImage());

        verify(mediaRepository).save(any(Media.class));
    }

    @Test
    void updateReturnsEmptyOptionalWhenMediaNotFound() {
        when(mediaRepository.findById(MEDIA_ID)).thenReturn(Optional.empty());

        Optional<MediaDto> result = mediaService.update(MEDIA_ID, testDto);

        assertTrue(result.isEmpty());
        verify(mediaRepository, never()).save(any());
    }
}
