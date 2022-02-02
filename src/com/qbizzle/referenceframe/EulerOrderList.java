package com.qbizzle.referenceframe;

public class EulerOrderList {
    public static final EulerOrder XYZ = new EulerOrder(Axis.Direction.X, Axis.Direction.Y, Axis.Direction.Z);
    public static final EulerOrder XZY = new EulerOrder(Axis.Direction.X, Axis.Direction.Z, Axis.Direction.Y);
    public static final EulerOrder YXZ = new EulerOrder(Axis.Direction.Y, Axis.Direction.X, Axis.Direction.Z);
    public static final EulerOrder YZX = new EulerOrder(Axis.Direction.Y, Axis.Direction.Z, Axis.Direction.X);
    public static final EulerOrder ZXY = new EulerOrder(Axis.Direction.Z, Axis.Direction.X, Axis.Direction.Y);
    public static final EulerOrder ZYX = new EulerOrder(Axis.Direction.Z, Axis.Direction.Y, Axis.Direction.X);
    public static final EulerOrder XYX = new EulerOrder(Axis.Direction.X, Axis.Direction.Y, Axis.Direction.X);
    public static final EulerOrder XZX = new EulerOrder(Axis.Direction.X, Axis.Direction.Z, Axis.Direction.X);
    public static final EulerOrder YXY = new EulerOrder(Axis.Direction.Y, Axis.Direction.X, Axis.Direction.Y);
    public static final EulerOrder YZY = new EulerOrder(Axis.Direction.Y, Axis.Direction.Z, Axis.Direction.Y);
    public static final EulerOrder ZXZ = new EulerOrder(Axis.Direction.Z, Axis.Direction.X, Axis.Direction.Z);
    public static final EulerOrder ZYZ = new EulerOrder(Axis.Direction.Z, Axis.Direction.Y, Axis.Direction.Z);

}
