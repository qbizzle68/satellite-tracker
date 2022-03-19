/**
 * @file
 * Contains the GeoPosition class that defines a more specific coordinate container
 * object from the Coordinate class.
 */

package com.qbizzle.coordinates;

import com.qbizzle.math.OrbitalMath;
import com.qbizzle.math.Vector;

import static com.qbizzle.math.OrbitalMath.EARTH_EQUITORIAL_RADIUS;
import static com.qbizzle.math.OrbitalMath.EARTH_POLAR_RADIUS;

/**
 * A coordinate container class that defines a geographic location as well as an
 * elevation above sea level. Other static methods exist to assist in earth radius
 * calculations (for variance with latitude) as well as geocentric and geodetic
 * conversions.
 */
@SuppressWarnings("unused")
public class GeoPosition extends Coordinates {
    /**
     * Flattening of earths radius, defined as the radio of the difference between
     * equitorial and polar radii, and the equitorial radius. (Re - Rp) / Re
     */
    public static final double FLATTENING = (OrbitalMath.EARTH_EQUITORIAL_RADIUS
            - OrbitalMath.EARTH_POLAR_RADIUS) / OrbitalMath.EARTH_EQUITORIAL_RADIUS;

    /**
     * Elevation of the geoposition, used to find a more accurate topocentric
     * position vector.
     */
    private double elevation;

    /**
     * Constructs a GeoPosition object with all default values of 0.0.
     */
    public GeoPosition() {
        super(0.0, 0.0);
        this.elevation = 0.0;
    }

    /**
     * Constructs a GeoPosition object with the {@code latitude} and {@code longitude}
     * parameters, and sets the elevation to the default value of 0.0.
     * @param latitude  The latitude of the geoposition.
     * @param longitude The longitude of the geoposition.
     */
    public GeoPosition(double latitude, double longitude) {
        super(latitude, longitude);
        this.elevation = 0.0;
    }

    /**
     * Constructs a GeoPosition object with the respective parameters.
     * @param latitude  The latitude of the geoposition.
     * @param longitude The longitude of the geoposition.
     * @param elevation The elevation of the geoposition in meters.
     */
    public GeoPosition(double latitude, double longitude, double elevation) {
        super(latitude, longitude);
        this.elevation = elevation;
    }

    /**
     * Constructs a GeoPosition object with the arcminute and arcsecond formats. There is no
     * elevation parameter in the constructor so the {@code GeoPosition#setelevation(double)} method
     * needs to be called.
     * @param latDegrees The latitude of the geoposition.
     * @param latMinutes The arcminutes of the geoposition.
     * @param latSeconds The arcseconds of the geoposition.
     * @param lngDegrees The longitude of the geoposition.
     * @param lngMinutes The arcminutes of the geoposition.
     * @param lngSeconds The arcseconds of the geoposition.
     */
    public GeoPosition(int latDegrees, int latMinutes, double latSeconds, int lngDegrees, int lngMinutes, double lngSeconds) {
        super(latDegrees, latMinutes, latSeconds, lngDegrees, lngMinutes, lngSeconds);
    }

    /**
     * Constructs a GeoPosition object from a topocentric position vector. The size
     * of the vector is irrelevant because the computation is done by trigonometric ratios.
     * @param position Position vector of the geolocation.
     */
    public GeoPosition(Vector position) {
        double geocentricLatitude = Math.toDegrees( Math.asin(position.z() / position.mag()) );
        this.latitude = geocentricToGeodetic(geocentricLatitude);
        this.longitude = Math.toDegrees( OrbitalMath.atan2(position.y(), position.x()) );
        this.elevation = position.mag() - radiusAtLatitude(this.latitude);
    }

    /**
     * Accessor method for the elevation.
     * @return The elevation of the geoposition.
     */
    public double getElevation() {
        return elevation;
    }

    /**
     * Setter method for the elevation.
     * @param elevation Elevation of the geoposition in meters.
     */
    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    /**
     * Creates a string representation of the geoposition and elevation.
     * @return A string representation of the GeoPosition object.
     */
    @Override
    public String toString() {
        return "GeoPosition{" +
                "elevation=" + elevation +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    /**
     * Converts a geocentric latitude to a geodetic latitude. Can be used
     * as a declination to geodetic conversion for objects in LEO with minimal error.
     * @param latitude  The geocentric latitude.
     * @return          The geodetic latitude.
     */
    public static double geocentricToGeodetic(double latitude) {
        double latMod90 = latitude % 90;
        double lhs = Math.tan( Math.toRadians(latMod90) ) / Math.pow(1.0 - FLATTENING, 2);
        return Math.toDegrees( Math.atan(lhs) );
    }

    /** Converts a geodetic latitude to a geocentric latitude. Can be used as a geodetic
     * to declination conversion for objects in LEO with minimal error.
     * @param latitude  The geodetic latitude.
     * @return          The geocentric latitude.
     */
    public static double geodeticToGeocentric(double latitude) {
        double latMod90 = latitude % 90;
        double coef = Math.pow(1.0 - FLATTENING, 2);
        double rhs = coef * Math.tan( Math.toRadians(latMod90) );
        return Math.toDegrees( Math.atan(rhs) );
    }

    /**
     * Computes the geocentric radius for a given @em geodetic latitude.
     * @param geodeticLatitude  The geodetic latitude
     * @return                  Radius in meters.
     */
    public static double radiusAtLatitude(double geodeticLatitude) {
        if (geodeticLatitude == 0.0) return EARTH_EQUITORIAL_RADIUS;
        else if (geodeticLatitude == 90.0) return EARTH_POLAR_RADIUS;
        double geocentricLat = Math.toRadians( geodeticToGeocentric(geodeticLatitude) );
        double bcos2 = Math.pow(EARTH_POLAR_RADIUS * Math.cos(geocentricLat), 2);
        double asin2 = Math.pow(EARTH_EQUITORIAL_RADIUS * Math.sin(geocentricLat), 2);
        return (EARTH_POLAR_RADIUS * EARTH_EQUITORIAL_RADIUS) / Math.sqrt(bcos2 + asin2);
    }

}
