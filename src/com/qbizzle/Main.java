package com.qbizzle;

import com.qbizzle.coordinates.GeoPosition;
import com.qbizzle.exception.InvalidTLEException;
import com.qbizzle.http.Requests;
import com.qbizzle.math.Matrix;
import com.qbizzle.orbit.StateVectors;
import com.qbizzle.orbit.TLE;
import com.qbizzle.referenceframe.EulerAngles;
import com.qbizzle.referenceframe.EulerOrderList;
import com.qbizzle.rotation.Rotation;
import com.qbizzle.time.JD;
import com.qbizzle.time.SiderealTime;
import com.qbizzle.tracking.*;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Date;
import java.util.Scanner;
import java.util.Vector;

public class Main {

    /**
     * Global variables to access in all methods.
     */
    static Scanner scanner = new Scanner(System.in);
    static GeoPosition geoPos = null;
    static TLE satTle = null;
    static double duration = 7.0;

    /**
     * Global strings for holding menu contents.
     */
    static String mainMenuHeader;
    static Vector<String> mainMenuList;
    static String locationMenuHeader;
    static Vector<String> locationMenuList;
    static String satelliteMenuHeader;
    static Vector<String> satelliteMenuList;

    enum MainMenuOption {
        GET_PASSES(1), SET_LOCATION(2), SET_SATELLITE(3), SET_DURATION(4), GET_DETAILS(5), EXIT(6);

        private int id;
        private MainMenuOption(int id) {
            this.id = id;
        }
        public static MainMenuOption fromID(int id) {
            for (MainMenuOption option : values()) {
                if (option.id == id) return option;
            }
            return null;
        }
    }

    enum LocationMenuOption {
        BY_ADDRESS(1), MANUAL(2), GO_BACK(3);

        private int id;
        private LocationMenuOption(int id) {
            this.id = id;
        }
        public static LocationMenuOption fromID(int id) {
            for (LocationMenuOption option : values()) {
                if (option.id == id) return option;
            }
            return null;
        }
    }

    enum SatelliteMenuOption {
        CELESTRAK(1), MANUAL(2), GO_BACK(3);

        private int id;
        private SatelliteMenuOption(int id) {
            this.id = id;
        }
        public static SatelliteMenuOption fromID(int id) {
            for (SatelliteMenuOption option : values()) {
                if (option.id == id) return option;
            }
            return null;
        }
    }

    public static void main(String[] args) {
        try {
            initialize();
        } catch (Exception e) {
            System.out.println("Error occurred loading default satellite.");
            System.out.println("Satellite must be loaded to avoid error.");
        }

        MainMenuOption menuInput;
        do {
            menuInput = MainMenuOption.fromID(displayMenu(mainMenuHeader, mainMenuList));
            switch (menuInput) {
                case GET_PASSES -> getPassesHandler();
                case SET_LOCATION -> setLocationHandler();
                case SET_SATELLITE -> {
                    setSatelliteHandler();
                    System.out.println(satTle);
                }
                case SET_DURATION -> duration = getNumber("Enter a duration in days to calculate passes (max 14)", 0, 14);
                case GET_DETAILS -> {
                    System.out.println("Location: " + geoPos);
                    System.out.println("TLE: " + satTle);
                    System.out.println("Duration: " + duration);
                }
                default -> {
                }
            }
        } while (menuInput != MainMenuOption.EXIT);

    }

    static double epsilon = 1e-4;
    static JD getSetTime(TLE tle, JD t, GeoPosition geoPos) {
        StateVectors state = SGP4.Propagate(tle, t);
        Matrix rotationMatrix = Rotation.getEulerMatrix(
                EulerOrderList.ZYX,
                new EulerAngles(
                        SiderealTime.LST(t, geoPos.getLongitude()) * 15.0,
                        90 - geoPos.getLatitude(),
                        0.0
                )
        );
        StateVectors sezState = new StateVectors(
                Tracker.getSEZPosition(state.Position(), t, geoPos),
                Rotation.RotateTo(rotationMatrix, state.Velocity().exclude(state.Position()))
        );
        AltAz altaz = Tracker.getAltAz(tle, t, geoPos);
        System.out.println("time: " + t.date(-6) + " alt: " + altaz.getAltitude());
        if (Math.abs(altaz.getAltitude()) < epsilon) return t;
        else {
            double angVelocity = sezState.Velocity().mag() / sezState.Position().mag(); // radians / s
            double timeToHorizon = Math.toRadians(altaz.getAltitude()) / angVelocity;
            return getSetTime(
                    tle,
                    t.Future(timeToHorizon / 86400.0),
                    geoPos
            );
        }
    }

