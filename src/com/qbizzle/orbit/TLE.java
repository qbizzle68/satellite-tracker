/** @file
 * This file contains the TLE class, which is used to read a Two Line Element string and
 * parse the elements into their appropriate values.
 */

package com.qbizzle.orbit;

import com.qbizzle.exception.InvalidTLEException;

/** This is a container class for TLE elements, and has the ability to parse
 * a TLE string into its distinct elements. Any other class wishing to be constructed
 * or operated on using values from a two line element should first construct a TLE instance,
 * then use that object to retrieve whichever data may be necessary.
 * @todo Create a constructor that takes a File argument and reads a TLE from a file.
 */
// maybe store all these data in a map?
public class TLE {
    private String m_satelliteName;
//    line 1
    private int m_catalogNumber, m_epochYear, m_ephemeris, m_setNumber;
    private char m_classification;
    private String m_cosparID;
    private double m_epoch, m_meanMotionDot, m_meanMotionDDot, m_BStar;
//    line 2
    private double m_inclination, m_lan, m_eccentricity, m_argumentOfPerigee, m_meanAnomaly, m_meanMotion;
    private int m_revNumber;

    /** Constructs a TLE instance from a string. The TLE should conform with the
     * standards used by other satellite tracking agencies such as NORAD. If the
     * TLE string is retrieved from common online repositories such as Celestrak
     * then formatting shouldn't be an issue.
     * @param tle A string containing the two line element separated into three lines.
     * @throws InvalidTLEException Ane exception will be thrown if some part of the
     * formatting is invalid.
     * @note Even though this is a two-line element, three lines are expected, with
     * the '0th' line being the satellite name.
     */
    public TLE(String tle) throws InvalidTLEException {
        if (checkLines(tle)) {
            if (parseString(tle)) System.out.println("TLE String parse successful.");
            else System.out.println("TLE String parse failed.");
        }
    }

    /** Creates a string with all the elements represented in name value pairs.
     * @return String to display the element values of the TLE.
     */
    public String toString() {
        return "Satellite name: " + m_satelliteName
            + "Line 1:"
            + "Catalog number: " + m_catalogNumber
            + "Classification: " + m_classification
            + "COSPAR ID: " + m_cosparID
            + "Epoch year: " + m_epochYear
            + "Epoch day: " + m_epoch
            + "Mu Dot: " + m_meanMotionDot
            + "Mu DDot: " + m_meanMotionDDot
            + "B*: " + m_BStar
            + "Ephemeris: " + m_ephemeris
            + "Set number: " + m_setNumber
            + "Line 2:"
            + "Inclination: " + m_inclination
            + "LAN: " + m_lan
            + "Eccentricity: " + m_eccentricity
            + "Argument of Perigee: " + m_argumentOfPerigee
            + "Mean Anomaly: " + m_meanAnomaly
            + "Mean Motion: " + m_meanMotion
            + "Revolution number: " + m_revNumber;
    }

    /// @name Accessor Methods
    /// Methods used to access the elements of a TLE.
///@{

    /** Gets the name of the satellite, the 0th line of the TLE.
     * @return String with the name of the satellite.
     */
    public String Name() {
        return m_satelliteName;
    }

    /** Gets the classification of the satellite, Unclassified, Classified or Secret.
     * @return A character with the classification of the satellite.
     */
    public char Classification() {
        return m_classification;
    }

    /** Gets the five digit catalog number of the satellite.
     * @return The catalog number of the satellite.
     */
    public int CatalogNumber() {
        return m_catalogNumber;
    }

    /** Gets the COSPAR ID of the satellite. A single string consisting of the launch year,
     * launch number of the year and the piece of the launch.
     * @return A string representation of the COSPAR ID.
     */
    public String CosparID() {
        return m_cosparID;
    }

    /** Gets the year of the TLE epoch or time the TLE was created.
     * @return Year the TLE was created.
     */
    public int EpochYear() {
        return m_epochYear;
    }

    /** Gets the day number of the TLE epoch.
     * @return The day number in solar days in which the TLE was created.
     */
    public double EpochDay() {
        return m_epoch;
    }

    /** Gets the inclination of the orbit in @em degrees.
     * @return The inclination of the orbit.
     */
    public double Inclination() {
        return m_inclination;
    }

    /** Gets the Longitude of the Ascending Node (or RAAN) of the orbit in @em degrees.
     * @return The LAN of the orbit.
     */
    public double LAN() {
        return m_lan;
    }

    /** Gets the eccentricity of the orbit.
     * @return The eccentricity of the orbit.
     */
    public double Eccentricity() {
        return m_eccentricity;
    }

    /** Gets the Argument Of Periapsis of the orbit in @em degrees.
     * @return The argument of periapsis of the orbit.
     */
    public double AOP() {
        return m_argumentOfPerigee;
    }

    /** Gets the mean anomaly of the orbit in @em degrees. Can be used to compute the
     * true anomaly of the orbit.
     * @return The mean anomaly of the orbit.
     */
    public double MeanAnomaly() {
        return m_meanAnomaly;
    }

    /** Gets the mean motion of the orbit in rev / day. Can be used to compute the
     * semi-major axis of the orbit.
     * @return The mean motion of the orbit.
     */
    public double MeanMotion() {
        return m_meanMotion;
    }

    /** Gets the first derivative of mean motion / 2 in rev / day^2.
     * @return Half the first derivative of mean motion.
     */
    public double MeanMotionDot() {
        return m_meanMotionDot;
    }

    /** Gets the second derivative of mean motion / 6 in rev / day^3.
     * @return A sixth of the second derivative of mean motion.
     */
    public double MeanMotionDDot() {
        return m_meanMotionDDot;
    }

