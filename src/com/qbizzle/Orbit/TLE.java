package com.qbizzle.Orbit;
import com.qbizzle.Orbit.COE;
import com.qbizzle.Orbit.OrbitalMath;

public class TLE {
    String m_satelliteName;
//    line 1
    private int m_catalogNumber, m_epochYear, m_ephemeris, m_setNumber;
    private char m_classification;
    String m_cosparID;
    private double m_epoch, m_meanMotionDot, m_meanMotionDDot, m_BStar;
//    line 2
    private double m_inclination, m_lan, m_eccentricity, m_argumentOfPerigee, m_meanAnomaly, m_meanMotion;
    private int m_revNumber;

    public TLE(String tle) throws BadTLEFormatException {
        if (checkLines(tle)) {
            if (parseString(tle)) System.out.println("TLE String parse successful.");
            else System.out.println("TLE String parse failed.");
        }
    }
//    TODO: make a constructor with a File argument and read TLE from it.

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

    //    accessor methods
    public String Name() {
        return m_satelliteName;
    }
    public char Classification() {
        return m_classification;
    }
    public int CatalogNumber() {
        return m_catalogNumber;
    }
    public String CosparID() {
        return m_cosparID;
    }
    public int EpochYear() {
        return m_epochYear;
    }
    public double EpochDay() {
        return m_epoch;
    }
    public double Inclination() {
        return m_inclination;
    }
    public double LAN() {
        return m_lan;
    }
    public double Eccentricity() {
        return m_eccentricity;
    }
    public double AOP() {
        return m_argumentOfPerigee;
    }
    public double MeanAnomaly() {
        return m_meanAnomaly;
    }
    public double MeanMotion() {
        return m_meanMotion;
    }
    public double MeanMotionDot() {
        return m_meanMotionDot;
    }
    public double MeanMotionDDot() {
        return m_meanMotionDDot;
    }
    public double BStar() {
        return m_BStar;
    }
    public int Ephemeris() {
        return m_ephemeris;
    }
    public int TLESetNumber() {
        return m_setNumber;
    }
    public int RevolutionNumber() {
        return m_revNumber;
    }

//    TODO: use regex to check format of these lines later. for reference: https://ai-solutions.com/_help_Files/two-line_element_set_file.htm
    private boolean checkLines(String tle) throws BadTLEFormatException {
        String delims = "[\n]";
        String[] tokens = tle.split(delims);

//        to use regex, split lines with '\s' delimiter then check each token with a specific regex.
//        rules for tle: check number of lines, then check length of lines, checksum, number of tokens per line, then format of each token.
        if (tokens.length > 3) throw new BadTLEFormatException("Too many lines in TLE, check TLE format.");
        else if (tokens.length < 3) throw new BadTLEFormatException("Too few lines in TLE, check TLE format.");
        else if (tokens[1].split("[\s]+").length != 9) throw new BadTLEFormatException("Line 1 token number mismatch, check TLE format.");
        else if (tokens[2].split("[\s]+").length != 8) throw new BadTLEFormatException("Line 2 token number mismatch, check TLE format.");
        return true;
    }

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

    private boolean parseString(String tle) {
        String[] lines = tle.split("[\n]");
        String[] line1Tokens = lines[1].split("[\s]+");
        String[] line2Tokens = lines[2].split("[\s]+");

        m_satelliteName = lines[0];
//        parsing line 1
        m_catalogNumber = Integer.parseInt(line1Tokens[1].substring(0, 5));
        m_classification = line1Tokens[1].charAt(5);
        m_cosparID = line1Tokens[2];
        m_epochYear = Integer.parseInt(line1Tokens[3].substring(0, 2));
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
        m_meanMotion = Double.parseDouble(line2Tokens[7].substring(0, 11));
        m_revNumber = Integer.parseInt(line2Tokens[7].substring(11, line2Tokens[7].length()-1));

        return true;
    }

}