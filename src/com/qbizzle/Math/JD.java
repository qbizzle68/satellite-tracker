package com.qbizzle.Math;

import java.util.Date;

public class JD {
    private final double m_julianDate;

//    only works if the date is after January 1, 1970
//    TODO: learn how to access individual elements of the date, then calculate the Julian Date appropriately.
//    TODO: does not accomidate for timezone offset, is calculator for GMT.
    public JD(Date date) {
        double elapsedTimeSec = date.getTime() / 1000.0;
        double unixJulianDate = 2440587.5;
        double numDays = elapsedTimeSec / 86400.0;
        m_julianDate = unixJulianDate + numDays;
    }
    public JD(int m, int d, int y, int h, int min, int s) {
        // TODO: validate the date values
        int julianNumber = (1461 * (y + 4800 + (m - 14)/12))/4 + (367 * (m - 2 - 12 * ((m - 14)/12)))/12 - (3 * ((y + 4900 + (m - 14)/12)/100))/4 + d - 32075;
        m_julianDate = julianNumber + ((h - 12) / 24.0) + (min / 1440.0) + (s / 86400.0);
    }
    public JD(double julianDate) {
        m_julianDate = julianDate;
    }

    public double JulianDate() {
        return m_julianDate;
    }
    public int Number() {
        return (int) m_julianDate;
    }
    public double Fraction() {
        return m_julianDate - (int) m_julianDate;
    }
}
