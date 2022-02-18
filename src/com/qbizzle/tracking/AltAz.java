/** @file
 * This file contains the AltAz class for containing the altitude and
 * azimuth angles of a satellite for a specific GeoPosition.
 */

package com.qbizzle.tracking;

import com.qbizzle.time.JD;

import java.util.Objects;

/** Container class for holding altitude and azimuth angles
 * for a position relative to a GeoPosition.
 */
public class AltAz implements Cloneable {

    private double altitude;
    private double azimuth;
    private JD epoch;

    /** Constructs an AltAz object with the given parameters.
     * @param altitude The altitude angle of the satellite.
     * @param azimuth The azimuth angle of the satellite from north moving clockwise.
     */
    public AltAz(double altitude, double azimuth) {
        this.azimuth = azimuth;
        this.altitude = altitude;
        epoch = new JD(0);
    }

    public AltAz(double altitude, double azimuth, JD epoch) {
        this.azimuth = azimuth;
        this.altitude = altitude;
        this.epoch = epoch;
    }

    @Override
    public String toString() {
        return "AltAz{" +
                "altitude=" + altitude +
                ", azimuth=" + azimuth +
                ", epoch=" + epoch +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AltAz altAz = (AltAz) o;
        return Double.compare(altAz.altitude, altitude) == 0 && Double.compare(altAz.azimuth, azimuth) == 0 && epoch.equals(altAz.epoch);
    }

    @Override
    public int hashCode() {
        return Objects.hash(altitude, azimuth, epoch);
    }

    // add clone once JD is cloneable

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

    public JD getEpoch() {
        return new JD(epoch.Value());
    }

    public void setEpoch(JD epoch) {
        this.epoch = epoch;
    }

}
