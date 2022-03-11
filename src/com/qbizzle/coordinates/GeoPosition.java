package com.qbizzle.coordinates;

import com.qbizzle.math.OrbitalMath;
import com.qbizzle.math.Vector;

import static com.qbizzle.math.OrbitalMath.EARTH_EQUITORIAL_RADIUS;
import static com.qbizzle.math.OrbitalMath.EARTH_POLAR_RADIUS;

public class GeoPosition extends Coordinates {
    public static final double FLATTENING = (OrbitalMath.EARTH_EQUITORIAL_RADIUS - OrbitalMath.EARTH_POLAR_RADIUS) / OrbitalMath.EARTH_EQUITORIAL_RADIUS;
    private double elevation;

    public GeoPosition() {
        this(0.0, 0.0);
    }

    public GeoPosition(double latitude, double longitude) {
        super(latitude, longitude);
        this.elevation = 0.0;
    }

    public GeoPosition(double latitude, double longitude, double elevation) {
        super(latitude, longitude);
        this.elevation = elevation;
    }

    public GeoPosition(int latDegrees, int latMinutes, double latSeconds, int lngDegrees, int lngMinutes, double lngSeconds) {
        super(latDegrees, latMinutes, latSeconds, lngDegrees, lngMinutes, lngSeconds);
    }

    public GeoPosition(Vector position) {
        double geocentricLatitude = Math.toDegrees( Math.asin(position.z() / position.mag()) );
        this.latitude = geocentricToGeodetic(geocentricLatitude);
        this.longitude = Math.toDegrees( OrbitalMath.atan2(position.y(), position.x()) );
        this.elevation = position.mag() - radiusAtLatitude(this.latitude);
    }

    public double getElevation() {
        return elevation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    @Override
    public String toString() {
        return "GeoPosition{" +
                "elevation=" + elevation +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    /** Converts a geocentric latitude to a geodetic latitude. Can be used
     * as a declination to geodetic conversion for objects in LEO with minimal error.
     * @param lat The geocentric latitude in degrees.
     * @return The geodetic latitude in degrees.
     */
    public static double geocentricToGeodetic(double lat) {
        double lhs = Math.tan( Math.toRadians(lat) ) / Math.pow(1.0 - FLATTENING, 2);
        return Math.toDegrees( Math.atan(lhs) );
    }

    /** Converts a geodetic latitude to a geocentric latitude. Can be used as a geodetic
     * to declination conversion for objects in LEO with minimal error.
     * @param lat The geodetic latitude in degrees.
     * @return The geocentric latitude in degrees.
     */
    public static double geodeticToGeocentric(double lat) {
        double coef = Math.pow(1.0 - FLATTENING, 2);
        double rhs = coef * Math.tan( Math.toRadians(lat) );
        return Math.toDegrees( Math.atan(rhs) );
    }

    /**
     * Computes the geocentric radius for a given @em geodetic latitude.
     * @param geodeticLat   Geodetic latitude in degrees, north direction being
     *                      positive, south being negative.
     * @return              Radius in meters.
     */
    public static double radiusAtLatitude(double geodeticLat) {
        if (geodeticLat == 0.0) return EARTH_EQUITORIAL_RADIUS;
        else if (geodeticLat == 90.0) return EARTH_POLAR_RADIUS;
        double geocentricLat = Math.toRadians( geodeticToGeocentric(geodeticLat) );
        double bcos2 = Math.pow(EARTH_POLAR_RADIUS * Math.cos(geocentricLat), 2);
        double asin2 = Math.pow(EARTH_EQUITORIAL_RADIUS * Math.sin(geocentricLat), 2);
        return (EARTH_POLAR_RADIUS * EARTH_EQUITORIAL_RADIUS) / Math.sqrt(bcos2 + asin2);
    }

}
