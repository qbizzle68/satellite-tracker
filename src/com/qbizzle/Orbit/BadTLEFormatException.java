/** @file
 * This file contains the BadTLEFormatException class, which is
 * used by the TLE class to alert to a badly formatted TLE text.
 */

package com.qbizzle.Orbit;

/**
 * This exception class is used to signal a bad formatted TLE string.
 * @todo possibly derive more descriptive exceptions that extend this class
 */
public class BadTLEFormatException extends Exception {
    public BadTLEFormatException(String errorMessage) {
        super(errorMessage);
    }
}
