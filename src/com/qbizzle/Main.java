package com.qbizzle;

import com.qbizzle.orbit.TLE;
import com.qbizzle.tracking.Coordinates;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main {

    Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException, InterruptedException, ParseException {
        Coordinates hutch = new Coordinates(38.060017, -97.930495);
//        JD viewTime = new JD(2, 27, 2022, 10, 43, 0);

//        SatellitePass satPass = Tracker.getPassInfo(tleZarya, viewTime, hutch);
//        System.out.println(satPass);

//        String geoJSON = getGeoPositionJSON("Hutchinson, KS");
//        Coordinates geoPosition = getGeoPositionFromJSOn(geoJSON);
//        String elevationJSON = getElevationJSON(geoPosition);
//        double elevation = getElevationFromJSON(elevationJSON);
//        System.out.println(geoPosition);
//        System.out.println(elevation);

        TLE tle = getTLE("ISS (ZARYA)");
        System.out.println(tle);


//        TLE tle = new TLE(response.body());
//
//        JD startTime = new JD(2, 28, 2022, 0, 0, 0, -6);
//        java.util.Vector<SatellitePass> passList = Tracker.getPasses(tle, startTime, startTime.Future(2.0), hutch);
//        System.out.println(passList.size());
//        for (SatellitePass pass :
//                passList) {
//            System.out.println(pass);
//        }

    }

    static String getElevationJSON(Coordinates geoPos) throws IOException, InterruptedException {
        final String URL = "http://open.mapquestapi.com/elevation/v1/profile?key=KEY&shapeFormat=raw&latLngCollection=";
        final String key = "vJ1uvrztzo5ctdTt7CgMAjFfZpWjtNxa";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(
                        URL.replace("KEY", key) + geoPos.getLatitude() + "," + geoPos.getLongitude())
                ).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    // assuming a json of the format from open.mapquest
    static double getElevationFromJSON(String jsonString) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(jsonString);
        JSONObject info = (JSONObject) json.get("info");
        JSONObject elevationProfile = ((JSONObject) ((JSONArray)json.get("elevationProfile")).get(0));
        return ((Number) elevationProfile.get("height")).doubleValue();
    }

    static String getGeoPositionJSON(String location) throws IOException, InterruptedException {
        final String URL = "http://open.mapquestapi.com/geocoding/v1/address?key=KEY&location=";
        final String key = "vJ1uvrztzo5ctdTt7CgMAjFfZpWjtNxa";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(
                        URL.replace("KEY", key) + location.replace(" ", "%20"))
                ).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    static Coordinates getGeoPositionFromJSOn(String jsonString) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(jsonString);
        JSONObject resultsObj = (JSONObject)((JSONArray) json.get("results")).get(0);
        JSONObject locationsObj = (JSONObject)((JSONArray) resultsObj.get("locations")).get(0);
        JSONObject latlng = (JSONObject) locationsObj.get("latLng");
        return  new Coordinates(
                ((Number) latlng.get("lat")).doubleValue(),
                ((Number) latlng.get("lng")).doubleValue()
        );
    }

//    right now this needs to be exact
    static TLE getTLE(String satName) throws IOException, InterruptedException {
        final String URL = "https://celestrak.com/NORAD/elements/gp.php?NAME=VALUE&FORMAT=TLE";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(
                        URL.replace("VALUE", satName.replace(" ", "%20")))
                ).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return new TLE(response.body());
    }

//    static java.util.Vector<SatellitePass> getPasses2(TLE tle, JD startTime, JD endTime, Coordinates geoPos) {
//        final double dt = 10 / 86400.0; // 10 seconds
//        java.util.Vector<SatellitePass> passList = new java.util.Vector<>();
//        JD currentTime = startTime;
//        SatellitePass satPass;
//        do {
//            try {
//                satPass = Tracker.getPassInfo(tle, currentTime, geoPos);
//                passList.add(satPass);
//                // todo: because the next guess is programed to guess 10 minutes into the past, it will find this same pass over and over, unless we jump more than 10 minutes into the future
//                currentTime = satPass.getSetTime().Future(11/1440.0);
//            } catch (Exception e) {
//                //todo: add a step method to move current JD forward or back in time
//                currentTime = currentTime.Future(dt);
//            }
//            //todo: add comparison methods for JD
//        } while(currentTime.Value() < endTime.Value());
//        return passList;
//    }


}