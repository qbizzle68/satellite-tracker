/**
 * @file
 * Contains Eclipse class with methods for determining if objects are eclipsed.
 */

package com.qbizzle.orbit;

import com.qbizzle.math.OrbitalMath;
import com.qbizzle.math.Vector;
import com.qbizzle.satellite.Satellite;
import com.qbizzle.time.JD;
import com.qbizzle.tracking.Sun;

/**
 * Eclipse class is a static class with methods pertaining to objects in
 * relation to other bodies and their shadows.
 */
public class Eclipse {

    /**
     * Radius of the Earth in meters.
     */
    private static final double earthRadius = OrbitalMath.EARTH_EQUITORIAL_RADIUS;

    /**
     * Radius of the Sun in meters.
     */
    private static final double sunRadius = 6.957E8; // meters

    /** Determines if an object is currently eclipsed by the earths shadow.
     * @param satPosition Position of the object relative to the Earth in IJK coordinates.
     * @param sunPosition Position of the Sun relative to the Earth in IJK coordinates.
     * @return Returns true if the object is at least partially eclipsed (umbral, penumbral,
     *          and annular), false otherwise.
     */
    public static boolean isEclipsed(Vector satPosition, Vector sunPosition) {
//        adjust position vectors to be relative to the satellite
        Vector earthFromSatPosition = satPosition.minus();
        Vector sunFromSatPosition = satPosition.minus().plus(sunPosition);
//        semi-diameters of earth and sun
        double thetaE = Math.asin(earthRadius / earthFromSatPosition.mag());
        double thetaS = Math.asin(sunRadius / sunFromSatPosition.mag());
//        angle between earth and sun centers relative to the satellite
        double theta = Math.acos(earthFromSatPosition.dot(sunFromSatPosition) /
                (earthFromSatPosition.mag() * sunFromSatPosition.mag()));

//        umbral eclipse
        if (thetaE > thetaS && theta < (thetaE - thetaS)) return true;
//        penumbral eclipse
        if (Math.abs(thetaE - thetaS) < theta && theta < (thetaE + thetaS)) return true;
//        annular eclipse
        return thetaS > thetaE && theta < (thetaS - thetaE);
    }

    public static boolean isEclipsed(Satellite satellite, JD time) {
        return isEclipsed(
                satellite.getState(time).position(),
                Sun.position(time)
        );
    }

}
