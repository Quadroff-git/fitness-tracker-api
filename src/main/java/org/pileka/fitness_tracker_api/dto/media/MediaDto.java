package org.pileka.fitness_tracker_api.dto.media;

import java.io.IOException;

/*
 * Since file uploading is very implementation-specific (e.g. Spring only provides proprietary MultipartFile), and
 * the priority is thin controller logic, I've decided to provide a universal interface for media upload dtos
 * to not make service layer api dependent on framework-specific classes
 */
public interface MediaDto {
    /**
     * Returns a string representing a MIME type of the file
     * @return a String MIME type representation
     */
    String getType();

    /**
     * Returns the size of the file in bytes
     * @return size of file in bits
     */
    long getSize();

    /**
     * Returns the file as bytes[]
     * @return a byte array representing the file
     * @throws IOException - in case of access errors
     */
    byte[] getBytes() throws IOException;

    /**
     * Return whether the uploaded file is empty
     */
    boolean isEmpty();
}
