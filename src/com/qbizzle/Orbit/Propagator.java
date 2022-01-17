package com.qbizzle.Orbit;

//public class SGP4 {
////    static StateVectors Propagate(TLE tle, double dt, double step) {
////        double a1 = Math.pow(Math.sqrt(OrbitalMath.MU)/tle.MeanMotion(), 2.0/3.0);
////        double del1 = 3 * 5.41308e-4 * (3 * Math.pow(Math.cos(tle.Inclination()), 2) - 1);
////        del1 = del1 / (2.0 * a1 * a1 * Math.pow(1 - Math.pow(tle.Eccentricity(),2),1.5));
////        double a0 = a1 * (1 - (1.0/3.0) * del1 - Math.pow(del1, 2) - (134.0/81.0) * Math.pow(del1, 3));
//////        double del0 =
////    }
//}

import com.qbizzle.Math.OrbitalMath;
import com.qbizzle.Math.Vector;

public class Propagator extends SGPConstants {
    private static double SGPKeplerEpsilon = 0.0000001;
//    private static TLE mtle;
//    private static COE mcoe;
//
//    public static StateVectors SGP4(TLE tle, double dt, double step) {
//        double remainingDT = dt;
//        mtle = tle; // THIS IS ASSIGNING ADDRESS, UNDO BEFORE ENDING METHOD
//        mcoe = new COE(tle);
//        while (remainingDT > 0) {
//
//        }
//        mtle = null;
//        mcoe = null;
//    }
//
//    private static StateVectors xsgp4Algorithm(TLE tle,

    public static StateVectors SGP(TLE tle, double dt, double step) {
        double a1 = Math.pow(CKE / tle.MeanMotion(), 2.0/3.0);
        double del1 = (3 * CK2) / (2 * a1 * a1);
        del1 = del1 * (3 * Math.pow(Math.cos(tle.Inclination()),2) - 1) / Math.pow(1 - tle.Eccentricity()*tle.Eccentricity(), 1.5);
        double a0 = a1 * (1 - (1.0 / 3.0)*del1 - del1*del1 - (134.0/81.0)*Math.pow(del1, 3));
        double p0 = a0 * (1 - tle.Eccentricity() * tle.Eccentricity());
        double q0 = a0 * (1 - tle.Eccentricity());
        double L0 = tle.MeanAnomaly() + tle.AOP() + tle.LAN();
        double dodt = -3.0 * CK2 * tle.MeanMotion() * Math.cos(tle.Inclination()) / (p0*p0);
        double dwdt = 3 * CK2 * tle.MeanMotion();
        dwdt = (5 * Math.pow(Math.cos(tle.Inclination()), 2) - 1) / (2.0 * p0 * p0);
        double a = a0 * Math.pow(
                tle.MeanMotion() / (tle.MeanMotion() + (tle.MeanMotionDot() * dt) + (0.5*tle.MeanMotionDDot())*(dt*dt)),
                2.0 / 3.0
        );
        double e = E6A;
        if (a > q0) e = 1 - (q0/a);
        double p = a * (1 - e*e);
        double LANs0 = tle.LAN() + dodt*dt;
        double Ws0 = tle.AOP() + dwdt*dt;
        double Ls = L0 + (tle.MeanMotion() + dwdt + dodt)*dt + (tle.MeanMotionDot()/2.0)*dt*dt
                + (tle.MeanMotionDDot()/6.0)*dt*dt*dt;
        double ayNSL = e * Math.sin(Ws0) - (J3 * Math.sin(tle.Inclination()) / (2 * J2 * p));
        double axNSL = tle.Eccentricity() * Math.cos(Ws0);
        double L = (J3 / (4 * J2 * p)) * axNSL * Math.sin(tle.Inclination());
        L *= (3 + 5 * Math.cos(tle.Inclination())) / (1 + Math.cos(tle.Inclination()));
        L = Ls - L;
        double EW = SGPKepler(L-LANs0, ayNSL, axNSL, L-LANs0);
        double ecosE = axNSL*Math.cos(EW) + ayNSL*Math.sin(EW);
        double esinE = axNSL*Math.sin(EW) - ayNSL*Math.cos(EW);
        double eL2 = axNSL*axNSL + ayNSL*ayNSL;
        double pL = a * (1 - eL2*eL2);
        double r = a * (1 - ecosE);
        double rdot = Math.sqrt(OrbitalMath.MU * a) * esinE / r;
        double rvdot = Math.sqrt(OrbitalMath.MU * pL) / r;
        double sinu = (a / r) * (Math.sin(EW) - ayNSL - axNSL *
                esinE / (1 + Math.sqrt(1 - eL2*eL2)));
        double cosu = (a / r) * (Math.cos(EW) - axNSL - ayNSL *
                esinE / (1 + Math.sqrt(1 - eL2*eL2)));
        double u = Math.atan2(sinu, cosu);
//        Short-period perturbations are now included by
        double rk = r + (0.5 * CK2 * Math.pow(Math.sin(tle.Inclination()), 2)
                * Math.cos(2 * u) / pL);
        double uk = u * (0.25 * CK2 * (7 * Math.pow(Math.cos(tle.Inclination()), 2) - 1)
                * Math.sin(2*u) / (pL*pL));
        double LANk = LANs0 + (
                3 * CK2 * Math.cos(tle.Inclination()) * Math.sin(2 * u)
                / (2 * pL * pL)
                );
        double ik = tle.Inclination() + (
                3 * CK2 * Math.sin(tle.Inclination()) * Math.cos(tle.Inclination()) * Math.sin(2 * u)
                / (2 * pL * pL)
                );
        Vector vecM = new Vector(
                -Math.sin(LANk) * Math.cos(ik),
                Math.cos(LANk) * Math.cos(ik),
                Math.sin(ik)
        );
        Vector vecN = new Vector(
                Math.cos(LANk),
                Math.sin(LANk),
                0
        );
        Vector vecU = vecM.scale(Math.sin(uk)).plus(vecN.scale(Math.cos(uk)));
        Vector vecV = vecM.scale(Math.cos(uk)).minus(vecN.scale(Math.sin(uk)));
        Vector vecRDot = vecU.scale(rdot).plus(vecV.scale(rvdot));
        return new StateVectors(
                vecU.scale(rk),
                vecRDot
        );
    }

//    private static StateVectors xsgpAlgorithm(TLE tle, StateVectors state, double dt) {
//
//    }

