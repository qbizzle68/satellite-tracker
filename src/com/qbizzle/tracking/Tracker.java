/** @file
 * This file contains a static class with methods to track or compute future
 * positions of satellites, as well as predict future overhead passes.
 */

package com.qbizzle.tracking;

import com.qbizzle.math.Matrix;
import com.qbizzle.math.OrbitalMath;
import com.qbizzle.math.Vector;
import com.qbizzle.orbit.StateVectors;
import com.qbizzle.orbit.TLE;
import com.qbizzle.referenceframe.Axis;
import com.qbizzle.referenceframe.EulerAngles;
import com.qbizzle.referenceframe.EulerOrderList;
import com.qbizzle.rotation.Rotation;
import com.qbizzle.time.JD;
import com.qbizzle.time.SiderealTime;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;

/** This is a static class with methods that will compute current or predict future positions
 * of satellites, and use that information to plan overhead passes. Many other methods involved
 * in tracking and determining if satellites are visible can also be found here.
 */
public class Tracker {

    /** Converts an angle in degree notation to hour-minute-seconds notation */
    private static final double HOURS_PER_DEGREE = 360.0 / 24.0;

    /** Computes the GeoPosition in which the satellite is directly overhead at
     * time dt in the past/future.
     * @param tle The TLE of the satellite to track.
     * @param dt The time of the desired position in solar days.
     * @return The GeoPosition of the satellite.
     */
    public static Coordinates getGeoPositionAt(TLE tle, double dt) {
        return getGeoPositionAt(tle, new JD(tle).Future(dt));
    }

    /** Computes the GeoPosition in which the satellite is directly overhead in the
     * past/future.
     * @param tle The TLE of the satellite to track.
     * @param t1 The time to compute the satellites position, in UTC.
     * @return The GeoPosition of the satellite.
     */
    public static Coordinates getGeoPositionAt(TLE tle, JD t1) {
//        StateVectors stateAtT1 = new StateVectors(tle, t1.Difference(new JD(tle)));
        // todo: check which ephemeris model to use before calling
        StateVectors stateAtT1 = SGP4.Propagate(tle, t1);
        double earthOffsetAngle = SiderealTime.EarthOffsetAngle(t1);
        Vector positionAtT1 = Rotation.RotateFrom(Axis.Direction.Z, -earthOffsetAngle, stateAtT1.Position());
        return new Coordinates(positionAtT1);
    }

    // may make getGeoPositionAt with COE, JD t0, JD t1 args.

    /** Computes an array of GeoPositions of a satellite over a given period.
     * @param tle The TLE of the satellite to track.
     * @param dt The amount of time to track in solar days.
     * @param interval The interval of time between successive tracks in solar days.
     * @return An array of GeoPositions making up the ground track.
     */
    @SuppressWarnings("unused")
    public static Coordinates[] getGroundTrack(TLE tle, double dt, double interval) {
        return getGroundTrack(tle, new JD(tle).Future(dt), interval);
    }

    /** Computes an array of GeoPositions of a satellite over a given period.
     * @param tle The TLE of the satellite to track.
     * @param t1 The time to track to in UTC.
     * @param interval The interval of time between successive tracks in solar days.
     * @return An array of GeoPositions making up the ground track.
     */
    public static Coordinates[] getGroundTrack(TLE tle, JD t1, double interval) {
        JD startTime = new JD(tle);
        int numIterations = (int)(t1.Difference(startTime) / interval) + 1;
        Coordinates[] arrGeoPos = new Coordinates[numIterations];

        for (int i = 0; i < numIterations - 1; i++) {
            JD currentTime = startTime.Future(interval * i);
            arrGeoPos[i] = getGeoPositionAt(tle, currentTime);
        }
        // The last interval will most likely not be of length interval, but should still be iterated.
        arrGeoPos[numIterations-1] = getGeoPositionAt(tle, t1);
        return arrGeoPos;
    }

