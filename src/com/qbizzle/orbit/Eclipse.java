package com.qbizzle.orbit;

import com.qbizzle.math.OrbitalMath;
import com.qbizzle.math.Vector;

public class Eclipse {
    private static final double earthRadius = OrbitalMath.EARTH_EQUITORIAL_RADIUS;
    private static final double sunRadius = 6.957E8; // meters

    // positions are relative to the satellite
    public static boolean isEclipsed(Vector satPosition, Vector sunPosition) {
//        semi-diameters of earth and sun
        double thetaE = Math.asin(earthRadius / satPosition.mag());
        double thetaS = Math.asin(sunRadius / sunPosition.mag());
//        angle between earth and sun centers
        double theta = Math.acos(satPosition.dot(sunPosition) / (satPosition.mag() * sunPosition.mag()));

        // umbral eclipse
        if (thetaE > thetaS && theta < (thetaE - thetaS)) return true;
        // penumbral eclipse
        if (Math.abs(thetaE - thetaS) < theta && theta < (thetaE + thetaS)) return true;
        // annular eclipse
        if (thetaS > thetaE && theta < (thetaS - thetaE)) return true;
        return false;
    }
}
