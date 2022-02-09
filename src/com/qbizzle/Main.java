package com.qbizzle;

import com.qbizzle.exception.InvalidTLEException;
import com.qbizzle.math.OrbitalMath;
import com.qbizzle.math.Vector;
import com.qbizzle.orbit.TLE;
import com.qbizzle.time.JD;
import com.qbizzle.tracking.AltAz;
import com.qbizzle.tracking.GeoPosition;
import com.qbizzle.tracking.Tracker;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

import static com.qbizzle.math.OrbitalMath.EARTH_EQUITORIAL_RADIUS;
import static com.qbizzle.math.OrbitalMath.EARTH_POLAR_RADIUS;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;

public class Main {

    Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InvalidTLEException, IOException {
        String strZarya = """
                ISS (ZARYA)            \s
                1 25544U 98067A   22030.51179398  .00005765  00000+0  11002-3 0  9999
                2 25544  51.6444 298.3935 0006761  77.9892 281.4353 15.49702707323823""";
        TLE tleZarya = new TLE(strZarya);
//        JD currentPosTime = new JD(1, 30, 2022, 23, 42, 0.0); // UTC
//        StateVectors state = new StateVectors(tleZarya, currentPosTime.Difference(new JD(tleZarya)));
//        System.out.println(state.Position());
//        double siderealTime = SiderealTime.siderealTime(currentPosTime, 0.0);
//        System.out.println("sidereal time: " + siderealTime);
//        double offsetAngle = siderealTime / 24.0 * 360.0;
//        System.out.println("offset angle: " + offsetAngle);
//
//        Vector issPos = Rotate(state.Position(), -offsetAngle);
//        System.out.println("iss position: " + issPos.toString());
//
//        double [] latlng = toLatLng(issPos);
//        System.out.println("declination: " + Math.toDegrees(latlng[0]));
//        System.out.println("lat: " + Math.toDegrees( geocentricToGeodetic(latlng[0]) ));
//        System.out.println("long: " + Math.toDegrees(latlng[1]));
//
//        GeoPosition geo1 = tracker.getPositionAt(tleZarya, 0.47570602);
//        GeoPosition geo2 = tracker.getPositionAt(tleZarya, currentPosTime);
//
//        System.out.println("geo1 lat: " + geo1.getLatitude());
//        System.out.println("geo1 lng: " + geo1.getLongitude());
//        System.out.println("geo2 lat: " + geo2.getLatitude());
//        System.out.println("geo2 lng: " + geo2.getLongitude());

//        double dt = 10 / 86400.0; // days
//        double timeToPlot = 2.0 / tleZarya.MeanMotion(); // days
//        int numItr = (int)(timeToPlot / dt);
//
//        JD startTime = new JD(tleZarya);
//        String filename = "output/zarya_2orbit_test1.csv";
//        FileWriter writer = new FileWriter(filename);
//        writer.write("latitude, longitude\n");
//        for (int i = 0; i < numItr; i++) {
//            JD currentTime = startTime.Future(dt * i);
//            StateVectors state = new StateVectors(tleZarya, dt * i);
////            double offsetAngle = ST(currentTime, 0.0) / 24.0 * 360.0;
//            double offsetAngle = SiderealTime.siderealTime(currentTime, 0.0) / 24.0 * 360.0;
//            Vector currentPosition = Rotate(state.Position(), -offsetAngle);
//            double[] latlng = toLatLng(currentPosition);
//            writer.write(Math.toDegrees(latlng[0]) + ", " + Math.toDegrees(latlng[1]) + '\n');
//            if (i % 10 == 0) writer.flush();
//        }
//
//        GeoPosition[] zaryaGroundTrack = getGroundTrack(tleZarya, timeToPlot, 10 / 86400.0);
//        FileWriter writer2 = new FileWriter("output/zarya_2orbit_test2.csv");
//        writer2.write("latitude, longitude\n");
//        for (int i = 0; i < Array.getLength(zaryaGroundTrack); i++) {
//            writer2.write(zaryaGroundTrack[i].getLatitude() + ", " + zaryaGroundTrack[i].getLongitude() + '\n');
//            if (i % 10 == 0) writer2.flush();
//        }

        GeoPosition geo = Tracker.getGeoPositionAt(tleZarya, 0.0);
        System.out.println("geo: " + geo);
        Vector sezPos = Tracker.getSEZPosition(tleZarya, 0.0, geo);
        System.out.println("sezPos: " + sezPos);
        AltAz altaz = Tracker.getAltAz(tleZarya, new JD(tleZarya), geo);
        System.out.println("altitude: " + altaz.getAltitude());
        System.out.println("azimuth: " + altaz.getAzimuth());

    }

    public static void noaaTest() {
        String strTLE = """
                NOAA 20 [+]            \s
                1 43013U 17073A   22032.53991112  .00000015  00000+0  27859-4 0  9999
                2 43013  98.7448 332.9363 0001514  88.1570 271.9780 14.19526485217944""";
        String strFutureTLE = """
                NOAA 20 [+]            \s
                1 43013U 17073A   22032.82185385  .00000017  00000+0  28794-4 0  9999
                2 43013  98.7449 333.2147 0001513  87.9741 272.1608 14.19526540217983""";
        TLE tle = new TLE(strTLE);
        TLE futureTLE = new TLE(strFutureTLE);
        GeoPosition guess = Tracker.getGeoPositionAt(tle, new JD(futureTLE));
        GeoPosition actual = Tracker.getGeoPositionAt(futureTLE, 0.0);
        System.out.println("guess: " + guess);
        System.out.println("actual: " + actual);

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

    public static double geocentricToGeodetic(double lat) {
        final double flattening = (EARTH_EQUITORIAL_RADIUS - EARTH_POLAR_RADIUS) / EARTH_EQUITORIAL_RADIUS;
        double lhs = Math.tan(lat) / Math.pow(1.0 - flattening, 2);
        return Math.atan(lhs);
    }

}