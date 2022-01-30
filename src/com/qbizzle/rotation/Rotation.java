/** @file
 * Contains a static class used for rotating vectors, matrices,
 * reference frames and other rotations.
 */

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

/** Rotation class with all static methods used to create matrices for
 * rotating other objects. The class also contains methods for doing
 * the actual rotation, in an effort to keep future code cleaner.
 * Rotations can be single axis or in groups of three (Euler or Tait-Brian
 * rotations), and they can be either intrinsic or extrinsic. Single axis
 * rotations are considered extrinsic by default, and Euler rotations are
 * considered intrinsic by default, but methods for the other types can be
 * found as well.
 * Angles for all rotations use the right-hand rule for the positive direction.
 * @todo create Rotate() methods to rotate rotations(matrices) intrinsically or extrinsically.
 * @todo create Rotate() methods to rotate reference frames intrinsically or extrinsically.
 */
public class Rotation {

//    map to correlate a character to an Axis.Direction
    private final static Map<String, Axis.Direction> axisMap = new HashMap<>() {{
        put("x", Axis.Direction.X); put("y", Axis.Direction.Y); put("z", Axis.Direction.Z);
    }};

    /// @name Single axis matrices
    /// Methods that create a rotation matrix from a single axis rotation.
///@{

    /** Creates a rotation matrix for an extrinsic rotation about a given axis.
     * @param axis The axis in which to rotate around extrinsically.
     * @param angle The angle to rotate in @em degrees.
     * @return A rotation matrix.
     */
    public static Matrix getMatrix(Axis.Direction axis, double angle) {
        return switch (axis.ordinal()) {
            case 1 -> yRotationMatrix(Math.toRadians(angle));
            case 2 -> zRotationMatrix(Math.toRadians(angle));
            default -> xRotationMatrix(Math.toRadians(angle));
        };
    }

    /** Creates a rotation matrix for an intrinsic rotation about a given axis.
     * @param vector A vector representing the axis in which to rotate around intrinsically.
     * @param angle The angle to rotate in @em degrees.
     * @return A rotation matrix.
     */
    public static Matrix getMatrixIntrinsic(Vector vector, double angle) {
        Vector normVector = vector.norm();
        Matrix w = new Matrix();
        w.set(0, 1, -normVector.z());
        w.set(0, 2, normVector.y());
        w.set(1, 0, normVector.z());
        w.set(1, 2, -normVector.x());
        w.set(2, 0, -normVector.y());
        w.set(2, 1, normVector.x());
        Matrix mat1 = I3.plus( w.scale( Math.sin( Math.toRadians(angle) ) ) );
        System.out.println(mat1);
        Matrix mat2 = w.mult(w).scale(2 * Math.pow( Math.sin( Math.toRadians(angle)/2.0 ), 2 ) );
        System.out.println(mat2);
        return mat1.plus(mat2);
    }

///@}

    /// @name Euler rotation matrices
    /// Methods that create a rotation matrix from Euler rotations.
///@{

    /** Creates a rotation matrix for an intrinsic Euler rotation about a series
     * of three axes.
     * @param axisOrder A string representing the order in which to rotate intrinsically. The
     *                  string must be exactly @em three characters long and contain only the
     *                  characters 'x', 'y' and 'z'.
     * @param angles The angles to rotate, in their rotation order, in @em degrees.
     * @return A rotation matrix.
     */
    public static Matrix getEulerMatrix(String axisOrder, EulerAngles angles) {
    String axisOrderLower = axisOrder.toLowerCase();
    checkEulerString(axisOrderLower);

    return getMatrix(axisMap.get(axisOrderLower.substring(0, 1)), angles.get(0))
            .mult( getMatrix(axisMap.get(axisOrderLower.substring(1, 2)), angles.get(1))
                    .mult( getMatrix(axisMap.get(axisOrderLower.substring(2, 3)), angles.get(2)) ) );
    }

