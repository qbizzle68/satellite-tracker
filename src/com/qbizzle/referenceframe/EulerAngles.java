/** @file
 * Contains the EulerAngles class.
 */

package com.qbizzle.referenceframe;

import java.util.Arrays;

/** Class used to represent a set of Euler angles. The class makes
 * handling an array more convenient, and keeps the code cleaner.
 * The class holds exactly three values, any values not set are
 * set to zero during construction. The angles are treated as being
 * in units of degrees.
 */
public class EulerAngles implements Cloneable {
    private double[] m_angles;

    /** Default constructor, which assigns all angles to 0. */
    public EulerAngles() {
        this(0.0, 0.0, 0.0);
    }

    /** Constructs a set of Euler angles to the arguments passed in.
     * @param alpha The first rotation angle in @em degrees.
     * @param beta The second rotation angle in @em degrees.
     * @param gamma The third rotation angle in @em degrees.
     */
    public EulerAngles(double alpha, double beta, double gamma) {
        m_angles = new double[]{alpha, beta, gamma};
        for (int i = 0; i < 3; i++) {
            if (m_angles[i] >= 360.0 || m_angles[i] < 0)
                m_angles[i] %= 360.0;
        }
    }

    /// @name Overridden methods
    /// Overridden methods inherited from Object.
///@{

    /** Converts the Euler angles to a string.
     * @return A string representing the angles.
     */
    @Override
    public String toString() {
        return "EulerAngles{" +
                "m_angles=" + Arrays.toString(m_angles) +
                '}';
    }

    /** Creates a clone of this object.
     * @return A deep copy of this instance.
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        EulerAngles rtn = (EulerAngles) super.clone();
        rtn.m_angles = m_angles.clone();
        return rtn;
    }

    /** Compares this object with another.
     * @param o Object to compare this instance to.
     * @return True if @p o is of type EulerAngles and its contents are equal to
     * this instance, false if otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EulerAngles that = (EulerAngles) o;
        return Arrays.equals(m_angles, that.m_angles);
    }

    /** Creates a hash code for this object.
     * @return The hash code for this instance.
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(m_angles);
    }

///@}

    /// @name Accessor methods
    /// Methods used to access the contents of this object.
///@{

    /** Sets a single angle.
     * @param index Index of the angle to change (x=0, y=1, z=2).
     * @param angle Angle to set for this @p index, in @em degrees.
     */
    public void set(int index, double angle) {
        m_angles[index] = angle;
    }

    /** Gets a single angle.
     * @param index Index of the angle to retrieve (x=0, y=1, z=2).
     * @return The angle measured in @em degrees.
     */
    public double get(int index) {
        return m_angles[index];
    }

///@}

    /** Reverses the order of the angles, convenient for undoing rotations.
     * @return The values of this instance in reverse order.
     */
    public EulerAngles reverse() {
        return new EulerAngles(m_angles[2], m_angles[1], m_angles[0]);
    }

}
