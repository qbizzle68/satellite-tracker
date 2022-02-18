package com.qbizzle.tracking;

import com.qbizzle.time.JD;

public class SatellitePass {
    private final JD riseTime, setTime, maxTime;
    private final String riseDirection, setDirection, maxDirection;
    private final double riseHeight, setHeight, maxHeight;

//    public SatellitePass(JD riseTime, AltAz rise, JD setTime, AltAz set, JD maxTime, AltAz max) {
    public SatellitePass(AltAz rise, AltAz set, AltAz max) {
        // todo: check if this is good enough or are we creating a shallow copy?
        this.riseTime = rise.getEpoch();
        this.setTime = set.getEpoch();
        this.maxTime = max.getEpoch();
        riseHeight = rise.getAltitude();
        setHeight = set.getAltitude();
        maxHeight = max.getAltitude();
        riseDirection = setDirections(rise.getAzimuth());
        setDirection = setDirections(set.getAzimuth());
        maxDirection = setDirections(max.getAzimuth());
    }

    @Override
    public String toString() {
        return "SatellitePass{" +
                "riseTime=" + riseTime +
                ", riseDirection='" + riseDirection + '\'' +
                ", riseHeight=" + riseHeight + ",\n\t " +
                "setTime=" + setTime +
                ", setDirection='" + setDirection + '\'' +
                ", setHeight=" + setHeight + ",\n\t " +
                "maxTime=" + maxTime +
                ", maxDirection='" + maxDirection + '\'' +
                ", maxHeight=" + maxHeight +
                '}';
    }

    public JD getRiseTime() {
        return new JD(riseTime.Value());
    }

    public JD getSetTime() {
        return new JD(setTime.Value());
    }

    public JD getMaxTime() {
        return new JD(maxTime.Value());
    }

    public double getRiseHeight() {
        return riseHeight;
    }

    public double getSetHeight() {
        return setHeight;
    }

    public double getMaxHeight() {
        return maxHeight;
    }

    public String getRiseDirection() {
        return riseDirection;
    }

    public String getSetDirection() {
        return setDirection;
    }

    public String getMaxDirection() {
        return maxDirection;
    }

    // azimuth in degrees from north
    private String setDirections(double azimuth) {
        azimuth %= 360.0;
        if (azimuth < 0) azimuth += 360.0;
        if (azimuth < 11.25 || azimuth >= 348.75) return "N";
        else if (azimuth < 33.75) return "NNE";
        else if (azimuth < 56.25) return "NE";
        else if (azimuth < 78.75) return "ENE";
        else if (azimuth < 101.25) return "E";
        else if (azimuth < 123.75) return "ESE";
        else if (azimuth < 146.25) return "SE";
        else if (azimuth < 168.75) return "SSE";
        else if (azimuth < 191.25) return "S";
        else if (azimuth < 213.75) return "SSW";
        else if (azimuth < 236.25) return "SW";
        else if (azimuth < 258.75) return "WSW";
        else if (azimuth < 281.25) return "W";
        else if (azimuth < 303.75) return "WNW";
        else if (azimuth < 326.25) return "NW";
        else return "NNW";
    }

}
