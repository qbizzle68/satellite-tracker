/** @file
 * Contains the base Coordinates class that defines the class structure for
 * coordinate like objects.
 */

package com.qbizzle.coordinates;

/**
 * Coordinates class to be a base class for other coordinate objects. It is
 * not abstract, so it could be used as a generic coordinate container if need be.
 * Latitude is modulated to a value between -90 and 90, positive indicating north
 * and negative indicating south. Longitude is modulated to a value between -180
 * and 180, positive being east and negative west.
 */
public class Coordinates {
    /**
     * Any latitude like angle, i.e. the shortest angle from the equitorial plane.
     */
    protected double latitude;
    /**
     * Any longitude like angle, i.e. the angle from a prime meridian to another.
     */
    protected double longitude;

    /**
     * Constructs an instance with default values of 0.0 for latitude and longitude.
     */
    public Coordinates() {
        this(0.0, 0.0);
    }

    /**
     * Constructs an instance with the {@code latitude} and {@code longitude} parameters.
     * @param latitude  Latitude of the coordinate.
     * @param longitude Longitude of the coordinate.
     */
    public Coordinates(double latitude, double longitude) {
        this.latitude = latitude % 90.0;
        this.longitude = longitude % 180.0;
    }

    /**
     * Constructs an instance with coordinates in arcminute and arcsecond format.
     * @param latDegrees Integer part of the latitude.
     * @param latMinutes Latitude arcminutes.
     * @param latSeconds Latitude arcseconds.
     * @param lngDegrees Integer part of the longitude.
     * @param lngMinutes Longitude arcminutes.
     * @param lngSeconds Longitude arcseconds.
     */
    public Coordinates(int latDegrees, int latMinutes, double latSeconds, int lngDegrees, int lngMinutes, double lngSeconds) {
        this.latitude = latDegrees
                + latMinutes / 60.0
                + latSeconds / 3600.0;
        this.longitude = lngDegrees
                + lngMinutes / 60.0
                + lngSeconds / 3600.0;
    }

    /**
     * Accessor method for the latitude.
     * @return The latitude coordinate.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Setter method for the latitude.
     * @param latitude Latitude of the coordinate.
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude % 90.0;
    }

    /**
     * Accessor method for the longitude.
     * @return The longitude coordinate.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Setter method for the longitude.
     * @param longitude Longitude of the coordinate.
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude % 180.0;
    }

    /**
     * Creates a string representation of the coordinate.
     * @return A string representation of the coordinates.
     */
    @Override
    public String toString() {
        return "RenameToCoordinates{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

}
