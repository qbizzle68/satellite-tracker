/** @file
 * Contains a class for keeping track of the order for any Euler
 * rotation.
 */

package com.qbizzle.referenceframe;

import java.util.Objects;

/** This class contains the order for an Euler rotation, as well as an
 * enumeration for accessing the correct values from an EulerAngles instance.
 */
public class EulerOrder {

    /** An enumeration for accessing the correct value of an EulerAngle.
     */
    public enum RotationNumber {
        first, second, third
    }

    /** The axis for the first rotation. */
    public final Axis.Direction first_rotation;
    /** The axis for the second rotation. */
    public final Axis.Direction second_rotation;
    /** The axis for the third rotation. */
    public final Axis.Direction third_rotation;

    /** Constructs a rotation from three directions.
     * @param first_axis The direction of the first rotation.
     * @param second_axis The direction of the second rotation.
     * @param third_axis The direction of the third rotation.
     */
    public EulerOrder(Axis.Direction first_axis, Axis.Direction second_axis, Axis.Direction third_axis) {
        first_rotation = first_axis;
        second_rotation = second_axis;
        third_rotation = third_axis;
    }

    /// @name Overridden methods
    /// Overridden methods from Java Object.
///@{

    /** Converts this object to a string.
     * @return A string representation of this object.
     */
    @Override
    public String toString() {
        return "EulerOrder{" +
               "first_rotation=" + first_rotation +
               ", second_rotation=" + second_rotation +
               ", third_rotation=" + third_rotation +
               '}';
    }

    /** Compares if an object is equivalent to this instance.
     * @param o Object to compare this instance to.
     * @return True if o is of type EulerOrder and the axes are equivalent, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EulerOrder that = (EulerOrder) o;
        return first_rotation == that.first_rotation && second_rotation == that.second_rotation && third_rotation == that.third_rotation;
    }

    /** Generates a hashcode for this instance.
     * @return The hash code representing this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(first_rotation, second_rotation, third_rotation);
    }

///@}

}
