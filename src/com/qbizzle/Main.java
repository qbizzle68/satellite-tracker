package com.qbizzle;

import com.qbizzle.Math.JD;
import com.qbizzle.Math.OrbitalMath;
import com.qbizzle.Math.Vector;
import com.qbizzle.Orbit.BadTLEFormatException;
import com.qbizzle.Orbit.COE;
import com.qbizzle.Orbit.TLE;

import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

import static java.lang.Math.toRadians;

public class Main {

    Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, BadTLEFormatException {
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

        String strLEOTLE = "ISS (ZARYA)             \n" +
                "1 25544U 98067A   22022.91470718  .00005958  00000+0  11386-3 0  9993\n" +
                "2 25544  51.6445 336.0056 0006830  51.7508  17.5213 15.49594026322655";
        COE coe = new COE(new TLE(strLEOTLE));
        System.out.println("sma: " + coe.sma);
        System.out.println("ecc: " + coe.ecc);
        System.out.println("inc: " + coe.inc);
        System.out.println("lan: " + coe.lan);
        System.out.println("aop: " + coe.aop);
        System.out.println("ta: " + coe.ta);
        System.out.println("ta-rad: " + Math.toRadians(coe.ta));
        System.out.println("ma: " + OrbitalMath.True2Mean(Math.toRadians(coe.ta), coe.ecc));
        System.out.println("ea: " + OrbitalMath.True2Eccentric(Math.toRadians(coe.ta), coe.ecc));
        System.out.println("ea: " + OrbitalMath.Mean2Eccentric(1.126815678, .000683));
        System.out.println("ta: " + OrbitalMath.Mean2True(1.126815678, .000683));
        System.out.println("pos: " + toIJK(new Vector(2910730.115,6138964.276,0), Math.toRadians(coe.aop), Math.toRadians(coe.inc), Math.toRadians(coe.lan)).toString());
        System.out.println("vel: " + toIJK(new Vector(-6920.006959,3286.284615,0), Math.toRadians(coe.aop), Math.toRadians(coe.inc), Math.toRadians(coe.lan)).toString());

        Vector vexp = new Vector(-1222362.021127256, 4678181.947355996, 4772805.981926173);
        Vector vact = new Vector(-1225493.0931284525, 4676792.423899453, 4769592.7817697795);
        double alpha = Math.acos(vexp.dot(vact) / (vexp.mag() * vact.mag()));
        System.out.println(Math.toDegrees(alpha));
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

    public static Vector toIJK(Vector vec, double aop, double inc, double lan) {
        double v1 = vec.x() * (Math.cos(aop)*Math.cos(lan) - Math.sin(aop)*Math.cos(inc)*Math.sin(lan))
                - vec.y() * (Math.sin(aop)*Math.cos(lan) + Math.cos(aop)*Math.cos(inc)*Math.sin(lan));
        double v2 = vec.x() * (Math.cos(aop)*Math.sin(lan) + Math.sin(aop)*Math.cos(inc)*Math.cos(lan))
                + vec.y() * (Math.cos(aop)*Math.cos(inc)*Math.cos(lan) - Math.sin(aop)*Math.sin(lan));
        double v3 = vec.x() * (Math.sin(aop)*Math.sin(inc))
                + vec.y() * (Math.cos(aop)*Math.sin(inc));
        return new Vector(v1, v2, v3);
    }

}