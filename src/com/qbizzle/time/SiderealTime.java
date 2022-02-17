/** @file
 * Contains a static class for handling computations involving sidereal time.
 */

package com.qbizzle.time;

/** Static class that can contains time methods for handling
 * computations related to sidereal and solar time differences.
 */
public class SiderealTime {

    /** Converts a certain solar time into sidereal time, useful for computing
     * Earth's geographic reference frame offset from the geocentric equitorial
     * celestial reference frame (offset of GMT to the first point of Aries).
     * @param julianDay The time to convert.
     * @return The local sidereal time of Greenwich, England.
     */
    public static double ST(JD julianDay) {
        double dt = julianDay.Value() - JD.J2000;
        return (18.697_374_558 + 24.065_709_824_419_08 * dt) % 24.0;
    }

    /** Converts a certain solar time into local sidereal time, useful for computing
     * your longitudinal offset from the geocentric equitorial celestial reference frame
     * (offset of your longitude to the first point of Aries).
     * @param julianDay The time to convert.
     * @param longitude The local longitude.
     * @return The local sidereal time.
     */
    public static double LST(JD julianDay, double longitude) {
        if (longitude < 0) longitude += 360.0;
        double lng2RA = longitude / 360.0 * 24.0;
        double LST = ST(julianDay) + lng2RA;
        return LST % 24.0;
    }

    /** Computes the earth offset angle from the celestial reference frame for
     * a given solar time.
     * @param julianDay The time to compute.
     * @return The offset angle in @em degrees.
     */
    public static double EarthOffsetAngle(JD julianDay) {
        return ST(julianDay) / 24.0 * 360.0;
    }

}
