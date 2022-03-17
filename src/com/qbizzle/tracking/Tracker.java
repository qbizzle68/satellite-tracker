/** @file
 * This file contains a static class with methods to track or compute future
 * positions of satellites, as well as predict future overhead passes.
 */

package com.qbizzle.tracking;

import com.qbizzle.coordinates.CelestialCoordinates;
import com.qbizzle.coordinates.GeoPosition;
import com.qbizzle.exception.DaylightPassException;
import com.qbizzle.exception.NoLightException;
import com.qbizzle.exception.NoPassException;
import com.qbizzle.math.Matrix;
import com.qbizzle.math.OrbitalMath;
import com.qbizzle.math.Vector;
import com.qbizzle.orbit.Eclipse;
import com.qbizzle.orbit.StateVectors;
import com.qbizzle.orbit.TLE;
import com.qbizzle.referenceframe.Axis;
import com.qbizzle.referenceframe.EulerAngles;
import com.qbizzle.referenceframe.EulerOrderList;
import com.qbizzle.rotation.Rotation;
import com.qbizzle.satellite.Satellite;
import com.qbizzle.time.JD;
import com.qbizzle.time.SiderealTime;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Iterator;

/** This is a static class with methods that will compute current or predict future positions
 * of satellites, and use that information to plan overhead passes. Many other methods involved
 * in tracking and determining if satellites are visible can also be found here.
 */
@SuppressWarnings("unused")
public class Tracker {
    static private final double squeezeEpsilon = 1.0 / 8640000.0; // 1/10th of a second

    /** Converts an angle in degree notation to hour-minute-seconds notation */
    private static final double DEGREES_PER_HOUR = 15.0;

    /** Computes the GeoPosition in which the satellite is directly overhead at
     * time dt in the past/future.
     * @param satellite The satellite to track.
     * @param dt        The time of the desired position in solar days.
     * @return          The GeoPosition of the satellite.
     */
//    public static GeoPosition getGeoPositionAt(TLE tle, double dt) {
    public static GeoPosition getGeoPositionAt(Satellite satellite, double dt) {
//    public static Coordinates getGeoPositionAt(TLE tle, double dt) {
//        return getGeoPositionAt(tle, new JD(tle).Future(dt));
        return getGeoPositionAt(satellite, new JD(satellite.getTle()).future(dt));
    }

    /** Computes the GeoPosition in which the satellite is directly overhead in the
     * past/future.
     * @param satellite The satellite to track.
     * @param time      The time to compute the satellites position, in UTC.
     * @return          The GeoPosition of the satellite.
     */
//    public static Coordinates getGeoPositionAt(TLE tle, JD t1) {
//    public static GeoPosition getGeoPositionAt(TLE tle, JD t) {
    public static GeoPosition getGeoPositionAt(Satellite satellite, JD time) {
        // todo: check which ephemeris model to use before calling
//        StateVectors stateAtT1 = SGP4.Propagate(tle, t);
        StateVectors stateAtT1 = satellite.getState(time);
        double earthOffsetAngle = SiderealTime.earthOffsetAngle(time);
        Vector positionAtT1 = Rotation.rotateFrom(Axis.Direction.Z, -earthOffsetAngle, stateAtT1.position());
//        return new Coordinates(positionAtT1);
        return new GeoPosition(positionAtT1);
    }

    /** Computes an array of GeoPositions of a satellite over a given period.
     * @param satellite The satellite to track.
     * @param dt        The amount of time to track in solar days.
     * @param interval  The interval of time between successive tracks in solar days.
     * @return          An array of GeoPositions making up the ground track.
     */
//    public static Coordinates[] getGroundTrack(TLE tle, double dt, double interval) {
//    public static GeoPosition[] getGroundTrack(TLE tle, double dt, double interval) {
    public static GeoPosition[] getGroundTrack(Satellite satellite, double dt, double interval) {
//        return getGroundTrack(tle, new JD(tle).Future(dt), interval);
        return getGroundTrack(satellite, new JD(satellite.getTle()).future(dt), interval);
    }

    /** Computes an array of GeoPositions of a satellite over a given period.
     * @param satellite The TLE of the satellite to track.
     * @param time      The time to track to in UTC.
     * @param interval  The interval of time between successive tracks in solar days.
     * @return          An array of GeoPositions making up the ground track.
     */
//    public static Coordinates[] getGroundTrack(TLE tle, JD t1, double interval) {
//    public static GeoPosition[] getGroundTrack(TLE tle, JD t, double interval) {
    public static GeoPosition[] getGroundTrack(Satellite satellite, JD time, double interval) {
//        JD startTime = new JD(tle);
        JD startTime = new JD(satellite.getTle());
        int numIterations = (int)(time.difference(startTime) / interval) + 1;
        GeoPosition[] arrGeoPos = new GeoPosition[numIterations];
//        Coordinates[] arrGeoPos = new Coordinates[numIterations];

        for (int i = 0; i < numIterations - 1; i++) {
            JD currentTime = startTime.future(interval * i);
            arrGeoPos[i] = getGeoPositionAt(satellite, currentTime);
        }
        // The last interval will most likely not be of length interval, but should still be iterated.
        arrGeoPos[numIterations-1] = getGeoPositionAt(satellite, time);
        return arrGeoPos;
    }

    /** Prints an array of GeoPositions of a satellite track to a CSV file.
     * @param satellite The TLE of the satellite to track.
     * @param dt        The amount of time to track in solar days.
     * @param interval  The interval of time between successive tracks in solar days.
     * @param filename  The file to print to, should contain .CSV extensions.
     * @return          True if the print was successful, false if otherwise.
     * @throws          IOException From FileWriter.
     */
//    public static boolean plotGroundTrack(TLE tle, double dt, double interval, String filename) throws IOException {
    public static boolean plotGroundTrack(Satellite satellite, double dt, double interval, String filename) throws IOException {
//        return plotGroundTrack(tle, new JD(tle).Future(dt), interval, filename);
        return plotGroundTrack(satellite, new JD(satellite.getTle()).future(dt), interval, filename);
    }

