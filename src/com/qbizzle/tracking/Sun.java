package com.qbizzle.tracking;

import com.qbizzle.math.OrbitalMath;
import com.qbizzle.math.Vector;
import com.qbizzle.time.JD;

public class Sun {
    public static final double AU = 1.495978707e11; // meters

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

    public static Vector Position2(JD t) {
        double JC = (t.Number() - JD.J2000) / 36525.0;
        double JCE = (t.Value() - JD.J2000) / 36525.0;
        double JME = JCE / 10.0;
        double L = Math.toDegrees( expandTableValues(LTable, JME) ) % 360.0;
        double B = Math.toDegrees( expandTableValues(BTable, JME) ) % 360.0;
        double R = expandTableValues(RTable, JME);

        double geocentricLongitude = (L + 180.0) % 360.0;
        double geocentricLatitude = -B;
//        double[] dNutation = getNutationDeltas(JCE);
        double epsilon0 = 0.0;
        for (int i = 0; i < 11; i++) {
            epsilon0 += UTable[i] * Math.pow(JME/10.0, i);
        }
        epsilon0 /= 3600.0;
//        double epsilon = (epsilon0 / 3600.0) + dNutation[1];
//        double dTau = -20.4898 / (3600.0 * R);
//        double lambda = geocentricLongitude + dNutation[0] + dTau;
//        double v0 = 280.46061837 + 360.98564736629 * (t.Value() - 2451545) +
//                0.000387933 * JC*JC - (JC*JC*JC / 39720000.0);
//        v0 = v0 % 360.0;
//        if (v0 < 0) v0 += 360.0;
//        double v = v0 + dNutation[0] * Math.cos( Math.toRadians(epsilon) );
//        double alpha = Math.toDegrees( Math.atan2(
//                Math.sin(Math.toRadians(lambda)) * Math.cos(Math.toRadians(epsilon)) -
//                        Math.tan(Math.toRadians(geocentricLatitude)) * Math.sin(Math.toRadians(epsilon)),
//                Math.cos(Math.toRadians(lambda))
//        ) ) % 360.0;
//        if (alpha < 0) alpha += 360.0;
//        double del = Math.toDegrees( Math.asin(
//                Math.sin(Math.toRadians(geocentricLatitude)) * Math.cos(Math.toRadians(epsilon)) +
//                        Math.cos(Math.toRadians(geocentricLatitude)) * Math.sin(Math.toRadians(epsilon)) * Math.sin(Math.toRadians(lambda))
//        ) );
//        double H = (v + coords.getLongitude() - alpha) % 360.0;
//        if (H < 0.0) H += 360.0;
//        double ksi = 8.794 / (3600.0 * R);
//        double u = Math.atan(0.99664719 * Math.tan( Math.toRadians(coords.getLatitude()) ));
//        // todo: adjust for elevation when adding it to Coordiantes
//        double E = 1830.14;
//        double x = Math.cos(u) + (E * Math.cos(Math.toRadians(coords.getLatitude())) / 6378140.0);
//        double y = 0.99664719 * Math.sin(u) + (E * Math.sin(Math.toRadians(coords.getLatitude())) / 6378140.0);
//        double xSinKsi = x * Math.sin(Math.toRadians(ksi));
//        double denominator = Math.cos(Math.toRadians(del)) - xSinKsi * Math.cos(Math.toRadians(H));
//        double dAlpha = Math.toDegrees( Math.atan2(
//                -xSinKsi * Math.sin(Math.toRadians(H)),
//                denominator
//        ) );
//        double alphap = alpha + dAlpha;
//        double delp = Math.toDegrees( Math.atan2(
//                (Math.sin(Math.toRadians(del)) - y * Math.sin(Math.toRadians(ksi))) * Math.cos(Math.toRadians(dAlpha)),
//                denominator
//        ) );


//        System.out.println("L: " + L + " B: " + B + " R: " + R);
//        System.out.println("dPsi: " + dNutation[0] + " dEpsilon: " + dNutation[1]);
//        System.out.println("epsilon: " + epsilon);
//        System.out.println("lambda: " + lambda);
//        System.out.println("alpha: " + alpha);
//        System.out.println("del: " + del);
//        System.out.println("H: " + H);
//        System.out.println("alpha': " + alphap);
//        System.out.println("del': " + delp);

        double latRad = Math.toRadians(geocentricLatitude);
        double lngRad = Math.toRadians(geocentricLongitude);
        double epsRad = Math.toRadians(epsilon0);
        return new Vector(
                R * Math.cos(latRad) * Math.cos(lngRad),
                R * (Math.cos(latRad) * Math.sin(lngRad) * Math.cos(epsRad) - Math.sin(latRad) * Math.sin(epsRad)),
                R * (Math.cos(latRad) * Math.sin(lngRad) * Math.sin(epsRad) + Math.sin(latRad) * Math.cos(epsRad))
        ).scale(AU);
    }

