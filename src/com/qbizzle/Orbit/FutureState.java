package com.qbizzle.Orbit;

import com.qbizzle.Math.JD;
import com.qbizzle.Math.Vector;

public class FutureState {
    public static StateVectors GetState(TLE tle, JD date) {
        return FutureState.GetState(
                tle,
                (new JD(tle)).Difference(date)
        );
    }

//    futureDays is in days (should be able to be negative)
    public static StateVectors GetState(TLE tle, double futureDays) {
        double dtSeconds = futureDays * 86400.0;
        double sma = OrbitalMath.mMotion2SMA(tle.MeanMotion());
        double meanAnomT = tle.MeanAnomaly()*OrbitalMath.DEG2RAD + dtSeconds * Math.sqrt(OrbitalMath.MU / Math.pow(sma, 3));
        if (meanAnomT > 2 * Math.PI) meanAnomT %= (2 * Math.PI);
        double eccAnom = OrbitalMath.Mean2Eccentric(meanAnomT, tle.Eccentricity());
        double trueAnom = OrbitalMath.Eccentric2True(eccAnom, tle.Eccentricity());
        double radius = sma * (1 - tle.Eccentricity() * Math.cos(eccAnom));
        Vector vecPos = new Vector(
                Math.cos(trueAnom),
                Math.sin(trueAnom),
                0
        ); vecPos.scaleEquals(radius);
        Vector vecVel = new Vector(
                -Math.sin(eccAnom),
                Math.sqrt(1 - tle.Eccentricity()*tle.Eccentricity()) * Math.cos(eccAnom),
                0
        ); vecVel.scaleEquals(Math.sqrt(OrbitalMath.MU * sma) / radius);
        double posX = vecPos.x() * (Math.cos(tle.AOP())*Math.cos(tle.LAN())
                - Math.sin(tle.AOP())*Math.cos(tle.Inclination()*Math.sin(tle.LAN())))
                - vecPos.y() * (Math.sin(tle.AOP())*Math.cos(tle.LAN())
                + Math.cos(tle.AOP())*Math.cos(tle.Inclination())*Math.sin(tle.LAN()));
        double posY = vecPos.x() * (Math.cos(tle.AOP())*Math.sin(tle.LAN())
                - Math.sin(tle.AOP())*Math.cos(tle.Inclination()*Math.cos(tle.LAN())))
                - vecPos.y() * (Math.cos(tle.AOP())*Math.cos(tle.Inclination())*Math.cos(tle.LAN())
                + Math.sin(tle.AOP())*Math.sin(tle.LAN()));
        double posZ = vecPos.x() * Math.sin(tle.AOP()) * Math.sin(tle.Inclination())
                + vecPos.y() * Math.cos(tle.AOP()) * Math.sin(tle.Inclination());
        double velX = vecVel.x() * (Math.cos(tle.AOP())*Math.cos(tle.LAN())
                - Math.sin(tle.AOP())*Math.cos(tle.Inclination()*Math.sin(tle.LAN())))
                - vecVel.y() * (Math.sin(tle.AOP())*Math.cos(tle.LAN())
                + Math.cos(tle.AOP())*Math.cos(tle.Inclination())*Math.sin(tle.LAN()));
        double velY = vecVel.x() * (Math.cos(tle.AOP())*Math.sin(tle.LAN())
                - Math.sin(tle.AOP())*Math.cos(tle.Inclination()*Math.cos(tle.LAN())))
                - vecVel.y() * (Math.cos(tle.AOP())*Math.cos(tle.Inclination())*Math.cos(tle.LAN())
                + Math.sin(tle.AOP())*Math.sin(tle.LAN()));
        double velZ = vecVel.x() * Math.sin(tle.AOP()) * Math.sin(tle.Inclination())
                + vecVel.y() * Math.cos(tle.AOP()) * Math.sin(tle.Inclination());
        return new StateVectors(
                new Vector(posX, posY, posZ),
                new Vector(velX, velY, velZ)
        );
    }
}
