/** @File Contains the ArrayLengthException checked exception, which
 * is used to alert an array of invalid length.
 */

package com.qbizzle.exception;

/** Unchecked exception thrown when an array of specific length
 * is not passed into a method as an argument.
 */
public class ArrayLengthException extends RuntimeException {

    /** Constructs an ArrayLengthException with an error message.
     * @param errorMessage The error message for this exception.
     */
    public ArrayLengthException(String errorMessage) {
        super(errorMessage);
    }

}