    static public double[] test(double JME) {
        double[] rtn = new double[3];
        rtn[0] = expandTableValues(LTable, JME);
        rtn[1] = expandTableValues(BTable, JME);
        rtn[2] = expandTableValues(RTable, JME);
        return rtn;
    }

    static private double tableSum(double[][] table, double JME) {
        double sum = 0;
        for (double[] doubles : table) {
            double sub = doubles[0] * Math.cos(doubles[1] + doubles[2] * JME);
            sum += sub;
        }
        return sum;
    }

    static private double expandTableValues(double[][][] table, double JME) {
        int tableLength = table.length;
        double[] arr = new double[tableLength];
        for (int i = 0; i < tableLength; i++) {
            arr[i] = tableSum(table[i], JME);
        }

        double sum = 0.0;
        for (int i = 0; i < tableLength; i++) {
            sum += arr[i] * Math.pow(JME, i);
        }
        return sum / 1E8;
    }

    static private double[] getXArray(double JCE) {
        double[] X = new double[5];
        for (int i = 0; i < 5; i++) {
            double Xi = 0.0;
            for (int j = 0; j < 4; j++) {
                Xi += XTable[i][j] * Math.pow(JCE, j);
            }
            X[i] = Xi;
        }
        return X;
    }

    static private double[] getNutationDeltas(double JCE) {
        double[] arrX = getXArray(JCE);
        double dPsi = 0.0, dEpsilon = 0.0;
        for (double[] row: NutationTable){
            double sumTerm = 0.0;
            for (int j = 0; j < 5; j++)
                sumTerm += Math.toRadians(arrX[j] * row[j]);
            dPsi += (row[5] + row[6] * JCE) * Math.sin(sumTerm);
            dEpsilon += (row[7] + row[8] * JCE) * Math.cos(sumTerm);
        }
        return new double[]{dPsi / 36000000.0, dEpsilon / 36000000.0};
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

//     better name for this?
    public enum TwilightType {
        Day, Civil, Nautical, Astronomical, Night
    }

    public static TwilightType getTwilightType(JD t, Coordinates coords) {
        Vector sunPos = Position2(t);
        double sunAngle = Tracker.getAltAz(sunPos, t, coords).getAltitude();
//        Vector toposPos = Tracker.getToposPosition(t, coords);
//        Vector sunTopos = sunPos.minus(toposPos);
//        Vector sunSEZ = RotateTo(
//                EulerOrderList.ZYX,
//                new EulerAngles(
//                        SiderealTime.LST(t, coords.getLongitude() * 24.0 / 360.0),
//                        90 - coords.getLatitude(),
//                        0.0
//                ),
//                sunTopos
//        );
//        double sunAngle = Math.toDegrees( Math.acos(sunSEZ.z() / sunSEZ.mag()) );
        System.out.println(sunAngle);
        if (sunAngle < -18) return TwilightType.Night;
        else if (sunAngle < -12) return TwilightType.Astronomical;
        else if (sunAngle < -6) return TwilightType.Nautical;
        else if (sunAngle < -(5.0 / 6.0)) return TwilightType.Civil;
        else return TwilightType.Day;
    }

    private static final double[][][] LTable = {
            {
                    {175347046, 0,          0},
                    {3341656,   4.6692568,  6283.07585},
                    {34894,     4.6261,     12566.1517},
                    {3497,      2.7441,     5753.3849},
                    {3418,      2.8289,     3.5231},
                    {3136,      3.6277,     77713.7715},
                    {2676,      4.4181,     7860.4194},
                    {2343,      6.1352,     3930.2097},
                    {1324,      0.7425,     11506.7698},
                    {1273,      2.0371,     529.691},
                    {1199,      1.1096,     1577.3435},
                    {990,       5.233,      5884.927},
                    {902,       2.045,      26.298},
                    {857,       3.508,      398.149},
                    {780,       1.179,      5223.694},
                    {753,       2.533,      5507.553},
                    {505,       4.583,      18849.228},
                    {492,       4.205,      775.523},
                    {357,       2.92,       0.067},
                    {317,       5.849,      11790.629},
                    {284,       1.899,      796.298},
                    {271,       0.315,      10977.079},
                    {243,       0.345,      5486.778},
                    {206,       4.806,      2544.314},
                    {205,       1.869,      5573.143},
                    {202,       2.458,      6069.777},
                    {156,       0.833,      213.299},
                    {132,       3.411,      2942.463},
                    {126,       1.083,      20.775},
                    {115,       0.645,      0.98},
                    {103,       0.636,      4694.003},
                    {102,       0.976,      15720.839},
                    {102,       4.267,      7.114},
                    {99,        6.21,       2146.17},
                    {98,        0.68,       155.42},
                    {86,        5.98,       161000.69},
                    {85,        1.3,        6275.96},
                    {85,        3.67,       71430.7},
                    {80,        1.81,       17260.15},
                    {79,        3.04,       12036.46},
                    {75,        1.76,       5088.63},
                    {74,        3.5,        3154.69},
                    {74,        4.68,       801.82},
                    {70,        0.83,       9437.76},
                    {62,        3.98,       8827.39},
                    {61,        1.82,       7084.9},
                    {57,        2.78,       6286.6},
                    {56,        4.39,       14143.5},
                    {56,        3.47,       6279.55},
                    {52,        0.19,       12139.55},
                    {52,        1.33,       1748.02},
                    {51,        0.28,       5856.48},
                    {49,        0.49,       1194.45},
                    {41,        5.37,       8429.24},
                    {41,        2.4,        19651.05},
                    {39,        6.17,       10447.39},
                    {37,        6.04,       10213.29},
                    {37,        2.57,       1059.38},
                    {36,        1.71,       2352.87},
                    {36,        1.78,       6812.77},
                    {33,        0.59,       17789.85},
                    {30,        0.44,       83996.85},
                    {30,        2.74,       1349.87},
                    {25,        3.16,       4690.48}
            },
            {
                    {628331966747.0,0,          0},
                    {206059,        2.678235,   6283.07585},
                    {4303,          2.6351,     12566.1517},
                    {425,           1.59,       3.523},
                    {119,           5.769,      26.298},
                    {109,           2.966,      1577.344},
                    {93,            2.59,       18849.23},
                    {72,            1.14,       529.69},
                    {68,            1.87,       398.15},
                    {67,            4.41,       5507.55},
                    {59,            2.89,       5223.69},
                    {56,            2.17,       155.42},
                    {45,            0.4,        796.3},
                    {36,            0.47,       775.52},
                    {29,            2.65,       7.11},
                    {21,            5.34,       0.98},
                    {19,            1.85,       5486.78},
                    {19,            4.97,       213.3},
                    {17,            2.99,       6275.96},
                    {16,            0.03,       2544.31},
                    {16,            1.43,       2146.17},
                    {15,            1.21,       10977.08},
                    {12,            2.83,       1748.02},
                    {12,            3.26,       5088.63},
                    {12,            5.27,       1194.45},
                    {12,            2.08,       4694},
                    {11,            0.77,       553.57},
                    {10,            1.3,        6286.6},
                    {10,            4.24,       1349.87},
                    {9,             2.7,        242.73},
                    {9,             5.64,       951.72},
                    {8,             5.3,        2352.87},
                    {6,             2.65,       9437.76},
                    {6,             4.67,       4690.48}
            },
            {
                    {52919, 0,      0},
                    {8720,  1.0721, 6283.0758},
                    {309,   0.867,  12566.152},
                    {27,    0.05,   3.52},
                    {16,    5.19,   26.3},
                    {16,    3.68,   155.42},
                    {10,    0.76,   18849.23},
                    {9,     2.06,   77713.77},
                    {7,     0.83,   775.52},
                    {5,     4.66,   1577.34},
                    {4,     1.03,   7.11},
                    {4,     3.44,   5573.14},
                    {3,     5.14,   796.3},
                    {3,     6.05,   5507.55},
                    {3,     1.19,   242.73},
                    {3,     6.12,   529.69},
                    {3,     0.31,   398.15},
                    {3,     2.28,   553.57},
                    {2,     4.38,   5223.69},
                    {2,     3.75,   0.98}
            },
            {
                    {289,   5.844,  6283.076},
                    {35,    0,      0},
                    {17,    5.49,   12566.15},
                    {3,     5.2,    155.42},
                    {1,     4.72,   3.52},
                    {1,     5.3,    18849.23},
                    {1,     5.97,   242.73}
            },
            {
                    {114,   3.142,  0},
                    {8,     4.13,   6283.08},
                    {1,     3.84,   12566.15}
            },
            {
                    {1, 3.14, 0}
            }
    };

    private static final double[][][] BTable = {
            {
                    {280,   3.199,  84334.662},
                    {102,   5.422,  5507.553},
                    {80,    3.88,   5223.69},
                    {44,    3.7,    2352.87},
                    {32,    4,      1577.34}
            },
            {
                    {9, 3.9,    5507.55},
                    {6, 1.73,   5223.69}
            }
    };

    private static final double[][][] RTable = {
            {
                    {100013989, 0,          0},
                    {1670700,   3.0984635,  6283.07585},
                    {13956,     3.05525,    12566.1517},
                    {3084,      5.1985,     77713.7715},
                    {1628,      1.1739,     5753.3849},
                    {1576,      2.8469,     7860.4194},
                    {925,       5.453,      11506.77},
                    {542,       4.564,      3930.21},
                    {472,       3.661,      5884.927},
                    {346,       0.964,      5507.553},
                    {329,       5.9,        5223.694},
                    {307,       0.299,      5573.143},
                    {243,       4.273,      11790.629},
                    {212,       5.847,      1577.344},
                    {186,       5.022,      10977.079},
                    {175,       3.012,      18849.228},
                    {110,       5.055,      5486.778},
                    {98,        0.89,       6069.78},
                    {86,        5.69,       15720.84},
                    {86,        1.27,       161000.69},
                    {65,        0.27,       7260.15},
                    {63,        0.92,       529.69},
                    {57,        2.01,       83996.85},
                    {56,        5.24,       71430.7},
                    {49,        3.25,       2544.31},
                    {47,        2.58,       775.52},
                    {45,        5.54,       9437.76},
                    {43,        6.01,       6275.96},
                    {39,        5.36,       4694},
                    {38,        2.39,       8827.39},
                    {37,        0.83,       19651.05},
                    {37,        4.9,        12139.55},
                    {36,        1.67,       12036.46},
                    {35,        1.84,       2942.46},
                    {33,        0.24,       7084.9},
                    {32,        0.18,       5088.63},
                    {32,        1.78,       398.15},
                    {28,        1.21,       6286.6},
                    {28,        1.9,        6279.55},
                    {26,        4.59,       10447.39}
            },
            {
                    {103019, 1.10749, 6283.07585},
                    {1721, 1.0644, 12566.1517},
                    {702, 3.142, 0},
                    {32, 1.02, 18849.23},
                    {31, 2.84, 5507.55},
                    {25, 1.32, 5223.69},
                    {18, 1.42, 1577.34},
                    {10, 5.91, 10977.08},
                    {9, 1.42, 6275.96},
                    {9, 0.27, 5486.78}
            },
            {
                    {4359,  5.7846, 6283.0758},
                    {124,   5.579,  12566.152},
                    {12,    3.14,   0},
                    {9,     3.63,   77713.77},
                    {6,     1.87,   5573.14},
                    {3,     5.47,   18849.23}
            },
            {
                    {145,   4.273,  6283.076},
                    {7,     3.92,   12566.15}
            },
            {
                    {4, 2.56, 6283.08}
            }
    };

    private static final double[][] XTable = {
            {297.85036, 445267.111480, -0.0019142, 1.0/189474.0},
            {357.527772, 35999.050340, -0.0001603, 1.0/-300000.0},
            {134.96298, 477198.867398, 0.0086972, 1.0/56250.0},
            {93.27191, 483202.017538, -0.0036825, 1.0/327270.0},
            {125.04452, -1934.136261, 0.0020708, 1.0/450000.0}
    };

    private static final double[][] NutationTable = {
            {0, 0, 0, 0, 1, -171996, -174.2, 92025, 8.9},
            {-2, 0, 0, 2, 2, -13187, -1.6, 5736, -3.1},
            {0, 0, 0, 2, 2, -2274, -0.2, 977, -0.5},
            {0, 0, 0, 0, 2, 2062, 0.2, -895, 0.5},
            {0, 1, 0, 0, 0, 1426, -3.4, 54, -0.1},
            {0, 0, 1, 0, 0, 712, 0.1, -7, 0},
            {-2, 1, 0, 2, 2, -517, 1.2, 224, -0.6},
            {0, 0, 0, 2, 1, -386, -0.4, 200, 0},
            {0, 0, 1, 2, 2, -301, 0, 129, -0.1},
            {-2, -1, 0, 2, 2, 217, -0.5, -95, 0.3},
            {-2, 0, 1, 0, 0, -158, 0, 0, 0},
            {-2, 0, 0, 2, 1, 129, 0.1, -70, 0},
            {0, 0, -1, 2, 2, 123, 0, -53, 0},
            {2, 0, 0, 0, 0, 63, 0, 0, 0},
            {0, 0, 1, 0, 1, 63, 0.1, -33, 0},
            {2, 0, -1, 2, 2, -59, 0, 26, 0},
            {0, 0, -1, 0, 1, -58, -0.1, 32, 0},
            {0, 0, 1, 2, 1, -51, 0, 27, 0},
            {-2, 0, 2, 0, 0, 48, 0, 0, 0},
            {0, 0, -2, 2, 1, 46, 0, -24, 0},
            {2, 0, 0, 2, 2, -38, 0, 16, 0},
            {0, 0, 2, 2, 2, -31, 0, 13, 0},
            {0, 0, 2, 0, 0, 29, 0, 0, 0},
            {-2, 0, 1, 2, 2, 29, 0, -12, 0},
            {0, 0, 0, 2, 0, 26, 0, 0, 0},
            {-2, 0, 0, 2, 0, -22, 0, 0, 0},
            {0, 0, -1, 2, 1, 21, 0, -10, 0},
            {0, 2, 0, 0, 0, 17, -0.1, 0, 0},
            {2, 0, -1, 0, 1, 16, 0, -8, 0},
            {-2, 2, 0, 2, 2, -16, 0.1, 7, 0},
            {0, 1, 0, 0, 1, -15, 0, 9, 0},
            {-2, 0, 1, 0, 1, -13, 0, 7, 0},
            {0, -1, 0, 0, 1, -12, 0, 6, 0},
            {0, 0, 2, -2, 0, 11, 0, 0, 0},
            {2, 0, -1, 2, 1, -10, 0, 5, 0},
            {2, 0, 1, 2, 2, -8, 0, 3, 0},
            {0, 1, 0, 2, 2, 7, 0, -3, 0},
            {-2, 1, 1, 0, 0, -7, 0, 0, 0},
            {0, -1, 0, 2, 2, -7, 0, 3, 0},
            {2, 0, 0, 2, 1, -7, 0, 3, 0},
            {2, 0, 1, 0, 0, 6, 0, 0, 0},
            {-2, 0, 2, 2, 2, 6, 0, -3, 0},
            {-2, 0, 1, 2, 1, 6, 0, -3, 0},
            {2, 0, -2, 0, 1, -6, 0, 3, 0},
            {2, 0, 0, 0, 1, -6, 0, 3, 0},
            {0, -1, 1, 0, 0, 5, 0, 0, 0},
            {-2, -1, 0, 2, 1, -5, 0, 3, 0},
            {-2, 0, 0, 0, 1, -5, 0, 3, 0},
            {0, 0, 2, 2, 1, -5, 0, 3, 0},
            {-2, 0, 2, 0, 1, 4, 0, 0, 0},
            {-2, 1, 0, 2, 1, 4, 0, 0, 0},
            {0, 0, 1, -2, 0, 4, 0, 0, 0},
            {-1, 0, 1, 0, 0, -4, 0, 0, 0},
            {-2, 1, 0, 0, 0, -4, 0, 0, 0},
            {1, 0, 0, 0, 0, -4, 0, 0, 0},
            {0, 0, 1, 2, 0, 3, 0, 0, 0},
            {0, 0, -2, 2, 2, -3, 0, 0, 0},
            {-1, -1, 1, 0, 0, -3, 0, 0, 0},
            {0, 1, 1, 0, 0, -3, 0, 0, 0},
            {0, -1, 1, 2, 2, -3, 0, 0, 0},
            {2, -1, -1, 2, 2, -3, 0, 0, 0},
            {0, 0, 3, 2, 2, -3, 0, 0, 0},
            {2, -1, 0, 2, 2, -3, 0, 0, 0}
    };

    private static final double[] UTable = {
            84381.448, -4680.93, -1.55, 1999.25,
            51.38, -249.67, -39.05, 7.12,
            27.87, 5.79, 2.45
    };

}
