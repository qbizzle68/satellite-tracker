package com.qbizzle.Math;

import java.util.*;

public class JD {
    private final double m_julianDate;
    public static final double SECONDSPERDAY = 86400.0;
    public static final double MINUTESPERDAY = 1440.0;
    public static final double HOURSPERDAY = 24.0;

    public JD(GregorianCalendar gcal) {
        this(
                gcal.get(Calendar.MONTH),
                gcal.get(Calendar.DATE),
                gcal.get(Calendar.YEAR),
                gcal.get(Calendar.HOUR_OF_DAY),
                gcal.get(Calendar.MINUTE),
                gcal.get(Calendar.SECOND)
        );
    }
    public JD(Date date) {
        this(
                MonthTable(date.toString().split("[\s]")[1]),
                Integer.parseInt(date.toString().split("[\s]")[2]),
                Integer.parseInt(date.toString().split("[\s]")[5]),
                Integer.parseInt(date.toString().split("[\s]")[3].substring(0, 2)),
                Integer.parseInt(date.toString().split("[\s]")[3].substring(3, 5)),
                Integer.parseInt(date.toString().split("[\s]")[3].substring(6))
        );
    }
    private JD(int m, int d, int y, int h, int min, int s) {
        int julianNumber = (1461 * (y + 4800 + (m - 14)/12))/4 + (367 * (m - 2 - 12 * ((m - 14)/12)))/12 - (3 * ((y + 4900 + (m - 14)/12)/100))/4 + d - 32075;
        m_julianDate = julianNumber + ((h - 12) / 24.0) + (min / 1440.0) + (s / 86400.0);
    }
//    possibly make this argument milliseconds since UNIX epoch
    public JD(double julianDate) {
        m_julianDate = julianDate;
    }

    public double Value() {
        return m_julianDate;
    }
    public int Number() {
        return (int) m_julianDate;
    }
    public double Fraction() {
        return m_julianDate - (int) m_julianDate;
    }

    public double Difference(JD jd) {
        return m_julianDate - jd.m_julianDate;
    }

    private static int MonthTable(String str) {
        return switch (str) {
            case "Feb" -> 2;
            case "Mar" -> 3;
            case "Apr" -> 4;
            case "May" -> 5;
            case "Jun" -> 6;
            case "Jul" -> 7;
            case "Aug" -> 8;
            case "Sep" -> 9;
            case "Oct" -> 10;
            case "Nov" -> 11;
            case "Dec" -> 12;
            default -> 1;
        };
    }
}