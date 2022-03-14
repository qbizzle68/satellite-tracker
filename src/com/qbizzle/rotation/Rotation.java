/** @file
 * Contains a static class used for rotating vectors, matrices,
 * reference frames and other rotations.
 */

package com.qbizzle.rotation;

import com.qbizzle.math.Matrix;
import com.qbizzle.math.Vector;
import com.qbizzle.referenceframe.Axis;
import com.qbizzle.referenceframe.EulerAngles;
import com.qbizzle.referenceframe.EulerOrder;
import com.qbizzle.referenceframe.ReferenceFrame;

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
     * @param order The order of the Euler rotation.
     * @param angles The angles to rotate, in their rotation order, in @em degrees
     * @return A single matrix representing the three rotations of the Euler angles.
     */
    public static Matrix getEulerMatrix(EulerOrder order, EulerAngles angles) {
        return getMatrix(order.first_rotation, angles.get(0))
                .mult( getMatrix(order.second_rotation, angles.get(1)) )
                .mult( getMatrix(order.third_rotation, angles.get(2)) );
    }

    /** Creates a rotation matrix for moving between two Euler rotations.
     * @param fromOrder The order of rotations of the reference frame moving from.
     * @param fromAngles The angles to rotate, in their rotation order, in @em degrees
     *                   of the reference frame moving from.
     * @param toOrder The order of rotations of the reference frame moving to.
     * @param toAngles The angles to rotate, in their rotation order, in @em degrees
     *                 of the reference frame moving to.
     * @return A single matrix representing the move between reference frames.
     */
    public static Matrix getEulerMatrix(EulerOrder fromOrder, EulerAngles fromAngles, EulerOrder toOrder, EulerAngles toAngles) {
        return getEulerMatrix(toOrder, toAngles).transpose()
                .mult(getEulerMatrix(fromOrder, fromAngles));
    }

    /** Creates a rotation matrix for moving between two Euler rotations.
     * @param from The reference frame moving from.
     * @param to The reference frame moving to.
     * @return A single matrix representing the rotation between reference frames.
     */
    public static Matrix getEulerMatrix(ReferenceFrame from, ReferenceFrame to) {
        return to.toMatrix().transpose().mult(from.toMatrix());
    }

    /** Creates a rotation matrix for an extrinsic Euler rotation about a series
     * of three axes.
     * @param order The order of the rotations.
     * @param angles The angles to rotate, in their rotation order, in @em degrees.
     * @return A single matrix representing the three rotations of the Euler angles.
     */
    public static Matrix getEulerMatrixExtrinsic(EulerOrder order, EulerAngles angles) {
        return getEulerMatrix(
                new EulerOrder( order.third_rotation, order.second_rotation, order.first_rotation ),
                new EulerAngles( angles.get(2), angles.get(1), angles.get(0) )
        );
    }

///@}

    /// @name Single axis rotators
    /// Methods that rotate objects from a single axis rotation.
///@{

    /** Rotates a vector from an inertial reference frame, to a rotated
     * reference frame.
     * @param rotMatrix Matrix representing the rotation to apply.
     * @param vector The vector to rotate.
     * @return The rotated vector.
     */
    public static Vector rotateTo(Matrix rotMatrix, Vector vector) {
        return rotMatrix.transpose().mult(vector);
    }

    /** Rotates a vector from a rotated reference frame, to an inertial
     * reference frame.
     * @param rotMatrix Matrix representing the rotation to apply.
     * @param vector The vector to rotate.
     * @return The rotated vector.
     */
    public static Vector rotateFrom(Matrix rotMatrix, Vector vector) {
        return rotMatrix.mult(vector);
    }

    /** Rotates a vector from an inertial reference frame, to a reference
     * frame rotated around a single axis extrinsically.
     * @param axis Axis in which to rotate around extrinsically.
     * @param angle The angle in which to rotate in @em degrees.
     * @param vector The vector to rotate.
     * @return The rotated vector.
     */
    public static Vector rotateTo(Axis.Direction axis, double angle, Vector vector) {
        return rotateTo(getMatrix(axis, angle), vector);
    }

    /** Rotates a vector from a reference frame rotated by a single axis
     * extrinsically, to an inertial reference frame.
     * @param axis Axis in which to rotate around.
     * @param angle The angle in which to rotate in @em degrees.
     * @param vector The vector to rotate.
     * @return The rotated vector.
     */
    public static Vector rotateFrom(Axis.Direction axis, double angle, Vector vector) {
        return rotateFrom(getMatrix(axis, angle), vector);
    }

    /** Rotates a vector from an inertial reference frame, to a reference
     * frame rotated around a single axis intrinsically.
     * @param axisVector A vector representing the axis in which to rotate around.
     * @param angle The angle in which to rotate in @em degrees.
     * @param rotateVector The vector to rotate.
     * @return The rotated vector.
     */
    public static Vector rotateIntrinsicTo(Vector axisVector, double angle, Vector rotateVector) {
        return rotateTo(getMatrixIntrinsic(axisVector, angle), rotateVector);
    }

    /** Rotates a vector from a reference frame rotated around a single axis
     * intrinsically, to an inertial reference frame.
     * @param axisVector A vector representing the axis in which to rotate around.
     * @param angle The angle in which to rotate in @em degrees.
     * @param rotateVector The vector to rotate.
     * @return The rotated vector.
     */
    @SuppressWarnings("unused")
    public static Vector rotateIntrinsicFrom(Vector axisVector, double angle, Vector rotateVector) {
        return rotateFrom(getMatrixIntrinsic(axisVector, angle), rotateVector);
    }

