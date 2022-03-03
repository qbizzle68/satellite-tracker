/**
 * @file
 * Contains the NoLightException used to signal a satellite is eclipsed.
 */

package com.qbizzle.exception;

/**
 * Signals that a satellite is eclipsed even though there may be a pass
 * above a given horizon.
 */
@SuppressWarnings("unused")
public class NoLightException extends RuntimeException {

    /**
     * Constructs an {@code NoLightException} with null as its error
     * detail message.
     */
    public NoLightException() {
        super();
    }

    /**
     * Constructs a {@code NoLightException} with the specified detail
     * message.
     * @param errorMessage
     *          The detail message.
     */
    public NoLightException(String errorMessage) {
        super(errorMessage);
    }

}