    /** Prints an array of GeoPositions of a satellite track to a CSV file.
     * @param satellite The satellite to track.
     * @param time      The time to track to in UTC.
     * @param interval  The interval of time between successive tracks in solar days.
     * @param filename  The file to print to, should contain .CSV extensions.
     * @return          True if the print was successful, false if otherwise.
     * @throws          IOException From FileWriter.
     * @todo find out if we can handle any exceptions for FileWriter, or need to do checks to find out
     *   if the file is being written. If no checks exist theres really no reason to return a boolean
     */
//    public static boolean plotGroundTrack(TLE tle, JD t, double interval, String filename) throws IOException {
    public static boolean plotGroundTrack(Satellite satellite, JD time, double interval, String filename) throws IOException {
        final int flushLimit = 10; // this is just a guess right now, previous files have been failing a little after 1000
        FileWriter writer = new FileWriter(filename);
//        Coordinates[] arrGroundTrack = getGroundTrack(tle, t1, interval);
//        GeoPosition[] arrGroundTrack = getGroundTrack(tle, t, interval);
        GeoPosition[] arrGroundTrack = getGroundTrack(satellite, time, interval);
        writer.write("latitude, longitude\n");
        for (int i = 0; i < Array.getLength(arrGroundTrack); i++) {
            writer.write(arrGroundTrack[i].getLatitude() + ", " + arrGroundTrack[i].getLongitude() + '\n');
            if (i % flushLimit == 0) writer.flush();
        }
        return true;
    }

    /**
     * Computes the position of a satellite in a topocentric reference frame.
     * @param satellite TLE of the satellite.
     * @param dt        Change in time relative to the @p tle epoch.
     * @param geoPosition
     *                  GeoPosition which corresponds to the center of the reference frame.
     * @return          The position vector in SEZ reference frame.
     */
//    public static Vector getSEZPosition(TLE tle, double dt, Coordinates geoPos) {
//    public static Vector getSEZPosition(TLE tle, double dt, GeoPosition geoPosition) {
    public static Vector getSEZPosition(Satellite satellite, double dt, GeoPosition geoPosition) {
//        return getSEZPosition(tle, new JD(tle).Future(dt), geoPos);
//        return getSEZPosition(tle, new JD(tle).Future(dt), geoPosition);
        return getSEZPosition(satellite, new JD(satellite.getTle()).future(dt), geoPosition);
    }

    /**
     * Computes the position of a satellite in a topocentric reference frame.
     * @param satellite TLE of the satellite.
     * @param time      Time in which to find the satellite position.
     * @param geoPosition
     *                  GeoPosition which corresponds to the center of the reference frame.
     * @return          The position vector in SEZ reference frame.
     */
//    public static Vector getSEZPosition(TLE tle, JD t1, Coordinates geoPos) {
//    public static Vector getSEZPosition(TLE tle, JD t, GeoPosition geoPosition) {
    public static Vector getSEZPosition(Satellite satellite, JD time, GeoPosition geoPosition) {
        // todo: check which ephemeris model to use before calling
        return getSEZPosition(
//                SGP4.Propagate(tle, t).Position(),
                satellite.getState(time).position(),
                time,
                geoPosition
        );
    }

    /**
     * Converts a position vector from a geocentric reference frame to a topocentric
     * reference frame.
     * @param position      Position vector of the satellite in the earth centered reference frame.
     * @param time          Time in which the satellite occupies this position. This is needed to find
     *                      the earth rotation offset from the celestial coordinate system.
     * @param geoPosition   GeoPosition which corresponds to the center of the reference frame.
     * @return              The position vector in SEZ reference frame.
     */
//    public static Vector getSEZPosition(Vector position, JD t1, Coordinates geoPos) {
    public static Vector getSEZPosition(Vector position, JD time, GeoPosition geoPosition) {
//        double localSiderealTime = SiderealTime.LST(t1, geoPos.getLongitude()) * HOURS_PER_DEGREE;
        double localSiderealTime = SiderealTime.getLocalSiderealTime(time, geoPosition.getLongitude()) * DEGREES_PER_HOUR;
//        Vector geoPosVector = getToposPosition(t1, geoPos);
        Vector geoPositionVector = getToposPosition(time, geoPosition);
        Matrix sezToIJK = Rotation.getEulerMatrix(
                EulerOrderList.ZYX,
                new EulerAngles(
                        localSiderealTime,
                        90-geoPosition.getLatitude(),
                        0
                )
        );
        Vector tmp = rotateTranslate(sezToIJK, geoPositionVector, position);
//        return rotateTranslate(sezToIJK, geoPositionVector, position);
        return tmp;
    }

    /**
     * Computes the position vector of a GeoPosition.
     * @param time          Time the position vector is needed. This is necessary for adjusting
     *                      for the earth's rotation offset.
     * @param geoPosition   The GeoPosition corresponding to the center of the reference frame.
     * @return              The position vector in earth centric reference frame.
     */
//    public static Vector getToposPosition(JD t, Coordinates topos) {
    public static Vector getToposPosition(JD time, GeoPosition geoPosition) {
        // todo: account for elevation with this radius
//        double radiusAtLat = Coordinates.radiusAtLatitude(topos.getLatitude());
        double radiusAtLat = GeoPosition.radiusAtLatitude(geoPosition.getLatitude());
//        double geocentricLat = Coordinates.geodeticToGeocentric(topos.getLatitude());
        double geocentricLat = GeoPosition.geodeticToGeocentric(geoPosition.getLatitude());
//        double localSiderealTime = SiderealTime.LST(t, topos.getLongitude()) * HOURS_PER_DEGREE;
        double localSiderealTime = SiderealTime.getLocalSiderealTime(time, geoPosition.getLongitude()) * DEGREES_PER_HOUR;
        return new Vector(
                radiusAtLat * Math.cos( Math.toRadians(geocentricLat) ) * Math.cos( Math.toRadians(localSiderealTime) ),
                radiusAtLat * Math.cos( Math.toRadians(geocentricLat) ) * Math.sin( Math.toRadians(localSiderealTime) ),
                radiusAtLat * Math.sin( Math.toRadians(geocentricLat) )
        );
    }

