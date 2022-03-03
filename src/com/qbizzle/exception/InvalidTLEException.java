/** @file
 * This file contains the InvalidTLEException class, which is
 * used by the TLE class to alert to a badly formatted TLE text.
 */

package com.qbizzle.exception;

/**
 * Signals there was an error while parsing a TLE string.
 * @todo possibly derive more descriptive exceptions that extend this class
 */
public class InvalidTLEException extends RuntimeException {

    /**
     * Constructs an {@code InvalidTLEException} with null as its error
     * detail message.
     */
    public InvalidTLEException() {
        super();
    }

    /** Constructs an {@code InvalidTLEException} with the specified
     * detail message.
     * @param errorMessage
     *          The detail message.
     */
    public InvalidTLEException(String errorMessage) {
        super(errorMessage);
    }

}
