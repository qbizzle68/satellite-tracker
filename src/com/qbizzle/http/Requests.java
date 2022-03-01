package com.qbizzle.http;

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
import java.util.Vector;

public class Requests {
    static private final String OPEN_MAPQUEST_ELEVATION_URL =
            "http://open.mapquestapi.com/elevation/v1/profile?key=KEY&shapeFormat=raw&latLngCollection=";
    static private final String OPEN_MAPQUEST_GPS_URL =
            "http://open.mapquestapi.com/geocoding/v1/address?key=KEY&location=";
    static private final String OPEN_MAPQUEST_KEY = "vJ1uvrztzo5ctdTt7CgMAjFfZpWjtNxa";
    static private final String CELESTRAK_GP_URL =
            "https://celestrak.com/NORAD/elements/gp.php?QUERY=VALUE&FORMAT=TLE";

    public static double getElevation(Coordinates geoPos) throws IOException, InterruptedException, ParseException {
        return getElevationFromJSON(
                getElevationJSON(geoPos)
        );
    }

    public static Coordinates getGeoPosition(String location) throws IOException, InterruptedException, ParseException {
        return getGeoPositionFromJSON(
                getGeoPositionJSON(location)
        );
    }

    public static String getElevationJSON(Coordinates geoPos) throws IOException, InterruptedException {
        String URL = new String(OPEN_MAPQUEST_ELEVATION_URL)
                .replace("KEY", OPEN_MAPQUEST_KEY) +
                geoPos.getLatitude() + "," + geoPos.getLongitude();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(URL))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static double getElevationFromJSON(String jsonString) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(jsonString);
        JSONObject elevationProfile = ((JSONObject) ((JSONArray)json.get("elevationProfile")).get(0));
        return ((Number) elevationProfile.get("height")).doubleValue();
    }

    public static String getGeoPositionJSON(String location) throws IOException, InterruptedException {
        String URL = new String(OPEN_MAPQUEST_GPS_URL)
                .replace("KEY", OPEN_MAPQUEST_KEY)
                + location.replace(" ", "%20");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(URL))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static Coordinates getGeoPositionFromJSON(String jsonString) throws ParseException {
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

    public static java.util.Vector<TLE> getTLEList(String satName) throws IOException, InterruptedException {
        String URL = new String(CELESTRAK_GP_URL)
                .replace("QUERY", "NAME")
                .replace("VALUE", satName.replace(" ", "%20"));
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(URL))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        java.util.Vector<TLE> tleList = new Vector<>();
        if (response.body().equals("No GP data found")) return tleList; // maybe throw an exception
        String[] responseArray = response.body().split("\n");
        for (int i = 0; i < responseArray.length; i+=3) {
            TLE tle = new TLE(
                    responseArray[i] + "\n"
                    + responseArray[i+1] + "\n"
                    + responseArray[i+2]
            );
            tleList.add(tle);
        }
        return tleList;
    }

}