    /**
     * Generates an AltAz object referencing the altitude and azimuth of a satellite at a
     * given time.
     * @param satellite     Satellite to track.
     * @param time          Time to find the position.
     * @param geoPosition   GeoPosition to find the relative altitude and azimuth for.
     * @return              An AltAz object with the epoch set as @p t.
     */
//    public static AltAz getAltAz(TLE tle, JD t, Coordinates geoPos) {
//    public static AltAz getAltAz(TLE tle, JD t, GeoPosition geoPosition) {
    public static AltAz getAltAz(Satellite satellite, JD time, GeoPosition geoPosition) {
        return getAltAz(
//                getSEZPosition(tle, t, geoPosition),
                getSEZPosition(satellite, time, geoPosition),
                time
        );
    }

    /**
     * Converts a position vector in a topocentric reference frame to a relative
     * altitude and azimuth.
     * @param sezPosition   Position vector in SEZ reference frame.
     * @param time          Time associated with the position.
     * @return              An AltAz object with the epoch set as @p t.
     */
    public static AltAz getAltAz(Vector sezPosition, JD time) {
        return new AltAz(
                Math.toDegrees( Math.asin(sezPosition.z() / sezPosition.mag())),
                Math.toDegrees( OrbitalMath.atan2(sezPosition.y(), -sezPosition.x()) ),
                time
        );
    }

    /* THIS IS A TEMPORARY FIX UNTIL THE 'SQEEZE' METHODS ARE REPLACED */
    public static AltAz getAltAz(TLE tle, JD time, GeoPosition geoPosition) {
        return getAltAz(
                getSEZPosition(new Satellite(tle), time, geoPosition),
                time
        );
    }

    /**
     * Determines if a satellite is above the horizon for a given GeoPosition and time.
     * @param satellite     Satellite to track.
     * @param time          Time the satellite would be above the horizon.
     * @param geoPosition   GeoPosition for determining the horizon.
     * @return              True if the altitude of the satellite is greater than 0,
     *                      false if otherwise.
     */
//    public static boolean isAboveHorizon(TLE tle, JD t, Coordinates geoPosition) {
//        public static boolean isAboveHorizon(TLE tle, JD t, GeoPosition geoPosition) {
    public static boolean isAboveHorizon(Satellite satellite, JD time, GeoPosition geoPosition) {
        // todo: wouldn't it be more efficient if we just called for SEZ position and returned (sezPos.z() > 0)
//        AltAz altaz = getAltAz(tle, t, geoPosition);
        return (getSEZPosition(satellite, time, geoPosition).z() > 0);
//        return (altaz.getAltitude() > 0);
    }

    /**
     * Computes the pass information for a satellite pass.
     * @param tle TLE of the satellite.
     * @param passTime  If there is a valid pass, this value must be between the pass rise
     *                  and set times. There may be some marginal error allowed but success
     *                  cannot be guaranteed.
     * @param geoPosition
     *                  The GeoPosition for the pass.
     * @return          A SatellitePass object containing the pass info.
     * @throws NoPassException
     *                  If the satellite is not above the horizon during this time.
     * @throws NoLightException
     *                  If the satellite is never in sunlight during a pass.
     * @throws DaylightPassException
     *                  If the pass occurs during daylight and is not visible.
     */
    public static SatellitePass getPassInfo(TLE tle, JD passTime, GeoPosition geoPosition) {
//    public static SatellitePass getPassInfo(Satellite satellite, JD passTime, GeoPosition geoPosition) {
//        todo: how to we make a better guess than 10 minutes?
//        todo: checking for daylight first should cut this processing time by half when used by getPasses
        AltAz rise = riseSqueeze(tle, passTime.future(-10.0 / 1440.0), passTime, geoPosition, null);
        AltAz set = setSqueeze(tle, rise.getEpoch(), passTime.future(10.0 / 1440.0), geoPosition, null);
        AltAz first = firstSqueeze(tle, rise.getEpoch(), set.getEpoch(), geoPosition);
        AltAz last = lastSqueeze(tle, first.getEpoch(), set.getEpoch(), geoPosition, null);

        JD startEpoch = (rise.getEpoch().value() < first.getEpoch().value()) ? first.getEpoch() : rise.getEpoch();
        JD finishEpoch = (set.getEpoch().value() < last.getEpoch().value()) ? set.getEpoch() : last.getEpoch();
        if (Sun.getTwilightType(passTime, geoPosition).ordinal() >= Sun.TwilightType.Nautical.ordinal()) {
            return new SatellitePass(
                    rise, set, first, last,
                    maxSqueeze(tle, startEpoch, finishEpoch, geoPosition, null)
            );
        }
        else throw new DaylightPassException("Pass not visible due to sunlight.");
    }
    public static SatellitePass getPassInfo3(Satellite satellite, JD passTime, GeoPosition geoPosition) {
        AltAz rise = riseSqueeze2(satellite, passTime.future(-15.0 / 1440.0), passTime, geoPosition);
        AltAz set = setSqueeze2(satellite, rise.getEpoch(), passTime.future(15.0 / 1440.0), geoPosition);
        AltAz first = firstSqueeze2(satellite, rise.getEpoch(), set.getEpoch(), geoPosition);
        AltAz last = lastSqueeze2(satellite, first.getEpoch(), set.getEpoch(), geoPosition);

        JD startEpoch = (rise.getEpoch().value() < first.getEpoch().value()) ? first.getEpoch() : rise.getEpoch();
        JD finishEpoch = (set.getEpoch().value() < last.getEpoch().value()) ? set.getEpoch() : last.getEpoch();
        if (Sun.getTwilightType(passTime, geoPosition).ordinal() >= Sun.TwilightType.Nautical.ordinal()) {
            return new SatellitePass(
                    rise, set, first, last,
                    maxSqueeze2(satellite, startEpoch, finishEpoch, geoPosition)
            );
        }
        else throw new DaylightPassException("Pass not visible due to sunlight.");
    }

