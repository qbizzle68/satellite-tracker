/** @file
 * This file contains the ReferenceFrame class that wraps around a matrix to
 * contain the axis directions of a given reference frame.
 */

package com.qbizzle.referenceframe;

import com.qbizzle.exception.ArrayLengthException;
import com.qbizzle.math.Matrix;
import com.qbizzle.math.Vector;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;

/** A reference frame class that acts as a wrapper around a matrix to record the axis
 * unit vectors for the frame. The columns of the matrix are the vector directions of the
 * three axes of the frame.
 */
public class ReferenceFrame implements Cloneable {
    private Matrix m_frame;
    private final Axis[] m_axes = new Axis[3];

    /** Constructs a reference frame with elementary axes */
    public ReferenceFrame() {
        m_frame = new Matrix(
                new Vector(1, 0, 0),
                new Vector(0, 1, 0),
                new Vector(0, 0, 1)
        );
        m_axes[0] = new Axis(Axis.Direction.X);
        m_axes[1] = new Axis(Axis.Direction.Y);
        m_axes[2] = new Axis(Axis.Direction.Z);
    }

    /** Constructs a reference frame from column vectors pointing
     * in the direction of the principal axes.
     * @param colVectors Array of 3 vectors representing the 3 reference
     *                   frame axes.
     */
    public ReferenceFrame(Vector[] colVectors) {
        this(colVectors, new Axis[]{new Axis(Axis.Direction.X), new Axis(Axis.Direction.X), new Axis(Axis.Direction.X)});
    }

    /** Constructs a reference frame from column vectors pointing
     * in the direction of the principal axes, and axis objects
     * containing metadata about the axes.
     * @param colVectors Array of 3 vectors representing the 3 reference
     *                   frame axes.
     * @param axes Array of 3 axis objects for the 3 principal axes.
     */
    public ReferenceFrame(Vector[] colVectors, Axis[] axes) {
        if (Array.getLength(colVectors) != 3)
            throw new ArrayLengthException("colVectors array is not of length=3, (length="+Array.getLength(colVectors)+")");
        if (Array.getLength(axes) != 3)
            throw new ArrayLengthException("axes array is not of length=3, (length="+Array.getLength(axes)+")");
        m_frame = new Matrix(colVectors[0], colVectors[1], colVectors[2]);
        System.arraycopy(axes, 0, m_axes, 0, 3);
    }

    /** Constructs a reference frame from a matrix whose column vectors
     * point in the direction of the principal axes.
     * @param frame Matrix that represents the rotation to this reference frame.
     */
    public ReferenceFrame(Matrix frame) {
        this(frame, new Axis[]{new Axis(Axis.Direction.X), new Axis(Axis.Direction.X), new Axis(Axis.Direction.X)});
    }

    /** Constructs a reference frame from a matrix whose column vectors
     * point in the direction of the principal axes.
     * @param frame Matrix that represents the rotation to this reference frame.
     * @param axes Array of 3 axis objects for the 3 principal axes.
     */
    public ReferenceFrame(Matrix frame, Axis[] axes) {
        if (Array.getLength(axes) != 3)
            throw new ArrayLengthException("axes array is not of length=3, (length="+Array.getLength(axes)+")");
        m_frame = (Matrix) frame.clone();
        System.arraycopy(axes, 0, m_axes, 0, 3);
    }

    /// @name Overridden methods
    /// Overridden methods inherited from Object.
///@{

    /** Converts the reference frame to a String representation of the
     * internal matrix storing the basis vectors.
     * @return A string representing this instance.
     */
    @Override
    public String toString() {
        return m_frame.toString();
    }

    /** Creates a clone of the reference frame.
     * @return A deep copy of this instance.
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        ReferenceFrame ref = (ReferenceFrame)super.clone();
        ref.m_frame = (Matrix)m_frame.clone();
        for (int i = 0; i < 3; i++) {
            ref.m_axes[i] = (Axis)m_axes[i].clone();
        }
        return ref;
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
        return m_frame.equals(that.m_frame) && Arrays.equals(m_axes, that.m_axes);
    }

    /** Creates a hashcode for the reference frame.
     * @return The hashcode for this instance.
     */
    @Override
    public int hashCode() {
        int result = Objects.hash(m_frame);
        result = 31 * result + Arrays.hashCode(m_axes);
        return result;
    }

///@}

    /// @name Accessor methods
    /// Accessor methods for the axes and vectors of the reference frame.
///@{

    /** Retrieves the axis object associated with the axis direction.
     * @param axis The axis direction to retrieve.
     * @return A copy of the axes.
     */
    public Axis axis(Axis.Direction axis) {
        return m_axes[axis.ordinal()];
    }

    /** Retrieves the vector associated with a basis vector.
     * @param axis The axis direction to retrieve.
     * @return A copy of the vector.
     */
    public Vector getAxisVector(Axis.Direction axis) {
        return m_frame.getColumn(axis.ordinal());
    }

    /** Sets the vector associated with a basis vector.
     * @param axis The axis direction to retrieve.
     * @param vec The vector to set the basis vector to.
     */
    public void setAxisVector(Axis.Direction axis, Vector vec) {
        for (int i = 0; i < 3; i++) {
            m_frame.set(axis.ordinal(), i, vec.get(i));
        }
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
