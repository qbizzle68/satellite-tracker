package com.qbizzle.satellite;

//import com.qbizzle.math.OrbitalMath;
//import com.qbizzle.math.Vector;
//import com.qbizzle.orbit.StateVectors;
//import com.qbizzle.orbit.TLE;
//import com.qbizzle.time.JD;
//
//public class Satellite extends SGP4TimeIndependentValues {
//    final String name;
//    final TLE tle;
//    boolean iFlag = false;
////    cache the most recent calculation internally
//    JD time;
//    StateVectors state;
//
//    public Satellite(TLE tle) {
//        super(tle);
//        this.iFlag = true;
//        this.name = tle.name();
//        this.tle = tle;
//    }
//
//    public StateVectors getState(JD t) {
//        return getState(t.Difference(new JD(this.tle)));
//    }
//
//    public StateVectors getState(double dt) {
////        update for secular gravity and atmospheric drag
//
//        double xmdf = me.xm0 + xmdot * dt;
//        double omgadf = me.omega0 + omgdot * dt;
//        double xnoddf = me.xnode0 + xn0dot * dt;
//        double omega = omgadf;
//        double xmp = xmdf;
//        double tsq = dt * dt;
//        double xnode = xnoddf + xnodcf * tsq;
//        double tempa = 1 - c1 * dt;
//        double tempe = tle.bStar() * c4 * dt;
//        double templ = t2cof * tsq;
//        if (!isImp) {
////            double c1sq = c1 * c1;
////            double d2 = 4 * a0dp * tsi * c1sq;
////            double temp = d2 * tsi * c1 / 3;
////            double d3 = (17 * a0dp + s4) * temp;
////            double d4 = 0.5 * temp * a0dp * tsi * (221 * a0dp + 31 * s4) * c1;
////            double t3cof = d2 + 2 * c1sq;
////            double t4cof = 0.25 * (3 * d3 + c1 * (12 * d2 + 10 * c1sq));
////            double t5cof = 0.2 * (3 * d4 + 12 * c1 * d3 + 6 * d2 * d2 + 15 * c1sq * (
////                    2 * d2 + c1sq));
//            double delomg = omgcof * dt;
//            double delm = xmcof * (Math.pow( 1 + eta * Math.cos(xmdf), 3) - delm0);
//            double temp = delomg + delm;
//            xmp = xmdf + temp;
//            omega = omgadf - temp;
//            double tcube = tsq * dt;
//            double tfour = dt * tcube;
//            tempa = tempa - d2 * tsq - d3 * tcube - d4 * tfour;
//            tempe = tempe + tle.bStar() * c5 * (Math.sin(xmp) - sinm0);
//            templ = templ + t3cof * tcube +
//                    tfour * (t4cof + dt * t5cof);
//        }
////        double a = Math.pow(a0dp * tempa, 2); only the tempa is raise to the power 2
//        double a = a0dp * Math.pow(tempa, 2);
//        double e = me.e0 - tempe;
//        double xl = xmp + omega + xnode + xn0dp * templ;
//        double beta = Math.sqrt(1 - e*e);
//        double xn = SGP4Constants.XKE / Math.pow(a, 1.5);
//
//        // long period periodics
//        double axn = e * Math.cos(omega);
//        double temp = 1 / (a * beta * beta);
//        double xll = temp * xlcof * axn;
//        double aynl = temp * aycof;
//        double xlt = xl + xll;
//        double ayn = e * Math.sin(omega) + aynl;
//
//        // solve keplers equation
//        double capu = (xlt - xnode) % SGP4Constants.TWOPI;
//        if (capu < 0) capu += SGP4Constants.TWOPI;
//        double temp2 = capu;
//        double sinepw = 0;
//        double cosepw = 0;
//        double temp3 = 0;
//        double temp4 = 0;
//        double temp5 = 0;
//        double temp6 = 0;
//        for (int i = 0; i < 10; i++) {
//            sinepw = Math.sin(temp2);
//            cosepw = Math.cos(temp2);
//            temp3 = axn * sinepw;
//            temp4 = ayn * cosepw;
//            temp5 = axn * cosepw;
//            temp6 = ayn * sinepw;
//            double epw = (capu - temp4 + temp3 - temp2) / (1 - temp5 - temp6) + temp2;
//            if (Math.abs(epw - temp2) <= SGP4Constants.E6A) break;
//            temp2 = epw;
//        }
//
//        // short period preliminary quantities
//        double ecose = temp5 + temp6;
//        double esine = temp3 - temp4;
//        double elsq = axn * axn + ayn * ayn;
//        temp = 1 - elsq;
//        double pl = a * temp;
//        double r = a * (1 - ecose);
//        double temp1 = 1.0 / r;
//        double rdot = SGP4Constants.XKE * Math.sqrt(a) * esine * temp1;
//        double rfdot = SGP4Constants.XKE * Math.sqrt(pl) * temp1;
//        temp2 = a * temp1;
//        double betal = Math.sqrt(temp);
//        temp3 = 1.0 / (1 + betal);
//        double cosu = temp2 * (cosepw - axn + ayn * esine * temp3);
//        double sinu = temp2 * (sinepw - ayn - axn * esine * temp3);
//        double u = OrbitalMath.atan2(sinu, cosu);
//        double sin2u = 2 * sinu * cosu;
//        double cos2u = 2 * cosu * cosu - 1;
//        temp = 1 / pl;
//        temp1 = SGP4Constants.CK2 * temp;
//        temp2 = temp1 * temp;
//
//        // update for short periodics
//        double rk = r * (1 - 1.5 * temp2 * betal * x3thm1) + 0.5 * temp1 * x1mth2 * cos2u;
//        double uk = u - 0.25 * temp2 * x7thm1 * sin2u;
//        double xnodek = xnode + 1.5 * temp2 * cosi0 * sin2u;
//        double xinck = me.xincl + 1.5 * temp2 * cosi0 * sini0 * cos2u;
//        double rdotk = rdot - xn * temp1 * x1mth2 * sin2u;
//        double rfdotk = rfdot + xn * temp1 * (x1mth2 * cos2u + 1.5 * x3thm1);
//
//        // orientation vectors
//        double sinuk = Math.sin(uk);
//        double cosuk = Math.cos(uk);
//        double sinik = Math.sin(xinck);
//        double cosik = Math.cos(xinck);
//        double sinnok = Math.sin(xnodek);
//        double cosnok = Math.cos(xnodek);
////        double xmx = -sinnok * sinuk + cosnok * cosuk; just typed this completely wrong
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
//        state = new StateVectors(
//                new Vector(
//                        rk * ux,
//                        rk * uy,
//                        rk * uz
//                ),
//                new Vector(
//                        rdotk * ux + rfdotk * vx,
//                        rdotk * uy + rfdotk * vy,
//                        rdotk * uz + rfdotk * vz
//                )
//        );
//        time = new JD(tle).Future(dt);
//        return state;
//    }
//
//}
//
//class MeanElements {
//    public double xm0, xnode0, omega0, e0, xincl, xn0, xndt20, xndd60;
//
//    public MeanElements(TLE tle) {
//        xm0 = tle.meanAnomaly();
//        xnode0 = tle.lan();
//        omega0 = tle.aop();
//        e0 = tle.eccentricity();
//        xincl = tle.inclination();
//        xn0 = tle.meanMotion() * SGP4Constants.TWOPI / SGP4Constants.XMNPDA;
//        xndt20 = tle.meanMotionDot();
//        xndd60 = tle.meanMotionDDot();
//    }
//}
//
//class SGP4Constants {
//    static final double
//        CK2     = 5.413080e-4,
//        CK4     = 0.62098875e-6,
//        E6A     = 1E-6,
//        Q0MS2T  = 1.88027916e-9,
//        S       = 1.01222928,
//        TOTHRD  = 2.0 / 3.0,
//        XJ3     = -0.253881e-5,
//        XKE     = 0.743669161e-1,
//        XKMPER  = 6378.135,
//        XMNPDA  = 1440.0,
//        AE      = 1.0,
//        DE2RA   = 0.174532925e-1,
//        PI      = Math.PI,
//        PIO2    = Math.PI / 2.0,
//        TWOPI   = 2.0 * Math.PI,
//        X3PIO2  = 3.0 * PIO2;
//}
//
//class SGP4TimeIndependentValues {
//    MeanElements me;
//    boolean isImp = false;
//    double cosi0, x3thm1, xn0dp, a0dp, s4, q0ms24, tsi, eta, c1, sini0, x1mth2, c4, c5, xmdot, omgdot, xn0dot,
//            omgcof, xmcof, xnodcf, t2cof, xlcof, aycof, delm0, sinm0, x7thm1, d2, d3, d4, t3cof, t4cof, t5cof;
////    double xn0dp, a0dp, s4, q0ms24;
////    double c1, c4, c5;
////    double xmdot, omgdot, xnodot, xnodcf, ISIMP, t2cof, tsi, omgcof, xmcof, eta, delm0;
////    double sinm0, xlcof, aycof, x3thm1, x1mth2, cosi0, sini0, x7thm1;
//    public SGP4TimeIndependentValues(TLE tle) {
////        double xn0dp, a0dp, s4, q0ms24;
////        double c1, c4, c5;
////        double xmdot, omgdot, xnodot, xnodcf, ISIMP, t2cof, tsi, omgcof, xmcof, eta, delm0;
////        double sinm0, xlcof, aycof, x3thm1, x1mth2, cosi0, sini0, x7thm1;
//
//        me = new MeanElements(tle);
//
////        Recover original mean motion (xn0dp) and semimajor axis (a0dp) from input elements
//        double a1 = Math.pow(SGP4Constants.XKE / me.xn0, SGP4Constants.TOTHRD);
//        cosi0 = Math.cos(me.xincl);
//        double theta2 = cosi0 * cosi0;
//        x3thm1 = 3 * theta2 - 1;
//        double e0sq = me.e0 * me.e0;
//        double beta02 = 1 - e0sq;
//        double beta0 = Math.sqrt(beta02);
//        double del1 = 1.5 * SGP4Constants.CK2 * x3thm1 / (a1 * a1 * beta0 * beta02);
//        double a0 = a1 * (1 - del1 * (0.5 * SGP4Constants.TOTHRD + del1 * (1 + 134.0 / 81.0 * del1)));
//        double del0 = 1.5 * SGP4Constants.CK2 * x3thm1 / (a0 * a0 * beta0 * beta02);
//        xn0dp = me.xn0 / (1 + del0);
//        a0dp = a0 / (1 - del0);
//
////        Initialization
//        if ((a0dp * (1 - me.e0) / SGP4Constants.AE) < (220 / SGP4Constants.XKMPER + SGP4Constants.AE))
//            isImp = true;
//        s4 = SGP4Constants.S;
//        q0ms24 = SGP4Constants.Q0MS2T;
//        double perige = (a0dp * (1 - me.e0) - SGP4Constants.AE) * SGP4Constants.XKMPER;
//        if (perige < 156) {
//            if (perige <= 98) s4 = 20;
//            else s4 = perige - 78;
//            q0ms24 = Math.pow((120 - s4) * SGP4Constants.AE / SGP4Constants.XKMPER, 4);
//            s4 = s4 / SGP4Constants.XKMPER + SGP4Constants.AE;
//        }
//        double pinvsq = 1.0 / (a0dp * a0dp * beta02 * beta02);
//        tsi = 1 / (a0dp - s4);
//        eta = a0dp * me.e0 * tsi;
//        double etasq = eta * eta;
//        double eeta = me.e0 * eta;
//        double psisq = Math.abs(1 - etasq);
//        double coef = q0ms24 * Math.pow(tsi, 4);
//        double coef1 = coef / Math.pow(psisq, 3.5); // this diverges from SGP4.java implementation
//        double c2 = coef1 * xn0dp * (a0dp * (1 + 1.5 * etasq + eeta * (4 + etasq)) + .75
//                * SGP4Constants.CK2 * tsi / psisq * x3thm1 * (8 + 3 * etasq * (8 + etasq)));
//        c1 = tle.bStar() * c2;
//        sini0 = Math.sin(me.xincl);
//        double a30ck2 = -SGP4Constants.XJ3 / SGP4Constants.CK2 * Math.pow(SGP4Constants.AE, 3);
//        double c3 = coef * tsi * a30ck2 * xn0dp * SGP4Constants.AE * sini0 / me.e0;
//        x1mth2 = 1 - theta2;
//        c4 = 2 * xn0dp * coef1 * a0dp * beta02 * (eta
//                * (2 + 0.5 * etasq) + me.e0 * (0.5 + 2 * etasq) - 2 * SGP4Constants.CK2 * tsi
//                / (a0dp * psisq) * (-3 * x3thm1 * (1 - 2 * eeta + etasq
//                * (1.5 - 0.5 * eeta)) + 0.75 * x1mth2 * (2 * etasq - eeta
//                * (1 + etasq)) * Math.cos(2 * me.omega0)));
//        c5 = 2 * coef1 * a0dp * beta02 * (1 + 2.75 * (etasq + eeta) + eeta * etasq);
//        double theta4 = theta2 * theta2;
//        double temp1 = 3 * SGP4Constants.CK2 * pinvsq * xn0dp;
//        double temp2 = temp1 * SGP4Constants.CK4 * pinvsq;
//        double temp3 = 1.25 * SGP4Constants.CK4 * pinvsq * pinvsq * xn0dp;
//        xmdot = xn0dp + 0.5 * temp1 * beta0 * x3thm1 + 0.0625 * temp2 * beta0
//                * (13 - 78 * theta2 + 137 * theta4);
//        double x1m5th = 1 - 5 * theta2;
//        omgdot = -0.5 * temp1 * x1m5th + 0.0625 * temp2 * (7 - 114 * theta2
//                + 395 * theta4) + temp3 * (3 - 36 * theta2 + 49 * theta4);
//        double xhdot1 = -temp1 * cosi0;
//        xn0dot = xhdot1 + (0.5 * temp2 * (4 - 19 * theta2) + 2 * temp3 * (3
//                - 7 *theta2)) * cosi0;
//        omgcof = tle.bStar() * c3 * Math.cos(me.omega0);
//        xmcof = -SGP4Constants.TOTHRD * coef * tle.bStar() * SGP4Constants.AE / eeta;
//        xnodcf = 3.5 * beta02 * xhdot1 * c1;
//        t2cof = 1.5 * c1;
//        xlcof = 0.125 * a30ck2 * sini0 * (3 + 5 * cosi0) / (1 + cosi0);
//        aycof = 0.25 * a30ck2 * sini0;
//        delm0 = Math.pow(1 + eta * Math.cos(me.xm0), 3);
//        sinm0 = Math.sin(me.xm0);
//        x7thm1 = 7 * theta2 - 1;
//        if (!isImp) {
//            double c1sq = c1 * c1;
//            d2 = 4 * a0dp * tsi * c1sq;
//            double temp = d2 * tsi * c1 / 3.0;
//            d3 = (17 * a0dp * s4) * temp;
//            d4 = 0.5 * temp * a0dp * tsi * (221 * a0dp + 31 * s4) * c1;
//            double t3cof = d2 + 2 * c1sq;
//            double t4cof = 0.25 * (3 * d3 + c1 * (12 * d2 + 10 * c1sq));
//            double t5cof = 0.2 * (3 * d4 + 12 * c1 * d3 + 6 * d2 * d2 + 15 * c1sq * (2 * d2 + c1sq));
//        }
//
//    }
//}