    /** Creates a rotation matrix for an extrinsic Euler rotation about a series
     * of three axes.
     * @param axisOrder A string representing the order in which to rotate extrinsically. The
     *                  string must be exactly @em three characters long and contain only the
     *                  characters 'x', 'y' and 'z'.
     * @param angles The angles to rotate, in their rotation order, in @em degrees.
     * @return A rotation matrix.
     */
    public static Matrix getEulerMatrixExtrinsic(String axisOrder, EulerAngles angles) {
        return getEulerMatrix(
                axisOrder.substring(2, 3) + axisOrder.charAt(1) + axisOrder.charAt(0),
                new EulerAngles(angles.get(2), angles.get(1), angles.get(0))
        );
    }

///@}

    /// @name Single axis rotators
    /// Methods that rotate objects from a single axis rotation.
///@{

    /** Rotates a vector around a single axis extrinsically. Same
     * as getMatrix(...).mult(vector).
     * @param rotMatrix Matrix representing the rotation to apply.
     * @param vector The vector to rotate.
     * @return The rotated vector.
     */
    public static Vector Rotate(Matrix rotMatrix, Vector vector) {
        return rotMatrix.mult(vector);
    }

    /** Rotates a vector around a single axis extrinsically. Same
     * as getMatrix(axis, angle).mult(vector).
     * @param axis Axis in which to rotate around extrinsically.
     * @param angle The angle in which to rotate in @em degrees.
     * @param vector The vector to rotate.
     * @return The rotated vector.
     */
    public static Vector Rotate(Axis.Direction axis, double angle, Vector vector) {
        return Rotate(getMatrix(axis, angle), vector);
    }

    /** Rotates a vector around a single axis intrinsically. Same
     * as getMatrixIntrinsic(axisVector, angle).mult(rotateVector).
     * @param axisVector A vector representing the axis in which to rotate around intrinsically.
     * @param angle The angle in which to rotate in @em degrees.
     * @param rotateVector The vector to rotate.
     * @return The rotated vector.
     */
    public static Vector RotateIntrinsic(Vector axisVector, double angle, Vector rotateVector) {
        return Rotate(getMatrixIntrinsic(axisVector, angle), rotateVector);
    }
    // to future self: not putting a RotateIntrinsic(Axis.Direction, double, Vector) here because
    // it seems excessive, not likely to be used, and needs extra code for very little if any payoff.

///@}

    /// @name Euler rotation rotators
    /// Methods that rotate objects from Euler rotations.
///@{

    /** Rotates a vector through a series of Euler rotations intrinsically. Same
     * as getEulerMatrix(axisOrder, angles).mult(vector).
     * @param axisOrder A string representing the order in which to rotate intrinsically. The
     *                  string must be exactly @em three characters long and contain only the
     *                  characters 'x', 'y' and 'z'.
     * @param angles The angles to rotate, in their rotation order, in @em degrees.
     * @param vector The vector to rotate.
     * @return The rotated vector.
     */
    public static Vector Rotate(String axisOrder, EulerAngles angles, Vector vector) {
        return Rotate(getEulerMatrix(axisOrder, angles), vector);
    }

    /** Rotates a vector through a series of Euler rotations extrinsically. Same
     * as getEulerMatrixExtrinsic(axisOrder, angles).mult(vector).
     * @param axisOrder A string representing the order in which to rotate extrinsically. The
     *                  string must be exactly @em three characters long and contain only the
     *                  characters 'x', 'y' and 'z'.
     * @param angles The angles to rotate, in their rotation order, in @em degrees.
     * @param vector The vector to rotate.
     * @return The rotated vector.
     */
    public static Vector RotateExtrinsic(String axisOrder, EulerAngles angles, Vector vector) {
        return Rotate(getEulerMatrixExtrinsic(axisOrder, angles), vector);
    }

    // struggling on this one hard. will come back to it later
//    @todo not sure about the logic here, need to double check
    @SuppressWarnings("unused")
    public static Vector Rotate(ReferenceFrame from, ReferenceFrame to, Vector vector) {
        Matrix rotation = to.toMatrix().mult(from.toMatrix().transpose());
        return rotation.mult(vector);
    }

///@}

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