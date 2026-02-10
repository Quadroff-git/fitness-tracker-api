package org.pileka.fitness_tracker_api.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pileka.fitness_tracker_api.domain.Media;
import org.pileka.fitness_tracker_api.dto.media.MediaDto;
import org.pileka.fitness_tracker_api.exception.InvalidFileUploadedException;
import org.pileka.fitness_tracker_api.mapper.MediaMapper;
import org.pileka.fitness_tracker_api.repository.MediaRepository;
import org.pileka.fitness_tracker_api.repository.UserRepository;
import org.pileka.fitness_tracker_api.service.impl.MediaServiceImpl;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.pileka.fitness_tracker_api.util.MediaTestUtil.*;
import static org.pileka.fitness_tracker_api.util.UserTestUtil.*;
import static org.pileka.fitness_tracker_api.util.AuthTestUtil.*;

@ExtendWith(MockitoExtension.class)
class MediaServiceImplTest {

    MediaRepository mediaRepository;
    UserRepository userRepository;
    MediaServiceImpl mediaService;

    MediaServiceImplTest() {
        this.mediaRepository = mock(MediaRepository.class);
        this.userRepository = mock(UserRepository.class);
        this.mediaService = new MediaServiceImpl(
                mediaRepository,
                userRepository,
                Mappers.getMapper(MediaMapper.class)
        );

        // Setting a @Value field
        ReflectionTestUtils.setField(mediaService, "MAX_FILE_SIZE", MAX_SIZE);
    }

    @Test
    void createSavesMedia() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(testUser));
        when(mediaRepository.save(any(Media.class))).thenReturn(getTestMedia());

        doWithMockedAuthUserUtil(
                testUserDetails,
                () -> mediaService.create(getTestMediaDtoMock())
        );

        verify(userRepository).findByUsername(USERNAME);
        verify(mediaRepository).save(any(Media.class));
    }

    @Test
    void createThrowsExceptionWhenFileEmpty() {
        MediaDto testMediaDto = mock(MediaDto.class);
        when(testMediaDto.isEmpty()).thenReturn(true);

        assertCreateThrowsExceptionForMediaDto(testMediaDto);
    }

    @Test
    void createThrowsExceptionWhenFileTooBig() {
        final long SIZE = 20L * 1024 * 1024; // in bytes
        assertTrue(SIZE > MAX_SIZE); // just in case

        MediaDto testMediaDto = mock(MediaDto.class);
        when(testMediaDto.getType()).thenReturn("image/jpg");
        when(testMediaDto.getSize()).thenReturn(SIZE);

        assertCreateThrowsExceptionForMediaDto(testMediaDto);
    }

    @Test
    void createThrowsExceptionWhenFileNotImage() {
        final String TYPE = "application/msword";
        assertTrue(!TYPE.startsWith("image"));

        MediaDto testMediaDto = mock(MediaDto.class);
        when(testMediaDto.getType()).thenReturn(TYPE);

        assertCreateThrowsExceptionForMediaDto(testMediaDto);
    }

    void assertCreateThrowsExceptionForMediaDto(MediaDto mediaDto) {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(testUser));
        when(mediaRepository.save(any(Media.class))).thenReturn(getTestMedia());

        // Method is called outside of static UserAuthUtil stubbing because it's not supposed to reach the call to it
        assertThrows(InvalidFileUploadedException.class, () -> mediaService.create(mediaDto));

        verify(mediaRepository, never()).save(any(Media.class));
    }
}
