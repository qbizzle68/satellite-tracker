/** @file
 * This file contains the AltAz class for containing the altitude and
 * azimuth angles of a satellite for a specific GeoPosition.
 */

package com.qbizzle.tracking;

/** Container class for holding altitude and azimuth angles
 * for a position relative to a GeoPosition.
 */
public class AltAz {

    private double altitude;
    private double azimuth;

    /** Constructs an AltAz object with the given parameters.
     * @param altitude The altitude angle of the satellite.
     * @param azimuth The azimuth angle of the satellite from north moving clockwise.
     */
    public AltAz(double altitude, double azimuth) {
        this.azimuth = azimuth;
        this.altitude = altitude;
    }

    /** Retrieves the azimuth of this object.
     * @return The azimuth angle.
     */
    public double getAzimuth() {
        return azimuth;
    }

    /** Sets the azimuth of this object.
     * @param azimuth The azimuth angle.
     */
    public void setAzimuth(double azimuth) {
        this.azimuth = azimuth;
    }

    /** Retrieves the altitude angle of this object.
     * @return The altitude angle.
     */
    public double getAltitude() {
        return altitude;
    }

    /** Sets the altitude of this object.
     * @param altitude The altitude angle.
     */
    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

}
