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
     * @param timeZone The timezone of the time to convert.
     * @return The local sidereal time of Greenwich, England.
     */
    public static double siderealTime(JD julianDay, double timeZone) {
        double dt = julianDay.Value() - (timeZone / 24.0) - JD.J2000;
        return (18.697_374_558 + 24.065_709_824_419_08 * dt) % 24.0;
    }

}
