package com.qbizzle;

import com.qbizzle.exception.InvalidTLEException;
import com.qbizzle.math.Matrix;
import com.qbizzle.math.OrbitalMath;
import com.qbizzle.math.Vector;
import com.qbizzle.orbit.TLE;
import com.qbizzle.referenceframe.Axis;
import com.qbizzle.rotation.Rotation;
import com.qbizzle.time.JD;
import com.qbizzle.time.SiderealTime;
import com.qbizzle.tracking.AltAz;
import com.qbizzle.tracking.Coordinates;
import com.qbizzle.tracking.SGP4;
import com.qbizzle.tracking.Tracker;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

public class Main {

    Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InvalidTLEException, IOException {
        String strZarya = """
                ISS (ZARYA)            \s
                1 25544U 98067A   22047.73152049  .00007798  00000+0  14545-3 0  9998
                2 25544  51.6427 213.1144 0005767 136.1669 236.9068 15.49802462326501""";
        TLE tleZarya = new TLE(strZarya);

//        JD viewTime = new JD(2, 17, 2022, 7, 15, 53, -6);
//        Vector issPosition = SGP4.Propagate(tleZarya, viewTime).Position();
//        Vector sunPosition = Sun.Position(viewTime);
//        Boolean isVisible = !Eclipse.isEclipsed(issPosition.minus(), issPosition.minus().plus(sunPosition));
//        System.out.println(isVisible);

//        double checkDuration = 14.0;
//        JD checkTo = new JD(tleZarya).Future(checkDuration);
//        double interval = 1.0 / 1440.0;
//        int numIterations = (int)(checkDuration / interval);
//
//        for (int i = 0; i < numIterations; i++) {
//            JD currentTime = new JD(tleZarya).Future(i * interval);
//        }

        // find position vector in ijk
        Coordinates hutch = new Coordinates(38.0608, -97.9298);
//        JD startTime = new JD(tleZarya);
        JD startTime = new JD(2, 16, 2022, 18, 0, 53);
        Vector pos = SGP4.Propagate(tleZarya, startTime).Position();
        System.out.println("original position: " + pos);

        // find position vector offset by finding sez origin offset to ijk origin
        double LST = SiderealTime.LST(startTime, hutch.getLongitude()) * 360.0 / 24.0;
        Vector hutchPos = new Vector(
                OrbitalMath.EARTH_EQUITORIAL_RADIUS * Math.cos( Math.toRadians(hutch.getLatitude()) ) * Math.cos( Math.toRadians(LST) ),
                OrbitalMath.EARTH_EQUITORIAL_RADIUS * Math.cos( Math.toRadians(hutch.getLatitude()) ) * Math.sin( Math.toRadians(LST) ),
                OrbitalMath.EARTH_EQUITORIAL_RADIUS * Math.sin( Math.toRadians(hutch.getLatitude()) )
        );
        double st = SiderealTime.ST(startTime);
//        hutchPos = Rotation.RotateFrom(Axis.Direction.Z, st, hutchPos);

        // find rotation matrix between ijk and sez
//        double LST = SiderealTime.LST(startTime, hutch.getLongitude()) * 360.0 / 24.0;
        Matrix sezToIJK = Rotation.getMatrix(Axis.Direction.Z, LST).mult(Rotation.getMatrix(Axis.Direction.Y, 90 - hutch.getLatitude()));

        // rotate from ijk to sez, then add the offset vector
        Vector sezPos = rotateTranslate(sezToIJK, hutchPos, pos);
        Vector sezPos2 = Tracker.getSEZPosition(tleZarya, startTime, hutch);
        System.out.println("sezPos: " + sezPos);
        System.out.println("sezPos2: " + sezPos2);

        // compute alt az
        AltAz altaz = new AltAz(
                Math.toDegrees( Math.asin(sezPos.z() / sezPos.mag()) ),
                Math.toDegrees( OrbitalMath.atan2(sezPos.y(), -sezPos.x()) )
        );
        System.out.println("alt: " + altaz.getAltitude() + " az: " + altaz.getAzimuth());

        System.out.println(Tracker.isAboveHorizon(tleZarya, startTime, hutch));

        double declination = OrbitalMath.atan2(pos.z(), pos.mag());
        System.out.println( Math.toDegrees(declination) );

    }

    // rotation is from inertial to rotated, offset in inertial frame pointing from inertial origin to translated origin
    static Vector rotateTranslate(Matrix rot, Vector offset, Vector original) {
        Vector rotOrig = (rot.transpose()).mult(original);
        Vector rotOffset = (rot.transpose()).mult(offset);
        return rotOrig.minus(rotOffset);
    }

    // pass a time the satellite is overhead, this will find when the sat crosses above
    // the horizon, and the time it falls below
    public static double[] times(TLE tle, Coordinates coords, JD t) {


        // find the time of sat-rise
        // can make way better guesses than 10 minutes but doing for simplicity
        double riseTime = riseSqueeze(tle, t.Future(-30.0/1440.0), t, coords);
        double setTime = setSqueeze(tle, t, t.Future(15.0/1440.0), coords);
        return new double[]{riseTime, setTime};
    }

    static double squeezeEpsilon = 1.0 / 864000.0; // 1/10th of a second

    static double riseSqueeze(TLE tle, JD lower, JD upper, Coordinates coords) {
//        System.out.println("rise - lower: " + lower.Value() + " upper: " + upper.Value());
        if (upper.Difference(lower) <= squeezeEpsilon) return upper.Value();
        JD biTime = lower.Future((upper.Value() - lower.Value()) / 2.0);
//        System.out.println("biTime: " + biTime.Value());
//        System.out.println(Tracker.isAboveHorizon(tle, biTime, coords));
        if (Tracker.isAboveHorizon(tle, biTime, coords)) return riseSqueeze(tle, lower, biTime, coords);
        else return riseSqueeze(tle, biTime, upper, coords);
    }

    static double setSqueeze(TLE tle, JD lower, JD upper, Coordinates coords) {
        if (upper.Difference(lower) <= squeezeEpsilon) return lower.Value();
        JD biTime = lower.Future((upper.Value() - lower.Value()) / 2.0);
        if (Tracker.isAboveHorizon(tle, biTime, coords)) return setSqueeze(tle, biTime, upper, coords);
        else return setSqueeze(tle, lower, biTime, coords);
    }

    static String getUTC(JD jd) {
        int J = jd.Number();
        int y = 4716;
        int j = 1401;
        int m = 2;
        int n = 12;
        int r = 4;
        int p = 1461;
        int v = 3;
        int u = 5;
        int s = 153;
        int w = 2;
        int B = 274277;
        int C = -38;
        int f = J + j + (((4 * J * B) / 146097) * 3) / 4 + C;
        int e = r * f + v;
        int g = (e % p) / r;
        int h = u * g + w;
        int D = (h % s) / u + 1;
        int M = (h / s + m) % n + 1;
        int Y = (e / p) - y + (n + m - M) / n;
        double htemp = jd.Fraction() + 0.5;
        if (htemp > 1) {
            D++; // this can cause the day to go over the valid number of days in a given month
            htemp -= 1.0;
        }
        int H = (int)(htemp * 24);
        htemp -= (H / 24.0);
        int Min = (int)(htemp * 1440.0);
        htemp -=(Min / 1440.0);
        return M + "/" + D + "/" + Y + " " + H + ":" + Min + ":" + (htemp * 86400.0);
    }

}