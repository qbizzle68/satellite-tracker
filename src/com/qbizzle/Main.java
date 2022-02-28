package com.qbizzle;

import com.qbizzle.math.OrbitalMath;
import com.qbizzle.math.Vector;
import com.qbizzle.orbit.TLE;
import com.qbizzle.time.JD;
import com.qbizzle.tracking.Coordinates;
import com.qbizzle.tracking.SatellitePass;
import com.qbizzle.tracking.Tracker;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main {

    Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException, InterruptedException {
//        String strZarya = """
//                ISS (ZARYA)            \s
//                1 25544U 98067A   22057.61893286  .00025540  00000+0  46219-3 0  9999
//                2 25544  51.6422 164.1505 0005487 193.8783 302.2559 15.49481576328039""";
//        TLE tleZarya = new TLE(strZarya);
        Coordinates hutch = new Coordinates(38.060017, -97.930495);
//        JD viewTime = new JD(2, 27, 2022, 10, 43, 0);

//        SatellitePass satPass = Tracker.getPassInfo(tleZarya, viewTime, hutch);
//        System.out.println(satPass);

        String URL = "https://celestrak.com/NORAD/elements/gp.php?NAME=ISS%20(ZARYA)&FORMAT=TLE";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(URL))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        System.out.println(response.body());

        TLE tle = new TLE(response.body());

        JD startTime = new JD(2, 28, 2022, 0, 0, 0, -6);
        java.util.Vector<SatellitePass> passList = getPasses2(tle, startTime, startTime.Future(2.0), hutch);
        System.out.println(passList.size());
        for (SatellitePass pass :
                passList) {
            System.out.println(pass);
        }

    }

    static java.util.Vector<SatellitePass> getPasses2(TLE tle, JD startTime, JD endTime, Coordinates geoPos) {
        final double dt = 10 / 86400.0; // 10 seconds
        java.util.Vector<SatellitePass> passList = new java.util.Vector<>();
        JD currentTime = startTime;
        SatellitePass satPass;
        do {
            try {
                satPass = Tracker.getPassInfo(tle, currentTime, geoPos);
                passList.add(satPass);
                // todo: because the next guess is programed to guess 10 minutes into the past, it will find this same pass over and over, unless we jump more than 10 minutes into the future
                currentTime = satPass.getSetTime().Future(11/1440.0);
            } catch (Exception e) {
                //todo: add a step method to move current JD forward or back in time
                currentTime = currentTime.Future(dt);
            }
            //todo: add comparison methods for JD
        } while(currentTime.Value() < endTime.Value());
        return passList;
    }

    static public Coordinates getCelestialCoordinates(Vector pos) {//, JD t1, Coordinates coords) {
//        Vector pos = getSEZPosition(tle/, t1, coords);
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

}