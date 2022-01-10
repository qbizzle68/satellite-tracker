package com.qbizzle.Orbit;
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

}
