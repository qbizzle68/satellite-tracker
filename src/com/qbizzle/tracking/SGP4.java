package com.qbizzle.tracking;

import com.qbizzle.math.OrbitalMath;
import com.qbizzle.math.Vector;
import com.qbizzle.orbit.StateVectors;
import com.qbizzle.orbit.TLE;
import com.qbizzle.time.JD;

/**
 * This class functionally works, but it is very inefficient and should be implemented
 * as an instantiable class that can be initialized to avoid repeatedly computing
 * non-time dependent values.
 */
public class SGP4 {
    /**
     * Constants used in the propagation model.
     */
    private static final double CK2 = 5.413080e-4;
    private static final double CK4 = 0.62098875e-6;
    private static final double E6A = 1E-6;
    private static final double Q0MS2T = 1.88027916e-9;
    private static final double S = 1.01222928;
    private static final double TOTHRD = 2.0 / 3.0;
    private static final double XJ3 = -0.253881e-5;
    private static final double XKE = 0.743669161e-1;
    private static final double XKMPER = 6378.135;
    private static final double XMNPDA = 1440.0;
    private static final double AE = 1.0;
    private static final double DE2RA = 0.174532925e-1;
    private static final double PI = Math.PI;
    private static final double PIO2 = PI / 2.0;
    private static final double TWOPI = PI * 2.0;
    private static final double X3PIO2 = 3.0 * PI / 2.0;

    /**
     * A container class for handling the mean elements of a TLE in the
     * appropriate units.
     */
    static class MeanElements {
        public final double n0;
        public final double e0;
        public final double i0;
        public final double M0;
        public final double w0;
        public final double L0;
        public final double ndot0;
        public final double nddot0;

        /**
         * Constructs a MeanElements object from the mean elements contained in
         * a TLE.
         * @param tle   TLE that contains the desired mean elements.
         */
        public MeanElements(TLE tle) {
            n0 = tle.meanMotion() * 2 * Math.PI / 1440.0;
            e0 = tle.eccentricity();
            i0 = Math.toRadians( tle.inclination() );
            M0 = Math.toRadians( tle.meanAnomaly() );
            w0 = Math.toRadians( tle.aop() );
            L0 = Math.toRadians( tle.lan() );
            ndot0 = tle.meanMotionDot() * 2 * Math.PI / (1440.0 * 1440.0);
            nddot0 = tle.meanMotionDDot() * 2 * Math.PI / (1440.0 * 1440.0 * 1440.0);
        }
    }

//    /** everything seems to be in km and minutes */

    /**
     * Propagates the satellite to a specific time to obtain the position
     * and velocity vectors.
     * @param tle   TLE of the satellite.
     * @param t1    Time to find the state vectors.
     * @return      A StateVectors object containing the position and velocity
     *              of the satellite.
     */
    public static StateVectors Propagate(TLE tle, JD t1) {
        return Propagate(tle, t1.Difference(new JD(tle)));
    }

    /**
     * Propagates the satellite forward or backwards by a certain amount of
     * time to obtain the position and velocity vectors.
     * @param tle   TLE of the satellite.
     * @param dt    Amount of time in the future or past to propagate. This
     *              is relative to the epoch of the @p tle and is measured
     *              in solar days.
     * @return      A StateVectors object containing the position and velocity
     *              of the satellite.
     */
    public static StateVectors Propagate(TLE tle, double dt) {
        StateVectors state = step(tle, new MeanElements(tle), dt * 1440.0);
        return new StateVectors(
                state.Position().scale(1000.0 * XKMPER),
                state.Velocity().scale(1000.0 * XKMPER / 60.0)
        );
    }

