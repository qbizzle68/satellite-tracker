/** @file
 * This file contains a mathematical vector class. This vector class is much
 * different from the standard programming vector classes and should not be
 * used as such. This vector class represents a vector in the used in physics
 * or linear algebra. As these vectors are describing 3-dimensional physical
 * quantities, all instances are of length 3.
 */

package com.qbizzle.Math;

import java.util.Arrays;

/** A vector class to represent a physical quantity. All instances have a length
 * of three. The three components are referred to as x, y and z, which coincide
 * with the i, j and k unit vectors respectively.
 */
public class Vector implements Cloneable {
    private double[] m_data = new double[3];

    /** Default constructor, assigns 0.0 to all three components. */
    public Vector() {
        this(0.0, 0.0, 0.0);
    }

    /** Same as the default constructor except sets a custom x value.
     * @param x Value to initialize the x-component to.
     */
    public Vector(double x) {
        this(x, 0.0, 0.0);
    }

    /** Same as 1-parameter constructor except also sets a custom y value.
     * @param x Value to initialize the x-component to.
     * @param y Value to initialize the y-component to.
     */
    public Vector(double x, double y) {
        this(x, y, 0.0);
    }

    /** Initializes the vector components to their respective parameter values.
     * @param x Value to initialize the x-component to.
     * @param y Value to initialize the y-component to.
     * @param z Value to initialize the z-component to.
     */
    public Vector(double x, double y, double z) {
        m_data[0] = x;
        m_data[1] = y;
        m_data[2] = z;
    }

    /// @name Overloaded methods.
    /// Methods that are inherited from Java Cloneable class.
///@{

    /** Returns a string representation of the vector. Syntax is of the form '[x, y, z]'.
     * @return A string representation of the vector.
     */
    @Override
    public String toString() {
        return "Vector{" +
                "m_data=" + Arrays.toString(m_data) +
                '}';
    }

    /** Indicates whether a Vector is 'equal to' this instance.
     * @param o Vector to compare this Vector to.
     * @return True if this object is the same as @p ob argument, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return Arrays.equals(m_data, vector.m_data);
    }

    /** Generates a hash code based on the component values of this vector.
     * @return A hash code for this vector instance.
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(m_data);
    }

    /** Creates and returns a copy of this object.
     * A deep copy of the vector is made before returning.
     * @return A clone of this instance.
     * @warning This method returns a Java Object, so explicit casting may be necessary
     * to avoid errors.
     */
    @Override
    public Object clone() {
        try {
            Vector tmp = (Vector)super.clone();
            tmp.m_data = m_data.clone();
            return tmp;
        }
        catch (CloneNotSupportedException ex) {
//            this can't be reached, so just please the IDE
            throw new InternalError(ex.toString());
        }
    }

///@}

    /// @name Operators
    /// Methods used to operator on a Vector.
///@{

    /** Addition operator.
     * @param rhs Vector with which this instance will be added to.
     * @return A Vector equivalent to this + @p rhs.
     */
    public Vector plus(Vector rhs) {
        return new Vector(
                this.m_data[0] + rhs.m_data[0],
                this.m_data[1] + rhs.m_data[1],
                this.m_data[2] + rhs.m_data[2]
        );
    }

    /** Addition assignment operator.
     * @param rhs Vector to be added to this instance.
     * @return A clone of this Vector, which has been added to.
     */
    public Vector plusEquals(Vector rhs) {
        this.m_data[0] += rhs.m_data[0];
        this.m_data[1] += rhs.m_data[1];
        this.m_data[2] += rhs.m_data[2];
        return (Vector)this.clone();
    }

    /** Unary subtraction operator.
     * @return A Vector whose components are negated.
     */
    public Vector minus() {
        return new Vector(
                -m_data[0],
                -m_data[1],
                -m_data[2]
        );
    }

    /** Subtraction operator.
     * @param rhs Vector with which this instance will be subtracted to.
     * @return A Vector equivalent to this - @p rhs.
     */
    public Vector minus(Vector rhs) {
        return new Vector(
                this.m_data[0] - rhs.m_data[0],
                this.m_data[1] - rhs.m_data[1],
                this.m_data[2] - rhs.m_data[2]
        );
    }

