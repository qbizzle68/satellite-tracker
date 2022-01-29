/** @file This file contains the InvalidTLEException class, which is
 * used by the TLE class to alert to a badly formatted TLE text.
 */

package com.qbizzle.exception;

/**
 * This exception class is used to signal a bad formatted TLE string.
 * @todo possibly derive more descriptive exceptions that extend this class
 */
public class InvalidTLEException extends Exception {

    /** Constructs an InvalidTLEException with an error message.
     * @param errorMessage The error message for this exception.
     */
    public InvalidTLEException(String errorMessage) {
        super(errorMessage);
    }

}
