/**
 *  This file contains the COE class (Classical Orbital Elements).
 *  This class is used to contain the orbital elements of a satellite for a given epoch.
 *  The values can be mean, osculating, or for any other way that's useful.
 *  The methods in this class are taken from Bate, Mueller, White and Saylor's
 *  "Fundamentals of Astrodynamics 2nd Edition."
 */

package com.qbizzle.Orbit;
import com.qbizzle.Math.Vector;

public class COE {
//    intended units are meters and degrees where applicable
    public double sma, ecc, lan, aop, inc, ta;
    private static final double CJ2 = -2.064734896e14;

    /**
     * Sets the class fields exactly to the constructor parameters.
     * @param sma Semi-major axis, ideally measured in meters
     * @param ecc Eccentricity of the orbit
     * @param lan Longitude of the Ascending Node (LAN), ideally measured in degrees [0, 360),
     *            also referred to as the Right Ascension of the Ascending Node (RAAN)
     * @param aop Argument Of Perigee, ideally measured in degrees [0, 360)
     * @param inc Inclination of the orbit, ideally measured in degrees [0, 180]
     * @param trueAnom True Anomaly of the satellites position, ideally measured in degrees [0, 360)
     */
    public COE(double sma, double ecc, double lan, double aop, double inc, double trueAnom) {
        this.sma = sma;
        this.ecc = ecc;
        this.lan = lan;
        this.aop = aop;
        this.inc = inc;
        this.ta = trueAnom;
    }

    /**
     * Sets the class fields to the mean orbital elements found in the tle parameter.
     * @param tle - A recent tle of the satellite the orbital elements describe.
     */
    public COE(TLE tle) {
        this(tle, 0);
    }

    /**
     * This constructor implements the algorithm for determining orbital elements in
     * section 10.3.5 of Fundamentals of Astrodynamics.
      * @param tle - A recent tle of the satellite the orbital elements describe.
     * @param dt - Time since the epoch of the TLE, measured in Julian Days.
     */
    public COE(TLE tle, double dt) {
        inc = tle.Inclination();
        double a0 = Math.pow( OrbitalMath.MU / Math.pow(tle.MeanMotion(), 2), 1.0/3.0 );
        double dM = (tle.MeanMotion() * dt
                + tle.MeanMotionDot() * dt * dt
                + tle.MeanMotionDDot() * dt * dt * dt) * 2 * Math.PI;
        double M1 = Math.toRadians(tle.MeanAnomaly()) + dM; // mean anomaly at t1 in rad
        ta = Math.toDegrees( OrbitalMath.Mean2True(M1, tle.Eccentricity()) ); // true anomaly at t1 in deg
        double n0 = tle.MeanMotion();
        double n0dot = tle.MeanMotionDot() * 2;
        double adot = -2.0 * a0 * n0dot / (3.0 * n0);
        sma = a0 + adot * dt;
        double edot = -2.0 * (1 - tle.Eccentricity()) * n0dot / (3.0 * n0);
        ecc = tle.Eccentricity() + edot * dt;
        double lanj2dot = CJ2 * Math.pow(a0, -3.5)
                * Math.pow( (1 - Math.pow(tle.Eccentricity(), 2)), -2)
                * Math.cos( Math.toRadians(inc) );
        lan = tle.LAN() + lanj2dot * dt;
        double aopj2dot = (CJ2 / 2.0) * Math.pow(a0, -3.5)
                * Math.pow( (1 - Math.pow(tle.Eccentricity(), 2)), -2)
                * Math.cos( Math.toRadians(inc) );
        aop = tle.AOP() + aopj2dot * dt;
    }

    /**
     * This constructor implements the algorithm for determining orbital elements in
     * section 2.4 of Fundamentals of Astrodynamics.
     * @param position state vector for satellite
     * @param velocity state vector for satellite
     */
    public COE(Vector position, Vector velocity) {
//        compute intermediate vectors used to calculate elements
        Vector angMomentum = position.cross(velocity);
        Vector lineOfNodes = angMomentum.cross(new Vector(0, 0, 1.0)).norm(); // pointing towards the ascending node
        Vector eccVector = eccentricVector(position, velocity);
//        set elements from vectors
        ecc = eccVector.mag();
        inc = Math.acos(angMomentum.z() / angMomentum.mag());
        lan = Math.acos(lineOfNodes.x() / lineOfNodes.mag());
        if (lineOfNodes.y() < 0) lan = 360 - lan;
        aop = Math.acos(lineOfNodes.dot(eccVector) / (lineOfNodes.mag() * eccVector.mag()));
        if (eccVector.z() < 0) aop = 360 - aop;
        ta = Math.acos(eccVector.dot(position) / (eccVector.mag() * position.mag()));
        if (position.dot(velocity) < 0) ta = 360 - ta;
        sma = (Math.pow(angMomentum.mag(), 2) * (1 - Math.pow(ecc, 2))) / OrbitalMath.MU;
    }

    /**
     * For computing the eccentric vector
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
