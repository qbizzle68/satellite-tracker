/** @file
 * This file contains a utility math class.
 */

package com.qbizzle.math;

/** Utility class used mostly for holding static final fields that are
 * often used and shouldn't be instantiated numerous times.
 */
public class util {
    /** Elementary vector in the x-direction, equal to (1, 0, 0). */
    public final static Vector e1 = new Vector(1.0, 0.0, 0.0);
    /** Elementary vector in the y-direction, equal to (0, 1, 0). */
    public final static Vector e2 = new Vector(0.0, 1.0, 0.0);
    /** Elementary vector in the z-direction, equal to (0, 0, 1). */
    public final static Vector e3 = new Vector(0.0, 0.0, 1.0);

    /** Identity 3x3 matrix. */
    public final static Matrix I3 = new Matrix(e1, e2, e3);

}
