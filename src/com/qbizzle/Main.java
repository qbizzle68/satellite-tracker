package com.qbizzle;

import com.qbizzle.orbit.TLE;
import com.qbizzle.time.JD;
import com.qbizzle.tracking.Coordinates;
import com.qbizzle.tracking.SatellitePass;
import com.qbizzle.tracking.Tracker;

import java.util.Scanner;

public class Main {

    Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        String strZarya = """
                ISS (ZARYA)            \s
                1 25544U 98067A   22049.72913178  .00009079  00000+0  16774-3 0  9997
                2 25544  51.6430 203.2209 0005652 141.6789 224.1673 15.49843671326816""";
        TLE tleZarya = new TLE(strZarya);

        Coordinates hutch = new Coordinates(38.06084167, -97.92977222);
        JD viewTime = new JD(2, 18, 2022, 14, 44, 30, -6);
        Coordinates coords = new Coordinates(30,131);
        SatellitePass satPass = Tracker.getPassInfo(tleZarya, viewTime, coords);
        System.out.println(satPass);


    }

//    public static SatellitePass getPassInfo(TLE tle, JD passTime, Coordinates coords) {
//        // todo: how to we make a better guess than 10 minutes?
//        AltAz rise = riseSqueeze(tle, passTime.Future(-10.0 / 1440.0), passTime, coords, null);
//        AltAz set = setSqueeze(tle, rise.getEpoch(), passTime.Future(10.0 / 1440.0), coords, null);
//        AltAz first = firstSqueeze(tle, rise.getEpoch(), set.getEpoch(), coords, false, null);
//        AltAz last = lastSqueeze(tle, first.getEpoch(), set.getEpoch(), coords, null);
//        AltAz start, finish;
//        if (rise.getEpoch().Value() < first.getEpoch().Value()) start = first;
//        else start = rise;
//        if (set.getEpoch().Value() < last.getEpoch().Value()) finish = set;
//        else finish = last;
//        return new SatellitePass(
//                start,
//                finish,
//                new AltAz(0, 0, new JD(0)) // change this when we find the max
//        );
//    }

//    // pass a time the satellite is overhead, this will find when the sat crosses above
//    // the horizon, and the time it falls below
//    public static double[] times(TLE tle, Coordinates coords, JD t) {
//        // find the time of sat-rise
//        // can make way better guesses than 10 minutes but doing for simplicity
//        double riseTime = riseSqueeze(tle, t.Future(-30.0/1440.0), t, coords);
//        double setTime = setSqueeze(tle, t, t.Future(15.0/1440.0), coords);
////        return new double[]{riseTime, setTime};
//        double[] horizonTimes = {riseTime, setTime};
//        System.out.println("rise horizon: " + horizonTimes[0] + " set horizon: " + horizonTimes[1]);
//
//        // find the earliest and latest time the sat is in sunlight
//        double firstVisible = firstSqueeze(tle, new JD(riseTime), new JD(setTime), false);
//        System.out.println("first lit: " + firstVisible);
//        double lastVisible = lastSqueeze(tle, new JD(firstVisible), new JD(setTime));
//        System.out.println("last lit: " + lastVisible);
//        return new double[]{0, 0};
//    }

