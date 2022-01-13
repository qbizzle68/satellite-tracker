package com.qbizzle.Orbit;

public class OrbitalMath {
    public static final double ER = 6378.1363e3; // KM
    public static final double MU = 3.986004418e14; // m3/s2
    public static final double G = 6.67408e-11; // m3/kgs2
    public static double newtonEpsilon = 0.0000001;
    public static final double RAD2DEG = 180.0 / Math.PI;
    public static final double DEG2RAD = Math.PI / 180.0;

//    mean motion in rev / day to semimajor axis in meters;
    public static double mMotion2SMA(double meanMotion) {
        double mMotionRad = meanMotion * 2 * Math.PI / 86400.0;
        return (Math.pow(MU, (1.0/3.0)) / Math.pow(mMotionRad, (2.0/3.0)));
    }

//    all anomalies are in radians
    public static double Mean2True(double meanAnomaly, double eccentricity) {
        return Eccentric2True(
                Mean2Eccentric(meanAnomaly, eccentricity),
                eccentricity
        );
    }
    public static double Mean2Eccentric(double meanAnomaly, double eccentricity) {
        return M2ENewtonRaphson(meanAnomaly, meanAnomaly, eccentricity);
    }
    public static double Eccentric2True(double eccentricAnomaly, double eccentricity) {
        return 2 * Math.atan2(
                Math.sqrt(1 + eccentricity) * Math.sin(eccentricAnomaly / 2.0),
                Math.sqrt(1 - eccentricity) * Math.cos(eccentricAnomaly / 2.0)
        );
    }
    public static double True2Mean(double trueAnomaly, double eccentricity) {
        return Eccentric2Mean(
                True2Eccentric(trueAnomaly, eccentricity),
                eccentricity
        );
    }
    public static double True2Eccentric(double trueAnomaly, double eccentricity) {
        return Math.acos( (eccentricity + Math.cos(trueAnomaly)) / (1 + eccentricity * Math.cos(trueAnomaly)) );
    }
//    Kepler's equation
    public static double Eccentric2Mean(double eccentricAnomaly, double eccentricity) {
        return eccentricAnomaly - eccentricity * Math.sin(eccentricAnomaly);
    }

    private static double M2ENewtonRaphson(double M, double Ej, double ecc) {
        double Ej1 = Ej - ((Ej - ecc * Math.sin(Ej) - M) / (1 - ecc * Math.cos(Ej)));
        if (Math.abs(Ej1 - Ej) <= newtonEpsilon) return Ej1;
        return M2ENewtonRaphson(M, Ej1, ecc);
    }
}