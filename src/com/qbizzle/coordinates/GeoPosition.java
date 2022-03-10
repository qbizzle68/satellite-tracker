package com.qbizzle.coordinates;

public class GeoPosition extends RenameToCoordinates {
    private double elevation;

    public GeoPosition() {
        this(0.0, 0.0);
    }

    public GeoPosition(double latitude, double longitude) {
        super(latitude, longitude);
    }

    public GeoPosition(double latitude, double longitude, double elevation) {
        super(latitude, longitude);
        this.elevation = elevation;
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
}
