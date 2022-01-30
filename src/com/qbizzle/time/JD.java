/** @file
 * This file contains a Julian Date class to help facilitate with differentiating time.
 * Since any date (day, month, year) can be quickly converted to a Julian Date, the largest
 * unit of time that needs to be handled should be a solar day (86,400.0 seconds), with hours,
 * minutes and seconds being quickly converted to a fractional portion of a day. This allows
 * any two moments in time to be converted to Julian Dates, then computing the difference (or
 * adding/subtracting time to find another JD) is quick and efficient.
 * The main constructors use standardized java classes so as to handle checking formatting
 * without the need for further implementation or duplication of code.
 */

package com.qbizzle.time;

import com.qbizzle.orbit.TLE;
import java.util.*;

/**
 * A Julian Day class used to handle time computations. The JD class internally stores the Julian Day
 * for a specific date and time and can quickly compute differences between other Julian Days, or
 * find a future or past Julian Day given a time difference. The main unit of time considered with
 * Julian Days is the solar day.
 * @note A solar day is equivalent to 24 hours, 1440 minutes or 86400 seconds.
 */
public class JD {
    private final double m_julianDate;
    /// Number of seconds in a solar day.
    @SuppressWarnings("unused")
    public static final double SECONDSPERDAY = 86400.0;
    /// Number of minutes in a solar day.
    @SuppressWarnings("unused")
    public static final double MINUTESPERDAY = 1440.0;
    /// Number of hours in a solar day.
    @SuppressWarnings("unused")
    public static final double HOURSPERDAY = 24.0;
    /// Julian Date number of the J2000 epoch of 1/1/2000 12:00:00.
    public static final double J2000 = 2451545.0;

    /** Constructs the Julian Date from the Java Date object.
     * @param date Date object that the Julian Day is meant to represent. Ideal
     *             for using the now() method to compute current satellite positions.
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

    /**Constructs the Julian Date from the epoch element of a TLE./ Ideal for computing
     * future positions from the TLE epoch.
     * @param tle @link com.qbizzle.orbit.TLE TLE @endlink that contains the epoch used to create
     *            a Julian Date.
     */
    public JD(TLE tle) {
        m_julianDate = GetDate(12, 31, tle.EpochYear()-1, 0, 0, 0) + tle.EpochDay();// - 0.5;
    }

    /** Constructs the Julian Date directly from a known Julian Date number
     * @param julianDate A known Julian Date.
     */
    public JD(double julianDate) {
        m_julianDate = julianDate;
    }

    /** Construct the Julian Date from Gregorian Date components.
     * @param mon Month number of the year, starting with 1.
     * @param day Day of the month.
     * @param yr Year relative to 1 AD.
     * @param hr Hour of the day in 24 hour notation.
     * @param min Minute of the hour.
     * @param sec Seconds, can include additional fractions of a second (hence the double type).
     * @note Parameter values that are out of their normal range may not behave as expected.
     */
    public JD(int mon, int day, int yr, int hr, int min, double sec) {
        m_julianDate = GetDate(mon, day, yr, hr, min, sec);
    }

    /// @name Getter methods.
    /// Methods to retrieve values associated with the current Julian Date.
///@{*/

    /** Getter method for retrieving the Julian Date value.
     * @return The Julian Date value associated with this instance.
     */
    public double Value() {
        return m_julianDate;
    }

    /** Getter method for retrieving the Julian Date Number.
     * @return The Julian Day number associated with this instance.
     * @note This is the integer portion of @link com.qbizzle.time.JD#Value Value(). @endlink
     */
    public int Number() {
        return (int) m_julianDate;
    }

    /** Getter method for retrieving the Julian Date fractional part.
     * @return The fractional portion of the Julian Date.
     * @note This is not the fractional time of the day, but the fractional portion of the
     * 24-hour period following 12pm of the associated day.
     */
    public double Fraction() {
        return m_julianDate - (int) m_julianDate;
    }

///@}*/

    /// @name Computation methods.
    /// Methods used to compute values between or to create new Julian Dates.
///@{*/

    /** Computes the difference between two Julian Dates in solar days.
     * @param jd The Julian Date in which to find the difference.
     * @return The number of solar days between the two dates.
     * @note A negative return value indicates the @p jd parameter date occurs before
     * this instance date.
     */
    public double Difference(JD jd) {
        return m_julianDate - jd.m_julianDate;
    }

    /** Computes a Julian Date relative to the current instance.
     * @param days Number of solar days added to current date.
     * @return A Julian Date equal to the current date plus the @p days param.
     * @note A negative value for @p days will return a Julian Date that precedes
     * the date of the current instance.
     */
    public JD Future(double days) {
        return new JD(m_julianDate + days);
    }

///@}*/

    /** Get the month number of the abbreviated string.
     * A helper method to convert the string abbreviation from the Java Date class to the
     * corresponding month number.
     * @param str The string abbreviation of the month gotten from the
     *            <a href="https://docs.oracle.com/javase/8/docs/api/java/util/Date.html#toString--">Date.toString()</a> method.
     * @return The integer number of the month referring to the @p str parameter.
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

    /** Converts values of a Gregorian Date into a Julian Date.
     * @param mon Month number of the year, starting with 1.
     * @param day Day of the month.
     * @param yr Year relative to 1 AD.
     * @param hr Hour of the day in 24 hour notation.
     * @param min Minute of the hour.
     * @param sec Seconds, can include additional fractions of a second.
     * @return The number of days since 12:00:00 1/1/4713 BCE.
     * @note The algorithm used for converting dates can be found
     *     <a href="https://en.wikipedia.org/wiki/Julian_day#Converting_Gregorian_calendar_date_to_Julian_Day_Number">
     *     here</a>.
     */
    private static double GetDate(int mon, int day, int yr, int hr, int min, double sec) {
        int julianNumber = (1461 * (yr + 4800 + (mon - 14)/12))/4 + (367 * (mon - 2 - 12 * ((mon - 14)/12)))/12 - (3 * ((yr + 4900 + (mon - 14)/12)/100))/4 + day - 32075;
        return (double)julianNumber + ((hr - 12) / 24.0) + (min / 1440.0) + (sec / 86400.0);
    }
}