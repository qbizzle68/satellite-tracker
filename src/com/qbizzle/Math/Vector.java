package com.qbizzle.Math;

import java.util.function.BiFunction;

public class Vector implements Cloneable {
//public class Vector implements Cloneable {
    private double[] m_data = new double[3];

    // constructors
    public Vector() {
        this(0.0, 0.0, 0.0);
    }
    public Vector(double x) {
        this(x, 0.0, 0.0);
    }
    public Vector(double x, double y) {
        this(x, y, 0.0);
    }

//    public Vector(double x, double y, double z) {
    public Vector(double x, double y, double z) {
        m_data[0] = x;
        m_data[1] = y;
        m_data[2] = z;
    }

    public String toString() {
        return "[" + m_data[0] + ", " + m_data[1] + ", " + m_data[2] + "]";
    }

    public boolean equals(Object ob) {
        if (ob instanceof Vector) {
            Vector rhs = (Vector) ob;
            return (this.m_data[0] == rhs.m_data[0]
                    && this.m_data[1] == rhs.m_data[1]
                    && this.m_data[2] == rhs.m_data[2]);
        }
        throw new IllegalArgumentException("Object not of type 'Vector'");
    }

    public Object clone() {
        try {
            Vector tmp = (Vector)super.clone();
            tmp.m_data = m_data.clone();
            return tmp;
        }
        catch (CloneNotSupportedException ex) {
            throw new InternalError(ex.toString());
        }
    }

    // returns a new vector equal to 'this' + 'rhs'.
    public Vector plus(Vector rhs) {
        return new Vector(
                this.m_data[0] + rhs.m_data[0],
                this.m_data[1] + rhs.m_data[1],
                this.m_data[2] + rhs.m_data[2]
        );
    }

    // synonymous to unary '-' operator.
    public Vector minus() {
        return new Vector(
                -m_data[0],
                -m_data[1],
                -m_data[2]
        );
    }

    // returns a vector equal to 'this' - 'rhs'.
    public Vector minus(Vector rhs) {
        return new Vector(
                this.m_data[0] - rhs.m_data[0],
                this.m_data[1] - rhs.m_data[1],
                this.m_data[2] - rhs.m_data[2]
        );
    }

    // returns a vector scaled by lambda.
    public Vector scale(double lambda) {
        return new Vector(
                this.m_data[0] * lambda,
                this.m_data[1] * lambda,
                this.m_data[2] * lambda
        );
    }

    // sets the current values of the vector equal to rhs, then returns a cloned vector of 'this'.
    public Vector set(Vector rhs) {
        m_data[0] = rhs.m_data[0];
        m_data[1] = rhs.m_data[1];
        m_data[2] = rhs.m_data[2];
        return (Vector)this.clone();
    }

    // returns the dot product of 'this' and 'rhs'.
    public double dot(Vector rhs) {
        double sum = 0;
        for (int i = 0; i < 3; i++)
            sum += this.m_data[i] * rhs.m_data[i];
        return sum;
    }

    // returns the cross product of 'this' and 'rhs'.
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

    // returns the magnitude of the vector.
    public double mag() {
        return Math.sqrt(
                m_data[0] * m_data[0] +
                m_data[1] * m_data[1] +
                m_data[2] * m_data[2]
        );
    }

    // returns a normalized version of the vector.
    public Vector norm() {
        Vector rtn = (Vector)this.clone();
        double mag = this.mag();
        rtn.m_data[0] /= mag;
        rtn.m_data[1] /= mag;
        rtn.m_data[2] /= mag;
        return rtn;
    }

    // returns either this first, second or third value of the vector.
    public double x() {
        return m_data[0];
    }
    public double y() {
        return m_data[1];
    }
    public double z() {
        return m_data[2];
    }
    // sets a given part of vector
    public void setX(double x) {
        m_data[0] = x;
    }
    public void setY(double y) {
        m_data[1] = y;
    }
    public void setZ(double z) {
        m_data[2] = z;
    }
}