package com.qbizzle;

import java.util.Scanner;
import com.qbizzle.Math.Vector;

public class Main {

    Scanner scanner = new Scanner(System.in);
    final double GRAV_PARAMETER = 6.6743e-11; // m3*kg-1*s-2

    public static void main(String[] args) {
        Vector v1 = new Vector(1,2, 3);
        Vector v2 = new Vector(4, 5, 6);
        System.out.println(v1.norm());
    }
}
//
//class Orbit {
//    // units are meters, and radians where applicable except for inclination (degrees)
//    public double semiMajorAxis, eccentricity, lan, aop, inclination, trueAnomaly;
//
//    public Orbit(double sma, double ecc, double lan, double aop, double inc, double trueAnom) {
//        semiMajorAxis = sma;
//        eccentricity = ecc;
//        this.lan = lan;
//        this.aop = aop;
//        inclination = inc;
//        trueAnomaly = trueAnom;
//    }
//}
//
//class Body {
//
//}
