/** @file
 * Contains the exception for indicating a satellite pass occurs during
 * daylight and will most likely not be visible.
 */

package com.qbizzle.exception;

/**
 * Signals that the pass being computed occurs while the apparent
 * position of the sun is above the horizon.
 */
@SuppressWarnings("unused")
public class DaylightPassException extends RuntimeException {

    /**
     * Constructs a {@code DaylightPassException} with null as its
     * error detail message.
     */
    public DaylightPassException() {
        super();
    }

    /**
     * Constructs a {@code DaylightPassException} with the specified
     * detail message.
     * @param errorMessage
     *        The detail message.
     */
    public DaylightPassException(String errorMessage) {
        super(errorMessage);
    }

}




