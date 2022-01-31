package com.qbizzle.tracking;

import com.qbizzle.math.OrbitalMath;
import com.qbizzle.math.Vector;

import static com.qbizzle.math.OrbitalMath.EARTH_EQUITORIAL_RADIUS;
import static com.qbizzle.math.OrbitalMath.EARTH_POLAR_RADIUS;

public class GeoPosition {
    private double[] m_coordinates;

    public GeoPosition() {
        this(0.0, 0.0);
    }

    public GeoPosition(double lat, double lng) {
        m_coordinates = new double[]{lat, lng};
    }

    public GeoPosition(Vector position) {
        m_coordinates = new double[2];
        double xyMag = Math.sqrt( Math.pow(position.x(), 2) + Math.pow(position.y(), 2) );
        double declination = Math.atan2(position.z(), xyMag);
        m_coordinates[0] = geocentricToGeodetic(declination);
        double lng = OrbitalMath.atan2(position.y(), position.x());
        m_coordinates[1] = (lng > Math.PI) ? lng - 2*Math.PI : lng;
    }

    public double getLatitude() {
        return m_coordinates[0];
    }

    public void setLatitude(double lat) {
        m_coordinates[0] = lat;
    }

    public double getLongitude() {
        return m_coordinates[1];
    }

    public void setLongitude(double lng) {
        m_coordinates[1] = lng;
    }

    public static double geocentricToGeodetic(double lat) {
        final double flattening = (EARTH_EQUITORIAL_RADIUS - EARTH_POLAR_RADIUS) / EARTH_EQUITORIAL_RADIUS;
        double lhs = Math.tan( Math.toRadians(lat) ) / Math.pow(1.0 - flattening, 2);
        return Math.toDegrees( Math.atan(lhs) );
    }

}
