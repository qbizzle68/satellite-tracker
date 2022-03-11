package com.qbizzle.coordinates;

public class Coordinates {
    double latitude;
    double longitude;

    public Coordinates() {
        this(0.0, 0.0);
    }

    public Coordinates(double latitude, double longitude) {
        this.latitude = latitude % 90.0;
        this.longitude = longitude % 180.0;
    }

    public Coordinates(int latDegrees, int latMinutes, double latSeconds, int lngDegrees, int lngMinutes, double lngSeconds) {
        this.latitude = latDegrees
                + latMinutes / 60.0
                + latSeconds / 3600.0;
        this.longitude = lngDegrees
                + lngMinutes / 60.0
                + lngSeconds / 3600.0;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude % 90.0;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude % 180.0;
    }

    @Override
    public String toString() {
        return "RenameToCoordinates{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
