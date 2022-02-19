package com.qbizzle.orbit;

import com.qbizzle.math.OrbitalMath;
import com.qbizzle.math.Vector;

public class Eclipse {
    private static final double earthRadius = OrbitalMath.EARTH_EQUITORIAL_RADIUS;
    private static final double sunRadius = 6.957E8; // meters

//    // positions are relative to the satellite
//    public static boolean isEclipsed(Vector satPosition, Vector sunPosition) {
////        semi-diameters of earth and sun
//        double thetaE = Math.asin(earthRadius / satPosition.mag());
//        double thetaS = Math.asin(sunRadius / sunPosition.mag());
////        angle between earth and sun centers
//        double theta = Math.acos(satPosition.dot(sunPosition) / (satPosition.mag() * sunPosition.mag()));
//
//        // umbral eclipse
//        if (thetaE > thetaS && theta < (thetaE - thetaS)) return true;
//        // penumbral eclipse
//        if (Math.abs(thetaE - thetaS) < theta && theta < (thetaE + thetaS)) return true;
//        // annular eclipse
//        if (thetaS > thetaE && theta < (thetaS - thetaE)) return true;
//        return false;
//    }

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
        if (thetaS > thetaE && theta < (thetaS - thetaE)) return true;
        return false;
    }
}
