/**
 * @file
 * Contains the {@code NoPassException} which signals that a satellite
 * is not above a given horizon during a certain time period.
 */

package com.qbizzle.exception;

/**
 * Signals that there is no overhead pass during a specified time frame.
 * Intended to be used to signal the stop of further computation for
 * finding pass information, as it is no longer relevant.
 */
public class NoPassException extends RuntimeException {

    /**
     * Constructs an {@code NoPassException} with null as its error
     * detail message.
     */
    public NoPassException() {
        super();
    }

    /**
     * Constructs an {@code NoPassException} with the specified
     * detail message.
     * @param errorMessage
     *          The detail messgae.
     */
    public NoPassException(String errorMessage) {
        super(errorMessage);
    }
}
