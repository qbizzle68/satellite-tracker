package com.qbizzle.rotation;

import com.qbizzle.exception.InvalidEulerRotationLengthException;
import com.qbizzle.exception.InvalidAxisException;
import com.qbizzle.math.Matrix;
import com.qbizzle.referenceframe.Axis;

import java.util.HashMap;
import java.util.Map;

public class Rotation {

    private static Map<String, Axis.Direction> axisMap = new HashMap<String, Axis.Direction>() {{
        put("x", Axis.Direction.X); put("y", Axis.Direction.Y); put("z", Axis.Direction.Z);
    }};

    // angle in degrees
    public static Matrix getMatrix(Axis.Direction axis, double angle) {
        return switch (axis.ordinal()) {
            case 1 -> yRotationMatrix(Math.toRadians(angle));
            case 2 -> zRotationMatrix(Math.toRadians(angle));
            default -> xRotationMatrix(Math.toRadians(angle));
        };
    }


    // intrinsic rotation
    public static Matrix getEulerMatrix(String axisOrder, double alpha, double beta, double gamma)
            throws InvalidEulerRotationLengthException, InvalidAxisException {
        if (axisOrder.length() != 3)
            throw new InvalidEulerRotationLengthException("Invalid number of Euler rotation axes, which is " + axisOrder.length());
        axisOrder = axisOrder.toLowerCase();
        for (int i = 0; i < 3; i++) {
            char chrAt = axisOrder.charAt(i);
            if (chrAt != 'x' && chrAt != 'y' && chrAt != 'z')
                throw new InvalidAxisException("Invalid Euler rotation axis="+i+" which is " + axisOrder.charAt(i));
        }

        return getMatrix(axisMap.get(axisOrder.substring(0, 1)), alpha)
                .mult( getMatrix(axisMap.get(axisOrder.substring(1, 2)), beta)
                        .mult( getMatrix(axisMap.get(axisOrder.substring(2, 3)), gamma) ) );
    }

    // alpha in radians
    private static Matrix xRotationMatrix(double alpha) {
        double cosAlpha = Math.cos( alpha );
        double sinAlpha = Math.sin( alpha );
        Matrix rtn = new Matrix();
        rtn.set(1, 1, cosAlpha);
        rtn.set(2, 2, cosAlpha);
        rtn.set(1, 2, -sinAlpha);
        rtn.set(2, 1, sinAlpha);
        rtn.set(0, 0, 1);
        return rtn;
    }

    // beta in radians
    private static Matrix yRotationMatrix(double beta) {
        double cosBeta = Math.cos( beta );
        double sinBeta = Math.sin( beta );
        Matrix rtn = new Matrix();
        rtn.set(0, 0, cosBeta);
        rtn.set(2, 2, cosBeta);
        rtn.set(2, 0, -sinBeta);
        rtn.set(0, 2, sinBeta);
        rtn.set(1, 1, 1);
        return rtn;
    }

    // gamma in radians
    private static Matrix zRotationMatrix(double gamma) {
        double cosGamma = Math.cos( gamma );
        double sinGamma = Math.sin( gamma );
        Matrix rtn = new Matrix();
        rtn.set(1, 1, cosGamma);
        rtn.set(0, 0, cosGamma);
        rtn.set(0, 1, -sinGamma);
        rtn.set(1, 0, sinGamma);
        rtn.set(2, 2, 1);
        return rtn;
    }

}