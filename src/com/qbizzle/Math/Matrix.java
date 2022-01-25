/** @file
 * This file contains the Matrix class associated with the Vector class.
 * The matrix should be able to function for any reason we wish, but the
 * intended use will be to operate on Vectors, mostly exclusively for rotations.
 */

package com.qbizzle.Math;

/** Matrix class used in conjunction with the Vector class to handle linear
 * algebra computations.
 */
public class Matrix implements Cloneable {
    double[][] m_data = new double[3][3];

    /** Default constructor, that sets each matrix component to 0.0. */
    public Matrix() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                m_data[i][j] = 0.0;
            }
        }
    }

    /** Column Vector constructor that sets each column of the matrix to its
     * relative column vector in the argument list. This constructor is ideal
     * for creating a rotation matrix where you know where you want the i, j
     * and k unit vectors to map to.
     * @param column1 Column Vector that the i unit vector maps to.
     * @param column2 Column Vector that the j unit vector maps to.
     * @param column3 Column Vector that the k unit vector maps to.
     */
    public Matrix(Vector column1, Vector column2, Vector column3) {
        m_data[0][0] = column1.x(); m_data[1][0] = column1.y(); m_data[2][0] = column1.z();
        m_data[0][1] = column2.x(); m_data[1][1] = column2.y(); m_data[2][1] = column2.z();
        m_data[0][2] = column3.x(); m_data[1][2] = column3.y(); m_data[2][2] = column3.z();
    }

    /// @name Overloaded methods.
    /// Methods that are inherited from Java Cloneable class.
///@{

    /** Returns a string representation of the matrix.
     * @return A string representation of the matrix.
     */
    public String toString() {
        return "[ " + m_data[0][0] + ", " + m_data[0][1] + ", " + m_data[0][2] + "\n  "
                + m_data[1][0] + ", " + m_data[1][1] + ", " + m_data[1][2] + "\n  "
                + m_data[2][0] + ", " + m_data[2][1] + ", " + m_data[2][2] + " ]";
    }

    /** Indicates whether a Matrix is 'equal to' this instance.
     * @param obj Matrix to compare this matrix to.
     * @return True if this object is the same as @p obj argument, false otherwise.
     * @throws IllegalArgumentException if @p obj argument is not of type Matrix.
     */
    public boolean equals(Object obj) {
        if (obj instanceof Matrix rhs) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (m_data[i][j] != rhs.m_data[i][j]) return false;
                }
            }
            return true;
        }
        throw new IllegalArgumentException("Object is not of type 'Matrix'");
    }

    /** Creates and returns a copy of this object.
     * A deep copy of the matrix is made before returning.
     * @return A clone of this instance.
     * @warning This method returns a Java Object, so explicit casting may be necessary
     * to avoid errors.
     */
    public Object clone() {
        try {
            Matrix tmp = (Matrix)super.clone();
            for (int i = 0; i < 3; i++) {
                tmp.m_data[i] = m_data[i].clone();
            }
            return tmp;
        } catch (CloneNotSupportedException ex) {
//            this can't be reached, just to please IDE
            throw new InternalError(ex.toString());
        }
    }