    public static SatellitePass getPassInfo2(Satellite satellite, JD passTime, GeoPosition geoPosition) {
        Vector pos = getSEZPosition(satellite, passTime, geoPosition);
        if (pos.z() < 0)
            throw new NoPassException("No overhead pass at " + passTime.date());
//        can we just call satellite.getRecentState() to compute the AltAz's.
        JD riseTime = getRiseTime(satellite, passTime, geoPosition);
        JD setTime = getSetTime(satellite, passTime, geoPosition);
        JD visibleTime, disappearTime;
//        boolean riseEclipsed = Eclipse.isEclipsed(satellite, riseTime);
//        boolean setEclipsed = Eclipse.isEclipsed(satellite, setTime);
//        boolean passEclipsed = Eclipse.isEclipsed(satellite, passTime);
        boolean riseEclipsed = true;
        boolean setEclipsed = true;
        boolean passEclipsed = true;
        if (riseEclipsed && setEclipsed)    //  never visible
            throw new DaylightPassException("Pass not visible due to sunlight.");
        else if (!riseEclipsed) {   //  visible when rising
            visibleTime = riseTime;
            if (!setEclipsed)
                disappearTime = setTime;
            else if (!passEclipsed)
                disappearTime = getLastVisibleTime(satellite, passTime, setTime, geoPosition);
            else
                disappearTime = getLastVisibleTime(satellite, riseTime, passTime, geoPosition);
        } else  {
//            riseEclipsed == true
//            setEclipsed == false
            disappearTime = setTime;
            if (passEclipsed)
                visibleTime = getFirstVisibleTime(satellite, passTime, setTime, geoPosition);
            else
                visibleTime = getFirstVisibleTime(satellite, riseTime, passTime, geoPosition);
        }

        JD startEpoch = (riseTime.value() < visibleTime.value()) ? visibleTime : riseTime;
        JD endEpoch = (disappearTime.value() < setTime.value()) ? disappearTime : setTime;
//        AltAz max = maxSqueeze(satellite, startEpoch, endEpoch, geoPosition, null);
        AltAz max = new AltAz(0, 0, new JD(0));
//        todo: make this maxTime instead of passTime
        if (Sun.getTwilightType(passTime, geoPosition).ordinal() >= Sun.TwilightType.Nautical.ordinal()) {
            return new SatellitePass(
                    getAltAz(satellite, riseTime, geoPosition),
                    getAltAz(satellite, setTime, geoPosition),
                    getAltAz(satellite, startEpoch, geoPosition),
                    getAltAz(satellite, endEpoch, geoPosition),
                    max
            );
        }
        else throw new DaylightPassException("Pass not visible due to sunlight.");

    }

    public static JD getRiseTime(Satellite satellite, JD time, GeoPosition geoPosition) {
        return getHorizonTime(satellite, time, geoPosition, -1.0);
    }

    public static JD getSetTime(Satellite satellite, JD time, GeoPosition geoPosition) {
        return getHorizonTime(satellite, time, geoPosition, 1.0);
    }

    static final double ALTITUDE_EPSILON = 1e-4;
    //    direction: -1 for rise and +1 for set
    public static JD getHorizonTime(Satellite satellite, JD time, GeoPosition geoPosition, double direction) {
        assert (direction == 1 || direction == -1);
        StateVectors state = satellite.getState(time);
        Matrix rotationMatrix = Rotation.getEulerMatrix(
                EulerOrderList.ZYX,
                new EulerAngles(
                        SiderealTime.getLocalSiderealTime(time, geoPosition.getLongitude() * 15.0),
                        90 - geoPosition.getLatitude(),
                        0.0
                )
        );
        StateVectors sezState = new StateVectors(
                Tracker.getSEZPosition(state.position(), time, geoPosition),
                Rotation.rotateTo(rotationMatrix, state.velocity().exclude(state.position()))
        );
        AltAz altaz = Tracker.getAltAz(satellite, time, geoPosition);
//        System.out.println("time: " + time.date(-6) + " alt: " + altaz.getAltitude());
        if (Math.abs(altaz.getAltitude()) < altitudeEpsilon) return time;
        else {
            double angVelocity = sezState.velocity().mag() / sezState.position().mag(); // radians / s
//            System.out.println("angVelocity: " + angVelocity);
//            todo: this takes shortest path to horizon, find the proper arc length to horizon
            double timeToHorizon = Math.toRadians( altaz.getAltitude() ) / angVelocity;
//            System.out.println("timeToHorizon: " + timeToHorizon);
            return getHorizonTime(
                    satellite,
                    time.future(direction * timeToHorizon / 86400.0),
                    geoPosition,
                    direction
            );
        }
    }

    private static final double ECLIPSE_EPSILON = 1.0 / 86400.0;
    public static JD getFirstVisibleTime(Satellite satellite, JD lower, JD upper, GeoPosition geoPosition) {
        JD biTime = lower.future((upper.value() - lower.value()) / 2.0);    //  preserves precision
//        boolean lowerEclipsed = Eclipse.isEclipsed(satellite, lower);
//        boolean biEclipsed = Eclipse.isEclipsed(satellite, biTime);
//        boolean upperEclipsed = Eclipse.isEclipsed(satellite, upper);
        boolean lowerEclipsed = true;
        boolean biEclipsed = true;
        boolean upperEclipsed = true;

        if (upper.difference(lower) <= ECLIPSE_EPSILON) {
            if (!upperEclipsed) return biTime;
            else throw new NoLightException("Object is eclipsed at " + upper.value());
        } else {
            if (lowerEclipsed && biEclipsed && !upperEclipsed) return getFirstVisibleTime(satellite, biTime, upper, geoPosition);
            else return getFirstVisibleTime(satellite, lower, biTime, geoPosition);
        }
    }

    public static JD getLastVisibleTime(Satellite satellite, JD lower, JD upper, GeoPosition geoPosition) {
        JD biTime = lower.future((upper.value() - lower.value()) / 2.0);    //  preserves precision
        if (upper.difference(lower) <= ECLIPSE_EPSILON) return biTime;
//        if (!Eclipse.isEclipsed(satellite, biTime))
        if (true)
            return getLastVisibleTime(satellite, biTime, upper, geoPosition);
        else return getFirstVisibleTime(satellite, lower, biTime, geoPosition);
    }

//    put max height method here