    static JD getRiseTime(TLE tle, JD t, GeoPosition geoPos) {
        StateVectors state = SGP4.Propagate(tle, t);
        Matrix rotationMatrix = Rotation.getEulerMatrix(
                EulerOrderList.ZYX,
                new EulerAngles(
                        SiderealTime.LST(t, geoPos.getLongitude()) * 15.0,
                        90 - geoPos.getLatitude(),
                        0.0
                )
        );
        StateVectors sezState = new StateVectors(
                Tracker.getSEZPosition(state.Position(), t, geoPos),
                Rotation.RotateTo(rotationMatrix, state.Velocity().exclude(state.Position()))
        );
        AltAz altaz = Tracker.getAltAz(tle, t, geoPos);
        System.out.println("time: " + t.date(-6) + " alt: " + altaz.getAltitude());
        if (Math.abs(altaz.getAltitude()) < epsilon) return t;
        else {
            double angVelocity = sezState.Velocity().mag() / sezState.Position().mag(); // radians / s
            double timeToHorizon = Math.toRadians(altaz.getAltitude()) / angVelocity;
            return getRiseTime(
                    tle,
                    t.Future(-timeToHorizon / 86400.0),
                    geoPos
            );
        }
    }

    static void getPassesHandler() {
        JD now = new JD(new Date());
        Vector<SatellitePass> passList = Tracker.getPasses(satTle, now, now.Future(duration), geoPos);
        if (passList.isEmpty()) System.out.println("No passes for this period");
        else {
            for (int i = 1; i <= passList.size(); i++) {
                SatellitePass satPass = passList.get(i-1);
                String strPass = "Pass " + i +
                        "\nfirst visible: " +
                        satPass.getVisibleTime().date() + // can add time zone here if we set it up
                        " | direction: " +
                        satPass.getVisibleDirection() +
                        " | height: " +
                        satPass.getVisibleHeight() +
                        "\nmax time: " +
                        satPass.getMaxTime().date() +
                        " | direction: " +
                        satPass.getMaxDirection() +
                        " | height: " +
                        satPass.getMaxHeight() +
                        "\ndisappear time: " +
                        satPass.getDisappearTime().date() +
                        " | direction: " +
                        satPass.getDisappearDirection() +
                        " | height: " +
                        satPass.getDisappearHeight();
                System.out.println(strPass);
            }
        }
    }

    static void setLocationHandler() {
        LocationMenuOption locationInput;
        do {
            locationInput = LocationMenuOption.fromID(displayMenu(locationMenuHeader, locationMenuList));
            switch (locationInput) {
                case BY_ADDRESS -> {
                    System.out.print("Enter address\nInput>");
                    scanner.next();
                    String address = scanner.nextLine();
                    try {
                        geoPos = Requests.getGeoPosition(address);
                        locationInput = LocationMenuOption.GO_BACK;
                    } catch (IOException e) {
//                        todo: find exactly what throws this
                        System.out.println("IOException occurred, try again.");
                    } catch (ParseException e) {
                        System.out.println("Unable to parse response.");
                    } catch (InterruptedException e) {
                        System.out.println("HTTP operation was interrupted.");
                    }
                }
                case MANUAL -> {
                    double lat, lng;
                    lat = getNumber("Enter latitude", -90, 90);
                    lng = getNumber("Enter longitude", -180, 180);
                    geoPos = new GeoPosition(lat, lng);
                    locationInput = LocationMenuOption.GO_BACK;
                }
            }
        } while (locationInput != LocationMenuOption.GO_BACK);
    }

