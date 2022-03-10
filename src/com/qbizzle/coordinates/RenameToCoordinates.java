package com.qbizzle.coordinates;

public class RenameToCoordinates {
    double latitude;
    double longitude;

    public RenameToCoordinates() {
        this(0.0, 0.0);
    }

    public RenameToCoordinates(double latitude, double longitude) {
        this.latitude = modPositive(latitude, 90.0);
        this.longitude = modPositive(longitude, 180.0);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = modPositive(latitude, 90.0);
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = modPositive(longitude, 180.0);
    }

    private double modPositive(double number, double modulus) {
        double rtn = number;
        if (rtn > modulus || rtn < -modulus)
            rtn %= modulus;
        if (rtn < 0)
            rtn += modulus;
        return rtn;
    }

    @Override
    public String toString() {
        return "RenameToCoordinates{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
