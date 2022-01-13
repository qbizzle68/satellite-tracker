package com.qbizzle.Math;

import com.qbizzle.Orbit.TLE;

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
        String[] strTokens = date.toString().split("[\s]");
        m_julianDate = GetDate(
                MonthTable(strTokens[1]),
                Integer.parseInt(strTokens[2]),
                Integer.parseInt(strTokens[5]),
                Integer.parseInt(strTokens[3].substring(0, 2)),
                Integer.parseInt(strTokens[3].substring(3, 5)),
                Integer.parseInt(strTokens[3].substring(6))
        );
//        this(
//                MonthTable(date.toString().split("[\s]")[1]),
//                Integer.parseInt(date.toString().split("[\s]")[2]),
//                Integer.parseInt(date.toString().split("[\s]")[5]),
//                Integer.parseInt(date.toString().split("[\s]")[3].substring(0, 2)),
//                Integer.parseInt(date.toString().split("[\s]")[3].substring(3, 5)),
//                Integer.parseInt(date.toString().split("[\s]")[3].substring(6))
//        );
    }
    public JD(TLE tle) {
        int year = tle.EpochYear();
        year += (year < 57) ? 2000 : 1900;
        int month = MonthHasDay((int)tle.EpochDay(), year);
        int day = (int)tle.EpochDay() - DaysPrecedingMonth(month, year);
        double partialDay = tle.EpochDay() - (int)tle.EpochDay();
        int hour = (int)(partialDay * 24);
        partialDay -= hour / 24.0;
        int minutes = (int)(partialDay * 1440.0);
        partialDay -= minutes / 1440.0;
        m_julianDate = GetDate(
                month,
                day,
                year,
                hour,
                minutes,
                (int)(partialDay * 86400.0)
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

    public static double GetDate(int m, int d, int y, int h, int min, int s) {
        int julianNumber = (1461 * (y + 4800 + (m - 14)/12))/4 + (367 * (m - 2 - 12 * ((m - 14)/12)))/12 - (3 * ((y + 4900 + (m - 14)/12)/100))/4 + d - 32075;
        return julianNumber + ((h - 12) / 24.0) + (min / 1440.0) + (s / 86400.0);
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
//    returns what month contains the day number
    private static int MonthHasDay(int day, int year) {
        int month = 1;
        while (true) {
            int dperm = DaysPerMonth(month, year);
            if (day > dperm) {
                day -= dperm;
                month++;
            }
            else return month;
        }
    }
//    returns the number of days in a given month
    public static int DaysPerMonth(int month, int year) {
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                return 28 + ((((year%4 == 0) && (year%100 != 0)) || (year%400 == 0)) ? 1 : 0);
        };
        return 31;
//        return switch (month) {
//            case 2 -> 28 + ((((year%4 == 0) && (year%100 != 0)) || (year%400 == 0)) ? 1 : 0);
//            case 3 -> 31;
//            case 4 -> 30;
//            case 5 -> 31;
//            case 6 -> 30;
//            case 7 -> 31;
//            case 8 -> 31;
//            case 9 -> 30;
//            case 10 -> 31;
//            case 11 -> 30;
//            case 12 -> 31;
//            default -> 31;
//        };
    }
//    returns the number of days that precede a given day
    private static int DaysPrecedingMonth(int month, int year) {
        int daySum = 0;
        for (int i = 1; i < month; i++) daySum += DaysPerMonth(i, year);
        return daySum;
    }
}