    static double getNumber(String message, double min, double max) {
        double input;
        System.out.print(message + "\nInput>");
        if (scanner.hasNextDouble()) input = scanner.nextDouble();
        else {
            scanner.next(); // why do i need this?
            System.out.println("Input must be a valid number.");
            return getNumber(message, min, max);
        }

        if (input < min || input > max) {
            System.out.println("Input must be a valid number between " + min + " and " + max + ".");
            return getNumber(message, min, max);
        }
        return input;
    }

    static void setSatelliteHandler() {
        SatelliteMenuOption satInput;
        do {
            satInput = SatelliteMenuOption.fromID(displayMenu(satelliteMenuHeader, satelliteMenuList));
            switch (satInput) {
                case CELESTRAK -> {
//                    todo: be able to choose how to query sats.
                    System.out.println("Enter satellite name\nInput>");
                    scanner.next();
                    String satName = scanner.nextLine();
                    satName.replace("\n", "");
                    Vector<TLE> satList = null;
                    try {
                        satList = Requests.getTLEList(satName);
                    } catch (Exception e) {
                        System.out.println("Error retrieving TLEs");
                    }
                    if (satList.isEmpty()) System.out.println("No satellites found");
                    else if (satList.size() > 1) {
//                        System.out.println("Which TLE would you like to use?");
                        for (int i = 1; i <= satList.size(); i++) {
                            System.out.println(i + ". " + satList.get(i-1));
                        }
                        int tleIndex = (int)getNumber("Which TLE would you like to use?", 1, satList.size());
                        satTle = satList.get(tleIndex-1);
                        satInput = SatelliteMenuOption.GO_BACK;
                    }
                    else {
                        satTle = satList.get(0);
                    }
                }
                case MANUAL -> {
                    System.out.println("Enter TLE (Satellite name as first line)\nInput>");
                    scanner.next();
//                    todo: this could easily fail, need to add checks
                    StringBuilder strTle = new StringBuilder(scanner.nextLine())
                            .append("\n" + scanner.nextLine())
                            .append("\n" + scanner.nextLine());
                    try {
                        satTle = new TLE(strTle.toString());
                        satInput = SatelliteMenuOption.GO_BACK;
//                        todo: add specific tle exceptions to describe whats wrong
                    } catch (InvalidTLEException e) {
                        System.out.println("Invalid TLE");
                    }
                }
            }
        } while (satInput != SatelliteMenuOption.GO_BACK);
    }

    static void initialize() throws IOException, InterruptedException {
        geoPos = new GeoPosition(38.060017, -97.930495);
        satTle = Requests.getTLEList("ISS (ZARYA").get(0);
        mainMenuHeader = "===== Sat-Trak =====";
        mainMenuList = new Vector<>(6);
        mainMenuList.add("Get Passes");
        mainMenuList.add("Set Location");
        mainMenuList.add("Set Satellite");
        mainMenuList.add("Set Duration");
        mainMenuList.add("Get Details");
        mainMenuList.add("Exit");
        locationMenuHeader = "===== Set Location =====";
        locationMenuList = new Vector<>(3);
        locationMenuList.add("Use Address");
        locationMenuList.add("Add Manually");
        locationMenuList.add("Go Back");
        satelliteMenuHeader = "===== Set Satellite =====";
        satelliteMenuList = new Vector<>(3);
        satelliteMenuList.add("Use Celestrak");
        satelliteMenuList.add("Import Manually");
        satelliteMenuList.add("Go Back");
    }

    static int displayMenu(String header, java.util.Vector<String> menuList) {
        StringBuilder strDisplay = new StringBuilder(new String(header)).append("\n");
        for (int i = 1; i <= menuList.size(); i++) {
            strDisplay.append(i).append(". ").append(menuList.get(i - 1)).append("\n");
        }
        System.out.print(strDisplay + "Input>");
        int input;
        if (scanner.hasNextInt()) input = scanner.nextInt();
        else {
            scanner.next();
            System.out.println("Invalid input, please enter a valid number.");
            return displayMenu(header, menuList);
        }
        if (input < 1 || input > menuList.size()){
            System.out.println("Input out of bounds, try again.");
            return displayMenu(header, menuList);
        }
        else return input;
    }

}