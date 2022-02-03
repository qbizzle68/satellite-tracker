package com.qbizzle.tracking;

import com.qbizzle.math.OrbitalMath;
import com.qbizzle.math.Vector;

import java.util.Arrays;

import static com.qbizzle.math.OrbitalMath.EARTH_EQUITORIAL_RADIUS;
import static com.qbizzle.math.OrbitalMath.EARTH_POLAR_RADIUS;

/** Container class for a location on earth expressed in geo-coordinates.
 * Latitudes are considered geodetic and range from [90, -90], positive being
 * north and negative being south. Longitudes range from [180, -180), positive
 * being east and negative being west.
 */
public class GeoPosition {
    private double[] m_coordinates;

    /** Constructs a GeoPosition to default values of 0, 0. */
    public GeoPosition() {
        this(0.0, 0.0);
    }

    /** Constructs a GeoPosition to the arguments.
     * @param lat Latitude in degrees.
     * @param lng Longitude in degrees.
     */
    public GeoPosition(double lat, double lng) {
        m_coordinates = new double[]{lat, lng};
    }

    /** Constructs a GeoPosition, with minutes and seconds arguments.
     * @param lat Latitude in degrees.
     * @param latMinutes Fraction of latitude in minutes.
     * @param latSeconds Fraction of minutes in seconds.
     * @param lng Longitude in degrees.
     * @param lngMinutes Fraction of longitude in minutes.
     * @param lngSeconds Fraction of minutes in seconds.
     */
    public GeoPosition(int lat, int latMinutes, double latSeconds, int lng, int lngMinutes, double lngSeconds) {
        this(lat + latMinutes/60.0 + latSeconds/3600.0,
                lng + lngMinutes/60.0 + lngSeconds/3600.0
        );
    }

    /** Constructs a GeoPosition from a position vector by computing
     * right ascension and declination first.
     * @param position Position vector in IJK reference frame.
     */
    public GeoPosition(Vector position) {
        m_coordinates = new double[2];
        double xyMag = Math.sqrt( Math.pow(position.x(), 2) + Math.pow(position.y(), 2) );
        double declination = Math.toDegrees( Math.atan2(position.z(), xyMag) );
        m_coordinates[0] = geocentricToGeodetic(declination);
        double lng = OrbitalMath.atan2(position.y(), position.x());
        m_coordinates[1] = Math.toDegrees( (lng > Math.PI) ? lng - 2*Math.PI : lng );
    }

    /// @name Overridden methods
    /// Overridden methods from Java Object
///@{

    /** Converts this object into a string format.
     * @return A string representation of this object.
     */
    @Override
    public String toString() {
        return "GeoPosition{" +
               "m_coordinates=" + Arrays.toString(m_coordinates) +
               '}';
    }

///@}

    /** Retrieves the latitude of this position.
     * @return The latitude in degrees.
     */
    public double getLatitude() {
        return m_coordinates[0];
    }

    /** Sets the latitude of this position.
     * @param lat The latitude in degrees.
     */
    public void setLatitude(double lat) {
        m_coordinates[0] = lat;
    }

    /** Retrieves the longitude of this position.
     * @return The longitude in degrees.
     */
    public double getLongitude() {
        return m_coordinates[1];
    }

    /** Sets the longitude of this position.
     * @param lng The longitude in degrees.
     */
    public void setLongitude(double lng) {
        m_coordinates[1] = lng;
    }

    /** Converts a geocentric longitude to a geodetic longitude. Can be used
     * as a declination to geodetic conversion for objects in LEO with minimal error.
     * @param lat The geocentric latitude in degrees.
     * @return The geodetic latitude in degrees.
     */
    public static double geocentricToGeodetic(double lat) {
        final double flattening = (EARTH_EQUITORIAL_RADIUS - EARTH_POLAR_RADIUS) / EARTH_EQUITORIAL_RADIUS;
        double lhs = Math.tan( Math.toRadians(lat) ) / Math.pow(1.0 - flattening, 2);
        return Math.toDegrees( Math.atan(lhs) );
    }

}
