package org.pileka.fitness_tracker_api.util;

import lombok.experimental.UtilityClass;
import org.pileka.fitness_tracker_api.domain.Media;
import org.pileka.fitness_tracker_api.dto.media.MediaDto;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.pileka.fitness_tracker_api.util.UserTestUtil.*;

@UtilityClass
public class MediaTestUtil {
    public final Long MEDIA_ID = 1L;
    public final byte[] IMAGE_BYTES = new byte[] { 1, 2, 3 };

    // in bytes. Should probably be taken from application properties, but not really worth it
    public final long MAX_SIZE = 10485760L;

    public Media getTestMedia() {
        Media testMedia = new Media();

        testMedia.setId(MEDIA_ID);
        testMedia.setImage(IMAGE_BYTES);
        testMedia.setUser(testUser);

        return testMedia;
    }

    public MediaDto getTestMediaDtoMock() {
        MediaDto testMediaDto = mock(MediaDto.class);
        try {
            when(testMediaDto.getBytes()).thenReturn(IMAGE_BYTES);
        }
        catch (IOException e) {} // Swallowing the exception because it won't be thrown anyway

        when(testMediaDto.isEmpty()).thenReturn(false);
        when(testMediaDto.getType()).thenReturn("image/jpg");
        when(testMediaDto.getSize()).thenReturn(8L * 1024 * 1024);

        return testMediaDto;
    }
}
