package com.qbizzle;

import java.util.Scanner;
import com.qbizzle.Math.Vector;
import com.qbizzle.Orbit.TLE;
import com.qbizzle.*;
import com.qbizzle.Orbit.BadTLEFormatException;
import com.sun.jdi.InternalException;

public class Main {

    Scanner scanner = new Scanner(System.in);
    final double GRAV_PARAMETER = 6.6743e-11; // m3*kg-1*s-2

    public static void main(String[] args) {
        String strZaryaTLE = "ISS (ZARYA)\n" +
                "1 25544U 98067A   08264.51782528 -.00002182  00000-0 -11606-4 0  2927\n" +
                "2 25544  51.6416 247.4627 0006703 130.5360 325.0288 15.72125391563537";
        String strTempSat1 = "TEMPSAT 1               \n" +
                "1 01512U 65065E   22008.55939465  .00000014  00000+0  71959-5 0  9999\n" +
                "2 01512  89.9018 220.0525 0071548 118.8571 305.9026 13.33444015744062";
        TLE zaryaTLE, tempSatTLE;
        try {
            zaryaTLE = new TLE(strZaryaTLE);
            tempSatTLE = new TLE(strTempSat1);
        } catch (BadTLEFormatException ex) {
            System.out.println(ex.toString());
            throw new InternalError();
        }
//        zaryaTLE.print();
        tempSatTLE.print();

    }
}

class State implements Cloneable {
    private Vector m_position;
    private Vector m_velocity;

//    constructors
    public State() {
        this(new Vector(), new Vector());
    }
    public State(Vector position, Vector velocity) {
        m_position = position;
        m_velocity = velocity;
    }

//    overloaded methods
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

//    getter methods
    public Vector getPosition() {
        return (Vector)m_position.clone();
    }
    public Vector getVelocity() {
        return (Vector)m_velocity.clone();
    }
//    setter methods
    public void setPosition(Vector position) {
        m_position = (Vector)position.clone();
    }
    public void setVelocity(Vector velocity) {
        m_velocity = (Vector)velocity.clone();
    }

//    updating Vector methods
    public void addPosition(Vector dPosition) {
        m_position.plusEquals(dPosition);
    }
    public void addVelocity(Vector dVelocity) {
        m_velocity.plusEquals(dVelocity);
    }
    public void scalePosition(double lambda) {
        m_position.scaleEquals(lambda);
    }
    public void scaleVelocity(double lambda) {
        m_velocity.scaleEquals(lambda);
    }
}




//class Body {
//
//}
