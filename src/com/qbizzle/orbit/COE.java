/** @file
 *  This file contains the COE class (Classical Orbital Elements).
 *  This class is used to contain the orbital elements of a satellite for a given epoch.
 *  The values can be mean, osculating, or for any other way that's useful.
 *  The methods in this class are taken from Bate, Mueller, White and Saylor's
 *  "Fundamentals of Astrodynamics 2nd Edition."
 */

package com.qbizzle.orbit;

import com.qbizzle.math.OrbitalMath;
import com.qbizzle.math.Vector;

/** Container class for the classical orbital elements. The class contains the semi-major axis,
 * eccentricity, longitude of ascending node (right ascension of ascending node), argument of
 * periapsis, inclination and true anomaly. Notable elements not contained are mean motion and
 * mean and eccentric anomalies. These quantities can all be computed using the
 * @link com.qbizzle.math.OrbitalMath OrbitalMath @endlink class. For ease of use, and to be
 * able to quickly construct custom orbits, all element fields have public access.
 * @note All angles are measured in degrees, so converting true anomaly will require also
 * converting units for the appropriate method.
 */
public class COE {
//    intended units are meters and degrees where applicable
    /** Semi-major axis measured in meters. */
    public double sma;
    /** Eccentricity of the orbit. */
    public double ecc;
    /** Longitude of the ascending node measured in degrees.
     * @note This term is also referred to as the Right Ascension
     * of the Ascending Node (RAAN).
     */
    public double lan;
    /** Argument of periapsis measured in degrees.
     * @note The term 'periapsis' is a generic term for the closest point of an orbit.
     * In an earth-centric model it is referred to as 'perigee', and in a sun-centric
     * model it is called 'perihelion'.
     */
    public double aop;
    /** Inclination of the orbit measured in degrees.
     * @note The inclination should be a value between 0 and 180 degrees.
     */
    public double inc;
    /** True anomaly of the orbit measured in degrees. */
    public double ta;
    /** Second Earth Zonal Harmonic. */
    private static final double CJ2 = -2.064734896e14;

    /** Sets the class fields exactly to the constructor parameters.
     * @param sma Semi-major axis, measured in meters.
     * @param ecc Eccentricity of the orbit.
     * @param lan Longitude of the Ascending Node (LAN) measured in degrees.
     * @param aop Argument Of Periapsis measured in degrees.
     * @param inc Inclination of the orbit measured in degrees.
     * @param trueAnom True Anomaly of the satellites position measured in degrees.
     * @note Parameters measured in degrees should be between 0 and 360 to ensure
     * proper behavior.
     */
    public COE(double sma, double ecc, double lan, double aop, double inc, double trueAnom) {
        this.sma = sma;
        this.ecc = ecc;
        this.lan = lan;
        this.aop = aop;
        this.inc = inc;
        this.ta = trueAnom;
    }

    /** Sets the class fields to the mean orbital elements found in the tle parameter.
     * @param tle - A recent tle of the satellite the orbital elements describe.
     */
    public COE(TLE tle) {
        this(tle, 0);
    }

    /** Constructs a set of orbital elements to a past of future time relative to
     * the epoch of the TLE. sThis constructor implements the algorithm for determining
     * orbital elements in section 10.3.5 of Fundamentals of Astrodynamics.
      * @param tle - A recent tle of the satellite the orbital elements describe.
     * @param dt - Time since the epoch of the TLE, measured in Julian Days.
     */
    public COE(TLE tle, double dt) {
        inc = tle.Inclination();
        @SuppressWarnings("SpellCheckingInspection")
        double nconvert = tle.MeanMotion() * 2 * Math.PI / 86400.0;
        double a0 = Math.pow( OrbitalMath.MU / Math.pow(nconvert, 2), 1.0/3.0 );
        double dM = (tle.MeanMotion() * dt // these are in rev / day
                + tle.MeanMotionDot() * dt * dt
                + tle.MeanMotionDDot() * dt * dt * dt) * 2 * Math.PI;
        double M1 = (2 * Math.PI * (tle.MeanAnomaly() / 360.0) + dM) % (2 * Math.PI);// mean anomaly at t1 in rad
        ta = Math.toDegrees( OrbitalMath.Mean2True(M1, tle.Eccentricity()) ); // true anomaly at t1 in deg
        double n0 = tle.MeanMotion();
        double n0dot = tle.MeanMotionDot() * 2;
        @SuppressWarnings("SpellCheckingInspection")
        double adot = -2.0 * a0 * n0dot / (3.0 * n0);
        sma = a0 + adot * dt;
        @SuppressWarnings("SpellCheckingInspection")
        double edot = -2.0 * (1 - tle.Eccentricity()) * n0dot / (3.0 * n0);
        ecc = tle.Eccentricity() + edot * dt;
        @SuppressWarnings("SpellCheckingInspection")
        double lanj2dot = CJ2 * Math.pow(a0, -3.5)
                * Math.pow( (1 - Math.pow(tle.Eccentricity(), 2)), -2)
                * Math.cos( Math.toRadians(inc) );
        lan = tle.LAN() + lanj2dot * dt;
        @SuppressWarnings("SpellCheckingInspection")
        double aopj2dot = (CJ2 / 2.0) * Math.pow(a0, -3.5)
                * Math.pow( (1 - Math.pow(tle.Eccentricity(), 2)), -2)
                * Math.cos( Math.toRadians(inc) );
        aop = tle.AOP() + aopj2dot * dt;
    }

    /** Constructs a set of orbital elements from a known set of state vectors.
     * This constructor implements the algorithm for determining orbital elements in
     * section 2.4 of Fundamentals of Astrodynamics.
     * @param position Position vector for the satellite.
     * @param velocity Velocity vector for the satellite.
     */
    public COE(Vector position, Vector velocity) {
//        compute intermediate vectors used to calculate elements
        Vector angMomentum = position.cross(velocity);
        Vector lineOfNodes = (new Vector(0, 0, 1.0)).cross(angMomentum).norm();
        Vector eccVector = eccentricVector(position, velocity);
//        set elements from vectors
        ecc = eccVector.mag();
        inc = Math.toDegrees( Math.acos(angMomentum.z() / angMomentum.mag()) );
        lan = Math.toDegrees( Math.acos(lineOfNodes.x() / lineOfNodes.mag()) );
        if (lineOfNodes.y() < 0) lan = 360 - lan;
        aop = Math.toDegrees( Math.acos(lineOfNodes.dot(eccVector) / (lineOfNodes.mag() * eccVector.mag())) );
        if (eccVector.z() < 0) aop = 360 - aop;
        ta = Math.toDegrees( Math.acos(eccVector.dot(position) / (eccVector.mag() * position.mag())) );
        if (position.dot(velocity) < 0) ta = 360 - ta;
        sma = Math.pow(angMomentum.mag(), 2) / ((1 - Math.pow(ecc, 2)) * OrbitalMath.MU);
    }

    /** Computes the eccentric vector of an orbit from known state vectors.
     * equation for computation should be displayed here
     * @param p position state vector of satellite
     * @param v velocity state vector of satellite
     * @return eccentric vector of the satellite's orbit (vector pointing towards perigee, with its
     *      magnitude equivalent to the eccentricity of the orbit relating to the state vector parameters
     */
    private Vector eccentricVector(Vector p, Vector v) {
        Vector rtn = v.scale(p.dot(v));
        rtn = p.scale(Math.pow(v.mag(), 2) - (OrbitalMath.MU / p.mag())).minus(rtn);
        return rtn.scale(1.0 / OrbitalMath.MU);
    }

}
