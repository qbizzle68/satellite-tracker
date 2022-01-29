/** @file Contains InvalidRotationAxis exception which is used to
 * alert when an invalid labeled rotation axis.
 */
package com.qbizzle.exception;

/** Exception thrown when a character or string representing
 * axes contains an invalid character (i.e. not 'x', 'y' or 'z').
 */
public class InvalidAxisException extends Exception {

    /** Constructs an InvalidAxisException with an error message.
     * @param errorMessage The error message for this exception.
     */
    public InvalidAxisException(String errorMessage) {
        super(errorMessage);
    }

}