    /** Subtraction assignment operator.
     * @param rhs Vector to be subtracted from this instance.
     * @return A clone of this Vector, which has been subtracted.
     */
    public Vector minusEquals(Vector rhs) {
        this.m_data[0] -= rhs.m_data[0];
        this.m_data[1] -= rhs.m_data[1];
        this.m_data[2] -= rhs.m_data[2];
        return (Vector)this.clone();
    }

    /** Scalar multiplication.
     * @param lambda Scalar to multiply this Vector by.
     * @return A Vector equal to @p lambda * this.
     */
    public Vector scale(double lambda) {
        return new Vector(
                this.m_data[0] * lambda,
                this.m_data[1] * lambda,
                this.m_data[2] * lambda
        );
    }

    /** Scalar multiplication assignment.
     * @param lambda Scalar to multiply this Vector by.
     * @return A clone of this Vector, which has been scaled.
     */
    public Vector scaleEquals(double lambda) {
        this.m_data[0] *= lambda;
        this.m_data[1] *= lambda;
        this.m_data[2] *= lambda;
        return (Vector)this.clone();
    }

    /** Sets this Vector without needing to set individual components.
     * @param rhs Vector to set this Vector to.
     * @return A clone of this Vector after it has been assigned.
     */
    public Vector set(Vector rhs) {
        m_data[0] = rhs.m_data[0];
        m_data[1] = rhs.m_data[1];
        m_data[2] = rhs.m_data[2];
        return (Vector)this.clone();
    }

///@}

    /// @name Vector Operators.
    /// Operators specific to Vectors.
///@{

    /** The dot product of two Vectors. Also referred to as an inner
     * product. Equal to the sum of the product of the components.
     * @param rhs The Vector to dot this Vector with.
     * @return The dot product of this and @p rhs.
     */
    public double dot(Vector rhs) {
        double sum = 0;
        for (int i = 0; i < 3; i++)
            sum += this.m_data[i] * rhs.m_data[i];
        return sum;
    }

    /** The cross product of two Vectors. The Vector returned is
     * created using the right-hand rule>
     * @param rhs The Vector to cross with this instance.
     * @return A Vector equal to this x @p rhs using the right-hand rule.
     */
    public Vector cross(Vector rhs) {
        Vector rtn = new Vector();
        rtn.m_data[0] = this.m_data[1] * rhs.m_data[2]
                - this.m_data[2] * rhs.m_data[1];
        rtn.m_data[1] = this.m_data[2] * rhs.m_data[0]
                - this.m_data[0] * rhs.m_data[2];
        rtn.m_data[2] = this.m_data[0] * rhs.m_data[1]
                - this.m_data[1] * rhs.m_data[0];
        return rtn;
    }

    /** Computes the magnitude of this Vector.
     * @return The magnitude of this vector, the square of the sum of the squares of each component.
     */
    public double mag() {
        return Math.sqrt(
                m_data[0] * m_data[0] +
                        m_data[1] * m_data[1] +
                        m_data[2] * m_data[2]
        );
    }

    /** Normalizes this Vector.
     * @return A Vector with the same direction as this but with magnitude equal to 1.
     */
    public Vector norm() {
        Vector rtn = (Vector)this.clone();
        double mag = this.mag();
        rtn.m_data[0] /= mag;
        rtn.m_data[1] /= mag;
        rtn.m_data[2] /= mag;
        return rtn;
    }

///@}

    /// @name Getter/setter methods.
    /// Methods to retrieve or set the component values of a Vector.
///@{

    /** Gets the x-component of the Vector.
     * @return Vector's x-component.
     */
    public double x() {
        return m_data[0];
    }

    /** Gets the y-component of the Vector.
     * @return Vector's y-component.
     */
    public double y() {
        return m_data[1];
    }

    /** Gets the z-component of the Vector.
     * @return Vector's z-component.
     */
    public double z() {
        return m_data[2];
    }

    /** Gets the ith-component of the Vector.
     * @param i Index of the vector you wish to retrieve.
     * @return Ith value of the Vector.
     */
    public double get(int i) {
        return m_data[i];
    }

    /** Sets the ith-component of the Vector
     * @param i Index of the vector you wish to set.
     * @param value Value to set the component to.
     */
    public void set(int i, double value) {
        m_data[i] = value;
    }

///@}

}