package com.qbizzle.referenceframe;

public class EulerOrder {
    public static final EulerOrderTemplate XYZ = new EulerOrderTemplate(Axis.Direction.X, Axis.Direction.Y, Axis.Direction.Z);
    public static final EulerOrderTemplate XZY = new EulerOrderTemplate(Axis.Direction.X, Axis.Direction.Z, Axis.Direction.Y);
    public static final EulerOrderTemplate YXZ = new EulerOrderTemplate(Axis.Direction.Y, Axis.Direction.X, Axis.Direction.Z);
    public static final EulerOrderTemplate YZX = new EulerOrderTemplate(Axis.Direction.Y, Axis.Direction.Z, Axis.Direction.X);
    public static final EulerOrderTemplate ZXY = new EulerOrderTemplate(Axis.Direction.Z, Axis.Direction.X, Axis.Direction.Y);
    public static final EulerOrderTemplate ZYX = new EulerOrderTemplate(Axis.Direction.Z, Axis.Direction.Y, Axis.Direction.X);
    public static final EulerOrderTemplate XYX = new EulerOrderTemplate(Axis.Direction.X, Axis.Direction.Y, Axis.Direction.X);
    public static final EulerOrderTemplate XZX = new EulerOrderTemplate(Axis.Direction.X, Axis.Direction.Z, Axis.Direction.X);
    public static final EulerOrderTemplate YXY = new EulerOrderTemplate(Axis.Direction.Y, Axis.Direction.X, Axis.Direction.Y);
    public static final EulerOrderTemplate YZY = new EulerOrderTemplate(Axis.Direction.Y, Axis.Direction.Z, Axis.Direction.Y);
    public static final EulerOrderTemplate ZXZ = new EulerOrderTemplate(Axis.Direction.Z, Axis.Direction.X, Axis.Direction.Z);
    public static final EulerOrderTemplate ZYZ = new EulerOrderTemplate(Axis.Direction.Z, Axis.Direction.Y, Axis.Direction.Z);

}
