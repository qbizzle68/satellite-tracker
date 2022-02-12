package com.qbizzle.orbit;

public class MeanElements {
    public final double n0;
    public final double e0;
    public final double i0;
    public final double M0;
    public final double w0;
    public final double L0;
    public final double ndot0;
    public final double nddot0;

    public MeanElements(TLE tle) {
        n0 = tle.MeanMotion() * 2 * Math.PI / 1440.0;
        e0 = tle.Eccentricity();
        i0 = Math.toRadians( tle.Inclination() );
        M0 = Math.toRadians( tle.MeanAnomaly() );
        w0 = Math.toRadians( tle.AOP() );
        L0 = Math.toRadians( tle.LAN() );
        ndot0 = tle.MeanMotionDot() * 2 * Math.PI / (1440.0 * 1440.0);
        nddot0 = tle.MeanMotionDDot() * 2 * Math.PI / (1440.0 * 1440.0 * 1440.0);
    }
}
