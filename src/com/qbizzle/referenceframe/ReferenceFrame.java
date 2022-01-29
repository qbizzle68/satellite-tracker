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

    /** Constructs a reference frame
     */
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

    public ReferenceFrame(Vector[] colVectors) throws ArrayLengthException {
        this(colVectors, new Axis[]{new Axis(Axis.Direction.X), new Axis(Axis.Direction.X), new Axis(Axis.Direction.X)});
    }

    public ReferenceFrame(Vector[] colVectors, Axis[] axes) throws ArrayLengthException {
        if (Array.getLength(colVectors) != 3)
            throw new ArrayLengthException("colVectors array is not of length=3, (length="+Array.getLength(colVectors)+")");
        if (Array.getLength(axes) != 3)
            throw new ArrayLengthException("axes array is not of length=3, (length="+Array.getLength(axes)+")");
        m_frame = new Matrix(colVectors[0], colVectors[1], colVectors[2]);
        System.arraycopy(axes, 0, m_axes, 0, 3);
    }

    public ReferenceFrame(Matrix frame) throws ArrayLengthException {
        this(frame, new Axis[]{new Axis(Axis.Direction.X), new Axis(Axis.Direction.X), new Axis(Axis.Direction.X)});
    }

    public ReferenceFrame(Matrix frame, Axis[] axes) throws ArrayLengthException {
        if (Array.getLength(axes) != 3)
            throw new ArrayLengthException("axes array is not of length=3, (length="+Array.getLength(axes)+")");
        m_frame = (Matrix) frame.clone();
        System.arraycopy(axes, 0, m_axes, 0, 3);
    }

    @Override
    public String toString() {
        return m_frame.toString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        ReferenceFrame ref = (ReferenceFrame)super.clone();
        ref.m_frame = (Matrix)m_frame.clone();
        for (int i = 0; i < 3; i++) {
            ref.m_axes[i] = (Axis)m_axes[i].clone();
        }
        return ref;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReferenceFrame that = (ReferenceFrame) o;
        return m_frame.equals(that.m_frame) && Arrays.equals(m_axes, that.m_axes);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(m_frame);
        result = 31 * result + Arrays.hashCode(m_axes);
        return result;
    }

    public Axis axis(Axis.Direction axis) {
        return m_axes[axis.ordinal()];
    }

    public Vector axisVector(Axis.Direction axis) {
        return m_frame.getColumn(axis.ordinal());
    }

    public Matrix toMatrix() {
        return (Matrix)m_frame.clone();
    }

    public void setAxisVector(Axis.Direction axis, Vector vec) {
        for (int i = 0; i < 3; i++) {
            m_frame.set(axis.ordinal(), i, vec.get(i));
        }
    }

}
