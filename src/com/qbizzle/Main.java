package com.qbizzle;

import java.util.Scanner;
import com.qbizzle.Math.Vector;
import com.sun.jdi.InternalException;

public class Main {

    Scanner scanner = new Scanner(System.in);
    final double GRAV_PARAMETER = 6.6743e-11; // m3*kg-1*s-2

    public static void main(String[] args) {
        Vector v1 = new Vector(21345.1342, 65345.673275, 134525.62341);
        Vector v2 = new Vector(653.23352, 2524.525643, 23425.534562);
        State state = new State(v1, v2);
        state.scaleVelocity(0.1);
        System.out.println(state);
    }
}

class State implements Cloneable {
    private Vector m_position;
    private Vector m_velocity;

    public State() {
        this(new Vector(), new Vector());
    }
    public State(Vector position, Vector velocity) {
        m_position = position;
        m_velocity = velocity;
    }

    public String toString() {
        return "Position: " + m_position.toString() + ", Velocity: " + m_velocity.toString();
    }
    public boolean equals(Object ob) {
        if (ob instanceof State) {
            State rhs = (State)ob;
            return (this.m_position.equals(rhs.m_position)
                && this.m_velocity.equals(rhs.m_velocity)
            );
        }
        throw new IllegalArgumentException("Object not of type 'State'");
    }
    public Object clone() {
        try {
            State tmp = (State)super.clone();
            tmp.m_position = (Vector)m_position.clone();
            tmp.m_velocity = (Vector)m_velocity.clone();
            return tmp;
        }
        catch (CloneNotSupportedException ex) {
            throw new InternalError(ex.toString());
        }
    }

    public Vector getPosition() {
        return (Vector)m_position.clone();
    }
    public Vector getVelocity() {
        return (Vector)m_velocity.clone();
    }

    public void setPosition(Vector position) {
        m_position = (Vector)position.clone();
    }
    public void setVelocity(Vector velocity) {
        m_velocity = (Vector)velocity.clone();
    }

    // TODO: add methods to Vector equivalent to += and -= and *= to simplify and speed up code.
    public void addPosition(Vector dPosition) {
        m_position = m_position.plus(dPosition);
//        return (Vector)m_position.clone();
    }
    public void addVelocity(Vector dVelocity) {
        m_velocity = m_velocity.plus(dVelocity);
//        return (Vector)m_velocity.clone();
    }
    public void scalePosition(double lambda) {
        m_position = m_position.scale(lambda);
//        return (Vector)m_position.clone();
    }
    public void scaleVelocity(double lambda) {
        m_velocity = m_velocity.scale(lambda);
//        return (Vector)m_velocity.clone();
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
