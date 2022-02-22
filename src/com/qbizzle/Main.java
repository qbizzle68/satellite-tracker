package com.qbizzle;

import com.qbizzle.exception.NoLightException;
import com.qbizzle.exception.NoPassException;
import com.qbizzle.math.OrbitalMath;
import com.qbizzle.math.Vector;
import com.qbizzle.orbit.TLE;
import com.qbizzle.time.JD;
import com.qbizzle.tracking.Coordinates;
import com.qbizzle.tracking.SatellitePass;
import com.qbizzle.tracking.Tracker;

import java.util.Scanner;

public class Main {

    Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        String strZarya = """
                ISS (ZARYA)            \s
                1 25544U 98067A   22052.65160095  .00007194  00000+0  13445-3 0  9993
                2 25544  51.6410 188.7488 0005260 152.5208 330.0202 15.49882275327260""";
        TLE tleZarya = new TLE(strZarya);

        Coordinates hutch = new Coordinates(38.060017, -97.930495);
        JD viewTime = new JD(2, 20, 2022, 5, 27, 32, -6);
        Coordinates coords = new Coordinates(30, 131);
//        SatellitePass satPass = Tracker.getPassInfo(tleZarya, viewTime, hutch);
//        System.out.println(satPass);

        JD riseJD = new JD(2, 22, 2022, 3, 51, 0, -6);
        try {
            SatellitePass riseAltAz = Tracker.getPassInfo(tleZarya, riseJD, hutch);
        } catch (NoPassException e) {
            System.out.println("NoPassException caught");
        } catch (NoLightException e) {
            System.out.println("NoLightException caught");
        }

    }

    static public Coordinates getCelestialCoordinates(Vector pos) {//, JD t1, Coordinates coords) {
//        Vector pos = getSEZPosition(tle, t1, coords);
//        pos = Rotation.RotateFrom(
//                EulerOrderList.ZYX,
//                new EulerAngles(
//                        SiderealTime.LST(t1, coords.getLongitude()) * 15.0,
//                        90 - coords.getLatitude(),
//                        0
//                ),
//                pos
//        );
        double ra = OrbitalMath.atan2(pos.y(), pos.x());
        if (ra < 0) ra += (2 * Math.PI);
        ra = (ra / (2 * Math.PI) * 24.0);
        return new Coordinates(
                ra,
                Math.toDegrees( Math.asin(pos.z() / pos.mag()) )
        );
    }

    // RADec being RA in time units and Dec in degrees
    // this doesn't account for proper motion of stars or planets
    static Coordinates adjustCoords(JD t, Coordinates RADec) {
        double JDCenturies = (t.Value() - JD.J2000) / 36525.0;
        double m = 3.07496 + 0.00186 * JDCenturies;
        double nra = 1.33621 - 0.00057 * JDCenturies;
        double ndec = 20.0431 - 0.0085 * JDCenturies;
        double ra = Math.toRadians( RADec.getLongitude() * 15 );
        double dec = Math.toRadians( RADec.getLatitude() );
        double dAlpha = m + (nra * Math.sin(ra) * Math.tan(dec));
        double dDel = ndec * Math.cos( ra );
        return new Coordinates(
                dDel * JDCenturies * 100,
                dAlpha * JDCenturies * 100
        );
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