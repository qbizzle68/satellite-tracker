/** @File Contains the ArrayLengthException checked exception, which
 * is used to alert an array of invalid length.
 */

package com.qbizzle.exception;

/** A checked exception thrown when an array of specific length
 * is not passed into a method as an argument.
 */
public class ArrayLengthException extends Exception {

    /** Constructs an ArrayLengthException with an error message.
     * @param errorMessage The error message for this exception.
     */
    ArrayLengthException(String errorMessage) {
        super(errorMessage);
    }

}
