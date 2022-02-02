package com.qbizzle.tracking;

import com.qbizzle.math.OrbitalMath;
import com.qbizzle.math.Vector;

import java.util.Arrays;

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

    public GeoPosition(int lat, int latMinutes, double latSeconds, int lng, int lngMinutes, double lngSeconds) {
        this(lat + latMinutes/60.0 + latSeconds/3600.0,
                lng + lngMinutes/60.0 + lngSeconds/3600.0
        );
    }

    public GeoPosition(Vector position) {
        m_coordinates = new double[2];
        double xyMag = Math.sqrt( Math.pow(position.x(), 2) + Math.pow(position.y(), 2) );
        double declination = Math.toDegrees( Math.atan2(position.z(), xyMag) );
        m_coordinates[0] = geocentricToGeodetic(declination);
        double lng = OrbitalMath.atan2(position.y(), position.x());
        m_coordinates[1] = Math.toDegrees( (lng > Math.PI) ? lng - 2*Math.PI : lng );
    }

    @Override
    public String toString() {
        return "GeoPosition{" +
               "m_coordinates=" + Arrays.toString(m_coordinates) +
               '}';
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
