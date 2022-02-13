package com.qbizzle.tracking;

import com.qbizzle.math.OrbitalMath;
import com.qbizzle.math.Vector;
import com.qbizzle.time.JD;

public class Sun {
    static final double AU = 1.495978707e11; // meters

    public static Vector Position(JD t) {
//         time since noon TT on 1, jan ,2000
        double n = t.Value() - 2451545.0;
//         mean longitude of the Sun
        double L = 4.89495042 + 0.0172027924 * n;
//         mean anomaly of the Sun
        double g = 6.240040768 + 0.0172019703 * n;
//         ecliptic longitude of the Sun
        double lambda = L + 0.0334230552 * Math.sin(g) + 0.00034906585 * Math.sin(2 * g);
//        ecliptic latitude of the Sun never exceeds 0.00033 degrees
        double beta = 0.0;
//        distance of the Sun from the Earth
        double R = 1.00014 - 0.01671 * Math.cos(g) - 0.00014 * Math.cos(2 * g);
//        obliquity of the ecliptic (axial tilt)
        double e = 0.4090877234 - 6.981317008e-9 * n;

//        vector for ECI coordinates
        return new Vector(
                R * AU * Math.cos(lambda),
                R * AU * Math.cos(e) * Math.sin(lambda),
                R * AU * Math.sin(e) * Math.sin(lambda)
        );
    }

    // x value is RA and y value is declination
    public static Vector Coordinates(JD t) {
//         time since noon TT on 1, jan ,2000
        double n = t.Value() - 2451545.0;
//         mean longitude of the Sun
        double L = 4.89495042 + 0.0172027924 * n;
//         mean anomaly of the Sun
        double g = 6.240040768 + 0.0172019703 * n;
//         ecliptic longitude of the Sun
        double lambda = L + 0.0334230552 * Math.sin(g) + 0.00034906585 * Math.sin(2 * g);
//        ecliptic latitude of the Sun never exceeds 0.00033 degrees
        double beta = 0.0;
//        distance of the Sun from the Earth
        double R = 1.00014 - 0.01671 * Math.cos(g) - 0.00014 * Math.cos(2 * g);
//        obliquity of the ecliptic (axial tilt)
        double e = 0.4090877234 - 6.981317008e-9 * n;
        return new Vector(
                Math.toDegrees (OrbitalMath.atan2(
                        Math.cos(e) * Math.sin(lambda),
                        Math.cos(lambda)
                ) ),
                Math.toDegrees( Math.asin( Math.sin(e) * Math.sin(lambda) ) )
        );
    }
}
