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
     * @param tle The TLE of the satellite to track.
     * @param dt The time of the desired position in solar days.
     * @return The GeoPosition of the satellite.
     */
    public static GeoPosition getGeoPositionAt(TLE tle, double dt) {
        return getGeoPositionAt(tle, new JD(tle).future(dt));
    }

    /** Computes the GeoPosition in which the satellite is directly overhead in the
     * past/future.
     * @param tle The TLE of the satellite to track.
     * @param t The time to compute the satellites position, in UTC.
     * @return The GeoPosition of the satellite.
     */
    public static GeoPosition getGeoPositionAt(TLE tle, JD t) {
        // todo: check which ephemeris model to use before calling
        StateVectors stateAtT1 = SGP4.propagate(tle, t);
        double earthOffsetAngle = SiderealTime.earthOffsetAngle(t);
        Vector positionAtT1 = Rotation.rotateFrom(Axis.Direction.Z, -earthOffsetAngle, stateAtT1.position());
        return new GeoPosition(positionAtT1);
    }

    /** Computes an array of GeoPositions of a satellite over a given period.
     * @param tle The TLE of the satellite to track.
     * @param dt The amount of time to track in solar days.
     * @param interval The interval of time between successive tracks in solar days.
     * @return An array of GeoPositions making up the ground track.
     */
    public static GeoPosition[] getGroundTrack(TLE tle, double dt, double interval) {
        return getGroundTrack(tle, new JD(tle).future(dt), interval);
    }

    /** Computes an array of GeoPositions of a satellite over a given period.
     * @param tle The TLE of the satellite to track.
     * @param t The time to track to in UTC.
     * @param interval The interval of time between successive tracks in solar days.
     * @return An array of GeoPositions making up the ground track.
     */
    public static GeoPosition[] getGroundTrack(TLE tle, JD t, double interval) {
        JD startTime = new JD(tle);
        int numIterations = (int)(t.difference(startTime) / interval) + 1;
        GeoPosition[] arrGeoPos = new GeoPosition[numIterations];

        for (int i = 0; i < numIterations - 1; i++) {
            JD currentTime = startTime.future(interval * i);
            arrGeoPos[i] = getGeoPositionAt(tle, currentTime);
        }
        // The last interval will most likely not be of length interval, but should still be iterated.
        arrGeoPos[numIterations-1] = getGeoPositionAt(tle, t);
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
        return plotGroundTrack(tle, new JD(tle).future(dt), interval, filename);
    }

    /** Prints an array of GeoPositions of a satellite track to a CSV file.
     * @param tle The TLE of the satellite to track.
     * @param t The time to track to in UTC.
     * @param interval The interval of time between successive tracks in solar days.
     * @param filename The file to print to, should contain .CSV extensions.
     * @return True if the print was successful, false if otherwise.
     * @throws IOException From FileWriter.
     * @todo find out if we can handle any exceptions for FileWriter, or need to do checks to find out
     *   if the file is being written. If no checks exist theres really no reason to return a boolean
     */
    public static boolean plotGroundTrack(TLE tle, JD t, double interval, String filename) throws IOException {
        final int flushLimit = 10; // this is just a guess right now, previous files have been failing a little after 1000
        FileWriter writer = new FileWriter(filename);
        GeoPosition[] arrGroundTrack = getGroundTrack(tle, t, interval);
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
     * @param geoPosition
     *                  GeoPosition which corresponds to the center of the reference frame.
     * @return          The position vector in SEZ reference frame.
     */
    public static Vector getSEZPosition(TLE tle, double dt, GeoPosition geoPosition) {
        return getSEZPosition(tle, new JD(tle).future(dt), geoPosition);
    }

    /**
     * Computes the position of a satellite in a topocentric reference frame.
     * @param tle       TLE of the satellite.
     * @param t         Time in which to find the satellite position.
     * @param geoPosition
     *                  GeoPosition which corresponds to the center of the reference frame.
     * @return          The position vector in SEZ reference frame.
     */
    public static Vector getSEZPosition(TLE tle, JD t, GeoPosition geoPosition) {
        // todo: check which ephemeris model to use before calling
        return getSEZPosition(
                SGP4.propagate(tle, t).position(),
                t,
                geoPosition
        );
    }

    /**
     * Converts a position vector from a geocentric reference frame to a topocentric
     * reference frame.
     * @param position      Position vector of the satellite in the earth centered reference frame.
     * @param t             Time in which the satellite occupies this position. This is needed to find
     *                      the earth rotation offset from the celestial coordinate system.
     * @param geoPosition   GeoPosition which corresponds to the center of the reference frame.
     * @return              The position vector in SEZ reference frame.
     */
    public static Vector getSEZPosition(Vector position, JD t, GeoPosition geoPosition) {
        double localSiderealTime = SiderealTime.getLocalSiderealTime(t, geoPosition.getLongitude()) * DEGREES_PER_HOUR;
        Vector geoPositionVector = getToposPosition(t, geoPosition);
        Matrix sezToIJK = Rotation.getEulerMatrix(
                EulerOrderList.ZYX,
                new EulerAngles(
                        localSiderealTime,
                        90-geoPosition.getLatitude(),
                        0
                )
        );
        return rotateTranslate(sezToIJK, geoPositionVector, position);
    }

    /**
     * Computes the position vector of a GeoPosition.
     * @param t             Time the position vector is needed. This is necessary for adjusting
     *                      for the earth's rotation offset.
     * @param geoPosition   The GeoPosition corresponding to the center of the reference frame.
     * @return              The position vector in earth centric reference frame.
     */
    public static Vector getToposPosition(JD t, GeoPosition geoPosition) {
        // todo: account for elevation with this radius
        double radiusAtLat = GeoPosition.radiusAtLatitude(geoPosition.getLatitude());
        double geocentricLat = GeoPosition.geodeticToGeocentric(geoPosition.getLatitude());
        double localSiderealTime = SiderealTime.getLocalSiderealTime(t, geoPosition.getLongitude()) * DEGREES_PER_HOUR;
        return new Vector(
                radiusAtLat * Math.cos( Math.toRadians(geocentricLat) ) * Math.cos( Math.toRadians(localSiderealTime) ),
                radiusAtLat * Math.cos( Math.toRadians(geocentricLat) ) * Math.sin( Math.toRadians(localSiderealTime) ),
                radiusAtLat * Math.sin( Math.toRadians(geocentricLat) )
        );
    }

    /**
     * Generates an AltAz object referencing the altitude and azimuth of a satellite at a
     * given time.
     * @param tle           TLE of the satellite.
     * @param t             Time to find the position.
     * @param geoPosition   GeoPosition to find the relative altitude and azimuth for.
     * @return              An AltAz object with the epoch set as @p t.
     */
    public static AltAz getAltAz(TLE tle, JD t, GeoPosition geoPosition) {
        return getAltAz(
                getSEZPosition(tle, t, geoPosition),
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
        public static boolean isAboveHorizon(TLE tle, JD t, GeoPosition geoPosition) {
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

    /**
     * Computes pass information for any and all passes over a GeoPosition for a given duration.
     * @param tle       TLE of the satellite. Note that the TLE becomes less accurate as time
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
        final double dt = 10 / 86400.0; // 10 seconds
        java.util.Vector<SatellitePass> passList = new java.util.Vector<>();
        JD currentTime = startTime;
        SatellitePass satPass;
        do {
            try {
                satPass = Tracker.getPassInfo(tle, currentTime, geoPosition);
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

//    todo: does this affect passList in the larger scope?
    public static java.util.Vector<SatellitePass> filterPasses(java.util.Vector<SatellitePass> passList, PassFilter filter) {
        Iterator<SatellitePass> iter = passList.iterator();
        while (iter.hasNext()) {
            SatellitePass pass = iter.next();

            switch (filter.getFilterType()) {
                case MINIMUM_HEIGHT -> {
                    if (pass.getMaxHeight() < filter.getMinimumHeight())
                        iter.remove();
                }
                case MINIMUM_DURATION -> {
                    if (pass.getDisappearTime().difference(pass.getRiseTime()) < filter.getMinimumDuration() / 1440.0)
                        iter.remove();
                }
                case MINIMUM_HEIGHT_DURATION -> {
                    if (pass.getMaxHeight() < filter.getMinimumHeight())
                        iter.remove();
                    else if (pass.getDisappearTime().difference(pass.getRiseTime()) < filter.getMinimumDuration() / 1440.0)
                        iter.remove();
                }
            }
        }

        return passList;
    }

    /**
     * Computes the right-ascension and declination of a satellite.
     * @param tle           TLE of the satellite.
     * @param t             Time to find the satellite's position.
     * @param geoPosition   GeoPosition viewing the satellite.
     * @return              A Coordinates object where the latitude is the declination
     *                      and the longitude is the right-ascension.
     */
    static public CelestialCoordinates getCelestialCoordinates(TLE tle, JD t, GeoPosition geoPosition) {
        Vector pos = getSEZPosition(tle, t, geoPosition);
        pos = Rotation.rotateFrom(
                EulerOrderList.ZYX,
                new EulerAngles(
                        SiderealTime.getLocalSiderealTime(t, geoPosition.getLongitude()) * DEGREES_PER_HOUR,
                        90 - geoPosition.getLatitude(),
                        0
                ),
                pos
        );
        double ra = OrbitalMath.atan2(pos.y(), pos.x());
        final double HOURS_PER_RADIAN = 24.0 * (2.0 * Math.PI);
        return new CelestialCoordinates(
                ra * HOURS_PER_RADIAN,
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
    static public CelestialCoordinates adjustRaDec(JD t, CelestialCoordinates RaDec) {
        double JDCenturies = (t.value() - JD.J2000) / 36525.0;
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
        static private AltAz riseSqueeze(TLE tle, JD lower, JD upper, GeoPosition geoPosition, AltAz rtn) {
        if (upper.difference(lower) <= squeezeEpsilon) {
            if (rtn.getAltitude() + altitudeEpsilon > 0) return rtn;
            else throw new NoPassException("No overhead pass at " + upper.date());
        }
        JD biTime = lower.future((upper.value() - lower.value()) / 2.0);
            AltAz altaz = Tracker.getAltAz(tle, biTime, geoPosition);
            if (altaz.getAltitude() > 0) return riseSqueeze(tle, lower, biTime, geoPosition, altaz);
            else return riseSqueeze(tle, biTime, upper, geoPosition, altaz);
        }

    /**
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
    static private AltAz setSqueeze(TLE tle, JD lower, JD upper, GeoPosition geoPosition, AltAz rtn) {
        if (upper.difference(lower) <= squeezeEpsilon) return rtn;
        JD biTime = lower.future((upper.value() - lower.value()) / 2.0);
        AltAz altaz = Tracker.getAltAz(tle, biTime, geoPosition);
        if (altaz.getAltitude() > 0) return setSqueeze(tle, biTime, upper, geoPosition, altaz);
        else return setSqueeze(tle, lower, biTime, geoPosition, altaz);
    }

    /**
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

    /**
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

//    todo: there's gotta be a better/more accurate way to compute this
    /**
     * This method computes the time the satellite achieves its maximum altitude
     * during a pass. Lower and Upper bounds should not be outside the bounds
     * of the values returned by {@link #riseSqueeze(TLE, JD, JD, GeoPosition, AltAz)}
     * and {@link #setSqueeze(TLE, JD, JD, GeoPosition, AltAz)}.
     * @param tle    TLE of the satellite.
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

}