//    static double squeezeEpsilon = 1.0 / 864000.0; // 1/10th of a second
//
//    static AltAz riseSqueeze(TLE tle, JD lower, JD upper, Coordinates coords, AltAz rtn) {
////        if (upper.Difference(lower) <= squeezeEpsilon) return upper.Value();
//        if (upper.Difference(lower) <= squeezeEpsilon) return rtn;
//        JD biTime = lower.Future((upper.Value() - lower.Value()) / 2.0);
//        AltAz altaz = Tracker.getAltAz(tle, biTime, coords);
//        if (altaz.getAltitude() > 0) return riseSqueeze(tle, lower, biTime, coords, altaz);
////        if (Tracker.isAboveHorizon(tle, biTime, coords)) return riseSqueeze(tle, lower, biTime, coords);
//        else return riseSqueeze(tle, biTime, upper, coords, altaz);
////        else return riseSqueeze(tle, biTime, upper, coords);
//    }
//
//    static AltAz setSqueeze(TLE tle, JD lower, JD upper, Coordinates coords, AltAz rtn) {
////        if (upper.Difference(lower) <= squeezeEpsilon) return lower.Value();
//        if (upper.Difference(lower) <= squeezeEpsilon) return rtn;
//        JD biTime = lower.Future((upper.Value() - lower.Value()) / 2.0);
//        AltAz altaz = Tracker.getAltAz(tle, biTime, coords);
//        if (altaz.getAltitude() > 0) return setSqueeze(tle, biTime, upper, coords, altaz);
////        if (Tracker.isAboveHorizon(tle, biTime, coords)) return setSqueeze(tle, biTime, upper, coords);
//        else return setSqueeze(tle, lower, biTime, coords, altaz);
////        else return setSqueeze(tle, lower, biTime, coords);
//    }
//
//    static AltAz firstSqueeze(TLE tle, JD lower, JD upper, Coordinates geoPos, Boolean wasLight, AltAz rtn) {
//        if (upper.Difference(lower) <= squeezeEpsilon) {
////            if (wasLight) return upper.Value();
//            if (wasLight) return rtn;
////            else return -1;
//            // todo: else throw exception for sat never being lit
//        }
//        JD biTime = lower.Future((upper.Value() - lower.Value()) / 2.0);
//        Vector sunPosition = Sun.Position(biTime);
//        Vector satPosition = SGP4.Propagate(tle, biTime).Position();
//        AltAz altaz = Tracker.getAltAz(satPosition, biTime, geoPos);
//        if (!Eclipse.isEclipsed(satPosition.minus(), satPosition.minus().plus(sunPosition))) {
////            return firstSqueeze(tle, lower, biTime, true);
//            return firstSqueeze(tle, lower, biTime, geoPos, true, altaz);
//        }
//        else {
////            if (wasLight) return firstSqueeze(tle, biTime, upper, true);
//            if (wasLight) return firstSqueeze(tle, biTime, upper, geoPos, true, altaz);
////            else return firstSqueeze(tle, lower, biTime, false);
//            else return firstSqueeze(tle, lower, biTime, geoPos, false, altaz);
//        }
//    }
//
//    // assume the sat is at one point lit, meaning there is a starting time gotten from firstSqueeze().
//    // then start at that time squeeze towards the time its not lit
//    static AltAz lastSqueeze(TLE tle, JD lower, JD upper, Coordinates geoPos, AltAz rtn) {
////        if (upper.Difference(lower) <= squeezeEpsilon) return upper.Value();
//        if (upper.Difference(lower) <= squeezeEpsilon) return rtn;
//        JD biTime = lower.Future((upper.Value() - lower.Value()) / 2.0);
//        Vector sunPosition = Sun.Position(biTime);
//        Vector satPosition = SGP4.Propagate(tle, biTime).Position();
//        AltAz altaz = Tracker.getAltAz(satPosition, biTime, geoPos);
//        if (!Eclipse.isEclipsed(satPosition.minus(), satPosition.minus().plus(sunPosition)))
////            return lastSqueeze(tle, biTime, upper);
//            return lastSqueeze(tle, biTime, upper, geoPos, altaz);
////        else return lastSqueeze(tle, lower, biTime);
//        else return lastSqueeze(tle, lower, biTime, geoPos, altaz);
//    }
//
//    // don't know if this is the best way to do this. it makes some assumptions on the shape of the visible path
//    // that I'm not sure is always appropriate for different sized orbits...
//    static AltAz maxSqueeze(TLE tle, JD lower, JD upper, Coordinates geoPos, AltAz rtn) {
//        if (upper.Difference(lower) <= squeezeEpsilon) return rtn;
//        AltAz lowerAltAz = Tracker.getAltAz(tle, lower, geoPos);
//        JD biTime = new JD((lower.Value() + upper.Value()) / 2.0);
//        AltAz biAltAz = Tracker.getAltAz(tle, biTime, geoPos);
//        AltAz upperAltAz = Tracker.getAltAz(tle, upper, geoPos);
//        if (biAltAz.getAltitude() >= lowerAltAz.getAltitude()) {
//            // path goes up and then down
//            if (biAltAz.getAltitude()  >= upperAltAz.getAltitude()) {
//                if (lowerAltAz.getAltitude() >= upperAltAz.getAltitude()) return maxSqueeze(tle, lower, biTime, geoPos, biAltAz);
//                else return maxSqueeze(tle, biTime, upper, geoPos, biAltAz);
//            }
//            // only traveling up
//            else return maxSqueeze(tle, biTime, upper, geoPos, biAltAz);
//        }
//        // only traveling down
//        else return maxSqueeze(tle, lower, biTime, geoPos, biAltAz);
//    }

    static String getUTC(JD jd) {
        int J = jd.Number();
        int y = 4716;
        int j = 1401;
        int m = 2;
        int n = 12;
        int r = 4;
        int p = 1461;
        int v = 3;
        int u = 5;
        int s = 153;
        int w = 2;
        int B = 274277;
        int C = -38;
        int f = J + j + (((4 * J * B) / 146097) * 3) / 4 + C;
        int e = r * f + v;
        int g = (e % p) / r;
        int h = u * g + w;
        int D = (h % s) / u + 1;
        int M = (h / s + m) % n + 1;
        int Y = (e / p) - y + (n + m - M) / n;
        double htemp = jd.Fraction() + 0.5;
        if (htemp > 1) {
            D++; // this can cause the day to go over the valid number of days in a given month
            htemp -= 1.0;
        }
        int H = (int)(htemp * 24);
        htemp -= (H / 24.0);
        int Min = (int)(htemp * 1440.0);
        htemp -=(Min / 1440.0);
        return M + "/" + D + "/" + Y + " " + H + ":" + Min + ":" + (htemp * 86400.0);
    }

}