    /** Prints an array of GeoPositions of a satellite track to a CSV file.
     * @param tle The TLE of the satellite to track.
     * @param dt The amount of time to track in solar days.
     * @param interval The interval of time between successive tracks in solar days.
     * @param filename The file to print to, should contain .CSV extensions.
     * @return True if the print was successful, false if otherwise.
     * @throws IOException From FileWriter.
     */
    @SuppressWarnings("unused")
    public static boolean plotGroundTrack(TLE tle, double dt, double interval, String filename) throws IOException {
        return plotGroundTrack(tle, new JD(tle).Future(dt), interval, filename);
    }

    /** Prints an array of GeoPositions of a satellite track to a CSV file.
     * @param tle The TLE of the satellite to track.
     * @param t1 The time to track to in UTC.
     * @param interval The interval of time between successive tracks in solar days.
     * @param filename The file to print to, should contain .CSV extensions.
     * @return True if the print was successful, false if otherwise.
     * @throws IOException From FileWriter.
     * @todo find out if we can handle any exceptions for FileWriter, or need to do checks to find out
     *   if the file is being written. If no checks exist theres really no reason to return a boolean
     */
    public static boolean plotGroundTrack(TLE tle, JD t1, double interval, String filename) throws IOException {
        final int flushLimit = 10; // this is just a guess right now, previous files have been failing a little after 1000
        FileWriter writer = new FileWriter(filename);
        Coordinates[] arrGroundTrack = getGroundTrack(tle, t1, interval);
        writer.write("latitude, longitude\n");
        for (int i = 0; i < Array.getLength(arrGroundTrack); i++) {
            writer.write(arrGroundTrack[i].getLatitude() + ", " + arrGroundTrack[i].getLongitude() + '\n');
            if (i % flushLimit == 0) writer.flush();
        }
        return true;
    }

    public static Vector getSEZPosition(TLE tle, double dt, Coordinates geoPos) {
        return getSEZPosition(tle, new JD(tle).Future(dt), geoPos);
    }

    public static Vector getSEZPosition(TLE tle, JD t1, Coordinates geoPos) {
        // todo: check which ephemeris model to use before calling
        StateVectors stateAtT1 = SGP4.Propagate(tle, t1);
        double localSiderealTime = SiderealTime.LST(t1, geoPos.getLongitude()) * HOURS_PER_DEGREE;
        // todo package this into a method
        Vector geoPosVector = new Vector(
                // todo get an accurate vector length, dependent on latitude and elevation
                OrbitalMath.EARTH_EQUITORIAL_RADIUS * Math.cos( Math.toRadians(geoPos.getLatitude()) ) * Math.cos( Math.toRadians(localSiderealTime) ),
                OrbitalMath.EARTH_EQUITORIAL_RADIUS * Math.cos( Math.toRadians(geoPos.getLatitude()) ) * Math.sin( Math.toRadians(localSiderealTime) ),
                OrbitalMath.EARTH_EQUITORIAL_RADIUS * Math.sin( Math.toRadians(geoPos.getLatitude()) )
        );
        Matrix sezToIJK = Rotation.getEulerMatrix(
                EulerOrderList.ZYX,
                new EulerAngles(localSiderealTime, 90-geoPos.getLatitude(), 0)
        );
        return rotateTranslate(sezToIJK, geoPosVector, stateAtT1.Position());
    }

    public static AltAz getAltAz(TLE tle, JD t1, Coordinates geoPos) {
        Vector sezPosition = getSEZPosition(tle, t1, geoPos);
        double xyMag = Math.sqrt( Math.pow(sezPosition.x(), 2) + Math.pow(sezPosition.y(), 2) );
        return new AltAz(
                Math.toDegrees( Math.asin(sezPosition.z() / sezPosition.mag())),
                Math.toDegrees( OrbitalMath.atan2(sezPosition.y(), -sezPosition.x()) )
        );
    }

    public static Boolean isAboveHorizon(TLE tle, JD t, Coordinates geoPosition) {
        AltAz altaz = getAltAz(tle, t, geoPosition);
        return (altaz.getAltitude() > 0);
    }

    // rotation is from inertial to rotated, offset in inertial frame pointing from inertial origin to translated origin.
    static private Vector rotateTranslate(Matrix rot, Vector offset, Vector original) {
        Vector rotOrig = rot.transpose().mult(original);
        Vector rotOffset = rot.transpose().mult(offset);
        return rotOrig.minus(rotOffset);
    }

}