/**
 * This file contains the StateVectors class which acts as a container for two Vector
 * objects, one for the position and on for the velocity of a satellite at a given epoch.
 * The class can construct itself into a set of state vectors in a future position as well,
 * utilizing the COE class, computing a set of future orbital elements and converting them
 * into state vectors.
 * Methods for transforming orbital elements to state vectors come from the
 * "Keplerian-Orbit-Elements-to-Cartesian-State-Vectors.pdf" file.
 */

package com.qbizzle.Orbit;
import com.qbizzle.Math.Vector;

public class StateVectors implements Cloneable {
    private Vector m_position, m_velocity;

    /**
     * Constructs the object directly from a known state
     * @param position known position of the satellite in geocentric-equitorial reference frame
     * @param velocity known position of the satellite in geocentric-equitorial reference frame
     */
    public StateVectors(Vector position, Vector velocity) {
        m_position = position;
        m_velocity = velocity;
    }

    /**
     * Constructs a set of state vectors using the methodology in the "Keplerian-Orbit-Elements-to-
     * Cartesian-State-Vectors.pdf" file
     * @param coe set of orbital elements used to convert to state vectors
     */
    public StateVectors(COE coe) {
        this(coe, 0.0);
    }

    /**
     * Constructs a set of state vectors in the past/future iwth a conic orbit model, i.e. the
     * orbital elements remain constatnt with time (no perturbations such as earth oblateness,
     * drag and deep space third body gravitational effects).
     * @param coe a set of orbital elements that describes the satellites orbit
     * @param dt the number of solar days in the past (negative) or future (positive).
     */
    public StateVectors(COE coe, double dt) {
        double Mt = OrbitalMath.True2Mean( Math.toRadians(coe.ta), coe.ecc )
                + dt * OrbitalMath.SMA2MMotion(coe.sma);
        double Et = OrbitalMath.Mean2Eccentric(Mt, coe.ecc);
        double Vt = OrbitalMath.Eccentric2True(Et, coe.ecc);
        double radius = coe.sma * (1 - coe.ecc * Math.cos( Math.toRadians(Et) ));
        Vector posOrbitFrame = new Vector( Math.cos(Vt), Math.sin(Vt), 0 ).scale(radius);
        Vector velOrbitFrame = new Vector(
                -Math.sin(Et),
                Math.sqrt(1 - Math.pow(coe.ecc, 2)) * Math.cos(Et),
                0
        ).scale(Math.sqrt(OrbitalMath.MU * coe.sma) / radius);
//        now rotate state vectors from orbital reference frame, to inertial IJK reference frame
//        TODO: replace this hard code with a rotation (probably a matrix).
        m_position = rotateToInertialFrame(
                posOrbitFrame,
                Math.toRadians(coe.lan),
                Math.toRadians(coe.inc),
                Math.toRadians(coe.aop)
        );
        m_velocity = rotateToInertialFrame(
                velOrbitFrame,
                Math.toRadians(coe.lan),
                Math.toRadians(coe.inc),
                Math.toRadians(coe.aop)
        );
    }

    /**
     * Constructs a set of state vectors by interpreting the TLE into orbital elements and
     * converting from orbital elements to state vectors.
     * @param tle a recent TLE to retrieve orbital elements from.
     */
    public StateVectors(TLE tle) {
        this( new COE(tle) );
    }

    /**
     * Constructs the object by interpreting the TLE into orbital elements and integrating
     * the changes over the time dt, then converting to state vectors
     * @param tle a recent TLE to retrieve orbital elements and derivative terms from.
     * @param dt the future or past(negative) time to integrate over
     */
    public StateVectors(TLE tle, double dt) {
        this( new COE(tle, dt) );
    }

    /**
     * methods overloading the Cloneable extension
     */
    public String toString() {
        return "Position: " + m_position.toString() + ", Velocity " + m_velocity.toString();
    }
    public boolean equals(Object ob) {
        if (ob instanceof StateVectors) {
            StateVectors state = (StateVectors) ob;
            return (m_position.equals(state.m_position) && m_velocity.equals(state.m_velocity));
        }
        else throw new IllegalArgumentException("Object not of type 'StateVectors'");
    }
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

    /**
     * accessor methods for state vectors
     */
    public Vector Position() {
        return m_position;
    }
    public Vector Velocity() {
        return m_velocity;
    }

//    this method is just to take the place of a rotation matrix to keep calling methods
//    less messy. this should not be needed when a sufficient rotation library is finished
//    angle parameters need to be in radians
    private Vector rotateToInertialFrame(Vector xOrbitFrame, double lan, double inc, double aop) {
        double xXTerm1 = xOrbitFrame.x()
                * (Math.cos(aop) * Math.cos(lan) - Math.sin(aop) * Math.cos(inc) * Math.sin(lan));
        double xXTerm2 = xOrbitFrame.y()
                * (Math.sin(aop) * Math.cos(lan) + Math.cos(aop) * Math.cos(inc) * Math.sin(lan));
        double xYTerm1 = xOrbitFrame.x()
                * (Math.cos(aop) * Math.sin(lan) + Math.sin(aop) * Math.cos(inc) * Math.cos(lan));
        double xYTerm2 = xOrbitFrame.y()
                * (Math.cos(aop) * Math.cos(inc) * Math.cos(lan) - Math.sin(aop) * Math.sin(lan));
        double xZTerm1 = xOrbitFrame.x() * (Math.sin(aop) * Math.sin(inc));
        double xZTerm2 = xOrbitFrame.y() * (Math.cos(aop) * Math.sin(inc));

        return new Vector(
                xXTerm1 - xXTerm2,
                xYTerm1 + xYTerm2,
                xZTerm1 + xZTerm2
        );
    }

}