    // TODO: we don't need the MeanElements, just convert them inside the step method
    // dt in minutes
    /**
     * Implementation of the SGP4 model.
     * @param tle       TLE of the satellite.
     * @param elements  Mean elements of the TLE in appropriate units.
     * @param dt        Change in time relative to the @p tle epoch.
     * @return          The state vectors of the satellite.
     */
    private static StateVectors step(TLE tle, MeanElements elements, double dt) {
        // mean elements
        double xm0 = elements.M0;
        double xnode0 = elements.L0;
        double omega0 = elements.w0;
        double e0 = elements.e0;
        double xincl = elements.i0;
        double xn0 = elements.n0;
        double bstar = tle.bStar();

        // recover original mean motion (xn0dp (n0'') and semimajor axis (a0dp (a0'') from input elements
        double a1 = Math.pow(XKE / xn0, TOTHRD);
        double cosi0 = Math.cos( xincl );
        double theta2 = cosi0 * cosi0;
        double x3thm1 = 3 * theta2 - 1;
        double e0sq = e0 * e0;
        double beta02 = 1 - e0sq;
        double beta0 = Math.sqrt(beta02);
        double del1 = 1.5 * CK2 * x3thm1 / (a1 * a1 * beta0 * beta02);
        double a0 = a1 * (1 - del1 * (0.5 * TOTHRD + del1 * (1 + (134.0 / 81.0) * del1)));
        double del0 = 1.5 * CK2 * x3thm1 / (a0 * a0 * beta0 * beta02);
        double xn0dp = xn0 / (1 + del0); // rad / min
        double a0dp = a0 / (1 - del0); // earth radii

        // initialization
        // for perigee less than 220 kilometers, the ISIMP flag is set and the equations are
        // truncated to linear variation in sqrt(a) and quadratic variation in mean anomaly.
        // Also, the c3 term, the delta omega term, and the delta M term are dropped.
        boolean ISIMP = (a0dp * (1 - e0) / AE) < (220.0 / XKMPER + AE);

        // for perigee below 156 km, the values of s and qoms2t are altered
        double s4 = S;
        double q0ms24 = Q0MS2T;
        double perige = (a0dp * (1 - e0) - AE) * XKMPER;
        if (perige < 156.0) {
            if (perige <= 98.0) s4 = 20.0;
            else s4 = perige - 78.0;
            q0ms24 = Math.pow((120 - s4) * AE / XKMPER, 4);
            s4 = s4 / XKMPER + AE;
        }
        double pinvsq = 1 / (a0dp * a0dp * beta02 * beta02);
        double tsi = 1 / (a0dp - s4);
        double eta = a0dp * e0 * tsi;
        double etasq = eta * eta;
        double eeta = e0 * eta;
        double psisq = Math.abs(1 - etasq);
        //double coef = Math.pow(q0ms24 * tsi, 4); q0ms24 is already raised to the fourth power
        double coef = q0ms24 * Math.pow(tsi, 4);
        double coef1 = Math.pow(coef / psisq, 3.5);
        double c2 = coef1 * xn0dp * (a0dp * (1 + 1.5 * etasq + eeta * (4 + etasq)) + .75 *
                CK2 * tsi / psisq * x3thm1 * (8 + 3 * etasq * (8 + etasq)));
        double c1 = bstar * c2;
        double sini0 = Math.sin(xincl);
        double a30vk2 = -XJ3 / CK2 * Math.pow(AE, 3);
        double c3 = coef * tsi * a30vk2 * xn0dp * AE * sini0 / e0;
        double x1mth2 = 1 - theta2;
        double c4 = 2 * xn0dp * coef1 * a0dp * beta02 * (eta *
                (2 + 0.5 * etasq) + e0 * (0.5 + 2 * etasq) - 2 * CK2 * tsi/
                (a0dp * psisq) * (-3 * x3thm1 * (1 - 2 * eeta + etasq *
                (1.5 - 0.5 * eeta)) + 0.75 * x1mth2 * (2 * etasq - eeta *
                (1 + etasq)) * Math.cos(2 * omega0)));
        double c5 = 2 * coef1 * a0dp * beta02 * (1 + 2.75 * (etasq + eeta) + eeta * etasq);
        double theta4 = theta2 * theta2;
        double temp1 = 3 * CK2 * pinvsq * xn0dp;
        double temp2 = temp1 * CK2 * pinvsq;
        double temp3 = 1.25 * CK4 * pinvsq * pinvsq * xn0dp;
        double xmdot = xn0dp + 0.5 * temp1 * beta0 * x3thm1 + 0.0625 * temp2 * beta0 *
                (13 - 78 * theta2 + 137 * theta4);
        double x1m5th = 1 - 5 * theta2;
        double omgdot = -0.5 * temp1 * x1m5th + 0.0625 * temp2 * (7 - 114 * theta2 +
                395 * theta4) + temp3 * (3 - 36 * theta2 + 49 * theta4);
        double xhdot1 = -temp1 * cosi0;
        double xnodot = xhdot1 + (0.5 * temp2 * (4 - 19 * theta2) + 2 * temp3 * (3 -
                7 * theta2)) * cosi0;
        double omgcof = bstar * c3 * Math.cos(omega0);
        double xmcof = -TOTHRD * coef * bstar * AE / eeta;
        double xnodcf = 3.5 * beta02 * xhdot1 * c1;
        double t2cof = 1.5 * c1;
        double xlcof = 0.125 * a30vk2 * sini0 * (3 + 5 * cosi0) / (1 + cosi0);
        double aycof = 0.25 * a30vk2 * sini0;
        double delm0 = Math.pow(1 + eta * Math.cos(xm0), 3);
        double sinm0 = Math.sin(xm0);
        double x7thm1 = 7 * theta2 - 1;

        // update for secular gravity and atmospheric drag

        double xmdf = xm0 + xmdot * dt;
        double omgadf = omega0 + omgdot * dt;
        double xnoddf = xnode0 + xnodot * dt;
        double omega = omgadf;
        double xmp = xmdf;
        double tsq = dt * dt;
        double xnode = xnoddf + xnodcf * tsq;
        double tempa = 1 - c1 * dt;
        double tempe = bstar * c4 * dt;
        double templ = t2cof * tsq;
        if (!ISIMP) {
            double c1sq = c1 * c1;
            double d2 = 4 * a0dp * tsi * c1sq;
            double temp = d2 * tsi * c1 / 3;
            double d3 = (17 * a0dp + s4) * temp;
            double d4 = 0.5 * temp * a0dp * tsi * (221 * a0dp + 31 * s4) * c1;
            double t3cof = d2 + 2 * c1sq;
            double t4cof = 0.25 * (3 * d3 + c1 * (12 * d2 + 10 * c1sq));
            double t5cof = 0.2 * (3 * d4 + 12 * c1 * d3 + 6 * d2 * d2 + 15 * c1sq * (
                    2 * d2 + c1sq));
            double delomg = omgcof * dt;
            double delm = xmcof * (Math.pow( 1 + eta * Math.cos(xmdf), 3) - delm0);
            temp = delomg + delm;
            xmp = xmdf + temp;
            omega = omgadf - temp;
            double tcube = tsq * dt;
            double tfour = dt * tcube;
            tempa = tempa - d2 * tsq - d3 * tcube - d4 * tfour;
            tempe = tempe + bstar * c5 * (Math.sin(xmp) - sinm0);
            templ = templ + t3cof * tcube +
                    tfour * (t4cof + dt * t5cof);
        }
//        double a = Math.pow(a0dp * tempa, 2); only the tempa is raise to the power 2
        double a = a0dp * Math.pow(tempa, 2);
        double e = e0 - tempe;
        double xl = xmp + omega + xnode + xn0dp * templ;
        double beta = Math.sqrt(1 - e*e);
        double xn = XKE / Math.pow(a, 1.5);

        // long period periodics
        double axn = e * Math.cos(omega);
        double temp = 1 / (a * beta * beta);
        double xll = temp * xlcof * axn;
        double aynl = temp * aycof;
        double xlt = xl + xll;
        double ayn = e * Math.sin(omega) + aynl;

        // solve keplers equation
        double capu = (xlt - xnode) % TWOPI;
        if (capu < 0) capu += TWOPI;
        temp2 = capu;
        double sinepw = 0;
        double cosepw = 0;
        double temp4 = 0;
        double temp5 = 0;
        double temp6 = 0;
        for (int i = 0; i < 10; i++) {
            sinepw = Math.sin(temp2);
            cosepw = Math.cos(temp2);
            temp3 = axn * sinepw;
            temp4 = ayn * cosepw;
            temp5 = axn * cosepw;
            temp6 = ayn * sinepw;
            double epw = (capu - temp4 + temp3 - temp2) / (1 - temp5 - temp6) + temp2;
            if (Math.abs(epw - temp2) <= E6A) break;
            temp2 = epw;
        }

        // short period preliminary quantities
        double ecose = temp5 + temp6;
        double esine = temp3 - temp4;
        double elsq = axn * axn + ayn * ayn;
        temp = 1 - elsq;
        double pl = a * temp;
        double r = a * (1 - ecose);
        temp1 = 1.0 / r;
        double rdot = XKE * Math.sqrt(a) * esine * temp1;
        double rfdot = XKE * Math.sqrt(pl) * temp1;
        temp2 = a * temp1;
        double betal = Math.sqrt(temp);
        temp3 = 1.0 / (1 + betal);
        double cosu = temp2 * (cosepw - axn + ayn * esine * temp3);
        double sinu = temp2 * (sinepw - ayn - axn * esine * temp3);
        double u = OrbitalMath.atan2(sinu, cosu);
        double sin2u = 2 * sinu * cosu;
        double cos2u = 2 * cosu * cosu - 1;
        temp = 1 / pl;
        temp1 = CK2 * temp;
        temp2 = temp1 * temp;

        // update for short periodics
        double rk = r * (1 - 1.5 * temp2 * betal * x3thm1) + 0.5 * temp1 * x1mth2 * cos2u;
        double uk = u - 0.25 * temp2 * x7thm1 * sin2u;
        double xnodek = xnode + 1.5 * temp2 * cosi0 * sin2u;
        double xinck = xincl + 1.5 * temp2 * cosi0 * sini0 * cos2u;
        double rdotk = rdot - xn * temp1 * x1mth2 * sin2u;
        double rfdotk = rfdot + xn * temp1 * (x1mth2 * cos2u + 1.5 * x3thm1);

        // orientation vectors
        double sinuk = Math.sin(uk);
        double cosuk = Math.cos(uk);
        double sinik = Math.sin(xinck);
        double cosik = Math.cos(xinck);
        double sinnok = Math.sin(xnodek);
        double cosnok = Math.cos(xnodek);
//        double xmx = -sinnok * sinuk + cosnok * cosuk; just typed this completely wrong
        double xmx = -sinnok * cosik;
        double xmy = cosnok * cosik;
        double ux = xmx * sinuk + cosnok * cosuk;
        double uy = xmy * sinuk + sinnok * cosuk;
        double uz = sinik * sinuk;
        double vx = xmx * cosuk - cosnok * sinuk;
        double vy = xmy * cosuk - sinnok * sinuk;
        double vz = sinik * cosuk;

        // position and velocity
        return new StateVectors(
                new Vector(
                        rk * ux,
                        rk * uy,
                        rk * uz
                ),
                new Vector(
                        rdotk * ux + rfdotk * vx,
                        rdotk * uy + rfdotk * vy,
                        rdotk * uz + rfdotk * vz
                )
        );
    }

//    // dt in minutes
//    private static StateVectors step(TLE tle, COE coe, double dt) {
//        boolean ISIMP = false;
//        // recover original mean motion and semimajor axis
//        double n0 = tle.MeanMotion() * 2 * PI / XMNPDA;
//        double a1 = Math.pow(XKE / n0, TOTHRD);
//        double cosi0 = Math.cos(coe.inc);
//        double theta2 = cosi0 * cosi0;
//        double x3thm1 = 3.0 * theta2 - 1;
//        double e0sq = coe.ecc * coe.ecc;
//        double beta02 = 1 - e0sq;
//        double beta0 = Math.sqrt(beta02);
//        double del1 = 1.5 * CK2 * x3thm1 / (a1 * a1 * beta0 * beta02);
//        double a0 = a1 * (1.0 - del1 * (0.5 * TOTHRD + del1 * (1.0 + 134.0 / 81.0 * del1)));
//        double del0 = 1.5 * CK2 * x3thm1 / (a0 * a0 * beta0 * beta02);
//        double xn0dp = n0 / (1.0 + del0);
//        double a0dp = a0 / (1.0 - del0);
//
//        // initialization
//
//        // for perigee less than 220 km, equations are truncated to linear variation in sqrt A and
//        // quadratic variation in mean anomaly. Also, the C3 term, the delta omega term, and the
//        // delta M term are dropped
//        if ((a0dp * (1 - coe.ecc) / AE) < (200 / XKMPER + AE)) ISIMP = true;
//        System.out.println("ISIMP = " + ISIMP);
//
//        // for perigee below 156 km, the values of S and qoms2t are altered
//        double s4 = S;
//        double q0ms24 = Q0MS2T;
//        double perige = (a0dp * (1 - coe.ecc) - AE) * XKMPER;
//        if (perige < 156.0) {
//            //todo remove the redundant assignment of s4 once the model works
//            if (perige > 98) s4 = perige - 78.0;
//            else s4 = 20;
////            s4 = perige - 78.0;
////            if (perige <= 98) s4 = 20;
//            q0ms24 = Math.pow(((120.0 - s4) * AE / XKMPER), 4);
//            s4 = s4 / XKMPER + AE;
//        }
//        double pinvsq = 1.0 / (a0dp*a0dp*beta02*beta02);
//        double tsi = 1.0 / (a0dp - s4);
//        double eta = a0dp * coe.ecc * tsi;
//        double etasq = eta * eta;
//        double eeta = coe.ecc * eta;
//        double psisq = Math.abs(1 - etasq);
//        double coef = Math.pow(q0ms24 * tsi, 4);
//        double coef1 = Math.pow(coef / psisq, 3.5);
//        double c2 = coef1 * xn0dp * (a0dp  * ( 1 + 1.5 * etasq + eeta * (4 + etasq)) +
//                .75 * (CK2 * tsi / psisq) * x3thm1 * (8 + 3 * etasq * (8 + etasq)));
//        double c1 = tle.BStar() * c2;
//        double sini0 = Math.sin( Math.toRadians(coe.inc) );
//        double a30vk2 = -XJ3 / CK2 * Math.pow(AE, 3); // could be an error
//        double c3 = coef * tsi * a30vk2 * xn0dp * AE * sini0 / coe.ecc;
//        double x1mth2 = - theta2;
//        double c4 = 2 * xn0dp * coef1 * a0dp * beta02 * (eta * (2 + 0.5 * etasq) + coe.ecc
//                * (0.5 + 2 * etasq) - 2 * CK2 * tsi / (a0dp * psisq) * (-3 * x3thm1 * (1 - 2
//                * eeta + etasq * (1.5 - 0.5 * eeta)) + 0.75 * x1mth2 * (2 * etasq - eeta
//                * (1 + etasq)) * Math.cos( Math.toRadians(2 * coe.aop) )));
//        double C5 = 2 * coef1 * a0dp * beta02 * (1 + 2.75 * (etasq + eeta) + eeta * etasq);
//        double theta4 = theta2 * theta2;
//        double temp1 = 3 * CK2 * pinvsq * xn0dp;
//        double temp2 = temp1 * CK2 * pinvsq;
//        double temp3 = 1.25 * CK4 * pinvsq * pinvsq * xn0dp;
//        double xmdot = xn0dp + 0.5 * temp1 * beta0 * x3thm1 + 0.0625 * temp2 * beta0 * (13 - 78 * theta2 + 137 * theta4);
//        double x1m5th = 1 - 5 * theta2;
//        double omgdot = -0.5 * temp1 * x1m5th + 0.0625 * temp2 * (7 - 114 * theta2 + 395 * theta4) + temp3
//                * (3 - 36 * theta2 + 49 * theta4);
//        double xhdot1 = -temp1 * cosi0;
//        double xn0dot = xhdot1 + (0.5 * temp2 * (4 - 19 * theta2) + 2 * temp3 * (3 - 7 * theta2)) * cosi0;
//        double omgcof = tle.BStar() * c3 * Math.cos( Math.toRadians(coe.aop) );
//        double xmcof = -TOTHRD * coef * tle.BStar() * AE / eeta;
//        double xn0dcf = 3.5 * beta02 * xhdot1 * c1;
//        double t2cof = 1.5 * c1;
//        double xlcof = 0.125 * a30vk2 * sini0 * (3 + 5 * cosi0) / (1 + cosi0);
//        double aycof = 0.25 * a30vk2 * sini0;
//        double xm0 = OrbitalMath.True2Mean( Math.toRadians(coe.ta), coe.ecc);
//        double delm0 = Math.pow(1 + eta * Math.cos( xm0 ), 3);
//        double sinm0 = Math.sin( xm0 );
//        double x7thm1 = 7 * theta2 - 1;
////        if (!ISIMP) {
////            double c1sq = c1 * c1;
////            double d2 = 4 * a0dp * tsi * c1sq;
////            double temp = d2 * tsi * c1 / 3.0;
////            double d3 = (17 * a0dp + s4) * temp;
////            double d4 = 0.5 * temp * a0dp * tsi * (221 * a0dp + 31 * s4) * c1;
////            double t3cof = d2 + 2 * c1sq;
////            double t4cof = 0.25 * (3 * d3 + c1 * (12 * d2 + 10 * c1sq));
////            double t5cof = 0.2 * (3 * d4 + 12 * c1 * d3 + 6 * d2 * d2 + 15 * c1sq * (2 * d2 + c1sq));
////        }
//
//        // update for secular gravity and atmospheric drag
//
//        double xmdf = xm0 + xmdot * dt;
//        double omgadf = Math.toRadians(coe.aop) + omgdot * dt;
//        double xn0ddf = Math.toRadians(coe.lan) + xn0dot * dt;
//        double omega = omgadf;
//        double xmp = xmdf;
//        double tsq = dt * dt;
//        double xnode = xn0ddf + xn0dcf * tsq;
//        double tempa = 1 - c1 * dt;
//        double tempe = tle.BStar() * c4 * dt;
//        double templ = t2cof * tsq;
//        if (!ISIMP) {
//            // from previous jump statement
//            double c1sq = c1 * c1;
//            double d2 = 4 * a0dp * tsi * c1sq;
//            double prevtemp = d2 * tsi * c1 / 3.0;
//            double d3 = (17 * a0dp + s4) * prevtemp;
//            double d4 = 0.5 * prevtemp * a0dp * tsi * (221 * a0dp + 31 * s4) * c1;
//            double t3cof = d2 + 2 * c1sq;
//            double t4cof = 0.25 * (3 * d3 + c1 * (12 * d2 + 10 * c1sq));
//            double t5cof = 0.2 * (3 * d4 + 12 * c1 * d3 + 6 * d2 * d2 + 15 * c1sq * (2 * d2 + c1sq));
//
//            // current jump statement
//            double delomg = omgcof * dt;
//            double delm = xmcof * (Math.pow(1 + eta * Math.cos(xmdf), 3) - delm0);
//            double temp = delomg + delm;
//            xmp = xmdf + temp;
//            omega = omgadf - temp;
//            double tcube = tsq * dt;
//            double tfour = tcube * dt;
//            tempa = tempa - d2 * tsq - d3 * tcube - d4 * tfour;
//            tempe = tempe + tle.BStar() * C5 * (Math.sin(xmp) - sinm0);
//            templ = templ = templ + t3cof * tcube + tfour * (t4cof + dt * t5cof);
//        }
//
//        // 110
//        double a = a0dp * tempa * tempa;
//        double e = coe.ecc - tempe;
//        double xl = xmp + omega + xnode + xn0dp * templ;
//        double beta = Math.sqrt(1 - e * e);
//        double xn = XKE / Math.pow(a, 1.5);
//
//        // long period periodics
//
//        double axn = e * Math.cos(omega);
//        double temp = 1 / (a * beta * beta);
//        double xll = temp * xlcof * axn;
//        double aynl = temp * aycof;
//        double xlt = xl + xll;
//        double ayn = e * Math.sin(omega) + aynl;
//
//        // solve kepler's equation
//
//        double capu = (xlt - xnode) % (2 * PI);
//        temp2 = capu;
//        double temp4 = 0;
//        double temp5 = 0;
//        double temp6 = 0;
//        double sinepw = 0;
//        double cosepw = 0;
//        for (int i = 0; i < 10; i++) {
//            System.out.println("i = " + i);
//            sinepw = Math.sin(temp2);
//            cosepw = Math.cos(temp2);
//            temp3 = axn * sinepw;
//            temp4 = ayn * cosepw;
//            temp5 = axn * cosepw;
//            temp6 = ayn * sinepw;
//            double epw = (capu - temp4 + temp3 - temp2) / (1 - temp5 - temp6) + temp2;
//            if (Math.abs(epw - temp2) <= E6A) break;
//            temp2 = epw;
//        }
//
//        // short period preliminary quantities
//
//        // 140
//        double ecose = temp5 + temp6;
//        double esine = temp3 - temp4;
//        double elsq = axn * axn + ayn * ayn;
//        temp = 1 - elsq;
//        double pl = a * temp;
//        double r = a * (1 - ecose);
//        temp1 = 1 / r;
//        double rdot = XKE * Math.sqrt(a) * esine * temp1;
//        double rfdot = XKE * Math.sqrt(pl) * temp1;
//        temp2 = a * temp1;
//        double betal = Math.sqrt(temp);
//        temp3 = 1 / (1 + betal);
//        double cosu = temp2 * (cosepw =-axn + ayn * esine * temp3);
//        double sinu = temp2 * (sinepw - ayn - axn * esine * temp3);
//        double u = OrbitalMath.atan2(sinu, cosu);
//        double sin2u = 2 * sinu * cosu;
//        double cos2u = 2 * cosu * cosu - 1;
//        temp = 1 / pl;
//        temp1 = CK2 * temp;
//        temp2 = temp1 * temp;
//
//        // update for short periodics
//
//        double rk = r * (1 - 1.5 * temp2 * betal * x3thm1) + .5 * temp1 * x1mth2 * cos2u;
//        double uk = u - .25 * temp2 * x7thm1 * sin2u;
//        double xnodek = xnode + 1.5 * temp2 * cosi0 * sin2u;
//        double xinck = Math.toRadians(coe.inc) + 1.5 * temp2 * cosi0 * sini0 * cos2u;
//        double rdotk = rdot - xn * temp1 * x1mth2 * sin2u;
//        double rfdotk = rfdot + xn * temp1 * (x1mth2 * cos2u + 1.5 * x3thm1);
//
//        // orientation vectors
//
//        double sinuk = Math.sin(uk);
//        double cosuk = Math.cos(uk);
//        double sinik = Math.sin(xinck);
//        double cosik = Math.cos(xinck);
//        double sinnok = Math.sin(xnodek);
//        double cosnok = Math.cos(xnodek);
//        double xmx = -sinnok * cosik;
//        double xmy = cosnok * cosik;
//        double ux = xmx * sinuk + cosnok * cosuk;
//        double uy = xmy * sinuk + sinnok * cosuk;
//        double uz = sinik * sinuk;
//        double vx = xmx * cosuk - cosnok * sinuk;
//        double vy = xmy * cosuk - sinnok * sinuk;
//        double vz = sinik * cosuk;
//
//        // position and velocity
//
//        return new StateVectors(
//                new Vector(
//                        (rk * ux) * XKMPER * 1000,
//                        (rk * uy) * XKMPER * 1000,
//                        (rk * uz) * XKMPER * 1000
//                ),
//                new Vector(
//                        (rdotk * ux + rfdotk * vx) * XKMPER * 1000 / 60.0,
//                        (rdotk * uy + rfdotk * vy) * XKMPER * 1000 / 60.0,
//                        (rdotk * uz + rfdotk * vz) * XKMPER * 1000 / 60.0
//                )
//        );
//
//    }

}
