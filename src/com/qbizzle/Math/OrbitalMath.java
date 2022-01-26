/** @file
 * This file contains the OrbitalMath class, which contains methods pertaining
 * to the common math involving orbital mechanics. The class contains methods
 * as well as frequently used constants.
 */

package com.qbizzle.Math;

/**
 * Static class that contains constants and methods that pertain to orbital math.
 */
public class OrbitalMath {
    /** Earth radius in meters. */
    @SuppressWarnings("unused")
    public static final double ER = 6.3781363e6; // m
    /** Earth gravitational parameter, equal to GM in m^3/s^2. */
    public static final double MU = 3.986004418e14; // m3/s2
    /** Newton's Gravitational parameter. */
    @SuppressWarnings("unused")
    public static final double G = 6.67408e-11; // m3/kgs2
    /** Epsilon value used in Newton Raphson Method. */
    public static double newtonEpsilon = 0.0000001;
    /** Constant to convert radians to degrees. */
    @SuppressWarnings("unused")
    public static final double RAD2DEG = 180.0 / Math.PI;
    /** Constant to convert degrees to radians. */
    @SuppressWarnings("unused")
    public static final double DEG2RAD = Math.PI / 180.0;

    /** Acts as a wrapper around the native atan2 method, but converts the output
     * to the interval (0, 2π).
     * @param y The y coordinate.
     * @param x The z coordinate.
     * @return The arc tangent of y/x.
     */
    public static double atan2(double y, double x) {
        double theta = Math.atan2(y, x);
        return (theta < 0) ? theta + 2*Math.PI : theta;
    }

//     \f$n = \sqrt{\frac{\mu}{a^3}}\f$.
    /** Converts mean motion to semi-major axis.
     * The conversion is done using Kepler's 2nd law.
     * @param meanMotion Mean orbital motion of the orbit in rev / day.
     * @return The semi-major axis in meters.
     */
    public static double mMotion2SMA(double meanMotion) {
        double mMotionRad = meanMotion * 2 * Math.PI / 86400.0;
        return (Math.pow(MU, (1.0 / 3.0)) / Math.pow(mMotionRad, (2.0 / 3.0)));
    }

    /** Converts semi-major axis to mean motion.
     * @param sma Semi-major axis of the orbit in meters.
     * @return The mean motion in radians/s.
     */
    public static double SMA2MMotion(double sma) {
        return Math.sqrt(MU / Math.pow(sma, 3));
    }

    /// @name Anomaly Conversions
    /// Methods to convert between the three anomalies.
    /// @note ALL ANOMALY CONVERSION METHODS ARE IN @em RADIANS.
///@{

    /** Converts mean anomaly to true anomaly. The method keeps us from needing
     * to convert anomalies twice in our code.
     * @param meanAnomaly Mean anomaly in @em radians.
     * @param eccentricity The orbits eccentricity.
     * @return True anomaly in @em radians.
     */
    public static double Mean2True(double meanAnomaly, double eccentricity) {
        return Eccentric2True(
                Mean2Eccentric(meanAnomaly, eccentricity),
                eccentricity
        );
    }

    /** Converts mean anomaly to eccentric anomaly. The method involves solving Kepler's
     * equation using the Newton-Raphson numerical method. The field @link com.qbizzle.Math.OrbitalMath.newtonEpsilon newtonEpsilon @endlink
     * is used to determine when to stop iterating the algorithm.
     * @param meanAnomaly Mean anomaly in @em radians.
     * @param eccentricity Eccentricity of the orbit.
     * @return The eccentric anomaly in @em radians.
     */
    public static double Mean2Eccentric(double meanAnomaly, double eccentricity) {
        return M2ENewtonRaphson(meanAnomaly, meanAnomaly, eccentricity);
    }

    /** Converts eccentric anomaly to true anomaly.
     * @param eccentricAnomaly Eccentric anomaly in @em radians.
     * @param eccentricity Eccentricity of the orbit.
     * @return The true anomaly in @em radians.
     */
    public static double Eccentric2True(double eccentricAnomaly, double eccentricity) {
        return atan2(
                Math.sqrt(1 - Math.pow(eccentricity,2)) * Math.sin(eccentricAnomaly),
                Math.cos(eccentricAnomaly) - eccentricity
        );
    }

    /** Converts true anomaly to mean anomaly. This method keeps us from having to make
     * two anomaly conversion in out code.
     * @param trueAnomaly True anomaly in @em radians.
     * @param eccentricity Eccentricity of the orbit.
     * @return The mean anomaly in @em radians.
     */
    public static double True2Mean(double trueAnomaly, double eccentricity) {
        return Eccentric2Mean(
                True2Eccentric(trueAnomaly, eccentricity),
                eccentricity
        );
    }

    /** Converts true anomaly to eccentric anomaly.
     * @param trueAnomaly True anomaly in @em radians.
     * @param eccentricity Eccentricity of the orbit.
     * @return The eccentric anomaly in @em radians.
     */
    public static double True2Eccentric(double trueAnomaly, double eccentricity) {
        return atan2(
                Math.sqrt(1 - Math.pow(eccentricity, 2)) * Math.sin(trueAnomaly),
                Math.cos(trueAnomaly) + eccentricity
        );
    }

    /** Converts eccentric anomaly to mean anomaly using Kepler's equation.
     * @param eccentricAnomaly Eccentric anomaly in @em radians.
     * @param eccentricity Eccentricity of the orbit.
     * @return The mean anomaly in @em radians.
     */
    public static double Eccentric2Mean(double eccentricAnomaly, double eccentricity) {
        return eccentricAnomaly - eccentricity * Math.sin(eccentricAnomaly);
    }

///@}

    /** Newton-Raphson numerical method used to solve Kepler's equation for eccentric anomaly.
     * The method is iterated until the difference in successive outputs is less than or equal to
     * the @link com.qbizzle.Math.OrbitalMath.newtonEpsilon newtonEpsilon @endlink field which can be adjusted if needed.
     * @param M Mean anomaly in @em radians.
     * @param Ej The Jth computation for eccentric anomaly. The initial guess should be the mean anomaly in @em radians.
     * @param ecc The eccentricity of the orbit.
     * @return The eccentric anomaly in @em radians.
     */
    private static double M2ENewtonRaphson(double M, double Ej, double ecc) {
        double Ej1 = Ej - ((Ej - ecc * Math.sin(Ej) - M) / (1 - ecc * Math.cos(Ej)));
        if (Math.abs(Ej1 - Ej) <= newtonEpsilon) return Ej1;
        return M2ENewtonRaphson(M, Ej1, ecc);
    }
}