    private static double SGPKepler(double EWi, double ayNSL, double axNSL, double U) {
        double DEWi = (U - ayNSL*Math.cos(EWi) + axNSL*Math.sin(EWi) - EWi) /
                (-ayNSL*Math.sin(EWi) - axNSL*Math.cos(EWi) + 1);
        if (Math.abs(DEWi) < SGPKeplerEpsilon) return EWi + DEWi;
        return SGPKepler(EWi+DEWi, ayNSL, axNSL, U);
    }
}

class SGPConstants {
    protected static final double J2 = 0.0010826269;
    protected static final double J3 = -0.0000025323;
    protected static final double J4 = -0.0000016204;
    protected static final double CK2 = 5.41308e-4;
    protected static final double CK4 = 0.62098875e-6;
    protected static final double E6A = 1.0e-6;
    protected static final double QOMS2T = 1.88027916e-9;
    protected static final double S = 1.01222928;
    protected static final double TOTHRD = 2.0 / 3.0;
    protected static final double XJ3 = -0.25388e-5;
    protected static final double XKE = 0.743669161e-1;
    protected static final double XKMPER = 6378.135;
    protected static final double XMNPDA = 1440.0;
    protected static final double AE = 1.0;
    protected static final double DE2RA = 0.174532925e-1;
    protected static final double PI = Math.PI;
    protected static final double PIO2 = PI / 2.0;
    protected static final double TWOPI = 2 * PI;
    protected static final double X3PIO2 = 3 * PIO2;
    protected static final double CKE = Math.sqrt(OrbitalMath.MU);
}