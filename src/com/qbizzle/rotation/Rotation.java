package com.qbizzle.rotation;

import com.qbizzle.exception.InvalidAxisException;
import com.qbizzle.exception.InvalidEulerRotationLengthException;
import com.qbizzle.math.Matrix;
import com.qbizzle.math.Vector;
import com.qbizzle.referenceframe.Axis;
import com.qbizzle.referenceframe.ReferenceFrame;

import java.util.HashMap;
import java.util.Map;

import static com.qbizzle.math.util.I3;

//@todo create an euler angles class as a container for all three euler angles (to be more explicit than a double array).
public class Rotation {

    private final static Map<String, Axis.Direction> axisMap = new HashMap<>() {{
        put("x", Axis.Direction.X); put("y", Axis.Direction.Y); put("z", Axis.Direction.Z);
    }};

    // angle in degrees
    // single axis extrinsic
    public static Matrix getMatrix(Axis.Direction axis, double angle) {
        return switch (axis.ordinal()) {
            case 1 -> yRotationMatrix(Math.toRadians(angle));
            case 2 -> zRotationMatrix(Math.toRadians(angle));
            default -> xRotationMatrix(Math.toRadians(angle));
        };
    }
    // single axis intrinsic
    public static Matrix getMatrixIntrinsic(Vector vector, double angle) {
        Vector normVector = vector.norm();
        Matrix w = new Matrix();
        w.set(0, 1, -normVector.z());
        w.set(0, 2, normVector.y());
        w.set(1, 0, normVector.z());
        w.set(1, 2, -normVector.x());
        w.set(2, 0, -normVector.y());
        w.set(2, 1, normVector.x());
        double sinangle = Math.sin( Math.toRadians(angle) );
        return I3.plus(w.scale(sinangle).plus(w.mult(w).scale(2 * Math.pow(sinangle/2.0, 2))));
    }

    // euler intrinsic rotation
    public static Matrix getEulerMatrix(String axisOrder, EulerAngles angles) {
//    public static Matrix getEulerMatrix(String axisOrder, double alpha, double beta, double gamma) {
//        if (axisOrder.length() != 3)
//            throw new InvalidEulerRotationLengthException("Invalid number of Euler rotation axes, which is " + axisOrder.length());
//        axisOrder = axisOrder.toLowerCase();
//        for (int i = 0; i < 3; i++) {
//            char chrAt = axisOrder.charAt(i);
//            if (chrAt != 'x' && chrAt != 'y' && chrAt != 'z')
//                throw new InvalidAxisException("Invalid Euler rotation axis="+i+" which is " + axisOrder.charAt(i));
//        }
        String axisOrderLower = axisOrder.toLowerCase();
        checkEulerString(axisOrderLower);

        return getMatrix(axisMap.get(axisOrderLower.substring(0, 1)), angles.get(0))
                .mult( getMatrix(axisMap.get(axisOrderLower.substring(1, 2)), angles.get(1))
                        .mult( getMatrix(axisMap.get(axisOrderLower.substring(2, 3)), angles.get(2)) ) );
//        return getMatrix(axisMap.get(axisOrder.substring(0, 1)), alpha)
//                .mult( getMatrix(axisMap.get(axisOrder.substring(1, 2)), beta)
//                        .mult( getMatrix(axisMap.get(axisOrder.substring(2, 3)), gamma) ) );
    }
    //euler extrinsic rotation
    public static Matrix getEulerMatrixExtrinsic(String axisOrder, EulerAngles angles) {
        return getEulerMatrix(
                axisOrder.substring(2, 3) + axisOrder.charAt(1) + axisOrder.charAt(0),
                new EulerAngles(angles.get(2), angles.get(1), angles.get(0))
        );
    }

    // single axis rotation
    public static Vector Rotate(Matrix rotMatrix, Vector vector) {
        return rotMatrix.mult(vector);
    }
    public static Vector Rotate(Axis.Direction axis, double angle, Vector vector) {
        return Rotate(getMatrix(axis, angle), vector);
    }
    public static Vector RotateIntrinsic(Vector vector, double angle) {
        return Rotate(getMatrixIntrinsic(vector, angle), vector);
    }

    // euler rotations
    public static Vector Rotate(String axisOrder, EulerAngles angles, Vector vector) {
        return Rotate(getEulerMatrix(axisOrder, angles), vector);
    }
    public static Vector RotateExtrinsic(String axisOrder, EulerAngles angles, Vector vector) {
        return Rotate(getEulerMatrixExtrinsic(axisOrder, angles), vector);
    }

    // struggling on this one hard. will come back to it later
//    @todo not sure about the logic here, need to double check
    public static Vector Rotate(ReferenceFrame from, ReferenceFrame to, Vector vector) {
        Matrix rotation = to.toMatrix().mult(from.toMatrix().transpose());
        return rotation.mult(vector);
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

    private static void checkEulerString(String axisOrder) {
        if (axisOrder.length() != 3)
            throw new InvalidEulerRotationLengthException("Invalid number of Euler rotation axes, which is " + axisOrder.length());
//        axisOrder = axisOrder.toLowerCase();
        for (int i = 0; i < 3; i++) {
            char chrAt = axisOrder.charAt(i);
            if (chrAt != 'x' && chrAt != 'y' && chrAt != 'z')
                throw new InvalidAxisException("Invalid Euler rotation axis="+i+" which is " + axisOrder.charAt(i));
        }
    }

}