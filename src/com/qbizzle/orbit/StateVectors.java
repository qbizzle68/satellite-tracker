/** @file
 * This file contains the StateVectors class which acts as a container for two Vector
 * objects, one for the position and on for the velocity of a satellite at a given epoch.
 * The class can construct itself into a set of state vectors in a future position as well,
 * utilizing the COE class, computing a set of future orbital elements and converting them
 * into state vectors.
 * Methods for transforming orbital elements to state vectors come from the
 * "Keplerian-Orbit-Elements-to-Cartesian-State-Vectors.pdf" file.
 */

package com.qbizzle.orbit;

import com.qbizzle.math.OrbitalMath;
import com.qbizzle.math.Vector;
import com.qbizzle.referenceframe.EulerAngles;
import com.qbizzle.referenceframe.EulerOrderList;
import com.qbizzle.rotation.Rotation;

import java.util.Objects;

/** Container class for holding the position and velocity vectors of a satellite at a given epoch.
 */
public class StateVectors implements Cloneable {
    private Vector m_position, m_velocity;

    /** Constructs the object directly from a known state.
     * @param position Known position of the satellite in geocentric-equitorial reference frame.
     * @param velocity Known velocity of the satellite in geocentric-equitorial reference frame.
     */
    public StateVectors(Vector position, Vector velocity) {
        m_position = position;
        m_velocity = velocity;
    }

    /** Constructs a set of state vectors using the methodology in the "Keplerian-Orbit-Elements-to-
     * Cartesian-State-Vectors.pdf" file.
     * @param coe Set of orbital elements used to convert to state vectors.
     */
    public StateVectors(COE coe) {
        this(coe, 0.0);
    }

    /** Constructs a set of state vectors in the past/future with a conic orbit model. The
     * orbital elements remain constant with time (no perturbations such as earth oblateness,
     * drag and deep space third body gravitational effects).
     * @param coe A set of orbital elements that describes the satellites orbit.
     * @param dt The number of solar days relative to known epoch.
     * @note A negative value of @p dt constructs a state @em before the current epoch.
     */
    public StateVectors(COE coe, double dt) {
        double Mt = OrbitalMath.true2Mean( Math.toRadians(coe.ta), coe.ecc )
                + dt * OrbitalMath.sma2MMotion(coe.sma);
        double Et = OrbitalMath.mean2Eccentric(Mt, coe.ecc);
        double Vt = OrbitalMath.eccentric2True(Et, coe.ecc);
        double radius = coe.sma * (1 - coe.ecc * Math.cos( Math.toRadians(Et) ));
        Vector posOrbitFrame = new Vector( Math.cos(Vt), Math.sin(Vt), 0 ).scale(radius);
        Vector velOrbitFrame = new Vector(
                -Math.sin(Et),
                Math.sqrt(1 - Math.pow(coe.ecc, 2)) * Math.cos(Et),
                0
        ).scale(Math.sqrt(OrbitalMath.MU * coe.sma) / radius);
//        now rotate state vectors from orbital reference frame, to inertial IJK reference frame
        m_position = Rotation.rotateFrom(
                EulerOrderList.ZXZ,
                new EulerAngles(coe.lan, coe.inc, coe.aop),
                posOrbitFrame
        );
        m_velocity = Rotation.rotateFrom(
                EulerOrderList.ZXZ,
                new EulerAngles(coe.lan, coe.inc, coe.aop),
                velOrbitFrame
        );
    }

    /** Constructs a set of state vectors by interpreting the TLE into orbital elements and
     * converting from orbital elements to state vectors.
     * @param tle a recent TLE to retrieve orbital elements from.
     */
    public StateVectors(TLE tle) {
        this( new COE(tle) );
    }

    /** Constructs the object by interpreting the TLE into orbital elements and integrating
     * the changes over the time dt, then converting to state vectors.
     * @param tle A recent TLE to retrieve orbital elements and derivative terms from.
     * @param dt The amount of time in solar days to integrate over.
     * @note A negative value of @p dt constructs a state @em before the current epoch.
     */
    public StateVectors(TLE tle, double dt) {
        this( new COE(tle, dt) );
    }

    /// @name Overloaded methods.
    /// Methods that are inherited from Java Cloneable class.
///@{

    /** Returns a string representation of the state vectors.
     * @return A string representation of the vector.
     */
    @Override
    public String toString() {
        return "StateVectors{" +
                "m_position=" + m_position +
                ", m_velocity=" + m_velocity +
                '}';
    }

    /** Indicates whether a State is 'equal to' this instance.
     * @param o State to compare this State to.
     * @return True if this object is the same as @p ob argument, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StateVectors that = (StateVectors) o;
        return m_position.equals(that.m_position) && m_velocity.equals(that.m_velocity);
    }

    /** Generates a hash code for this class based on the Vector values.
     * @return A hash code for this StateVectors instance.
     */
    @Override
    public int hashCode() {
        return Objects.hash(m_position, m_velocity);
    }

    /** Creates and returns a copy of this object.
     * A deep copy of the vectors are made before returning.
     * @return A clone of this instance.
     * @warning This method returns a Java Object, so explicit casting may be necessary
     * to avoid errors.
     */
    @Override
    public Object clone() {
        try {
            StateVectors rtn = (StateVectors)super.clone();
            rtn.m_position = (Vector) m_position.clone();
            rtn.m_velocity = (Vector) m_velocity.clone();
            return rtn;
        } catch(CloneNotSupportedException ex) {
            throw new InternalError();
        }
    }

///@}

    /// @name Accessor methods.
    /// Methods to access the state vectors.
///@{

    /** Gets the position state vector.
     * @return A Vector with the satellites position.
     */
    public Vector position() {
        return m_position;
    }

    /** Gets the velocity state vector.
     * @return A Vector with the satellite's velocity.
     */
    public Vector velocity() {
        return m_velocity;
    }

///@}

}