    /**
     * Computes pass information for any and all passes over a GeoPosition for a given duration.
     * @param tle TLE of the satellite. Note that the TLE becomes less accurate as time
     *                  moves away from the TLE epoch. This should be kept in mind
     * @param startTime Time to begin looking for passes.
     * @param endTime   End of period to look for passes. Note that the TLE becomes less accurate
     *                  as time move away from the TLE epoch. This should be kept in mind when
     *                  choosing this value.
     * @param geoPosition
     *                  The GeoPosition for the pass.
     * @return          A SatellitePass object containing the pass info.
     * @throws NoPassException
     *                  If the satellite is not above the horizon during this time.
     * @throws NoLightException
     *                  If the satellite is never in sunlight during a pass.
     * @throws DaylightPassException
     *                  If the pass occurs during daylight and is not visible.
     */
    public static java.util.Vector<SatellitePass> getPasses(TLE tle, JD startTime, JD endTime, GeoPosition geoPosition) {
//    public static java.util.Vector<SatellitePass> getPasses(Satellite satellite, JD startTime, JD endTime, GeoPosition geoPosition) {
        final double dt = 10 / 86400.0; // 10 seconds
        java.util.Vector<SatellitePass> passList = new java.util.Vector<>();
        JD currentTime = startTime;
        SatellitePass satPass;
        do {
            try {
//                satPass = Tracker.getPassInfo(tle, currentTime, geoPosition);
//                satPass = getPassInfo2(satellite, currentTime, geoPosition);
                satPass = getPassInfo(tle, currentTime, geoPosition);
                passList.add(satPass);
                // todo: because the next guess is programed to guess 10 minutes into the past, it will find this same pass over and over, unless we jump more than 10 minutes into the future
                currentTime = satPass.getSetTime().future(11/1440.0);
            } catch (Exception e) {
                //todo: add a step method to move current JD forward or back in time
                currentTime = currentTime.future(dt);
            }
            //todo: add comparison methods for JD
        } while(currentTime.value() < endTime.value());
        return passList;
    }

    public static java.util.Vector<SatellitePass> getPasses(Satellite satellite, JD startTime, JD endTime, GeoPosition geoPosition) {
        final double dt = 10 / 86400.0; // 10 seconds
        java.util.Vector<SatellitePass> passList = new java.util.Vector<>();
        JD currentTime = startTime;
        SatellitePass satPass;
        do {
            try {
                satPass = getPassInfo3(satellite, currentTime, geoPosition);
                passList.add(satPass);
                currentTime = satPass.getSetTime().future(11.0 / 1440.0);
            } catch (Exception e) {
                currentTime = currentTime.future(dt);
            }
        } while(currentTime.value() < endTime.value());
        return passList;
    }

//    public static java.util.Vector<SatellitePass> getPasses2(Satellite satellite, JD startTime, JD endTime, GeoPosition geoPosition) {
//        final double dt = 10 / 86400.0; // 10 seconds
//        java.util.Vector<SatellitePass> passList = new java.util.Vector<>();
//    }

    //    todo: does this affect passList in the larger scope?
    public static java.util.Vector<SatellitePass> filterPasses(java.util.Vector<SatellitePass> passList, PassFilter filter) {
        Iterator<SatellitePass> iter = passList.iterator();
        while (iter.hasNext()) {
            SatellitePass pass = iter.next();

            switch (filter.getFilterType()) {
                case MINIMUM_HEIGHT -> {
                    if (pass.getMaxHeight() < filter.getMinimumHeight())
//                        passList.remove(pass);
                        iter.remove();
                }
                case MINIMUM_DURATION -> {
                    if (pass.getDisappearTime().difference(pass.getRiseTime()) < filter.getMinimumDuration() / 1440.0)
//                        passList.remove(pass);
                        iter.remove();
                }
                case MINIMUM_HEIGHT_DURATION -> {
                    if (pass.getMaxHeight() < filter.getMinimumHeight())
//                        passList.remove(pass);
                        iter.remove();
                    else if (pass.getDisappearTime().difference(pass.getRiseTime()) < filter.getMinimumDuration() / 1440.0)
//                        passList.remove(pass);
                        iter.remove();
                }
            }
        }
//        for (SatellitePass pass :
//                passList) {
//            switch (filter.getFilterType()) {
//                case MINIMUM_HEIGHT -> {
//                    if (pass.getMaxHeight() < filter.getMinimumHeight())
//                        passList.remove(pass);
//                }
//                case MINIMUM_DURATION -> {
//                    if (pass.getDisappearTime().Difference(pass.getRiseTime()) < filter.getMinimumDuration() / 1440.0)
//                        passList.remove(pass);
//                }
//                case MINIMUM_HEIGHT_DURATION -> {
//                    if (pass.getMaxHeight() < filter.getMinimumHeight())
//                        passList.remove(pass);
//                    else if (pass.getDisappearTime().Difference(pass.getRiseTime()) < filter.getMinimumDuration() / 1440.0)
//                        passList.remove(pass);
//                }
//            }
//        }
        return passList;
    }

