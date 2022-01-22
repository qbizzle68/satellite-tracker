package com.qbizzle;

import com.qbizzle.Math.JD;
import com.qbizzle.Math.OrbitalMath;
import com.qbizzle.Math.Vector;

import java.util.Scanner;

import static java.lang.Math.toRadians;

public class Main {

    Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
//        double siderealTime = ST(new JD(new Date()), -6.0);
////        int hour = (int) siderealTime;
////        siderealTime -= hour;
////        int minute = (int)(siderealTime * 60.0);
////        siderealTime -= (minute / 60);
////        double second = siderealTime * 60.0;
////        System.out.println("Sidereal Time: " + hour + ":" + minute + ":" + second);
//        double offsetAngle = siderealTime / 24.0 * 360.0;
//        String strZarya = "ISS (ZARYA)\n" +
//                "1 25544U 98067A   08264.51782528  .00007752  00000+0  14579-3 0  9998\n" +
//                "2 25544  51.6445 346.8728 0006795  43.1425  16.8937 15.49568201322317";
//        // 22020.72035763 replace the epoch to this
//        TLE tleZarya;
//        try {
//             tleZarya = new TLE(strZarya);
//        } catch(BadTLEFormatException ex) {
//            System.out.println("Bad tle format");
//            throw new InternalError("Bad tle format");
//        }
//        StateVectors state = new StateVectors(tleZarya);
//        System.out.println(state.Position().toString());
//        System.out.println(offsetAngle);
//        Vector issPos = Rotate(state.Position(), -offsetAngle);
//        System.out.println(issPos.toString());

        System.out.println(OrbitalMath.Mean2Eccentric(0.7853981634, 0.0123));
    }

    // time zone should be relative to GMT (CST = -6.0)
    public static double ST(JD now, double tZone) {
        double dt = now.Value() - (tZone / 24.0) - JD.J2000;
        return (18.697_374_558 + 24.065_709_824_419_08 * dt) % 24.0;
    }

    // rotate vector around polar axis
    public static Vector Rotate(Vector vec, double phi) {
        double phiRad = toRadians(phi);
        double v1 = vec.x() * Math.cos(phiRad) - vec.y() * Math.sin(phiRad);
        double v2 = vec.x() * Math.sin(phiRad) + vec.y() * Math.cos(phiRad);
        return new Vector(v1, v2, vec.z());
    }

}