///@}

    /// @name Euler rotation rotators
    /// Methods that rotate objects from Euler rotations.
///@{

    /** Rotates a vector from an inertial reference frame, to a reference
     * frame rotated by an Euler rotation intrinsically.
     * @param order The order of rotations.
     * @param angles The angles to rotate, in their rotation order, in @em degrees.
     * @param vector The vector to rotate.
     * @return The rotated vector.
     */
    public static Vector rotateTo(EulerOrder order, EulerAngles angles, Vector vector) {
        return rotateTo(getEulerMatrix(order, angles), vector);
    }

    /** Rotates a vector from a reference frame rotated by an intrinsic Euler rotation,
     * to an inertial reference frame.
     * @param order The order of rotations.
     * @param angles The angles to rotate, in their rotation order, in @em degrees.
     * @param vector The vector to rotate.
     * @return The rotated vector.
     */
    public static Vector rotateFrom(EulerOrder order, EulerAngles angles, Vector vector) {
        return rotateFrom(getEulerMatrix(order, angles), vector);
    }

    /** Rotates a vector from an inertial reference frame, to a reference frame
     * rotated by an Euler rotation extrinsically.
     * @param order The order of rotations.
     * @param angles The angles to rotate, in their rotation order, in @em degrees.
     * @param vector The vector to rotate.
     * @return The rotated vector.
     */
    @SuppressWarnings("unused")
    public static Vector rotateExtrinsicTo(EulerOrder order, EulerAngles angles, Vector vector) {
        return rotateTo(getEulerMatrixExtrinsic(order, angles), vector);
    }

    /** Rotates a vector from a reference frame rotated by an extrinsic Euler rotation,
     * to an inertial reference frame.
     * @param order The order of rotations.
     * @param angles The angles to rotate, in their rotation order, in @em degrees.
     * @param vector The vector to rotate.
     * @return The rotated vector.
     */
    @SuppressWarnings("unused")
    public static Vector rotateExtrinsicFrom(EulerOrder order, EulerAngles angles, Vector vector) {
        return rotateFrom(getEulerMatrixExtrinsic(order, angles), vector);
    }

    /** Rotates a vector from one reference frame to another.
     * @param from   The reference frame moving from.
     * @param to     The reference frame moving to.
     * @param vector The vector to rotate.
     * @return The rotated vector.
     */
    public static Vector rotate(ReferenceFrame from, ReferenceFrame to, Vector vector) {
        return to.toMatrix().transpose().mult(from.toMatrix()).mult(vector);
    }

///@}

    /** Generates an extrinsic rotation around the X-Axis.
     * @param alpha Angle to rotate in @em radians.
     * @return The rotation matrix.
     */
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

    /** Generates an extrinsic rotation around the Y-Axis.
     * @param beta Angle to rotate in @em radians.
     * @return The rotation matrix.
     */
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

    /** Generates an extrinsic ration around the Z-Axis.
     * @param gamma Angle to rotate in @em radians.
     * @return The rotation matrix.
     */
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