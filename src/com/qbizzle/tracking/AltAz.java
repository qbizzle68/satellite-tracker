package com.qbizzle.tracking;

public class AltAz {

    private double azimuth;
    private double altitude;

    public AltAz(double azimuth, double altitude) {
        this.azimuth = azimuth;
        this.altitude = altitude;
    }

    public double getAzimuth() {
        return azimuth;
    }

    public void setAzimuth(double azimuth) {
        this.azimuth = azimuth;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

}
