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
@SuppressWarnings("unused")
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
        // todo: check which ephemeris model to use before calling
        StateVectors stateAtT1 = SGP4.Propagate(tle, t1);
        double earthOffsetAngle = SiderealTime.EarthOffsetAngle(t1);
        Vector positionAtT1 = Rotation.RotateFrom(Axis.Direction.Z, -earthOffsetAngle, stateAtT1.Position());
        return new Coordinates(positionAtT1);
    }

    /** Computes an array of GeoPositions of a satellite over a given period.
     * @param tle The TLE of the satellite to track.
     * @param dt The amount of time to track in solar days.
     * @param interval The interval of time between successive tracks in solar days.
     * @return An array of GeoPositions making up the ground track.
     */
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

    /**
     * Computes the position of a satellite in a topocentric reference frame.
     * @param tle       TLE of the satellite.
     * @param dt        Change in time relative to the @p tle epoch.
     * @param geoPos    GeoPosition which corresponds to the center of the reference frame.
     * @return          The position vector in SEZ reference frame.
     */
    public static Vector getSEZPosition(TLE tle, double dt, Coordinates geoPos) {
        return getSEZPosition(tle, new JD(tle).Future(dt), geoPos);
    }

    /**
     * Computes the position of a satellite in a topocentric reference frame.
     * @param tle       TLE of the satellite.
     * @param t1        Time in which to find the satellite position.
     * @param geoPos    GeoPosition which corresponds to the center of the reference frame.
     * @return          The position vector in SEZ reference frame.
     */
    public static Vector getSEZPosition(TLE tle, JD t1, Coordinates geoPos) {
        // todo: check which ephemeris model to use before calling
        return getSEZPosition(
                SGP4.Propagate(tle, t1).Position(),
                t1,
                geoPos
        );
    }

    /**
     * Converts a position vector from a geocentric reference frame to a topocentric
     * reference frame.
     * @param position  Position vector of the satellite in the earth centered reference frame.
     * @param t1        Time in which the satellite occupies this position. This is needed to find
     *                  the earth rotation offset from the celestial coordinate system.
     * @param geoPos    GeoPosition which corresponds to the center of the reference frame.
     * @return          The position vector in SEZ reference frame.
     */
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

    /**
     * Computes the position vector of a GeoPosition.
     * @param t     Time the position vector is needed. This is necessary for adjusting
     *              for the earth's rotation offset.
     * @param topos The GeoPosition corresponding to the center of the reference frame.
     * @return      The position vector in earth centric reference frame.
     */
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

    /**
     * Generates an AltAz object referencing the altitude and azimuth of a satellite at a
     * given time.
     * @param tle       TLE of the satellite.
     * @param t         Time to find the position.
     * @param geoPos    GeoPosition to find the relative altitude and azimuth for.
     * @return          An AltAz object with the epoch set as @p t.
     */
    public static AltAz getAltAz(TLE tle, JD t, Coordinates geoPos) {
        return getAltAz(
                getSEZPosition(tle, t, geoPos),
                t
        );
    }

    /**
     * Converts a position vector in a topocentric reference frame to a relative
     * altitude and azimuth.
     * @param sezPosition   Position vector in SEZ reference frame.
     * @param t             Time associated with the position.
     * @return              An AltAz object with the epoch set as @p t.
     */
    public static AltAz getAltAz(Vector sezPosition, JD t) {
        return new AltAz(
                Math.toDegrees( Math.asin(sezPosition.z() / sezPosition.mag())),
                Math.toDegrees( OrbitalMath.atan2(sezPosition.y(), -sezPosition.x()) ),
                t
        );
    }

    /**
     * Determines if a satellite is above the horizon for a given GeoPosition and time.
     * @param tle           TLE for the satellite.
     * @param t             Time the satellite would be above the horizon.
     * @param geoPosition   GeoPosition for determining the horizon.
     * @return              True if the altitude of the satellite is greater than 0,
     *                      false if otherwise.
     */
    public static boolean isAboveHorizon(TLE tle, JD t, Coordinates geoPosition) {
        // todo: wouldn't it be more efficient if we just called for SEZ position and returned (sezPos.z() > 0)
        AltAz altaz = getAltAz(tle, t, geoPosition);
        return (altaz.getAltitude() > 0);
    }

    /**
     * Computes the pass information for a satellite pass.
     * @param tle       TLE of the satellite.
     * @param passTime  If there is a valid pass, this value must be between the pass rise
     *                  and set times. There may be some marginal error allowed but success
     *                  cannot be guaranteed.
     * @param coords    The GeoPosition for the pass.
     * @return          A SatellitePass object containing the pass info.
     * @throws NoPassException
     *                  If the satellite is not above the horizon during this time.
     * @throws NoLightException
     *                  If the satellite is never in sunlight during a pass.
     * @throws DaylightPassException
     *                  If the pass occurs during daylight and is not visible.
     */
    public static SatellitePass getPassInfo(TLE tle, JD passTime, Coordinates coords) {
//        todo: how to we make a better guess than 10 minutes?
//        todo: checking for daylight first should cut this processing time by half when used by getPasses
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

    /**
     * Computes pass information for any and all passes over a GeoPosition for a given duration.
     * @param tle       TLE of the satellite. Note that the TLE becomes less accurate as time
     *                  moves away from the TLE epoch. This should be kept in mind
     * @param startTime Time to begin looking for passes.
     * @param endTime   End of period to look for passes. Note that the TLE becomes less accurate
     *                  as time move away from the TLE epoch. This should be kept in mind when
     *                  choosing this value.
     * @param geoPos    The GeoPosition for the pass.
     * @return          A SatellitePass object containing the pass info.
     * @throws NoPassException
     *                  If the satellite is not above the horizon during this time.
     * @throws NoLightException
     *                  If the satellite is never in sunlight during a pass.
     * @throws DaylightPassException
     *                  If the pass occurs during daylight and is not visible.
     */
    public static java.util.Vector<SatellitePass> getPasses(TLE tle, JD startTime, JD endTime, Coordinates geoPos) {
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

    /**
     * Computes the right-ascension and declination of a satellite.
     * @param tle    TLE of the satellite.
     * @param t      Time to find the satellite's position.
     * @param coords GeoPosition viewing the satellite.
     * @return       A Coordinates object where the latitude is the declination
     *               and the longitude is the right-ascension.
     */
    static public Coordinates getCelestialCoordinates(TLE tle, JD t, Coordinates coords) {
        Vector pos = getSEZPosition(tle, t, coords);
        pos = Rotation.RotateFrom(
                EulerOrderList.ZYX,
                new EulerAngles(
                        SiderealTime.LST(t, coords.getLongitude()) * HOURS_PER_DEGREE,
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

    /**
     * Adjusts right-ascension and declination due to precession and nutation of the
     * celestial pole. Right-ascension is in time units (hours) and declination in degrees.
     * If this is used to adjust a star it does not account for proper motion.
     * @param t     Epoch to convert coordinates to.
     * @param RaDec Coordinates in J2000 epoch reference frame with right-ascension
     *              as longitude and declination as latitude.
     * @return      Adjusted coordinates with right-ascension as longitude and
     *              declination as latitude.
     */
    static public Coordinates adjustRaDec(JD t, Coordinates RaDec) {
        double JDCenturies = (t.Value() - JD.J2000) / 36525.0;
        double m = 3.07496 + 0.00186 * JDCenturies;
        double nra = 1.33621 - 0.00057 * JDCenturies;
        double ndec = 20.0431 - 0.0085 * JDCenturies;
        double ra = Math.toRadians( RaDec.getLongitude() * 15 );
        double dec = Math.toRadians( RaDec.getLatitude() );
        double dAlpha = m + (nra * Math.sin(ra) * Math.tan(dec));
        double dDel = ndec * Math.cos( ra );
        return new Coordinates(
                dDel * JDCenturies * 100,
                dAlpha * JDCenturies * 100
        );
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
     * @param coords GeoPosition of the pass.
     * @param rtn    Value of the previous computation passed to the next call, initial is excepted
     *               to be {@code null}.
     * @return  An AltAz object corresponding to the rise time of the interested pass.
     * @throws NoPassException
     *              Signals if the satellite never rises during the original lower-upper time frame.
     */
    static private AltAz riseSqueeze(TLE tle, JD lower, JD upper, Coordinates coords, AltAz rtn) {
        if (upper.Difference(lower) <= squeezeEpsilon) {
            if (rtn.getAltitude() + altitudeEpsilon > 0) return rtn;
            else throw new NoPassException("No overhead pass at " + upper.date());
        }
        JD biTime = lower.Future((upper.Value() - lower.Value()) / 2.0);
        AltAz altaz = Tracker.getAltAz(tle, biTime, coords);
        if (altaz.getAltitude() > 0) return riseSqueeze(tle, lower, biTime, coords, altaz);
        else return riseSqueeze(tle, biTime, upper, coords, altaz);
    }

    /**
     * Finds the time the satellite sets below the horizon. The method is recursive and uses a
     * bifurcation method to 'squeeze' the bounds towards the correct value. {@link #riseSqueeze(TLE, JD, JD, Coordinates, AltAz)}
     * should be called beforehand, since that method will detect if no pass occurs, as well as gives the best
     * initial lower bounds to the recursion call.
     * @param tle    TLE of the satellite.
     * @param lower  Lower bound of the possible set times, initial value should be the answer from
     *               {@link #riseSqueeze(TLE, JD, JD, Coordinates, AltAz)}.
     * @param upper  Upper bound of the possible set times, should be later than the actual set time or
     *               else the recursion will converge to this time and not be correct.
     * @param coords GeoPosition of the pass.
     * @param rtn    Value of the previous computation passed to the next call, initial is excepted
     *               to be {@code null}.
     * @return  An AltAz object corresponding to the set time of the interested pass.
     */
    static private AltAz setSqueeze(TLE tle, JD lower, JD upper, Coordinates coords, AltAz rtn) {
        if (upper.Difference(lower) <= squeezeEpsilon) return rtn;
        JD biTime = lower.Future((upper.Value() - lower.Value()) / 2.0);
        AltAz altaz = Tracker.getAltAz(tle, biTime, coords);
        if (altaz.getAltitude() > 0) return setSqueeze(tle, biTime, upper, coords, altaz);
        else return setSqueeze(tle, lower, biTime, coords, altaz);
    }

    /**
     * Finds the time the satellite first becomes visible due to sunlight. The method is recursive and uses a
     * bifurcation method to 'squeeze' the bounds towards the correct value. {@link #riseSqueeze(TLE, JD, JD, Coordinates, AltAz)}
     * and {@link #setSqueeze(TLE, JD, JD, Coordinates, AltAz)} should provide the initial bounds.
     * @param tle    TLE of the satellite.
     * @param lower  Lower bound of the possible first times, initial value should be the answer from
     *               {@link #riseSqueeze(TLE, JD, JD, Coordinates, AltAz)} and the method shouldn't
     *               be used if a value wasn't returned.
     * @param upper  Upper bound of the possible first times, initial value should be the answer from
     *               {@link #setSqueeze(TLE, JD, JD, Coordinates, AltAz)}.
     * @param geoPos GeoPosition of the pass.
     * @return  An AltAz object corresponding to the first visible time of the interested pass.
     * @throws NoLightException
     *              Signals if the satellite doesn't encounter any sunlight during a pass.
     */
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

    /**
     * Finds the time the satellite is last visible due to sunlight. The method is recursive and uses
     * a bifurcation method to 'squeeze' the bounds towards the correct value. {@link #firstSqueeze(TLE, JD, JD, Coordinates)}
     * should be called before this method to ensure there is a valid lower bound for the time-frame
     * the satellite is lit, as well as provide the initial lower bound.
     * @param tle    The TLE of the satellite.
     * @param lower  Lower bound of the possible last times, initial value should be the answer from
     *               {@link #firstSqueeze(TLE, JD, JD, Coordinates)} and the method shouldn't be
     *               used if  a value wasn't returned.
     * @param upper  Upper bound of the possible last times, initial value should be the value returned
     *               by {@link #setSqueeze(TLE, JD, JD, Coordinates, AltAz)}.
     * @param geoPos GeoPosition of the pass.
     * @param rtn    Value of the previous computation passed to the next call, initial is excepted
     *               to be {@code null}.
     * @return  An AltAz object corresponding to the last visible time of the interested pass.
     */
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

//    todo: there's gotta be a better/more accurate way to compute this
    /**
     * This method computes the time the satellite achieves its maximum altitude
     * during a pass. Lower and Upper bounds should not be outside the bounds
     * of the values returned by {@link #riseSqueeze(TLE, JD, JD, Coordinates, AltAz)}
     * and {@link #setSqueeze(TLE, JD, JD, Coordinates, AltAz)}.
     * @param tle    TLE of the satellite.
     * @param lower  Lower bound of the possible max times.
     * @param upper  Upper bound of the possible max times.
     * @param geoPos GeoPosition of the pass.
     * @param rtn    Value of the previous computation passed to the next call, initial is expected
     *               to be {@code null}.
     * @return  An AltAz object corresponding to the time the satellite achieves its
     *          maximum altitude.
     */
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