package com.qbizzle.tracking;

import com.qbizzle.math.Vector;
import com.qbizzle.orbit.StateVectors;
import com.qbizzle.orbit.TLE;
import com.qbizzle.referenceframe.Axis;
import com.qbizzle.time.JD;
import com.qbizzle.time.SiderealTime;

import static com.qbizzle.rotation.Rotation.Rotate;


public class tracker {

    public static GeoPosition getPositionAt(TLE tle, double dt) {
        return getPositionAt(tle, new JD(tle).Future(dt));
    }

    // jd needs to be time in utc
    public static GeoPosition getPositionAt(TLE tle, JD t1) {
        StateVectors stateAtT1 = new StateVectors(tle, t1.Difference(new JD(tle)));
        double siderealTimeAtT1 = SiderealTime.siderealTime(t1, 0.0); // the timezone should go away if we incorporate it into the JD constructor
        double earthOffsetAngle = siderealTimeAtT1 / 24.0 * 360.0;
        Vector positionAtT1 = Rotate(Axis.Direction.Z, -earthOffsetAngle, stateAtT1.Position());
        return new GeoPosition(positionAtT1);
    }

    // may make getpositionat with COE, JD t0, JD t1 args.

    public static GeoPosition[] getGroundTrack(TLE tle, double dt, double interval) {
        return getGroundTrack(tle, new JD(tle).Future(dt), interval);
    }

    public static GeoPosition[] getGroundTrack(TLE tle, JD t1, double interval) {
        // The last interval will most likely not be of length interval, but should still be iterated.
        JD startTime = new JD(tle);
        int numIterations = (int)(t1.Difference(startTime) / interval) + 1;
        GeoPosition[] arrGeoPos = new GeoPosition[numIterations];

        for (int i = 0; i < numIterations - 1; i++) {
            JD currentTime = startTime.Future(interval * i);
            arrGeoPos[i] = getPositionAt(tle, currentTime);
        }
        arrGeoPos[numIterations-1] = getPositionAt(tle, t1);
        return arrGeoPos;
    }

}