    /**
     * Computes the right-ascension and declination of a satellite.
     * @param satellite     TLE of the satellite.
     * @param time          Time to find the satellite's position.
     * @param geoPosition   GeoPosition viewing the satellite.
     * @return              A Coordinates object where the latitude is the declination
     *                      and the longitude is the right-ascension.
     */
//    static public Coordinates getCelestialCoordinates(TLE tle, JD t, Coordinates coords) {
//    static public CelestialCoordinates getCelestialCoordinates(TLE tle, JD t, GeoPosition geoPosition) {
    static public CelestialCoordinates getCelestialCoordinates(Satellite satellite, JD time, GeoPosition geoPosition) {
//        Vector pos = getSEZPosition(tle, t, geoPosition);
//        Vector pos = getSEZPosition(satellite, time, geoPosition);
        Vector pos = Rotation.rotateFrom(
                EulerOrderList.ZYX,
                new EulerAngles(
                        SiderealTime.getLocalSiderealTime(time, geoPosition.getLongitude()) * DEGREES_PER_HOUR,
                        90 - geoPosition.getLatitude(),
                        0
                ),
                getSEZPosition(satellite, time, geoPosition)
        );
        double ra = OrbitalMath.atan2(pos.y(), pos.x());
        final double HOURS_PER_RADIAN = 24.0 * (2.0 * Math.PI);
//        if (ra < 0) ra += (2 * Math.PI);
//        ra = (ra / (2 * Math.PI) * 24.0);
//        return new Coordinates(
//                ra,
//                Math.toDegrees( Math.asin(pos.z() / pos.mag()) )
//        );
        return new CelestialCoordinates(
                ra * HOURS_PER_RADIAN,
                Math.toDegrees( Math.asin(pos.z() / pos.mag()) )
        );
    }

    /**
     * Adjusts right-ascension and declination due to precession and nutation of the
     * celestial pole. Right-ascension is in time units (hours) and declination in degrees.
     * If this is used to adjust a star it does not account for proper motion.
     * @param time  Epoch to convert coordinates to.
     * @param RaDec Coordinates in J2000 epoch reference frame with right-ascension
     *              as longitude and declination as latitude.
     * @return      Adjusted coordinates with right-ascension as longitude and
     *              declination as latitude.
     */
//    static public Coordinates adjustRaDec(JD t, Coordinates RaDec) {
    static public CelestialCoordinates adjustRaDec(JD time, CelestialCoordinates RaDec) {
        double JDCenturies = (time.value() - JD.J2000) / 36525.0;
        double m = 3.07496 + 0.00186 * JDCenturies;
        double nra = 1.33621 - 0.00057 * JDCenturies;
        double ndec = 20.0431 - 0.0085 * JDCenturies;
        double ra = Math.toRadians( RaDec.getLongitude() * 15 );
        double dec = Math.toRadians( RaDec.getLatitude() );
        double dAlpha = m + (nra * Math.sin(ra) * Math.tan(dec));
        double dDel = ndec * Math.cos( ra );
        return new CelestialCoordinates(
                dDel * JDCenturies * 100,
                dAlpha * JDCenturies * 100
        );
//        return new Coordinates(
//                dDel * JDCenturies * 100,
//                dAlpha * JDCenturies * 100
//        );
    }

    /**
     * Rotates and then translates said rotated reference frame.
     * @param rot      Rotation from inertial to rotated reference frame.
     * @param offset   Vector offset between reference frame origins, with respect to the
     *                 inertial reference frame, pointing towards the translated origin.
     * @param original Original vector to rotate and translate.
     * @return  The rotated and translated vector.
     */
    static private Vector rotateTranslate(Matrix rot, Vector offset, Vector original) {
        Vector rotOrig = rot.transpose().mult(original);
        Vector rotOffset = rot.transpose().mult(offset);
        return rotOrig.minus(rotOffset);
    }

    /**
     * Epsilon value for determining if an altitude is considered above the horizon
     * (since it is difficult to find out exactly when altitude is equal to zero).
     */
    static private final double altitudeEpsilon = 0.01;

    /**
     * Finds the time the satellite rises above the horizon. The method is recursive and uses a
     * bifurcation method to 'squeeze' the bounds towards the correct value. If the satellite
     * position at the upper bound is not above the horizon then there is not a valid pass
     * at that time.
     * @param tle    TLE of the satellite.
     * @param lower  Lower bound of the possible rise times, should be below the actual rise time
     *               or else the recursion will converge to this time and not be correct.
     * @param upper  Upper bound of the possible rise times, should be during the overhead pass or
     *               else the pass will not be valid and an exception will be thrown.
     * @param geoPosition
     *               GeoPosition of the pass.
     * @param rtn    Value of the previous computation passed to the next call, initial is excepted
     *               to be {@code null}.
     * @return  An AltAz object corresponding to the rise time of the interested pass.
     * @throws NoPassException
     *              Signals if the satellite never rises during the original lower-upper time frame.
     */
//    static private AltAz riseSqueeze(TLE tle, JD lower, JD upper, Coordinates coords, AltAz rtn) {
    static private AltAz riseSqueeze(TLE tle, JD lower, JD upper, GeoPosition geoPosition, AltAz rtn) {
        if (upper.difference(lower) <= squeezeEpsilon) {
            if (rtn.getAltitude() + altitudeEpsilon > 0) return rtn;
            else throw new NoPassException("No overhead pass at " + upper.date());
        }
        JD biTime = lower.future((upper.value() - lower.value()) / 2.0);
//        AltAz altaz = Tracker.getAltAz(tle, biTime, coords);
        AltAz altaz = Tracker.getAltAz(tle, biTime, geoPosition);
//            if (altaz.getAltitude() > 0) return riseSqueeze(tle, lower, biTime, coords, altaz);
        if (altaz.getAltitude() > 0) return riseSqueeze(tle, lower, biTime, geoPosition, altaz);
//            else return riseSqueeze(tle, biTime, upper, coords, altaz);
        else return riseSqueeze(tle, biTime, upper, geoPosition, altaz);
    }
    static private AltAz riseSqueeze2(Satellite satellite, JD lower, JD upper, GeoPosition geoPosition) {
        if (upper.difference(lower) <= squeezeEpsilon) {
            AltAz altaz = getAltAz(satellite, lower, geoPosition);
            if (altaz.getAltitude() + altitudeEpsilon > 0) return altaz;
            else throw new NoPassException("No overhead pass at " + lower.date());
        }
        JD biTime = lower.future((upper.value() - lower.value()) / 2.0);
        AltAz altaz = getAltAz(satellite, biTime, geoPosition);
        if (altaz.getAltitude() > 0) return riseSqueeze2(satellite, lower, biTime, geoPosition);
        else return riseSqueeze2(satellite, biTime, upper, geoPosition);
    }

