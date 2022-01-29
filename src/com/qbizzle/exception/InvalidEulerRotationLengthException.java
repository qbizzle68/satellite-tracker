/** @file Contains InvalidEulerRotationLengthException class used
 * when an Euler rotation order String is not valid length (which is usually 3).
 */
package com.qbizzle.exception;

/** Exception used to indicate the order of an Euler rotation is not
 * a valid length.
 */
public class InvalidEulerRotationLengthException extends Exception {

    /** Constructs an InvalidEulerRotationLengthException with an error message.
     * @param errorMessage The error message for this exception.
     */
    public InvalidEulerRotationLengthException(String errorMessage) {
        super(errorMessage);
    }

}
