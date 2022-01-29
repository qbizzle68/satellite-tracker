package com.qbizzle.math;

public class util {
    public final static Vector e1 = new Vector(1.0, 0.0, 0.0);
    public final static Vector e2 = new Vector(0.0, 1.0, 0.0);
    public final static Vector e3 = new Vector(0.0, 0.0, 1.0);

    public final static Matrix I3 = new Matrix(e1, e2, e3);

    public final static JD J2000 = new JD(1, 1, 2000, 12, 0, 0.0);
}
