/**
 * This file contains a Julian Date class to help facilitate with differentiating time.
 * Since any date (day, month, year) can be quickly converted to a Julian Date the largest
 * unit of time that needs to be handled should be a solar day (86,400.0 seconds), with hours,
 * minutes and seconds being quickly converted to a fractional portion of a day. This allows
 * any two moments in time to be converted to Julian Dates, then computing the difference (or
 * adding/subtracting time to find another JD) is quick and efficient.
 * The main constructors use standardized java classes so as to handle checking formatting
 * without the need for further implementation or duplication of code.
 */

package com.qbizzle.Math;

import com.qbizzle.Orbit.TLE;
import java.util.*;

public class JD {
    private final double m_julianDate;
    /**
     * static final rates of various time quantities per day
     */
    public static final double SECONDSPERDAY = 86_400.0;
    public static final double MINUTESPERDAY = 1440.0;
    public static final double HOURSPERDAY = 24.0;

    /**
     * Constructs the Julian Date from the GregorianCalendar object
     * @param gcal GregorianCalendar that the Julian Day is meant to represent. Ideal
     *             for constructing a certain date and time (using GregorianCalendar's constructor)
     */
    public JD(GregorianCalendar gcal) {
        m_julianDate = GetDate(
                gcal.get(Calendar.MONTH),
                gcal.get(Calendar.DATE),
                gcal.get(Calendar.YEAR),
                gcal.get(Calendar.HOUR_OF_DAY),
                gcal.get(Calendar.MINUTE),
                gcal.get(Calendar.SECOND)
        );
    }

    /**
     * Constructs the Julian Date from the Date object
     * @param date Date object that the Julian Day is meant to represent. Ideal
     *             for using the now() method to compute current satellite position.
     */
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
    }

    /**
     * Constructs the Julian Date from the epoch element from a TLE. Ideal for computing
     * future positions from the TLE epoch.
     * @param tle TLE with the epoch to create a Julian Date from
     */
    public JD(TLE tle) {
        m_julianDate = GetDate(12, 31, tle.EpochYear()-1, 0, 0, 0) + tle.EpochDay();
    }

    /**
     * Constructs the Julian Date directly from a known Julian Date number
     * @param julianDate the Julian Date in double form.
     */
    public JD(double julianDate) {
        m_julianDate = julianDate;
    }

    /**
     * Turns elements of a Gregorian Date into a Julian Date as described by the algorithm
     * found at "https://en.wikipedia.org/wiki/Julian_day#Converting_Gregorian_calendar_date_to_Julian_Day_Number"
     * @param m Gregorian month
     * @param d day of the month
     * @param y year, a value for a year in BCE should be (1 - year(BCE))
     * @param h hour of the day [0-24) (note: a value greater than 23 would add more days to the date)
     * @param min minute of the hour [0-59] (note: a value greater than 59 would add more hours to the date)
     * @param s second of the minute [0-59] (note: a value greater than 59 would add more minutes to the date)
     * @return Julian Date associated with the date and time of the parameters
     */
    public static double GetDate(int m, int d, int y, int h, int min, int s) {
        int julianNumber = (1461 * (y + 4800 + (m - 14)/12))/4 + (367 * (m - 2 - 12 * ((m - 14)/12)))/12 - (3 * ((y + 4900 + (m - 14)/12)/100))/4 + d - 32075;
        return julianNumber + ((h - 12) / 24.0) + (min / 1440.0) + (s / 86400.0);
    }

    /**
     * @return the value of the Julian Date this object was constructed by
     */
    public double Value() {
        return m_julianDate;
    }

    /**
     * @return the integer portion of the Julian Date
     */
    public int Number() {
        return (int) m_julianDate;
    }

    /**
     * @return the fractional portion of the Julian Date (time past 12:00:00 of the given day)
     */
    public double Fraction() {
        return m_julianDate - (int) m_julianDate;
    }

    /**
     * Computes the difference between two Julian Dates in solar days
     * @param jd the Julian Date in which to find the difference
     * @return the number of solar days between the two dates. A negative
     *      number indicates the jd param is in the future
     */
    public double Difference(JD jd) {
        return m_julianDate - jd.m_julianDate;
    }

    /**
     * Computes a Julian Date in the past or future from the current date.
     * @param days number of solar days added to current date. a negative number will return
     *             a date in the past from the current date.
     * @return a Julian Date equal to the current date plus the days param.
     */
    public JD Future(double days) {
        return new JD(m_julianDate + days);
    }

    /**
     * helper method to return the month value from the string value returned by the java
     * Date class.
     * @param str string abbreviation of the month gotten from the Date.toString() method
     * @return the integer number of the month referring to the str param
     */
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

    /**
     * Computes what month contains the day of the given year. The year is needed to determine
     * how many days are in February (leap year)
     * @param day day number of the year
     * @param year year in order to determine length of February
     * @return the month number which contains the day of the year
     */
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

    /**
     * returns the number of days in a month using year to determine leap year
     * @param month month number of the year
     * @param year year number in order to determine February
     * @return the number of days in the requested month
     */
    public static int DaysPerMonth(int month, int year) {
        switch (month) {
            default:
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
        }
    }

    /**
     * Computes the number of days in the year that precede a given month of the year.
     * @param month month number
     * @param year needed to determine number of days in February
     * @return number of days that occur before the first of the month param
     */
    private static int DaysPrecedingMonth(int month, int year) {
        int daySum = 0;
        for (int i = 1; i < month; i++) daySum += DaysPerMonth(i, year);
        return daySum;
    }
}