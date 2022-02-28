package com.qbizzle.tracking;

import com.qbizzle.time.JD;

@SuppressWarnings("unused")
public class SatellitePass {
    private final JD riseTime, setTime, visibleTime, maxTime, disappearTime;
    private final String riseDirection, setDirection, visibleDirection, disappearDirection, maxDirection;
    private final double riseHeight, setHeight, visibleHeight, disappearHeight, maxHeight;

//    public SatellitePass(JD riseTime, AltAz rise, JD setTime, AltAz set, JD maxTime, AltAz max) {
    public SatellitePass(AltAz rise, AltAz set, AltAz visible, AltAz disappear, AltAz max) {
        // todo: check if this is good enough or are we creating a shallow copy?
        this.riseTime = rise.getEpoch();
        this.setTime = set.getEpoch();
        this.maxTime = max.getEpoch();
        this.visibleTime = visible.getEpoch();
        this.disappearTime = disappear.getEpoch();
        if (riseTime.Value() <= visibleTime.Value()) {
            visibleHeight = visible.getAltitude();
            visibleDirection = setDirections(visible.getAzimuth());
        } else {
            visibleHeight = rise.getAltitude();
            visibleDirection = setDirections(rise.getAzimuth());
        }
        if (disappearTime.Value() <= setTime.Value()) {
            disappearHeight = disappear.getAltitude();
            disappearDirection = setDirections(disappear.getAzimuth());
        } else{
            disappearHeight = set.getAltitude();
            disappearDirection = setDirections(set.getAzimuth());
        }

        maxHeight = max.getAltitude();
        riseHeight = rise.getAltitude();
        setHeight = set.getAltitude();
        maxDirection = setDirections(max.getAzimuth());
        riseDirection = setDirections(rise.getAzimuth());
        setDirection = setDirections(set.getAzimuth());
    }

    @Override
    public String toString() {
        return "SatellitePass{" +
                "visibleTime=" + visibleTime.Date() +
                ", visibleDirection='" + visibleDirection + '\'' +
                ", visibleHeight=" + visibleHeight + ",\n\t " +
                "maxTime=" + maxTime.Date() +
                ", maxDirection='" + maxDirection + '\'' +
                ", maxHeight=" + maxHeight + ",\n\t " +
                "disappearTime=" + disappearTime.Date() +
                ", disappearDirection='" + disappearDirection + '\'' +
                ", disappearHeight=" + disappearHeight +
                '}';
    }

    public JD getRiseTime() {
        return new JD(riseTime.Value());
    }

    public JD getSetTime() {
        return new JD(setTime.Value());
    }

    public JD getVisibleTime() {
        return new JD(visibleTime.Value());
    }

    public JD getDisappearTime() {
        return new JD(disappearTime.Value());
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

    public double getVisibleHeight() {
        return visibleHeight;
    }

    public double getDisappearHeight() {
        return disappearHeight;
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

    public String getVisibleDirection() {
        return visibleDirection;
    }

    public String getDisappearDirection() {
        return disappearDirection;
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
