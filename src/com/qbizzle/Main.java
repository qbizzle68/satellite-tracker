package com.qbizzle;

import java.util.Scanner;
import com.qbizzle.Math.Vector;
import com.sun.jdi.InternalException;

public class Main {

    Scanner scanner = new Scanner(System.in);
    final double GRAV_PARAMETER = 6.6743e-11; // m3*kg-1*s-2

    public static void main(String[] args) {

    }
}

//class State extends Clonable {
//    private Vector m_position;
//    private Vector m_velocity;
//
//    public State() {
//        this(new Vector(), new Vector());
//    }
//    public State(Vector position, Vector velocity) {
//        m_position = position;
//        m_velocity = velocity;
//    }
//
//    public String toString() {
//        return "Position: " + m_position.toString() + ", Velocity: " + m_velocity.toString();
//    }
//    public boolean equals(Object ob) {
//        if (ob instanceof State) {
//            State rhs = (State)ob;
//            return (this.m_position.equals(rhs.m_position)
//                && this.m_velocity.equals(rhs.m_velocity)
//            );
//        }
//        throw new IllegalArgumentException("Object not of type 'State'");
//    }
//    public Object clone() {
//        try {
//            State tmp = (State)super.clone();
//            tmp.m_position = (Vector)m_position.clone();
//            tmp.m_velocity = (Vector)m_velocity.clone();
//        }
//        catch (CloneNotSupportedException ex) {
//            throw InternalError(ex.toString());
//        }
//    }
//}

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
