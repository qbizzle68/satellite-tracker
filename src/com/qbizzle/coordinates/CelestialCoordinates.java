package com.qbizzle.coordinates;

import com.qbizzle.math.OrbitalMath;
import com.qbizzle.math.Vector;

public class CelestialCoordinates extends Coordinates {
    public static final double DEGREESPERHOUR = 15.0;

    public CelestialCoordinates() {
        super(0.0, 0.0);
    }

    public CelestialCoordinates(double declination, double rightAscension) {
        this.latitude = declination % 90.0;
        this.longitude = modPositive(rightAscension);
    }

    public CelestialCoordinates(int latDegrees, int latMinutes, double latSeconds, int lngDegrees, int lngMinutes, double lngSeconds) {
        super(latDegrees, latMinutes, latSeconds, lngDegrees, lngMinutes, lngSeconds);
    }

    public CelestialCoordinates(Vector position) {
        this.latitude = Math.toDegrees( Math.asin(position.z() / position.mag()) );
        this.longitude = Math.toDegrees( OrbitalMath.atan2(position.y(), position.x()) );
    }

    @Override
    public double getLongitude() {
        return longitude * DEGREESPERHOUR;
    }

    @Override
    public void setLongitude(double longitude) {
        this.longitude = modPositive(longitude);
    }

    public double getRightAscension() {
        return this.longitude;
    }

    public void setRightAscension(double rightAscension) {
        this.longitude = modPositive(rightAscension);
    }

    public double getDeclination() {
        return this.latitude;
    }

    public void setDeclination(double declination) {
        this.latitude = declination;
    }

    private double modPositive(double number) {
        double rtn = number;
        if (rtn > 24.0 || rtn < -24.0)
            rtn %= 24.0;
        if (rtn < 0)
            rtn += 24.0;
        return rtn;
    }

}
