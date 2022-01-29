package com.qbizzle;

import com.qbizzle.exception.InvalidTLEException;
import com.qbizzle.math.JD;
import com.qbizzle.math.OrbitalMath;
import com.qbizzle.math.Vector;
import com.qbizzle.orbit.StateVectors;
import com.qbizzle.orbit.TLE;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;

public class Main {

    Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InvalidTLEException, IOException {
        String strZarya = "ISS (ZARYA)             \n" +
                "1 25544U 98067A   22027.75547926  .00007563  00000+0  14194-3 0  9990\n" +
                "2 25544  51.6451 312.0370 0006903  67.9238  24.2396 15.49669141323407";
        TLE tleZarya = new TLE(strZarya);
//        JD currentPosTime = new JD(1, 28, 2022, 18, 0, 0.0); // UTC
//        StateVectors state = new StateVectors(tleZarya, currentPosTime.Difference(new JD(tleZarya)));
//        System.out.println(state.Position());
//        double siderealTime = ST(currentPosTime, 0.0);
//        System.out.println("sidereal time: " + siderealTime);
//        double offsetAngle = siderealTime / 24.0 * 360.0;
//        System.out.println("offset angle: " + offsetAngle);
//
//        Vector issPos = Rotate(state.Position(), -offsetAngle);
//        System.out.println("iss position: " + issPos.toString());
//
//        double [] latlng = toLatLng(issPos);
//        System.out.println("lat: " + Math.toDegrees(latlng[0]));
//        System.out.println("long: " + Math.toDegrees(latlng[1]));

        double dt = 10 / 86400.0; // days
        double timeToPlot = 1.0; //2.0 / tleZarya.MeanMotion(); // days
        int numItr = (int)(timeToPlot / dt);

        JD startTime = new JD(tleZarya);
        String filename = "output/iss_1day_10sec.csv";
        FileWriter writer = new FileWriter(filename);
        writer.write("latitude, longitude\n");
        for (int i = 0; i < numItr; i++) {
            JD currentTime = startTime.Future(dt * i);
            StateVectors state = new StateVectors(tleZarya, dt * i);
            double offsetAngle = ST(currentTime, 0.0) / 24.0 * 360.0;
            Vector currentPosition = Rotate(state.Position(), -offsetAngle);
            double[] latlng = toLatLng(currentPosition);
            writer.write(Math.toDegrees(latlng[0]) + ", " + Math.toDegrees(latlng[1]) + '\n');
        }


    }

    //14:54

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

    public static double[] toLatLng(Vector position) {
        double xyMag = sqrt(Math.pow(position.x(), 2) + Math.pow(position.y(), 2));
        double lat = Math.atan2(position.z(), xyMag);
        double lng = OrbitalMath.atan2(position.y(), position.x());
        if (lng > Math.PI) lng -= 2*Math.PI;

        return new double[]{lat, lng};
    }

}