package com.qbizzle.Orbit;
import com.qbizzle.Math.Vector;

public class StateVectors implements Cloneable {
    private Vector m_position, m_velocity;

    public StateVectors(Vector position, Vector velocity) {
        m_position = position;
        m_velocity = velocity;
    }
    public StateVectors(TLE tle) {
        StateVectors tmp = FutureState.GetState(tle, 0);
        m_position = tmp.m_position;
        m_velocity = tmp.m_velocity;
    }

//    method overloading
    public String toString() {
        return "Position: " + m_position.toString() + ", Velocity " + m_velocity.toString();
    }
    public boolean equals(Object ob) {
        if (ob instanceof StateVectors) {
            StateVectors state = (StateVectors) ob;
            return (m_position.equals(state.m_position) && m_velocity.equals(state.m_velocity));
        }
        else throw new IllegalArgumentException("Object not of type 'StateVectors'");
    }
    public Object clone() {
        try {
            StateVectors rtn = (StateVectors)super.clone();
            rtn.m_position = (Vector) m_position.clone();
            rtn.m_velocity = (Vector) m_velocity.clone();
            return rtn;
        } catch(CloneNotSupportedException ex) {
            throw new InternalError();
        }
    }

//    accessor methods
    public Vector Position() {
        return m_position;
    }
    public Vector Velocity() {
        return m_velocity;
    }

}
