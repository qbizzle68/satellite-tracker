package com.qbizzle.rotation;

public class EulerAngles {
    double[] m_angles;

    public EulerAngles() {
        this(0.0, 0.0, 0.0);
    }

    public EulerAngles(double alpha, double beta, double gamma) {
        m_angles = new double[]{alpha, beta, gamma};
        for (int i = 0; i < 3; i++) {
            if (m_angles[i] >= 360.0 || m_angles[i] < 0)
                m_angles[i] %= 360.0;
        }
    }

    public void set(int index, double angle) {
        m_angles[index] = angle;
    }

    public double get(int index) {
        return m_angles[index];
    }

}