///@}

    /** Retrieves a certain component of the matrix by index (starting at 0).
     * @param row Row index of desired element.
     * @param col Column index of desired element.
     * @return The value at the indexed position.
     * @throws ArrayIndexOutOfBoundsException if either of the indexes are out
     * of range (indexing starts at 0).
     */
    public double get(int row, int col) {
        return m_data[row][col];
    }

    /** Sets a certain component of the matrix by index (starting at 0).
     * @param row Row index of element to modify.
     * @param col Column index of element to modify.
     * @param value Value to set the desired component to.
     * @throws ArrayIndexOutOfBoundsException if either of the indexes are out
     * of range (indexing starts at 0).
     */
    public void set(int row, int col, double value) {
        m_data[row][col] = value;
    }

    /** Retrieves a certain column of the matrix by index (starting at 0).
     * @param col Column index of desired column vector.
     * @return The desired column vector of the matrix.
     * @throws ArrayIndexOutOfBoundsException if column index is out of range
     * (indexing starts at 0).
     */
    @SuppressWarnings("UnusedReturnValue")
    public Vector getColumn(int col) {
        return new Vector(m_data[0][col], m_data[1][col], m_data[2][col]);
    }

    /** Matrix addition operator.
     * @param rhs Matrix to be added to this matrix.
     * @return The Matrix sum of this instance and @p rhs argument.
     */
    public Matrix plus(Matrix rhs) {
        Matrix rtn = new Matrix();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                rtn.m_data[i][j] = m_data[i][j] + rhs.m_data[i][j];
            }
        }
        return rtn;
    }

    /** Matrix addition assignment operator.
     * @param rhs Matrix to be added to this matrix.
     * @return A clone of this instance after the addition.
     */
    public Matrix plusEquals(Matrix rhs) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                m_data[i][j] += rhs.m_data[i][j];
            }
        }
        return (Matrix)this.clone();
    }

    /** Unary subtraction operator.
     * @return A matrix with negated component values of this instance.
     */
    public Matrix minus() {
        Matrix rtn = new Matrix();
        for (int i = 0; i < 3; i++) {
            System.arraycopy(this.m_data[i], 0, rtn.m_data[i], 0, 3);
        }
        return rtn;
    }

    /** Matrix subtraction operator.
     * @param rhs Matrix to subtract from this instance.
     * @return A Matrix equal to this instance minus @p rhs.
     */
    public Matrix minus(Matrix rhs) {
        Matrix rtn = new Matrix();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                rtn.m_data[i][j] = m_data[i][j] - rhs.m_data[i][j];
            }
        }
        return rtn;
    }

    /** Matrix subtraction assignment operator.
     * @param rhs Matrix to subtract from this instance.
     * @return A clone of this instance after it has been subtracted.
     */
    public Matrix minusEquals(Matrix rhs) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                m_data[i][j] -= rhs.m_data[i][j];
            }
        }
        return (Matrix)this.clone();
    }

    /** Scalar multiplication operator.
     * @param lambda Scalar to multiply each matrix component by.
     * @return A Matrix with all components multiplies by @p lambda.
     */
    public Matrix scale(double lambda) {
        Matrix rtn = new Matrix();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                rtn.m_data[i][j] = m_data[i][j] * lambda;
            }
        }
        return rtn;
    }

    /** Scalar multiplication assignment operator.
     * @param lambda Scalar to multiply each matrix component by.
     * @return A clone of this instance after it has been multiplied.
     */
    public Matrix scaleEquals(double lambda) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                m_data[i][j] *= lambda;
            }
        }
        return (Matrix)this.clone();
    }

    /** Vector multiplication operator. Vector multiplication is right-handed
     * so that the matrix columns are where the ijk unit vectors map to, instead
     * of rows. This is to increase 'readability' of the matrix, and is also
     * the rationale behind why the @link com.qbizzle.Math.Matrix#getColumn
     * getColumn() @endlink is not a @em getRow() method.
     * @param rhs Vector to multiply matrix by. This is the vector that is to be operated
     *            on by the Matrix, that is to say the vector that will be rotated by this
     *            matrix.
     * @return The vector after being rotated by this matrix.
     */
    public Vector mult(Vector rhs) {
        Vector rtn = new Vector();
        for (int i = 0; i < 3; i++) {
            double sum = 0.0;
            for (int j = 0; j < 3; j++) {
                sum += m_data[i][j] * rhs.get(j);
            }
            rtn.set(i, sum);
        }
        return rtn;
    }

    /** Matrix multiplication operator. Used to compound multiple rotation matrices.
     * @param rhs Matrix to compound this rotation by.
     * @return A single matrix composed of the two rotations.
     */
    public Matrix mult(Matrix rhs) {
        Matrix rtn = new Matrix();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                double sum = 0.0;
                for (int k = 0; k < 3; k++) {
                    sum += m_data[i][k] * rhs.m_data[k][j];
                }
                rtn.m_data[i][j] = sum;
            }
        }
        return rtn;
    }

    /** Matrix multiplication assignment operator.
     * @param rhs Matrix to compound this rotation by.
     * @return A clone of this matrix after it has been multiplied.
     */
    @SuppressWarnings("UnusedReturnValue")
    public Matrix multEquals(Matrix rhs) {
        Matrix tmp = this.mult(rhs);
        for (int i = 0; i < 3; i++) {
            System.arraycopy(tmp.m_data[i], 0, m_data[i], 0, 3);
        }
        return (Matrix)this.clone();
    }

}