    /*
     * Finds the time the satellite sets below the horizon. The method is recursive and uses a
     * bifurcation method to 'squeeze' the bounds towards the correct value. {@link #riseSqueeze(TLE, JD, JD, GeoPosition, AltAz)}
     * should be called beforehand, since that method will detect if no pass occurs, as well as gives the best
     * initial lower bounds to the recursion call.
     * @param tle    TLE of the satellite.
     * @param lower  Lower bound of the possible set times, initial value should be the answer from
     *               {@link #riseSqueeze(TLE, JD, JD, GeoPosition, AltAz)}.
     * @param upper  Upper bound of the possible set times, should be later than the actual set time or
     *               else the recursion will converge to this time and not be correct.
     * @param geoPosition
     *               GeoPosition of the pass.
     * @param rtn    Value of the previous computation passed to the next call, initial is excepted
     *               to be {@code null}.
     * @return  An AltAz object corresponding to the set time of the interested pass.
     */
//    static private AltAz setSqueeze(TLE tle, JD lower, JD upper, Coordinates coords, AltAz rtn) {
    static private AltAz setSqueeze(TLE tle, JD lower, JD upper, GeoPosition geoPosition, AltAz rtn) {
        if (upper.difference(lower) <= squeezeEpsilon) return rtn;
        JD biTime = lower.future((upper.value() - lower.value()) / 2.0);
//        AltAz altaz = Tracker.getAltAz(tle, biTime, coords);
        AltAz altaz = Tracker.getAltAz(tle, biTime, geoPosition);
//        if (altaz.getAltitude() > 0) return setSqueeze(tle, biTime, upper, coords, altaz);
        if (altaz.getAltitude() > 0) return setSqueeze(tle, biTime, upper, geoPosition, altaz);
//        else return setSqueeze(tle, lower, biTime, coords, altaz);
        else return setSqueeze(tle, lower, biTime, geoPosition, altaz);
    }
    static private AltAz setSqueeze2(Satellite satellite, JD lower, JD upper, GeoPosition geoPosition) {
        if (upper.difference(lower) <= squeezeEpsilon)
            return getAltAz(satellite, lower, geoPosition);
        JD biTime = lower.future((upper.value() - lower.value()) / 2.0);
        AltAz altaz = getAltAz(satellite, biTime, geoPosition);
        if (altaz.getAltitude() > 0) return setSqueeze2(satellite, biTime, upper, geoPosition);
        else return setSqueeze2(satellite, lower, biTime, geoPosition);
    }

    /*
     * Finds the time the satellite first becomes visible due to sunlight. The method is recursive and uses a
     * bifurcation method to 'squeeze' the bounds towards the correct value. {@link #riseSqueeze(TLE, JD, JD, GeoPosition, AltAz)}
     * and {@link #setSqueeze(TLE, JD, JD, GeoPosition, AltAz)} should provide the initial bounds.
     * @param tle    TLE of the satellite.
     * @param lower  Lower bound of the possible first times, initial value should be the answer from
     *               {@link #riseSqueeze(TLE, JD, JD, GeoPosition, AltAz)} and the method shouldn't
     *               be used if a value wasn't returned.
     * @param upper  Upper bound of the possible first times, initial value should be the answer from
     *               {@link #setSqueeze(TLE, JD, JD, GeoPosition, AltAz)}.
     * @param geoPosition
     *               GeoPosition of the pass.
     * @return  An AltAz object corresponding to the first visible time of the interested pass.
     * @throws NoLightException
     *              Signals if the satellite doesn't encounter any sunlight during a pass.
     */
//    static private AltAz firstSqueeze(TLE tle, JD lower, JD upper, Coordinates geoPos) {
    static private AltAz firstSqueeze(TLE tle, JD lower, JD upper, GeoPosition geoPosition) {
        JD biTime = lower.future((upper.value() - lower.value()) / 2.0);
        Vector lowerSunPosition = Sun.position(lower);
        Vector biSunPosition = Sun.position(biTime);
        Vector upperSunPosition = Sun.position(upper);
        Vector lowerSatPosition = SGP4.propagate(tle, lower).position();
        Vector biSatPosition = SGP4.propagate(tle, biTime).position();
        Vector upperSatPosition = SGP4.propagate(tle, upper).position();
        boolean lowerEclipsed = Eclipse.isEclipsed(lowerSatPosition, lowerSunPosition);
        boolean biEclipsed = Eclipse.isEclipsed(biSatPosition, biSunPosition);
        boolean upperEclipsed = Eclipse.isEclipsed(upperSatPosition, upperSunPosition);

        if (upper.difference(lower) <= squeezeEpsilon) {
            if (!upperEclipsed) return Tracker.getAltAz(tle, upper, geoPosition);
            else throw new NoLightException("Object is eclipsed at " + upper.value());
        } else {
            if (lowerEclipsed && biEclipsed && !upperEclipsed) return firstSqueeze(tle, biTime, upper, geoPosition);
            else return firstSqueeze(tle, lower, biTime, geoPosition);
        }
    }
    static private AltAz firstSqueeze2(Satellite satellite, JD lower, JD upper, GeoPosition geoPosition) {
        JD biTime = lower.future((upper.value() - lower.value()) / 2.0);
        Vector lowerSunPosition = Sun.position(lower);
        Vector biSunPosition = Sun.position(biTime);
        Vector upperSunPosition = Sun.position(upper);
        Vector lowerSatPosition = satellite.getState(lower).position();
        Vector biSatPosition = satellite.getState(biTime).position();
        Vector upperSatPosition = satellite.getState(upper).position();
        boolean lowerEclipsed = Eclipse.isEclipsed(lowerSatPosition, lowerSunPosition);
        boolean biEclipsed = Eclipse.isEclipsed(biSatPosition, biSunPosition);
        boolean upperEclipsed = Eclipse.isEclipsed(upperSatPosition, upperSunPosition);

        if (upper.difference(lower) <= squeezeEpsilon) {
            if (!upperEclipsed) return getAltAz(satellite, upper, geoPosition);
            else throw new NoLightException("Object is eclipsed at " + upper.value());
        } else {
            if (lowerEclipsed && biEclipsed && !upperEclipsed) return firstSqueeze2(satellite, biTime, upper, geoPosition);
            else return firstSqueeze2(satellite, lower, biTime, geoPosition);
        }
    }

