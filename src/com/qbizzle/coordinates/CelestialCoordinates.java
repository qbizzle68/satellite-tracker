/**
 * @file
 * Contains a coordinate container for celestial coordinates defined as
 * right ascension and declination.
 */

package com.qbizzle.coordinates;

import com.qbizzle.math.OrbitalMath;
import com.qbizzle.math.Vector;

/**
 * Coordinate container class with latitude defined as declination and
 * longitude defined as right ascension. Declination is measured in degrees
 * from -90 to 90, with north being positive and south being negative. Right
 * ascension is measured in time units, with 24 hours in 360 degrees and 60
 * seconds per hour and 60 minutes per second.
 */
@SuppressWarnings("unused")
public class CelestialCoordinates extends Coordinates {

    /**
     * Conversion constant for degrees per hour (360.0 / 24.0)
     */
    public static final double DEGREESPERHOUR = 15.0;

    /**
     * Constructs a CelestialCoordinates object with default values of 0.0.
     */
    public CelestialCoordinates() {
        super(0.0, 0.0);
    }

    /**
     * Constructs a CelestialCoordinates object with the given {@code declination}
     * and {@code rightAscension} parameters.
     * @param declination    The declination of the celestial object.
     * @param rightAscension The right ascension of the celestial object.
     */
    public CelestialCoordinates(double declination, double rightAscension) {
        this.latitude = declination % 90.0;
        this.longitude = modPositive(rightAscension);
    }

    /**
     * Constructs a CelestialCoordinates object with the parameters in
     * minute/arcminute and second/arcsecond format.
     * @param decDegrees Integer part of the declination.
     * @param decMinutes Arcminutes of declination.
     * @param decSeconds Arcseconds of declination.
     * @param raDegrees  Integer part of right ascension.
     * @param raMinutes  Minutes of right ascension.
     * @param raSeconds  Seconds of right ascension.
     */
    public CelestialCoordinates(int decDegrees, int decMinutes, double decSeconds, int raDegrees, int raMinutes, double raSeconds) {
        super(decDegrees, decMinutes, decSeconds, raDegrees, raMinutes, raSeconds);
    }

    /**
     * Constructs a CelestialCoordinates object from a geocentric position vector.
     * The length of the vector is not significant because the values are computed
     * using trigonometric ratios.
     * @param position Geocentric position vector.
     */
    public CelestialCoordinates(Vector position) {
        this.latitude = Math.toDegrees( Math.asin(position.z() / position.mag()) );
        this.longitude = Math.toDegrees( OrbitalMath.atan2(position.y(), position.x()) );
    }

    /**
     * Accessor method for the longitude of the coordinates. The returned value is the
     * coordinate's right ascension converted from hours to degrees.
     * @return Longitude of the coordinates.
     */
    @Override
    public double getLongitude() {
        return longitude * DEGREESPERHOUR;
    }

    /**
     * Sets the coordinates right ascension from a longitude measured in degrees.
     * @param longitude Longitude of the coordinate.
     */
    @Override
    public void setLongitude(double longitude) {
        this.longitude = modPositive(longitude);
    }

    /**
     * Accessor method for the right ascension of the coordinate in time units.
     * @return Right ascension of the coordinates.
     */
    public double getRightAscension() {
        return this.longitude;
    }

    /**
     * Sets the coordinate's right ascension in time units.
     * @param rightAscension Right ascension of the coordinate.
     */
    public void setRightAscension(double rightAscension) {
        this.longitude = modPositive(rightAscension);
    }

    /**
     * Accessor method for the coordinate declination. This method is a pseudonym
     * for {@code getLatitude()}.
     * @return The declination of the coordinate.
     */
    public double getDeclination() {
        return this.latitude;
    }

    /**
     * Sets the coordinate's declination in time units. This method is a pseudonym
     * for {@code setLatitude(double)}.
     * @param declination The declination of the coordinate.
     */
    public void setDeclination(double declination) {
        this.latitude = declination;
    }

    /**
     * Modulo function for finding the least non-negative residue of a floating point
     * number mod 24. This method is intended to ensure the right ascension is always
     * set to a number between 0 and 24 inclusive.
     * @param number Number to convert.
     * @return       Least non-negative residue of @p number mod 24.
     */
    private double modPositive(double number) {
        double rtn = number;
        if (rtn > 24.0 || rtn < -24.0)
            rtn %= 24.0;
        if (rtn < 0)
            rtn += 24.0;
        return rtn;
    }

}
