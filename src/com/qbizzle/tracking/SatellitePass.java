/**
 * @file
 * Contains values for major points during an overhead satellite pass.
 */

package com.qbizzle.tracking;

import com.qbizzle.time.JD;

/**
 * SatellitePass class contains an epoch, height and direction for five major
 * positions of a pass: when it first rises above the horizon, when it first becomes
 * visible (due to sunlight), when it reaches its max height, when it sets below
 * the horizon, and when it disappears from view (due to sunlight).
 * <p>
 * First visible means when the satellite can first be seen. If the satellite is already sunlit
 * when it rises above the horizon, then visible and rise times are the same, otherwise the
 * visible time will be after rise time, since the satellite entering sunlight is the limiting
 * factor for visibility.
 * <p>
 * Disappear means when the satellite is last able to be seen. If the satellite is sunlit as it
 * sets below the horizon, then disappear and set times are the same, otherwise the disappearing
 * time will be before set time, since the satellite leaving sunlight is the limiting factor
 * for visibility.
 * <p>
 * Max time is the time the satellite is at its highest point. This can be when the satellite becomes
 * visible, as the satellite disappears, or any other time in between.
 */
@SuppressWarnings("unused")
public class SatellitePass {
    private final JD riseTime, setTime, visibleTime, maxTime, disappearTime;
    private final String riseDirection, setDirection, visibleDirection, disappearDirection, maxDirection;
    private final double riseHeight, setHeight, visibleHeight, disappearHeight, maxHeight;

    /**
     * Constructs a SatellitePass based on the attributes of the five major event times of a
     * pass, wrapped inside AltAz objects.
     * @param rise      AltAz pertaining to the rise time of the pass.
     * @param set       AltAz pertaining to the set time of the pass.
     * @param visible   AltAz pertaining to visible (due to sunlight) time of the pass.
     * @param disappear AltAz pertaining to disappearing (due to sunlight) time of the pass.
     * @param max       AltAz pertaining to the maximum time of the pass.
     */
    public SatellitePass(AltAz rise, AltAz set, AltAz visible, AltAz disappear, AltAz max) {
        // todo: check if this is good enough or are we creating a shallow copy?
        this.riseTime = rise.getEpoch();
        this.setTime = set.getEpoch();
        this.maxTime = max.getEpoch();
        this.visibleTime = visible.getEpoch();
        this.disappearTime = disappear.getEpoch();
        if (riseTime.value() <= visibleTime.value()) {
            visibleHeight = visible.getAltitude();
            visibleDirection = setDirections(visible.getAzimuth());
        } else {
            visibleHeight = rise.getAltitude();
            visibleDirection = setDirections(rise.getAzimuth());
        }
        if (disappearTime.value() <= setTime.value()) {
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

    /**
     * Converts the internal values into a String.
     * @return  A String containing the internal values.
     */
    @Override
    public String toString() {
        return "SatellitePass{" +
                "visibleTime=" + visibleTime.date() +
                ", visibleDirection='" + visibleDirection + '\'' +
                ", visibleHeight=" + visibleHeight + ",\n\t " +
                "maxTime=" + maxTime.date() +
                ", maxDirection='" + maxDirection + '\'' +
                ", maxHeight=" + maxHeight + ",\n\t " +
                "disappearTime=" + disappearTime.date() +
                ", disappearDirection='" + disappearDirection + '\'' +
                ", disappearHeight=" + disappearHeight +
                '}';
    }

    /**
     * Retrieves the time the satellite rises above the horizon for this pass.
     * @return The time in Julian Days.
     */
    public JD getRiseTime() {
        return new JD(riseTime.value());
    }

    /**
     * Retrieves the time the satellite sets below the horizon for this pass.
     * @return The time in Julian Days.
     */
    public JD getSetTime() {
        return new JD(setTime.value());
    }

    /**
     * Retrieves the time the satellite first becomes visible.
     * @return The time in Julian Days.
     */
    public JD getVisibleTime() {
        return new JD(visibleTime.value());
    }

    /**
     * Retrieves the time the satellite is last visible.
     * @return The time in Julian Days.
     */
    public JD getDisappearTime() {
        return new JD(disappearTime.value());
    }

    /**
     * Retrieves the time the satellite is at its maximum altitude.
     * @return The time in Julian Days.
     */
    public JD getMaxTime() {
        return new JD(maxTime.value());
    }

    /**
     * Retrieves the height the satellite rises above the horizon. This should always be
     * very close to zero, but the method is here to use for debugging.
     * @return The height in degrees above the horizon.
     */
    public double getRiseHeight() {
        return riseHeight;
    }

    /**
     * Retrieves the height the satellite sets below the horizon. This should always be
     * very close to zero, but the method is here to use for debugging.
     * @return The height in degrees above the horizon.
     */
    public double getSetHeight() {
        return setHeight;
    }

    /**
     * Retrieves the height the satellite first becomes visible above the horizon. This value
     * can be different from the rise height if the satellite is not sunlight until after it rises.
     * @return  The height in degrees above the horizon.
     */
    public double getVisibleHeight() {
        return visibleHeight;
    }

    /**
     * Retrieves the height the satellite is last visible above the horizon. This value can be
     * different from the set height if the satellite stops being sunlit before it sets.
     * @return  The height in degrees above the horizon.
     */
    public double getDisappearHeight() {
        return disappearHeight;
    }

    /**
     * Retrieves the maximum height the satellite achieves during the pass.
     * @return The height in degrees above the horizon.
     */
    public double getMaxHeight() {
        return maxHeight;
    }

    /**
     * Retrieves the compass direction the satellite rises above the horizon.
     * @return A String containing an abbreviation of the compass direction.
     */
    public String getRiseDirection() {
        return riseDirection;
    }

    /**
     * Retrieves the compass direction the satellite sets below the horizon.
     * @return A String containing an abbreviation of the compass direction.
     */
    public String getSetDirection() {
        return setDirection;
    }

    /**
     * Retrieves the compass direction the satellite first becomes visible.
     * @return A String containing an abbreviation of the compass direction.
     */
    public String getVisibleDirection() {
        return visibleDirection;
    }

    /**
     * Retrieves the compass direction the satellite is last visible.
     * @return A String containing an abbreviation of the compass direction.
     */
    public String getDisappearDirection() {
        return disappearDirection;
    }

    /**
     * Retrieves the compass direction the satellite achieves its maximum height.
     * @return A String containing an abbreviation of the compass direction.
     */
    public String getMaxDirection() {
        return maxDirection;
    }

    /**
     * Converts the compass heading to a String abbreviation of the compass direction.
     * @param azimuth   The azimuth of the satellite, from 0 to 360 measuring clockwise
     *                  from north.
     * @return          The String abbreviation.
     */
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
