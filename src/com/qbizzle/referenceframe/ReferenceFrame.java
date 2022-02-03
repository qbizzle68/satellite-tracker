/** @file
 * This file contains the ReferenceFrame class that wraps around a matrix to
 * contain the axis directions of a given reference frame.
 */

package com.qbizzle.referenceframe;

import com.qbizzle.math.Matrix;
import com.qbizzle.rotation.Rotation;

import java.util.Objects;

/** A reference frame class that acts as a wrapper around a matrix to record the axis
 * unit vectors for the frame. The columns of the matrix are the vector directions of the
 * three axes of the frame.
 */
public class ReferenceFrame {
    private Matrix m_frame;
    private final EulerOrder m_order;
    private EulerAngles m_angles;

    /** Constructs a reference frame with elementary axes */
    public ReferenceFrame() {
        this(
                new EulerOrder(Axis.Direction.X, Axis.Direction.Y, Axis.Direction.Z),
                new EulerAngles(0.0, 0.0, 0.0)
        );
    }

    /** Constructs a reference frame with an Euler order and angles.
     * @param order Euler rotation order.
     * @param angles Euler angles in rotation order, in @em degrees.
     */
    public ReferenceFrame(EulerOrder order, EulerAngles angles) {
        m_order = order;
        m_angles = angles;
        m_frame = Rotation.getEulerMatrix(order, angles);
    }

    /// @name Overridden methods
    /// Overridden methods inherited from Object.
///@{

    /** Converts the reference frame to a String representation of the
     * internal matrix storing the basis vectors, as well as the order and angles
     * associated with the reference frame.
     * @return A string representing this instance.
     */
    @Override
    public String toString() {
        return "ReferenceFrame{" +
               "m_frame=" + m_frame +
               ", m_order=" + m_order +
               ", m_angles=" + m_angles +
               '}';
    }

    /** Compares the contents of this object to another one.
     * @param o The object to compare this instance to.
     * @return True if @p o is of type ReferenceFrame and the internal
     * matrix and axes are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReferenceFrame that = (ReferenceFrame) o;
        return m_frame.equals(that.m_frame) && m_order.equals(that.m_order) && m_angles.equals(that.m_angles);
    }

    /** Creates a hashcode for the reference frame.
     * @return The hashcode for this instance.
     */
    @Override
    public int hashCode() {
        return Objects.hash(m_frame, m_order, m_angles);
    }

///@}

    /// @name Accessor methods
    /// Accessor methods for the axes and vectors of the reference frame.
///@{

    /** Retrieves the axis direction associated with the rotation number.
     * @param number The Euler rotation number.
     * @return The rotation direction of the rotation.
     */
    public Axis.Direction axis(EulerOrder.RotationNumber number) {
        return switch (number) {
            case first -> m_order.first_rotation;
            case second -> m_order.second_rotation;
            case third -> m_order.third_rotation;
        };
    }

    /** Sets the EulerAngles and adjusts the internal matrix.
     * @param angles The angles in their rotation order in @em degrees.
     */
    public void setAngles(EulerAngles angles) {
        m_angles = angles;
        m_frame = Rotation.getEulerMatrix(m_order, m_angles);
    }

    /** Converts reference frame to a matrix whose column vectors are the
     * basis vectors of this frame.
     * @return A copy of the matrix representing this reference frame.
     */
    public Matrix toMatrix() {
        return (Matrix)m_frame.clone();
    }

///@}

}
