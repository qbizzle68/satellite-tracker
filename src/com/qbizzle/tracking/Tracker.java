/** @file
 * This file contains a static class with methods to track or compute future
 * positions of satellites, as well as predict future overhead passes.
 */

package com.qbizzle.tracking;

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
    static private final double squeezeEpsilon = 1.0 / 8640000.0; // 1/10th of a second

    /** Converts an angle in degree notation to hour-minute-seconds notation */
    private static final double HOURS_PER_DEGREE = 360.0 / 24.0;

    /** Computes the GeoPosition in which the satellite is directly overhead at
     * time dt in the past/future.
     * @param tle The TLE of the satellite to track.
     * @param dt The time of the desired position in solar days.
     * @return The GeoPosition of the satellite.
     */
    @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
    public static Vector getSEZPosition(TLE tle, double dt, Coordinates geoPos) {
        return getSEZPosition(tle, new JD(tle).Future(dt), geoPos);
    }

    public static Vector getSEZPosition(TLE tle, JD t1, Coordinates geoPos) {
        // todo: check which ephemeris model to use before calling
        return getSEZPosition(
                SGP4.Propagate(tle, t1).Position(),
                t1,
                geoPos
        );
    }

    public static Vector getSEZPosition(Vector position, JD t1, Coordinates geoPos) {
        double localSiderealTime = SiderealTime.LST(t1, geoPos.getLongitude()) * HOURS_PER_DEGREE;
        Vector geoPosVector = getToposPosition(t1, geoPos);
        Matrix sezToIJK = Rotation.getEulerMatrix(
                EulerOrderList.ZYX,
                new EulerAngles(
                        localSiderealTime,
                        90-geoPos.getLatitude(),
                        0
                )
        );
        return rotateTranslate(sezToIJK, geoPosVector, position);
    }

    public static Vector getToposPosition(JD t, Coordinates topos) {
        // todo: account for elevation with this radius
        double radiusAtLat = Coordinates.radiusAtLatitude(topos.getLatitude());
        double geocentricLat = Coordinates.geodeticToGeocentric(topos.getLatitude());
        double localSiderealTime = SiderealTime.LST(t, topos.getLongitude()) * HOURS_PER_DEGREE;
        return new Vector(
                radiusAtLat * Math.cos( Math.toRadians(geocentricLat) ) * Math.cos( Math.toRadians(localSiderealTime) ),
                radiusAtLat * Math.cos( Math.toRadians(geocentricLat) ) * Math.sin( Math.toRadians(localSiderealTime) ),
                radiusAtLat * Math.sin( Math.toRadians(geocentricLat) )
        );
    }

    public static AltAz getAltAz(TLE tle, JD t1, Coordinates geoPos) {
        return getAltAz(
                getSEZPosition(tle, t1, geoPos),
                t1
        );
    }

    public static AltAz getAltAz(Vector sezPosition, JD t1) {
        return new AltAz(
                Math.toDegrees( Math.asin(sezPosition.z() / sezPosition.mag())),
                Math.toDegrees( OrbitalMath.atan2(sezPosition.y(), -sezPosition.x()) ),
                t1
        );
    }

    @SuppressWarnings("unused")
    public static boolean isAboveHorizon(TLE tle, JD t, Coordinates geoPosition) {
        // todo: wouldn't it be more efficient if we just called for SEZ position and returned (sezPos.z() > 0)
        AltAz altaz = getAltAz(tle, t, geoPosition);
        return (altaz.getAltitude() > 0);
    }

    public static SatellitePass getPassInfo(TLE tle, JD passTime, Coordinates coords) {
        // todo: how to we make a better guess than 10 minutes?
        AltAz rise = riseSqueeze(tle, passTime.Future(-10.0 / 1440.0), passTime, coords, null);
        AltAz set = setSqueeze(tle, rise.getEpoch(), passTime.Future(10.0 / 1440.0), coords, null);
        AltAz first = firstSqueeze(tle, rise.getEpoch(), set.getEpoch(), coords);
        AltAz last = lastSqueeze(tle, first.getEpoch(), set.getEpoch(), coords, null);

        JD startEpoch = (rise.getEpoch().Value() < first.getEpoch().Value()) ? first.getEpoch() : rise.getEpoch();
        JD finishEpoch = (set.getEpoch().Value() < last.getEpoch().Value()) ? set.getEpoch() : last.getEpoch();
        if (Sun.getTwilightType(passTime, coords).ordinal() >= Sun.TwilightType.Nautical.ordinal()) {
            return new SatellitePass(
                    rise, set, first, last,
                    maxSqueeze(tle, startEpoch, finishEpoch, coords, null)
            );
        }
        else throw new DaylightPassException("Pass not visible due to sunlight.");
    }

    @SuppressWarnings("unused")
    // returns a Coordinates with declination as latitude and right ascension as longitude in hours
    static public Coordinates getCelestialCoordinates(TLE tle, JD t1, Coordinates coords) {
        Vector pos = getSEZPosition(tle, t1, coords);
        pos = Rotation.RotateFrom(
                EulerOrderList.ZYX,
                new EulerAngles(
                        SiderealTime.LST(t1, coords.getLongitude()) * HOURS_PER_DEGREE,
                        90 - coords.getLatitude(),
                        0
                ),
                pos
        );
        double ra = OrbitalMath.atan2(pos.y(), pos.x());
        if (ra < 0) ra += (2 * Math.PI);
        ra = (ra / (2 * Math.PI) * 24.0);
        return new Coordinates(
                ra,
                Math.toDegrees( Math.asin(pos.z() / pos.mag()) )
        );
    }

    @SuppressWarnings("unused")
    // rotation is from inertial to rotated, offset in inertial frame pointing from inertial origin to translated origin.
    static private Vector rotateTranslate(Matrix rot, Vector offset, Vector original) {
        Vector rotOrig = rot.transpose().mult(original);
        Vector rotOffset = rot.transpose().mult(offset);
        return rotOrig.minus(rotOffset);
    }

    static private final double altitudeEpsilon = 0.01;
    static private AltAz riseSqueeze(TLE tle, JD lower, JD upper, Coordinates coords, AltAz rtn) {
        if (upper.Difference(lower) <= squeezeEpsilon) {
            if (rtn.getAltitude() + altitudeEpsilon > 0) return rtn;
            else throw new NoPassException("No overhead pass at " + upper.Date());
        }
        JD biTime = lower.Future((upper.Value() - lower.Value()) / 2.0);
        AltAz altaz = Tracker.getAltAz(tle, biTime, coords);
        if (altaz.getAltitude() > 0) return riseSqueeze(tle, lower, biTime, coords, altaz);
        else return riseSqueeze(tle, biTime, upper, coords, altaz);
    }

    // should be called after riseSqueeze, as riseSqueeze will detect if no pass occurs, also gives lower bound
    static private AltAz setSqueeze(TLE tle, JD lower, JD upper, Coordinates coords, AltAz rtn) {
        if (upper.Difference(lower) <= squeezeEpsilon) return rtn;
        JD biTime = lower.Future((upper.Value() - lower.Value()) / 2.0);
        AltAz altaz = Tracker.getAltAz(tle, biTime, coords);
        if (altaz.getAltitude() > 0) return setSqueeze(tle, biTime, upper, coords, altaz);
        else return setSqueeze(tle, lower, biTime, coords, altaz);
    }

    static private AltAz firstSqueeze(TLE tle, JD lower, JD upper, Coordinates geoPos) {
        JD biTime = lower.Future((upper.Value() - lower.Value()) / 2.0);
        Vector lowerSunPosition = Sun.Position(lower);
        Vector biSunPosition = Sun.Position(biTime);
        Vector upperSunPosition = Sun.Position(upper);
        Vector lowerSatPosition = SGP4.Propagate(tle, lower).Position();
        Vector biSatPosition = SGP4.Propagate(tle, biTime).Position();
        Vector upperSatPosition = SGP4.Propagate(tle, upper).Position();
        boolean lowerEclipsed = Eclipse.isEclipsed(lowerSatPosition, lowerSunPosition);
        boolean biEclipsed = Eclipse.isEclipsed(biSatPosition, biSunPosition);
        boolean upperEclipsed = Eclipse.isEclipsed(upperSatPosition, upperSunPosition);

        if (upper.Difference(lower) <= squeezeEpsilon) {
            if (!upperEclipsed) return Tracker.getAltAz(tle, upper, geoPos);
            else throw new NoLightException("Object is eclipsed at " + upper.Value());
        } else {
            if (lowerEclipsed && biEclipsed && !upperEclipsed) return firstSqueeze(tle, biTime, upper, geoPos);
            else return firstSqueeze(tle, lower, biTime, geoPos);
        }
    }

    // should call firstSqueeze first to get the initial lower time, as well as to be sure
    // the lower time is initially lit
    static private AltAz lastSqueeze(TLE tle, JD lower, JD upper, Coordinates geoPos, AltAz rtn) {
        if (upper.Difference(lower) <= squeezeEpsilon) return rtn;
        JD biTime = lower.Future((upper.Value() - lower.Value()) / 2.0);
        Vector sunPosition = Sun.Position(biTime);
        Vector satPosition = SGP4.Propagate(tle, biTime).Position();
        AltAz altaz = Tracker.getAltAz(
                getSEZPosition(satPosition, biTime, geoPos),
                biTime
        );
        if (!Eclipse.isEclipsed(satPosition, sunPosition))
            return lastSqueeze(tle, biTime, upper, geoPos, altaz);
        else return lastSqueeze(tle, lower, biTime, geoPos, altaz);
    }

    // don't know if this is the best way to do this. it makes some assumptions on the shape of the visible path
    // that I'm not sure is always appropriate for different sized orbits...
    static AltAz maxSqueeze(TLE tle, JD lower, JD upper, Coordinates geoPos, AltAz rtn) {
        if (upper.Difference(lower) <= squeezeEpsilon) return rtn;
        AltAz lowerAltAz = Tracker.getAltAz(tle, lower, geoPos);
        JD biTime = new JD((lower.Value() + upper.Value()) / 2.0);
        AltAz biAltAz = Tracker.getAltAz(tle, biTime, geoPos);
        AltAz upperAltAz = Tracker.getAltAz(tle, upper, geoPos);
        if (biAltAz.getAltitude() >= lowerAltAz.getAltitude()) {
            // path goes up and then down
            if (biAltAz.getAltitude() >= upperAltAz.getAltitude()) {
                if (lowerAltAz.getAltitude() >= upperAltAz.getAltitude()) return maxSqueeze(tle, lower, biTime, geoPos, biAltAz);
                else return maxSqueeze(tle, biTime, upper, geoPos, biAltAz);
            }
            // only traveling up
            else return maxSqueeze(tle, biTime, upper, geoPos, biAltAz);
        }
        // only traveling down
        else return maxSqueeze(tle, lower, biTime, geoPos, biAltAz);
    }

}