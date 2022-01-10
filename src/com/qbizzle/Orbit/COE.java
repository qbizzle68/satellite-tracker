package com.qbizzle.Orbit;
import com.qbizzle.Math.Vector;
import com.qbizzle.Orbit.OrbitalMath;

public class COE {
    // units are meters, and radians where applicable except for inclination (degrees)
    private double m_semiMajorAxis, m_eccentricity, m_lan, m_aop, m_inclination, m_trueAnomaly;

    public COE(double sma, double ecc, double lan, double aop, double inc, double trueAnom) {
        m_semiMajorAxis = sma;
        m_eccentricity = ecc;
        this.m_lan = lan;
        this.m_aop = aop;
        m_inclination = inc;
        m_trueAnomaly = trueAnom;
    }

    public COE(TLE tle) {
        double meanMotion = tle.MeanMotion();
        m_semiMajorAxis = OrbitalMath.mMotion2SMA(meanMotion);
        m_eccentricity = tle.Eccentricity();
        m_lan = tle.LAN();
        m_aop = tle.AOP();
        m_inclination = tle.Inclination();
        m_trueAnomaly = OrbitalMath.Mean2True(meanMotion, m_eccentricity);
    }

    public COE(Vector position, Vector velocity) {
        Vector angMomentum = position.cross(velocity);
//        pointing towards the ascending node
        Vector lineOfNodes = angMomentum.cross(new Vector(0, 0, 1.0)).norm();
        Vector eccVector = eccentricVector(position, velocity);
        m_eccentricity = eccVector.mag();
        m_inclination = Math.acos(angMomentum.z() / angMomentum.mag());
        m_lan = Math.acos(lineOfNodes.x() / lineOfNodes.mag());
        if (lineOfNodes.y() < 0) m_lan = 360 - m_lan;
        m_aop = Math.acos(lineOfNodes.dot(eccVector) / (lineOfNodes.mag() * eccVector.mag()));
        if (eccVector.z() < 0) m_aop = 360 - m_aop;
        m_trueAnomaly = Math.acos(eccVector.dot(position) / (eccVector.mag() * position.mag()));
        if (position.dot(velocity) < 0) m_trueAnomaly = 360 - m_trueAnomaly;
        m_semiMajorAxis = (Math.pow(angMomentum.mag(), 2) * (1 - Math.pow(m_eccentricity, 2))) / OrbitalMath.MU;
    }

    private Vector eccentricVector(Vector p, Vector v) {
        Vector rtn = v.scale(p.dot(v));
        rtn = p.scale(Math.pow(v.mag(), 2) - (OrbitalMath.MU / p.mag())).minus(rtn);
        return rtn.scale(1.0 / OrbitalMath.MU);
    }

}