    /** Gets the B* coefficient also known as the drag term.
     * @return The B* value of the tle.
     */
    public double BStar() {
        return m_BStar;
    }

    /** Gets the ephemeris type. This number is almost exclusively 0, although a value of 2 signifies
     * a different model used to propagate drag.
     * @return The ephemeris type of the TLE.
     */
    public int Ephemeris() {
        return m_ephemeris;
    }

    /** Gets the TLE set number, which is incremented whenever a new TLE is generated for this satellite.
     * @return The TLE set number.
     */
    public int TLESetNumber() {
        return m_setNumber;
    }

    /** Gets the revolution number of the satellite at the epoch.
     * @return The revolution number of the satellite.
     */
    public int RevolutionNumber() {
        return m_revNumber;
    }

///@}

    /** Checks each line of the TLE to ensure it has the correct formatting.
     * @param tle TLE string used to construct a TLE instance.
     * @return True if the string is formatted correctly.
     * @throws InvalidTLEException Thrown if there is an error in the TLE format.
     * @todo Use regex to check the format of these lines. for reference: https://ai-solutions.com/_help_Files/two-line_element_set_file.htm
     */
    private boolean checkLines(String tle) throws InvalidTLEException {
        String delims = "[\n]";
        String[] tokens = tle.split(delims);

//        to use regex, split lines with '\s' delimiter then check each token with a specific regex.
//        rules for tle: check number of lines, then check length of lines, checksum, number of tokens per line, then format of each token.
        if (tokens.length > 3) throw new InvalidTLEException("Too many lines in TLE, check TLE format.");
        else if (tokens.length < 3) throw new InvalidTLEException("Too few lines in TLE, check TLE format.");
        else if (tokens[1].split("[\s]+").length != 9) throw new InvalidTLEException("Line 1 token number mismatch, check TLE format.");
        else if (tokens[2].split("[\s]+").length != 8) throw new InvalidTLEException("Line 2 token number mismatch, check TLE format.");
        return true;
    }

    /** Method to corroborate the checksum value in each line.
     * @param line A line of the TLE which the characters are summed together.
     * @return True if the checksum is correct, false if otherwise.
     */
    @SuppressWarnings("unused")
    private boolean checksum(String line) {
        int sum = 0;
        for (int i = 0; i < line.length()-1; i++) {
//            only add characters that are numbers or '-' character
            int asciiVal = line.charAt(i) - '0';
//            after line 39: '-' ascii value goes from 45 to -3
            if ((asciiVal >= 0 && asciiVal <= 9) || asciiVal == -3) {
                if (asciiVal == -3) sum++;
                else sum += asciiVal;
            }
        }
        return ((sum & 10) == (line.charAt(line.length()-1) - '0'));
    }

    /** Method to parse the string into individual elements. This method is the
     * meat of the TLE class. The string is parsed into lines, then each line
     * is parsed into individual elements and those elements are stored in
     * their corresponding variable.
     * @param tle String that contains the TLE.
     * @return True if the string was parsed successfully, false if otherwise.
     * @todo Evaluate if this should be a void function, right now it can only return true.
     */
    private boolean parseString(String tle) {
        String[] lines = tle.split("[\n]");
        for (int i = 0; i < 3; i++) lines[i] = lines[i].trim();
        String[] line1Tokens = lines[1].split("[\s]+");
        String[] line2Tokens = lines[2].split("[\s]+");

        m_satelliteName = lines[0];
//        parsing line 1
        m_catalogNumber = Integer.parseInt(line1Tokens[1].substring(0, 5));
        m_classification = line1Tokens[1].charAt(5);
        m_cosparID = line1Tokens[2];
        m_epochYear = Integer.parseInt(line1Tokens[3].substring(0, 2));
        if (m_epochYear >= 57) m_epochYear += 1900;
        else m_epochYear += 2000;
        m_epoch = Double.parseDouble(line1Tokens[3].substring(2));
        m_meanMotionDot = Double.parseDouble(line1Tokens[4]);
        String strDDot;
        if (line1Tokens[5].length() == 8) { // if there is a leading +/-
            strDDot = line1Tokens[5].charAt(0) + ('.' + line1Tokens[5].substring(1, 6))
                    + 'e' + line1Tokens[5].substring(6);
        } else {
            strDDot = ('.' + line1Tokens[5].substring(0, 5)) + 'e' + line1Tokens[5].substring(5);
        }
        m_meanMotionDDot = Double.parseDouble(strDDot);
        String strBStar;
        if (line1Tokens[6].length() == 8) { // if there is a leading +/-
            strBStar = line1Tokens[6].charAt(0) + ('.' + line1Tokens[6].substring(1, 6))
                    + 'e' + line1Tokens[6].substring(6);
        } else {
            strBStar = ('.' + line1Tokens[6].substring(0, 5)) + 'e' + line1Tokens[6].substring(5);
        }
        m_BStar = Double.parseDouble(strBStar);
        m_ephemeris = Integer.parseInt(line1Tokens[7]);
        m_setNumber = Integer.parseInt(line1Tokens[8].substring(0, line1Tokens[8].length()-1));
//        parsing line 2
        m_inclination = Double.parseDouble(line2Tokens[2]);
        m_lan = Double.parseDouble(line2Tokens[3]);
        m_eccentricity = Double.parseDouble('.' + line2Tokens[4]);
        m_argumentOfPerigee = Double.parseDouble(line2Tokens[5]);
        m_meanAnomaly = Double.parseDouble(line2Tokens[6]);
        m_meanMotion = Double.parseDouble(line2Tokens[7].substring(0, line2Tokens[7].length()-6));
        m_revNumber = Integer.parseInt(line2Tokens[7].substring(line2Tokens[7].length()-6, line2Tokens[7].length()-1));

        return true;
    }

}