    /*
     * Finds the time the satellite is last visible due to sunlight. The method is recursive and uses
     * a bifurcation method to 'squeeze' the bounds towards the correct value. {@link #firstSqueeze(TLE, JD, JD, GeoPosition)}
     * should be called before this method to ensure there is a valid lower bound for the time-frame
     * the satellite is lit, as well as provide the initial lower bound.
     * @param tle    The TLE of the satellite.
     * @param lower  Lower bound of the possible last times, initial value should be the answer from
     *               {@link #firstSqueeze(TLE, JD, JD, GeoPosition)} and the method shouldn't be
     *               used if  a value wasn't returned.
     * @param upper  Upper bound of the possible last times, initial value should be the value returned
     *               by {@link #setSqueeze(TLE, JD, JD, GeoPosition, AltAz)}.
     * @param geoPosition
     *               GeoPosition of the pass.
     * @param rtn    Value of the previous computation passed to the next call, initial is excepted
     *               to be {@code null}.
     * @return  An AltAz object corresponding to the last visible time of the interested pass.
     */
//    static private AltAz lastSqueeze(TLE tle, JD lower, JD upper, Coordinates geoPos, AltAz rtn) {
    static private AltAz lastSqueeze(TLE tle, JD lower, JD upper, GeoPosition geoPosition, AltAz rtn) {
        if (upper.difference(lower) <= squeezeEpsilon) return rtn;
        JD biTime = lower.future((upper.value() - lower.value()) / 2.0);
        Vector sunPosition = Sun.position(biTime);
        Vector satPosition = SGP4.propagate(tle, biTime).position();
        AltAz altaz = Tracker.getAltAz(
                getSEZPosition(satPosition, biTime, geoPosition),
                biTime
        );
        if (!Eclipse.isEclipsed(satPosition, sunPosition))
            return lastSqueeze(tle, biTime, upper, geoPosition, altaz);
        else return lastSqueeze(tle, lower, biTime, geoPosition, altaz);
    }
    static private AltAz lastSqueeze2(Satellite satellite, JD lower, JD upper, GeoPosition geoPosition) {
        if (upper.difference(lower) <= squeezeEpsilon)
            return getAltAz(satellite, lower, geoPosition);
        JD biTime = lower.future((upper.value() - lower.value()) / 2.0);
        Vector sunPosition = Sun.position(biTime);
        Vector satPosition = satellite.getState(biTime).position();
        AltAz altaz = getAltAz(
                getSEZPosition(satPosition, biTime, geoPosition),
                biTime
        );
        if (!Eclipse.isEclipsed(satPosition, sunPosition))
            return lastSqueeze2(satellite, biTime, upper, geoPosition);
        else return lastSqueeze2(satellite, lower, biTime, geoPosition);
    }

//    todo: there's gotta be a better/more accurate way to compute this
    /**
     * This method computes the time the satellite achieves its maximum altitude
     * during a pass. Lower and Upper bounds should not be outside the bounds
     * of the values returned by .
     * @param tle
     *               TLE of the satellite.
     * @param lower  Lower bound of the possible max times.
     * @param upper  Upper bound of the possible max times.
     * @param geoPosition
     *               GeoPosition of the pass.
     * @param rtn    Value of the previous computation passed to the next call, initial is expected
     *               to be {@code null}.
     * @return  An AltAz object corresponding to the time the satellite achieves its
     *          maximum altitude.
     */
    static AltAz maxSqueeze(TLE tle, JD lower, JD upper, GeoPosition geoPosition, AltAz rtn) {
//    static AltAz maxSqueeze(Satellite satellite, JD lower, JD upper, GeoPosition geoPosition, AltAz rtn) {
        if (upper.difference(lower) <= squeezeEpsilon) return rtn;
        AltAz lowerAltAz = Tracker.getAltAz(tle, lower, geoPosition);
        JD biTime = new JD((lower.value() + upper.value()) / 2.0);
        AltAz biAltAz = Tracker.getAltAz(tle, biTime, geoPosition);
        AltAz upperAltAz = Tracker.getAltAz(tle, upper, geoPosition);
        if (biAltAz.getAltitude() >= lowerAltAz.getAltitude()) {
            // path goes up and then down
            if (biAltAz.getAltitude() >= upperAltAz.getAltitude()) {
                if (lowerAltAz.getAltitude() >= upperAltAz.getAltitude()) return maxSqueeze(tle, lower, biTime, geoPosition, biAltAz);
                else return maxSqueeze(tle, biTime, upper, geoPosition, biAltAz);
            }
            // only traveling up
            else return maxSqueeze(tle, biTime, upper, geoPosition, biAltAz);
        }
        // only traveling down
        else return maxSqueeze(tle, lower, biTime, geoPosition, biAltAz);
    }
    static AltAz maxSqueeze2(Satellite satellite, JD lower, JD upper, GeoPosition geoPosition) {
        if (upper.difference(lower) <= squeezeEpsilon)
            return getAltAz(satellite, lower, geoPosition);
        AltAz lowerAltAz = getAltAz(satellite, lower, geoPosition);
        JD biTime = lower.future((upper.value() - lower.value()) / 2.0);
        AltAz biAltAz = getAltAz(satellite, biTime, geoPosition);
        AltAz upperAltAz = getAltAz(satellite, upper, geoPosition);
        if (biAltAz.getAltitude() >= lowerAltAz.getAltitude()) {
            if (biAltAz.getAltitude() >= upperAltAz.getAltitude()) {
                if (lowerAltAz.getAltitude() >= upperAltAz.getAltitude()) return maxSqueeze2(satellite, lower, biTime, geoPosition);
                else return maxSqueeze2(satellite, biTime, upper, geoPosition);
            }
            else return maxSqueeze2(satellite, biTime, upper, geoPosition);
        }
        else return maxSqueeze2(satellite, lower, biTime, geoPosition);
    }

}