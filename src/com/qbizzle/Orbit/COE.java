package com.qbizzle.Orbit;

public class COE {
    // units are meters, and radians where applicable except for inclination (degrees)
    public double semiMajorAxis, eccentricity, lan, aop, inclination, trueAnomaly;

    public COE(double sma, double ecc, double lan, double aop, double inc, double trueAnom) {
        semiMajorAxis = sma;
        eccentricity = ecc;
        this.lan = lan;
        this.aop = aop;
        inclination = inc;
        trueAnomaly = trueAnom;
    }

}
