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

    public Matrix() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                m_data[i][j] = 0.0;
            }
        }
    }

    public Matrix(Vector column1, Vector column2, Vector column3) {
        m_data[0][0] = column1.x(); m_data[0][1] = column1.y(); m_data[0][2] = column1.z();
        m_data[1][0] = column2.x(); m_data[1][1] = column2.y(); m_data[1][2] = column2.z();
        m_data[2][0] = column3.x(); m_data[2][1] = column3.y(); m_data[2][2] = column3.z();
    }

    public String toString() {
        return "[ " + m_data[0][0] + ", " + m_data[0][1] + ", " + m_data[0][2] + "\n  "
                + m_data[1][0] + ", " + m_data[1][1] + ", " + m_data[1][2] + "\n  "
                + m_data[2][0] + ", " + m_data[2][1] + ", " + m_data[2][2] + " ]";
    }

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

    public double get(int row, int col) {
        return m_data[row][col];
    }

    public void set(int row, int col, double value) {
        m_data[row][col] = value;
    }

    public Vector getColumn(int col) {
        return new Vector(m_data[0][col], m_data[1][col], m_data[2][col]);
    }

    public Matrix plus(Matrix rhs) {
        Matrix rtn = new Matrix();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                rtn.m_data[i][j] = m_data[i][j] + rhs.m_data[i][j];
            }
        }
        return rtn;
    }

    public Matrix pluseEquals(Matrix rhs) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                m_data[i][j] += rhs.m_data[i][j];
            }
        }
        return (Matrix)this.clone();
    }

    public Matrix minus(Matrix rhs) {
        Matrix rtn = new Matrix();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                rtn.m_data[i][j] = m_data[i][j] - rhs.m_data[i][j];
            }
        }
        return rtn;
    }

    public Matrix minusEquals(Matrix rhs) {
        Matrix rtn = new Matrix();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                m_data[i][j] -= rhs.m_data[i][j];
            }
        }
        return (Matrix)this.clone();
    }

    public Matrix scale(double lambda) {
        Matrix rtn = new Matrix();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                rtn.m_data[i][j] = m_data[i][j] * lambda;
            }
        }
        return rtn;
    }

    public Matrix scaleEquals(double lambda) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                m_data[i][j] *= lambda;
            }
        }
        return (Matrix)this.clone();
    }

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

    public Matrix multEquals(Matrix rhs) {
        Matrix tmp = this.mult(rhs);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                m_data[i][j] = tmp.m_data[i][j];
            }
        }
        return (Matrix)this.clone();
    }

}
